package com.mz.imtaz.view;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.vaadin.ui.NumberField;

import com.mz.imtaz.entity.Bank;
import com.mz.imtaz.entity.ClassRoom;
import com.mz.imtaz.entity.ClassRoomDetail;
import com.mz.imtaz.entity.DailyRecordItem;
import com.mz.imtaz.entity.Dicipline;
import com.mz.imtaz.entity.GeneralCode;
import com.mz.imtaz.entity.PaymentDescription;
import com.mz.imtaz.entity.RecordUtility;
import com.mz.imtaz.entity.School;
import com.mz.imtaz.entity.Teacher;
import com.mz.imtaz.enums.Salutation;
import com.mz.imtaz.repository.BankRepository;
import com.mz.imtaz.repository.ClassRoomDetailRepository;
import com.mz.imtaz.repository.ClassRoomRepository;
import com.mz.imtaz.repository.DailyRecordItemRepository;
import com.mz.imtaz.repository.DiciplineRepository;
import com.mz.imtaz.repository.GeneralCodeRepository;
import com.mz.imtaz.repository.PaymentDescriptionRepository;
import com.mz.imtaz.repository.SchoolRepository;
import com.mz.imtaz.repository.TeacherRepository;
import com.mz.imtaz.util.Helper;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.converter.StringToBigDecimalConverter;
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
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringView(name = ConfigureView.NAME)
public class ConfigureView extends VerticalLayout implements View {

	enum TabType {
		SCHOOL, CLASSROOM, CLASSROOM_DETAIL, TEACHER, DICIPLINE, STUDENT_ACTIVITY, PAYMENT_DESCRIPTION, BANK, GENERAL;
	}

	public enum GeneralCodeCategory {
		STUDENT_STATUS("Status Pelajar"),
		STUDENT_ACADEMIC_STATUS("Kod Status Pengajian Pelajar"),
		STUDENT_GRADE_MONTHLY("Kod Penilaian Keputusan Bulanan");

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
	private DiciplineRepository diciplineRepo;
	@Autowired
	private DailyRecordItemRepository dailyRecordItemRepo;
	@Autowired
	private PaymentDescriptionRepository paymentDescRepo;
	@Autowired
	private BankRepository bankRepo;
	@Autowired
	private GeneralCodeRepository generalCodeRepo;
	
	private Map<String, Object> dataProviderMap = new HashMap<String, Object>();

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
		tab.addTab(getTabContent(TabType.DICIPLINE.name()), "Disiplin").setId(TabType.DICIPLINE.name());
		tab.addTab(getTabContent(TabType.STUDENT_ACTIVITY.name()), "Item Rekod Harian Pelajar").setId(TabType.STUDENT_ACTIVITY.name());
		tab.addTab(getTabContent(TabType.PAYMENT_DESCRIPTION.name()), "Perihal Bayaran").setId(TabType.PAYMENT_DESCRIPTION.name());
		tab.addTab(getTabContent(TabType.BANK.name()), "Bank").setId(TabType.BANK.name());
		tab.addTab(getTabContent(TabType.GENERAL.name()), "Am").setId(TabType.GENERAL.name());
		
		tab.addSelectedTabChangeListener(listener -> {
			String id = listener.getTabSheet().getId();
			tabListener(id);
		});

		addComponent(tab);
	}
	
	private void tabListener(String id) {
		if(TabType.CLASSROOM.name().equals(id)) {
			dataProviderMap.put(id, DataProvider.ofCollection(classRoomRepo.findAllActive(Sort.by(Sort.Direction.ASC, "level"))));
		}else if(TabType.TEACHER.name().equals(id)) {
			dataProviderMap.put(id, DataProvider.ofCollection(teacherRepo.findAllActive(Sort.by(Sort.Direction.ASC, "name"))));
		}else if(TabType.CLASSROOM_DETAIL.name().equals(id)) {
			dataProviderMap.put(id, DataProvider.ofCollection(classRoomDetailRepo.findAllWithOrder()));
		}else if(TabType.DICIPLINE.name().equals(id)) {
			dataProviderMap.put(id, DataProvider.ofCollection(diciplineRepo.findAllActive(Sort.by(Sort.Direction.ASC, "description"))));
		}else if(TabType.STUDENT_ACTIVITY.name().equals(id)) {
			dataProviderMap.put(id, DataProvider.ofCollection(dailyRecordItemRepo.findAllActive(Sort.by(Sort.Direction.ASC,"sequence"))));
		}else if(TabType.PAYMENT_DESCRIPTION.name().equals(id)) {
			dataProviderMap.put(id, DataProvider.ofCollection(paymentDescRepo.findAllActive(Sort.by(Sort.Direction.ASC, "description"))));
		}else if(TabType.BANK.name().equals(id)) {
			dataProviderMap.put(id, DataProvider.ofCollection(bankRepo.findAllActive(Sort.by(Sort.Direction.ASC, "name"))));
		}else if(TabType.GENERAL.name().equals(id)) {
			dataProviderMap.put(id, DataProvider.ofCollection(new ArrayList<GeneralCode>()));
		}
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
		}else if(TabType.DICIPLINE.name().equalsIgnoreCase(id)) {
			layout = configureDiciplineTab();
		}else if(TabType.STUDENT_ACTIVITY.name().equalsIgnoreCase(id)) {
			layout = configureStudentActivityTab();
		}else if(TabType.PAYMENT_DESCRIPTION.name().equalsIgnoreCase(id)) {
			layout = configurePaymentDescriptionTab();
		}else if(TabType.BANK.name().equalsIgnoreCase(id)) {
			layout = configureBankTab();
		}else if(TabType.GENERAL.name().equalsIgnoreCase(id)) {
			layout = configureGeneralTab();
		}

		layout.setId(id);

		return layout;
	}

	private VerticalLayout configureSchoolTab() {

		VerticalLayout mainLayout = new VerticalLayout();
        FormLayout formLayout = new FormLayout();

        List<School> schoolList = schoolRepo.findAll();
        School school = schoolList != null && schoolList.size() > 0 ? schoolList.get(0) : new School();
        if(school.getPoscode() == null)
        	school.setPoscode(0);
        
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
        		school.setRecordUtility(new RecordUtility());
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
        mainLayout.addComponent(formLayout);

		return mainLayout;
	}

	@SuppressWarnings("unchecked")
	private VerticalLayout configureClassRoomTab() {

		VerticalLayout mainLayout = new VerticalLayout();

        Button btnNew = new Button(VaadinIcons.PLUS);
        Button btnDelete = new Button(VaadinIcons.TRASH);
        btnDelete.setEnabled(false);
        HorizontalLayout buttonBar = new HorizontalLayout(btnNew, btnDelete);

        Grid<ClassRoom> grid = new Grid<>();
        tabListener(TabType.CLASSROOM.name());
        ListDataProvider<ClassRoom> dataProvider = (ListDataProvider<ClassRoom>) dataProviderMap.get(TabType.CLASSROOM.name());
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
        		item.setRecordUtility(new RecordUtility());
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
	                item.getRecordUtility().disabled();
	                if(item.getPkid() != null)classRoomRepo.save(item);
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

        mainLayout.addComponent(buttonBar);
        mainLayout.addComponent(grid);

        return mainLayout;
	}

	@SuppressWarnings("unchecked")
	private VerticalLayout configureTeacherTab() {

		VerticalLayout mainLayout = new VerticalLayout();

        Button btnNew = new Button(VaadinIcons.PLUS);
        Button btnDelete = new Button(VaadinIcons.TRASH);
        btnDelete.setEnabled(false);
        HorizontalLayout buttonBar = new HorizontalLayout(btnNew, btnDelete);

        Grid<Teacher> grid = new Grid<>();
        tabListener(TabType.TEACHER.name());
        ListDataProvider<Teacher> dataProvider = (ListDataProvider<Teacher>) dataProviderMap.get(TabType.TEACHER.name());
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
                item.setRecordUtility(new RecordUtility());
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
	                item.getRecordUtility().disabled();
	                if(item.getPkid() != null)teacherRepo.save(item);
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

        mainLayout.addComponent(buttonBar);
        mainLayout.addComponent(grid);

        return mainLayout;
	}

	@SuppressWarnings("unchecked")
	private VerticalLayout configureClassRoomDetailTab() {

		VerticalLayout mainLayout = new VerticalLayout();

        Button btnNew = new Button(VaadinIcons.PLUS);
        Button btnDelete = new Button(VaadinIcons.TRASH);
        Button btnRefresh = new Button(VaadinIcons.REFRESH);
        btnDelete.setEnabled(false);
        HorizontalLayout buttonBar = new HorizontalLayout(btnNew, btnDelete, btnRefresh);
        
        Grid<ClassRoomDetail> grid = new Grid<>();
        tabListener(TabType.CLASSROOM_DETAIL.name());
        ListDataProvider<ClassRoomDetail> dataProvider = (ListDataProvider<ClassRoomDetail>) dataProviderMap.get(TabType.CLASSROOM_DETAIL.name());;
        grid.setDataProvider(dataProvider);
        grid.getEditor().setEnabled(true);
        grid.setSizeFull();

        TextField tfName = new TextField();
        tfName.setMaxLength(50);
        tfName.setRequiredIndicatorVisible(true);
        grid.addColumn(ClassRoomDetail::getName).setCaption("Nama")
        .setEditorComponent(tfName, ClassRoomDetail::setName)
        .setSortable(true);

        grid.addColumn(ClassRoomDetail::getClassRoom).setCaption("Kategori Kelas");
        grid.addComponentColumn(v -> {
        	ComboBox<ClassRoom> cbClassRoom = new ComboBox<>();
            cbClassRoom.setDataProvider(DataProvider.ofCollection(classRoomRepo.findAllActive(Sort.by(Direction.ASC, "level"))));
            cbClassRoom.setItemCaptionGenerator(item -> item.getName());
            return cbClassRoom;
        });

        
        grid.addColumn(ClassRoomDetail::getTeacher).setCaption("Pengajar");
        grid.addComponentColumn(v -> {
        	ComboBox<Teacher> cbTeacher = new ComboBox<>();
            cbTeacher.setDataProvider(DataProvider.ofCollection(teacherRepo.findAllActive(Sort.by(Direction.ASC, "name"))));
            cbTeacher.setItemCaptionGenerator(item -> item.getSalutation() + " " + item.getName());
            return cbTeacher;
        });
        grid.setDescriptionGenerator(item -> item.getPkid()+"");


		btnRefresh.addClickListener(listener -> {
//			cbClassRoom.setDataProvider(DataProvider.ofCollection(classRoomRepo.findAllActive(Sort.by(Direction.ASC, "level"))));
//			cbTeacher.setDataProvider(DataProvider.ofCollection(teacherRepo.findAllActive(Sort.by(Direction.ASC, "name"))));
		});

        grid.getEditor().addSaveListener(evt -> {
        	try {
        		ClassRoomDetail item = evt.getBean();
        		item.setRecordUtility(new RecordUtility());
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
	        		item.getRecordUtility().disabled();
	                if(item.getPkid() != null)classRoomDetailRepo.save(item);
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

        mainLayout.addComponent(buttonBar);
        mainLayout.addComponent(grid);

        return mainLayout;
	}

	@SuppressWarnings("unchecked")
	private VerticalLayout configureDiciplineTab() {

		VerticalLayout mainLayout = new VerticalLayout();

        Button btnNew = new Button(VaadinIcons.PLUS);
        Button btnDelete = new Button(VaadinIcons.TRASH);
        btnDelete.setEnabled(false);
        HorizontalLayout buttonBar = new HorizontalLayout(btnNew, btnDelete);

        Grid<Dicipline> grid = new Grid<>();
        tabListener(TabType.DICIPLINE.name());
        ListDataProvider<Dicipline> dataProvider = (ListDataProvider<Dicipline>) dataProviderMap.get(TabType.DICIPLINE.name());
        grid.setDataProvider(dataProvider);
        grid.getEditor().setEnabled(true);
        grid.setSizeFull();
        
        TextField tfDesc = new TextField();
        tfDesc.setWidth(70, Unit.PERCENTAGE);
        tfDesc.setMaxLength(50);
        tfDesc.setRequiredIndicatorVisible(true);
        grid.addColumn(Dicipline::getDescription).setCaption("Perihal")
        .setEditorComponent(tfDesc, Dicipline::setDescription)
        .setSortable(true);

        grid.getEditor().addSaveListener(evt -> {
        	try {
                Dicipline item = evt.getBean();
        		item.setRecordUtility(new RecordUtility());
                diciplineRepo.save(item);
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
	                Dicipline item = grid.getSelectedItems().iterator().next();
	        		item.getRecordUtility().disabled();
	                if(item.getPkid() != null)diciplineRepo.save(item);
	                dataProvider.getItems().remove(item);
	                dataProvider.refreshAll();
	            }
        	}catch (Exception e) {
        		 Notification.show("Rekod tidak berjaya dipadam.", Notification.Type.ERROR_MESSAGE);
			}
        });

        btnNew.addClickListener(evt -> {
        	dataProvider.getItems().add(new Dicipline());
            dataProvider.refreshAll();
        });

        mainLayout.addComponent(buttonBar);
        mainLayout.addComponent(grid);

        return mainLayout;
	}

	@SuppressWarnings("unchecked")
	private VerticalLayout configureStudentActivityTab(){

		VerticalLayout mainLayout = new VerticalLayout();

        Button btnNew = new Button(VaadinIcons.PLUS);
        Button btnDelete = new Button(VaadinIcons.TRASH);
        btnDelete.setEnabled(false);
        HorizontalLayout buttonBar = new HorizontalLayout(btnNew, btnDelete);

        Grid<DailyRecordItem> grid = new Grid<>();
        tabListener(TabType.STUDENT_ACTIVITY.name());
        ListDataProvider<DailyRecordItem> dataProvider = (ListDataProvider<DailyRecordItem>) dataProviderMap.get(TabType.STUDENT_ACTIVITY.name());
        grid.setDataProvider(dataProvider);
        grid.getEditor().setEnabled(true);
        grid.setSizeFull();

        TextField tfName = new TextField();
        tfName.setWidth(70, Unit.PERCENTAGE);
        tfName.setMaxLength(50);
        tfName.setRequiredIndicatorVisible(true);
        grid.addColumn(DailyRecordItem::getDescription).setCaption("Perihal")
        .setEditorComponent(tfName, DailyRecordItem::setDescription)
        .setSortable(true);

        NumberField nfLevel = new NumberField();
        nfLevel.setWidth(30, Unit.PERCENTAGE);
        nfLevel.setRequiredIndicatorVisible(true);
        nfLevel.setMaxLength(2);
        grid.addColumn(DailyRecordItem::getSequence).setCaption("Susunan")
        .setEditorBinding(grid.getEditor().getBinder()
        		.forField(nfLevel)
    	    	.withConverter(Integer::valueOf, String::valueOf)
    	    	.bind(DailyRecordItem::getSequence, DailyRecordItem::setSequence))
        .setSortable(true);

        grid.getEditor().addSaveListener(evt -> {
        	try {
        		DailyRecordItem item = evt.getBean();
        		item.setRecordUtility(new RecordUtility());
        		dailyRecordItemRepo.save(item);
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
	        		DailyRecordItem item = grid.getSelectedItems().iterator().next();
	        		item.getRecordUtility().disabled();
	                if(item.getPkid() != null)dailyRecordItemRepo.save(item);
	                dataProvider.getItems().remove(item);
	                dataProvider.refreshAll();
	            }
        	}catch (Exception e) {
        		 Notification.show("Rekod tidak berjaya dipadam.", Notification.Type.ERROR_MESSAGE);
			}
        });

        btnNew.addClickListener(evt -> {
        	DailyRecordItem dailyRecordItem = new DailyRecordItem();
        	dailyRecordItem.setSequence(0);
        	dataProvider.getItems().add(dailyRecordItem);
            dataProvider.refreshAll();
        });

        mainLayout.addComponent(buttonBar);
        mainLayout.addComponent(grid);

        return mainLayout;
	}

	@SuppressWarnings("unchecked")
	private VerticalLayout configurePaymentDescriptionTab() {


		final String ERROR_BIGDECIMAL = "Format amaun tidak betul, mesti xxx,xxx.00 dimana x ialah nombor.";

		VerticalLayout mainLayout = new VerticalLayout();

        Button btnNew = new Button(VaadinIcons.PLUS);
        Button btnDelete = new Button(VaadinIcons.TRASH);
        btnDelete.setEnabled(false);
        HorizontalLayout buttonBar = new HorizontalLayout(btnNew, btnDelete);

        Grid<PaymentDescription> grid = new Grid<>();
        tabListener(TabType.PAYMENT_DESCRIPTION.name());
        ListDataProvider<PaymentDescription> dataProvider = (ListDataProvider<PaymentDescription>) dataProviderMap.get(TabType.PAYMENT_DESCRIPTION.name());
        grid.setDataProvider(dataProvider);
        grid.getEditor().setEnabled(true);
        grid.setSizeFull();

        TextField tfDescription = new TextField();
        tfDescription.setWidth(70, Unit.PERCENTAGE);
        tfDescription.setMaxLength(50);
        tfDescription.setRequiredIndicatorVisible(true);
        grid.addColumn(PaymentDescription::getDescription).setCaption("Perihal")
        .setEditorComponent(tfDescription, PaymentDescription::setDescription)
        .setSortable(true);

        NumberField nfAmount = new NumberField();
        nfAmount.setValue("0");
        nfAmount.setWidth(30, Unit.PERCENTAGE);
        nfAmount.setMaxLength(10);
        nfAmount.setMinimumFractionDigits(2);
        nfAmount.setNegativeAllowed(false);
        nfAmount.setGroupingSeparator(',');
        grid.addColumn(PaymentDescription::getAmount, item -> Helper.formatBigDecimal(item)).setCaption("Amaun(RM)")
        .setEditorBinding(grid.getEditor().getBinder()
    		.forField(nfAmount)
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
	    	.bind(PaymentDescription::getAmount, PaymentDescription::setAmount));

        grid.getEditor().addSaveListener(evt -> {
        	try {
        		PaymentDescription item = evt.getBean();
        		item.setRecordUtility(new RecordUtility());
                paymentDescRepo.save(item);
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
	        		PaymentDescription item = grid.getSelectedItems().iterator().next();
	                item.getRecordUtility().disabled();
	                if(item.getPkid() != null)paymentDescRepo.save(item);
	                dataProvider.getItems().remove(item);
	                dataProvider.refreshAll();
	            }
        	}catch (Exception e) {
        		 Notification.show("Rekod tidak berjaya dipadam.", Notification.Type.ERROR_MESSAGE);
			}
        });

        btnNew.addClickListener(evt -> {
        	dataProvider.getItems().add(new PaymentDescription());
            dataProvider.refreshAll();
        });

        mainLayout.addComponent(buttonBar);
        mainLayout.addComponent(grid);

        return mainLayout;
	}
	
	@SuppressWarnings("unchecked")
	private VerticalLayout configureBankTab() {

		VerticalLayout mainLayout = new VerticalLayout();

        Button btnNew = new Button(VaadinIcons.PLUS);
        Button btnDelete = new Button(VaadinIcons.TRASH);
        btnDelete.setEnabled(false);
        HorizontalLayout buttonBar = new HorizontalLayout(btnNew, btnDelete);

        Grid<Bank> grid = new Grid<>();
        tabListener(TabType.BANK.name());
        ListDataProvider<Bank> dataProvider = (ListDataProvider<Bank>) dataProviderMap.get(TabType.BANK.name());
        grid.setDataProvider(dataProvider);
        grid.getEditor().setEnabled(true);
        grid.setSizeFull();

        TextField tfName = new TextField();
        tfName.setWidth(70, Unit.PERCENTAGE);
        tfName.setMaxLength(50);
        tfName.setRequiredIndicatorVisible(true);
        grid.addColumn(Bank::getName).setCaption("Nama")
        .setEditorComponent(tfName, Bank::setName)
        .setSortable(true);

        grid.getEditor().addSaveListener(evt -> {
        	try {
                Bank item = evt.getBean();
        		item.setRecordUtility(new RecordUtility());
                bankRepo.save(item);
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
	                Bank item = grid.getSelectedItems().iterator().next();
	                item.getRecordUtility().disabled();
	                if(item.getPkid() != null)bankRepo.save(item);
	                dataProvider.getItems().remove(item);
	                dataProvider.refreshAll();
	            }
        	}catch (Exception e) {
        		 Notification.show("Rekod tidak berjaya dipadam.", Notification.Type.ERROR_MESSAGE);
			}
        });

        btnNew.addClickListener(evt -> {
        	dataProvider.getItems().add(new Bank());
            dataProvider.refreshAll();
        });

        mainLayout.addComponent(buttonBar);
        mainLayout.addComponent(grid);

        return mainLayout;
	}

	@SuppressWarnings("unchecked")
	private VerticalLayout configureGeneralTab() {

		VerticalLayout mainLayout = new VerticalLayout();

        ComboBox<GeneralCodeCategory> cbCategory = new ComboBox<>("Kategori");
        cbCategory.setWidth(WIDTH, Unit.PIXELS);
        cbCategory.setItems(Arrays.asList(GeneralCodeCategory.values()));
        cbCategory.setItemCaptionGenerator(item -> item.getDescription());
        cbCategory.setEmptySelectionAllowed(false);

        Button btnNew = new Button(VaadinIcons.PLUS);
        Button btnDelete = new Button(VaadinIcons.TRASH);
        btnDelete.setEnabled(false);
        FormLayout cbBar = new FormLayout(cbCategory);
        HorizontalLayout buttonBar = new HorizontalLayout(btnNew, btnDelete);

        Grid<GeneralCode> grid = new Grid<>();
        tabListener(TabType.GENERAL.name());
        ListDataProvider<GeneralCode> dataProvider = (ListDataProvider<GeneralCode>) dataProviderMap.get(TabType.GENERAL.name());
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
        		List<GeneralCode> list = generalCodeRepo.findByCategory(codeCategory.name(), Sort.by(Sort.Direction.ASC, "level"));
        		dataProvider.getItems().clear();
        		dataProvider.getItems().addAll(list != null ? list : new ArrayList<GeneralCode>());
        		dataProvider.refreshAll();
        	}
        });

        grid.getEditor().addSaveListener(evt -> {
        	try {
                GeneralCode item = evt.getBean();
        		item.setRecordUtility(new RecordUtility());
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
	                item.getRecordUtility().disabled();
	                if(item.getPkid() != null)generalCodeRepo.save(item);
	                dataProvider.getItems().remove(item);
	                dataProvider.refreshAll();
	            }
        	}catch (Exception e) {
        		 Notification.show("Rekod tidak berjaya dipadam.", Notification.Type.ERROR_MESSAGE);
			}
        });

        btnNew.addClickListener(evt -> {
        	if(Helper.notNull(cbCategory.getSelectedItem()) != null) {
	        	GeneralCode generalCode = new GeneralCode();
	        	generalCode.setLevel(0);
	        	generalCode.setCategory(Helper.notNull(cbCategory.getSelectedItem()).name());
	        	dataProvider.getItems().add(generalCode);
	            dataProvider.refreshAll();
        	}else {
        		Notification.show("Sila pilih kategori kod.", Notification.Type.WARNING_MESSAGE);
        	}
        });

        mainLayout.addComponent(cbBar);
        mainLayout.addComponent(buttonBar);
        mainLayout.addComponent(grid);

        return mainLayout;
	}
}
