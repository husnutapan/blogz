package com.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.db.DBProcessor;
import com.model.Comment;
import com.model.Content;
import com.model.User;

public class YorumDAO {

	public int yorumKaydet(String icerikYazarAdi, String icerikYazarSoyadi, String sistemdeUser,
			String sistemdeUserPassword, String iceriginYapilmaTarihi, String yazilanComment) throws SQLException {

		int userid = getUserid(sistemdeUser, sistemdeUserPassword);
		int yazarid = getYazarid(icerikYazarAdi, icerikYazarSoyadi);
		int contentid = getContentid(yazarid, iceriginYapilmaTarihi);

		Date date = new Date();

		DBProcessor db = new DBProcessor();
		db.connect();

		String sql = "insert into tbl_comment(comment,create_date,content_id,user_id) values('" + yazilanComment + "','"
				+ date.toString() + "'," + "'" + contentid + "'," + userid + ")";

		Statement stmt = db.conn.createStatement();
		int sonuc = stmt.executeUpdate(sql);

		return sonuc;

	}

	public int getContentid(int yazarid, String iceriginYapilmaTarihi) throws SQLException {

		DBProcessor db = new DBProcessor();

		db.connect();

		Statement stmt = db.conn.createStatement();

		String sql = "Select * from tbl_icerik" + " where user_id='" + yazarid + "' " + "and create_date='"
				+ iceriginYapilmaTarihi + "'";
		int deger;
		ResultSet set = stmt.executeQuery(sql);
		User user = null;
		while (set.next()) {
			deger = set.getInt("id");
			return deger;
		}
		return 0;
	}

	public int getYazarid(String icerikYazarAdi, String icerikYazarSoyadi) throws SQLException {
		DBProcessor db = new DBProcessor();

		db.connect();

		Statement stmt = db.conn.createStatement();

		String sql = "Select * from tbl_user" + " where adi='" + icerikYazarAdi + "' " + "and soyadi='"
				+ icerikYazarSoyadi + "'";
		int deger;
		ResultSet set = stmt.executeQuery(sql);
		User user = null;
		while (set.next()) {
			deger = set.getInt("id");
			return deger;
		}
		return 0;
	}

	public int getUserid(String sistemdeUser, String sistemdeUserPassword) throws SQLException {

		DBProcessor db = new DBProcessor();

		db.connect();

		Statement stmt = db.conn.createStatement();

		String sql = "Select * from tbl_user" + " where username='" + sistemdeUser + "' " + "and password='"
				+ sistemdeUserPassword + "'";
		int deger;
		ResultSet set = stmt.executeQuery(sql);
		User user = null;
		while (set.next()) {
			deger = set.getInt("id");
			return deger;
		}

		return 0;
	}

	public List<Comment> getContentsComment(int contentid) throws SQLException {
		DBProcessor db = new DBProcessor();

		db.connect();

		Statement stmt = db.conn.createStatement();

		String sql = "SELECT comment,create_date ,adi,soyadi FROM tbl_comment tc "
				+ "inner join tbl_user tu on tc.user_id=tu.id where tc.content_id=" + contentid + "";

		ResultSet set = stmt.executeQuery(sql);

		ArrayList<Comment> liste = new ArrayList<Comment>();

		while (set.next()) {
			Comment comment = new Comment();
			comment.setAd(set.getString("adi"));
			comment.setSoyad(set.getString("soyadi"));
			comment.setCreateDate(set.getString("create_date"));
			comment.setComment(set.getString("comment"));

			liste.add(comment);
		}
		return liste;
	}
}
