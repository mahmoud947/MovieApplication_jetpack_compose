package com.example.core.base

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

interface ViewSideEffect

data class ErrorMessageSideEffect(val message: String?) : ViewSideEffect
data class ValidationErrorSideEffect(@StringRes val message: Int) : ViewSideEffect
object UnAuthorizedSideEffect : ViewSideEffect
object ConnectionMissedSideEffect : ViewSideEffect

object PopBackStack : ViewSideEffect
data object Idle : ViewSideEffect

@Composable
fun Flow<ViewSideEffect>.OnEffect(action: (effect: ViewSideEffect) -> Unit) {
    LaunchedEffect(Unit) {
        onEach { effect ->
            action(effect)
        }.collect()
    }
}