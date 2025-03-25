package parkingSystem.model;

import parkingSystem.model.parking.ParkingLot;
import parkingSystem.model.parking.ParkingSpace;
import parkingSystem.model.util.ValidationRequestBuffer;

import java.util.List;

public class ManagerUser extends AbstractUser {

	public ManagerUser(String name, String email, String password) {
		super(name, email, password, "Manager");
		this.isValid = true;
	}

	@Override
	public double getRate() {
		throw new IllegalArgumentException("Managers do not have rate");
	}

	@Override
	public String toString() {
		return "Manager: " + name + " (" + password + ")";
	}
}

