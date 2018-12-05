package com.mz.imtaz.view;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import com.mz.imtaz.entity.ClassRoomDetail;
import com.mz.imtaz.entity.DailyActivity;
import com.mz.imtaz.entity.DailyActivityItem;
import com.mz.imtaz.entity.DailyRecordDiscipline;
import com.mz.imtaz.entity.DailyRecordItem;
import com.mz.imtaz.entity.RecordUtility;
import com.mz.imtaz.entity.Records;
import com.mz.imtaz.entity.Student;
import com.mz.imtaz.entity.UserContext;
import com.mz.imtaz.repository.ClassRoomDetailRepository;
import com.mz.imtaz.repository.DailyActivityItemRepository;
import com.mz.imtaz.repository.DailyActivityRepository;
import com.mz.imtaz.repository.DailyRecordDisciplineRepository;
import com.mz.imtaz.repository.DailyRecordItemRepository;
import com.mz.imtaz.repository.RecordsRepository;
import com.mz.imtaz.repository.StudentRepository;
import com.mz.imtaz.util.Helper;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = DailyActivityRecordView.NAME)
public class DailyActivityRecordView extends VerticalLayout implements View{
	
	Logger logger = Logger.getLogger(DailyActivityRecordView.class.getName());
	
	public static final String NAME = "DailyActivityRecordView";

	private final static float WIDTH = 500f;
	final int page = 0;
	final int limit = 10;

	@Autowired
	private DailyActivityRepository dailyActivityRepo;
	@Autowired
	private DailyActivityItemRepository dailyActivityItemRepo;
	@Autowired
	private DailyRecordItemRepository dailyItemRepo;
	@Autowired
	private ClassRoomDetailRepository classRoomDetailRepo;
	@Autowired
	private RecordsRepository recordsRepo;
	@Autowired
	private StudentRepository studentRepo;
	@Autowired
	private DailyRecordDisciplineRepository dailyRecordDisciplineRepo;

	@PostConstruct
    public void init() {
		headerSection();
		bodySection();
	}

	private void headerSection() {

		setCaption("<h3>Rekod Aktiviti Harian Pelajar</h3>");
		setCaptionAsHtml(true);
	}

	@SuppressWarnings("unchecked")
	private void bodySection() {
		VerticalLayout gridLayout = new VerticalLayout();

        ComboBox<ClassRoomDetail> cbClassRoomDetail = new ComboBox<>("Kelas");
        cbClassRoomDetail.setWidth(WIDTH, Unit.PIXELS);
        cbClassRoomDetail.setItems(classRoomDetailRepo.findAllWithOrder());
        cbClassRoomDetail.setItemCaptionGenerator(item -> item.getClassRoom().getDescription() + " " + item.getName() + " - " + item.getTeacher().getSalutation() + " " + item.getTeacher().getName());
        cbClassRoomDetail.setEmptySelectionAllowed(false);

        ComboBox<Student> cbStudent = new ComboBox<>("Pelajar");
        cbStudent.setWidth(WIDTH, Unit.PIXELS);
        cbStudent.setItemCaptionGenerator(item -> item.getName());
        cbStudent.setEmptySelectionAllowed(false);

        DateField dpDate = new DateField();
        dpDate.setValue(LocalDate.now());
        dpDate.setDateFormat("dd/MM/yyyy");
        dpDate.setRangeEnd(LocalDate.now());

        Button btnRefresh = new Button(VaadinIcons.REFRESH);
        Button btnSave = new Button("Simpan");
        Button btnCancel = new Button("Batal");

        cbClassRoomDetail.addSelectionListener(listener -> {
        	if(Helper.notNull(listener.getSelectedItem()) != null){
        		cbStudent.setItems(studentRepo.findByClassRoomDetail(listener.getSelectedItem().get()));
        	}
        });

		List<DailyRecordItem> dailyItemList = dailyItemRepo.findAllActive(Sort.by(Sort.Direction.ASC, "sequence"));
		if(dailyItemList == null) dailyItemList = new ArrayList<DailyRecordItem>();
		
		int rowNum = 0, columnNum = 0, index = 0;
		if(dailyItemList.size() >= 4) {
			BigDecimal num = new BigDecimal(Math.sqrt(dailyItemList.size()));
			num = num.setScale(0, RoundingMode.HALF_UP);
			columnNum = rowNum = num.intValue() ;
		}else {
			rowNum = 2;
			columnNum = 2;
		}
		
		GridLayout grid = new GridLayout(columnNum, rowNum);
		grid.setEnabled(true);
		grid.setSizeFull();
		grid.setHeightUndefined();
		
		List<DailyRecordDiscipline> dailyDisciplineList = dailyRecordDisciplineRepo.findAllActive(Sort.by(Sort.Direction.ASC, "description"));
		for(DailyRecordItem item : dailyItemList) {
    		VerticalLayout layout = new VerticalLayout();
    		layout.setCaption((index+1) + ". " + item.getDescription());
    		ComboBox<DailyRecordDiscipline> cbDiscipline = new ComboBox<>();
			cbDiscipline.setWidth(100, Unit.PERCENTAGE);
			cbDiscipline.setPopupWidth(null);
			cbDiscipline.setItems(dailyDisciplineList);
			cbDiscipline.setItemCaptionGenerator(item2 -> item2.getDescription());
			cbDiscipline.setEmptySelectionCaption("Selesai");
			cbDiscipline.setId(""+item.getPkid());
			layout.addComponent(cbDiscipline);
			grid.addComponent(layout);
		    index++;
		}
		
		btnRefresh.addClickListener(listener -> {
			DailyActivity dailyActivity =  dailyActivityRepo.findByClassRoomDetail(
				Helper.notNull(cbClassRoomDetail.getSelectedItem()), 
				Helper.notNull(cbStudent.getSelectedItem()), 
				Helper.notNull(dpDate.getValue())
			);		
			List<DailyActivityItem> dailyActivityItemList = dailyActivityItemRepo.findByDailyActivity(dailyActivity);
		
			Map<Integer, DailyRecordDiscipline> activityItemMap = new HashMap<>();
			if(dailyActivityItemList != null && !dailyActivityItemList.isEmpty()) {
				for(DailyActivityItem item : dailyActivityItemList) {
					activityItemMap.put(item.getDailyRecordItem().getPkid(), item.getDailyRecordDiscipline());
				}
			}
			
			for(int x = 0; x < grid.getRows() ; x++) {
				for(int y = 0; y < grid.getColumns() ; y++) {
					ComboBox<DailyRecordDiscipline> cbDiscipline = null;
					try {
						cbDiscipline = (ComboBox<DailyRecordDiscipline>)((VerticalLayout)grid.getComponent(x, y)).getComponent(0);
						cbDiscipline.setValue(activityItemMap.get(new Integer(cbDiscipline.getId())));
					}catch(Exception e) {
						logger.fine("set value : x : " + x + ", y : " + y + ", error : " + e.getMessage());
					}
				}
			}
		});

        HorizontalLayout dpLayout = new HorizontalLayout(dpDate, btnRefresh);
        dpLayout.setCaption("Tarikh");
		addComponent(new FormLayout(cbClassRoomDetail, cbStudent, dpLayout));
		
		btnSave.addClickListener(listener -> {
			UserContext userContext = Helper.getUserContext();
			DailyActivity dailyActivity =  dailyActivityRepo.findByClassRoomDetail(
				Helper.notNull(cbClassRoomDetail.getSelectedItem()), 
				Helper.notNull(cbStudent.getSelectedItem()), 
				Helper.notNull(dpDate.getValue())
			);
			
			if(dailyActivity == null) {
				Records records = recordsRepo.findRecordsByClassRoomDetailAndStudent(
						Helper.notNull(cbClassRoomDetail.getSelectedItem()), 
						Helper.notNull(cbStudent.getSelectedItem())
				);
				
				deleteCurrentDailyRecord(records, dpDate.getValue());
				
				dailyActivity = new DailyActivity();
				dailyActivity.setRecords(records);
				if(dailyActivity.getRecords() == null) {
					
					Notification.show("Sila pilih maklumat pelajar.", Type.ERROR_MESSAGE);
					return;
				}
				dailyActivity.setRecordUtility(new RecordUtility(userContext.getPkid()));
				dailyActivity.setDate(Helper.notNull(dpDate.getValue()));
				dailyActivity = dailyActivityRepo.save(dailyActivity);
			}
			
			for(int x = 0; x < grid.getRows() ; x++) {
				for(int y = 0; y < grid.getColumns() ; y++) {
					ComboBox<DailyRecordDiscipline> cbDiscipline = null;
					try {
						cbDiscipline = (ComboBox<DailyRecordDiscipline>)((VerticalLayout)grid.getComponent(x, y)).getComponent(0);
					}catch(Exception e) {
						logger.fine("x : " + x + ", y : " + y + ", error : " + e.getMessage());
					}
					if(cbDiscipline != null) {
						DailyActivityItem item = new DailyActivityItem();
						item.setDailyActivity(dailyActivity);
						item.setDailyRecordItem(Helper.notNull(dailyItemRepo.findById(new Integer(cbDiscipline.getId()))));
						item.setDailyRecordDiscipline(cbDiscipline.getValue());
						item = dailyActivityItemRepo.save(item);
					}
				}
			}
			
			dailyActivity = dailyActivityRepo.save(dailyActivity);
			
			if(dailyActivity != null) {
				Notification.show("Kemaskini rekod harian pelajar berjaya.", Type.HUMANIZED_MESSAGE);
			}
		});
		
		Panel mainPanel = new Panel();
		mainPanel.setCaption("Kemaskini Rekod Harian (Sila Kosongkan Jika Pelajar Telah Menyelesaikan)");
		gridLayout.addComponent(grid);
		mainPanel.setContent(gridLayout);
		addComponent(mainPanel);
		addComponent(new HorizontalLayout(btnSave, btnCancel));
	}
	
	void deleteCurrentDailyRecord(Records records, LocalDate date) {
		
		if(records == null)
			return;
		
		DailyActivity dailyActivity = dailyActivityRepo.findByRecords(records, date);
		if(dailyActivity != null) {
			List<DailyActivityItem> list = dailyActivityItemRepo.findByDailyActivity(dailyActivity);
			if(list != null && !list.isEmpty()) {
				dailyActivityItemRepo.deleteAll(list);
			}
			dailyActivityRepo.delete(dailyActivity);
		}
	}
}
