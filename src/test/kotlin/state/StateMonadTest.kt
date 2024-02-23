package state

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import types.State

class StateMonadTest {

    private fun <A> push(a: A): Stack<A, Unit> =
        State { s ->
            Unit to (s + a)
        }

    private fun <A> pop(): Stack<A, A> =
        State { s ->
            s.last() to s.dropLast(1)
        }

    @Test
    fun pushesToStack() {
        val makePush = push(1) bind { _ ->
            push(2) bind { _ ->
                push(3)
            }
        }
        val (_, result) = makePush.runState(listOf())
        Assertions.assertEquals(3, result.size)
        Assertions.assertEquals(listOf(1, 2, 3), result)
    }

    @Test
    fun popsFromStack() {
        val makePop: Stack<Int, Int> = pop<Int>() bind { _ ->
            pop<Int>() bind { _ ->
                pop()
            }
        }
        val (_, result) = makePop.runState(listOf(1, 2, 3, 4))
        Assertions.assertEquals(1, result.size)
        Assertions.assertEquals(listOf(1), result)
    }

    @Test
    fun pushesAndPops() {
        val stack: Stack<Int, Unit> = push(1) bind { _ ->
            push(2) bind {_ ->
                push(3) bind { _ ->
                    pop<Int>() bind { _ ->
                        push(4) bind { _ ->
                            pop<Int>() bind { x ->
                                push(x * 2)
                            }
                        }
                    }
                }
            }
        }
        val (_, result) = stack.runState(listOf())
        Assertions.assertEquals(3, result.size)
        Assertions.assertEquals(listOf(1, 2, 8), result)
    }
}

typealias Stack<A, B> = State<List<A>, B>