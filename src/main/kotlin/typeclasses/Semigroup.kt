package typeclasses

/**
 * implementation of Semigroup type class from Haskell
 */
interface Semigroup<out A> {
    /**
     * (<>) :: a -> a -> a
     *
     * semigroup associative operator
     *
     * (a <> b) <> c = a <> (b <> c)
     *
     * uses `this` as an implicit first argument (a)
     *
     * @param other semigroup
     * @return a - resulting semigroup
     */
    infix fun diam(other: Semigroup<@UnsafeVariance A>): Semigroup<A>
}