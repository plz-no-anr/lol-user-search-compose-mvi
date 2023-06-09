package com.plz.no.anr.lol.data.utils

import android.content.Context
import com.plz.no.anr.lol.data.di.CoroutineQualifiers
import com.plz.no.anr.lol.domain.model.common.LocalJson
import com.plz.no.anr.lol.domain.model.common.json.ChampionJson
import com.plz.no.anr.lol.domain.model.common.json.MapJson
import com.plz.no.anr.lol.domain.model.common.json.RuneJson
import com.plz.no.anr.lol.domain.model.common.json.SummonerJson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import timber.log.Timber

class JsonUtils(
    private val context: Context,
    @CoroutineQualifiers.DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) {
    suspend fun getLocalJson(): LocalJson? = withContext(defaultDispatcher) {
        try {
            val json = Json { ignoreUnknownKeys = true }
            val mapString = context.assets.open(JsonFileName.MAP).reader().readText()
            val champString = context.assets.open(JsonFileName.CHAMPION).reader().readText()
            val runeString = context.assets.open(JsonFileName.RUNE).reader().readText()
            val jsonString = context.assets.open(JsonFileName.SUMMONER).reader().readText()

            val mapJson = json.decodeFromString<MapJson>(mapString)
            val champJson = json.decodeFromString<ChampionJson>(champString)
            val runeJson = json.decodeFromString<List<RuneJson>>(runeString)
            val summonerJson = json.decodeFromString<SummonerJson>(jsonString)

            return@withContext LocalJson(champJson, mapJson, runeJson, summonerJson)
        } catch (e: Exception) {
            Timber.e(e)
            return@withContext null
        }
    }

}