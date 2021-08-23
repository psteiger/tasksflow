package com.freelapp.tasksflow

@DslMarker
internal annotation class TasksDSL

@TasksDSL
interface TasksScope {
    suspend fun task(block: suspend TaskScope.() -> Unit)
}

@TasksDSL
object TaskScope
