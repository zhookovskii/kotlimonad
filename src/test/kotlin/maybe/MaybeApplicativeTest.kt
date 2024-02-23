package maybe

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import types.Maybe
import types.Nothing
import utils.maybeApplication
import utils.validate

class MaybeApplicativeTest {

    @Test
    fun appliesLegalFunction() {
        val arg: Collection<*> = setOf(1)
        val mb = Maybe.pure(arg)
        val result = mb ast maybeApplication(1)
        validate(result, true)
    }

    @Test
    fun failsOnIllegalFunction() {
        val arg: Collection<*> = listOf(2)
        val result = Maybe.pure(arg) ast maybeApplication(10) ast Maybe.pure { b -> b.not() }
        Assertions.assertTrue(result is Nothing)
    }

    @Test
    fun applicationChain() {
        val arg: Collection<*> = setOf("my ass", "great")
        val result = Maybe.pure(arg) ast
                maybeApplication(2) ast
                Maybe.pure { b -> b.not() } ast
                Maybe.pure { b -> setOf(b) } ast
                Maybe.pure { s -> "${s.single()}" }
        validate(result, "false")
    }
}
