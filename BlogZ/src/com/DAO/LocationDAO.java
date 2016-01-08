package com.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.common.Commons;
import com.db.DBProcessor;
import com.model.Location;

public class LocationDAO implements Commons<Integer, Location> {

	private int deger;



	@Override
	public Integer kaydet(Location t2) throws SQLException {

		DBProcessor db=new DBProcessor();		
		db.connect();
		
		int contendId=contentIdGetir();
		
		String sql = "insert into tbl_location (enlem,boylam,detail,content_id) values('"+t2.getEnlem()+"','"+t2.getBoylam()+"',"
				+ "'"+t2.getDetail()+"',"+contendId+")";
		
		
		Statement stmt=db.conn.createStatement();
		int sonuc = stmt.executeUpdate(sql);
		
		 return sonuc;
		
	}

	
	
	public Integer contentIdGetir() throws SQLException
	{
		Statement statement = DBProcessor.conn.createStatement();
		
		String sql = "SELECT id FROM tbl_icerik";
		
		int sayac=0;
		ResultSet resultSet = statement.executeQuery(sql);
		
		while(resultSet.next())
		{
			sayac++;
		}
		
		
		return sayac;
	}
	
	

}
