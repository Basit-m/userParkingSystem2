package parkingSystem.model;

public class FacultyUser extends AbstractUser {

	public FacultyUser(String name, String email, String password) {
		super(name, email, password, "Faculty");
		// TODO Auto-generated constructor stub
	}

	@Override
	public double getRate() {
		if(!isValid) {
			return 15.00;
		}
		return 8;
	}

}
