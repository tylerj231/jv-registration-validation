package core.basesyntax;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.basesyntax.exception.InvalidAgeException;
import core.basesyntax.exception.InvalidLoginException;
import core.basesyntax.exception.InvalidPasswordException;
import core.basesyntax.exception.InvalidRegistrationDataException;
import core.basesyntax.model.User;
import core.basesyntax.service.RegistrationValidator;
import core.basesyntax.service.RegistrationValidatorImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RegistrationTest {
    private final RegistrationValidator registrationValidator = new RegistrationValidatorImpl();
    private User testUserValid;
    private User testUserInvalid;

    @BeforeAll
    void setUp() {
        testUserValid = new User();
        testUserInvalid = new User();
        testUserValid.setLogin("testuser23");
        testUserValid.setPassword("password23");
        testUserValid.setAge(25);
        testUserValid.setId(1L);

        testUserInvalid.setLogin("test");
        testUserInvalid.setPassword("pass");
        testUserInvalid.setAge(15);
        testUserInvalid.setId(1L);

    }

    @Test
    void containsValidLogin_Ok() {
        boolean actual = registrationValidator.isLoginValid(testUserValid);
        assertTrue(actual);

    }

    @Test
    void containsValidPassword_Ok() {
        boolean actual = registrationValidator.isPasswordValid(testUserValid);
        assertTrue(actual);
    }

    @Test
    void containsValidAge_Ok() {
        boolean actual = registrationValidator.isValidAge(testUserValid);
        assertTrue(actual);
    }

    @Test
    void isValidRegistrationData_Ok() {
        boolean actual = registrationValidator.isValidRegistrationData(testUserValid);
        assertTrue(actual);
    }

    @Test
    void isValidRegistrationData_notOk() {
        assertThrows(
                InvalidRegistrationDataException.class,
                () -> registrationValidator.isValidRegistrationData(testUserInvalid)
        );
    }

    @Test
    void containsValidLogin_notOK() {
        assertThrows(
                InvalidLoginException.class,
                () -> registrationValidator.isLoginValid(testUserInvalid)
        );
    }

    @Test
    void containsValidPassword_notOk() {
        assertThrows(InvalidPasswordException.class,
                () -> registrationValidator.isPasswordValid(testUserInvalid)
        );
    }

    @Test
    void containsValidAge_notOk() {
        assertThrows(InvalidAgeException.class,
                () -> registrationValidator.isValidAge(testUserInvalid)
        );
    }
}
