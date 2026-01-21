package dev.elysium.eraces.bootstrap

class InitReport {
    private val results = mutableListOf<InitResult>()

    fun add(result: InitResult) {
        results += result
    }

    fun all(): List<InitResult> = results

    fun errors() = results.count { it.status == InitStatus.ERROR }
    fun warnings() = results.count { it.status == InitStatus.WARNING }
    fun success() = results.count { it.status == InitStatus.OK }
}