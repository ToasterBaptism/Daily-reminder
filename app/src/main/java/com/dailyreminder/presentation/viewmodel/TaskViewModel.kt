package com.dailyreminder.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dailyreminder.data.local.repository.PlannerRepository
import com.dailyreminder.data.model.Task
import com.dailyreminder.data.util.DateUtils
import com.dailyreminder.domain.usecase.task.CompleteTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: PlannerRepository,
    private val completeTaskUseCase: CompleteTaskUseCase
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    val uiState: StateFlow<TaskUiState> = tasks.map { tasks ->
        TaskUiState(
            tasks = tasks.sortedWith(
                compareBy<Task> { it.isCompleted }
                    .thenBy { it.dueDateTime }
                    .thenBy { it.priority.ordinal }
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TaskUiState()
    )

    init {
        loadTasks()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            repository.getAllTasks().collect { taskList ->
                _tasks.value = taskList
            }
        }
    }

    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            completeTaskUseCase(task, !task.isCompleted)
        }
    }

    fun refreshTasks() {
        loadTasks()
    }
}

data class TaskUiState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)