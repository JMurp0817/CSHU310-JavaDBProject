import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class Project {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
		
		Connection con = null;
		Statement stmt = null;
		try
		{	
			int nRemotePort = port; // remote port number of your database
			String strDbPassword = "password";                    // database login password
			String dbName = "finalProject";  
			
			/*
			 * LOAD the Database DRIVER and obtain a CONNECTION
			 * 
			 * */
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:"+nRemotePort+"/test?verifyServerCertificate=false&useSSL=true&serverTimezone=UTC", "msandbox",
					strDbPassword);
			
			con.setAutoCommit(false);
			
			stmt = con.createStatement();
			
			ResultSet resultSet = null;
			
			System.out.println(args[0]+" "+ args[1]);
			
			if(args[0] == "/?") {
				usage();
			} else if(args[0].equals("CreateItem")) {
				
			} else if(args[0].equals("CreatePurchase")) {
				
			} else if(args[0].equals("CreateShipment")) {
				
			} else if(args[0].equals("GetItems")) {
				if(args[1].equals("%")) {
					resultSet = stmt.executeQuery("SELECT ItemCode,ItemDescription,Price FROM `"+dbName+"`.`Item`");
				} else {
					resultSet = stmt.executeQuery("SELECT ItemCode,ItemDescription,Price FROM `"+dbName+"`.`Item` WHERE `ItemCode`='"+args[1]+"'");
				}
			} else if(args[0].equals("GetShipments")) {
				if(args[1].equals("%")) {
					resultSet = stmt.executeQuery("SELECT * FROM `"+dbName+"`.`Shipment` JOIN `"+dbName+"`.`Item` ON `Shipment`.`ItemID` = `Item`.`ID`");
				} else {
					resultSet = stmt.executeQuery("SELECT * FROM `"+dbName+"`.`Shipment` JOIN `"+dbName+"`.`Item` ON `Shipment`.`ItemID` = `Item`.`ID` WHERE ItemCode='"+args[1]+"'");
				}
			} else if(args[0].equals("GetPurchases")) {
				if(args[1].equals("%")) {
					resultSet = stmt.executeQuery("SELECT * FROM `"+dbName+"`.`Purchase` JOIN `"+dbName+"`.`Item` ON `Purchase`.`ItemID` = `Item`.`ID`");
				} else {
					resultSet = stmt.executeQuery("SELECT * FROM `"+dbName+"`.`Purchase` JOIN `"+dbName+"`.`Item` ON `Purchase`.`ItemID` = `Item`.`ID` WHERE ItemCode='"+args[1]+"'");
				}
			} else if(args[0].equals("ItemsAvailable")) {
				
			} else if(args[0].equals("UpdateItem")) {
				
			} else if(args[0].equals("DeleteItem")) {
				
			} else if(args[0].equals("DeleteShipment")) {
				
			} else if(args[0].equals("DeletePurchase")) {
				
			} else {
				usage();
			}
			
			ResultSetMetaData rsmd = resultSet.getMetaData();

			int columnsNumber = rsmd.getColumnCount();
			if(columnsNumber > 0) {
				while (resultSet.next()) {
					for (int i = 1; i <= columnsNumber; i++) {
						if (i > 1) System.out.print(",  ");
						String columnValue = resultSet.getString(i);
						System.out.print(rsmd.getColumnName(i) + " " + columnValue);
					}
					System.out.println(" ");
				}	
			} else {
				System.out.println("Result set is empty");
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
	
	public static void usage() {
		System.out.println("Usage: java Project [item1] [item2]");
	}

}
