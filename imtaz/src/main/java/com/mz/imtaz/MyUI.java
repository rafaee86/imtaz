package com.mz.imtaz;

import org.springframework.beans.factory.annotation.Value;
import org.vaadin.teemusa.sidemenu.SideMenu;

import com.mz.imtaz.entity.UserContext;
import com.mz.imtaz.util.Helper;
import com.mz.imtaz.view.CashFlowView;
import com.mz.imtaz.view.ChangePasswordView;
import com.mz.imtaz.view.ConfigureView;
import com.mz.imtaz.view.DailyActivityRecordView;
import com.mz.imtaz.view.DisciplineRecordView;
import com.mz.imtaz.view.MemorizeTargetView;
import com.mz.imtaz.view.PaymentView;
import com.mz.imtaz.view.RecordsHistoryView;
import com.mz.imtaz.view.RecordsRegisterView;
import com.mz.imtaz.view.ReportView;
import com.mz.imtaz.view.StudentRegisterView;
import com.mz.imtaz.view.UserView;
import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */

@SpringUI
@SpringViewDisplay
@Theme("mytheme")
public class MyUI extends UI implements ViewDisplay{

	private SideMenu sideMenu = new SideMenu();
    private final String menuCaption = "Sistem Imtaz";
    private final String mainTitle = "Sistem Pengurusan IMTAZ";

    @Value("${app.contextRoot}")
    private String contextRoot;
    
    @Override
    protected void init(VaadinRequest vaadinRequest) {
    	
    	ThemeResource logo = new ThemeResource("images/ImtazLogo-small.png");
//    	setIcon(new ThemeResource("images/ImtazLogo.ico"));
    	sideMenu.setMenuCaption(menuCaption, logo);
    	
    	setContent(sideMenu);
    	getPage().setTitle(mainTitle);

        sideMenu.setSizeFull();
        sideMenu.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        
        UserContext userContext = Helper.getUserContext();
        Boolean isAdmin = userContext != null && userContext.getUser() != null && userContext.getUser().getIsAdministrator() != null && userContext.getUser().getIsAdministrator();
        setUser(userContext != null ? userContext.getUsername() : "Anonymous", VaadinIcons.MALE, isAdmin);
        
        sideMenu.addNavigation("Dashboard", VaadinIcons.HOME, "");
        sideMenu.addNavigation("Pendaftaran Pelajar", VaadinIcons.FORM, StudentRegisterView.NAME);
        sideMenu.addNavigation("Pendaftaran Kelas", VaadinIcons.BUILDING, RecordsRegisterView.NAME);
        sideMenu.addNavigation("Kemasukan Target Hafazan", VaadinIcons.RECORDS, MemorizeTargetView.NAME);
        sideMenu.addNavigation("Rekod Harian Pelajar", VaadinIcons.RECORDS, DailyActivityRecordView.NAME);
        sideMenu.addNavigation("Rekod Disiplin Pelajar", VaadinIcons.EXCLAMATION_CIRCLE, DisciplineRecordView.NAME);
        sideMenu.addNavigation("Aliran Tunai", VaadinIcons.MONEY_WITHDRAW, CashFlowView.NAME);
        sideMenu.addNavigation("Bayaran", VaadinIcons.MONEY_DEPOSIT, PaymentView.NAME);
        sideMenu.addNavigation("Sejarah Pelajar", VaadinIcons.ARCHIVES, RecordsHistoryView.NAME);
        sideMenu.addNavigation("Laporan", VaadinIcons.NEWSPAPER, ReportView.NAME);
        if(isAdmin)
        	sideMenu.addNavigation("Penyelenggaraan", VaadinIcons.TOOLS, ConfigureView.NAME);
    }

	@Override
	public void showView(View view) {
		 sideMenu.setContent((Component) view);
	}
	
	private void setUser(String name, Resource icon, Boolean isAdmin) {
		sideMenu.setUserName(name);
		sideMenu.setUserIcon(icon);

		sideMenu.clearUserMenu();
		if(isAdmin)
	        sideMenu.addUserMenuItem("Daftar Pengguna", VaadinIcons.USER, () -> {
	        	getNavigator().navigateTo(UserView.NAME);
	        });
        sideMenu.addUserMenuItem("Kemaskini Kata Laluan", VaadinIcons.PASSWORD, () ->{
        	getNavigator().navigateTo(ChangePasswordView.NAME);
        });
        sideMenu.addUserMenuItem("Keluar", VaadinIcons.EXIT, () ->{
        	Page.getCurrent().setLocation(contextRoot + "logout");
        });
	}
}
