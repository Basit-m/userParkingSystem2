package parkingSystem.model;

public class NonFacultyUser extends AbstractUser {
	public NonFacultyUser(String name, String email, String password) {
		super(name, email, password, "Non-Faculty");
		// TODO Auto-generated constructor stub
	}
	@Override
	public double getRate() {
		if(!isValid) {
			return 15.00;
		}
		return 10;
	}
}
