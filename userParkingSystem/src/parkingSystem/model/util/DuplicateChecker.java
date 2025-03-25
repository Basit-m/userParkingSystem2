package parkingSystem.model.util;

import parkingSystem.model.AbstractUser;

import java.util.ArrayList;
import java.util.List;

public class DuplicateChecker {

    public static boolean usernameNotTaken(List<AbstractUser> users, String test) {
        List<String> usernameList = new ArrayList<>();
        for(AbstractUser user : users) {
            usernameList.add(user.getName());
        }

        if(usernameList.contains(test)) {
            return false;
        }
        return true;
    }

    public static boolean emailNotTaken(List<AbstractUser> users, String email) {
        List<String> emailList = new ArrayList<>();
        for(AbstractUser user : users) {
            emailList.add(user.getEmail());
        }

        if(emailList.contains(email)) {
            return false;
        }
        return true;
    }

}
