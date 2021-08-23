package com.freelapp.tasksflow

@PublishedApi
internal class TasksScopeImpl : TasksScope {
    private val tasks = mutableListOf<suspend () -> Any?>()

    override fun task(block: suspend () -> Any?) {
        tasks.add(block)
    }

    fun build(): List<suspend () -> Any?> =
        tasks.toList()
}
