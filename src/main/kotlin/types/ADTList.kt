package types

import typeclasses.Foldable
import typeclasses.Monoid
import typeclasses.Semigroup

/**
 * implementation of List type from Haskell
 */
sealed class ADTList<out A>: Monoid<A>, Foldable<A> {

    // list concatenation (++)
    operator fun plus(other: ADTList<@UnsafeVariance A>): ADTList<A> {
        return when (this) {
            is Nil -> other
            is Cons -> Cons(head, tail + other)
        }
    }

    companion object {
        val mempty = Nil
    }

    override fun diam(other: Semigroup<@UnsafeVariance A>): ADTList<A> = this + (other as ADTList<A>)

    override fun <B> foldr(f: (A, B) -> B, z: B): B {
        return when (this) {
            is Nil -> z
            is Cons -> f(head, tail.foldr(f, z))
        }
    }
}

data object Nil : ADTList<kotlin.Nothing>()
data class Cons<out A>(val head: A, val tail: ADTList<A>): ADTList<A>()

typealias HString = ADTList<Char>

fun HString.show(): String {
    return when (this) {
        is Nil -> ""
        is Cons -> head + tail.show()
    }
}

fun String.hString(): HString {
    return if (isEmpty()) {
        Nil
    } else {
        Cons(first(), drop(1).hString())
    }
}
