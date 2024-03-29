package com.plznoanr.lol.feature.setting

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.plznoanr.lol.core.model.Profile
import com.plznoanr.lol.core.mvibase.MviEvent
import com.plznoanr.lol.core.mvibase.MviSideEffect
import com.plznoanr.lol.core.mvibase.MviState

@Stable
data class UiState(
    val profile: Profile? = null,
    val apiKey: String = "",
    val isDarkTheme: Boolean = false
): MviState

@Immutable
sealed interface Event: MviEvent {
    data class OnThemeChange(val isDarkTheme: Boolean): Event
    data class OnKeyChange(val key: String): Event
}

sealed interface SideEffect: MviSideEffect

data class OnShowSnackbar(val message: String): SideEffect