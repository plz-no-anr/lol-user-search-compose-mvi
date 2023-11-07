package com.plznoanr.lol.feature.home

import androidx.lifecycle.viewModelScope
import com.plznoanr.lol.core.common.model.Paging
import com.plznoanr.lol.core.domain.usecase.key.DeleteKeyUseCase
import com.plznoanr.lol.core.domain.usecase.key.GetKeyUseCase
import com.plznoanr.lol.core.domain.usecase.key.SaveKeyUseCase
import com.plznoanr.lol.core.domain.usecase.profile.GetProfileUseCase
import com.plznoanr.lol.core.domain.usecase.profile.SaveProfileUseCase
import com.plznoanr.lol.core.domain.usecase.summoner.DeleteAllSummonerUseCase
import com.plznoanr.lol.core.domain.usecase.summoner.DeleteSummonerUseCase
import com.plznoanr.lol.core.domain.usecase.summoner.GetSyncSummonerListUseCase
import com.plznoanr.lol.core.model.Profile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import plznoanr.coma.core.ComaViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val deleteAllSummonerUseCase: DeleteAllSummonerUseCase,
    private val deleteSummonerUseCase: DeleteSummonerUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val saveProfileUseCase: SaveProfileUseCase,
    private val getKeyUseCase: GetKeyUseCase,
    private val saveKeyUseCase: SaveKeyUseCase,
    private val deleteKeyUseCase: DeleteKeyUseCase,
    private val getSyncSummonerListUseCase: GetSyncSummonerListUseCase
) : ComaViewModel<HomeUiState, HomeIntent, HomeSideEffect>() {

    private var paging = Paging()

    override fun setInitialState(): HomeUiState = HomeUiState()

    override fun handleIntents(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.OnLoad -> onLoad()
            is HomeIntent.OnSearch -> postSideEffect { HomeSideEffect.Navigation.ToSearch }
            is HomeIntent.Refresh -> refreshSummonerList()
            is HomeIntent.Summoner.OnDeleteAll -> deleteAllSummoner()
            is HomeIntent.Summoner.OnDelete -> deleteSummoner(intent.name)
            is HomeIntent.Profile.OnAdd -> addProfile(intent.profile)
            is HomeIntent.Key.OnGet -> postSideEffect { HomeSideEffect.MoveGetApiKey }
            is HomeIntent.Key.OnAdd -> insertKey(intent.key)
            is HomeIntent.Key.OnDelete -> deleteKey()
            is HomeIntent.Spectator.OnWatch -> postSideEffect {
                HomeSideEffect.Navigation.ToSpectator(
                    intent.summonerId
                )
            }
        }
    }

    private fun onLoad() {
        combine(
            getKeyUseCase(),
            getProfileUseCase(),
            getSyncSummonerListUseCase(paging)
        ) { key, profile, summoners ->
            reduce {
                copy(
                    isLoading = false,
                    key = key,
                    profile = profile,
                    data = summoners
                )
            }
        }.onStart {
            reduce { copy(isLoading = true) }
        }.launchIn(viewModelScope)
    }

    private fun refreshSummonerList() {
//        viewModelScope.launch {
//            readSummonerListUseCase(paging)
//                .onStart { reduce { copy(isRefreshing = true) } }
//                .onEach { result ->
//                    result.onSuccess {
//                        reduce {
//                            copy(
//                                data = it.asReversed(),
//                                isRefreshing = false
//                            )
//                        }
//                    }.onFailure {
//                        reduce {
//                            copy(
//                                isRefreshing = false
//                            )
//                        }
//                        postSideEffect {
//                            HomeSideEffect.Toast(it.message ?: "Error")
//                        }
//                    }
//                }.first()
//        }

    }

    private fun deleteAllSummoner() {
//        deleteAllSummonerUseCase(Unit)
//            .onStart { reduce { copy(isLoading = true) } }
//            .onEach { result ->
//                result.onSuccess {
//                    reduce {
//                        copy(
//                            data = emptyList(),
//                            isLoading = false
//                        )
//                    }
//                }.onFailure {
//                    reduce {
//                        copy(
//                            error = it.message,
//                            isLoading = false
//                        )
//                    }
//                }
//            }.launchIn(viewModelScope)
    }

    private fun deleteSummoner(name: String) {
//        deleteSummonerUseCase(name)
//            .onStart { reduce { copy(isLoading = true) } }
//            .onEach { result ->
//                result.onSuccess {
//                    reduce {
//                        copy(
//                            data = data?.filter { it.name != name }?.asReversed(),
//                            isLoading = false
//                        )
//                    }
//                }.onFailure {
//                    reduce {
//                        copy(
//                            error = it.message,
//                            isLoading = false
//                        )
//                    }
//                }
//            }.launchIn(viewModelScope)
    }

    private fun addProfile(profile: Profile) {
//        insertProfileUseCase(profile)
//            .onStart { reduce { copy(isLoading = true) } }
//            .onEach { result ->
//                result.onSuccess {
//                    reduce {
//                        copy(
//                            profile = profile,
//                            isLoading = false
//                        )
//                    }
//                }.onFailure {
//                    reduce {
//                        copy(
//                            error = it.message,
//                            isLoading = false
//                        )
//                    }
//                }
//            }.launchIn(viewModelScope)
    }

    private fun insertKey(key: String) {
//        viewModelScope.launch {
//            insertKeyUseCase(key)
//                .onStart { reduce { copy(isLoading = true) } }
//                .onEach { result ->
//                    result.onSuccess {
//                        reduce {
//                            copy(
//                                key = key,
//                                isLoading = false
//                            )
//                        }
//                    }.onFailure {
//                        reduce {
//                            copy(
//                                error = it.message,
//                                isLoading = false
//                            )
//                        }
//                    }
//                }.first()
//        }
    }

    private fun deleteKey() {
//        deleteKeyUseCase()
//            .onStart { reduce { copy(isLoading = true) } }
//            .onEach { result ->
//                result.onSuccess {
//                    reduce {
//                        copy(
//                            key = null,
//                            isLoading = false
//                        )
//                    }
//                }.onFailure {
//                    reduce {
//                        copy(
//                            error = it.message,
//                            isLoading = false
//                        )
//                    }
//                }
//            }.launchIn(viewModelScope)
    }

}