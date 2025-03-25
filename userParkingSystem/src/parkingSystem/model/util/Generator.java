package parkingSystem.model.util;

import parkingSystem.model.AbstractUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Generator {

    private static final String[] adjs = {"Common", "Uncommon", "Rare", "Epic", "Legendary"};
    private static final String[] nouns = {"Zeus", "Atlas", "Hades", "Ares", "Kratos"};

    private static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String lower = "abcdefghijklmnopqrstuvwxyz";
    private static final String digits = "0123456789";
    private static final String symbols = "~`!@#$%^&*()_-+={[}]|\\:;\"'<,>.?/";
    private static final String all = upper + lower + digits + symbols;
    private static final Random random = new Random();

    public static String generateUniqueUsername(List<AbstractUser> existing) {
        List<String> existingToStr = new ArrayList<>();
        for(AbstractUser user : existing) {
            existingToStr.add(user.getName());
        }
        String test;
        do {
            String adjective= adjs[random.nextInt(adjs.length)];
            String noun = nouns[random.nextInt(nouns.length)];
            int randomNumber = 100 + random.nextInt(900);
            test = adjective + noun + randomNumber;
        } while(existingToStr.contains(test));

        return test;
    }

    public static String generateStrongPassword() {
        String pass = "";

        pass += upper.charAt(random.nextInt(upper.length()));
        pass += lower.charAt(random.nextInt(lower.length()));
        pass += digits.charAt(random.nextInt(digits.length()));
        pass += symbols.charAt(random.nextInt(symbols.length()));

        return pass;
    }

}
