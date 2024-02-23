# kotlimonad

Haskell-style programming embedded into Kotlin

Haskell:
```haskell
push :: a -> State [a] ()
push x = State $ \xs -> ((), x:xs)

pop :: State [a] a
pop = State $ \(x:xs) -> (x, xs)

stackOps :: State [Int] ()
stackOps = push 4 >>= \_ -> 
             pop >>= \x ->
               push (x + 2) >>= \_ ->
                 push 7
                 
exec :: ()
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
    stackOps.runState(listOf())
}
```