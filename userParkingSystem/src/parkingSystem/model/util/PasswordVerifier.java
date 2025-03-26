package parkingSystem.model.util;

public class PasswordVerifier {

    private PasswordVerifier() {}

    public static boolean isStrongPassword(String password) {

        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasDigit = password.matches(".*[0-9].*");
        boolean hasSymbol = password.matches(".*[^A-Za-z0-9].*");

        return hasLower && hasUpper && hasDigit && hasSymbol;

    }

}
