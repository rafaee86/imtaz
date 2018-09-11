package com.mz.imtaz.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.ui.NumberField;

import com.mz.imtaz.entity.ClassRoom;
import com.mz.imtaz.entity.ClassRoomDetail;
import com.mz.imtaz.entity.GeneralCode;
import com.mz.imtaz.entity.School;
import com.mz.imtaz.entity.Teacher;
import com.mz.imtaz.enums.Salutation;
import com.mz.imtaz.repository.ClassRoomDetailRepository;
import com.mz.imtaz.repository.ClassRoomRepository;
import com.mz.imtaz.repository.GeneralCodeRepository;
import com.mz.imtaz.repository.SchoolRepository;
import com.mz.imtaz.repository.TeacherRepository;
import com.vaadin.data.Binder;
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
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringView(name = ConfigureView.NAME)
public class ConfigureView extends VerticalLayout implements View {

	enum TabType {
		SCHOOL, CLASSROOM, CLASSROOM_DETAIL, TEACHER, GENERAL;
	}

	enum GeneralCodeCategory {
		STUDENT_STATUS("Status Pelajar"),
		STUDENT_ACADEMIC_STATUS("Kod Status Pengajian Pelajar"),
		STUDENT_GRADE_MONTHLY("Kod Penilaian Keputusan Bulanan"),
		STUDENT_DICIPLINE("Kod Disiplin Pelajar") ;

		private String description;

		private GeneralCodeCategory(String description) {
			this.description = description;
		}

		public String getDescription() {
			return description;
		}
	}

	public static final String NAME = "ConfigureView";
	private final static float WIDTH = 500f;

	@Autowired
	private SchoolRepository schoolRepo;
	@Autowired
	private ClassRoomRepository classRoomRepo;
	@Autowired
	private TeacherRepository teacherRepo;
	@Autowired
	private ClassRoomDetailRepository classRoomDetailRepo;
	@Autowired
	private GeneralCodeRepository generalCodeRepo;

	@PostConstruct
    public void init() {

		TabSheet tab = new TabSheet();
		tab.setHeight(100.0f, Unit.PERCENTAGE);
		tab.addStyleName(ValoTheme.TABSHEET_FRAMED);
		tab.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);

		tab.addTab(getTabContent(TabType.SCHOOL.name()), "Sekolah");
		tab.addTab(getTabContent(TabType.CLASSROOM.name()), "Kategori Kelas").setId(TabType.CLASSROOM.name());
		tab.addTab(getTabContent(TabType.TEACHER.name()), "Pengajar").setId(TabType.TEACHER.name());
		tab.addTab(getTabContent(TabType.CLASSROOM_DETAIL.name()), "Kelas").setId(TabType.CLASSROOM_DETAIL.name());
		tab.addTab(getTabContent(TabType.GENERAL.name()), "Am").setId(TabType.GENERAL.name());

		addComponent(tab);
	}

	private VerticalLayout getTabContent(String id) {

		VerticalLayout layout = null;
		if(TabType.SCHOOL.name().equalsIgnoreCase(id)) {
			layout = configureSchoolTab();
		}else if(TabType.CLASSROOM.name().equalsIgnoreCase(id)) {
			layout = configureClassRoomTab();
		}else if(TabType.CLASSROOM_DETAIL.name().equalsIgnoreCase(id)) {
			layout = configureClassRoomDetailTab();
		}else if(TabType.TEACHER.name().equalsIgnoreCase(id)) {
			layout = configureTeacherTab();
		}else if(TabType.GENERAL.name().equalsIgnoreCase(id)) {
			layout = configureGeneralTab();
		}

		layout.setId(id);

		return layout;
	}

	private VerticalLayout configureSchoolTab() {

		VerticalLayout mainLayout = new VerticalLayout();
        FormLayout formLayout = new FormLayout();

        Label label = new Label("Skrin untuk mengemaskini maklumat sekolah. Sila masukkan medan bertanda * dan tekan butang Kemaskini.");

        List<School> schoolList = schoolRepo.findAll();
        School school = schoolList != null && schoolList.size() > 0 ? schoolList.get(0) : new School();
        Binder<School> binder = new Binder<>();
        binder.setBean(school);

        TextField tfName = new TextField("Nama Sekolah");
        tfName.setRequiredIndicatorVisible(true);
        tfName.setWidth(WIDTH, Unit.PIXELS);
        tfName.setMaxLength(200);
        binder
	    	.forField(tfName)
	    	.withValidator(input -> input != null && !input.isEmpty(), "Sila masukkan nama sekolah")
	    	.bind(School::getName, School::setName);

        TextField tfShortName = new TextField("Kependekan Nama Sekolah");
        tfShortName.setRequiredIndicatorVisible(true);
        tfShortName.setWidth(WIDTH, Unit.PIXELS);
        tfShortName.setMaxLength(50);
        binder
	    	.forField(tfShortName)
	    	.withValidator(input -> input != null && !input.isEmpty(), "Sila masukkan kependekan nama sekolah")
	    	.bind(School::getShortName, School::setShortName);

        TextField tfAddress1 = new TextField("Alamat 1");
        tfAddress1.setRequiredIndicatorVisible(false);
        tfAddress1.setWidth(WIDTH, Unit.PIXELS);
        tfAddress1.setMaxLength(255);
        binder
	    	.forField(tfAddress1)
	    	.bind(School::getAddress1, School::setAddress1);

        TextField tfAddress2 = new TextField("Alamat 2");
        tfAddress2.setRequiredIndicatorVisible(false);
        tfAddress2.setWidth(WIDTH, Unit.PIXELS);
        tfAddress2.setMaxLength(255);
        binder
	    	.forField(tfAddress2)
	    	.bind(School::getAddress2, School::setAddress2);

        TextField tfPoscode = new TextField("Poskod");
        tfPoscode.setRequiredIndicatorVisible(false);
        tfPoscode.setWidth(WIDTH, Unit.PIXELS);
        tfPoscode.setMaxLength(5);
        binder
	    	.forField(tfPoscode)
	    	.withConverter(Integer::valueOf, String::valueOf)
	    	.bind(School::getPoscode, School::setPoscode);

        TextField tfTown = new TextField("Bandar");
        tfTown.setRequiredIndicatorVisible(false);
        tfTown.setWidth(WIDTH, Unit.PIXELS);
        tfTown.setMaxLength(50);
        binder
	    	.forField(tfTown)
	    	.bind(School::getTown, School::setTown);

        TextField tfState = new TextField("Negeri");
        tfState.setRequiredIndicatorVisible(false);
        tfState.setWidth(WIDTH, Unit.PIXELS);
        tfState.setMaxLength(50);
        binder
	    	.forField(tfState)
	    	.bind(School::getState, School::setState);

        formLayout.addComponent(tfName);
        formLayout.addComponent(tfShortName);
        formLayout.addComponent(tfAddress1);
        formLayout.addComponent(tfAddress2);
        formLayout.addComponent(tfPoscode);
        formLayout.addComponent(tfTown);
        formLayout.addComponent(tfState);

        Button btnSave = new Button("Kemaskini");
        btnSave.addClickListener(evt ->{
        	Boolean isValid = binder.writeBeanIfValid(school);
        	if(isValid != null && isValid) {
        		School saveItem = schoolRepo.save(school);
        		binder.setBean(saveItem);
        		Notification.show("Kemaskini maklumat sekolah telah berjaya.", Type.HUMANIZED_MESSAGE);
        	}else {
        		Notification.show("Kemaskini maklumat sekolah tidak berjaya.", Type.ERROR_MESSAGE);
        	}
        });

        Button btnCancel = new Button("Batal");
        btnCancel.addClickListener(evt ->{

        });

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponent(btnSave);
        horizontalLayout.addComponent(btnCancel);
        formLayout.addComponent(horizontalLayout);
        mainLayout.addComponent(label);
        mainLayout.addComponent(formLayout);

		return mainLayout;
	}

	private VerticalLayout configureClassRoomTab() {

		VerticalLayout mainLayout = new VerticalLayout();

        Label label = new Label("Skrin untuk mengemaskini maklumat kategori kelas.");

        Button btnNew = new Button(VaadinIcons.PLUS);
        Button btnDelete = new Button(VaadinIcons.TRASH);
        btnDelete.setEnabled(false);
        HorizontalLayout buttonBar = new HorizontalLayout(btnNew, btnDelete);

        Grid<ClassRoom> grid = new Grid<>();
        ListDataProvider<ClassRoom> dataProvider = DataProvider.ofCollection(classRoomRepo.findAll());
        grid.setDataProvider(dataProvider);
        grid.getEditor().setEnabled(true);
        grid.setSizeFull();

        TextField tfName = new TextField();
        tfName.setWidth(70, Unit.PERCENTAGE);
        tfName.setMaxLength(50);
        tfName.setRequiredIndicatorVisible(true);
        grid.addColumn(ClassRoom::getName).setCaption("Nama")
        .setEditorComponent(tfName, ClassRoom::setName)
        .setSortable(true);

        NumberField nfLevel = new NumberField();
        nfLevel.setWidth(30, Unit.PERCENTAGE);
        nfLevel.setRequiredIndicatorVisible(true);
        nfLevel.setMaxLength(2);
        grid.addColumn(ClassRoom::getLevel).setCaption("Tahap")
        .setEditorBinding(grid.getEditor().getBinder()
        		.forField(nfLevel)
    	    	.withConverter(Integer::valueOf, String::valueOf)
    	    	.bind(ClassRoom::getLevel, ClassRoom::setLevel))
        .setSortable(true);

        grid.getEditor().addSaveListener(evt -> {
        	try {
                ClassRoom item = evt.getBean();
                classRoomRepo.save(item);
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
	                ClassRoom item = grid.getSelectedItems().iterator().next();
	                classRoomRepo.delete(item);
	                dataProvider.getItems().remove(item);
	                dataProvider.refreshAll();
	            }
        	}catch (Exception e) {
        		 Notification.show("Rekod tidak berjaya dipadam.", Notification.Type.ERROR_MESSAGE);
			}
        });

        btnNew.addClickListener(evt -> {
        	ClassRoom classRoom = new ClassRoom();
        	classRoom.setLevel(0);
        	dataProvider.getItems().add(classRoom);
            dataProvider.refreshAll();
        });

        mainLayout.addComponent(label);
        mainLayout.addComponent(buttonBar);
        mainLayout.addComponent(grid);

        return mainLayout;
	}

	private VerticalLayout configureTeacherTab() {

		VerticalLayout mainLayout = new VerticalLayout();

        Label label = new Label("Skrin untuk mengemaskini maklumat Pengajar. Sila masukkan medan bertanda * dan tekan butang Kemaskini.");

        Button btnNew = new Button(VaadinIcons.PLUS);
        Button btnDelete = new Button(VaadinIcons.TRASH);
        btnDelete.setEnabled(false);
        HorizontalLayout buttonBar = new HorizontalLayout(btnNew, btnDelete);

        Grid<Teacher> grid = new Grid<>();
        ListDataProvider<Teacher> dataProvider = DataProvider.ofCollection(teacherRepo.findAll());
        grid.setDataProvider(dataProvider);
        grid.getEditor().setEnabled(true);
        grid.setSizeFull();

        ComboBox<String> cbSalute = new ComboBox<>();
        cbSalute.setItems(Arrays.asList(Salutation.values()).stream().map(m ->m.name()).collect(Collectors.toList()));
        grid.addColumn(Teacher::getSalutation).setCaption("Gelaran")
        .setEditorComponent(cbSalute, Teacher::setSalutation);

        TextField tfName = new TextField();
        tfName.setMaxLength(50);
        tfName.setRequiredIndicatorVisible(true);
        grid.addColumn(Teacher::getName).setCaption("Nama")
        .setEditorComponent(tfName, Teacher::setName)
        .setSortable(true);

        grid.getEditor().addSaveListener(evt -> {
        	try {
                Teacher item = evt.getBean();
                teacherRepo.save(item);
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
	                Teacher item = grid.getSelectedItems().iterator().next();
	                teacherRepo.delete(item);
	                dataProvider.getItems().remove(item);
	                dataProvider.refreshAll();
	            }
        	}catch (Exception e) {
        		 Notification.show("Rekod tidak berjaya dipadam.", Notification.Type.ERROR_MESSAGE);
			}
        });

        btnNew.addClickListener(evt -> {
        	dataProvider.getItems().add(new Teacher());
            dataProvider.refreshAll();
        });

        mainLayout.addComponent(label);
        mainLayout.addComponent(buttonBar);
        mainLayout.addComponent(grid);

        return mainLayout;
	}

	private VerticalLayout configureClassRoomDetailTab() {

		VerticalLayout mainLayout = new VerticalLayout();

        Label label = new Label("Skrin untuk mengemaskini maklumat Kelas.");

        Button btnNew = new Button(VaadinIcons.PLUS);
        Button btnDelete = new Button(VaadinIcons.TRASH);
        Button btnRefresh = new Button(VaadinIcons.REFRESH);
        btnDelete.setEnabled(false);
        HorizontalLayout buttonBar = new HorizontalLayout(btnNew, btnDelete, btnRefresh);

        Grid<ClassRoomDetail> grid = new Grid<>();
        ListDataProvider<ClassRoomDetail> dataProvider = DataProvider.ofCollection(classRoomDetailRepo.findAllWithOrder());
        grid.setDataProvider(dataProvider);
        grid.getEditor().setEnabled(true);
        grid.setSizeFull();

        TextField tfName = new TextField();
        tfName.setMaxLength(50);
        tfName.setRequiredIndicatorVisible(true);
        grid.addColumn(ClassRoomDetail::getName).setCaption("Nama")
        .setEditorComponent(tfName, ClassRoomDetail::setName)
        .setSortable(true);

        ComboBox<ClassRoom> cbClassRoom = new ComboBox<>();
        cbClassRoom.setDataProvider(DataProvider.ofCollection(classRoomRepo.findAll()));
        cbClassRoom.setItemCaptionGenerator(item -> item.getName());
        grid.addColumn(ClassRoomDetail::getClassRoom, item -> item !=  null ? item.getName() : "").setCaption("Kategori Kelas")
        .setEditorComponent(cbClassRoom, ClassRoomDetail::setClassRoom);

        ComboBox<Teacher> cbTeacher = new ComboBox<>();
        cbTeacher.setDataProvider(DataProvider.ofCollection(teacherRepo.findAll()));
        cbTeacher.setItemCaptionGenerator(item -> item.getSalutation() + " " + item.getName());
        grid.addColumn(ClassRoomDetail::getTeacher, item -> item !=  null ? item.getSalutation() + " " + item.getName() : "").setCaption("Pengajar")
        .setEditorComponent(cbTeacher, ClassRoomDetail::setTeacher)
        .setDescriptionGenerator(item -> item.getPkid()+"");


		btnRefresh.addClickListener(listener -> {
			cbClassRoom.setDataProvider(DataProvider.ofCollection(classRoomRepo.findAll()));
			cbTeacher.setDataProvider(DataProvider.ofCollection(teacherRepo.findAll()));
			grid.setDataProvider(DataProvider.ofCollection(classRoomDetailRepo.findAllWithOrder()));
		});

        grid.getEditor().addSaveListener(evt -> {
        	try {
        		ClassRoomDetail item = evt.getBean();
                classRoomDetailRepo.save(item);
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
	        		ClassRoomDetail item = grid.getSelectedItems().iterator().next();
	                classRoomDetailRepo.delete(item);
	                dataProvider.getItems().remove(item);
	                dataProvider.refreshAll();
	            }
        	}catch (Exception e) {
        		 Notification.show("Rekod tidak berjaya dipadam.", Notification.Type.ERROR_MESSAGE);
			}
        });

        btnNew.addClickListener(evt -> {
        	dataProvider.getItems().add(new ClassRoomDetail());
            dataProvider.refreshAll();
        });

        mainLayout.addComponent(label);
        mainLayout.addComponent(buttonBar);
        mainLayout.addComponent(grid);

        return mainLayout;
	}

	private VerticalLayout configureGeneralTab() {

		VerticalLayout mainLayout = new VerticalLayout();

        Label label = new Label("Skrin untuk mengemaskini maklumat am.");

        ComboBox<GeneralCodeCategory> cbCategory = new ComboBox<>("Kategori");
        cbCategory.setWidth(WIDTH, Unit.PIXELS);
        cbCategory.setItems(Arrays.asList(GeneralCodeCategory.values()));
        cbCategory.setItemCaptionGenerator(item -> item.getDescription());

        Button btnNew = new Button(VaadinIcons.PLUS);
        Button btnDelete = new Button(VaadinIcons.TRASH);
        btnDelete.setEnabled(false);
        FormLayout cbBar = new FormLayout(cbCategory);
        HorizontalLayout buttonBar = new HorizontalLayout(btnNew, btnDelete);

        Grid<GeneralCode> grid = new Grid<>();
        ListDataProvider<GeneralCode> dataProvider = DataProvider.ofCollection(new ArrayList<GeneralCode>());
        grid.setDataProvider(dataProvider);
        grid.getEditor().setEnabled(true);
        grid.setSizeFull();

        TextField tfCode = new TextField();
        tfCode.setMaxLength(50);
        tfCode.setRequiredIndicatorVisible(true);
        grid.addColumn(GeneralCode::getCode).setCaption("Kod")
        .setEditorComponent(tfCode, GeneralCode::setCode)
        .setSortable(true);

        TextField tfDesc = new TextField();
        tfDesc.setMaxLength(200);
        tfDesc.setRequiredIndicatorVisible(true);
        grid.addColumn(GeneralCode::getDescription).setCaption("Perihal")
        .setEditorComponent(tfDesc, GeneralCode::setDescription)
        .setSortable(true);

        NumberField nfLevel = new NumberField();
        nfLevel.setMaxLength(8);
        nfLevel.setRequiredIndicatorVisible(true);
        nfLevel.setMaxLength(2);
        grid.addColumn(GeneralCode::getLevel).setCaption("Jujukan")
        .setEditorBinding(grid.getEditor().getBinder()
        		.forField(nfLevel)
    	    	.withConverter(Integer::valueOf, String::valueOf)
    	    	.bind(GeneralCode::getLevel, GeneralCode::setLevel))
        .setSortable(true);

        cbCategory.addValueChangeListener(listener -> {
        	GeneralCodeCategory codeCategory = listener.getValue();
        	if(codeCategory != null) {
        		List<GeneralCode> list = generalCodeRepo.findByCategoryOrderByLevelAsc(codeCategory.name());
        		dataProvider.getItems().clear();
        		dataProvider.getItems().addAll(list != null ? list : new ArrayList<GeneralCode>());
        		dataProvider.refreshAll();
        	}
        });

        grid.getEditor().addSaveListener(evt -> {
        	try {
                GeneralCode item = evt.getBean();
                generalCodeRepo.save(item);
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
	        		GeneralCode item = grid.getSelectedItems().iterator().next();
	        		generalCodeRepo.delete(item);
	                dataProvider.getItems().remove(item);
	                dataProvider.refreshAll();
	            }
        	}catch (Exception e) {
        		 Notification.show("Rekod tidak berjaya dipadam.", Notification.Type.ERROR_MESSAGE);
			}
        });

        btnNew.addClickListener(evt -> {
        	if(cbCategory.getSelectedItem() != null && cbCategory.getSelectedItem().get() != null) {
	        	GeneralCode generalCode = new GeneralCode();
	        	generalCode.setLevel(0);
	        	generalCode.setCategory(cbCategory.getSelectedItem().get().name());
	        	dataProvider.getItems().add(generalCode);
	            dataProvider.refreshAll();
        	}else {
        		Notification.show("Sila pilih kategori kod.", Notification.Type.WARNING_MESSAGE);
        	}
        });

        mainLayout.addComponent(label);
        mainLayout.addComponent(cbBar);
        mainLayout.addComponent(buttonBar);
        mainLayout.addComponent(grid);

        return mainLayout;
	}
}
