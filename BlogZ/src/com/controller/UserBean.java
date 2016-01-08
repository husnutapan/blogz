package com.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.websocket.Session;

import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import com.DAO.UserDAO;
import com.model.Content;
import com.model.Location;
import com.model.MapYazarlar;
import com.model.User;

@ManagedBean(name = "userBean")
@SessionScoped
public class UserBean {

	private List<DefaultMapModel> multiModel;
	private MapModel simpleModel;
	private Marker marker;
	private MapYazarlar mapYazarlar;
	private boolean yorumYazilabilirmi;
	
	
	private Content content;
	
	private User user;
	private User user1;
	private User user2;

	public UserBean() throws SQLException {
		mapYazarlar = new MapYazarlar();
		content = new Content();
		multiModel = new ArrayList<DefaultMapModel>();
		simpleModel = new DefaultMapModel();
		user = new User();
		user1 = new User();
		user2 = new User();
		
		getMapsMarker();
		
	}

	public String login() throws SQLException {

		UserDAO dao = new UserDAO();

		boolean islogin = dao.isLogin(user1);
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().put("userName1",user1.getUsername());
		context.getExternalContext().getSessionMap().put("userPassword1",user1.getPassword());

		if (islogin) {
			return "Kullanici?faces-redirect=true";
		} else {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Confirm ERROR", "Hatalý Giriþ Tekrar Deneyiniz."));
			user1.setUsername(null);
			user1.setPassword(null);
			return "login";
		}

	}

	public String login1() throws SQLException {

		UserDAO dao = new UserDAO();

		
		//sessionMap
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().put("userName",user2.getUsername());
		context.getExternalContext().getSessionMap().put("userPassword",user2.getPassword());
		
		
		boolean islogin = dao.isLogin(user2);
		if (islogin) {
			return "Admin?faces-redirect=true";
		} else {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Confirm ERROR", "Hatalý Giriþ Tekrar Deneyiniz."));
			user2.setUsername(null);
			user2.setPassword(null);
			return "login";
		}

	}

	public void userKaydet() throws SQLException {
		UserDAO userDAO = new UserDAO();
		userDAO.userKaydet(user);
	}

	public void validatePassword(ComponentSystemEvent event) throws SQLException {

		FacesContext fc = FacesContext.getCurrentInstance();

		UIComponent components = event.getComponent();

		// get password
		UIInput uiInputName = (UIInput) components.findComponent("name");
		String name = uiInputName.getLocalValue() == null ? "" : uiInputName.getLocalValue().toString();
		UIInput uiInputSurname = (UIInput) components.findComponent("surname");
		String surname = uiInputSurname.getLocalValue() == null ? "" : uiInputSurname.getLocalValue().toString();
		UIInput uiInputUsername = (UIInput) components.findComponent("username");
		String username = uiInputUsername.getLocalValue() == null ? "" : uiInputUsername.getLocalValue().toString();
		UIInput uiInputPassword = (UIInput) components.findComponent("password");
		String password = uiInputPassword.getLocalValue() == null ? "" : uiInputPassword.getLocalValue().toString();

		// get confirm password
		UIInput uiInputConfirmPassword = (UIInput) components.findComponent("confirmPassword");
		String confirmPassword = uiInputConfirmPassword.getLocalValue() == null ? ""
				: uiInputConfirmPassword.getLocalValue().toString();

		// Let required="true" do its job.
		if (password.isEmpty() || confirmPassword.isEmpty()) {
			return;
		}
		if (!password.equals(confirmPassword)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Confirm ERROR",
					"Þifre ile Validator eþleþmiyor veya boþ alanlarý doldurunuz!!!"));
			fc.renderResponse();
		}

		if (password.equals(confirmPassword) && name != null && username != null && surname != null
				&& password != null) {
			user.setAdi(name);
			user.setSoyadi(surname);
			user.setUsername(username);
			user.setPassword(password);
			userKaydet();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Teþekkürler", "Sisteme Baþarý Ýle Kayýt Oldunuz."));
			fc.renderResponse();
		}

	}

	public void getMapsMarker() throws SQLException {

		UserDAO userDAO = new UserDAO();
		List<Location> markerList = userDAO.gmapMarker();
		for (int i = 0; i < markerList.size(); i++) {
			LatLng coord1  = new LatLng(markerList.get(i).getEnlem(), markerList.get(i).getBoylam());
			simpleModel.addOverlay(new Marker(coord1, markerList.get(i).getDetail()));
			
			multiModel.add(i,(DefaultMapModel) simpleModel);
			
		}
		LatLng coord1 = new LatLng(markerList.get(0).getEnlem(), markerList.get(0).getBoylam());
		simpleModel.addOverlay(new Marker(coord1, markerList.get(0).getDetail()));

	}

	public void onMarkerSelect(OverlaySelectEvent event) throws SQLException {
        marker = (Marker) event.getOverlay();
        UserDAO userDAO= new UserDAO();
        mapYazarlar=userDAO.getMapBilgiler(marker.getTitle().toString());
        yorumYazilabilirmi=true;
        
     
        FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().put("icerikOlusturmaTarihi",mapYazarlar.getOlusturulmaTarihi());
		context.getExternalContext().getSessionMap().put("icerikYazarAdi",mapYazarlar.getYazarAd());
		context.getExternalContext().getSessionMap().put("icerikYazarSoyadi",mapYazarlar.getYazarSoyad());
		
		YorumBean bean = new YorumBean();
		
    }
	
	public String logout()
	{
		 user=null;
		 user1 = null;
		 user2 = null;
		return "login.xhtml?faces-redirect=true";
	}

	
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser1() {
		return user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;
	}

	public User getUser2() {
		return user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;
	}

	public MapModel getSimpleModel() {
		return simpleModel;
	}

	public void setSimpleModel(MapModel simpleModel) {
		this.simpleModel = simpleModel;
	}

	public List<DefaultMapModel> getMultiModel() {
		return multiModel;
	}

	public void setMultiModel(List<DefaultMapModel> multiModel) {
		this.multiModel = multiModel;
	}

	public Marker getMarker() {
		return marker;
	}

	public void setMarker(Marker marker) {
		this.marker = marker;
	}

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	public MapYazarlar getMapYazarlar() {
		return mapYazarlar;
	}

	public void setMapYazarlar(MapYazarlar mapYazarlar) {
		this.mapYazarlar = mapYazarlar;
	}

	public boolean isYorumYazilabilirmi() {
		return yorumYazilabilirmi;
	}

	public void setYorumYazilabilirmi(boolean yorumYazilabilirmi) {
		this.yorumYazilabilirmi = yorumYazilabilirmi;
	}
	
	
	
	
}
