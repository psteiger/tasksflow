package com.freelapp.tasksflow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlin.math.min

fun tasksFlow(init: suspend TasksScope.() -> Unit): Flow<Int> =
    flow {
        val scope = TasksScopeImpl()
        scope.init()
        runTasks(scope.build())
    }

suspend inline fun FlowCollector<Int>.runTasks(block: TasksScope.() -> Unit) {
    runTasks(tasks(block))
}

suspend inline fun FlowCollector<Int>.runTasks(tasks: List<suspend TaskScope.() -> Unit>) {
    val unit: Double = 100.0 / tasks.size
    tasks.forEachIndexed { index, task ->
        TaskScope.task()
        val progress = min(((index+1) * unit).toInt(), 100)
        emit(progress)
    }
}

inline fun tasks(block: TasksScope.() -> Unit): List<suspend TaskScope.() -> Unit> =
    TasksScopeImpl().apply(block).build()

