package com.freelapp.tasksflow

@PublishedApi
internal class TasksScopeImpl : TasksScope {
    private val tasks = mutableListOf<suspend TaskScope.() -> Unit>()

    override suspend fun task(block: suspend TaskScope.() -> Unit) {
        tasks.add(block)
    }

    fun build(): List<suspend TaskScope.() -> Unit> =
        tasks.toList()
}
