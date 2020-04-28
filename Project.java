import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class Project {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
		
		Connection con = null;
		Statement queryStmt = null, updateStmt = null;
		try
		{	
			int nRemotePort = 55797; // remote port number of your database
			String strDbPassword = "19hello99";                    // database login password
			String dbName = "finalProject";
			
			/*
			 * LOAD the Database DRIVER and obtain a CONNECTION
			 * 
			 * */
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:"+nRemotePort+"/test?verifyServerCertificate=false&useSSL=true&serverTimezone=UTC", "msandbox",
					strDbPassword);
			
			con.setAutoCommit(false);
			
			queryStmt = con.createStatement();
			updateStmt = con.createStatement();
			
			ResultSet resultSet = null;
			int updateResultSet = 0;
      		String sql = "";
			
      if(args.length<=0){
        usage();
      } else if(args[0] == "/?") {
				usage();
			} else if(args[0].equals("CreateItem")) {
				if(args.length!=4) {
					usage();
				} else {
					PreparedStatement stmt = con.prepareStatement("INSERT INTO "+dbName+".Item (`ItemCode`, `ItemDescription`, `Price`) VALUES (?,?,?)");
					stmt.setString(1, args[1]);
					stmt.setString(2, args[2]);
					stmt.setDouble(3, Double.parseDouble(args[3]));
					updateResultSet = stmt.executeUpdate();
				}
			} else if(args[0].equals("CreatePurchase")) {
				if(args.length!=3) {
					usage();
				} else {
					PreparedStatement stmt = con.prepareStatement("INSERT INTO "+dbName+".Purchase (ItemID, Quantity) SELECT ID, ? FROM "+dbName+".Item WHERE ItemCode = ?");
					stmt.setInt(1, Integer.parseInt(args[2]));
					stmt.setString(2, args[1]);
					updateResultSet = stmt.executeUpdate();
				}
			} else if(args[0].equals("CreateShipment")) {
				if(args.length!=4) {
					usage();
				} else {
					PreparedStatement stmt = con.prepareStatement("INSERT INTO `"+dbName+"`.`Shipment` (ItemID, Quantity, ShipmentDate) SELECT ID, ?, ? FROM `"+dbName+"`.`Item` WHERE `ItemCode` = ?");
					stmt.setInt(1,Integer.parseInt(args[2]));
					stmt.setString(2, args[3]);
					stmt.setString(3, args[1]);
					updateResultSet = stmt.executeUpdate();
				}
			} else if(args[0].equals("GetItems")) {
				if(args.length != 2) {
					usage();
				} else {
					if(args[1].equals("%")) {
						resultSet = queryStmt.executeQuery("SELECT ItemCode,ItemDescription,Price FROM `"+dbName+"`.`Item`");
					} else {
						resultSet = queryStmt.executeQuery("SELECT ItemCode,ItemDescription,Price FROM `"+dbName+"`.`Item` WHERE `ItemCode`='"+args[1]+"'");
					}
				}
			} else if(args[0].equals("GetShipments")) {
				if(args.length != 2) {
					usage();
				} else {
					if(args[1].equals("%")) {
						resultSet = queryStmt.executeQuery("SELECT * FROM `"+dbName+"`.`Shipment` JOIN `"+dbName+"`.`Item` ON `Shipment`.`ItemID` = `Item`.`ID`");
					} else {
						resultSet = queryStmt.executeQuery("SELECT * FROM `"+dbName+"`.`Shipment` JOIN `"+dbName+"`.`Item` ON `Shipment`.`ItemID` = `Item`.`ID` WHERE ItemCode='"+args[1]+"'");
					}
				}
			} else if(args[0].equals("GetPurchases")) {
				if(args.length != 2) {
					usage();
				} else {
					if(args[1].equals("%")) {
						resultSet = queryStmt.executeQuery("SELECT * FROM `"+dbName+"`.`Purchase` JOIN `"+dbName+"`.`Item` ON `Purchase`.`ItemID` = `Item`.`ID`");
					} else {
						resultSet = queryStmt.executeQuery("SELECT * FROM `"+dbName+"`.`Purchase` JOIN `"+dbName+"`.`Item` ON `Purchase`.`ItemID` = `Item`.`ID` WHERE ItemCode='"+args[1]+"'");
					}
				}
			} else if(args[0].equals("ItemsAvailable")) {
				if(args.length != 2) {
					usage();
				} else {
					if(args[1].equals("%")) {
						resultSet = queryStmt.executeQuery("SELECT i.ItemCode, i.ItemDescription, COALESCE(s.ssum,0) - COALESCE(p.psum,0) as ItemsAvailable FROM `"+dbName+"`.Item i LEFT JOIN (SELECT sum(Purchase.Quantity) as psum,Purchase.ItemID FROM `"+dbName+"`.`Purchase` GROUP BY Purchase.ItemID) p ON p.ItemID = i.ID LEFT JOIN (SELECT sum(Shipment.Quantity) as ssum,Shipment.ItemID FROM `"+dbName+"`.`Shipment` GROUP BY  Shipment.ItemID) s ON s.ItemID=i.ID GROUP BY i.ItemCode");
					} else {
						resultSet = queryStmt.executeQuery("SELECT i.ItemCode, i.ItemDescription, COALESCE(s.ssum,0) - COALESCE(p.psum,0) as ItemsAvailable FROM `"+dbName+"`.Item i LEFT JOIN (SELECT sum(Purchase.Quantity) as psum,Purchase.ItemID FROM `"+dbName+"`.`Purchase` GROUP BY Purchase.ItemID) p ON p.ItemID = i.ID LEFT JOIN (SELECT sum(Shipment.Quantity) as ssum,Shipment.ItemID FROM `"+dbName+"`.`Shipment` GROUP BY  Shipment.ItemID) s ON s.ItemID=i.ID WHERE i.ItemCode = '"+args[1]+"' GROUP BY i.ItemCode");
					}
				}
			} else if(args[0].equals("UpdateItem")) {
				if(args.length!=3){
					usage();
				} else {
					updateResultSet = updateStmt.executeUpdate("UPDATE `"+dbName+"`.`Item` SET Price = "+args[2]+" WHERE ItemCode = "+args[1]+";");
				}
			} else if(args[0].equals("DeleteItem")) {
				if(args.length != 2) {
					usage();
				} else {
					updateResultSet = updateStmt.executeUpdate("DELETE FROM `"+dbName+"`.`Item` WHERE ItemCode ='"+args[1]+"'");
					if(updateResultSet == 1) {
						System.out.println("Successfully deleted "+args[1]);
					} else {
						System.out.println("There is no Item with the code "+args[1]);
					}
				}
			} else if(args[0].equals("DeleteShipment")) {
				if(args.length != 2) {
					usage();
				} else {
					updateResultSet = updateStmt.executeUpdate("DELETE FROM `"+dbName+"`.`Shipment` WHERE ItemID IN (SELECT ID FROM `"+dbName+"`.`Item` WHERE ItemCode = '"+args[1]+"')  ORDER BY ShipmentDate DESC LIMIT 1");
					if(updateResultSet == 1) {
						System.out.println("Successfully deleted most recent Shipment of "+args[1]);
					} else {
						System.out.println("There are no Shipments of "+args[1]);
					}	
				}
			} else if(args[0].equals("DeletePurchase")) {
				if(args.length != 2) {
					usage();
				} else {
					updateResultSet = updateStmt.executeUpdate("DELETE FROM `"+dbName+"`.`Purchase` WHERE ItemID IN (SELECT ID FROM `"+dbName+"`.`Item` WHERE ItemCode = '"+args[1]+"')  ORDER BY PurchaseDate DESC LIMIT 1");
					if(updateResultSet == 1) {
						System.out.println("Successfully deleted most recent Purchase of "+args[1]);
					} else {
						System.out.println("There are no Purchases of "+args[1]);
					}	
				}
			} else {
				usage();
			}
			
			if(resultSet != null) {
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
				}
			} else {
				if(updateResultSet == 1) {
					System.out.print("Updated successfully!\n");
				} else {
					System.out.print("Error, statement failed to update the database.\n");
				}
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
