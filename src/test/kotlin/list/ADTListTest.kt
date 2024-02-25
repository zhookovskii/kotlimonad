package list

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import types.*

class ADTListTest {

    private fun range(a: Int, b: Int, step: Int = 1): ADTList<Int> =
        (b downTo a step step).fold(Nil) { acc: ADTList<Int>, n: Int -> Cons(n, acc) }

    @Test
    fun concatenatesLists() {
        val expected = range(1, 20)
        Assertions.assertEquals(
            expected,
            range(1, 10) + range(11, 20)
        )
        Assertions.assertEquals(
            expected,
            range(1, 10) diam range(11, 20)
        )
    }

    @Test
    fun concatenatesStrings() {
        Assertions.assertEquals(
            "big moma".hString(),
            "big".hString() + " ".hString() + "moma".hString()
        )
    }

    @Test
    fun mapsList() {
        Assertions.assertEquals(
            range(3, 12, 3),
            range(1, 4) fmap { n -> n * 3 }
        )
    }

    @Test
    fun mapsString() {
        Assertions.assertEquals(
            "ABOBA".hString(),
            "aboba".hString() fmap Char::uppercaseChar
        )
    }

    @Test
    fun foldsList() {
        Assertions.assertEquals(
            120,
            range(1, 5).foldr(1) { n, acc -> n * acc }
        )
    }

    @Test
    fun filtersList() {
        Assertions.assertEquals(
            listOf(3, 9, 12),
            (listOf(3, 5, 8, 9, 10, 12, 17).adtList() filter { n -> n % 3 == 0 }).asList()
        )
    }
}