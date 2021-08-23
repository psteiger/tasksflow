@PublishedApi
internal class TasksScopeImpl : TasksScope {
    private val tasks = mutableListOf<suspend () -> Any?>()

    override fun task(block: suspend () -> Any?) {
        tasks.add(block)
    }

    fun build() = tasks.toList()
}
