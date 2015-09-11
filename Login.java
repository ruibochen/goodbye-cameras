/*
 * Copyright (c) 2015 Ruibo Chen All rights reserved.
 * Ruibo Chen PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

/**
 * Login frame of the application
 * Contains user login information and password
 * Connection is controlled here
 * checks if db exists, creates it if it doesnt
 * else load mainFrame of application
 */

public class Login extends JFrame {

	public JPanel contentPane;
	public static Connection connection;
	public static JTextField username;
	public static JPasswordField passwordField;

	// Create the frame.
	public Login() {
		setTitle("Goodbye Cameras!");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1024, 768);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Username");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel.setBounds(242, 176, 200, 50);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Password");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1.setBounds(242, 306, 200, 50);
		contentPane.add(lblNewLabel_1);
		
		username = new JTextField();
		username.setBounds(509, 176, 200, 50);
		contentPane.add(username);
		username.setColumns(10);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
			    	Class.forName(App.driver);
			    	connection = DriverManager.getConnection(App.url, username.getText(), passwordField.getText());	    	
			    	JOptionPane.showMessageDialog(null, "Login Successful");
			    	
			    	String pw = passwordField.getText();//temp, remove later
			    	
					if (checkDBExist()==false){
						System.out.println("Building Database");
						BuildDB newDB = new BuildDB();
						newDB.createTables();
						newDB.populateData();
						newDB.closeConnections();
					}
			    	
					dispose(); // at the condition in which user/pw is true

					MainFrame open = new MainFrame();
					open.setVisible(true);
			    	
					} catch (ClassNotFoundException a){
						JOptionPane.showMessageDialog(null, "no driver class found");
					} catch (SQLException a){
						JOptionPane.showMessageDialog(null, "Password incorrect, Try again.");
					} catch (Exception a) {
						a.printStackTrace();
					}//end try

			}
		});
		btnLogin.setBounds(509, 438, 200, 50);
		contentPane.add(btnLogin);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(509, 309, 200, 50);
		contentPane.add(passwordField);
	}
	
	public boolean checkDBExist() throws Exception{
		boolean dbStatus = false;
		ResultSet schemaList = null;
		Connection connection = null;
		
		try{
		//establish connection to local sql server
    	Class.forName(App.driver);
    	//connection = DriverManager.getConnection(url, "root", "root");
    	connection = DriverManager.getConnection(App.url, username.getText(), passwordField.getText());
    	
    	//get list of schemas
		schemaList = connection.getMetaData().getCatalogs(); 
		
		//iterate each catalog in the ResultSet
		while (schemaList.next()) {
			// Get the database name, which is at position 1
			String databaseName = schemaList.getString(1);

			//check if database exists, if exists,
			//close schemaList and return exists
			if (App.dbName.equals(databaseName)){
				System.out.println("Database already exists");
				dbStatus = true;
				break;
			}//end if block
		}//end while block

		} catch (ClassNotFoundException e){
			System.out.println("no driver class found");// No driver class found!
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		    if (schemaList != null) {
		    	try {
		    		schemaList.close();
		    	} catch (SQLException e) {
		    		System.out.println("SQL Exception while closing schemaList");
		    	} // nothing we can do
		    }//end of if
			
		    if (connection != null) {
		    	try {
		    		connection.close();
		    	} catch (SQLException e) {
		    		System.out.println("SQL Exception while closing connection");
		    	} // nothing we can do
		    }//end of if
		}
		
		System.out.println("dbStatus = " + dbStatus);
		return dbStatus;//return the status of the DB
	}//end checkDBExist method

}
