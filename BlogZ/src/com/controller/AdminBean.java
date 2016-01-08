package com.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.xml.bind.ParseConversionEvent;

import org.apache.catalina.deploy.LoginConfig;
import org.primefaces.component.api.UIColumn;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import com.DAO.AdminDAO;
import com.DAO.LocationDAO;
import com.model.Content;

import com.model.Location;

@ManagedBean(name = "adminBean")
@SessionScoped
public class AdminBean {

	private MapModel emptyModel;

	private Content content;
	
	private Content selectedContent;

	private String mesaj;

	private Location location;

	private ArrayList<Content> liste;

	public AdminBean() throws SQLException {

		
		selectedContent= new Content();
		
		emptyModel = new DefaultMapModel();

		content = new Content();

		liste = new ArrayList<Content>();

		location = new Location();

		listeDoldur();
	
	}

	public void listeDoldur() throws SQLException {
		AdminDAO dao = new AdminDAO();
		int deger1 = dao.getAdminId(String.valueOf(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userName")),
				String.valueOf(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userPassword")));
		System.out.println(deger1);
		
		
		liste = dao.listeDoldur(deger1);
	}

	public void addMessage(FacesMessage message) {

		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void kaydet() throws SQLException {

		AdminDAO dao = new AdminDAO();
		Date date = new Date();
		
		
		AdminDAO adminDAO = new AdminDAO();
		int deger1 = adminDAO.getAdminId(String.valueOf(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userName")),
				String.valueOf(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userPassword")));
		
		
		
		content.setUserId(deger1);
		content.setCreateDate(date.toLocaleString());

		int b = dao.kaydet(content);
		System.out.println("deger:"+b);
		if (b > 0) {

			LocationDAO locationDAO = new LocationDAO();
			int deger = locationDAO.kaydet(location);
			if (deger > 0) {
				setMesaj("KAYDEDÝLDÝ");
			} else {
				setMesaj("Fail!");
			}
		} else
			setMesaj("Fail!");

		saveMessage();

		content = new Content();

		liste = new ArrayList<Content>();

		listeDoldur();

	}

	public void saveMessage() {
		FacesContext context = FacesContext.getCurrentInstance();

		context.addMessage(null, new FacesMessage("Mesaj", getMesaj()));
	}

	public void addMarker() {
		Marker marker = new Marker(new LatLng(location.getEnlem(), location.getBoylam()), location.getDetail());
		emptyModel.addOverlay(marker);

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Marker Added", "Enlem:" + location.getEnlem()
						+ ", Boylam:" + location.getBoylam() + ", Location detail:" + location.getDetail()));
	}


	public ArrayList<Content> getListe() {
		return liste;
	}

	public void setListe(ArrayList<Content> liste) {
		this.liste = liste;
	}

	public String getMesaj() {
		return mesaj;
	}

	public void setMesaj(String mesaj) {
		this.mesaj = mesaj;
	}

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	public MapModel getEmptyModel() {
		return emptyModel;
	}

	public void setEmptyModel(MapModel emptyModel) {
		this.emptyModel = emptyModel;
	}
	
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Content getSelectedContent() {
		return selectedContent;
	}

	public void setSelectedContent(Content selectedContent) {
		this.selectedContent = selectedContent;
	}
	

	public void onCellEdit(CellEditEvent event) throws SQLException {
		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();
		int valueid = event.getRowIndex();
		UIColumn new123 = event.getColumn();
		
		AdminDAO adminDAO = new AdminDAO();
		adminDAO.updateCell(selectedContent,String.valueOf(newValue),String.valueOf(oldValue));

		if (newValue != null && !newValue.equals(oldValue)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed",
					"Old: " + oldValue + ", New:" + newValue);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}
	
}
