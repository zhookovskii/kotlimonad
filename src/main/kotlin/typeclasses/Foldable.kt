package typeclasses

/**
 * implementation of Foldable type class from Haskell
 */
interface Foldable<out A> {
    /**
     * foldr :: t a -> (a -> b -> b) -> b -> b
     *
     * right-associative fold
     *
     * uses `this` as an implicit first argument (t a), hence the order of the arguments
     *
     * @param f function to fold with
     * @param z initial value
     * @return b - resulting value
     */
    fun <B> foldr(f: (A, B) -> B, z: B): B
}