package typeclasses

/**
 * implementation of Functor type class from Haskell
 */
interface Functor<out A> {
    /**
     * fmap :: f a -> (a -> b) -> f b
     *
     * applies a function to a value wrapped into functor
     *
     * uses `this` as an implicit first argument (f a), hence the order of arguments
     *
     * @param f function to apply
     * @return f b - resulting functor
     */
    infix fun <B> fmap(f: (A) -> B): Functor<B>
}