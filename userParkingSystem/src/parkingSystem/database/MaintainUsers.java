package parkingSystem.database;
import parkingSystem.model.AbstractUser;
import parkingSystem.model.StudentUser;
import parkingSystem.model.FacultyUser;
import parkingSystem.model.ManagerUser;
import parkingSystem.model.VisitorUser;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import java.io.*;
import java.util.*;

public class MaintainUsers
{
	private static final String File_Path = "users.csv";
	public ArrayList<AbstractUser> users = new ArrayList<>();
	
	public void load() throws Exception
	{
		users.clear();
	    CsvReader reader = new CsvReader(File_Path);
	    reader.readHeaders();

	    while (reader.readRecord()) 
	    {
	    	String name = reader.get("name");
	        String email = reader.get("email");
	        String password = reader.get("password");
	        String role = reader.get("role");
            boolean isValidated = Boolean.parseBoolean(reader.get("validated"));


	        AbstractUser user;
	        switch (role.toLowerCase()) 
	        {
	        	case "student":
	        		user = new StudentUser(name, email, password);
	                    break;
	                    
	        	case "faculty":
	        		user = new FacultyUser(name, email, password);
	                    break;
	                    
	        	case "manager":
					user = new ManagerUser(name,email,password);
	                user.setValid(true);
					break;
					
	        	case "visitor":
	        		user = new VisitorUser(name, email, password);
	                user.setValid(true);
	                    break;
	        	default:
	        		System.out.println("Invalid role: " + role);
	                   	continue;
	            }
	        
	        if (!role.equalsIgnoreCase("Manager") || !role.equalsIgnoreCase("Visitor")  ) 
	        {
	            user.setValid(isValidated);
	        }	            users.add(user);
	        }
	        reader.close();
	}
	
	public void update() throws Exception 
	{
		 CsvWriter csvOutput = new CsvWriter(new FileWriter(File_Path, false), ',');

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
	
	
	public boolean validateUser(String managerEmail, String userEmail) throws Exception 
	{
        AbstractUser manager = getUserByEmail(managerEmail);
        if (manager == null || (!manager.getRole().equalsIgnoreCase("Manager") && !manager.getRole().equalsIgnoreCase("SuperManager"))) 
        {
            System.out.println("Error: Only Managers or the Super Manager can validate users.");
            return false;
        }

        AbstractUser user = getUserByEmail(userEmail);

            if (user != null && !user.isValidated()) 
            {
                user.validateUser();
                update(); 
                System.out.println("User " + userEmail + " has been validated by " + managerEmail);
                return true;
            }
       
        System.out.println("Error: User not found or already validated.");
        return false;
    }
	
	public AbstractUser getUserByEmail(String email) 
	{
        for (AbstractUser user : users) 
        {
            if (user.getEmail().equalsIgnoreCase(email)) 
            {
                return user;
            }
        }
        return null;
    }
	
	public void updateUser(String email, String newName, String newEmail, String newPassword) throws Exception 
	{
		boolean userUpdate = false;
		AbstractUser user = getUserByEmail(email); 
			if (user != null) 
			{
				user.setName(newName);
				if(!user.getRole().equalsIgnoreCase("Manager"))
				{
		            user.setEmail(newEmail);

				}
				else
				{
					System.out.println("Error: Managers can't change their email.");
	
				}
	            user.setPassword(newPassword);
	            userUpdate = true;	            
			}
		
		
		if(userUpdate)
		{
			 update(); 
             System.out.println("User updated successfully!");
		}
		else
		{
	        System.out.println("User not found!");	  

		}
	}

	 public void addUser(AbstractUser user) throws Exception
	 {
		 for (AbstractUser existingUser : users)
		 {
			 if (existingUser.getEmail().equals(user.getEmail())) 
			 {
				 System.out.println("A user with this email already exists!");
		         return;  
		     }
		 }
	    users.add(user);
	    update();
	    System.out.println("User added successfully!");

	 }
	 
}
