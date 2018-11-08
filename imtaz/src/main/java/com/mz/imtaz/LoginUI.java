package com.mz.imtaz;

import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringUI(path = "/login")
public class LoginUI extends UI {
	
	@Override
	protected void init(VaadinRequest request) {
		
		Label label = new Label(html, ContentMode.HTML);
		label.setWidth("100%");
		VerticalLayout layout = new VerticalLayout(label);
		layout.setSizeFull();
		setContent(layout);
	}

	private String html = 
			"<table align='center' border=1 style='width:800px;height:600px;'><tr><td style='width:70%;height:60%;' align='center'>" + 
			"<table border=1 ><tr><td>" + 
			"<form action='/perform_login' method='POST'>" + 
			"<table><tbody><tr><td>Id Pengguna:</td><td><input type='text' name='myusername' ></td></tr>" + 
			"<tr><td>Kata Laluan:</td><td><input type='password' name='mypassword'></td></tr>" + 
			"<tr><td colspan='2'><input name='submit' type='submit' value='Login'></td></tr>" + 
			"</tbody></table>" + 
			"</form>" + 
			"</td></tr></table></td></tr></table>";
	
}
