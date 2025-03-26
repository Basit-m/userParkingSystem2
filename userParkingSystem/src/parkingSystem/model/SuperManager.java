package parkingSystem.model;

public class SuperManager extends ManagerUser {

    //For testing, can change to one that may be in CSV
    private static SuperManager instance;

    private SuperManager(String name, String email, String password) {
        super(name, email, password);
    }

    public ManagerUser generateManagerAccount(String username, String email, String password) {
        return new ManagerUser(username, email, password);
    }

    public static SuperManager generateSuperManagerAccount(String name, String email, String password) {
        if(instance == null) {
            instance = new SuperManager(name, email, password);
            return instance;
        }
        throw new IllegalArgumentException("Instance already exists");
    }

    public static SuperManager getInstance() {
        if (instance == null) {
            return null;
        }
        return instance;
    }

    @Override
    public String toString() {
        return "Super Manager: " + name + " (" + password + ")";
    }

}
