package com.mz.imtaz.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.mz.imtaz.entity.ClassRoomDetail;
import com.mz.imtaz.entity.Discipline;
import com.mz.imtaz.entity.DisciplineRecord;
import com.mz.imtaz.entity.DisciplineRecordItem;
import com.mz.imtaz.entity.RecordUtility;
import com.mz.imtaz.entity.Records;
import com.mz.imtaz.entity.Student;
import com.mz.imtaz.entity.UserContext;
import com.mz.imtaz.enums.DisciplineStatus;
import com.mz.imtaz.repository.ClassRoomDetailRepository;
import com.mz.imtaz.repository.DisciplineRecordItemRepository;
import com.mz.imtaz.repository.DisciplineRecordRepository;
import com.mz.imtaz.repository.DisciplineRepository;
import com.mz.imtaz.repository.RecordsHistoryRepository;
import com.mz.imtaz.repository.RecordsRepository;
import com.mz.imtaz.repository.StudentRepository;
import com.mz.imtaz.util.Helper;
import com.vaadin.addon.pagination.Pagination;
import com.vaadin.addon.pagination.PaginationResource;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.LocalDateToDateConverter;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SpringView(name = DisciplineRecordView.NAME)
public class DisciplineRecordView extends VerticalLayout implements View {

	public static final String NAME = "DisciplineRecordView";
	private final static float WIDTH = 500f;

	final int page = 0;
	final int limit = 10;
	final int targetPage = 0;
	final int targetLimit = 10;

	private ListDataProvider<DisciplineRecord> dataProvider;
	private Long total = 0L;

	@Autowired
	private DisciplineRecordRepository disciplineRecordRepo;
	@Autowired
	private DisciplineRecordItemRepository disciplineItemRecordRepo;
	@Autowired
	private DisciplineRepository disciplineRepo;
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

		setCaption("<h3>Kemasukan Rekod Disiplin Pelajar</h3>");
		setCaptionAsHtml(true);
	}

	private void bodySection() {

		Button btnNew = new Button(VaadinIcons.PLUS);
		Button btnDelete = new Button(VaadinIcons.TRASH);
		btnDelete.setEnabled(false);
        HorizontalLayout buttonBar = new HorizontalLayout(btnNew, btnDelete);

        ComboBox<ClassRoomDetail> cbClassRoomDetail = new ComboBox<>("Kelas");
        cbClassRoomDetail.setWidth(WIDTH, Unit.PIXELS);
        cbClassRoomDetail.setItems(classRoomDetailRepo.findAllWithOrder());
        cbClassRoomDetail.setItemCaptionGenerator(item -> Helper.notNull(item.getClassRoom().getDescription()) + " " + Helper.notNull(item.getName()) + " - " + Helper.notNull(item.getTeacher().getSalutation()) + " " + Helper.notNull(item.getTeacher().getName()));
        cbClassRoomDetail.setEmptySelectionAllowed(false);

        ComboBox<Student> cbStudent = new ComboBox<>("Pelajar");
        cbStudent.setWidth(WIDTH, Unit.PIXELS);
        cbStudent.setItemCaptionGenerator(item -> item.getName());
        cbStudent.setEmptySelectionAllowed(false);

        cbClassRoomDetail.addSelectionListener(listener -> {
        	if(Helper.notNull(listener.getSelectedItem()) != null)
        		cbStudent.setItems(studentRepo.findByClassRoomDetail(listener.getSelectedItem().get()));
        });

		Grid<DisciplineRecord> grid = new Grid<>();
		dataProvider = DataProvider.ofCollection(new ArrayList<DisciplineRecord>());
	    grid.setDataProvider(dataProvider);
		grid.setEnabled(true);
		grid.getEditor().setEnabled(true);
		grid.setSizeFull();
		grid.setHeightUndefined();

		grid.addColumn(DisciplineRecord::getOffendedDate, item -> Helper.dateFormat(item))
		.setCaption("Tarikh Kesalahan");

		grid.addColumn(DisciplineRecord::getDisciplineRecordItemList, item-> item.stream().map(item2 -> Helper.notNull(item2.getDiscipline().getDescription())).collect( Collectors.joining( ", " ) ))
		.setCaption("Kesalahan");

		grid.addColumn(DisciplineRecord::getActionDescription)
		.setCaption("Tindakan");

		grid.addColumn(DisciplineRecord::getActionDate, item -> Helper.dateFormat(item))
		.setCaption("Tarikh Tindakan");

		grid.addColumn(DisciplineRecord::getStatus, item -> item.getDescription())
		.setCaption("Tindakan Pihak Tahfiz");

        grid.addSelectionListener(evt -> {
        	if (evt.getFirstSelectedItem().isPresent()) {
                btnDelete.setEnabled(true);
            } else {
                btnDelete.setEnabled(false);
            }
        });

        btnDelete.addClickListener(evt -> {
        	try {
        		UserContext userContext = Helper.getUserContext();
	        	if (!grid.getSelectedItems().isEmpty()) {
	        		DisciplineRecord item = grid.getSelectedItems().iterator().next();
	                item.getRecordUtility().disabled(userContext.getPkid());
	                if(item.getPkid() != null) {
	                	disciplineRecordRepo.save(item);
	                	Helper.setRecordsHistory(
                			recordsHistoryRepository, 
                			"Memadam Maklumat Pelajar", 
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
        	DisciplineRecord target = listener.getFirstSelectedItem() != null ? listener.getFirstSelectedItem().get() : null;
        	if(target != null) {
        		createWindow(target, false);
        	}
        });

        cbStudent.addSelectionListener(listener -> {
        	ClassRoomDetail classRoomDetail = cbClassRoomDetail.getSelectedItem() != null && cbClassRoomDetail.getSelectedItem().get() != null ? cbClassRoomDetail.getSelectedItem().get() : null;
        	Student student = Helper.notNull(listener.getSelectedItem()) != null ? listener.getSelectedItem().get() : null;

        	if(classRoomDetail != null && student != null) {
				List<DisciplineRecord> targetList = disciplineRecordRepo.findByClassRoomDetail(classRoomDetail, student);
				List<DisciplineRecord> subTargetList = targetList != null && targetList.size() > limit ? targetList.subList(0, limit) : targetList;
				total = Long.valueOf(subTargetList != null ? targetList.size() : 0);
		        dataProvider = DataProvider.ofCollection(subTargetList != null ? subTargetList : new ArrayList<DisciplineRecord>());
		        grid.setDataProvider(dataProvider);
			}
		});

		Pagination pagination = new Pagination(PaginationResource.newBuilder().setTotal(total).setPage(page).setLimit(limit).build());
	    pagination.setItemsPerPage(10, 20, 50, 100);
		pagination.addPageChangeListener(event -> {
			Pageable pageable = PageRequest.of(event.pageIndex(), event.limit());
			List<DisciplineRecord> pageTargetAllList = null;
			ClassRoomDetail classRoomDetail = Helper.notNull(cbClassRoomDetail.getSelectedItem()) != null ? cbClassRoomDetail.getSelectedItem().get() : null;
        	Student student = Helper.notNull(cbStudent.getSelectedItem()) != null ? cbStudent.getSelectedItem().get() : null;
			if(classRoomDetail != null && student != null)
				pageTargetAllList = disciplineRecordRepo.findByClassRoomDetail(classRoomDetail, student);
			Long totalAll = Long.valueOf(pageTargetAllList != null ? pageTargetAllList.size() : 0);
			List<DisciplineRecord> pageRecordsSubList = null;
			if(classRoomDetail != null && student != null)
				pageRecordsSubList = disciplineRecordRepo.findByClassRoomDetailPageable(classRoomDetail, student, pageable);
			pagination.setTotalCount(totalAll);
			grid.setItems(pageRecordsSubList);
		});

        btnNew.addClickListener(evt -> {
        	ClassRoomDetail detail = Helper.notNull(cbClassRoomDetail.getSelectedItem());
        	Student student = Helper.notNull(cbStudent.getSelectedItem());

        	if(detail != null && student != null) {
        		Records records = recordsRepo.findRecordsByClassRoomDetailAndStudent(detail, student);
        		if(records != null) {
        			createWindow(new DisciplineRecord(records), true);
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

	private void createWindow(DisciplineRecord disciplineRecord, boolean isNew) {

		VerticalLayout mainLayout = new VerticalLayout();
		FormLayout formLayout = new FormLayout();

		Binder<DisciplineRecord> binder = new Binder<>();
		binder.setBean(disciplineRecord);
		
		Window modal = new Window((isNew ? "Kemasukan " : "Kemaskini ") + "Rekod Disipline");
        modal.center();
        modal.setModal(true);
        modal.setSizeUndefined();

        DateField dfOffendedDate = new DateField("Tarikh Kesalahan");
        dfOffendedDate.setValue(LocalDate.now());
        dfOffendedDate.setDateFormat("dd/MM/yyyy");
        dfOffendedDate.setWidth(WIDTH, Unit.PIXELS);
        dfOffendedDate.setRangeEnd(LocalDate.now());
        binder
        	.forField(dfOffendedDate)
        	.withConverter(new LocalDateToDateConverter())
        	.bind(DisciplineRecord::getOffendedDate, DisciplineRecord::setOffendedDate);
        
        ListSelect<Discipline> cbOffendedType = new ListSelect<>("Kesalahan", disciplineRepo.findAllActive(Sort.by(Sort.Direction.ASC, "description")));
        cbOffendedType.setRows(6);
        cbOffendedType.setItemCaptionGenerator(item -> item.getDescription());
        cbOffendedType.setWidth(WIDTH, Unit.PIXELS);
        List<DisciplineRecordItem> DisciplineRecordItems = Helper.notNull(disciplineRecord.getDisciplineRecordItemList());
        if(DisciplineRecordItems != null) {
	        List<Discipline> DisciplineList = DisciplineRecordItems.stream().map(item -> item.getDiscipline()).collect(Collectors.toList());
	        for(Discipline item : DisciplineList) {
	        	cbOffendedType.select(item);
	        }
        }
    	
        TextArea tfActionDescription = new TextArea("Tindakan");
        tfActionDescription.setMaxLength(200);
        tfActionDescription.setRows(4);
        tfActionDescription.setWidth(WIDTH, Unit.PIXELS);
        binder
			.forField(tfActionDescription)
			.bind(DisciplineRecord::getActionDescription, DisciplineRecord::setActionDescription);
        
        DateField dfActionDate = new DateField("Tarikh Tindakan");
        dfActionDate.setValue(LocalDate.now());
        dfActionDate.setDateFormat("dd/MM/yyyy");
        dfActionDate.setWidth(WIDTH, Unit.PIXELS);
        dfActionDate.setRangeEnd(LocalDate.now());
        binder
	    	.forField(dfActionDate)
	    	.withConverter(new LocalDateToDateConverter())
	    	.bind(DisciplineRecord::getActionDate, DisciplineRecord::setActionDate);
        
        ComboBox<DisciplineStatus> cbStatus = new ComboBox<>("Tindakan Pihak Tahfiz");
        cbStatus.setItems(Arrays.asList(DisciplineStatus.values()));
        cbStatus.setItemCaptionGenerator(item -> item.getDescription());
        cbStatus.setWidth(WIDTH, Unit.PIXELS);
        binder
			.forField(cbStatus)
			.bind(DisciplineRecord::getStatus, DisciplineRecord::setStatus);
        
        Button btnSave = new Button("Kemaskini");
        btnSave.addClickListener(evt ->{
    		UserContext userContext = Helper.getUserContext();
        	Boolean isValid = binder.writeBeanIfValid(disciplineRecord);
        	
        	if(isValid != null && isValid) {
        		if(!isNew && Helper.notNull(disciplineRecord.getDisciplineRecordItemList()).size() > 0) {
        			disciplineItemRecordRepo.deleteAll(disciplineRecord.getDisciplineRecordItemList());
            	}
        		disciplineRecord.setRecordUtility(new RecordUtility(userContext.getPkid()));
        		DisciplineRecord editedBean = disciplineRecordRepo.save(disciplineRecord);
        		Helper.setRecordsHistory(
        			recordsHistoryRepository, 
        			(isNew ? "Menambah" : "Mengemaskini") + " Maklumat Disiplin.", 
        			Helper.notNull(editedBean.getRecords().getStudent().getPkid()),
        			Helper.notNull(editedBean.getRecords().getClassRoomDetail().getClassRoom().getDescription()) + " - " + 
                	Helper.notNull(editedBean.getRecords().getClassRoomDetail().getName())
        		);
        		Set<Discipline> cbOffendedTypeSet = cbOffendedType.getSelectedItems();
        		for(Discipline Discipline : cbOffendedTypeSet) {
        			DisciplineRecordItem DisciplineItem  = new DisciplineRecordItem();
        			DisciplineItem.setDiscipline(Discipline);
        			DisciplineItem.setDisciplineRecord(editedBean);
        			DisciplineItem = disciplineItemRecordRepo.save(DisciplineItem);
        			editedBean.addDisciplineRecordItemList(DisciplineItem);
        		}
        		binder.setBean(editedBean);
        		if(isNew) {
        			dataProvider.getItems().add(editedBean);
        		}else {
        			dataProvider.getItems().remove(disciplineRecord);
        			dataProvider.getItems().add(editedBean);
        		}
        		Notification.show("Kemaskini maklumat sekolah telah berjaya.", Type.HUMANIZED_MESSAGE);
        		dataProvider.refreshAll();
        		modal.close();
        	}else {
        		Notification.show("Kemaskini maklumat sekolah tidak berjaya.", Type.ERROR_MESSAGE);
        	}
        });

        Button btnCancel = new Button("Batal");
        btnCancel.addClickListener(evt ->{
        	modal.close();
        });
        
        formLayout.addComponent(dfOffendedDate);
        formLayout.addComponent(cbOffendedType);
        formLayout.addComponent(dfActionDate);
        formLayout.addComponent(tfActionDescription);
        formLayout.addComponent(cbStatus);
        
        mainLayout.addComponent(formLayout);
        mainLayout.addComponent(new HorizontalLayout(btnSave, btnCancel));

        modal.setContent(mainLayout);
   	 	UI.getCurrent().addWindow(modal);
	}

}
