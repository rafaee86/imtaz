package com.mz.imtaz.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import com.mz.imtaz.entity.ReportHeader;
import com.mz.imtaz.entity.ReportItem;
import com.mz.imtaz.repository.ReportHeaderRepository;
import com.mz.imtaz.repository.ReportItemRepository;
import com.vaadin.navigator.View;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = ReportView.NAME)
public class ReportView extends VerticalLayout implements View {

	public static final String NAME = "ReportView";
	
	@Autowired
	private ReportHeaderRepository reportHeaderRepo;
	
	@Autowired
	private ReportItemRepository reportItemRepo;

	@PostConstruct
    public void init() {
		Panel panel = new Panel(); 
		VerticalLayout layout = new VerticalLayout();
		
		headerSection(panel);
		bodySection(layout);
		
		panel.setContent(layout);
		addComponent(panel);
	}

	private void headerSection(Panel panel) {

		panel.setCaption("<h3>Laporan</h3>");
		panel.setCaptionAsHtml(true);
		panel.setDescription("Skrin untuk kemasukan target hafazan bulanan. Sila tekan butang Tambah untuk menambah rekod baru.");
	}

	private void bodySection(VerticalLayout layout) {
		
		List<ReportHeader> reportHeaderList = Optional.ofNullable(reportHeaderRepo.findByActive(Boolean.TRUE, Sort.by(Sort.Direction.ASC, "level"))).orElse(new ArrayList<ReportHeader>());
		Accordion accord = new Accordion();
		accord.setHeight(100.0f, Unit.PERCENTAGE);
 
        for (ReportHeader reportHeader : reportHeaderList) {
        	accord.setWidth(100.0f, Unit.PERCENTAGE); 
            VerticalLayout bodyLayout = new VerticalLayout();
            bodyLayout.setMargin(true); 
            
            List<ReportItem> reportItemList = Optional.ofNullable(reportItemRepo.findByReportHeaderAndActive(reportHeader, Boolean.TRUE, Sort.by(Sort.Direction.ASC, "level"))).orElse(new ArrayList<ReportItem>());
            for (ReportItem reportItem : reportItemList) {
            	bodyLayout.addComponent(new Link(reportItem.getName(), new ExternalResource(reportItem.getUrl()), "_blank", 0, 0, BorderStyle.DEFAULT));
            }
            accord.addTab(bodyLayout, reportHeader.getName());
        }
        
        layout.addComponent(accord);
	}

}
