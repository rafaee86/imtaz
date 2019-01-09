package com.mz.imtaz.view;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.vaadin.ui.NumberField;

import com.mz.imtaz.entity.Bank;
import com.mz.imtaz.entity.ClassRoom;
import com.mz.imtaz.entity.ClassRoomDetail;
import com.mz.imtaz.entity.DailyRecordDiscipline;
import com.mz.imtaz.entity.DailyRecordItem;
import com.mz.imtaz.entity.Discipline;
import com.mz.imtaz.entity.GeneralCode;
import com.mz.imtaz.entity.MiscEntity;
import com.mz.imtaz.entity.PaymentDescription;
import com.mz.imtaz.entity.RecordUtility;
import com.mz.imtaz.entity.School;
import com.mz.imtaz.entity.Teacher;
import com.mz.imtaz.entity.UserContext;
import com.mz.imtaz.enums.MiscEntityCategory;
import com.mz.imtaz.enums.Salutation;
import com.mz.imtaz.repository.BankRepository;
import com.mz.imtaz.repository.ClassRoomDetailRepository;
import com.mz.imtaz.repository.DailyRecordDisciplineRepository;
import com.mz.imtaz.repository.DailyRecordItemRepository;
import com.mz.imtaz.repository.DisciplineRepository;
import com.mz.imtaz.repository.GeneralCodeRepository;
import com.mz.imtaz.repository.MiscEntityRepository;
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
	
	private final Logger logger = Logger.getLogger(ConfigureView.class.getName());

	enum TabType {
		SCHOOL, CLASSROOM_DETAIL, TEACHER, DISCIPLINE, RECORD_DAILY_DISCIPLINE, STUDENT_ACTIVITY, PAYMENT_DESCRIPTION, BANK, GENERAL, MISCENTITY;
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
	private TeacherRepository teacherRepo;
	@Autowired
	private ClassRoomDetailRepository classRoomDetailRepo;
	@Autowired
	private DisciplineRepository disciplineRepo;
	@Autowired
	private DailyRecordDisciplineRepository dailyRecordDisciplineRepo;
	@Autowired
	private DailyRecordItemRepository dailyRecordItemRepo;
	@Autowired
	private PaymentDescriptionRepository paymentDescRepo;
	@Autowired
	private BankRepository bankRepo;
	@Autowired
	private GeneralCodeRepository generalCodeRepo;
	@Autowired
	private MiscEntityRepository miscEntityRepo;
		
	private Map<String, Object> dataProviderMap = new HashMap<String, Object>();

	@PostConstruct
    public void init() {

		TabSheet tab = new TabSheet();
		tab.setHeight(100.0f, Unit.PERCENTAGE);
		tab.addStyleName(ValoTheme.TABSHEET_FRAMED);
		tab.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);

		tab.addTab(getTabContent(TabType.SCHOOL.name()), "Sekolah");
		tab.addTab(getTabContent(TabType.TEACHER.name()), "Pengajar").setId(TabType.TEACHER.name());
		tab.addTab(getTabContent(TabType.CLASSROOM_DETAIL.name()), "Kelas").setId(TabType.CLASSROOM_DETAIL.name());
		tab.addTab(getTabContent(TabType.DISCIPLINE.name()), "Disiplin").setId(TabType.DISCIPLINE.name());
		tab.addTab(getTabContent(TabType.RECORD_DAILY_DISCIPLINE.name()), "Disiplin (Rekod Harian)").setId(TabType.RECORD_DAILY_DISCIPLINE.name());
		tab.addTab(getTabContent(TabType.STUDENT_ACTIVITY.name()), "Item Rekod Harian Pelajar").setId(TabType.STUDENT_ACTIVITY.name());
		tab.addTab(getTabContent(TabType.PAYMENT_DESCRIPTION.name()), "Perihal Bayaran").setId(TabType.PAYMENT_DESCRIPTION.name());
		tab.addTab(getTabContent(TabType.BANK.name()), "Bank").setId(TabType.BANK.name());
		tab.addTab(getTabContent(TabType.GENERAL.name()), "Am").setId(TabType.GENERAL.name());
		tab.addTab(getTabContent(TabType.MISCENTITY.name()), "Lain-Lain").setId(TabType.MISCENTITY.name());
		
		tab.addSelectedTabChangeListener(listener -> {
			String id = listener.getTabSheet().getId();
			tabListener(id);
		});

		addComponent(tab);
	}
	
	private void tabListener(String id) {
		if(TabType.TEACHER.name().equals(id)) {
			dataProviderMap.put(id, DataProvider.ofCollection(teacherRepo.findAllActive(Sort.by(Sort.Direction.ASC, "name"))));
		}else if(TabType.CLASSROOM_DETAIL.name().equals(id)) {
			dataProviderMap.put(id, DataProvider.ofCollection(classRoomDetailRepo.findAllWithOrder()));
		}else if(TabType.DISCIPLINE.name().equals(id)) {
			dataProviderMap.put(id, DataProvider.ofCollection(disciplineRepo.findAllActive(Sort.by(Sort.Direction.ASC, "description"))));
		}else if(TabType.RECORD_DAILY_DISCIPLINE.name().equals(id)) {
			dataProviderMap.put(id, DataProvider.ofCollection(dailyRecordDisciplineRepo.findAllActive(Sort.by(Sort.Direction.ASC, "description"))));
		}else if(TabType.STUDENT_ACTIVITY.name().equals(id)) {
			dataProviderMap.put(id, DataProvider.ofCollection(dailyRecordItemRepo.findAllActive(Sort.by(Sort.Direction.ASC,"sequence"))));
		}else if(TabType.PAYMENT_DESCRIPTION.name().equals(id)) {
			dataProviderMap.put(id, DataProvider.ofCollection(paymentDescRepo.findAllActive(Sort.by(Sort.Direction.ASC, "description"))));
		}else if(TabType.BANK.name().equals(id)) {
			dataProviderMap.put(id, DataProvider.ofCollection(bankRepo.findAllActive(Sort.by(Sort.Direction.ASC, "name"))));
		}else if(TabType.GENERAL.name().equals(id)) {
			dataProviderMap.put(id, DataProvider.ofCollection(new ArrayList<GeneralCode>()));
		}else if(TabType.MISCENTITY.name().equals(id)) {
			
		}
	}

	private VerticalLayout getTabContent(String id) {

		VerticalLayout layout = null;
		if(TabType.SCHOOL.name().equalsIgnoreCase(id)) {
			layout = configureSchoolTab();
		}else if(TabType.CLASSROOM_DETAIL.name().equalsIgnoreCase(id)) {
			layout = configureClassRoomDetailTab();
		}else if(TabType.TEACHER.name().equalsIgnoreCase(id)) {
			layout = configureTeacherTab();
		}else if(TabType.DISCIPLINE.name().equalsIgnoreCase(id)) {
			layout = configureDisciplineTab();
		}else if(TabType.RECORD_DAILY_DISCIPLINE.name().equalsIgnoreCase(id)) {
			layout = configureDailyRecordDisciplineTab();
		}else if(TabType.STUDENT_ACTIVITY.name().equalsIgnoreCase(id)) {
			layout = configureStudentActivityTab();
		}else if(TabType.PAYMENT_DESCRIPTION.name().equalsIgnoreCase(id)) {
			layout = configurePaymentDescriptionTab();
		}else if(TabType.BANK.name().equalsIgnoreCase(id)) {
			layout = configureBankTab();
		}else if(TabType.GENERAL.name().equalsIgnoreCase(id)) {
			layout = configureGeneralTab();
		}else if(TabType.MISCENTITY.name().equalsIgnoreCase(id)) {
			layout = configureMiscEntityTab();
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
        		UserContext userContext = Helper.getUserContext();
        		school.setRecordUtility(new RecordUtility(userContext.getPkid()));
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
        cbSalute.setItems(Arrays.asList(Salutation.values()).stream().map(m ->m.getDescription()).collect(Collectors.toList()));
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
        		UserContext userContext = Helper.getUserContext();
                Teacher item = evt.getBean();
                item.setRecordUtility(new RecordUtility(userContext.getPkid()));
                teacherRepo.save(item);
                dataProvider.refreshAll();
            } catch (Exception e) {
                Notification.show("Rekod tidak berjaya dikemaskini.", Notification.Type.ERROR_MESSAGE);
            }
		});
        
        grid.getEditor().addCancelListener(evt -> {
        	Helper.removeGrid(dataProvider, evt.getBean(), evt.getBean().getPkid() == null);
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
        		UserContext userContext = Helper.getUserContext();
	        	if (!grid.getSelectedItems().isEmpty()) {
	                Teacher item = grid.getSelectedItems().iterator().next();
	                item.getRecordUtility().disabled(userContext.getPkid());
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
        ListDataProvider<ClassRoomDetail> dataProvider = (ListDataProvider<ClassRoomDetail>) dataProviderMap.get(TabType.CLASSROOM_DETAIL.name());
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
        cbClassRoom.setDataProvider(DataProvider.ofCollection(Arrays.asList(ClassRoom.values())));
        cbClassRoom.setItemCaptionGenerator(item -> item.getDescription());
        grid.addColumn(ClassRoomDetail::getClassRoom, item -> item !=  null ? item.getDescription() : "").setCaption("Kategori Kelas")
        .setEditorComponent(cbClassRoom, ClassRoomDetail::setClassRoom);

        ComboBox<Teacher> cbTeacher = new ComboBox<>();
        cbTeacher.setDataProvider(DataProvider.ofCollection(teacherRepo.findAllActive(Sort.by(Direction.ASC, "name"))));
        cbTeacher.setItemCaptionGenerator(item -> item.getSalutation() + " " + item.getName());
        grid.addColumn(ClassRoomDetail::getTeacher, item -> item !=  null ? item.getSalutation() + " " + item.getName() : "").setCaption("Pengajar")
        .setEditorComponent(cbTeacher, ClassRoomDetail::setTeacher)
        .setDescriptionGenerator(item -> item.getPkid()+"");

		btnRefresh.addClickListener(listener -> {
			cbTeacher.setDataProvider(DataProvider.ofCollection(teacherRepo.findAllActive(Sort.by(Direction.ASC, "name"))));
		});

        grid.getEditor().addSaveListener(evt -> {
    		UserContext userContext = Helper.getUserContext();
        	try {
        		ClassRoomDetail item = evt.getBean();
        		item.setRecordUtility(new RecordUtility(userContext.getPkid()));
                classRoomDetailRepo.save(item);
                dataProvider.refreshAll();
            } catch (Exception e) {
                Notification.show("Rekod tidak berjaya dikemaskini.", Notification.Type.ERROR_MESSAGE);
            }
		});
        
        grid.getEditor().addCancelListener(evt -> {
        	Helper.removeGrid(dataProvider, evt.getBean(), evt.getBean().getPkid() == null);
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
        		UserContext userContext = Helper.getUserContext();
	        	if (!grid.getSelectedItems().isEmpty()) {
	        		ClassRoomDetail item = grid.getSelectedItems().iterator().next();
	        		item.getRecordUtility().disabled(userContext.getPkid());
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
	private VerticalLayout configureDisciplineTab() {

		VerticalLayout mainLayout = new VerticalLayout();

        Button btnNew = new Button(VaadinIcons.PLUS);
        Button btnDelete = new Button(VaadinIcons.TRASH);
        btnDelete.setEnabled(false);
        HorizontalLayout buttonBar = new HorizontalLayout(btnNew, btnDelete);

        Grid<Discipline> grid = new Grid<>();
        tabListener(TabType.DISCIPLINE.name());
        ListDataProvider<Discipline> dataProvider = (ListDataProvider<Discipline>) dataProviderMap.get(TabType.DISCIPLINE.name());
        grid.setDataProvider(dataProvider);
        grid.getEditor().setEnabled(true);
        grid.setSizeFull();
        
        TextField tfDesc = new TextField();
        tfDesc.setWidth(70, Unit.PERCENTAGE);
        tfDesc.setMaxLength(50);
        tfDesc.setRequiredIndicatorVisible(true);
        grid.addColumn(Discipline::getDescription).setCaption("Perihal")
        .setEditorComponent(tfDesc, Discipline::setDescription)
        .setSortable(true);

        grid.getEditor().addSaveListener(evt -> {
        	try {
        		UserContext userContext = Helper.getUserContext();
                Discipline item = evt.getBean();
        		item.setRecordUtility(new RecordUtility(userContext.getPkid()));
                disciplineRepo.save(item);
                dataProvider.refreshAll();
            } catch (Exception e) {
                Notification.show("Rekod tidak berjaya dikemaskini.", Notification.Type.ERROR_MESSAGE);
            }
		});
        
        grid.getEditor().addCancelListener(evt -> {
        	Helper.removeGrid(dataProvider, evt.getBean(), evt.getBean().getPkid() == null);
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
        		UserContext userContext = Helper.getUserContext();
	        	if (!grid.getSelectedItems().isEmpty()) {
	                Discipline item = grid.getSelectedItems().iterator().next();
	        		item.getRecordUtility().disabled(userContext.getPkid());
	                if(item.getPkid() != null)disciplineRepo.save(item);
	                dataProvider.getItems().remove(item);
	                dataProvider.refreshAll();
	            }
        	}catch (Exception e) {
        		 Notification.show("Rekod tidak berjaya dipadam.", Notification.Type.ERROR_MESSAGE);
			}
        });

        btnNew.addClickListener(evt -> {
        	dataProvider.getItems().add(new Discipline());
            dataProvider.refreshAll();
        });

        mainLayout.addComponent(buttonBar);
        mainLayout.addComponent(grid);

        return mainLayout;
	}
	
	@SuppressWarnings("unchecked")
	private VerticalLayout configureDailyRecordDisciplineTab() {

		VerticalLayout mainLayout = new VerticalLayout();

        Button btnNew = new Button(VaadinIcons.PLUS);
        Button btnDelete = new Button(VaadinIcons.TRASH);
        btnDelete.setEnabled(false);
        HorizontalLayout buttonBar = new HorizontalLayout(btnNew, btnDelete);

        Grid<DailyRecordDiscipline> grid = new Grid<>();
        tabListener(TabType.RECORD_DAILY_DISCIPLINE.name());
        ListDataProvider<DailyRecordDiscipline> dataProvider = (ListDataProvider<DailyRecordDiscipline>) dataProviderMap.get(TabType.RECORD_DAILY_DISCIPLINE.name());
        grid.setDataProvider(dataProvider);
        grid.getEditor().setEnabled(true);
        grid.setSizeFull();
        
        TextField tfCode = new TextField();
        tfCode.setWidth(70, Unit.PERCENTAGE);
        tfCode.setMaxLength(50);
        tfCode.setRequiredIndicatorVisible(true);
        grid.addColumn(DailyRecordDiscipline::getCode).setCaption("Kod")
        .setEditorComponent(tfCode, DailyRecordDiscipline::setCode);
        
        TextField tfDesc = new TextField();
        tfDesc.setWidth(70, Unit.PERCENTAGE);
        tfDesc.setMaxLength(200);
        tfDesc.setRequiredIndicatorVisible(true);
        grid.addColumn(DailyRecordDiscipline::getDescription).setCaption("Perihal")
        .setEditorComponent(tfDesc, DailyRecordDiscipline::setDescription)
        .setSortable(true);

        grid.getEditor().addSaveListener(evt -> {
        	try {
        		UserContext userContext = Helper.getUserContext();
        		DailyRecordDiscipline item = evt.getBean();
        		item.setRecordUtility(new RecordUtility(userContext.getPkid()));
        		dailyRecordDisciplineRepo.save(item);
                dataProvider.refreshAll();
            } catch (Exception e) {
                Notification.show("Rekod tidak berjaya dikemaskini.", Notification.Type.ERROR_MESSAGE);
            }
		});
        
        grid.getEditor().addCancelListener(evt -> {
        	Helper.removeGrid(dataProvider, evt.getBean(), evt.getBean().getPkid() == null);
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
        		UserContext userContext = Helper.getUserContext();
	        	if (!grid.getSelectedItems().isEmpty()) {
	        		DailyRecordDiscipline item = grid.getSelectedItems().iterator().next();
	        		item.getRecordUtility().disabled(userContext.getPkid());
	                if(item.getPkid() != null)dailyRecordDisciplineRepo.save(item);
	                dataProvider.getItems().remove(item);
	                dataProvider.refreshAll();
	            }
        	}catch (Exception e) {
        		 Notification.show("Rekod tidak berjaya dipadam.", Notification.Type.ERROR_MESSAGE);
			}
        });

        btnNew.addClickListener(evt -> {
        	dataProvider.getItems().add(new DailyRecordDiscipline());
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
        		UserContext userContext = Helper.getUserContext();
        		DailyRecordItem item = evt.getBean();
        		item.setRecordUtility(new RecordUtility(userContext.getPkid()));
        		dailyRecordItemRepo.save(item);
                dataProvider.refreshAll();
            } catch (Exception e) {
                Notification.show("Rekod tidak berjaya dikemaskini.", Notification.Type.ERROR_MESSAGE);
            }
		});
        
        grid.getEditor().addCancelListener(evt -> {
        	Helper.removeGrid(dataProvider, evt.getBean(), evt.getBean().getPkid() == null);
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
        		UserContext userContext = Helper.getUserContext();
	        	if (!grid.getSelectedItems().isEmpty()) {
	        		DailyRecordItem item = grid.getSelectedItems().iterator().next();
	        		item.getRecordUtility().disabled(userContext.getPkid());
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
        		UserContext userContext = Helper.getUserContext();
        		PaymentDescription item = evt.getBean();
        		item.setRecordUtility(new RecordUtility(userContext.getPkid()));
                paymentDescRepo.save(item);
                dataProvider.refreshAll();
            } catch (Exception e) {
                Notification.show("Rekod tidak berjaya dikemaskini.", Notification.Type.ERROR_MESSAGE);
            }
		});
        
        grid.getEditor().addCancelListener(evt -> {
        	Helper.removeGrid(dataProvider, evt.getBean(), evt.getBean().getPkid() == null);
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
        		UserContext userContext = Helper.getUserContext();
	        	if (!grid.getSelectedItems().isEmpty()) {
	        		PaymentDescription item = grid.getSelectedItems().iterator().next();
	                item.getRecordUtility().disabled(userContext.getPkid());
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
        		UserContext userContext = Helper.getUserContext();
                Bank item = evt.getBean();
        		item.setRecordUtility(new RecordUtility(userContext.getPkid()));
                bankRepo.save(item);
                dataProvider.refreshAll();
            } catch (Exception e) {
                Notification.show("Rekod tidak berjaya dikemaskini.", Notification.Type.ERROR_MESSAGE);
            }
		});
        
        grid.getEditor().addCancelListener(evt -> {
        	Helper.removeGrid(dataProvider, evt.getBean(), evt.getBean().getPkid() == null);
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
        		UserContext userContext = Helper.getUserContext();
	        	if (!grid.getSelectedItems().isEmpty()) {
	                Bank item = grid.getSelectedItems().iterator().next();
	                item.getRecordUtility().disabled(userContext.getPkid());
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
        		UserContext userContext = Helper.getUserContext();
                GeneralCode item = evt.getBean();
        		item.setRecordUtility(new RecordUtility(userContext.getPkid()));
                generalCodeRepo.save(item);
                dataProvider.refreshAll();
            } catch (Exception e) {
                Notification.show("Rekod tidak berjaya dikemaskini.", Notification.Type.ERROR_MESSAGE);
            }
		});
        
        grid.getEditor().addCancelListener(evt -> {
        	Helper.removeGrid(dataProvider, evt.getBean(), evt.getBean().getPkid() == null);
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
        		UserContext userContext = Helper.getUserContext();
	        	if (!grid.getSelectedItems().isEmpty()) {
	        		GeneralCode item = grid.getSelectedItems().iterator().next();
	                item.getRecordUtility().disabled(userContext.getPkid());
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

	private VerticalLayout configureMiscEntityTab() {
		
		VerticalLayout mainLayout = new VerticalLayout();
		
		FormLayout formLayout = new FormLayout();
		
		Binder<MiscEntity> binder1 = new Binder<>();
		Binder<MiscEntity> binder2 = new Binder<>();
		
		MiscEntity miscEntity1 = miscEntityRepo.findOne(
			Example.of(
				MiscEntity.builder()
				.category(MiscEntityCategory.MAX_DISCIPLINE_ALLOWED_TO_OUTING)
				.build()
			)
		).orElse(new MiscEntity(MiscEntityCategory.MAX_DISCIPLINE_ALLOWED_TO_OUTING));
		
		MiscEntity miscEntity2 = miscEntityRepo.findOne(
				Example.of(
					MiscEntity.builder()
					.category(MiscEntityCategory.STUDENT_RUNNINGNUM_PREFIX)
					.build()
				)
			).orElse(new MiscEntity(MiscEntityCategory.STUDENT_RUNNINGNUM_PREFIX));
		
		binder1.setBean(miscEntity1);
		binder2.setBean(miscEntity2);
		
		TextField tfDayOfDisciplineAllowed = new TextField("Bil. Kesalahan Maks. Yang Dibenarkan Outing");
		tfDayOfDisciplineAllowed.setWidth(WIDTH, Unit.PIXELS);
		tfDayOfDisciplineAllowed.setRequiredIndicatorVisible(true);
		binder1
			.forField(tfDayOfDisciplineAllowed)
	    	.withValidator(input -> input != null && !input.isEmpty(), "Sila masukkan nilai " + tfDayOfDisciplineAllowed.getCaption())
			.bind(MiscEntity::getValue1, MiscEntity::setValue1);
		
		TextField tfStudentRunningNumPrefix = new TextField("Awalan Larian Nombor Pelajar");
		tfStudentRunningNumPrefix.setWidth(WIDTH, Unit.PIXELS);
		tfStudentRunningNumPrefix.setRequiredIndicatorVisible(true);
		binder2
			.forField(tfStudentRunningNumPrefix)
	    	.withValidator(input -> input != null && !input.isEmpty(), "Sila masukkan nilai " + tfStudentRunningNumPrefix.getCaption())
			.bind(MiscEntity::getValue1, MiscEntity::setValue1);
		
		Button btnSave = new Button("Simpan");
		btnSave.addClickListener(evt -> {
			Boolean isValid = (
					binder1.writeBeanIfValid(miscEntity1) &&
					binder2.writeBeanIfValid(miscEntity2)
			) && (
					Helper.isNotNull(tfDayOfDisciplineAllowed.getValue()) && 
					Helper.isNotNull(tfStudentRunningNumPrefix.getValue())
			);
			
        	if(isValid != null && isValid) {
        		UserContext userContext = Helper.getUserContext();
        		
        		miscEntity1.setDescription(tfDayOfDisciplineAllowed.getCaption());
        		miscEntity1.setRecordUtility(new RecordUtility(userContext.getPkid()));
        		MiscEntity saveItem = miscEntityRepo.save(miscEntity1);
        		binder1.setBean(saveItem);
        		
        		miscEntity2.setDescription(tfStudentRunningNumPrefix.getCaption());
        		miscEntity2.setRecordUtility(new RecordUtility(userContext.getPkid()));
        		MiscEntity saveItem2 = miscEntityRepo.save(miscEntity2);
        		binder2.setBean(saveItem2);
        		
        		Notification.show("Kemaskini maklumat lain-lain telah berjaya.", Type.HUMANIZED_MESSAGE);
        	}else {
        		Notification.show("Kemaskini maklumat lain-lain tidak berjaya.", Type.ERROR_MESSAGE);
        	}
		});
		
		formLayout.addComponent(tfDayOfDisciplineAllowed);
		formLayout.addComponent(tfStudentRunningNumPrefix);
		
		mainLayout.addComponent(formLayout);
		mainLayout.addComponent(new HorizontalLayout(btnSave));
		
		return mainLayout;
	} 
}
