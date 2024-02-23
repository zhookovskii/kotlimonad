package utils

import org.junit.jupiter.api.Assertions
import types.*
import types.Nothing

// functor and applicative tests
val arr = Array(10) { it }

fun maybeGet(idx: Int): Maybe<Int> {
    return if (idx in arr.indices) {
        Just(arr[idx])
    } else {
        Nothing
    }
}

fun <T> validate(mb: Maybe<T>, expected: T) {
    Assertions.assertTrue(mb is Just)
    Assertions.assertEquals(expected, (mb as Just).value)
}

fun eitherGet(idx: Int): Either<String, Int> {
    return if (idx in arr.indices) {
        Right(arr[idx])
    } else {
        Left("out of bounds")
    }
}

fun <T> validate(ei: Either<String, T>, expected: T) {
    Assertions.assertTrue(ei is Right)
    Assertions.assertEquals(expected, (ei as Right).right)
}

// monad tests
@JvmInline
value class Password(val password: String)

data class User(val username: String, val password: Password, val age: Int)

sealed interface ValidationError
data object BadEmail : ValidationError
data object BadPassword : ValidationError
data object BadAge : ValidationError

fun maybeAge(age: Int): Maybe<Int> {
    return if (age >= 18) {
        Just(age)
    } else {
        Nothing
    }
}

fun maybeUsername(email: String): Maybe<String> {
    return if (email.contains("@")) {
        Just(email.split("@").first())
    } else {
        Nothing
    }
}

fun maybePassword(password: String): Maybe<Password> {
    return if (password.length < 10) {
        Just(Password(password))
    } else {
        Nothing
    }
}

fun eitherAge(age: Int): Either<ValidationError, Int> {
    return if (age >= 18) {
        Right(age)
    } else {
        Left(BadAge)
    }
}

fun eitherUsername(email: String): Either<ValidationError, String> {
    return if (email.contains("@")) {
        Right(email.split("@").first())
    } else {
        Left(BadEmail)
    }
}

fun eitherPassword(password: String): Either<ValidationError, Password> {
    return if (password.length < 10) {
        Right(Password(password))
    } else {
        Left(BadPassword)
    }
}

val functionArr = arrayOf(
    { c: Collection<*> -> c.isEmpty() },
    { c: Collection<*> -> c.size == 1 },
    { c: Collection<*> -> c.contains("my ass") }
)

fun maybeApplication(idx: Int): Maybe<(Collection<*>) -> Boolean> {
    return if (idx in functionArr.indices) {
        Just(functionArr[idx])
    } else {
        Nothing
    }
}

fun eitherApplication(idx: Int): Either<String, (Collection<*>) -> Boolean> {
    return if (idx in functionArr.indices) {
        Right(functionArr[idx])
    } else {
        Left("out of bounds")
    }
}