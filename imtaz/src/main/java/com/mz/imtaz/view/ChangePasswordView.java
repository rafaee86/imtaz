package com.mz.imtaz.view;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.vaadin.dialogs.ConfirmDialog;

import com.mz.imtaz.entity.RecordUtility;
import com.mz.imtaz.entity.User;
import com.mz.imtaz.entity.UserContext;
import com.mz.imtaz.repository.UserRepository;
import com.mz.imtaz.util.Helper;
import com.vaadin.data.Binder;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@SpringView(name = ChangePasswordView.NAME)
public class ChangePasswordView extends VerticalLayout implements View {

	public static final String NAME = "ChangePasswordView";
	
	@Autowired
	private UserRepository userRepo;
	
	@PostConstruct
    public void init() {
		headerSection();
		bodySection();
	}

	private void headerSection() {

		setCaption("<h3>Kemaskini KataLaluan</h3>");
		setCaptionAsHtml(true);
	}
	
	private void bodySection() {
		UserContext userContext = Helper.getUserContext();
		if(userContext == null || (userContext != null && userContext.getUser() == null)) {
			Notification.show("Data pengguna tidak wujud.", Type.HUMANIZED_MESSAGE);
			return;
		}
		VerticalLayout mainLayout = new VerticalLayout();

        Button btnEdit = new Button("Kemaskini");
        
        FormLayout formLayout = new FormLayout();
        User user = !(userContext.getUser().getPkid() == null || "Administrator".equals(userContext.getUser().getUsername())) ? userRepo.findById(userContext.getPkid()).orElse(null) : userContext.getUser();
        Binder<User> binder = new Binder<>();
        binder.setBean(user);
        
        TextField tfName = new TextField("Nama Penuh");
        tfName.setRequiredIndicatorVisible(true);
        binder
	    	.forField(tfName)
	    	.withValidator(input -> input != null && !input.isEmpty(), "Sila masukkan nama penuh.")
	    	.bind(User::getFullname, User::setFullname);
        
        PasswordField pfPassword = new PasswordField("Kata Laluan");
        pfPassword.setRequiredIndicatorVisible(true);
        binder
	    	.forField(pfPassword)
	    	.withValidator(input -> input != null && !input.isEmpty(), "Sila masukkan kata laluan.")
	    	.bind(User::getPlainPassword, User::setPlainPassword);
        
        PasswordField pfPasswordVerify = new PasswordField("Kata Laluan Pengesahan");
        pfPasswordVerify.setRequiredIndicatorVisible(true);
        pfPasswordVerify.setValue(user.getPlainPassword());
        
        CheckBox cbIsAdmin = new CheckBox("Pentadbir?");
        binder
	        .forField(cbIsAdmin)
	    	.bind(User::getIsAdministrator, User::setIsAdministrator);
        
        btnEdit.addClickListener(evt -> {
        	if(pfPassword.getValue().equals(pfPasswordVerify.getValue())) {
        		Boolean isValid = binder.writeBeanIfValid(user);
            	if(isValid != null && isValid) {
            		user.setPassword(new BCryptPasswordEncoder().encode(user.getPlainPassword()));
            		user.setRecordUtility(new RecordUtility());
            		User savedUser = userRepo.save(user);
            		if(savedUser != null) {
            			ConfirmDialog.show(getUI(), "Notifikasi", "Kemaskini maklumat pengguna telah berjaya. Sistem akan daftar keluar.", "Ok", "Batal", listener -> {
            				Page.getCurrent().setLocation("/logout");
            			});
            		}
            	}
        	}else {
        		Notification.show("Kata laluan tidak sama.", Type.WARNING_MESSAGE);
        	}
        });
        
        formLayout.addComponent(tfName);
        formLayout.addComponent(pfPassword);
        formLayout.addComponent(pfPasswordVerify);
        formLayout.addComponent(cbIsAdmin);
        
        mainLayout.addComponent(formLayout);
        mainLayout.addComponent(new HorizontalLayout(btnEdit));
        addComponent(mainLayout);
	}
}
