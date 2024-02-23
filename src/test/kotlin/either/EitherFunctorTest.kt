package either

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import types.Left
import utils.eitherGet
import utils.validate

class EitherFunctorTest {

    @Test
    fun mapsRight() {
        val ei = eitherGet(0) fmap { it + 1 }
        validate(ei, 1)
    }

    @Test
    fun doesntMapLeft() {
        val ei = eitherGet(-1) fmap { it + 1 }
        Assertions.assertTrue(ei is Left)
    }

    @Test
    fun mapChain() {
        val ei = eitherGet(3) fmap { it + 1 } fmap { it * 2 } fmap { it % 3 }
        validate(ei, 2)
    }
}