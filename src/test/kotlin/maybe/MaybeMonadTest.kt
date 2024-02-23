package maybe

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import types.Just
import types.Maybe
import types.Nothing
import utils.*

class MaybeMonadTest {

    private fun buildUser(email: String, password: String, age: Int): Maybe<User> =
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

    @Test
    fun allowsCorrectUser() {
        val email = "scarymonsters@mail.ru"
        val password = "1234567"
        val age = 20
        val user = buildUser(email, password, age)
        Assertions.assertEquals(
            Just(
                User(
                    "scarymonsters",
                    Password(password),
                    age
                )
            ),
            user
        )
    }

    @Test
    fun discardsBadEmail() {
        val email = "hahahahahaha"
        val password = "12345"
        val age = 25
        val user = buildUser(email, password, age)
        Assertions.assertEquals(Nothing, user)
    }

    @Test
    fun discardsBadPassword() {
        val email = "whatisinternet@gmail.com"
        val password = "123".repeat(5)
        val age = 72
        val user = buildUser(email, password, age)
        Assertions.assertEquals(Nothing, user)
    }

    @Test
    fun discardsBadAge() {
        val email = "stupidinfantonline@yahoo.com"
        val password = "agooagoo"
        val age = 13
        val user = buildUser(email, password, age)
        Assertions.assertEquals(Nothing, user)
    }
}