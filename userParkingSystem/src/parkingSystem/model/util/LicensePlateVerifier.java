package parkingSystem.model.util;

public class LicensePlateVerifier {
    private LicensePlateVerifier() {}

    public static boolean isValidLicense(String license) {
        return license.matches("^[A-Z0-9]{3,4}[- ]?[A-Z0-9]{3,4}$");
    }
}
