package com.mz.imtaz.view;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.mz.imtaz.entity.ClassRoom;
import com.mz.imtaz.entity.ClassRoomDetail;
import com.mz.imtaz.entity.RecordUtility;
import com.mz.imtaz.entity.Records;
import com.mz.imtaz.entity.Student;
import com.mz.imtaz.repository.ClassRoomDetailRepository;
import com.mz.imtaz.repository.ClassRoomRepository;
import com.mz.imtaz.repository.RecordsRepository;
import com.mz.imtaz.repository.StudentRepository;
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
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
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
		Label label = new Label("Skrin untuk mendaftar pelajar di kelas baru. Sila tekan butang Tambah untuk menambah kelas baru.");

		addComponent(label);
	}

	private void bodySection() {
		Button btnNew = new Button(VaadinIcons.PLUS);
		Button btnSave = new Button("Simpan");
        HorizontalLayout buttonBar = new HorizontalLayout(btnNew, btnSave);

        ComboBox<ClassRoom> cbClassRoom = new ComboBox<>("Kategori Kelas");
        cbClassRoom.setWidth(WIDTH, Unit.PIXELS);
        cbClassRoom.setItems(classRoomRepo.findAll());
        cbClassRoom.setItemCaptionGenerator(item -> item.getName());
        cbClassRoom.setEmptySelectionAllowed(false);

        ComboBox<ClassRoomDetail> cbClassRoomDetail = new ComboBox<>("Kelas");
        cbClassRoomDetail.setWidth(WIDTH, Unit.PIXELS);
        cbClassRoomDetail.setItemCaptionGenerator(item -> item.getName() + " - " + (item.getTeacher() != null ? item.getTeacher().getSalutation() + " " +item.getTeacher().getName() : "<Tiada Pengajar>"));
        cbClassRoomDetail.setEmptySelectionAllowed(false);

        cbClassRoom.addSelectionListener(listener -> {
        	if(listener.getSelectedItem() != null && listener.getSelectedItem().get() != null)
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
        	Records records = listener.getFirstSelectedItem() != null ? listener.getFirstSelectedItem().get() : null;
        	if(records != null) {
        		createWindow(records, false);
        	}
        });

        cbClassRoomDetail.addSelectionListener(listener -> {
			if(listener.getSelectedItem() != null && listener.getSelectedItem().get() != null) {
				List<Records> recordsList = recordsRepo.findRecordsByClassRoomDetail(listener.getSelectedItem().get());
				List<Records> subRecordsList = recordsList != null && recordsList.size() > limit ? recordsList.subList(0, limit) : recordsList;
				total = Long.valueOf(subRecordsList != null ? recordsList.size() : 0);
		        dataProvider = DataProvider.ofCollection(subRecordsList != null ? subRecordsList : new ArrayList<Records>());
		        grid.setDataProvider(dataProvider);
			}
		});

		Pagination pagination = new Pagination(PaginationResource.newBuilder().setTotal(total).setPage(page).setLimit(limit).build());
	    pagination.setItemsPerPage(1,10, 20, 50, 100);
		pagination.addPageChangeListener(event -> {
			Pageable pageable = PageRequest.of(event.pageIndex(), event.limit());
			List<Records> pageRecordsAllList = null;
			if(cbClassRoomDetail.getSelectedItem() != null && cbClassRoomDetail.getSelectedItem().get() != null)
				pageRecordsAllList = recordsRepo.findRecordsByClassRoomDetail(cbClassRoomDetail.getSelectedItem().get());
			Long totalAll = Long.valueOf(pageRecordsAllList != null ? pageRecordsAllList.size() : 0);
			List<Records> pageRecordsSubList = null;
			if(cbClassRoomDetail.getSelectedItem() != null && cbClassRoomDetail.getSelectedItem().get() != null)
				pageRecordsSubList = recordsRepo.findRecordsByClassRoomDetailPageable(cbClassRoomDetail.getSelectedItem().get(), pageable);
			pagination.setTotalCount(totalAll);
			grid.setItems(pageRecordsSubList);
		});

        btnNew.addClickListener(evt -> {
        	ClassRoomDetail detail = cbClassRoomDetail.getSelectedItem() != null && cbClassRoomDetail.getSelectedItem().get() != null ? cbClassRoomDetail.getSelectedItem().get() : null;
        	if(detail != null)
        		createWindow(new Records(detail), true);
        	else
        		Notification.show("Kelas tidak dipilih.", Type.ERROR_MESSAGE);
        });

        btnSave.addClickListener(evt -> {
        	for(Records item : dataProvider.getItems()) {
        		if(item.getRecordUtility() == null) {
        			item.setRecordUtility(new RecordUtility(true, 0));
        			item = recordsRepo.save(item);
        		}
        	}
        	Notification.show("Rekod Kemasukan Pelajar Telah Berjaya Di Kemaskini.", Type.HUMANIZED_MESSAGE);
        });

		addComponent(new FormLayout(cbClassRoom, cbClassRoomDetail));
		addComponent(buttonBar);
		addComponent(grid);
		addComponent(pagination);
	}

	private void createWindow(Records records, boolean isNew) {

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

		List<Student> studentList = studentRepo.findAll();
		List<Student> subStudentList = studentList != null && studentList.size() > studentLimit ? studentList.subList(0, studentLimit) : studentList;
		Long total = Long.valueOf(subStudentList != null ? studentList.size() : 0);
        grid.addSelectionListener(listener -> {
        	Student student = listener.getFirstSelectedItem() != null ? listener.getFirstSelectedItem().get() : null;
        	if(student != null) {
        		records.setStudent(student);
        		dataProvider.getItems().add(records);
        		dataProvider.refreshAll();
        		modal.close();
        	}
        });

		Pagination pagination = new Pagination(PaginationResource.newBuilder().setTotal(total).setPage(studentPage).setLimit(studentLimit).build());
	    pagination.setItemsPerPage(1,10, 20, 50, 100);
		pagination.addPageChangeListener(event -> {
			Pageable pageable = PageRequest.of(event.pageIndex(), event.limit());
			String tfValue = tfSearch.getValue() != null ? tfSearch.getValue().toUpperCase() : "";
			List<Student> pageStudAllList = studentRepo.findByName(tfValue);
			Long totalAll = Long.valueOf(pageStudAllList != null ? pageStudAllList.size() : 0);
			List<Student> pageStudSubList = studentRepo.findByNamePageable(tfValue, pageable);
			pagination.setTotalCount(totalAll);
			grid.setItems(pageStudSubList);
		});

		btnSearch.addClickListener(listener -> {
			Pageable pageable = PageRequest.of(studentPage, studentLimit, Sort.Direction.ASC, "name");
			String tfValue = tfSearch.getValue() != null ? tfSearch.getValue().toUpperCase() : "";
			List<Student> pageStudAllList = studentRepo.findByName(tfValue);
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
}
