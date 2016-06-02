package com.db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;

public class DBProcessor {
	
	public static Connection conn;
	
	public static void connect()
	{
		try {
			//db jar's driver class
			Class.forName("org.postgresql.Driver");
			//your url change your optional db
			String url="jdbc:postgresql://localhost:5432/blogz";
			String userName="username";
			String password="password";
			
			conn=DriverManager.getConnection(url,userName,password);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
	
	

}
