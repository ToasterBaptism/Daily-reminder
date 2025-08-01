package com.dailyreminder.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dailyreminder.data.local.repository.PlannerRepository
import com.dailyreminder.data.model.Task
import com.dailyreminder.data.model.TaskCategory
import com.dailyreminder.data.model.Priority
import com.dailyreminder.domain.usecase.task.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: PlannerRepository,
    private val getTasksUseCase: GetTasksUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val completeTaskUseCase: CompleteTaskUseCase
) : ViewModel() {
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _filterCategory = MutableStateFlow<TaskCategory?>(null)
    val filterCategory: StateFlow<TaskCategory?> = _filterCategory.asStateFlow()
    
    private val _filterPriority = MutableStateFlow<Priority?>(null)
    val filterPriority: StateFlow<Priority?> = _filterPriority.asStateFlow()
    
    private val _showCompleted = MutableStateFlow(false)
    val showCompleted: StateFlow<Boolean> = _showCompleted.asStateFlow()
    
    private val _sortBy = MutableStateFlow(TaskSortBy.DUE_DATE)
    val sortBy: StateFlow<TaskSortBy> = _sortBy.asStateFlow()
    
    val allTasks = getTasksUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    val filteredTasks = combine(
        allTasks,
        filterCategory,
        filterPriority,
        showCompleted,
        sortBy
    ) { tasks, category, priority, completed, sort ->
        tasks
            .filter { task ->
                (category == null || task.category == category) &&
                (priority == null || task.priority == priority) &&
                (completed || !task.isCompleted)
            }
            .sortedWith(getSortComparator(sort))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    
    val taskStats = allTasks.map { tasks ->
        TaskStats(
            total = tasks.size,
            completed = tasks.count { it.isCompleted },
            overdue = tasks.count { task ->
                !task.isCompleted && 
                task.dueDateTime != null && 
                task.dueDateTime < kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault())
            },
            highPriority = tasks.count { it.priority == Priority.HIGH || it.priority == Priority.URGENT }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TaskStats()
    )
    
    fun setFilterCategory(category: TaskCategory?) {
        _filterCategory.value = category
    }
    
    fun setFilterPriority(priority: Priority?) {
        _filterPriority.value = priority
    }
    
    fun setShowCompleted(show: Boolean) {
        _showCompleted.value = show
    }
    
    fun setSortBy(sortBy: TaskSortBy) {
        _sortBy.value = sortBy
    }
    
    fun toggleTaskCompletion(taskId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                completeTaskUseCase(taskId)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to update task"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun createTask(
        title: String,
        description: String = "",
        category: TaskCategory = TaskCategory.PERSONAL,
        priority: Priority = Priority.MEDIUM,
        dueDateTime: LocalDateTime? = null,
        estimatedDurationMinutes: Int = 0,
        tags: List<String> = emptyList(),
        notificationEnabled: Boolean = true
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val task = Task(
                    id = 0, // Will be auto-generated
                    title = title,
                    description = description,
                    category = category,
                    priority = priority,
                    dueDateTime = dueDateTime,
                    estimatedDurationMinutes = estimatedDurationMinutes,
                    progress = 0,
                    isCompleted = false,
                    completedDateTime = null,
                    tags = tags,
                    subtasks = emptyList(),
                    notificationEnabled = notificationEnabled,
                    createdDateTime = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()),
                    updatedDateTime = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault())
                )
                createTaskUseCase(task)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to create task"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun clearError() {
        _error.value = null
    }
    
    private fun getSortComparator(sortBy: TaskSortBy): Comparator<Task> {
        return when (sortBy) {
            TaskSortBy.DUE_DATE -> compareBy<Task> { it.dueDateTime }
            TaskSortBy.PRIORITY -> compareByDescending<Task> { it.priority.ordinal }
            TaskSortBy.CREATED_DATE -> compareByDescending<Task> { it.createdDateTime }
            TaskSortBy.TITLE -> compareBy<Task> { it.title.lowercase() }
            TaskSortBy.CATEGORY -> compareBy<Task> { it.category.name }
        }
    }
}

enum class TaskSortBy {
    DUE_DATE,
    PRIORITY,
    CREATED_DATE,
    TITLE,
    CATEGORY
}

data class TaskStats(
    val total: Int = 0,
    val completed: Int = 0,
    val overdue: Int = 0,
    val highPriority: Int = 0
) {
    val completionRate: Float = if (total > 0) completed.toFloat() / total else 0f
}