package either

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import types.Either
import types.Left
import types.Right
import utils.*

class EitherMonadTest {

    private fun buildUser(
        email: String,
        password: String,
        age: Int
    ): Either<ValidationError, User> =
        eitherAge(age) bind { checkedAge ->
            eitherPassword(password) bind { checkedPassword ->
                eitherUsername(email) bind { checkedUsername ->
                    Either.wrap<ValidationError, User>(
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
            Right(
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
        Assertions.assertTrue(Left(BadEmail) == user)
    }

    @Test
    fun discardsBadPassword() {
        val email = "whatisinternet@gmail.com"
        val password = "123".repeat(5)
        val age = 72
        val user = buildUser(email, password, age)
        Assertions.assertTrue(Left(BadPassword) == user)
    }

    @Test
    fun discardsBadAge() {
        val email = "stupidinfantonline@yahoo.com"
        val password = "agooagoo"
        val age = 13
        val user = buildUser(email, password, age)
        Assertions.assertTrue(Left(BadAge) == user)
    }
}