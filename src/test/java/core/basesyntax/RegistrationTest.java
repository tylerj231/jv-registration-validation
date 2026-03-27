package core.basesyntax;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.basesyntax.dao.StorageDao;
import core.basesyntax.dao.StorageDaoImpl;
import core.basesyntax.db.Storage;
import core.basesyntax.exception.InvalidAgeException;
import core.basesyntax.exception.InvalidLoginException;
import core.basesyntax.exception.InvalidPasswordException;
import core.basesyntax.exception.InvalidRegistrationDataException;
import core.basesyntax.exception.UserAlreadyExistException;
import core.basesyntax.model.User;
import core.basesyntax.service.RegistrationService;
import core.basesyntax.service.RegistrationServiceImpl;
import core.basesyntax.service.RegistrationValidator;
import core.basesyntax.service.RegistrationValidatorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RegistrationTest {
    private final RegistrationService registrationService = new RegistrationServiceImpl();
    private final RegistrationValidator registrationValidator = new RegistrationValidatorImpl();
    private final StorageDao storageDao = new StorageDaoImpl();
    private User testUserValid;
    private User testUserInvalid;

    @BeforeEach
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
        Storage.people.remove(testUserValid);

    }

    @Test
    void registerUser_Ok() {
        registrationService.register(testUserValid);
        boolean expected = true;
        boolean actual = Storage.people.contains(testUserValid);
        assertEquals(expected, actual);
        Storage.people.remove(testUserValid);
    }

    @Test
    void isNewUser_newUser_Ok() {
        User user = storageDao.get(testUserValid.getLogin());
        assertNull(user);
    }

    @Test
    void isLoginValid_validLogin_Ok() {
        boolean actual = registrationValidator.isLoginValid(testUserValid);
        assertTrue(actual);

    }

    @Test
    void isValidLoginMinLength_Ok() {
        testUserValid.setLogin("userUs");
        boolean actual = registrationValidator.isLoginValid(testUserValid);
        assertTrue(actual);
    }

    @Test
    void isValidPassword_validPassword_Ok() {
        boolean actual = registrationValidator.isPasswordValid(testUserValid);
        assertTrue(actual);
    }

    @Test
    void isValidPasswordMinLength_Ok() {
        testUserValid.setPassword("minmin");
        boolean actual = registrationValidator.isPasswordValid(testUserValid);
        assertTrue(actual);
    }

    @Test
    void isValidAge_validAge_Ok() {
        boolean actual = registrationValidator.isValidAge(testUserValid);
        assertTrue(actual);
    }

    @Test
    void isMinValidAge_Ok() {
        testUserValid.setAge(18);
        boolean actual = registrationValidator.isValidAge(testUserValid);
        assertTrue(actual);
    }

    @Test
    void isValidRegistrationData_validRegistrationData_Ok() {
        boolean actual = registrationValidator.validateRegistrationData(testUserValid);
        assertTrue(actual);
    }

    @Test
    void isValidRegistrationData_registrationData_notOk() {
        assertThrows(
                InvalidRegistrationDataException.class,
                () -> registrationValidator.validateRegistrationData(testUserInvalid)
        );
    }

    @Test
    void isValidRegistrationData_registrationDataIsNull_notOk() {
        testUserInvalid = null;
        assertThrows(
                InvalidRegistrationDataException.class,
                () -> registrationValidator.validateRegistrationData(testUserInvalid)
        );
    }

    @Test
    void isNewUser_newUser_notOk() {
        User user = storageDao.add(testUserValid);
        assertThrows(UserAlreadyExistException.class, () -> registrationValidator.isUserNew(user));
    }

    @Test
    void isValidLogin_login_notOK() {
        assertThrows(
                InvalidLoginException.class,
                () -> registrationValidator.isLoginValid(testUserInvalid)
        );
    }

    @Test
    void isValidLogin_loginLength_notOK() {
        testUserInvalid.setLogin("ulog1");
        assertThrows(
                InvalidLoginException.class,
                () -> registrationValidator.isLoginValid(testUserInvalid)
        );
    }

    @Test
    void isValidLogin_loginIsNull_notOK() {
        testUserInvalid.setLogin(null);
        assertThrows(
                InvalidRegistrationDataException.class,
                () -> registrationValidator.isLoginValid(testUserInvalid)
        );
    }

    @Test
    void isValidPassword_password_notOk() {
        assertThrows(InvalidPasswordException.class,
                () -> registrationValidator.isPasswordValid(testUserInvalid)
        );
    }

    @Test
    void isValidPassword_passwordLength_notOk() {
        testUserInvalid.setPassword("12345");
        assertThrows(InvalidPasswordException.class,
                () -> registrationValidator.isPasswordValid(testUserInvalid)
        );
    }

    @Test
    void isValidPassword_passwordIsNull_notOk() {
        testUserInvalid.setPassword(null);
        assertThrows(InvalidRegistrationDataException.class,
                () -> registrationValidator.isPasswordValid(testUserInvalid)
        );
    }

    @Test
    void isValidAge_age_notOk() {
        assertThrows(InvalidAgeException.class,
                () -> registrationValidator.isValidAge(testUserInvalid)
        );
    }

    @Test
    void isValidAge_ageIsLessThanMin_notOk() {
        testUserInvalid.setAge(17);
        assertThrows(InvalidAgeException.class,
                () -> registrationValidator.isValidAge(testUserInvalid)
        );
    }

    @Test
    void isValidAge_ageIsNull_notOk() {
        testUserInvalid.setAge(null);
        assertThrows(InvalidRegistrationDataException.class,
                () -> registrationValidator.isValidAge(testUserInvalid)
        );
    }
}
