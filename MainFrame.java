/*
 * Copyright (c) 2015 Ruibo Chen All rights reserved.
 * Ruibo Chen PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import net.proteanit.sql.DbUtils;
import java.sql.*;
import java.util.Date;

/**
 * Main application window
 * Search function supporting
 * dynamic sql query with variable binding
 * delete row function
 * add row function
 * load interactive map
 */

public class MainFrame extends JFrame {

	public JPanel contentPane;
	public JTextField line_1;
	public JTextField line_2;
	public JTextField city;
	public JTextField state;
	public JTextField zip_code;
	public JTable table;
	public String tableName; //name of the table called to action
	public int cameraID; //the id of the camera to be deleted
	public String where; //where statement for queries
	public Statement statement;
	public JTextField id;
    public Date date;
    public PreparedStatement pst;
	
	//delete camera
	public void deleteCamera(){
		try{ 
			Class.forName(App.driver);
			Login.connection = DriverManager.getConnection(App.url, Login.username.getText(), Login.passwordField.getText());	    
			statement = Login.connection.createStatement();
			String update = "DELETE FROM " + App.dbName + "." + tableName + " "
					+ " WHERE id = " + cameraID + ";";
			int rowsAffected = statement.executeUpdate(update);
			
			//check if any rows were removed
			if(rowsAffected > 0) {
			   JOptionPane.showMessageDialog(null, "ID successfully deleted!");
			   backup(update);
			} else {
			    JOptionPane.showMessageDialog(null, "ID does not exist!", "not deleted", JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception a) {
			a.printStackTrace();
		} finally {
		    if (statement != null) {
		    	try {
		    		statement.close();
		    	} catch (SQLException e) {
		    		System.out.println("SQL Exception while closing schemaList");
		    	} // nothing we can do
		    }//end of if
		    if (Login.connection != null) {
		    	try {
		    		Login.connection.close();
		    	} catch (SQLException e) {
		    		System.out.println("SQL Exception while closing connection");
		    	} // nothing we can do
		    }//end of if
		}
	}
	
	//print to archive
    public void backup(String output){
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(App.archiveLog, true)))) {
		    out.println(Login.username.getText() + " " + date.toString() + " " + output);
		}catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	//tests if any field is empty
	public boolean fieldEmpty(){
		if (line_1.getText().equals("") || line_2.getText().equals("") || city.getText().equals("") 
				|| state.getText().equals("") || zip_code.getText().equals("")){
			return false;
		}
		return true;
	}
	
	//add camera location
	public void addCamera(){
		try{ 
			Class.forName(App.driver);
			Login.connection = DriverManager.getConnection(App.url, Login.username.getText(), Login.passwordField.getText());	   
			statement = Login.connection.createStatement();
			String update = "INSERT INTO " + App.dbName + "." + tableName + " "
					+ "(line_1, line_2, city, state, zip_code) "
					+ "VALUES('"+ line_1.getText() 
					+ "', '"+ line_2.getText() 
					+ "', '"+ city.getText() 
					+ "', '"+ state.getText() 
					+ "', '"+ zip_code.getText() +"')";
			statement.executeUpdate(update);
			backup(update);
		} catch (Exception a) {
			a.printStackTrace();
		} finally {
		    if (statement != null) {
		    	try {
		    		statement.close();
		    	} catch (SQLException e) {
		    		System.out.println("SQL Exception while closing schemaList");
		    	} // nothing we can do
		    }//end of if
		    if (Login.connection != null) {
		    	try {
		    		Login.connection.close();
		    	} catch (SQLException e) {
		    		System.out.println("SQL Exception while closing connection");
		    	} // nothing we can do
		    }//end of if
		}
		
		JOptionPane.showMessageDialog(null, "Thank you for adding a camera location!");
	}
	
	//runs a dynamic sql query to database
	public void search(){
		try{
		int count = 0;
		where="";
		
		//generating where clause dynamically
		if(!line_1.getText().equals("")){
			where += (where == "") ? " WHERE line_1 LIKE ? " : " AND line_1 LIKE ?";
		}
		if(!line_2.getText().equals("")){
			where += (where == "") ? " WHERE line_2 LIKE ? " : " AND line_2 LIKE ?";
		}
		if(!city.getText().equals("")){
			where += (where == "") ? " WHERE city LIKE ? " : " AND city LIKE ?";
		}
		if(!state.getText().equals("")){
			where += (where == "") ? " WHERE state LIKE ? " : " AND state LIKE ?";
		}
		if(!zip_code.getText().equals("")){
			where += (where == "") ? " WHERE zip_code = ? " : " AND zip_code = ?";
		}

		//query to be executed
    	String query = 
    			  "SELECT * "
    			+ "FROM " + App.dbName + "." + tableName + " "
    			+  where;

    	Class.forName(App.driver);
    	Login.connection = DriverManager.getConnection(App.url, Login.username.getText(), Login.passwordField.getText());	   
    	pst = Login.connection.prepareStatement(query); //the statement
    	
    	//binding variables
		if(!line_1.getText().equals("")){
			count++;
			pst.setString(count, "%"+line_1.getText()+"%");
		}					
		if(!line_2.getText().equals("")){
			count++;
			pst.setString(count, "%"+line_2.getText()+"%");
		}
		if(!city.getText().equals("")){
			count++;
			pst.setString(count, "%"+city.getText()+"%");
		}
		if(!state.getText().equals("")){
			count++;
			pst.setString(count, "%"+state.getText().toLowerCase()+"%");
		}
		if(!zip_code.getText().equals("")){
			count++;
			pst.setString(count, zip_code.getText());
		}

    	ResultSet rs=pst.executeQuery();//executing query
    	table.setModel(DbUtils.resultSetToTableModel(rs));//outputting results
		} catch (Exception a){
			a.printStackTrace();
		} finally {
		    if (pst != null) {
		    	try {
		    		pst.close();
		    	} catch (SQLException e) {
		    		System.out.println("SQL Exception while closing connection");
		    	} // nothing we can do
		    }//end of if
		    if (Login.connection != null) {
		    	try {
		    		Login.connection.close();
		    	} catch (SQLException e) {
		    		System.out.println("SQL Exception while closing connection");
		    	} // nothing we can do
		    }//end of if
		}
	}
	
	/**
	 * Create the frame.
	 */
	public MainFrame() {
		date = new Date();// display time and date using toString()
		setTitle("Goodbye Cameras!");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1024, 768);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		line_1 = new JTextField();
		line_1.setBounds(187, 21, 140, 25);
		contentPane.add(line_1);
		line_1.setColumns(10);
		
		line_2 = new JTextField();
		line_2.setBounds(337, 21, 140, 25);
		contentPane.add(line_2);
		line_2.setColumns(10);
		
		city = new JTextField();
		city.setBounds(487, 21, 140, 25);
		contentPane.add(city);
		city.setColumns(10);
		
		state = new JTextField();
		state.setBounds(637, 21, 140, 25);
		contentPane.add(state);
		state.setColumns(10);
		
		zip_code = new JTextField();
		zip_code.setBounds(787, 21, 140, 25);
		contentPane.add(zip_code);
		zip_code.setColumns(10);
		
		JTextPane text1 = new JTextPane();
		text1.setText("line_1");
		text1.setBounds(187, 57, 140, 20);
		contentPane.add(text1);
		
		JTextPane text2 = new JTextPane();
		text2.setText("line_2");
		text2.setBounds(337, 57, 140, 20);
		contentPane.add(text2);
		
		JTextPane text3 = new JTextPane();
		text3.setText("city");
		text3.setBounds(487, 57, 140, 20);
		contentPane.add(text3);
		
		JTextPane text4 = new JTextPane();
		text4.setText("state");
		text4.setBounds(637, 57, 140, 20);
		contentPane.add(text4);
		
		JTextPane text5 = new JTextPane();
		text5.setText("zip code");
		text5.setBounds(787, 57, 140, 20);
		contentPane.add(text5);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Find red light cameras", "Find speeding cameras", "Add red light camera", "Add speeding camera"}));
		comboBox.setBounds(10, 21, 160, 25);
		contentPane.add(comboBox);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 122, 988, 563);
		contentPane.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);

		//the master button which controls all actions
		JButton go = new JButton("GO");
		go.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					//get values from JTextFields
					String box = comboBox.getSelectedItem().toString(); //get value from comboBox
					
					//assign values from comboBox
					if (box.equals("Find red light cameras")){
						tableName = App.RLCameras;
					} else if(box.equals("Find speeding cameras")){
						tableName = App.SCameras;
					} else if (box.equals("Add red light camera")){
						tableName = "add_rlcamera";
					} else {
						tableName = "add_scamera";
					}//end if block
					
					
					//if state and zip code is inputted, state must be length 2 and zip code is length 5
					if(state.getText().length()>=3 || state.getText().length()==1){ //test that state is 2 characters
						JOptionPane.showMessageDialog(null, "Please input exactly two characters for the state.");
						return;
					} else if ((zip_code.getText().length() >= 1 && zip_code.getText().length()<= 4) || zip_code.getText().length() >=6 ){ //test that zip code is 5 characters
		    			JOptionPane.showMessageDialog(null, "Please input exactly 5 numbers for the zip code.");
		    			return;
					} 

					//check if to perform search query
					if (tableName.equals(App.RLCameras) || tableName.equals(App.SCameras)){//if the values are for search
						search();
					}//end if
					
					//if we are adding red light camera
			    	if (tableName.equals("add_rlcamera")){ // else we are adding new red light cameras
			    			tableName = App.RLCameras;
			    			if(!fieldEmpty()){
			    			JOptionPane.showMessageDialog(null, "All fields are required");
			    			} else{
			    				addCamera();
			    			}
			    	} else if (tableName.equals("add_scamera")){ // else we are adding speeding cameras
		    				tableName = App.SCameras;
			    			if(!fieldEmpty()){
			    			JOptionPane.showMessageDialog(null, "All fields are required");
			    			} else{
			    				addCamera();
			    			}
			    	}//end if block
					
			}//end action performed
		});//end go
		go.setBounds(937, 21, 61, 56);
		contentPane.add(go);
		
		id = new JTextField();
		id.setText("id");
		id.setBounds(859, 696, 40, 25);
		contentPane.add(id);
		id.setColumns(10);
		
		//combo box to selete which camera to delete from
		JComboBox deleteBox = new JComboBox();
		deleteBox.setModel(new DefaultComboBoxModel(new String[] {"red light camera", "speeding camera"}));
		deleteBox.setBounds(709, 696, 140, 25);
		contentPane.add(deleteBox);
		
		//delete button
		JButton delete = new JButton("Delete");
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String box = deleteBox.getSelectedItem().toString(); //get value from comboBox
				
				cameraID = Integer.parseInt(id.getText());
				
				//assign values from comboBox
				if (box.equals("red light camera")){
					tableName = App.RLCameras;
				} else if(box.equals("speeding camera")){
					tableName = App.SCameras;
				}
				
				deleteCamera();	
			}
		});
		delete.setBounds(909, 696, 89, 25);
		contentPane.add(delete);
		
		JButton loadMap = new JButton("Load Visual Map");
		loadMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Map map = new Map(); //loads the interactive map
			}
		});
		loadMap.setBounds(824, 88, 174, 23);
		contentPane.add(loadMap);

	}
}
