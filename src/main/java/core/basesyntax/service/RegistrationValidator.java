package core.basesyntax.service;

import core.basesyntax.model.User;

public interface RegistrationValidator {
    boolean validateRegistrationData(User user);

    boolean isUserNew(User user);

    boolean isLoginValid(User user);

    boolean isPasswordValid(User user);

    boolean isValidAge(User user);
}
