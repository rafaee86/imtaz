package com.mz.imtaz.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.vaadin.ui.NumberField;

import com.mz.imtaz.entity.RecordUtility;
import com.mz.imtaz.entity.Student;
import com.mz.imtaz.enums.GuardianType;
import com.mz.imtaz.enums.RegistrationType;
import com.mz.imtaz.repository.RecordsHistoryRepository;
import com.mz.imtaz.repository.StudentRepository;
import com.mz.imtaz.util.Helper;
import com.vaadin.addon.pagination.Pagination;
import com.vaadin.addon.pagination.PaginationResource;
import com.vaadin.data.Binder;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SpringView(name = StudentRegisterView.NAME)
public class StudentRegisterView extends VerticalLayout implements View{

	public static final String NAME = "StudentRegisterView";
	final int page = 1;
	final int limit = 10;

	@Autowired
	private StudentRepository studentRepo;
	@Autowired
	private RecordsHistoryRepository recordsHistoryRepository;

	private ListDataProvider<Student> dataProvider;

	@PostConstruct
    public void init() {
		headerSection();
		bodySection();
	}


	private void headerSection() {

		setCaption("<h3>Pendaftaran Pelajar</h3>");
		setCaptionAsHtml(true);
		Label label = new Label("Skrin untuk mendaftar pelajar baru. Sila tekan butang Tambah untuk menambah pelajar baru.");

		addComponent(label);
	}

	private void bodySection() {

		Button btnNew = new Button(VaadinIcons.PLUS);
        HorizontalLayout buttonBar = new HorizontalLayout(btnNew);

		Grid<Student> grid = new Grid<Student>();
		grid.setEnabled(true);
		grid.setSizeFull();
		grid.setHeightUndefined();

		grid.addColumn(Student::getName).setCaption("Nama");
		grid.addColumn(Student::getIcNo).setCaption("No K/P");

		List<Student> studentList = studentRepo.findAllActive(Sort.by(Sort.Direction.ASC, "name"));
		List<Student> subStudentList = studentList != null && studentList.size() > limit ? studentList.subList(0, limit) : studentList;
		Long total = Long.valueOf(subStudentList != null ? studentList.size() : 0);
        dataProvider = DataProvider.ofCollection(subStudentList != null ? subStudentList : new ArrayList<Student>());
        grid.setDataProvider(dataProvider);
        grid.addSelectionListener(listener -> {
        	Student student = Helper.notNull(listener.getFirstSelectedItem()) != null ? listener.getFirstSelectedItem().get() : null;
        	if(student != null) {
        		createWindow(student, false);
        	}
        });

		Pagination pagination = new Pagination(PaginationResource.newBuilder().setTotal(total).setPage(page).setLimit(limit).build());
	    pagination.setItemsPerPage(10, 20, 50, 100);
		pagination.addPageChangeListener(event -> {
			Pageable pageable = PageRequest.of(event.pageIndex(), event.limit(), Sort.Direction.ASC,"name");
			List<Student> pageStudAllList = studentRepo.findAllActive(Sort.by(Sort.Direction.ASC, "name"));
			Long totalAll = Long.valueOf(pageStudAllList != null ? pageStudAllList.size() : 0);
			List<Student> pageStudSubList = studentRepo.findAllActive(pageable);
			pagination.setTotalCount(totalAll);
			grid.setItems(pageStudSubList);
		});

        btnNew.addClickListener(evt -> {
        	createWindow(new Student(), true);
        });

		addComponent(buttonBar);
		addComponent(grid);
		addComponent(pagination);
	}

	private void createWindow(final Student student, Boolean isNew) {
    	Binder<Student> binder = new Binder<Student>();
    	binder.setBean(student);
    	final float WIDTH = 600f;

    	Window modal = new Window((isNew ? "Pendaftaran " : "Kemaskini ") + "Pelajar");
        modal.center();
        modal.setModal(true);
        modal.setSizeUndefined();

        Panel studentPanel = new Panel("Butir-butir Diri Pelajar");
        Panel parentPanel = new Panel("Butir-butir Diri Ibu Bapa / Penjaga");

        ComboBox<RegistrationType> cbRegistrationType = new ComboBox<RegistrationType>("Jenis Pendaftaran");
        cbRegistrationType.setRequiredIndicatorVisible(true);
        cbRegistrationType.setEmptySelectionAllowed(false);
        cbRegistrationType.setItems(Arrays.asList(RegistrationType.values()));
        cbRegistrationType.setItemCaptionGenerator(item -> item.getDescription());
        cbRegistrationType.setWidth(WIDTH, Unit.PIXELS);
        binder
	    	.forField(cbRegistrationType)
	    	.withValidator(input -> input != null , "Sila masukkan Jenis Pendaftaran.")
	    	.bind(Student::getRegistrationType, Student::setRegistrationType);

        TextField tfName = new TextField("Nama Pelajar");
        tfName.setRequiredIndicatorVisible(true);
        tfName.setMaxLength(255);
        tfName.setWidth(WIDTH, Unit.PIXELS);
        binder
	    	.forField(tfName)
	    	.withValidator(input -> input != null && !input.isEmpty() , "Sila masukkan Nama Pelajar.")
	    	.bind(Student::getName, Student::setName);

        NumberField tfIcNo = new NumberField("No Kad Pengenalan");
        tfIcNo.setRequiredIndicatorVisible(true);
        tfIcNo.setWidth(WIDTH, Unit.PIXELS);
        tfIcNo.setMaxLength(12);
        binder
	    	.forField(tfIcNo)
	    	.withValidator(new RegexpValidator("hanya aksara nombor dibenarkan!", "[-]?[0-9]*\\.?,?[0-9]+"))
	    	.bind(Student::getIcNo, Student::setIcNo);

        DateField dfDob = new DateField("Tarikh Lahir");
        dfDob.setRequiredIndicatorVisible(true);
        dfDob.setWidth(WIDTH, Unit.PIXELS);
        dfDob.setRangeStart(LocalDate.of(1900, 1, 1));
        dfDob.setRangeEnd(LocalDate.now());
        binder
	    	.forField(dfDob)
	    	.bind(Student::getDob, Student::setDob);

        TextField tfcbNo = new TextField("No Surat Beranak");
        tfcbNo.setRequiredIndicatorVisible(true);
        tfcbNo.setMaxLength(15);
        tfcbNo.setWidth(WIDTH, Unit.PIXELS);
        binder
	    	.forField(tfcbNo)
	    	.bind(Student::getCbNo, Student::setCbNo);

        TextField tfAddress1 = new TextField("Alamat 1");
        tfAddress1.setRequiredIndicatorVisible(false);
        tfAddress1.setMaxLength(255);
        tfAddress1.setWidth(WIDTH, Unit.PIXELS);
        binder
	    	.forField(tfAddress1)
	    	.bind(Student::getAddress1, Student::setAddress1);

        TextField tfAddress2 = new TextField("Alamat 2");
        tfAddress2.setRequiredIndicatorVisible(false);
        tfAddress2.setMaxLength(255);
        tfAddress2.setWidth(WIDTH, Unit.PIXELS);
        binder
	    	.forField(tfAddress2)
	    	.bind(Student::getAddress2, Student::setAddress2);

        NumberField tfPoscode = new NumberField("Poskod");
        tfPoscode.setRequiredIndicatorVisible(false);
        tfPoscode.setMaxLength(5);
        tfPoscode.setWidth(WIDTH, Unit.PIXELS);
        binder
	    	.forField(tfPoscode)
	    	.bind(Student::getPoscode, Student::setPoscode);

        TextField tfTown = new TextField("Bandar");
        tfTown.setRequiredIndicatorVisible(false);
        tfTown.setMaxLength(50);
        tfTown.setWidth(WIDTH, Unit.PIXELS);
        binder
	    	.forField(tfTown)
	    	.bind(Student::getTown, Student::setTown);

        TextField tfState = new TextField("Negeri");
        tfState.setRequiredIndicatorVisible(false);
        tfState.setMaxLength(50);
        tfState.setWidth(WIDTH, Unit.PIXELS);
        binder
	    	.forField(tfTown)
	    	.bind(Student::getState, Student::setState);

        TextField tfSickness = new TextField("Penyakit");
        tfSickness.setRequiredIndicatorVisible(false);
        tfSickness.setMaxLength(200);
        tfSickness.setWidth(WIDTH, Unit.PIXELS);
        binder
	    	.forField(tfSickness)
	    	.bind(Student::getSickness, Student::setSickness);

        TextField tfAllergies = new TextField("Alahan");
        tfAllergies.setRequiredIndicatorVisible(false);
        tfAllergies.setMaxLength(200);
        tfAllergies.setWidth(WIDTH, Unit.PIXELS);
        binder
	    	.forField(tfSickness)
	    	.bind(Student::getAllergies, Student::setAllergies);

        TextField tfReadQuranDesc = new TextField("Jika Ya, Nyatakan Al-Quran atau Iqra'");
        tfReadQuranDesc.setRequiredIndicatorVisible(false);
        tfReadQuranDesc.setMaxLength(250);
        tfReadQuranDesc.setWidth(WIDTH, Unit.PIXELS);
        tfReadQuranDesc.setVisible(false);
        binder
	    	.forField(tfReadQuranDesc)
	    	.bind(Student::getReadingQuranDesc, Student::setReadingQuranDesc);

        CheckBox cbCanReadQuran = new CheckBox("Boleh Membaca Al-Quran?");
        cbCanReadQuran.addValueChangeListener(item -> tfReadQuranDesc.setVisible(item.getValue() != null && item.getValue()));

        TextField tfMemorizeQuranDesc = new TextField("Jika Ya, Nyatakan Berapa Juzuk Yang Telah Dihafal'");
        tfMemorizeQuranDesc.setRequiredIndicatorVisible(false);
        tfMemorizeQuranDesc.setMaxLength(250);
        tfMemorizeQuranDesc.setWidth(WIDTH, Unit.PIXELS);
        tfMemorizeQuranDesc.setVisible(false);
        binder
	    	.forField(tfMemorizeQuranDesc)
	    	.bind(Student::getMemorizeQuranDesc, Student::setMemorizeQuranDesc);

        CheckBox cbEverMemorizeQuran = new CheckBox("Pernah Menghafal Al-Quran");
        cbEverMemorizeQuran.addValueChangeListener(item -> tfMemorizeQuranDesc.setVisible(item.getValue() != null && item.getValue()));

        TextField tfPrevSchool = new TextField("Nama Sekolah Lama'");
        tfPrevSchool.setRequiredIndicatorVisible(false);
        tfPrevSchool.setMaxLength(250);
        tfPrevSchool.setWidth(WIDTH, Unit.PIXELS);
        binder
	    	.forField(tfPrevSchool)
	    	.bind(Student::getNamePrevSchool, Student::setNamePrevSchool);

        NumberField tfTelNoPrevNo = new NumberField("No Telefon Sekolah Lama");
        tfTelNoPrevNo.setRequiredIndicatorVisible(false);
        tfTelNoPrevNo.setMaxLength(20);
        tfTelNoPrevNo.setWidth(WIDTH, Unit.PIXELS);
        binder
	    	.forField(tfTelNoPrevNo)
	    	.bind(Student::getTelNoPrevSchool, Student::setTelNoPrevSchool);

        TextField tfRegisterJustification = new NumberField("Nyatakan Tujuan Masuk Di Maahad Tahfiz Ini");
        tfRegisterJustification.setRequiredIndicatorVisible(false);
        tfRegisterJustification.setMaxLength(250);
        tfRegisterJustification.setWidth(WIDTH, Unit.PIXELS);
        binder
	    	.forField(tfRegisterJustification)
	    	.bind(Student::getRegisterJustification, Student::setRegisterJustification);

        TextField tfRegisterJustification2 = new NumberField("Apakah Sebab Maahad Tahfiz Menjadi Pilihan?");
        tfRegisterJustification2.setRequiredIndicatorVisible(false);
        tfRegisterJustification2.setMaxLength(250);
        tfRegisterJustification2.setWidth(WIDTH, Unit.PIXELS);
        binder
	    	.forField(tfRegisterJustification2)
	    	.bind(Student::getRegisterJustification2, Student::setRegisterJustification2);

        // ----------------------------------- Parent Panel ---------------------------------------------------------//

        ComboBox<GuardianType> cbGuardianType = new ComboBox<GuardianType>("Jenis Penjaga");
        cbGuardianType.setEmptySelectionAllowed(true);
        cbGuardianType.setEmptySelectionAllowed(false);
        cbGuardianType.setItems(Arrays.asList(GuardianType.values()));
        cbGuardianType.setItemCaptionGenerator(item -> item.getDescription());
        cbGuardianType.setWidth(WIDTH, Unit.PIXELS);
        binder
	    	.forField(cbGuardianType)
	    	.bind(Student::getGuardianType, Student::setGuardianType);

        TextField tfFatherName = new TextField("Nama Bapa/Penjaga");
        tfFatherName.setRequiredIndicatorVisible(true);
        tfFatherName.setMaxLength(250);
        tfFatherName.setWidth(WIDTH, Unit.PIXELS);
        binder
	    	.forField(tfFatherName)
	    	.bind(Student::getFatherName, Student::setFatherName);

        NumberField tfFatherIcNo = new NumberField("No Kad Pengenalan Bapa/Penjaga");
        tfFatherIcNo.setRequiredIndicatorVisible(false);
        tfFatherIcNo.setMaxLength(12);
        tfFatherIcNo.setWidth(WIDTH, Unit.PIXELS);
        binder
	    	.forField(tfFatherIcNo)
	    	.bind(Student::getFatherIcNo, Student::setFatherIcNo);

        TextField tfFatherJob = new TextField("Pekerjaan Bapa/Penjaga");
        tfFatherJob.setRequiredIndicatorVisible(false);
        tfFatherJob.setMaxLength(250);
        tfFatherJob.setWidth(WIDTH, Unit.PIXELS);
        binder
	    	.forField(tfFatherJob)
	    	.bind(Student::getFatherJob, Student::setFatherJob);

        TextField tfMotherName = new TextField("Nama Ibu");
        tfMotherName.setRequiredIndicatorVisible(false);
        tfMotherName.setMaxLength(250);
        tfMotherName.setWidth(WIDTH, Unit.PIXELS);
        binder
	    	.forField(tfMotherName)
	    	.bind(Student::getMotherName, Student::setMotherName);

        NumberField tfMotherIcNo = new NumberField("No Kad Pengenalan Ibu");
        tfMotherIcNo.setRequiredIndicatorVisible(false);
        tfMotherIcNo.setMaxLength(12);
        tfMotherIcNo.setWidth(WIDTH, Unit.PIXELS);
        binder
	    	.forField(tfMotherIcNo)
	    	.bind(Student::getMotherIcNo, Student::setMotherIcNo);

        TextField tfMotherJob = new TextField("Pekerjaan Ibu");
        tfMotherJob.setRequiredIndicatorVisible(false);
        tfMotherJob.setMaxLength(250);
        tfMotherJob.setWidth(WIDTH, Unit.PIXELS);
        binder
	    	.forField(tfMotherJob)
	    	.bind(Student::getMotherJob, Student::setMotherJob);

        TextField tfParentTelNo = new TextField("No Telefon");
        tfParentTelNo.setRequiredIndicatorVisible(false);
        tfParentTelNo.setMaxLength(20);
        tfParentTelNo.setWidth(WIDTH, Unit.PIXELS);
        binder
	    	.forField(tfParentTelNo)
	    	.bind(Student::getParentTelNo, Student::setParentTelNo);

        TextField tfParentHpNo = new TextField("No HP");
        tfParentHpNo.setRequiredIndicatorVisible(false);
        tfParentHpNo.setMaxLength(20);
        tfParentHpNo.setWidth(WIDTH, Unit.PIXELS);
        binder
	    	.forField(tfParentHpNo)
	    	.bind(Student::getParentHpNo, Student::setParentHpNo);

        FormLayout studentLayout = new FormLayout();
        studentLayout.addComponent(cbRegistrationType);
        studentLayout.addComponent(tfName);
        studentLayout.addComponent(tfIcNo);
        studentLayout.addComponent(tfcbNo);
        studentLayout.addComponent(dfDob);
        studentLayout.addComponent(tfAddress1);
        studentLayout.addComponent(tfAddress2);
        studentLayout.addComponent(tfPoscode);
        studentLayout.addComponent(tfTown);
        studentLayout.addComponent(tfState);
        studentLayout.addComponent(tfSickness);
        studentLayout.addComponent(tfAllergies);
        studentLayout.addComponent(new HorizontalLayout(cbCanReadQuran, tfReadQuranDesc));
        studentLayout.addComponent(new HorizontalLayout(cbEverMemorizeQuran, tfMemorizeQuranDesc));
        studentLayout.addComponent(tfTelNoPrevNo);
        studentLayout.addComponent(tfRegisterJustification);
        studentLayout.addComponent(tfRegisterJustification2);

        FormLayout parentLayout = new FormLayout();
        parentLayout.addComponent(cbGuardianType);
        parentLayout.addComponent(tfFatherName);
        parentLayout.addComponent(tfFatherIcNo);
        parentLayout.addComponent(tfFatherJob);
        parentLayout.addComponent(tfMotherName);
        parentLayout.addComponent(tfMotherIcNo);
        parentLayout.addComponent(tfMotherJob);
        parentLayout.addComponent(tfParentTelNo);
        parentLayout.addComponent(tfParentHpNo);

        studentPanel.setContent(studentLayout);
        parentPanel.setContent(parentLayout);

        Button btnSave = new Button("Simpan");
        Button btnDelete = new Button("Padam");
        btnDelete.setVisible(!isNew);
        Button btnCancel = new Button("Batal");

        btnSave.addClickListener(listener -> {
        	Boolean isValid = binder.writeBeanIfValid(student);
        	if(isValid != null && isValid) {
        		student.setRecordUtility(new RecordUtility());
        		Student editedBean = studentRepo.save(student);
        		if(isNew) {
        			dataProvider.getItems().add(editedBean);
        		}else {
        			dataProvider.getItems().remove(student);
        			dataProvider.getItems().add(editedBean);
        		}
        		Helper.setRecordsHistory(recordsHistoryRepository, (isNew ? "Mendaftar" : "Mengemaskini") + " Maklumat Pelajar", editedBean.getPkid());
            	dataProvider.refreshAll();
            	modal.close();
        	}else {
        		Notification.show("Rekod Pelajar Tidak Berjaya Di Kemaskini.", Type.ERROR_MESSAGE);
        	}
        });
        btnDelete.addClickListener(evt -> {
        	try {
	        	if (binder.getBean() != null) {
	                Student item = binder.getBean();
	                item.getRecordUtility().disabled();
	                if(item.getPkid() != null) {
	                	studentRepo.save(item);
	                	Helper.setRecordsHistory(recordsHistoryRepository, "Memadam Maklumat Pelajar", item.getPkid());
	                }
	                dataProvider.getItems().remove(item);
	                dataProvider.refreshAll();
	            	modal.close();
	            }
        	}catch (Exception e) {
        		 Notification.show("Rekod Pelajar tidak berjaya dipadam.", Notification.Type.ERROR_MESSAGE);
			}
        });
        btnCancel.addClickListener(listener -> {
        	binder.removeBean();
        	modal.close();
        });

        VerticalLayout mainLayout = new VerticalLayout(studentPanel, parentPanel, new HorizontalLayout(btnSave, btnDelete, btnCancel));

        modal.setContent(mainLayout);
   	 	UI.getCurrent().addWindow(modal);
	}

}
