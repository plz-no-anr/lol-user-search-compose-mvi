package com.plz.no.anr.lol.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plz.no.anr.lol.domain.usecase.json.InitialLocalJsonUseCase
import com.plz.no.anr.lol.utils.NetworkManager
import com.plz.no.anr.lol.utils.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

sealed class AppState {
    object Loading : AppState()

    data class Success(val data: Boolean) : AppState()

    data class Error(val throwable: Throwable) : AppState()
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkManager: NetworkManager,
    private val initialLocalJsonUseCase: InitialLocalJsonUseCase
) : ViewModel() {

    private val _appState: MutableStateFlow<AppState> = MutableStateFlow(AppState.Loading)

    init {
        Timber.w("$this created.")
        viewModelScope.launch {
            initLocalJson()
        }
    }

    private suspend fun initLocalJson() {
        withContext(Dispatchers.IO) {
            initialLocalJsonUseCase(Unit)
                .collect {
                    it.onSuccess { result ->
                        Timber.d("initialLocalJson result : $result")
                        _appState.emit(AppState.Success(result))
                    }.onFailure { t ->
                        Timber.d("Initial LocalJson fail")
                        _appState.emit(AppState.Error(t))
                    }
                }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        return networkManager.networkState.value is NetworkState.Connected
    }

    fun isAppInitialized(): Boolean = !(isNetworkAvailable() && _appState.value is AppState.Success)


}