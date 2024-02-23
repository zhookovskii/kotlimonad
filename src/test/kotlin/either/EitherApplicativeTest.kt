package either

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import types.*
import utils.eitherApplication
import utils.validate

class EitherApplicativeTest {

    @Test
    fun appliesLegalFunction() {
        val arg: Collection<*> = setOf(1)
        val ei: Either<String, Collection<*>> = Either.pure(arg)
        val result = ei ast eitherApplication(1)
        validate(result, true)
    }

    @Test
    fun failsOnIllegalFunction() {
        val arg: Collection<*> = listOf(2)
        val ei: Either<String, Collection<*>> = Either.pure(arg)
        val result = ei ast eitherApplication(10) ast Right { b: Boolean -> b.not() }
        Assertions.assertTrue(result is Left)
    }

    @Test
    fun applicationChain() {
        val arg: Collection<*> = setOf("my ass", "great")
        val ei: Either<String, Collection<*>> = Right(arg)
        val result = ei ast
                eitherApplication(2) ast
                Right { b -> b.not() } ast
                Right { b -> setOf(b) } ast
                Right { s -> "${s.single()}" }
        validate(result, "false")
    }
}