import java.sql.Connection;

public class Project {

	public static void main(String[] args) {
		
		if(args.length==0) {
			usage();
			System.exit(0);
		}
		
		Connection con;
		
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
	
	public static void usage() {
		System.out.println("Usage: java Project [item1] [item2]");
	}

}
