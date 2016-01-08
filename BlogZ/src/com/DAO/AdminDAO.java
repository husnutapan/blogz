package com.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.faces.context.FacesContext;

import com.common.Commons;
import com.db.DBProcessor;
import com.model.Content;
import com.model.User;

public class AdminDAO implements Commons<Integer, Content> {

	public ArrayList<Content> listeDoldur(int userId) throws SQLException {

		DBProcessor db = new DBProcessor();

		db.connect();

		Statement stmt = db.conn.createStatement();

		String sql = "Select * from tbl_icerik where user_id=" + userId;

		ResultSet set = stmt.executeQuery(sql);

		ArrayList<Content> liste = new ArrayList<Content>();

		Content c;

		while (set.next()) {

			c = new Content();

			c.setContent(set.getString("content"));
			c.setId(set.getInt("id"));
			c.setTag(set.getString("tags"));
			c.setCreateDate(set.getString("create_date"));
			c.setTitle(set.getString("title"));

			liste.add(c);

		}

		return liste;

	}

	@Override
	public Integer kaydet(Content t2) throws SQLException {

		DBProcessor db = new DBProcessor();

		db.connect();

		Statement stmt = db.conn.createStatement();

		String sql = "INSERT INTO tbl_icerik( " + " title, content, tags, create_date, user_id) " + " VALUES ('"
				+ t2.getTitle() + "', '" + t2.getContent() + "" + "','" + t2.getTag() + "'," + "'" + t2.getCreateDate()
				+ "'" + " ," + t2.getUserId() + ") ";
		int bb = stmt.executeUpdate(sql);

		return bb;
	}

	public boolean updateCell(Content selectedContent, String newValue, String oldValue) throws SQLException {
		
		System.out.println(selectedContent.getTag());
		DBProcessor db = new DBProcessor();

		db.connect();
		
		
		PreparedStatement pstmt = db.conn.prepareStatement("update tbl_icerik set title=?,content=? ,tags=? where id=?");

		pstmt.setString(1, selectedContent.getTitle());
		pstmt.setString(2, selectedContent.getContent());
		pstmt.setString(3, selectedContent.getTag());
		pstmt.setInt(4, selectedContent.getId());
		
		pstmt.executeUpdate();
		return true;
	}

	public int getAdminId(String kullaniciAd, String sifre) throws SQLException {
		
		DBProcessor db = new DBProcessor();

		db.connect();

		Statement stmt = db.conn.createStatement();

		String sql = "Select * from tbl_user where username='"+kullaniciAd+"' and password='"+sifre+"'";

		ResultSet set = stmt.executeQuery(sql);

		ArrayList<Content> liste = new ArrayList<Content>();

		User user = null;
		while (set.next()) {
			int deger = set.getInt("id");
			
			
			
			System.out.println(deger);
			return deger;
		}
		return 0;
	}

}
