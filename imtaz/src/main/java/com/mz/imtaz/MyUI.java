package com.mz.imtaz;

import org.vaadin.teemusa.sidemenu.SideMenu;

import com.mz.imtaz.view.CashFlowView;
import com.mz.imtaz.view.ConfigureView;
import com.mz.imtaz.view.DailyActivityRecordView;
import com.mz.imtaz.view.DiciplineRecordView;
import com.mz.imtaz.view.MemorizeTargetView;
import com.mz.imtaz.view.PaymentView;
import com.mz.imtaz.view.RecordsHistoryView;
import com.mz.imtaz.view.RecordsRegisterView;
import com.mz.imtaz.view.ReportView;
import com.mz.imtaz.view.StudentRegisterView;
import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.ClassResource;
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
    private final String menuCaption = "<h1>Sistem Imtaz</h1>";
    private final String mainTitle = "Sistem Pengurusan IMTAZ";

    @Override
    protected void init(VaadinRequest vaadinRequest) {

    	setIcon(new ClassResource("images/ImtazLogo.ico"));
    	setContent(sideMenu);
    	getPage().setTitle(mainTitle);

        sideMenu.setSizeFull();
        sideMenu.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        sideMenu.setCaptionAsHtml(true);
        sideMenu.setCaption(menuCaption);
        sideMenu.addNavigation("Dashboard", VaadinIcons.HOME, "");
        sideMenu.addNavigation("Pendaftaran Pelajar", VaadinIcons.FORM, StudentRegisterView.NAME);
        sideMenu.addNavigation("Pendaftaran Kelas", VaadinIcons.BUILDING, RecordsRegisterView.NAME);
        sideMenu.addNavigation("Kemasukan Target Hafazan", VaadinIcons.RECORDS, MemorizeTargetView.NAME);
        sideMenu.addNavigation("Rekod Harian Pelajar", VaadinIcons.RECORDS, DailyActivityRecordView.NAME);
        sideMenu.addNavigation("Rekod Disiplin Pelajar", VaadinIcons.EXCLAMATION_CIRCLE, DiciplineRecordView.NAME);
        sideMenu.addNavigation("Aliran Tunai", VaadinIcons.MONEY_WITHDRAW, CashFlowView.NAME);
        sideMenu.addNavigation("Bayaran", VaadinIcons.MONEY_DEPOSIT, PaymentView.NAME);
        sideMenu.addNavigation("Sejarah Pelajar", VaadinIcons.ARCHIVES, RecordsHistoryView.NAME);
        sideMenu.addNavigation("Laporan", VaadinIcons.NEWSPAPER, ReportView.NAME);
        sideMenu.addNavigation("Penyelenggaraan", VaadinIcons.TOOLS, ConfigureView.NAME);
    }

	@Override
	public void showView(View view) {
		 sideMenu.setContent((Component) view);
	}
}
