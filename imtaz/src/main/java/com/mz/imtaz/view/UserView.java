package com.mz.imtaz.view;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.mz.imtaz.entity.RecordUtility;
import com.mz.imtaz.entity.User;
import com.mz.imtaz.entity.UserContext;
import com.mz.imtaz.repository.UserRepository;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@SpringView(name = UserView.NAME)
public class UserView extends VerticalLayout implements View {

	public static final String NAME = "UserView";
	
	@Autowired
	private UserRepository userRepo;
	
	@PostConstruct
    public void init() {
		headerSection();
		bodySection();
	}

	private void headerSection() {

		setCaption("<h3>Pendaftaran Pengguna</h3>");
		setCaptionAsHtml(true);
	}
	
	private void bodySection() {
		UserContext userContext = (UserContext)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		VerticalLayout mainLayout = new VerticalLayout();

        Button btnNew = new Button(VaadinIcons.PLUS);
        Button btnDelete = new Button(VaadinIcons.TRASH);
        Button btnRefresh = new Button(VaadinIcons.REFRESH);
        btnDelete.setEnabled(false);
        HorizontalLayout buttonBar = new HorizontalLayout(btnNew, btnDelete, btnRefresh);
        
        Grid<User> grid = new Grid<>();
        ListDataProvider<User> dataProvider = DataProvider.ofCollection(userRepo.findByEnabledOrderByFullname(true));
        grid.setDataProvider(dataProvider);
        grid.getEditor().setEnabled(true);
        grid.setSizeFull();
        
        TextField tfName = new TextField();
        tfName.setMaxLength(200);
        tfName.setRequiredIndicatorVisible(true);
        grid.addColumn(User::getFullname).setCaption("Nama")
        .setEditorComponent(tfName, User::setFullname)
        .setSortable(true);
        
        TextField tfUsername = new TextField();
        tfUsername.setMaxLength(50);
        tfUsername.setRequiredIndicatorVisible(true);
        grid.addColumn(User::getUsername).setCaption("Id Pengguna")
        .setEditorComponent(tfUsername, User::setUsername)
        .setSortable(true);

        PasswordField pfPassword = new PasswordField();
        pfPassword.setMaxLength(50);
        pfPassword.setRequiredIndicatorVisible(true);
        grid.addColumn(User::getPlainPassword).setCaption("Kata Laluan")
        .setEditorComponent(pfPassword, User::setPlainPassword);
        
        DateField dfExpiredDate = new DateField();
        dfExpiredDate.setValue(LocalDate.now().plus(1, ChronoUnit.YEARS));
        grid.addColumn(User::getExpiredDate).setCaption("Tarikh Luput")
        .setEditorComponent(dfExpiredDate, User::setExpiredDate);

        grid.getEditor().addSaveListener(evt -> {
        	try {
        		User item = evt.getBean();
        		item.setPassword(new BCryptPasswordEncoder().encode(item.getPlainPassword()));
        		item.setEnabled(true);
        		item.setIsLock(false);
        		item.setRecordUtility(new RecordUtility(userContext.getUser().getPkid()));
                userRepo.save(item);
                dataProvider.refreshAll();
            } catch (Exception e) {
                Notification.show("Rekod tidak berjaya dikemaskini.", Notification.Type.ERROR_MESSAGE);
            }
		});
        grid.addSelectionListener(evt -> {
        	if (evt.getFirstSelectedItem().isPresent()) {
                btnDelete.setEnabled(true);
            } else {
                btnDelete.setEnabled(false);
            }
        });

        btnDelete.addClickListener(evt -> {
        	try {
	        	if (!grid.getSelectedItems().isEmpty()) {
	        		User item = grid.getSelectedItems().iterator().next();
	        		item.getRecordUtility().disabled(null);
	                if(item.getPkid() != null)userRepo.save(item);
	                dataProvider.getItems().remove(item);
	                dataProvider.refreshAll();
	            }
        	}catch (Exception e) {
        		 Notification.show("Rekod tidak berjaya dipadam.", Notification.Type.ERROR_MESSAGE);
			}
        });

        btnNew.addClickListener(evt -> {
        	dataProvider.getItems().add(new User());
            dataProvider.refreshAll();
        });

        mainLayout.addComponent(buttonBar);
        mainLayout.addComponent(grid);
        addComponent(mainLayout);
	}
}
