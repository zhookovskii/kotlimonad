package types

import typeclasses.Applicative
import typeclasses.Monad

/**
 * implementation of State type from Haskell
 */
@Suppress("UNCHECKED_CAST")
data class State<S, out A>(
    val runState: (S) -> Pair<A, S>
): Monad<A> {

    companion object {
        fun <S, A> pure(a: A): State<S, A> = State { s -> a to s }

        fun <S, A> wrap(a: A): State<S, A> = pure(a)
    }

    override fun <B> fmap(f: (A) -> B): State<S, B> =
        State { s ->
            val (a, newState) = runState(s)
            f(a) to newState
        }

    override fun <B> ast(f: Applicative<(A) -> B>): State<S, B> =
        State { s ->
            val (runA, firstS) = runState(s)
            val (runF, secondS) = (f as State<S, (A) -> B>).runState(firstS)
            runF(runA) to secondS
        }

    override fun <B> bind(f: (A) -> Monad<B>): State<S, B> =
        State { s ->
            val (a, newState) = runState(s)
            (f(a) as State<S, B>).runState(newState)
        }
}
