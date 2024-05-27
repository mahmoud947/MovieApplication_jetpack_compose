package com.example.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

abstract class BaseViewModel<Event : ViewEvent, UiState : ViewState> : ViewModel() {
    private val initialState: UiState by lazy { setInitialState() }
    abstract fun setInitialState(): UiState

    private val _viewState: MutableStateFlow<UiState> = MutableStateFlow(initialState)
    val viewState: StateFlow<UiState> = _viewState.asStateFlow()

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()
    private val _effect: Channel<ViewSideEffect> = Channel()
    val effect = _effect.receiveAsFlow()

    lateinit var lastEvent: Event private set


    private val _showErrorDialog = MutableStateFlow(false)
    val showErrorDialog: StateFlow<Boolean> = _showErrorDialog.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()

    fun showError(message: String) {
        _errorMessage.value = message
        _showErrorDialog.value = true
    }

    fun hideErrorDialog() {
        _showErrorDialog.value = false
    }

    // Defines a CoroutineExceptionHandler that handles uncaught exceptions in coroutines
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        viewModelScope.launch {
            handleCoroutineException(exception)
        }
    }

    // This scope includes the CoroutineExceptionHandler for use in launched coroutines
    protected val viewModelScopeWithHandler = viewModelScope + coroutineExceptionHandler

    init {
        subscribeToEvents()
    }

    private fun subscribeToEvents() {
        viewModelScope.launch {
            _event.collect { event ->
                lastEvent = event
                handleEvents(event)
            }
        }
    }

    protected abstract fun handleEvents(event: Event)

    fun setEvent(event: Event) {
        viewModelScope.launch { _event.emit(event) }
    }

    fun setState(reducer: UiState.() -> UiState) {
        _viewState.update(reducer)
    }


    suspend fun setEffect(builder: () -> ViewSideEffect) {
        val effectValue = builder()
        _effect.send(effectValue)
    }


    // Function to handle exceptions, can be overridden by child ViewModels
    protected open fun handleCoroutineException(exception: Throwable) {

    }

    fun launchCoroutine(
        dispatcher: CoroutineDispatcher = Dispatchers.Main,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit,
    ): Job {
        return viewModelScopeWithHandler.launch(
            context = dispatcher, // Use the specified dispatcher
            start = start,
            block = block,
        )
    }
    override fun onCleared() {
        super.onCleared()
    }



}
