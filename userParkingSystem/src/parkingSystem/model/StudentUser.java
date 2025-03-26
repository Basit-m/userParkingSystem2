package parkingSystem.model;

public class StudentUser extends AbstractUser {
	public StudentUser(String name, String email, String password) {
		super(name, email, password, "Student");
		// TODO Auto-generated constructor stub
	}
	@Override
	public double getRate() {
		if(!isValid) {
			return 15.00;
		}
		return 5;
	}

}
