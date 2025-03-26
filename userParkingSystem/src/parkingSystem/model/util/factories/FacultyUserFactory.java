package parkingSystem.model.util.factories;

import parkingSystem.model.AbstractUser;
import parkingSystem.model.FacultyUser;

public class FacultyUserFactory implements AbstractUserFactory {
    @Override
    public AbstractUser createUser(String name, String email, String password) {
        return new FacultyUser(name, email, password);
    }
}
