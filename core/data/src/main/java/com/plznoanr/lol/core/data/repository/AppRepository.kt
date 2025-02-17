package com.plznoanr.lol.core.data.repository

import com.plznoanr.lol.core.common.di.AppDispatcher
import com.plznoanr.lol.core.common.di.Dispatcher
import com.plznoanr.lol.core.common.model.Result
import com.plznoanr.lol.core.data.utils.JsonParser
import com.plznoanr.lol.core.data.utils.asEntity
import com.plznoanr.lol.core.database.data.app.AppLocalDataSource
import com.plznoanr.lol.core.datastore.SettingPreferenceDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

interface AppRepository {

    fun getApiKey(): Flow<String>

    suspend fun insertApiKey(key: String)

    suspend fun deleteApiKey()

    suspend fun initializeJsonData(): Result<Boolean>

}

class DefaultAppRepository @Inject constructor(
    private val appLocalDataSource: AppLocalDataSource,
    private val settingPreferenceDataSource: SettingPreferenceDataSource,
    private val jsonParser: JsonParser,
    @Dispatcher(AppDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) : AppRepository {

    override fun getApiKey(): Flow<String> = settingPreferenceDataSource.apiKeyFlow

    override suspend fun insertApiKey(key: String) {
        settingPreferenceDataSource.updateApiKey(key)
    }

    override suspend fun deleteApiKey() {
        settingPreferenceDataSource.clearApiKey()
    }

    private suspend fun isLocalInitialize() = settingPreferenceDataSource.initFlow.first() ?: false

    private suspend fun getJson() = requireNotNull(jsonParser.getLocalJson()) {
        throw Exception()
    }

    override suspend fun initializeJsonData(): Result<Boolean> = runCatching {
        withContext(ioDispatcher) {
            val init = isLocalInitialize()
            Timber.d("isInit: $init")
            if (!init) {
                val json = getJson()
                launch {
                    json.map.data.values.forEach {
                        appLocalDataSource.insertMap(it.asEntity())
                    }
                }
                launch {
                    json.champ.data.values.forEach {
                        appLocalDataSource.insertChamp(it.asEntity())
                    }
                }
                launch {
                    json.rune.forEach {
                        appLocalDataSource.insertRune(it.asEntity())
                    }
                }
                launch {
                    json.summoner.data.values.forEach {
                        appLocalDataSource.insertSpell(it.asEntity())
                    }
                }
                launch { settingPreferenceDataSource.updateInit(true) }
                Result.Success(true)
            } else {
                Result.Success(false)
            }
        }
    }.getOrElse { Result.NoJsonDataError }

}