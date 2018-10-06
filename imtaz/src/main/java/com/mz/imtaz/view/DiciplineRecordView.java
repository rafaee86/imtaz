package com.mz.imtaz.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.mz.imtaz.entity.ClassRoomDetail;
import com.mz.imtaz.entity.Dicipline;
import com.mz.imtaz.entity.DiciplineRecord;
import com.mz.imtaz.entity.DiciplineRecordItem;
import com.mz.imtaz.entity.Records;
import com.mz.imtaz.entity.School;
import com.mz.imtaz.entity.Student;
import com.mz.imtaz.enums.DiscipLineStatus;
import com.mz.imtaz.repository.ClassRoomDetailRepository;
import com.mz.imtaz.repository.ClassRoomRepository;
import com.mz.imtaz.repository.DiciplineRecordItemRepository;
import com.mz.imtaz.repository.DiciplineRecordRepository;
import com.mz.imtaz.repository.DiciplineRepository;
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
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SpringView(name = DiciplineRecordView.NAME)
public class DiciplineRecordView extends VerticalLayout implements View {

	public static final String NAME = "DiciplineRecordView";
	private final static float WIDTH = 500f;
	private final static float JUZUK_WIDTH = 80f;

	final int page = 0;
	final int limit = 1;
	final int targetPage = 0;
	final int targetLimit = 1;

	private ListDataProvider<DiciplineRecord> dataProvider;
	private Long total = 0L;

	@Autowired
	private DiciplineRecordRepository diciplineRecordRepo;
	@Autowired
	private DiciplineRecordItemRepository diciplineItemRecordRepo;
	@Autowired
	private DiciplineRepository diciplineRepo;
	@Autowired
	private RecordsRepository recordsRepo;
	@Autowired
	private StudentRepository studentRepo;
	@Autowired
	private ClassRoomDetailRepository classRoomDetailRepo;
	@Autowired
	private ClassRoomRepository classRoomRepo;

	@PostConstruct
    public void init() {
		headerSection();
		bodySection();
	}

	private void headerSection() {

		setCaption("<h3>Kemasukan Rekod Disiplin Pelajar</h3>");
		setCaptionAsHtml(true);
		Label label = new Label("Skrin untuk kemasukan rekod disiplin pelajar. Sila tekan butang Tambah untuk menambah rekod baru.");

		addComponent(label);
	}

	private void bodySection() {

		Button btnNew = new Button(VaadinIcons.PLUS);
		Button btnDelete = new Button(VaadinIcons.TRASH);
		btnDelete.setEnabled(false);
        HorizontalLayout buttonBar = new HorizontalLayout(btnNew, btnDelete);

        ComboBox<ClassRoomDetail> cbClassRoomDetail = new ComboBox<>("Kelas");
        cbClassRoomDetail.setWidth(WIDTH, Unit.PIXELS);
        cbClassRoomDetail.setItems(classRoomDetailRepo.findAll(Sort.by(Sort.Direction.ASC, "classRoom.level", "name")));
        cbClassRoomDetail.setItemCaptionGenerator(item -> item.getClassRoom().getName() + " " + item.getName() + " - " + item.getTeacher().getSalutation() + " " + item.getTeacher().getName());
        cbClassRoomDetail.setEmptySelectionAllowed(false);

        ComboBox<Student> cbStudent = new ComboBox<>("Pelajar");
        cbStudent.setWidth(WIDTH, Unit.PIXELS);
        cbStudent.setItemCaptionGenerator(item -> item.getName());
        cbStudent.setEmptySelectionAllowed(false);

        cbClassRoomDetail.addSelectionListener(listener -> {
        	if(listener.getSelectedItem() != null && listener.getSelectedItem().get() != null)
        		cbStudent.setItems(studentRepo.findByClassRoomDetail(listener.getSelectedItem().get()));
        });

		Grid<DiciplineRecord> grid = new Grid<>();
		dataProvider = DataProvider.ofCollection(new ArrayList<DiciplineRecord>());
	    grid.setDataProvider(dataProvider);
		grid.setEnabled(true);
		grid.getEditor().setEnabled(true);
		grid.setSizeFull();
		grid.setHeightUndefined();

		grid.addColumn(DiciplineRecord::getOffendedDate, item -> Helper.dateFormat(item))
		.setCaption("Tarikh Kesalahan");

		grid.addColumn(DiciplineRecord::getDiciplineRecordItemList, item-> item.stream().map(item2 -> Helper.notNull(item2.getDicipline().getDescription())).collect( Collectors.joining( ", " ) ))
		.setCaption("Kesalahan");

		grid.addColumn(DiciplineRecord::getActionDescription)
		.setCaption("Tindakan");

		grid.addColumn(DiciplineRecord::getActionDate, item -> Helper.dateFormat(item))
		.setCaption("Tarikh Tindakan");

		grid.addColumn(DiciplineRecord::getStatus, item -> item.getDescription())
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
	        	if (!grid.getSelectedItems().isEmpty()) {
	        		DiciplineRecord item = grid.getSelectedItems().iterator().next();
	                diciplineRecordRepo.delete(item);
	                dataProvider.getItems().remove(item);
	                dataProvider.refreshAll();
	            }
        	}catch (Exception e) {
        		 Notification.show("Rekod tidak berjaya dipadam.", Notification.Type.ERROR_MESSAGE);
			}
        });

        grid.addSelectionListener(listener -> {
        	DiciplineRecord target = listener.getFirstSelectedItem() != null ? listener.getFirstSelectedItem().get() : null;
        	if(target != null) {
        		createWindow(target, false);
        	}
        });

        cbStudent.addSelectionListener(listener -> {
        	ClassRoomDetail classRoomDetail = cbClassRoomDetail.getSelectedItem() != null && cbClassRoomDetail.getSelectedItem().get() != null ? cbClassRoomDetail.getSelectedItem().get() : null;
        	Student student = listener.getSelectedItem() != null && listener.getSelectedItem().get() != null ? listener.getSelectedItem().get() : null;

        	if(classRoomDetail != null && student != null) {
				List<DiciplineRecord> targetList = diciplineRecordRepo.findByClassRoomDetail(classRoomDetail, student);
				List<DiciplineRecord> subTargetList = targetList != null && targetList.size() > limit ? targetList.subList(0, limit) : targetList;
				total = Long.valueOf(subTargetList != null ? targetList.size() : 0);
		        dataProvider = DataProvider.ofCollection(subTargetList != null ? subTargetList : new ArrayList<DiciplineRecord>());
		        grid.setDataProvider(dataProvider);
			}
		});

		Pagination pagination = new Pagination(PaginationResource.newBuilder().setTotal(total).setPage(page).setLimit(limit).build());
	    pagination.setItemsPerPage(1,10, 20, 50, 100);
		pagination.addPageChangeListener(event -> {
			Pageable pageable = PageRequest.of(event.pageIndex(), event.limit());
			List<DiciplineRecord> pageTargetAllList = null;
			ClassRoomDetail classRoomDetail = cbClassRoomDetail.getSelectedItem() != null && cbClassRoomDetail.getSelectedItem().get() != null ? cbClassRoomDetail.getSelectedItem().get() : null;
        	Student student = cbStudent.getSelectedItem() != null && cbStudent.getSelectedItem().get() != null ? cbStudent.getSelectedItem().get() : null;
			if(classRoomDetail != null && student != null)
				pageTargetAllList = diciplineRecordRepo.findByClassRoomDetail(classRoomDetail, student);
			Long totalAll = Long.valueOf(pageTargetAllList != null ? pageTargetAllList.size() : 0);
			List<DiciplineRecord> pageRecordsSubList = null;
			if(classRoomDetail != null && student != null)
				pageRecordsSubList = diciplineRecordRepo.findByClassRoomDetailPageable(classRoomDetail, student, pageable);
			pagination.setTotalCount(totalAll);
			grid.setItems(pageRecordsSubList);
		});

        btnNew.addClickListener(evt -> {
        	ClassRoomDetail detail = Helper.notNull(cbClassRoomDetail.getSelectedItem().get());
        	Student student = Helper.notNull(cbStudent.getSelectedItem().get());

        	if(detail != null && student != null) {
        		Records records = recordsRepo.findRecordsByClassRoomDetailAndStudent(detail, student);
        		if(records != null) {
        			createWindow(new DiciplineRecord(records), true);
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

	private void createWindow(DiciplineRecord diciplineRecord, boolean isNew) {

		VerticalLayout mainLayout = new VerticalLayout();
		FormLayout formLayout = new FormLayout();

		Binder<DiciplineRecord> binder = new Binder<>();
		binder.setBean(diciplineRecord);
		
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
        	.bind(DiciplineRecord::getOffendedDate, DiciplineRecord::setOffendedDate);
        
        ListSelect<Dicipline> cbOffendedType = new ListSelect<>("Kesalahan", diciplineRepo.findAll(Sort.by(Sort.Direction.ASC, "description")));
        cbOffendedType.setRows(6);
        cbOffendedType.setItemCaptionGenerator(item -> item.getDescription());
        cbOffendedType.setWidth(WIDTH, Unit.PIXELS);
        List<DiciplineRecordItem> diciplineRecordItems = Helper.notNull(diciplineRecord.getDiciplineRecordItemList());
        if(diciplineRecordItems != null) {
	        List<Dicipline> diciplineList = diciplineRecordItems.stream().map(item -> item.getDicipline()).collect(Collectors.toList());
	        for(Dicipline item : diciplineList) {
	        	cbOffendedType.select(item);
	        }
        }
    	
        TextArea tfActionDescription = new TextArea("Tindakan");
        tfActionDescription.setMaxLength(200);
        tfActionDescription.setRows(4);
        tfActionDescription.setWidth(WIDTH, Unit.PIXELS);
        binder
			.forField(tfActionDescription)
			.bind(DiciplineRecord::getActionDescription, DiciplineRecord::setActionDescription);
        
        DateField dfActionDate = new DateField("Tarikh Tindakan");
        dfActionDate.setValue(LocalDate.now());
        dfActionDate.setDateFormat("dd/MM/yyyy");
        dfActionDate.setWidth(WIDTH, Unit.PIXELS);
        dfActionDate.setRangeEnd(LocalDate.now());
        binder
	    	.forField(dfActionDate)
	    	.withConverter(new LocalDateToDateConverter())
	    	.bind(DiciplineRecord::getActionDate, DiciplineRecord::setActionDate);
        
        ComboBox<DiscipLineStatus> cbStatus = new ComboBox<>("Tindakan Pihak Tahfiz");
        cbStatus.setItems(Arrays.asList(DiscipLineStatus.values()));
        cbStatus.setItemCaptionGenerator(item -> item.getDescription());
        cbStatus.setWidth(WIDTH, Unit.PIXELS);
        binder
			.forField(cbStatus)
			.bind(DiciplineRecord::getStatus, DiciplineRecord::setStatus);
        
        Button btnSave = new Button("Kemaskini");
        btnSave.addClickListener(evt ->{
        	Boolean isValid = binder.writeBeanIfValid(diciplineRecord);
        	
        	if(isValid != null && isValid) {
        		if(!isNew && Helper.notNull(diciplineRecord.getDiciplineRecordItemList()).size() > 0) {
        			diciplineItemRecordRepo.deleteAll(diciplineRecord.getDiciplineRecordItemList());
            	}
        		DiciplineRecord editedBean = diciplineRecordRepo.save(diciplineRecord);
        		Set<Dicipline> cbOffendedTypeSet = cbOffendedType.getSelectedItems();
        		for(Dicipline dicipline : cbOffendedTypeSet) {
        			DiciplineRecordItem diciplineItem  = new DiciplineRecordItem();
        			diciplineItem.setDicipline(dicipline);
        			diciplineItem.setDiciplineRecord(editedBean);
        			diciplineItem = diciplineItemRecordRepo.save(diciplineItem);
        			editedBean.addDiciplineRecordItemList(diciplineItem);
        		}
        		binder.setBean(editedBean);
        		if(isNew) {
        			dataProvider.getItems().add(editedBean);
        		}else {
        			dataProvider.getItems().remove(diciplineRecord);
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
