package com.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.DAO.YorumDAO;
import com.model.Comment;

@ManagedBean(name = "yorumBean")
public class YorumBean {

	private Comment comment;
	private String yazilanComment;
	private List<Comment> commentList;
	

	public YorumBean() throws SQLException {
		yazilanComment = new String();
		commentList=new ArrayList<Comment>();
		uygunCommentGetir();
	}

	public void commentEkle() throws SQLException {
		
		String iceriginYapilmaTarihi = String.valueOf(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("icerikOlusturmaTarihi"));
		String sistemdeUser =String.valueOf(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userName1"));
		String sistemdeUserPassword =String.valueOf(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userPassword1"));
		String icerikYazarAdi =String.valueOf(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("icerikYazarAdi"));
		String icerikYazarSoyadi =String.valueOf(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("icerikYazarSoyadi"));
		
		if (yazilanComment.equals("")) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Confirm ERROR", "Lutfen yoruma birþeyler ekleyin."));
		}
		else
		{
			YorumDAO yorumDAO = new YorumDAO();
			int sonuc=yorumDAO.yorumKaydet(icerikYazarAdi,icerikYazarSoyadi,sistemdeUser,sistemdeUserPassword,iceriginYapilmaTarihi,yazilanComment);
			if(sonuc>0)
			{
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage("Successful", "Yorum baþarýyla eklendi."));
				yazilanComment=null;
				
			}
			else
			{
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage("Confirm ERROR", "Sistemde bir hata olustu Lütfen tekrar yorumunuzu yazýn."));
			}
		
		}

	}

	
	public void uygunCommentGetir() throws SQLException
	{
	
		YorumDAO yorumDAO = new YorumDAO();
		int yazarid = yorumDAO.getYazarid(String.valueOf(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("icerikYazarAdi")), String.valueOf(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("icerikYazarSoyadi")));
		int contentid = yorumDAO.getContentid(yazarid,String.valueOf(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("icerikOlusturmaTarihi")));
		
		List<Comment> yorumlar =yorumDAO.getContentsComment(contentid);
		commentList = new ArrayList<Comment>();
		for (int i = 0; i <yorumlar.size(); i++) {
			comment = new Comment();
			this.comment.setAd(yorumlar.get(i).getAd());
			this.comment.setSoyad(yorumlar.get(i).getSoyad());
			this.comment.setCreateDate(yorumlar.get(i).getCreateDate());
			this.comment.setComment(yorumlar.get(i).getComment());
			
			commentList.add(comment);
		}
	}
	
	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}

	public String getYazilanComment() {
		return yazilanComment;
	}

	public void setYazilanComment(String yazilanComment) {
		this.yazilanComment = yazilanComment;
	}

	public List<Comment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}
}
