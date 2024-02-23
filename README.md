# kotlimonad

Haskell-style programming embedded into Kotlin

example: Stack on State

Haskell:
```haskell
push :: a -> State [a] ()
push x = state $ \xs -> ((), x:xs)

pop :: State [a] a
pop = state $ \(x:xs) -> (x, xs)

stackOps :: State [Int] ()
stackOps = push 4 >>= \_ -> 
             pop >>= \x ->
               push (x + 2) >>= \_ ->
                 push 7
                 
exec :: ((), [Int])
exec = runState stackOps []
```

Kotlin:
```kotlin
fun <A> push(a: A): State<List<A>, Unit> =
    State { s ->
        Unit to (s + a)
    }

fun <A> pop(): State<List<A>, A> =
    State { s ->
        s.last() to s.dropLast(1)
    }

fun exec() {
    val stackOps: State<List<Int>, Unit> = push(4) bind { _ ->
        pop<Int>() bind { x ->
            push(x + 2) bind { _ ->
                push(7)
            }
        }
    }
    val (_: Unit, stack: List<Int>) = stackOps.runState(listOf())
}
```

## Type classes

This implementation translates a small subset 
of Haskell type system into 
OOP code (albeit, quite naively)

Haskell:
```haskell
class Functor f => Applicative f where
    pure  :: a -> f a
    (<*>) :: f (a -> b) -> f a -> f b
```

Kotlin:
```kotlin
interface Applicative<out A>: Functor<A> {
    infix fun <B> ast(f: Applicative<(A) -> B>): Applicative<B>
}
```

While this solution entertains the idea of one programming
paradigm being expressed through another one, of course
there are some core limitations to this approach

For example, as you can see in the code above,
the Applicative interface does not require 
`pure :: a -> f a`

That is due to this function not making much sense as a
*method*: in Haskell `pure` is used to inject values into
Applicative, but we can't call `pure` without having
an Applicative instance in the first place

```kotlin
val mb = Just(4) // the value is injected already
val injected = mb.pure(4) // why???
```

This is also the reason for arguments order differing
from Haskell: as you can see above, the `ast` function,
which is analogous to `<*>` in Haskell, has

```haskell
ast :: f a -> f (a -> b) -> f b
```

as its "type signature" instead of

```haskell
(<*>) :: f (a -> b) -> f a -> f b
```

because it uses `this` as its implicit
first argument `f a` - the instance of Applicative,
upon which we call the `ast` method

But what about `pure`? The only workaround I've 
come up with is implementing `pure` (and `wrap`, 
which is analogous to `return` in Haskell) as 
*static methods*

```kotlin
val mb = Maybe.pure(4) // a bit less awkward
```

But this reduces strictness greatly, because we cannot
require defining static fields / methods on interface
level... but it's still kinda neat!

```kotlin
fun buildUser(email: String, password: String, age: Int): Maybe<User> =
    maybeAge(age) bind { checkedAge ->
        maybePassword(password) bind { checkedPassword ->
            maybeUsername(email) bind { checkedUsername ->
                Maybe.wrap(
                    User(
                        checkedUsername,
                        checkedPassword,
                        checkedAge
                    )
                )
            }
        }
    }
```

## OOP vs FP

But because Kotlin is an OOP-based language, of course,
using constructors instead of static `pure` / `wrap`
methods is sometimes more convenient

Especially, when Kotlin requires you to hardcode type 
parameters, it gets quite mouthful...
```kotlin
val perms = setOf(AllowWrite, AllowRead)

val reader = write("hello") bind { _ ->
    write("aboba") bind { _ ->
        read() bind { a ->
            when (a) {
                is Just -> Reader.wrap<Set<Permission>, Boolean>(a.value == "aboba")
                is Nothing -> Reader.wrap(false)
            }
        }
    }
}

val result = reader.runReader(perms)
```

That's where we come to the obvious conclusion, that
OOP is way less expressive of a type system, in
comparison to that which is based on higher order 
types, like in Haskell

This gap in expressiveness is also what I believe to be
the reason for my implementation reeking with unchecked
casts and unsafe variances

Let's look at the Monad type class

```haskell
class Monad m where
```

`m` here is a higher order type (or *kind*), which
has a kind signature of

`m :: * -> *`

And when defining a Monad instance, for example, State

```haskell
newtype State s a = State { runState :: s -> (a, s) }

instance Monad (State s) where
```

`State s` is placed instead of `m`, because it possesses
the exact kind signature of 

`State s :: * -> *`

And what is `s`? Well it could be any type, it has an
implicit `forall`, which means no boundaries apply to
this type parameter

This is all good and swell, but does not really
translate well into Kotlin

```kotlin
interface Monad<out A>: Applicative<A>
```

There is no `m` here, because `m` is a *class* which
implements the Monad interface, this is why the Monad
interface is only parametrized by `A` - the type which
is injected into the monadic context

Kotlin does not allow us to access `m` from the Monad
interface, and this is because, while defining `bind`
(which is analogous to `(>>=)` in Haskell),
we can only state that Kleisli arrow `a -> m b`
carries a type of 

`f: (A) -> Monad<B>`

Which, again, reduces strictness greatly, and is the
exact reason why implementation of the `bind` method
produces unchecked casts

```kotlin
override fun <B> bind(f: (A) -> Monad<B>): State<S, B> =
    State { s ->
        val (a, newState) = runState(s)
        (f(a) as State<S, B>).runState(newState) // unchecked cast
    }
```

Which brings us to a quite obvious conclusion

While trying to translate one programming paradigm 
through another was fun, this solution
also exposes the vast gap in power between the type
systems of the two and portrays how some functional
logic just cannot be implemented in an OOP-based
language

But still I would love to be proven wrong and look at
any suggestions on how to improve this code

