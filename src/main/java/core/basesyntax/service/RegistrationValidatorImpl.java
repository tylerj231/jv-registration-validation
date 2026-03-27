package core.basesyntax.service;

import core.basesyntax.dao.StorageDao;
import core.basesyntax.dao.StorageDaoImpl;
import core.basesyntax.exception.InvalidAgeException;
import core.basesyntax.exception.InvalidLoginException;
import core.basesyntax.exception.InvalidPasswordException;
import core.basesyntax.exception.InvalidRegistrationDataException;
import core.basesyntax.exception.UserAlreadyExistException;
import core.basesyntax.model.User;

public class RegistrationValidatorImpl implements RegistrationValidator {
    private static final int MIN_LOGIN_LENGTH = 6;
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MIN_VALID_AGE = 18;
    private final StorageDao storageDao = new StorageDaoImpl();

    public boolean validateRegistrationData(User user) {
        if (user == null) {
            throw new InvalidRegistrationDataException(
                    "Could not register user. User cannot be null"
            );
        }
        try {
            isUserNew(user);
            isLoginValid(user);
            isPasswordValid(user);
            return isValidAge(user);
        } catch (UserAlreadyExistException
                | InvalidLoginException
                | InvalidPasswordException
                | InvalidAgeException e
        ) {
            throw new InvalidRegistrationDataException(
                    "Could not register. The following issue occurred:"
                            + e.getMessage()
            );
        }

    }

    public boolean isUserNew(User user) {
        if (user.getLogin() == null) {
            throw new InvalidRegistrationDataException(
                    "Could not register user. Login cannot be null"
            );
        }

        String login = user.getLogin();

        if (storageDao.get(login) != null) {
            throw new UserAlreadyExistException(
                    "User with %s login already exists".formatted(login)
            );
        }
        return true;
    }

    public boolean isLoginValid(User user) {
        if (user.getLogin() == null) {
            throw new InvalidRegistrationDataException(
                    "Could not register user. Login cannot be null"
            );
        }

        int loginLength = user.getLogin().length();

        if (loginLength < MIN_LOGIN_LENGTH) {
            throw new InvalidLoginException(
                    "Login length must be at least 6 characters long. Provided: "
                            + loginLength
            );
        }
        return true;
    }

    public boolean isPasswordValid(User user) {
        if (user.getPassword() == null) {
            throw new InvalidRegistrationDataException(
                    "Could not register user. Password cannot be null"
            );
        }

        int passwordLength = user.getPassword().length();

        if (passwordLength < MIN_PASSWORD_LENGTH) {
            throw new InvalidPasswordException(
                    "Password length must be at least 6 characters long. Provided: "
                            + passwordLength
            );
        }
        return true;
    }

    public boolean isValidAge(User user) {
        if (user.getAge() == null) {
            throw new InvalidRegistrationDataException(
                    "Could not register user. Invalid age parameter"
            );
        }

        int age = user.getAge();

        if (age < MIN_VALID_AGE) {
            throw new InvalidAgeException(
                    "Age must be at least 18 years old. Actual: " + age
            );
        }
        return true;
    }
}
