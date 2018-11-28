
import java.sql.*;
import java.util.*;
import java.io.*;

public class Project_v4 {
    public static void main(String args[]) {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/ProjectAssignment?serverTimezone=UTC&useSSL=TRUE";
            String user, pass;
            user = readEntry("UserId: ");
            pass = readEntry("Password: ");
            conn = DriverManager.getConnection(url, user, pass);
            
            mainMenu(conn);
            
        }
        catch (ClassNotFoundException e){
            System.out.println ("Could not load the driver");
        }

        catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) { /* ignored */}
            }
        }
    }
    static String readEntry(String prompt) {
        try {
            StringBuffer buffer = new StringBuffer();
            System.out.print(prompt);
            System.out.flush();
            int c = System.in.read();
            while(c != '\n' && c != -1) {
                buffer.append((char)c);
                c = System.in.read();
            }
            return buffer.toString().trim();
        } catch (IOException e) {
            return "";
        }
    }
    //Method to print the main menu and the options that can be chosen
    static void printMainMenu(){
    	System.out.println("***********************************************************");
        System.out.println("         Welcome to the Housing System");     
        System.out.println("***********************************************************");
        System.out.println("The following actions can be performed");
        System.out.println("1. Resident Login");
        System.out.println("2. Applicant Registration/Apply");
        System.out.println("3. Admin");
        System.out.println("4. Quit");
        System.out.print("Please enter the action you wish to take: ");
    }
    //Prints the main menu and takes user input for what to do. 
    static void mainMenu(Connection conn){
    	Scanner console=new Scanner(System.in);
    	printMainMenu();
        String choice=console.nextLine();
        while(false==choice.equals("4")){
        	if(true==choice.equals("1")){
        		System.out.println("You've chosen Resident Login.");
        	}else if(true==choice.equals("2")){
        		System.out.println("Please enter R if you are currently an applicant, or N to create a new applicant account.");
        		choice=console.nextLine().toUpperCase();
        		if(true==choice.equals("R")){
        			System.out.println("**Returning Applicant**");
        			String aID=applicantLogin(conn);
        			if(aID!=null){
        				applicantMenu(aID,conn);
        			}else{
        				System.out.println("Could not find an account matching that user name and password.");
        			}
        		}else if(true==choice.equals("N")){
        			System.out.println("**New Applicant**");
        			String ID = newPerson(conn);
        			if(false==ID.equals("bad")){
        				newApplicant(conn,ID);
        				applicantMenu(ID,conn);
        			}
        		}else{
        			System.out.println("That is not a valid response.");
        		}
        	}else if(true==choice.equals("3")){
        		System.out.println("You've chosen Admin");
        		adminMenu(conn);
        	}else{ //If user did not put in a valid entry
        		System.out.println("There is no action matching that input.");
        	}
        	printMainMenu();
        	choice=console.nextLine();
        }
        System.out.println("Thank you and have a great day.");
    }
    //Has the user attempt to login 
    static String applicantLogin(Connection conn){
    	try{
    		String query = "select applicant_id from applicant where applicant_id = ? and password = ?";
    		PreparedStatement p = conn.prepareStatement (query);
    		String id = readEntry("Enter applicant_id: ");
    		String password = readEntry("Enter password: ");
    		p.clearParameters();
    		p.setString(1,id);
    		p.setString(2,password);
    		ResultSet r = p.executeQuery();
    		String aid=null;
    		while(r.next()) {
    			aid = r.getString(1);
    			System.out.println("Thank You for logging in.");
          }
    		return aid;
    	}catch(SQLException ex) { 
            System.out.println("Could not find an account matching that user name and password.");
            return null;
    	}
    }
    
    //Has the user attempt to login 
    static String newPerson(Connection conn){
    	try{
    		Scanner console=new Scanner(System.in);
    		String query = "insert into person values (?,?,?,?,?,?,?,?,?,?)";
    		PreparedStatement p = conn.prepareStatement (query);
    		String name = readEntry("Enter your name: ");
    		String student_id = readEntry("Enter your student ID: ");
    		String ssn=readEntry("Enter your Social Security Number: ");
    		String gender=readEntry("Enter your gender(F for female, M for male): ");
    		String spouse_id=null;
    		System.out.println("Do you have a spouse? (Y or N)");
    		String yON=console.next();
    		if(true==yON.equals("Y")){
    			spouse_id=readEntry("Please enter your spouses student ID: ");
    		}
    		String address=readEntry("Please Enter your Address: ");
    		String phone=readEntry("Please enter your phone number: ");
    		String studyYear=readEntry("Please enter your year of study: ");
    		String fh_id=null;
    		System.out.println("Do you have a family head? (Y or N)");
    		String yON2=console.next();
    		if(true==yON2.equals("Y")){
    			fh_id=readEntry("Please enter your family heads student ID: ");
    		}
    		String major=readEntry("Please enter your major: ");
    		p.clearParameters();
    		p.setString(1,name);
    		p.setString(2, student_id);
    		p.setString(3, ssn);
    		p.setString(4, gender);
    		p.setString(5, spouse_id);
    		p.setString(6, address);
    		p.setString(7, phone);
    		p.setString(8, studyYear);
    		p.setString(9, fh_id);
    		p.setString(10, major);
    		p.executeUpdate();
    		System.out.println("Your information has been successfully entered into the database");
    		return student_id;
    	}catch(SQLException ex) { 
            System.out.println("There was a problem entering your data");
            return "bad";
    	}
    }
    
    static void newApplicant(Connection conn,String ID){
    	try{
    		Scanner console=new Scanner(System.in);
    		String query = "insert into applicant values (?,?,curdate(),0,?)";
    		PreparedStatement p = conn.prepareStatement (query);
    		String roommate_id=null;
    		System.out.println("Do you have a preferred roommate? (Y or N)");
    		String yON=console.next();
    		if(true==yON.equals("Y")){
    			roommate_id=readEntry("Please enter your preferred roommates student ID: ");
    		}
    		String password = readEntry("Please choose a password: ");
    		p.clearParameters();
    		p.setString(1,ID);
    		p.setString(2, roommate_id);
    		p.setString(3, password);
    		p.executeUpdate();
    		System.out.println("Your account has been successfully created.");
    	}catch(SQLException ex) { 
            System.out.println(ex);
    	}
    }
    
    
    //Prints the options available for applicants
    static void printApplicationMenu(){
    	System.out.println("***********************************************************");
        System.out.println("         Application Registration/Apply Menu");     
        System.out.println("***********************************************************");
        System.out.println("The following actions can be performed");
        System.out.println("1. Check Available Units");
        System.out.println("2. Submit Booking Request");
        System.out.println("3. Quit");
        System.out.print("Please enter the action you wish to take: ");
    }
    //Prints options available for applicants and takes user input on which to perform
    static void applicantMenu(String aID,Connection conn){
    	Scanner console=new Scanner(System.in);
    	printApplicationMenu();
    	String choice=console.nextLine();
        while(false==choice.equals("3")){
        	if(true==choice.equals("1")){
        		System.out.println("**You've chosen Check Available Units**");
        		printUnitTypes(conn);
        		checkUnitAvailability(conn);
        	}else if(true==choice.equals("2")){
        		System.out.println("You've chosen Submit Booking Requests");
        		int x=bookingRequest(aID,conn);
        		if(x==1){
        			break;
        		}
        	}else{ //If user did not put in a valid entry
        		System.out.println("There is no action matching that input.");
        	}
        	printApplicationMenu();
        	choice=console.nextLine();
        }
        System.out.println("Returning to Main Menu");
    }
    static void printUnitTypes(Connection conn){
    	try{
    		System.out.println("ID, Apartment/Suite, # of Bedrooms, # of People allowed, Married allowed, Price");
    		Scanner console=new Scanner(System.in);
    		String query1="select * from unit_type";
    		PreparedStatement p = conn.prepareStatement(query1);
    		p.clearParameters();
    		ResultSet r1= p.executeQuery();
    		while(r1.next()){
    			int id=r1.getInt(1);
    			String aOS=r1.getString(2);
    			int bed=r1.getInt(3);
    			int people=r1.getInt(4);
    			int married=r1.getInt(5);
    			int price=r1.getInt(6);
    			System.out.println(id+", "+aOS+", "+bed+", "+people+", "+married+", "+price);
    		}
    	}catch(SQLException ex) { 
            System.out.println("Could not find an account matching that user name and password.");
    	}
    }
    
    static void checkUnitAvailability(Connection conn){
    	try{
    		String query = "select * from unit where vacant_date < NOW() and unit_type = ?";
    		PreparedStatement p = conn.prepareStatement (query);
    		String id = readEntry("Enter the ID of the apartment type you would like to search for: ");
    		p.clearParameters();
    		p.setString(1, id);
    		ResultSet r = p.executeQuery();
    		System.out.println("**Address, Building #, Unit ID, Apt #, Vacant Date**");
    		System.out.println("----------------------------------------------------");
    		while (r.next ()) {
    			String address=r.getString(1);
    			int building=r.getInt(2);
    			int unit=r.getInt(3);
    			int numb=r.getInt(4);
    			String date=r.getString(5);
    			System.out.println(address+", "+building+", "+unit+", "+numb+", "+date);
    		}
    		System.out.println("----------------------------------------------------");
    	}catch(SQLException ex){ 
		  	System.out.println(ex);
    	}
    }
    
    static int bookingRequest(String ID, Connection conn){
    	try{
    		String query0 = "select spouse_id from person where student_id = ?";
    		PreparedStatement p0 = conn.prepareStatement (query0);
    		p0.setString(1, ID);
    		ResultSet r0=p0.executeQuery();
    		String spouse_id="";
    		while(r0.next()){
    			spouse_id=r0.getString(1);
    		}
    		Scanner console=new Scanner(System.in);
    		String query = "select building_num, unit_type from unit where vacant_date<NOW();";
    		PreparedStatement p = conn.prepareStatement (query);
    		ResultSet r = p.executeQuery();
    		System.out.println("The available building ands unit types are: ");
    		String[] types=new String[20];
    		int position=0;
    		while(r.next()){
    			int build=r.getInt(1);
    			String type=r.getString(2);
    			types[position]=type;
    			System.out.println(build+", "+type);
    			position++;
    		}
    		String query2 = "insert into unit_preference values (?,1,?)";
    		PreparedStatement p2 = conn.prepareStatement (query2);
    		System.out.println("Please enter the ID of the unit type you would like to book: ");
    		String choice=console.next();
    		if(spouse_id!=null&&false==choice.equals("4")&&false==choice.equals("6")){
    			System.out.println("Married students may only apply for unit types 4 and 6");
    			return 0;
    		}else if(false==Arrays.asList(types).contains(choice)){
    			System.out.println("That unit type is not available");
    			p2.setString(1,ID);
    			p2.setString(2,choice);
    			System.out.println(p2);
    			p2.executeUpdate();
    			System.out.println("Your request has been filed in case of a future opening.");
    			return 0;
    		}else{
    			System.out.println("Success!");
    			applicantToResident(ID,conn,choice);
    			System.out.println("You have successfully booked an apartment, and are now a resident!");
    			System.out.println("Please login as a resident from the main menu.");
    			return 1;
    		}
    	}catch(SQLException ex){
    		System.out.println(ex);
    		return 0;
    	}
    }
    static void applicantToResident(String ID, Connection conn,String choice){
    	String address=changeAddress(conn,ID,choice);
    	changeVacantDate(conn,address);
    	addResident(conn,ID,address);
    	removeApplicant(conn,ID);
    	removeUnitPreferences(conn,ID);
    }
    
    static String changeAddress(Connection conn, String ID, String choice){
    	try{
    		String query = "select address from unit where unit_type= ? and vacant_date<NOW();";
    		PreparedStatement p = conn.prepareStatement (query);
    		p.setString(1, choice);
    		ResultSet r = p.executeQuery();
    		r.next();
    		String aAddress=r.getString(1);
    		String query2="update person set address= ? where student_id=?";
    		PreparedStatement p2 = conn.prepareStatement (query2);
    		p2.setString(1, aAddress);
    		p2.setString(2, ID);
    		p2.executeUpdate();
    		return aAddress;
    	}catch(SQLException ex){
    		System.out.println("test1");
    		return null;
    	}
    }
    
    static void changeVacantDate(Connection conn, String address){
    	try{
    		String query="update unit set vacant_date= date_add(curdate(), interval 3 MONTH) where address=?";
    		PreparedStatement p = conn.prepareStatement (query);
    		p.setString(1, address);
    		p.executeUpdate();
    	}catch(SQLException ex){
    		System.out.println("test2");
    	}
    }
    
    static void addResident(Connection conn, String ID, String address){
    	try{
    		String query="select unit_price from unit_type where type_id in(select unit_type from unit where unit.address= ?)";
    		PreparedStatement p = conn.prepareStatement (query);
    		p.setString(1, address);
    		ResultSet r = p.executeQuery();
    		r.next();
    		String price=r.getString(1);
    		String query3="select password from applicant where applicant_id=?";
    		PreparedStatement p3 = conn.prepareStatement (query3);
    		p3.setString(1, ID);
    		ResultSet r3 = p3.executeQuery();
    		r3.next();
    		String password=r3.getString(1);
    		String query2="insert into resident values(?,0,?,?)";
    		PreparedStatement p2 = conn.prepareStatement (query2);
    		p2.setString(1, ID);
    		p2.setString(2, price);
    		p2.setString(3, password);
    		p2.executeUpdate();
    	}catch(SQLException ex){
    		System.out.println(ex);
    	}
    }
    
    static void removeApplicant(Connection conn, String ID){
    	try{
    		String query = "delete from applicant where applicant_id=?";
    		PreparedStatement p = conn.prepareStatement (query);
    		p.setString(1,ID);
    		p.executeUpdate();
    	}catch(SQLException ex){
    		System.out.println(ex);
    	}
    }
    
    static void removeUnitPreferences(Connection conn, String ID){
    	try{
    		String query = "delete from unit_preference where applicant_id=?";
    		PreparedStatement p = conn.prepareStatement (query);
    		p.setString(1,ID);
    		p.executeUpdate();
    	}catch(SQLException ex){
    		System.out.println(ex);
    	}
    }
    
    //Prints the admin menu
    static void printAdminMenu(){
    	System.out.println("***********************************************************");
        System.out.println("         Administration Menu");     
        System.out.println("***********************************************************");
        System.out.println("The following actions can be performed");
        System.out.println("1. Manage Residents");
        System.out.println("2. Manage Applicants");
        System.out.println("3. Demographic Studies");
        System.out.println("4. Manage Maintenance Orders");
        System.out.println("5. Administrative Reports");
        System.out.println("6. Quit");
        System.out.print("Please enter the action you wish to take: ");
    }
    //Prints options available for administrators and takes input for which option to perform
    static void adminMenu(Connection conn){
    	Scanner console=new Scanner(System.in);
    	printAdminMenu();
    	String choice=console.nextLine();
        while(false==choice.equals("6")){
        	if(true==choice.equals("1")){
        		System.out.println("You've chosen Manage Residents.");
        	}else if(true==choice.equals("2")){
        		System.out.println("You've chosen Manage Applicants");
        	}else if(true==choice.equals("3")){
        		System.out.println("You've chosen Demographic Studies");
        	}else if(true==choice.equals("4")){
        		System.out.println("You've chosen Manage Maintenance Orders");
        	}else if(true==choice.equals("5")){
        		System.out.println("You've chosen Administrative Reports");
        		adminReportsMenu(conn);
        	}else{ //If user did not put in a valid entry
        		System.out.println("There is no action matching that input.");
        	}
        	printAdminMenu();
        	choice=console.nextLine();
        }
        System.out.println("Returning to Main Menu");
    }
    static void printAdminReportsMenu(){
    	System.out.println("***********************************************************");
        System.out.println("         Administrative Reports Menu");     
        System.out.println("***********************************************************");
        System.out.println("The following reports can be performed");
        System.out.println("1. Housing Department Reports");
        System.out.println("2. Applicants Reports");
        System.out.println("3. Resident Reports");
        System.out.println("4. Maintenance Department Reports");
        System.out.println("5. Quit");
        System.out.print("Please enter the action you wish to take: ");
    }
    static void adminReportsMenu(Connection conn){
    	Scanner console=new Scanner(System.in);
    	printAdminReportsMenu();
    	String choice=console.nextLine();
        try{
			while(false==choice.equals("5")){
				if(true==choice.equals("1")){
					System.out.println("You've chosen Housing Department Reports.");
				}else if(true==choice.equals("2")){
					applicantReport(conn);
					System.out.println("You've chosen Applicants Reports");
				}else if(true==choice.equals("3")){
					System.out.println("You've chosen Residents Reports");
				}else if(true==choice.equals("4")){
					System.out.println("You've chosen Maintenance Department Reports");
				}else{ //If user did not put in a valid entry
					System.out.println("There is no action matching that input.");
				}
				printAdminReportsMenu();
				choice=console.nextLine();
			}
		}catch (SQLException ex) {
			System.out.println(ex);
		} catch (IOException e) {
			e.printStackTrace();
		}
        System.out.println("Returning to Admin Menu");
    }
	static void applicantReport(Connection conn) throws SQLException, IOException{
		Scanner console=new Scanner(System.in);
		System.out.println("Please enter a semester date (YYYYMMDD): ");
		String semester = console.nextLine();
		String query =  "SELECT person.name, person.address " +
				"FROM person, applicant " +
				"WHERE student_id = applicant_id " +
				"AND application_date <= \'" + semester + "\';";
		PreparedStatement p = conn.prepareStatement (query);
		ResultSet rset;



		try {
			rset = p.executeQuery(query);
		} catch (SQLException e) {
			System.out.println("could not execute query ");
			while (e!= null) {
				System.out.println ("Message  :" + e.getMessage());
				e = e.getNextException();
			}
			p.close();
			return;
		}

		System.out.println("\nAPPLICANT NAMES AND ADDRESSES FOR NEXT SEMESTER");
		System.out.println("--------------------------------------------------");

		while(rset.next())
		{
			System.out.print(rset.getString(1) + " - ");
			System.out.println(rset.getString(2));
		}
		System.out.println();
		p.close();

	}
}
