package dev.elysium.eraces.bootstrap

enum class InitStatus {
    OK,
    WARNING,
    ERROR
}

data class InitResult(
    val name: String,
    val status: InitStatus,
    val message: String? = null,
    val timeMs: Long
)