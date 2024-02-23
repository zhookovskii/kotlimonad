package types

import typeclasses.Applicative
import typeclasses.Monad

/**
 * implementation of Maybe type from Haskell
 */
sealed class Maybe<out A>: Monad<A> {

    companion object {
        fun <A> pure(a: A): Maybe<A> = Just(a)

        fun <A> wrap(a: A): Maybe<A> = pure(a)
    }

    override fun <B> fmap(f: (A) -> B): Maybe<B> {
        return when (this) {
            is Just    -> Just(f(value))
            is Nothing -> Nothing
        }
    }

    override fun <B> ast(f: Applicative<(A) -> B>): Maybe<B> {
        return when (this) {
            is Just -> when (val mf = f as Maybe<(A) -> B>) {
                is Just -> Just(mf.value(value))
                is Nothing -> Nothing
            }
            is Nothing -> Nothing
        }
    }

    override fun <B> bind(f: (A) -> Monad<B>): Maybe<B> {
        return when (this) {
            is Just    -> f(value) as Maybe<B>
            is Nothing -> Nothing
        }
    }
}

data class Just<out A>(val value: A): Maybe<A>()
data object Nothing: Maybe<kotlin.Nothing>()
