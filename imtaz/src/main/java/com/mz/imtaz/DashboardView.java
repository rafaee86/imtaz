package com.mz.imtaz;

import javax.annotation.PostConstruct;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = "")
public class DashboardView extends VerticalLayout implements View {

	@PostConstruct
    public void init() {
        Label title = new Label("Dashboard");
        title.setStyleName("h2");
        addComponent(title);
	}
}
