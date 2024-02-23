package types

import typeclasses.Applicative
import typeclasses.Monad
import typeclasses.Monoid

/**
 * implementation of Writer type from Haskell
 *
 * `wrap` and `pure` definitions are omitted as there seems to be
 * no way of accessing `mempty` from type parameter W
 */
@Suppress("UNCHECKED_CAST")
data class Writer<W : Monoid<*>, A>(
    val runWriter: () -> Pair<A, W>
): Monad<A> {

    override fun <B> fmap(f: (A) -> B): Writer<W, B> =
        Writer {
            val (a, w) = runWriter()
            f(a) to w
        }

    override fun <B> ast(f: Applicative<(A) -> B>): Writer<W, B> =
        Writer {
            val (runA, fw) = runWriter()
            val (runF, sw) = (f as Writer<W, (A) -> B>).runWriter()
            runF(runA) to ((fw diam sw) as W)
        }

    override fun <B> bind(f: (A) -> Monad<B>): Writer<W, B> =
        Writer {
            val (a, fw) = runWriter()
            val (b, sw) = (f(a) as Writer<W, B>).runWriter()
            b to ((fw diam sw) as W)
        }
}