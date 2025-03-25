package parkingSystem.model.util.factories;

import parkingSystem.model.AbstractUser;
import parkingSystem.model.VisitorUser;

public class VisitorUserFactory implements AbstractUserFactory {
    @Override
    public AbstractUser createUser(String name, String email, String password) {
        AbstractUser user = new VisitorUser(name, email, password);
        user.validateUser();
        return user;
    }
}
