package it.sii.challenge.valand.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DataSource{
	
	public String dbUri="jdbc:mysql://localhost/challengeSii";
	public String username="root";
	public String password="root";
	
	public Connection getConnection() throws PersistenceException{
	
		Connection connection=null;
		try{
		Class.forName("com.mysql.jdbc.Driver");
		connection=DriverManager.getConnection(dbUri, username, password);
		}
		catch (ClassNotFoundException e){
			throw new PersistenceException(e.getMessage());
		}
		catch (SQLException e){
			throw new PersistenceException(e.getMessage());
		}
	return connection;
	}
	
}
