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
    private final StorageDao storageDao = new StorageDaoImpl();

    public boolean isValidRegistrationData(User user) {
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
        String login = user.getLogin();
        if (storageDao.get(login) != null) {
            throw new UserAlreadyExistException(
                    "User with %s login already exists".formatted(login)
            );
        }
        return true;
    }

    public boolean isLoginValid(User user) {
        int loginLength = user.getLogin().length();
        if (loginLength < 6) {
            throw new InvalidLoginException(
                    "Login length must be at least 6 characters long. Provided: "
                            + loginLength
            );
        }
        return true;
    }

    public boolean isPasswordValid(User user) {
        int passwordLength = user.getPassword().length();

        if (passwordLength < 6) {
            throw new InvalidPasswordException(
                    "Password length must be at least 6 characters long. Provided: "
                            + passwordLength
            );
        }
        return true;
    }

    public boolean isValidAge(User user) {
        int age = user.getAge();
        if (age < 18) {
            throw new InvalidAgeException("Age must be at least 18 years old. Actual: " + age);
        }
        return true;
    }
}
