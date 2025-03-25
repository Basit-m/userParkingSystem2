package parkingSystem.model.util;

public class EmailVerifier {

    private EmailVerifier() {}

    public static boolean isValidEmail(String email) {

        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    }

}
