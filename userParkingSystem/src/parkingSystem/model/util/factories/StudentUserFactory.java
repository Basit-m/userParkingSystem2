package parkingSystem.model.util.factories;

import parkingSystem.model.AbstractUser;
import parkingSystem.model.StudentUser;

public class StudentUserFactory implements AbstractUserFactory
{
    @Override
    public AbstractUser createUser(String name, String email, String password)
    {
        return new StudentUser(name, email, password);
    }
}
