package types

import typeclasses.Foldable
import typeclasses.Functor
import typeclasses.Monoid
import typeclasses.Semigroup

/**
 * implementation of List type from Haskell
 */
sealed class ADTList<out A>: Monoid<A>, Foldable<A>, Functor<A> {

    // list concatenation (++)
    operator fun plus(other: ADTList<@UnsafeVariance A>): ADTList<A> {
        return when (this) {
            is Nil -> other
            is Cons -> Cons(head, tail + other)
        }
    }

    infix fun filter(cond: (A) -> Boolean): ADTList<A> {
        return when (this) {
            is Nil -> Nil
            is Cons -> if (cond(head)) Cons(head, tail filter cond) else tail filter cond
        }
    }

    override fun <B> fmap(f: (A) -> B): ADTList<B> {
        return when (this) {
            is Nil -> Nil
            is Cons -> Cons(f(head), tail fmap f)
        }
    }

    companion object {
        val mempty = Nil
    }

    override fun diam(other: Semigroup<@UnsafeVariance A>): ADTList<A> = this + (other as ADTList<A>)

    override fun <B> foldr(z: B, f: (A, B) -> B): B {
        return when (this) {
            is Nil -> z
            is Cons -> f(head, tail.foldr(z, f))
        }
    }
}

data object Nil : ADTList<kotlin.Nothing>()
data class Cons<out A>(val head: A, val tail: ADTList<A>): ADTList<A>()

typealias HString = ADTList<Char>

fun HString.show(): String = foldr("", Char::plus)

fun <A> ADTList<A>.asList(): List<A> = foldr(listOf()) { a, acc -> listOf(a) + acc }

fun <A> List<A>.adtList(): ADTList<A> {
    return if (isEmpty()) {
        Nil
    } else {
        Cons(first(), drop(1).adtList())
    }
}

fun String.hString(): HString {
    return if (isEmpty()) {
        Nil
    } else {
        Cons(first(), drop(1).hString())
    }
}
