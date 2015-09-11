/*
 * Copyright (c) 2015 Ruibo Chen All rights reserved.
 * Ruibo Chen PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Build the DB
 * Populates the DB with data from website
 * http://nyctmc.org/multiview2.php
 */

public class BuildDB {
	
    public Statement statement = null;
	public boolean dbExist = false; //to check if the DB has already been created.
    public Date date;
    
	public int count = 0;
	public String webpage = null;
    public static final String USER_AGENT = "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.2) Gecko/20100115 Firefox/3.6";

    public InputStream getURLInputStream(String sURL) throws Exception {
    	URLConnection oConnection = (new URL(sURL)).openConnection();
        oConnection.setRequestProperty("User-Agent", USER_AGENT);
        return oConnection.getInputStream();
    } // getURLInputStream

    public BufferedReader read(String url) throws Exception {
    	InputStream content = (InputStream)getURLInputStream(url);
        return new BufferedReader (new InputStreamReader(content));
    } // read
    
    //backup DUI to archive
    public void backup(String output){
		//print to archive
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(App.archiveLog, true)))) {
		    out.println(Login.username.getText() + " " + date.toString() + " " + output);
		}catch (IOException e) {
			e.printStackTrace();
		}
    }
    
	public BuildDB(){
	    date = new Date();// display time and date using toString()
		
		try {
			//establish connection to local sql server
	    	Class.forName(App.driver);
			Login.connection = DriverManager.getConnection(App.url, Login.username.getText(), Login.passwordField.getText());	
			statement = Login.connection.createStatement();		//create statement
			String createDB = "CREATE DATABASE " + App.dbName;	//string to create DB
			statement.executeUpdate(createDB);				//execute the statement to create DB
			backup(createDB);								//backup create DB to archive

		} catch (Exception e) {
			e.printStackTrace();
		} 
	}//end of constructor
	
	//method to create tables for red light cameras and speeding cameras
	public void createTables() throws SQLException{
		 try {
			 //create red light camera table
			 System.out.println("Creating table red_light cameras");
			 String update = "CREATE TABLE " + App.dbName + "." + App.RLCameras + "( " +
						"id INT NOT NULL AUTO_INCREMENT, " +
						"line_1 VARCHAR(45), " +
						"line_2 VARCHAR(45), " +
						"city VARCHAR(45), " +
						"state CHAR(2), " +
						"zip_code VARCHAR(5), " +
						"PRIMARY KEY (id)) ENGINE=INNODB;";
			 statement.executeUpdate(update);
			 backup(update);	//backing up red_light camera table

		     //create speeding camera table
			 System.out.println("Creating table speeding cameras");
			 update = "CREATE TABLE " + App.dbName + "." + App.SCameras + "( " +
						"id INT NOT NULL AUTO_INCREMENT, " +
						"line_1 VARCHAR(45), " +
						"line_2 VARCHAR(45), " +
						"city VARCHAR(45), " +
						"state CHAR(2), " +
						"zip_code VARCHAR(5), " +
						"PRIMARY KEY (id)) ENGINE=INNODB;";
		     statement.executeUpdate(update);
		     backup(update);	//backing up speeding camera table
		 } catch (SQLException e) {
		     System.out.println("SQL Exception while creating tables");
		 }
	}//end of createTables() method
	
	//method to populate data into red light cameras and speeding cameras table
	public void populateData() throws Exception{
		webpage = "http://nyctmc.org/multiview2.php";
    	BufferedReader reader = read(webpage);
    	String line = reader.readLine();
    	
    	while (line != null) {
    		Pattern pattern =  Pattern.compile("(?<=<span class=\"OTopTitle\" >)(.*)(?=</span>)");
    		Matcher matcher = pattern.matcher(line);
                
    		if (matcher.find()) {//if match = true
    			count++;
    			System.out.println(matcher.group()); //should put data into db
    			
    			 try {
    				 	if (count < 212){
    				 		String update = "INSERT INTO " + App.dbName + "." + App.RLCameras + " " +
        				 			"(line_1, city, state, zip_code) " +
        				 			"VALUES('"+matcher.group()+"', 'new york', 'ny', '10029') ";
    				 		statement.executeUpdate(update);
    				 		backup(update);
    				 	} else if (count < 296) {
    				 		String update = "INSERT INTO " + App.dbName + "." + App.SCameras + " " +
        				 			"(line_1, city, state, zip_code)" +
        				 			"VALUES('"+matcher.group()+"', 'brooklyn', 'ny', '11220')";
    				 		statement.executeUpdate(update);
    				 		backup(update);
    				 	} else if(count < 328){
    				 		String update = "INSERT INTO " + App.dbName + "." + App.SCameras + " " +
            				 		"(line_1, city, state, zip_code)" +
            				 		"VALUES('"+matcher.group()+"', 'bronx', 'ny', '10470')";
    				 		statement.executeUpdate(update);
    				 		backup(update);
    				 	} else if(count < 474){
    				 		String update = "INSERT INTO " + App.dbName + "." + App.SCameras + " " +
            				 		"(line_1, city, state, zip_code)" +
            				 		"VALUES('"+matcher.group()+"', 'queens', 'ny', '11355')";
    				 		statement.executeUpdate(update);
    				 		backup(update);
    				 	} else {
    				 		String update = "INSERT INTO " + App.dbName + "." + App.RLCameras + " " +
                				 	"(line_1, city, state, zip_code)" +
                				 	"VALUES('"+matcher.group()+"', 'staten island', 'ny', '10301')";
    				 		statement.executeUpdate(update);
    				 		backup(update);
        				 	}
    				 	
    			 } catch (SQLException e) {
    			    	//System.out.println("SQL Exception while populating data");
    			    	e.printStackTrace();
    			 }//end try block
    			
            }//end if

            line = reader.readLine(); //get new line
            
    	}//end outer while loop
		
	}//end populateData method
	
	public void closeConnections(){	//closing the connections
		if (statement != null) {
			try {
		    	statement.close();
		    } catch (SQLException e) {
		    	System.out.println("SQL Exception while closing statement");
		    } // nothing we can do
		}//end of if
		    		
		if (Login.connection != null) {
		    try {
		    	Login.connection.close();
		    } catch (SQLException e) {
		    	System.out.println("SQL Exception while closing connection");
		    } // nothing we can do
		}//end of if
	}//end closeConnections()
			
}
