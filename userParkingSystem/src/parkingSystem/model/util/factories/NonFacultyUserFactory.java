package parkingSystem.model.util.factories;

import parkingSystem.model.AbstractUser;
import parkingSystem.model.NonFacultyUser;

public class NonFacultyUserFactory implements AbstractUserFactory {
    @Override
    public AbstractUser createUser(String name, String email, String password) {
        return new NonFacultyUser(name, email, password);
    }
}
