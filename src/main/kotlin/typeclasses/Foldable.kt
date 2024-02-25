package typeclasses

/**
 * implementation of Foldable type class from Haskell
 */
interface Foldable<out A> {
    /**
     * foldr :: t a -> b -> (a -> b -> b) -> b
     *
     * right-associative fold
     *
     * uses `this` as an implicit first argument (t a), hence the order of the arguments
     *
     * arguments order is manipulated to make usage more Kotlin-friendly
     *
     * @param f function to fold with
     * @param z initial value
     * @return b - resulting value
     */
    fun <B> foldr(z: B, f: (A, B) -> B): B
}