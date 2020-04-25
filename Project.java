import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Project {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
		
		if (args.length<3){
			System.out.println("Usage DBConnectTest <yourportnumber> <sandbox password> <dbname>");
		}
		else{
			Connection con = null;
			Statement stmt = null, stmt2 = null;
			try
			{	
				int nRemotePort = port; // remote port number of your database
				String strDbPassword = "password";                    // database login password
				String dbName = "finalProject";  
				
				/*
				 * STEP 1 and 2
				 * LOAD the Database DRIVER and obtain a CONNECTION
				 * 
				 * */
				Class.forName("com.mysql.cj.jdbc.Driver");
				System.out.println("jdbc:mysql://localhost:"+nRemotePort+"/test?verifyServerCertificate=false&useSSL=true");
				con = DriverManager.getConnection("jdbc:mysql://localhost:"+nRemotePort+"/test?verifyServerCertificate=false&useSSL=true&serverTimezone=UTC", "msandbox",
						strDbPassword);
				// Do something with the Connection
				System.out.println("Database [test db] connection succeeded!");
				System.out.println();
				
				
				if(args[0] == "/?") {
					usage();
				} else if(args[0].equals("CreateItem")) {
					
				} else if(args[0].equals("CreatePurchase")) {
					
				} else if(args[0].equals("CreateShipment")) {
					
				} else if(args[0].equals("GetItems")) {
					
				} else if(args[0].equals("GetShipments")) {
					
				} else if(args[0].equals("GetPurchases")) {
					
				} else if(args[0].equals("ItemsAvailable")) {
					
				} else if(args[0].equals("UpdateItem")) {
					
				} else if(args[0].equals("DeleteItem")) {
					
				} else if(args[0].equals("DeleteShipment")) {
					
				} else if(args[0].equals("DeletePurchase")) {
					
				} else {
					usage();
				}
				
				
			}
			catch( SQLException e )
			{
				System.out.println(e.getMessage());
				con.rollback(); // In case of any exception, we roll back to the database state we had before starting this transaction
			}
			catch(Exception e) {
				System.out.println("Error");
				System.out.println(e.getMessage());
			}
			finally{
				
				/*
				 * STEP 5
				 * CLOSE CONNECTION AND SSH SESSION
				 * 
				 * */				
				con.setAutoCommit(true); // restore dafault mode
				con.close();
			}
		}
	}
	
	public static void usage() {
		System.out.println("Usage: java Project [item1] [item2]");
	}

}
