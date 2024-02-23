package writer

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import types.*

class WriterMonadTest {

    private fun timesTwo(a: Int): Writer<HString, Int> = Writer {
        a * 2 to "$a times two\n".hString()
    }

    private fun plusOne(a: Int): Writer<HString, Int> = Writer {
        a + 1 to "$a plus one\n".hString()
    }

    private fun fact(n: Int): Writer<HString, Int> {
        return if (n <= 1) {
            Writer { n to "$n! = 1\n".hString() }
        } else {
            fact(n - 1) bind { ns -> Writer { ns * n to "$n! = ${ns * n}\n".hString() } }
        }
    }

    @Test
    fun appendsLog() {
        val empty: HString = HString.mempty
        val writer: Writer<HString, Int> = Writer { 1 to empty } bind
                ::timesTwo bind
                ::plusOne bind
                ::plusOne
        val (a, log) = writer.runWriter()
        Assertions.assertEquals(4, a)
        Assertions.assertEquals(
            listOf(
                "1 times two",
                "2 plus one",
                "3 plus one"
            ).joinToString("\n") + "\n",
            log.show()
        )
    }

    @Test
    fun appendsRecursive() {
        val writer = fact(6)
        val (a, log) = writer.runWriter()
        Assertions.assertEquals(720, a)
        Assertions.assertEquals(
            listOf(
                "1! = 1",
                "2! = 2",
                "3! = 6",
                "4! = 24",
                "5! = 120",
                "6! = 720"
            ).joinToString("\n") + "\n",
            log.show()
        )
    }
}