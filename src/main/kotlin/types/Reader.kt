package types

import typeclasses.Applicative
import typeclasses.Monad

/**
 * implementation of Reader type from Haskell
 */
@Suppress("UNCHECKED_CAST")
data class Reader<E, A>(
    val runReader: (E) -> A
): Monad<A> {

    companion object {
        fun <E, A> pure(a: A): Reader<E, A> = Reader { _ -> a }

        fun <E, A> wrap(a: A): Reader<E, A> = pure(a)
    }

    override fun <B> fmap(f: (A) -> B): Reader<E, B> =
        Reader { r ->
            f(runReader(r))
        }

    override fun <B> ast(f: Applicative<(A) -> B>): Reader<E, B> =
        Reader { r ->
            val ra = runReader(r)
            val rf = (f as Reader<E, (A) -> B>).runReader(r)
            rf(ra)
        }

    override fun <B> bind(f: (A) -> Monad<B>): Reader<E, B> =
        Reader { r ->
            val newReader = f(runReader(r)) as Reader<E, B>
            newReader.runReader(r)
        }
}