package com.freelapp.tasksflow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlin.math.min

interface TasksScope {
    fun task(block: suspend () -> Any?)
}

inline fun tasksFlow(crossinline init: suspend TasksScope.() -> Unit): Flow<Int> =
    flow {
        val scope = TasksScopeImpl()
        scope.init()
        runTasks(scope.build())
    }

suspend inline fun FlowCollector<Int>.runTasks(block: TasksScope.() -> Unit) {
    runTasks(tasks(block))
}

suspend inline fun FlowCollector<Int>.runTasks(tasks: List<suspend () -> Any?>) {
    val unit: Double = 100.0 / tasks.size
    tasks.forEachIndexed { index, task ->
        task()
        val progress = min(((index+1) * unit).toInt(), 100)
        emit(progress)
    }
}

inline fun tasks(block: TasksScope.() -> Unit): List<suspend () -> Any?> =
    TasksScopeImpl().apply(block).build()

