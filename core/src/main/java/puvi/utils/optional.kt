package puvi.utils

sealed class Optional<out T>

data class Some<out T>(val value: T) : Optional<T>() {
    operator fun invoke() = value
}

object None : Optional<Nothing>()

fun <T> T?.toOptional() = when (this) {
    null -> None
    else -> Some(this)
}

fun <T> Optional<T>.toNullable(): T? =
    when (this) {
        is Some<T> -> value
        is None -> null
    }

fun <T, R> Optional<T>.map(f: (T) -> R): Optional<R> =
    when (this) {
        is Some -> Some(f(value))
        is None -> None
    }

fun <T, R> Optional<T>.flatMap(f: (T) -> Optional<R>): Optional<R> =
    when (this) {
        is Some -> f(value)
        is None -> None
    }
