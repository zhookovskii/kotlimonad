package typeclasses

/**
 * implementation of Applicative type class from Haskell
 *
 * note that this definition lacks `pure :: a -> f a`
 *
 * that is due to this function not making much sense as a non-static method
 *
 * still, while defining an Applicative instance, it is recommended to
 * implement a static `pure` function
 */
interface Applicative<out A>: Functor<A> {
    /**
     * (<*>) :: f a -> f (a -> b) -> f b
     *
     * applies a function, wrapped into applicative, to a value wrapped into applicative
     *
     * uses `this` as an implicit first argument (f a), hence the order of arguments
     *
     * @param f wrapped function to apply
     * @return f b - resulting applicative
     */
    infix fun <B> ast(f: Applicative<(A) -> B>): Applicative<B>
}