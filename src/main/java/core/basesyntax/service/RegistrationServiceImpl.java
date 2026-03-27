package core.basesyntax.service;

import core.basesyntax.dao.StorageDao;
import core.basesyntax.dao.StorageDaoImpl;
import core.basesyntax.model.User;

public class RegistrationServiceImpl implements RegistrationService {
    private final StorageDao storageDao = new StorageDaoImpl();
    private final RegistrationValidatorImpl registrationValidator = new RegistrationValidatorImpl();

    @Override
    public User register(User user) {
        registrationValidator.validateRegistrationData(user);
        return storageDao.add(user);
    }
}
