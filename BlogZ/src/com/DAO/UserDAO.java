package com.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.primefaces.model.map.Marker;

import com.db.DBProcessor;
import com.model.Location;
import com.model.MapYazarlar;
import com.model.User;

public class UserDAO {

	public boolean isLogin(User user2) throws SQLException {

		DBProcessor db = new DBProcessor();

		db.connect();

		Statement stmt = db.conn.createStatement();

		String sql = "Select * from tbl_user" + " where username='" + user2.getUsername() + "' " + "and password='"
				+ user2.getPassword() + "'";

		ResultSet set = stmt.executeQuery(sql);

		boolean b = false;

		while (set.next()) {
			b = true;
		}
		return b;

	}

	public void userKaydet(User user) throws SQLException {
		DBProcessor db = new DBProcessor();

		db.connect();

		String query = " insert into tbl_user (adi,soyadi,username,password,role_id)" + " values (?, ?, ?, ?, ?)";

		PreparedStatement preparedStmt = DBProcessor.conn.prepareStatement(query);
		preparedStmt.setString(1, user.getAdi());
		preparedStmt.setString(2, user.getSoyadi());
		preparedStmt.setString(3, user.getUsername());
		preparedStmt.setString(4, user.getPassword());
		preparedStmt.setInt(5, 1);

		preparedStmt.execute();

	}

	public List<Location> gmapMarker() throws SQLException {
		DBProcessor db = new DBProcessor();

		db.connect();

		Statement stmt = db.conn.createStatement();

		String sql = "Select enlem,boylam,detail from tbl_location";

		ResultSet set = stmt.executeQuery(sql);

		List<Location> list = new ArrayList<Location>();

		while (set.next()) {
			Location location = new Location();
			location.setBoylam(set.getDouble("boylam"));
			location.setEnlem(set.getDouble("enlem"));
			location.setDetail(set.getString("detail"));
			list.add(location);
		}
		return list;

	}

	public MapYazarlar getMapBilgiler(String baslik) throws SQLException {
		DBProcessor db = new DBProcessor();

		db.connect();

		Statement stmt = db.conn.createStatement();
		
		String query = " SELECT ti.title, ti.content,ti.tags,ti.create_date,tu.adi,tu.soyadi,tl.detail FROM tbl_location tl "
				+ "inner join tbl_icerik ti on tl.content_id=ti.id "
				+ "inner join tbl_user tu on ti.user_id=tu.id where detail='"+baslik.toString()+"'";
		
		
		ResultSet set = stmt.executeQuery(query);

		MapYazarlar mapYazarlar = new MapYazarlar();

		while (set.next()) {
			mapYazarlar.setTitle(set.getString("title"));
			mapYazarlar.setIcerik(set.getString("content"));
			mapYazarlar.setTagler(set.getString("tags"));
			mapYazarlar.setOlusturulmaTarihi(set.getString("create_date"));
			mapYazarlar.setYazarAd(set.getString("adi"));
			mapYazarlar.setYazarSoyad(set.getString("soyadi"));

		}
		return mapYazarlar;
	}
}