package types

import typeclasses.Applicative
import typeclasses.Monad

/**
 * implementation of Either type from Haskell
 */
@Suppress("UNCHECKED_CAST")
sealed class Either<out E, out A>: Monad<A> {

    companion object {
        fun <E, A> pure(a: A): Either<E, A> = Right(a)

        fun <E, A> wrap(a: A): Either<E, A> = pure(a)
    }

    override fun <B> fmap(f: (A) -> B): Either<E, B> {
        return when (this) {
            is Right -> Right(f(right))
            is Left -> Left(left)
        }
    }

    override fun <B> ast(f: Applicative<(A) -> B>): Either<E, B> {
        return when (this) {
            is Right -> when (val ef = f as Either<E, (A) -> B>) {
                is Right -> Right(ef.right(right))
                is Left -> ef
            }
            is Left -> this
        }
    }

    override fun <B> bind(f: (A) -> Monad<B>): Either<E, B> {
        return when (this) {
            is Left -> Left(left)
            is Right -> f(right) as Either<E, B>
        }
    }
}

data class Left<out E>(val left: E): Either<E, kotlin.Nothing>()
data class Right<out A>(val right: A): Either<kotlin.Nothing, A>()
