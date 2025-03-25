package parkingSystem.model.util;

import parkingSystem.model.AbstractUser;

import java.util.ArrayList;
import java.util.List;

public class ValidationRequestBuffer {

    private static ValidationRequestBuffer instance;
    private List<AbstractUser> requests;

    private ValidationRequestBuffer() {
        this.requests = new ArrayList<>();
    }
    public static ValidationRequestBuffer getInstance() {
        if(instance == null) {
            instance = new ValidationRequestBuffer();
        }
        return instance;
    }

    public void addRequest(AbstractUser user) {
        requests.add(user);
    }
    public void removeRequest(AbstractUser user) {
        requests.remove(user);
    }
    public List<AbstractUser> getRequests() {
        return this.requests;
    }
    public void setRequests(List<AbstractUser> requests) {
        this.requests = requests;
    }
    public void notifyManager(AbstractUser user) {
    }
}
