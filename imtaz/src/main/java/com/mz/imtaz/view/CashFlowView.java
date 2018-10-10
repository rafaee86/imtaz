package com.mz.imtaz.view;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.vaadin.ui.NumberField;

import com.mz.imtaz.entity.CashFlow;
import com.mz.imtaz.entity.RecordUtility;
import com.mz.imtaz.repository.CashFlowRepository;
import com.mz.imtaz.repository.RecordsHistoryRepository;
import com.mz.imtaz.util.Helper;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.converter.StringToBigDecimalConverter;
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
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = CashFlowView.NAME)
public class CashFlowView extends VerticalLayout implements View {

	public static final String NAME = "CashFlowView";
	private final static float WIDTH = 500f;
	private final static String ERROR_BIGDECIMAL = "Format amaun tidak betul, mesti xxx,xxx.00 dimana x ialah nombor.";

	private ListDataProvider<CashFlow> dataProvider;

	@Autowired
	private CashFlowRepository cashFlowRepo;

	@PostConstruct
    public void init() {
		headerSection();
		bodySection();
	}

	private void headerSection() {

		setCaption("<h3>Aliran Tunai</h3>");
		setCaptionAsHtml(true);
		setDescription("Skrin untuk kemasukan target hafazan bulanan. Sila tekan butang Tambah untuk menambah rekod baru.");
	}

	private void bodySection() {

		Button btnNew = new Button(VaadinIcons.PLUS);
		Button btnDelete = new Button(VaadinIcons.TRASH);
		btnDelete.setEnabled(false);
        HorizontalLayout buttonBar = new HorizontalLayout(btnNew, btnDelete);

        DateField dfDate = new DateField();
        dfDate.setValue(LocalDate.now());
        dfDate.setRangeEnd(LocalDate.now());
        dfDate.setDateFormat("dd/MM/yyyy");

        Button btnRefresh = new Button(VaadinIcons.REFRESH);

        Grid<CashFlow> grid = new Grid<CashFlow>();
		dataProvider = DataProvider.ofCollection(new ArrayList<CashFlow>());
	    grid.setDataProvider(dataProvider);
		grid.setEnabled(true);
		grid.getEditor().setEnabled(true);
		grid.setSizeFull();
		grid.setHeightUndefined();

		TextField tfDescription = new TextField();
		tfDescription.setRequiredIndicatorVisible(true);
		tfDescription.setWidth(WIDTH, Unit.PIXELS);
		tfDescription.setMaxLength(200);
		grid
			.addColumn(CashFlow::getDescription).setCaption("Perihal Barang")
			.setEditorComponent(tfDescription, CashFlow::setDescription)
			.setSortable(true);

		NumberField tfAmount = new NumberField();
		tfAmount.setValue("0");
		tfAmount.setRequiredIndicatorVisible(true);
		tfAmount.setWidth(WIDTH, Unit.PIXELS);
		tfAmount.setMaxLength(10);
		tfAmount.setMinimumFractionDigits(2);
		tfAmount.setNegativeAllowed(false);
		tfAmount.setGroupingSeparator(',');
		grid
			.addColumn(CashFlow::getAmount, item -> item != null ? item.toPlainString() : "0.00").setCaption("Amaun Harga(RM)")
			.setEditorBinding(grid.getEditor().getBinder()
	        		.forField(tfAmount)
	        		.withValidator((s,a) -> {
	        			try {
	        				new BigDecimal(s);
	        				return ValidationResult.ok();
	        			}catch (Exception e) {
							return ValidationResult.error(ERROR_BIGDECIMAL);
						}
	        		})
	        		.withNullRepresentation("")
	    	    	.withConverter(new StringToBigDecimalConverter(ERROR_BIGDECIMAL))
	    	    	.bind(CashFlow::getAmount, CashFlow::setAmount))
			.setSortable(true);

		grid.getEditor().addSaveListener(evt -> {
        	try {
        		CashFlow item = evt.getBean();
        		item.setRecordUtility(new RecordUtility());
        		cashFlowRepo.save(item);
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
	                CashFlow item = grid.getSelectedItems().iterator().next();
	                item.getRecordUtility().disabled();
	                if(item.getPkid() != null)cashFlowRepo.save(item);
	                dataProvider.getItems().remove(item);
	                dataProvider.refreshAll();
	            }
        	}catch (Exception e) {
        		 Notification.show("Rekod tidak berjaya dipadam.", Notification.Type.ERROR_MESSAGE);
			}
        });

        btnRefresh.addClickListener(listener -> {
        	LocalDate dateValue = dfDate.getValue();
        	if(dateValue != null) {
        		List<CashFlow> cashFlowList = cashFlowRepo.findByTransactionDate(Helper.localDateToDate(dateValue), Sort.by(Sort.Direction.ASC, "pkid"));
        		dataProvider.getItems().clear();
        		dataProvider.getItems().addAll(cashFlowList);
        		dataProvider.refreshAll();
        	}
        });

        btnNew.addClickListener(evt -> {
        	CashFlow cashFlow = new CashFlow();
        	if(dfDate.getValue() != null)
        		cashFlow.setTransactionDate(Helper.localDateToDate(dfDate.getValue()));
        	else {
        		cashFlow.setTransactionDate(new Date());
        	}
        	dataProvider.getItems().add(cashFlow);
            dataProvider.refreshAll();
        });

        HorizontalLayout hlDate = new HorizontalLayout(dfDate, btnRefresh);
        hlDate.setCaption("Tarikh Transaksi");
        addComponent(hlDate);
		addComponent(buttonBar);
		addComponent(grid);
	}
}
