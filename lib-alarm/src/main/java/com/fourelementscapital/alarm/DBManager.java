/******************************************************************************
*
* Copyright: Intellectual Property of Four Elements Capital Pte Ltd, Singapore.
* All rights reserved.
*
******************************************************************************/ 

package com.fourelementscapital.alarm;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * The DBManager class provides an Access Interface between the Database and Java.
 * It can be used to get data from the database, insert new data and update existing data.
 * The config file containing the details for the database hostname and credentials is located in the resources folder.
 */
class DBManager {
	
	/**
	 * The database name for the current instance of DBManager
	 */
	private String dbName;
	
	/**
	 * The connection string for the database connection. Stored in config file
	 */
	private String connectionString;
	
	/**
	 * The resultSet which contains the returned results from the SQL Query of GetDatabase
	 */
	ResultSet resultSet = null;	
	
	/**
	 * The connection object of the current connection.
	 */
	private Connection conn = null;	
	
	/**
	 * Initialize the DBManager with the database name. 
	 * @param dbName The name of the database e.x. trading, tradingRef, fundamentals
	 * @throws IOException
	 */
	public DBManager(String dbName) throws IOException
	{
		this.dbName = dbName;
	}	
	
	/**
	 * This function connects to the dbName database;
	 * @throws SQLException This is thrown incase we are not able to connect to the database;
	 * @throws ClassNotFoundException This is thrown incase the driver is not found on this machine
	 * @throws IOException This is thrown incase the config file if not found on this machine under resources folder
	 */
	public void connect() throws SQLException, ClassNotFoundException, IOException
	{
		
		//String url = "jdbc:jtds:sqlserver://"+databaseServerAddress+";DatabaseName="+databaseName;
		String driver = 	Utils.GetConfigValue("database_"+dbName.toLowerCase()+"_"+"driver");	
		
		Class.forName(driver);
		
		String connStr = Utils.GetConfigValue("database_"+dbName.toLowerCase()+"_"+"connstring");
		
		System.out.println("+++++ " + connStr);
        conn = DriverManager.getConnection(connStr);
        connectionString = conn.toString();
	}	
	
	/**
	 * Closes the current Database connection.
	 * @throws SQLException
	 */
	public void closeConnection() throws SQLException
	{
		if(!conn.isClosed())
			conn.close();
		if(resultSet!=null)
			resultSet.close();
	}	
	
	/**
	 * This function returns the result set of the SQL Query used by GetDatabase;
	 * @param query The SQL Query passed to it.
	 * @return
	 * @throws SQLException
	 */
	public ResultSet executeQuery(String query) throws SQLException
	{
		Date start = new Date();
		Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		resultSet = statement.executeQuery(query);
		Date end = new Date();
		long duration = (end.getTime() - start.getTime());
		//Utils.Log("DEBUG: Query ("+query+") took "+duration+ " miliseconds");
		return resultSet;
	}	
	
	/**
	 * Used to query a database and access the resultant data in a ResultSet
	 * @param tableName The name of the database table to be accessed
	 * @param selectedFields A list of the values to be selected through the queries. <code>new ArrayList<String>();</code> 
	 * be passed to select all fields (*)
	 * @param queryParams A hashmap containing the selection filters in the format <code>[column_name]=[value]</code>
	 * @param customQuery Any additional parameters to be added after the Where clause. Could be a GROUP BY or ORDER BY
	 * @return The resulting data in  a ResultSet
	 * @throws SQLException
	 */
	public ResultSet GetDatabase(String tableName,ArrayList<String> selectedFields, Map<String,Object> queryParams, String customQuery) throws SQLException
	{
		String queryBuilder = "";
		if(selectedFields.size()==0)
		{
			selectedFields.add("*");
		}
		
		queryBuilder += "SELECT "+Utils.Join(selectedFields,",")+" FROM "+tableName;
		
		if(queryParams != null && queryParams.size()>0)
		{
			queryBuilder += " WHERE ";
			for (Map.Entry<String,Object> entry : queryParams.entrySet()) {
				
				try{
					Float f = Float.parseFloat(entry.getValue().toString());
					queryBuilder += " "+entry.getKey() + " = "+entry.getValue()+" AND";
				}
				catch(Exception ex)
				{
					queryBuilder += " "+entry.getKey() + " = '"+entry.getValue()+"' AND";
				}
				
			}
			if(customQuery!= null && customQuery!="")
				queryBuilder += customQuery+" AND";
			queryBuilder = queryBuilder.substring(0,queryBuilder.length()-3);
		}
		System.out.println(queryBuilder);
		return executeQuery(queryBuilder);
	}	

}


