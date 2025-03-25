package parkingSystem.model;

public abstract class AbstractUser {
	private static int idGenerator = 1;
	
	protected int id;
	protected String name, email, password, role; 
	protected boolean isValid; 
	
	public AbstractUser(String name, String email, String password, String role)
	{
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.isValid = role.equals("Visitor");
		this.id = idGenerator++;
	}
	
	public abstract double getRate();
	
	public boolean isValid() 
	{
        return isValid;
    }

    public void setValid(boolean isValid) 
    {
        this.isValid = isValid;
    }
    
	public void setName(String name) 
	{
        this.name = name;
    }
	
	 public String getName() 
	 { 	
		 return name; 
	 }
	 
	 public void setEmail(String email) 
	 {
	        this.email = email;
	 }
	 
	 
	 public String getEmail() 
	 { 
		 return email; 
	 }
	 
	 public void setPassword(String password) 
	 {
	        this.password = password;
	 }
	 
	 public String getPassword() {
		return this.password;
	 }
	 
	 public String getRole() 
	 { 
		 return role; 
	 }
	 
	 public boolean isValidated() 
	 { 
		 return isValid; 
	 }
	 
	 public void validateUser() 
	 { 
		 this.isValid = true; 
	 }
	 public void removeValidation() {
		this.isValid = false;
	 }
	   
	 @Override
	 public String toString() {
	        return name + " (" + email + ") - Role: " + role;
	    }
}
