package typeclasses

/**
 * implementation of Monad type class from Haskell
 *
 * note that this definition lacks `return :: a -> m a`
 *
 * that is due to this function not making much sense as a non-static method
 *
 * still, while defining a Monad instance, it is recommended to
 * implement a static `wrap` (`return` is a keyword in Kotlin) function
 */
interface Monad<out A>: Applicative<A> {
    /**
     * (>>=) :: m a -> (a -> m b) -> m b
     *
     * chains a calculation to this monad
     *
     * uses `this` as an implicit first argument (m a)
     *
     * @param f Kleisli arrow (a -> m b)
     * @return m b - resulting monad
     */
    infix fun <B> bind(f: (A) -> Monad<B>): Monad<B>
}
