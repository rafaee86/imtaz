package com.mz.imtaz;

import org.vaadin.teemusa.sidemenu.SideMenu;

import com.mz.imtaz.view.ConfigureView;
import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
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
@Theme("valo")
public class MyUI extends UI implements ViewDisplay{

	private SideMenu sideMenu = new SideMenu();
    private final String menuCaption = "<h1>Sistem Imtaz</h1>";

    @Override
    protected void init(VaadinRequest vaadinRequest) {

    	setContent(sideMenu);
        sideMenu.setSizeFull();
        sideMenu.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        sideMenu.setCaptionAsHtml(true);
        sideMenu.setCaption(menuCaption);
        sideMenu.addNavigation("Dashboard", VaadinIcons.HOME, "");
        sideMenu.addNavigation("Pendaftaran Pelajar", VaadinIcons.FORM, "");
        sideMenu.addNavigation("Pendaftaran Kelas", VaadinIcons.BUILDING, "");
        sideMenu.addNavigation("Kemasukan Rekod Markah Pelajar", VaadinIcons.RECORDS, "");
        sideMenu.addNavigation("Aliran Tunai", VaadinIcons.MONEY_WITHDRAW, "");
        sideMenu.addNavigation("Pendaftaran Pelajar Yaman", VaadinIcons.GLOBE, "");
        sideMenu.addNavigation("Tetapan", VaadinIcons.TOOLS, ConfigureView.NAME);
    }

	@Override
	public void showView(View view) {
		 sideMenu.setContent((Component) view);
	}
}
