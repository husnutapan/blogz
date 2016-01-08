package com.db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;

public class DBProcessor {
	
	public static Connection conn;
	
	public static void connect()
	{
		try {
			
			Class.forName("org.postgresql.Driver");
			String url="jdbc:postgresql://localhost:5432/blogz";
			String userName="postgres";
			String password="husnu1994";
			
			conn=DriverManager.getConnection(url,userName,password);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
	
	

}
