package com.mz.imtaz.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.mz.imtaz.entity.ClassRoom;
import com.mz.imtaz.entity.ClassRoomDetail;
import com.mz.imtaz.entity.GeneralCode;
import com.mz.imtaz.entity.RecordUtility;
import com.mz.imtaz.entity.Records;
import com.mz.imtaz.entity.Student;
import com.mz.imtaz.repository.ClassRoomDetailRepository;
import com.mz.imtaz.repository.ClassRoomRepository;
import com.mz.imtaz.repository.GeneralCodeRepository;
import com.mz.imtaz.repository.RecordsHistoryRepository;
import com.mz.imtaz.repository.RecordsRepository;
import com.mz.imtaz.repository.StudentRepository;
import com.mz.imtaz.util.Helper;
import com.mz.imtaz.view.ConfigureView.GeneralCodeCategory;
import com.vaadin.addon.pagination.Pagination;
import com.vaadin.addon.pagination.PaginationResource;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SpringView(name = RecordsRegisterView.NAME)
public class RecordsRegisterView  extends VerticalLayout implements View{

	public static final String NAME = "RecordsRegisterView";
	final int page = 0;
	final int limit = 10;
	final int studentPage = 0;
	final int studentLimit = 10;

	private final static float WIDTH = 500f;

	@Autowired
	private RecordsRepository recordsRepo;
	@Autowired
	private ClassRoomRepository classRoomRepo;
	@Autowired
	private ClassRoomDetailRepository classRoomDetailRepo;
	@Autowired
	private StudentRepository studentRepo;
	@Autowired
	private RecordsHistoryRepository recordsHistoryRepository;
	@Autowired
	private GeneralCodeRepository generalCodeRepo;

	private ListDataProvider<Records> dataProvider;
	private Long total = 0L;

	@PostConstruct
    public void init() {
		headerSection();
		bodySection();
	}

	private void headerSection() {
		setCaption("<h3>Pendaftaran Kelas</h3>");
		setCaptionAsHtml(true);
	}

	private void bodySection() {
		Button btnNew = new Button(VaadinIcons.PLUS);
		Button btnSave = new Button("Simpan");
		Button btnDelete = new Button(VaadinIcons.TRASH);
		btnDelete.setEnabled(false);
        HorizontalLayout buttonBar = new HorizontalLayout(btnNew, btnSave, btnDelete);

        ComboBox<ClassRoom> cbClassRoom = new ComboBox<>("Kategori Kelas");
        cbClassRoom.setWidth(WIDTH, Unit.PIXELS);
        cbClassRoom.setItems(classRoomRepo.findAllActive(Sort.by(Sort.Direction.ASC, "level")));
        cbClassRoom.setItemCaptionGenerator(item -> item.getName());
        cbClassRoom.setEmptySelectionAllowed(false);

        ComboBox<ClassRoomDetail> cbClassRoomDetail = new ComboBox<>("Kelas");
        cbClassRoomDetail.setWidth(WIDTH, Unit.PIXELS);
        cbClassRoomDetail.setItemCaptionGenerator(item -> item.getName() + " - " + (item.getTeacher() != null ? item.getTeacher().getSalutation() + " " +item.getTeacher().getName() : "<Tiada Pengajar>"));
        cbClassRoomDetail.setEmptySelectionAllowed(false);

        cbClassRoom.addSelectionListener(listener -> {
        	if(Helper.notNull(listener.getSelectedItem()) != null)
        		cbClassRoomDetail.setItems(classRoomDetailRepo.findByClassRoom(listener.getSelectedItem().get()));
        });

		Grid<Records> grid = new Grid<Records>();
		grid.setEnabled(true);
		grid.setSizeFull();
		grid.setHeightUndefined();

		grid.addColumn(Records::getClassRoomDetail, item -> item !=  null ? item.getClassRoom().getName() : "").setCaption("Kategori Kelas");
		grid.addColumn(Records::getClassRoomDetail, item -> item !=  null ? item.getName() : "").setCaption("Kelas");
		grid.addColumn(Records::getStudent, item -> item !=  null ? item.getName() : "").setCaption("Nama");
		grid.addColumn(Records::getStudent, item -> item !=  null ? item.getIcNo() : "").setCaption("No K/P");

		dataProvider = DataProvider.ofCollection(new ArrayList<Records>());
        grid.setDataProvider(dataProvider);
        grid.addSelectionListener(listener -> {
        	Records records = Helper.notNull(listener.getFirstSelectedItem()) != null ? listener.getFirstSelectedItem().get() : null;
        	if(records != null) {
        		if(records.getPkid() != null) {
        			createEditWindow(records);
        			btnDelete.setEnabled(false);
        		}else {
        			btnDelete.setEnabled(true);
        		}
        	}
        });

        cbClassRoomDetail.addSelectionListener(listener -> {
			if(Helper.notNull(listener.getSelectedItem()) != null) {
				List<Records> recordsList = recordsRepo.findRecordsByClassRoomDetail(listener.getSelectedItem().get());
				List<Records> subRecordsList = recordsList != null && recordsList.size() > limit ? recordsList.subList(0, limit) : recordsList;
				total = Long.valueOf(subRecordsList != null ? recordsList.size() : 0);
		        dataProvider = DataProvider.ofCollection(subRecordsList != null ? subRecordsList : new ArrayList<Records>());
		        grid.setDataProvider(dataProvider);
			}
		});

		Pagination pagination = new Pagination(PaginationResource.newBuilder().setTotal(total).setPage(page).setLimit(limit).build());
	    pagination.setItemsPerPage(10, 20, 50, 100);
		pagination.addPageChangeListener(event -> {
			Pageable pageable = PageRequest.of(event.pageIndex(), event.limit());
			List<Records> pageRecordsAllList = null;
			if(Helper.notNull(cbClassRoomDetail.getSelectedItem()) != null)
				pageRecordsAllList = recordsRepo.findRecordsByClassRoomDetail(cbClassRoomDetail.getSelectedItem().get());
			Long totalAll = Long.valueOf(pageRecordsAllList != null ? pageRecordsAllList.size() : 0);
			List<Records> pageRecordsSubList = null;
			if(Helper.notNull(cbClassRoomDetail.getSelectedItem()) != null)
				pageRecordsSubList = recordsRepo.findRecordsByClassRoomDetailPageable(cbClassRoomDetail.getSelectedItem().get(), pageable);
			pagination.setTotalCount(totalAll);
			grid.setItems(pageRecordsSubList);
		});

        btnNew.addClickListener(evt -> {
        	ClassRoomDetail detail = Helper.notNull(cbClassRoomDetail.getSelectedItem()) != null ? cbClassRoomDetail.getSelectedItem().get() : null;
        	if(detail != null)
        		createWindow(new Records(detail));
        	else
        		Notification.show("Kelas tidak dipilih.", Type.ERROR_MESSAGE);
        });
        
        btnDelete.addClickListener(evt -> {
        	try {
	        	if (!grid.getSelectedItems().isEmpty()) {
	                Records item = grid.getSelectedItems().iterator().next();
	                dataProvider.getItems().remove(item);
	                dataProvider.refreshAll();
	            }
        	}catch (Exception e) {
        		 Notification.show("Rekod tidak berjaya dipadam.", Notification.Type.ERROR_MESSAGE);
			}
        });

        btnSave.addClickListener(evt -> {
        	for(Records item : dataProvider.getItems()) {
        		if(item.getRecordUtility() == null) {
        			item.setRecordUtility(new RecordUtility());
        			item = recordsRepo.save(item);
            		Helper.setRecordsHistory(
            			recordsHistoryRepository, 
            			"Kemaskini Pelajar Di Kelas Baru.", 
            			Helper.notNull(item.getStudent().getPkid()),
            			Helper.notNull(item.getClassRoomDetail().getClassRoom().getName()) + " - " + 
                        Helper.notNull(item.getClassRoomDetail().getName())
            		);
        		}
        	}
        	Notification.show("Rekod Kemasukan Pelajar Telah Berjaya Di Kemaskini.", Type.HUMANIZED_MESSAGE);
        });

		addComponent(new FormLayout(cbClassRoom, cbClassRoomDetail));
		addComponent(buttonBar);
		addComponent(grid);
		addComponent(pagination);
	}

	private void createWindow(Records records) {

		Window modal = new Window("Carian Pelajar");
        modal.center();
        modal.setModal(true);
        modal.setSizeUndefined();

        TextField tfSearch = new TextField("Carian Nama Pelajar");
        tfSearch.setWidth(WIDTH, Unit.PIXELS);
        Button btnSearch = new Button(VaadinIcons.SEARCH);

        Grid<Student> grid = new Grid<Student>();
		grid.setEnabled(true);
		grid.setSizeFull();
		grid.setHeightUndefined();

		grid.addColumn(Student::getName).setCaption("Nama");
		grid.addColumn(Student::getIcNo).setCaption("No K/P");

		List<Student> studentList = studentRepo.findAllActive(Sort.by(Sort.Direction.ASC, "name"));
		List<Student> subStudentList = studentList != null && studentList.size() > studentLimit ? studentList.subList(0, studentLimit) : studentList;
		Long total = Long.valueOf(subStudentList != null ? studentList.size() : 0);
        grid.addSelectionListener(listener -> {
        	Student student = Helper.notNull(listener.getFirstSelectedItem()) != null ? listener.getFirstSelectedItem().get() : null;
        	if(student != null) {
        		records.setStudent(student);
        		dataProvider.getItems().add(records);
        		dataProvider.refreshAll();
        		modal.close();
        	}
        });

		Pagination pagination = new Pagination(PaginationResource.newBuilder().setTotal(total).setPage(studentPage).setLimit(studentLimit).build());
	    pagination.setItemsPerPage(10, 20, 50, 100);
		pagination.addPageChangeListener(event -> {
			Pageable pageable = PageRequest.of(event.pageIndex(), event.limit());
			String tfValue = tfSearch.getValue() != null ? tfSearch.getValue().toUpperCase() : "";
			List<Student> pageStudAllList = studentRepo.findByName(tfValue, Sort.by(Sort.Direction.ASC, "name"));
			Long totalAll = Long.valueOf(pageStudAllList != null ? pageStudAllList.size() : 0);
			List<Student> pageStudSubList = studentRepo.findByNamePageable(tfValue, pageable);
			pagination.setTotalCount(totalAll);
			grid.setItems(pageStudSubList);
		});

		btnSearch.addClickListener(listener -> {
			Pageable pageable = PageRequest.of(studentPage, studentLimit, Sort.Direction.ASC, "name");
			String tfValue = tfSearch.getValue() != null ? tfSearch.getValue().toUpperCase() : "";
			List<Student> pageStudAllList = studentRepo.findByName(tfValue, Sort.by(Sort.Direction.ASC, "name"));
			Long totalAll = Long.valueOf(pageStudAllList != null ? pageStudAllList.size() : 0);
			List<Student> pageStudSubList = studentRepo.findByNamePageable(tfValue, pageable);
			pagination.setTotalCount(totalAll);
			grid.setItems(pageStudSubList);
		});

        VerticalLayout mainLayout = new VerticalLayout();

        mainLayout.addComponent(new HorizontalLayout(tfSearch, btnSearch));
        mainLayout.addComponent(grid);
        mainLayout.addComponent(pagination);

        modal.setContent(mainLayout);
   	 	UI.getCurrent().addWindow(modal);
	}
	
	private void createEditWindow(Records records) {

		if(records != null) {
			Window modal = new Window("Skrin Pindah Kelas");
	        modal.center();
	        modal.setModal(true);
	        modal.setSizeUndefined();
	        
	        TextField tfCurrClassRoom = new TextField("Kategori Kelas");
	        tfCurrClassRoom.setWidth(WIDTH, Unit.PIXELS);
	        tfCurrClassRoom.setEnabled(false);
	        tfCurrClassRoom.setValue(Helper.notNull(records.getClassRoomDetail().getClassRoom().getName()));
	        
	        TextField tfCurrClassRoomDetail = new TextField("Kelas");
	        tfCurrClassRoomDetail.setWidth(WIDTH, Unit.PIXELS);
	        tfCurrClassRoomDetail.setEnabled(false);
	        tfCurrClassRoomDetail.setValue(Helper.notNull(records.getClassRoomDetail().getName()));
	        
	        TextField tfCurrStudent = new TextField("Nama Pelajar");
	        tfCurrStudent.setWidth(WIDTH, Unit.PIXELS);
	        tfCurrStudent.setEnabled(false);
	        tfCurrStudent.setValue(Helper.notNull(records.getStudent().getName()));
	        
	        ComboBox<ClassRoom> cbClassRoom = new ComboBox<>("Kategori Kelas");
	        cbClassRoom.setWidth(WIDTH, Unit.PIXELS);
	        cbClassRoom.setItems(classRoomRepo.findAllActive(Sort.by(Sort.Direction.ASC, "level")));
	        cbClassRoom.setItemCaptionGenerator(item -> item.getName());
	        cbClassRoom.setEmptySelectionAllowed(false);

	        ComboBox<ClassRoomDetail> cbClassRoomDetail = new ComboBox<>("Kelas");
	        cbClassRoomDetail.setWidth(WIDTH, Unit.PIXELS);
	        cbClassRoomDetail.setItemCaptionGenerator(item -> item.getName() + " - " + (item.getTeacher() != null ? item.getTeacher().getSalutation() + " " +item.getTeacher().getName() : "<Tiada Pengajar>"));
	        cbClassRoomDetail.setEmptySelectionAllowed(false);

	        cbClassRoom.addSelectionListener(listener -> {
	        	if(Helper.notNull(listener.getSelectedItem()) != null)
	        		cbClassRoomDetail.setItems(classRoomDetailRepo.findByClassRoom(listener.getSelectedItem().get()));
	        });
	        	        
	        ComboBox<GeneralCode> cbStatus = new ComboBox<>("Status Pelajar");
	        cbStatus.setWidth(WIDTH, Unit.PIXELS);
	        cbStatus.setItemCaptionGenerator(item -> item.getCode() + " - " + item.getDescription());
	        cbStatus.setEmptySelectionAllowed(false);
	        cbStatus.setItems(generalCodeRepo.findByCategory(GeneralCodeCategory.STUDENT_STATUS.name(), Sort.by(Sort.Direction.ASC, "level")));

	        CheckBox ckbClassRoom = new CheckBox("", false);
	        CheckBox ckbStatus = new CheckBox("", false);
	        
	        cbClassRoom.setEnabled(false);
        	cbClassRoomDetail.setEnabled(false);
        	cbStatus.setEnabled(!false);
	        
	        ckbStatus.addValueChangeListener(listener -> {
	        	Boolean _status = !listener.getValue();
	        	ckbClassRoom.setValue(_status);
	        	cbClassRoom.setEnabled(_status);
	        	cbClassRoomDetail.setEnabled(_status);
	        	cbStatus.setEnabled(!_status);
	        });
	        ckbClassRoom.addValueChangeListener(listener -> {
	        	Boolean _status = !listener.getValue();
	        	ckbStatus.setValue(!listener.getValue());
	        	cbClassRoom.setEnabled(!_status);
	        	cbClassRoomDetail.setEnabled(!_status);
	        	cbStatus.setEnabled(_status);
	        });
	        
	        Button btnSave = new Button("Kemaskini");
	        
	        btnSave.addClickListener(listener -> {
	        	try {
	        		if(ckbClassRoom.getValue()) {
		        		ClassRoomDetail classRoomDetail = Helper.notNull(cbClassRoomDetail.getValue());
		        			        		
						Records newRecords = records.clone();
						newRecords.setClassRoomDetail(classRoomDetail);
						newRecords.setPkid(null);
						newRecords.getRecordUtility().setCreatedDate(new Date());
						newRecords.getRecordUtility().enabled();
						
						newRecords = recordsRepo.save(newRecords);
						recordsRepo.save(records);
						
						Helper.setRecordsHistory(
							recordsHistoryRepository, 
							"Pindah Kelas Ke " +
							Helper.notNull(newRecords.getClassRoomDetail().getClassRoom().getName()) + " - " + 
			                Helper.notNull(newRecords.getClassRoomDetail().getName()),
							newRecords.getStudent().getPkid(),
							Helper.notNull(records.getClassRoomDetail().getClassRoom().getName()) + " - " + 
			                Helper.notNull(records.getClassRoomDetail().getName())
						);
						
						dataProvider.getItems().remove(records);
		        		dataProvider.refreshAll();
		        		Notification.show("Proses Pindahan Kelas Telah Berjaya.", Type.HUMANIZED_MESSAGE);
		        		modal.close();
	        		}else if(ckbStatus.getValue()) {
	        			GeneralCode status = Helper.notNull(cbStatus.getValue());
	        			if(status != null) {
	        				records.getStudent().setStatus(status.getPkid());
	        				recordsRepo.save(records);
	        				Notification.show("Proses Kemaskini Status Pelajar Telah Berjaya.", Type.HUMANIZED_MESSAGE);
			        		modal.close();
	        			}
	        		}
				} catch (Exception e) {
					e.printStackTrace();
					Notification.show("Proses Tidak Berjaya.", Type.ERROR_MESSAGE);
					modal.close();
				}
	        	
	        });
	        
	        VerticalLayout mainLayout = new VerticalLayout();
	        FormLayout formLayout = new FormLayout();
	        Panel panel = new Panel("Kemaskini Kelas Baru");
	        Panel panelStatus = new Panel("Kemaskini Status Pelajar");
	        FormLayout panelLayout = new FormLayout();
	        FormLayout panelLayoutStatus = new FormLayout();
	        
	        formLayout.addComponent(tfCurrClassRoom);
	        formLayout.addComponent(tfCurrClassRoomDetail);
	        formLayout.addComponent(tfCurrStudent);
	        
	        panelLayout.addComponent(cbClassRoom);
	        panelLayout.addComponent(cbClassRoomDetail);
	        panelLayoutStatus.addComponent(cbStatus);
	        
	        panel.setContent(panelLayout);
	        mainLayout.addComponent(formLayout);
	        mainLayout.addComponent(ckbClassRoom);
	        mainLayout.addComponent(panel);
	        
	        mainLayout.addComponent(ckbStatus);
	        panelStatus.setContent(panelLayoutStatus);
	        mainLayout.addComponent(panelStatus);
	        
	        mainLayout.addComponent(btnSave);
	        
	        modal.setContent(mainLayout);
	   	 	UI.getCurrent().addWindow(modal);
		}
	}
}
