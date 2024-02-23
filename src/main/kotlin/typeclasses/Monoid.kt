package typeclasses

/**
 * implementation of Monoid type class from Haskell
 *
 * when defining a Monoid instance, a static field for monoid neutral
 * element `mempty :: a` should be implemented
 */
interface Monoid<out A>: Semigroup<A>