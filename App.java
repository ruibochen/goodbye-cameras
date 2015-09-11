/*
 * Copyright (c) 2015 Ruibo Chen All rights reserved.
 * Ruibo Chen PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * This is the main launcher of the program.
 */

public class App {
	
	public static String dbName; //name of the schema
    public static String RLCameras; //red light camera table
    public static String SCameras; //speeding camera table
    public static String archiveLog; //log file
    public static String driver = "com.mysql.jdbc.Driver"; //mysql driver
    public static String url = "jdbc:mysql://localhost/mysql"; //mysql server location

	public static void main(String[] args){
		
		Scanner constant = null;
/*
		try{//load constant file
			constant = new Scanner(new FileReader(args[0]));
		} catch (IndexOutOfBoundsException e) {
			System.out.println("IndexOutOfBoundsException - usage: " + System.getProperty("user.dir") + " <input file>");
            System.exit(1);
		} catch (IOException ioe) {
			System.out.println("IOException - usage: " + System.getProperty("user.dir") + " <input file>");
            System.exit(1);
		} 
*/		
		//get values from constant file
		dbName = "camera";//constant.next();//camera
		RLCameras = "RLCameras"; //constant.next();//RLCameras
		SCameras = "SCameras"; //constant.next();//SCameras
		archiveLog = "archive_log.txt"; //constant.next();//archive_log.txt

		//start app
		Login app = new Login();
		app.setVisible(true);
			
			
	}
}
