package com.mz.imtaz.view;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.mz.imtaz.entity.ClassRoom;
import com.mz.imtaz.entity.ClassRoomDetail;
import com.mz.imtaz.entity.Juzuk;
import com.mz.imtaz.entity.MemorizeTarget;
import com.mz.imtaz.entity.RecordUtility;
import com.mz.imtaz.entity.Records;
import com.mz.imtaz.entity.Student;
import com.mz.imtaz.repository.ClassRoomDetailRepository;
import com.mz.imtaz.repository.MemorizeTargetRepository;
import com.mz.imtaz.repository.RecordsHistoryRepository;
import com.mz.imtaz.repository.RecordsRepository;
import com.mz.imtaz.repository.StudentRepository;
import com.mz.imtaz.util.Helper;
import com.vaadin.addon.pagination.Pagination;
import com.vaadin.addon.pagination.PaginationResource;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SpringView(name = MemorizeTargetView.NAME)
public class MemorizeTargetView extends VerticalLayout implements View {

	public static final String NAME = "MemorizeTargetView";
	private final static float WIDTH = 500f;
	private final static float JUZUK_WIDTH = 80f;

	final int page = 0;
	final int limit = 10;
	final int targetPage = 0;
	final int targetLimit = 10;

	private ListDataProvider<MemorizeTarget> dataProvider;
	private Long total = 0L;

	@Autowired
	private MemorizeTargetRepository targetRepo;
	@Autowired
	private RecordsRepository recordsRepo;
	@Autowired
	private StudentRepository studentRepo;
	@Autowired
	private ClassRoomDetailRepository classRoomDetailRepo;
	@Autowired
	private RecordsHistoryRepository recordsHistoryRepository;

	@PostConstruct
    public void init() {
		headerSection();
		bodySection();
	}

	private void headerSection() {

		setCaption("<h3>Kemasukan Target Hafazan</h3>");
		setCaptionAsHtml(true);
	}

	private void bodySection() {

		Button btnNew = new Button(VaadinIcons.PLUS);
		Button btnDelete = new Button(VaadinIcons.TRASH);
		btnDelete.setEnabled(false);
        HorizontalLayout buttonBar = new HorizontalLayout(btnNew, btnDelete);

        ComboBox<ClassRoomDetail> cbClassRoomDetail = new ComboBox<>("Kelas");
        cbClassRoomDetail.setWidth(WIDTH, Unit.PIXELS);
        cbClassRoomDetail.setItems(classRoomDetailRepo.findByClassRoom(ClassRoom.HAFAZAN));
        cbClassRoomDetail.setItemCaptionGenerator(item -> item.getClassRoom().getDescription() + " " + item.getName() + " - " + item.getTeacher().getSalutation() + " " + item.getTeacher().getName());
        cbClassRoomDetail.setEmptySelectionAllowed(false);

        ComboBox<Student> cbStudent = new ComboBox<>("Pelajar");
        cbStudent.setWidth(WIDTH, Unit.PIXELS);
        cbStudent.setItemCaptionGenerator(item -> item.getName());
        cbStudent.setEmptySelectionAllowed(false);

        cbClassRoomDetail.addSelectionListener(listener -> {
        	if(Helper.notNull(listener.getSelectedItem()) != null)
        		cbStudent.setItems(studentRepo.findByClassRoomDetail(listener.getSelectedItem().get()));
        });

		Grid<MemorizeTarget> grid = new Grid<MemorizeTarget>();
		dataProvider = DataProvider.ofCollection(new ArrayList<MemorizeTarget>());
	    grid.setDataProvider(dataProvider);
		grid.setEnabled(true);
		grid.getEditor().setEnabled(true);
		grid.setSizeFull();
		grid.setHeightUndefined();

		grid.addColumn(MemorizeTarget::getDailyTarget, item ->( item != null ? "Juz " + item.getJuz() + " Muka " + item.getPage() + " Baris " + item.getLine() : ""))
		.setCaption("Target Harian");

		grid.addColumn(MemorizeTarget::getStartPage)
		.setCaption("M/S Mula");

		grid.addColumn(MemorizeTarget::getLastPage)
		.setCaption("M/S Akhir");

		grid.addColumn(MemorizeTarget::getTotalDailyTarget, item ->( item != null ? "Juz " + item.getJuz() + " Muka " + item.getPage() + " Baris " + item.getLine() : ""))
		.setCaption("Jumlah Target");

		grid.addColumn(MemorizeTarget::getTotalMemorize, item ->( item != null ? "Juz " + item.getJuz() + " Muka " + item.getPage() + " Baris " + item.getLine() : ""))
		.setCaption("Jumlah Hafazan");

		grid.addColumn(MemorizeTarget::getTotalBalance, item ->( item != null ? "Juz " + item.getJuz() + " Muka " + item.getPage() + " Baris " + item.getLine() : ""))
		.setCaption("Baki");

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
	                MemorizeTarget item = grid.getSelectedItems().iterator().next();
	                item.getRecordUtility().disabled(null);
	                if(item.getPkid() != null) {
	                	targetRepo.save(item);
	            		Helper.setRecordsHistory(
	            			recordsHistoryRepository, 
	            			"Memadam Target Hafazan Bulanan.", 
	            			Helper.notNull(item.getRecords().getStudent().getPkid()),
	            			Helper.notNull(item.getRecords().getClassRoomDetail().getClassRoom().getDescription()) + " - " + 
	                        Helper.notNull(item.getRecords().getClassRoomDetail().getName())
	            		);
	                }
	                dataProvider.getItems().remove(item);
	                dataProvider.refreshAll();
	            }
        	}catch (Exception e) {
        		 Notification.show("Rekod tidak berjaya dipadam.", Notification.Type.ERROR_MESSAGE);
			}
        });

        grid.addSelectionListener(listener -> {
        	MemorizeTarget target = Helper.notNull(listener.getFirstSelectedItem()) != null ? listener.getFirstSelectedItem().get() : null;
        	if(target != null) {
        		createWindow(target, false);
        	}
        });

        cbStudent.addSelectionListener(listener -> {
        	ClassRoomDetail classRoomDetail = Helper.notNull(cbClassRoomDetail.getSelectedItem()) != null ? cbClassRoomDetail.getSelectedItem().get() : null;
        	Student student = Helper.notNull(listener.getSelectedItem()) != null ? listener.getSelectedItem().get() : null;

        	if(classRoomDetail != null && student != null) {
				List<MemorizeTarget> targetList = targetRepo.findByClassRoomDetail(classRoomDetail, student);
				List<MemorizeTarget> subTargetList = targetList != null && targetList.size() > limit ? targetList.subList(0, limit) : targetList;
				total = Long.valueOf(targetList != null ? targetList.size() : 0);
		        dataProvider = DataProvider.ofCollection(subTargetList != null ? subTargetList : new ArrayList<MemorizeTarget>());
		        grid.setDataProvider(dataProvider);
			}
		});

		Pagination pagination = new Pagination(PaginationResource.newBuilder().setTotal(total).setPage(page).setLimit(limit).build());
	    pagination.setItemsPerPage(10, 20, 50, 100);
		pagination.addPageChangeListener(event -> {
			Pageable pageable = PageRequest.of(event.pageIndex(), event.limit());
			List<MemorizeTarget> pageTargetAllList = null;
			ClassRoomDetail classRoomDetail = cbClassRoomDetail.getSelectedItem() != null && cbClassRoomDetail.getSelectedItem().get() != null ? cbClassRoomDetail.getSelectedItem().get() : null;
        	Student student = Helper.notNull(cbStudent.getSelectedItem()) != null ? cbStudent.getSelectedItem().get() : null;
			if(classRoomDetail != null && student != null)
				pageTargetAllList = targetRepo.findByClassRoomDetail(classRoomDetail, student);
			Long totalAll = Long.valueOf(pageTargetAllList != null ? pageTargetAllList.size() : 0);
			List<MemorizeTarget> pageRecordsSubList = null;
			if(classRoomDetail != null && student != null)
				pageRecordsSubList = targetRepo.findByClassRoomDetailPageable(classRoomDetail, student, pageable);
			pagination.setTotalCount(totalAll);
			grid.setItems(pageRecordsSubList);
		});

        btnNew.addClickListener(evt -> {
        	ClassRoomDetail detail = cbClassRoomDetail.getSelectedItem() != null && cbClassRoomDetail.getSelectedItem().get() != null ? cbClassRoomDetail.getSelectedItem().get() : null;
        	Student student = cbStudent.getSelectedItem() != null && cbStudent.getSelectedItem().get() != null ? cbStudent.getSelectedItem().get() : null;

        	if(detail != null && student != null) {
        		Records records = recordsRepo.findRecordsByClassRoomDetailAndStudent(detail, student);
        		if(records != null) {
        			createWindow(new MemorizeTarget(records), true);
        		}else {
        			Notification.show("Rekod pelajar di kelas ini tidak sah.", Type.ERROR_MESSAGE);
        		}
        	}else {
        		Notification.show("Kelas/Pelajar tidak dipilih.", Type.ERROR_MESSAGE);
        	}
        });

        addComponent(new FormLayout(cbClassRoomDetail, cbStudent));
		addComponent(buttonBar);
		addComponent(grid);
		addComponent(pagination);
	}

	private void createWindow(MemorizeTarget target, boolean isNew) {

		VerticalLayout mainLayout = new VerticalLayout();
		FormLayout formLayout = new FormLayout();

		Window modal = new Window("Kemaskini Target Hafazan");
        modal.center();
        modal.setModal(true);
        modal.setSizeUndefined();

        TextField tfStudent = new TextField("Nama Pelajar");
        tfStudent.setReadOnly(true);
        tfStudent.setValue(target.getRecords() != null && target.getRecords().getStudent() != null ? target.getRecords().getStudent().getName() : "");

        TextField tfDailyTargetJuz = new TextField();
        tfDailyTargetJuz.setRequiredIndicatorVisible(false);
        tfDailyTargetJuz.setWidth(JUZUK_WIDTH, Unit.PIXELS);
        tfDailyTargetJuz.setMaxLength(3);
        tfDailyTargetJuz.setPlaceholder("Juz");

        TextField tfDailyTargetPage = new TextField();
        tfDailyTargetPage.setRequiredIndicatorVisible(false);
        tfDailyTargetPage.setWidth(JUZUK_WIDTH, Unit.PIXELS);
        tfDailyTargetPage.setMaxLength(3);
        tfDailyTargetPage.setPlaceholder("Muka");

        TextField tfDailyTargetLine = new TextField();
        tfDailyTargetLine.setRequiredIndicatorVisible(false);
        tfDailyTargetLine.setWidth(JUZUK_WIDTH, Unit.PIXELS);
        tfDailyTargetLine.setMaxLength(3);
        tfDailyTargetLine.setPlaceholder("Baris");

        if(!isNew && target.getDailyTarget() != null) {
        	Juzuk juzuk = target.getDailyTarget();
        	tfDailyTargetJuz.setValue(juzuk.getJuz()+"");
        	tfDailyTargetPage.setValue(juzuk.getPage()+"");
        	tfDailyTargetLine.setValue(juzuk.getLine()+"");
        }

        TextField tfStartPage = new TextField("M/S Mula");
        tfStartPage.setRequiredIndicatorVisible(false);
        tfStartPage.setWidth(WIDTH, Unit.PIXELS);
        tfStartPage.setMaxLength(4);

        TextField tfLastPage = new TextField("M/S Tamat");
        tfLastPage.setRequiredIndicatorVisible(false);
        tfLastPage.setWidth(WIDTH, Unit.PIXELS);
        tfLastPage.setMaxLength(4);

        TextField tfTotalTargetJuz = new TextField();
        tfTotalTargetJuz.setRequiredIndicatorVisible(false);
        tfTotalTargetJuz.setWidth(JUZUK_WIDTH, Unit.PIXELS);
        tfTotalTargetJuz.setMaxLength(3);
        tfTotalTargetJuz.setPlaceholder("Juz");

        TextField tfTotalTargetPage = new TextField();
        tfTotalTargetPage.setRequiredIndicatorVisible(false);
        tfTotalTargetPage.setWidth(JUZUK_WIDTH, Unit.PIXELS);
        tfTotalTargetPage.setMaxLength(3);
        tfTotalTargetPage.setPlaceholder("Muka");

        TextField tfTotalTargetLine = new TextField();
        tfTotalTargetLine.setRequiredIndicatorVisible(false);
        tfTotalTargetLine.setWidth(JUZUK_WIDTH, Unit.PIXELS);
        tfTotalTargetLine.setMaxLength(3);
        tfTotalTargetLine.setPlaceholder("Baris");

        if(!isNew && target.getTotalDailyTarget() != null) {
        	Juzuk juzuk = target.getTotalDailyTarget();
        	tfTotalTargetJuz.setValue(juzuk.getJuz()+"");
        	tfTotalTargetPage.setValue(juzuk.getPage()+"");
        	tfTotalTargetLine.setValue(juzuk.getLine()+"");
        }

        TextField tfTotalMemorizeJuz = new TextField();
        tfTotalMemorizeJuz.setRequiredIndicatorVisible(false);
        tfTotalMemorizeJuz.setWidth(JUZUK_WIDTH, Unit.PIXELS);
        tfTotalMemorizeJuz.setMaxLength(3);
        tfTotalMemorizeJuz.setPlaceholder("Juz");

        TextField tfTotalMemorizePage = new TextField();
        tfTotalMemorizePage.setRequiredIndicatorVisible(false);
        tfTotalMemorizePage.setWidth(JUZUK_WIDTH, Unit.PIXELS);
        tfTotalMemorizePage.setMaxLength(3);
        tfTotalMemorizePage.setPlaceholder("Muka");

        TextField tfTotalMemorizeLine = new TextField();
        tfTotalMemorizeLine.setRequiredIndicatorVisible(false);
        tfTotalMemorizeLine.setWidth(JUZUK_WIDTH, Unit.PIXELS);
        tfTotalMemorizeLine.setMaxLength(3);
        tfTotalMemorizeLine.setPlaceholder("Baris");

        if(!isNew && target.getTotalMemorize() != null) {
        	Juzuk juzuk = target.getTotalMemorize();
        	tfTotalMemorizeJuz.setValue(juzuk.getJuz()+"");
        	tfTotalMemorizePage.setValue(juzuk.getPage()+"");
        	tfTotalMemorizeLine.setValue(juzuk.getLine()+"");
        }

        TextField tfBalance = new TextField();
        tfBalance.setReadOnly(true);
        tfBalance.setWidth(WIDTH, Unit.PIXELS);

        if(!isNew) {
        	tfStartPage.setValue(target.getStartPage()+"");
        	tfLastPage.setValue(target.getLastPage()+"");
        	Juzuk balanceJuzuk = target.getTotalBalance();
        	tfBalance.setValue(balanceJuzuk != null ? balanceJuzuk.toString() : "");
        }

        formLayout.addComponent(tfStudent);
        HorizontalLayout hlDailyTarget = new HorizontalLayout(tfDailyTargetJuz, tfDailyTargetPage, tfDailyTargetLine);
        hlDailyTarget.setCaption("Target Harian");
        formLayout.addComponent(hlDailyTarget);
        formLayout.addComponent(tfStartPage);
        formLayout.addComponent(tfLastPage);
        HorizontalLayout hlTotalTarget = new HorizontalLayout(tfTotalTargetJuz, tfTotalTargetPage, tfTotalTargetLine);
        hlTotalTarget.setCaption("Jumlah Target");
        formLayout.addComponent(hlTotalTarget);
        HorizontalLayout hlTotalMemorize = new HorizontalLayout(tfTotalMemorizeJuz, tfTotalMemorizePage, tfTotalMemorizeLine);
        hlTotalMemorize.setCaption("Jumlah Hafazan");
        formLayout.addComponent(hlTotalMemorize);

        Button btnSave = new Button("Kemaskini");
        btnSave.addClickListener(evt ->{

        	if(target != null) {
        		target.setDailyTarget(new Juzuk(Integer.parseInt(tfDailyTargetJuz.getValue()), Integer.parseInt(tfDailyTargetPage.getValue()), Integer.parseInt(tfDailyTargetLine.getValue())));
        		target.setStartPage(Integer.parseInt(tfStartPage.getValue()));
        		target.setLastPage(Integer.parseInt(tfLastPage.getValue()));
        		target.setTotalDailyTarget(new Juzuk(Integer.parseInt(tfTotalTargetJuz.getValue()), Integer.parseInt(tfTotalTargetPage.getValue()), Integer.parseInt(tfTotalTargetLine.getValue())));
        		target.setTotalMemorize(new Juzuk(Integer.parseInt(tfTotalMemorizeJuz.getValue()), Integer.parseInt(tfTotalMemorizePage.getValue()), Integer.parseInt(tfTotalMemorizeLine.getValue())));

        		target.setRecordUtility(new RecordUtility());
        		MemorizeTarget editedBean = targetRepo.save(target);
        		if(isNew) {
        			dataProvider.getItems().add(editedBean);
        		}else {
        			dataProvider.getItems().remove(target);
        			dataProvider.getItems().add(editedBean);
        		}
        		
        		Helper.setRecordsHistory(
        			recordsHistoryRepository, 
        			(isNew ? "Kemasukan" : "Mengemaskini") + " Target Hafazan Bulanan.", 
        			Helper.notNull(editedBean.getRecords().getStudent().getPkid()),
        			Helper.notNull(editedBean.getRecords().getClassRoomDetail().getClassRoom().getDescription()) + " - " + 
                    Helper.notNull(editedBean.getRecords().getClassRoomDetail().getName())
        		);
            	dataProvider.refreshAll();
            	modal.close();
        		Notification.show("Rekod kemasukan Hafazan Harian Telah Berjaya Di Kemaskini.", Type.HUMANIZED_MESSAGE);
        	}
        });

        Button btnCancel = new Button("Batal");
        btnCancel.addClickListener(evt ->{
        	modal.close();
        });

        mainLayout.addComponent(formLayout);
        mainLayout.addComponent(new HorizontalLayout(btnSave, btnCancel));

        modal.setContent(mainLayout);
   	 	UI.getCurrent().addWindow(modal);
	}

}
