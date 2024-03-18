package com.plznoanr.lol.feature.home

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.plznoanr.lol.core.common.model.parseError
import com.plznoanr.lol.core.designsystem.component.AppProgressBar
import com.plznoanr.lol.core.designsystem.component.OnBottomReached
import com.plznoanr.lol.core.designsystem.component.error.ErrorScreen
import com.plznoanr.lol.core.model.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    navCallbackFlow: Flow<Boolean>
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val eventChannel = remember { Channel<Event>(Channel.UNLIMITED) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.Main.immediate) {
            eventChannel
                .consumeAsFlow()
                .onEach(viewModel::onEvent)
                .collect()
        }
    }

    val onEvent = remember {
        { event: Event ->
            eventChannel.trySend(event).getOrThrow()
        }
    }

    val lazyListState = rememberLazyListState().apply {
        OnBottomReached {
            if (state.summonerList.size >= 20) {
                onEvent(OnNextPage)
            }
        }
    }

    LaunchedEffect(Unit) {
        navCallbackFlow
            .onEach {
                lazyListState.animateScrollToItem(0)
            }.collect()
    }


    HomeScreen(
        state = state,
        onEvent = onEvent,
        sideEffectFlow = viewModel.sideEffectFlow,
        lazyListState = lazyListState
    )
}

@Composable
internal fun HomeScreen(
    state: UiState,
    onEvent: (Event) -> Unit,
    sideEffectFlow: Flow<SideEffect>,
    lazyListState: LazyListState
) {
    LaunchedEffect(Unit) {
        sideEffectFlow.onEach { sideEffect ->
            when (sideEffect) {
                else -> Unit
            }
        }.collect()
    }
    when {
        state.isLoading -> AppProgressBar()
        state.error != null -> ErrorScreen(
            error = state.error.parseError()
        ) { onEvent(OnInit) }

        else -> {
            HomeContent(
                data = state.summonerList,
                isRefreshing = state.isRefreshing,
                isLoadNextPage = state.isLoadNextPage,
                lazyListState = lazyListState,
                onRefresh = { onEvent(OnRefresh) },
                onBookmarked = { onEvent(OnBookmark(it)) },
                onDeleteAll = { onEvent(OnDeleteAll) },
            )
        }
    }
}


@Preview
@Composable
private fun HomeScreenPreview() {
//    HomeScreen(
//        state = UiState(),
//        onEvent = {},
//    )
}

private fun getDummyProfile() = Profile(
    id = "id",
    nickname = "name",
    icon = "http://ddragon.leagueoflegends.com/cdn/13.6.1/img/profileicon/6.png",
    level = "100"
)
