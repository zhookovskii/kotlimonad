package maybe

import utils.maybeGet
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import types.Nothing
import utils.validate

class MaybeFunctorTest {

    @Test
    fun mapsJust() {
        val mb = maybeGet(0) fmap { it + 1 }
        validate(mb, 1)
    }

    @Test
    fun doesntMapNothing() {
        val mb = maybeGet(-1) fmap { it + 1 }
        Assertions.assertTrue(mb is Nothing)
    }

    @Test
    fun mapChain() {
        val mb = maybeGet(3) fmap { it + 1 } fmap { it * 2 } fmap { it % 3 }
        validate(mb, 2)
    }
}