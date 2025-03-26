package parkingSystem.model.util.factories;

import parkingSystem.model.*;

public interface AbstractUserFactory
{
    AbstractUser createUser(String name, String email, String password);

}


