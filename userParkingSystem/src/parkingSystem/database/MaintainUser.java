package parkingSystem.database;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import parkingSystem.model.AbstractUser;
import parkingSystem.model.ManagerUser;
import parkingSystem.model.SuperManager;
import parkingSystem.model.util.factories.*;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MaintainUser {

    private static final String FILE_PATH = "users.csv";
    private ArrayList<AbstractUser> users = new ArrayList<>();

    public void load() throws IOException {
        users.clear();
        CsvReader reader = new CsvReader(FILE_PATH);
        reader.readHeaders();

        while(reader.readRecord()) {
            String name = reader.get("name");
            String email = reader.get("email");
            String password = reader.get("password");
            String role = reader.get("role");
            boolean isValidated = Boolean.parseBoolean(reader.get("validated"));

            AbstractUserFactory factory;
            AbstractUser user;
            switch (role.toLowerCase()) {
                case "student":
                    factory = new StudentUserFactory();
                    user = factory.createUser(name, email, password);
                    break;
                case "faculty":
                    factory = new FacultyUserFactory();
                    user = factory.createUser(name, email, password);
                    break;
                case "nonfaculty":
                    factory = new NonFacultyUserFactory();
                    user = factory.createUser(name, email, password);
                    break;
                case "visitor":
                    factory = new VisitorUserFactory();
                    user = factory.createUser(name, email, password);
                    break;
                case "manager":
                    user = new ManagerUser(name, email, password);
                    break;
                case "super manager":
                    user = SuperManager.generateSuperManagerAccount(name, email, password);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid role!");
            }

            user.setValid(isValidated);
            users.add(user);
            reader.close();

        }

    }
    public void update() throws Exception {

        CsvWriter csvOutput = new CsvWriter(new FileWriter(FILE_PATH, false), ',');

        csvOutput.write("name");
        csvOutput.write("email");
        csvOutput.write("password");
        csvOutput.write("role");
        csvOutput.write("validated");
        csvOutput.endRecord();

        for (AbstractUser u : users)
        {
            csvOutput.write(u.getName());
            csvOutput.write(u.getEmail());
            csvOutput.write(u.getPassword());
            csvOutput.write(u.getRole());
            csvOutput.write(String.valueOf(u.isValidated()));
            csvOutput.endRecord();
        }
        csvOutput.close();

    }



}
