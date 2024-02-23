package reader

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import types.Just
import types.Maybe
import types.Nothing
import types.Reader

class ReaderMonadTest {

    private val list: MutableList<String> = mutableListOf()

    private fun write(s: String): Reader<Set<Permission>, Unit> =
        Reader { r ->
            if (r.contains(AllowWrite)) list.add(s)
        }

    private fun read(): Reader<Set<Permission>, Maybe<String>> =
        Reader { r ->
            if (r.contains(AllowRead)) Just(list.last()) else Nothing
        }

    sealed interface Permission
    data object AllowRead : Permission
    data object AllowWrite : Permission

    @AfterEach
    fun cleanUp(): Unit = list.clear()

    @Test
    fun readsAndWrites() {
        val perms = setOf(AllowWrite, AllowRead)
        val reader = write("hello") bind { _ ->
            write("aboba") bind { _ ->
                read() bind { a ->
                    when (a) {
                        is Just -> Reader.wrap<Set<Permission>, Boolean>(a.value == "aboba")
                        is Nothing -> Reader.wrap(false)
                    }
                }
            }
        }
        val result = reader.runReader(perms)
        Assertions.assertTrue(result)
    }

    @Test
    fun failsWithoutReadPermission() {
        val perms = setOf(AllowWrite)
        val reader = read()
        val result = reader.runReader(perms)
        Assertions.assertTrue(result is Nothing)
    }

    @Test
    fun failsWithoutWritePermission() {
        val perms = setOf(AllowRead)
        val reader = write("why") bind { _ ->
            write("where") bind { _ ->
                write("shishka")
            }
        }
        reader.runReader(perms)
        Assertions.assertEquals(0, list.size)
    }
}