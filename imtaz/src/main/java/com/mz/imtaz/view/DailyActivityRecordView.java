package com.mz.imtaz.view;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import com.mz.imtaz.entity.ClassRoomDetail;
import com.mz.imtaz.entity.DailyRecordItem;
import com.mz.imtaz.entity.Student;
import com.mz.imtaz.repository.ClassRoomDetailRepository;
import com.mz.imtaz.repository.DailyActivityItemRepository;
import com.mz.imtaz.repository.DailyActivityRepository;
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
import com.vaadin.ui.Panel;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.VerticalLayout;

import lombok.Getter;

@SpringView(name = DailyActivityRecordView.NAME)
public class DailyActivityRecordView extends VerticalLayout implements View{

	@Getter
	enum RadioItem{

		NOT_DONE("Belum Selesai"), DONE("Selesai"), NOT_APPLICABLE("N/A");

		private String description;

		RadioItem(String description) {
			this.description = description;
		}

		public List<RadioItem> lists(){
			return Arrays.asList(values());
		}
	}

	public static final String NAME = "DailyActivityRecordView";

	private final static float WIDTH = 500f;
	final int page = 0;
	final int limit = 10;

	@Autowired
	private DailyActivityRepository dailyActivityRepo;
	@Autowired
	private DailyRecordItemRepository dailyRecordItemRepo;
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
        cbClassRoomDetail.setItemCaptionGenerator(item -> item.getClassRoom().getName() + " " + item.getName() + " - " + item.getTeacher().getSalutation() + " " + item.getTeacher().getName());
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
			columnNum = rowNum = num.intValue() + 1;
		}else {
			rowNum = 2;
			columnNum = 2;
		}
		
		GridLayout grid = new GridLayout(columnNum, rowNum);
		grid.setEnabled(true);
		grid.setSizeFull();
		grid.setHeightUndefined();

		for(DailyRecordItem item : dailyItemList) {
    		VerticalLayout layout = new VerticalLayout();
    		layout.setCaption((index+1) + ". " + item.getDescription());
			RadioButtonGroup<RadioItem> radio = new RadioButtonGroup<>();
			radio.setItems(Arrays.asList(RadioItem.values()));
			radio.setItemCaptionGenerator(item2 -> item2.getDescription());
			radio.setId(""+item.getPkid());
			radio.setValue(RadioItem.NOT_DONE);
			layout.addComponent(radio);
			grid.addComponent(layout);
		    index++;            
		}
		
//		btnRefresh.addClickListener(listener -> {
//			DailyActivity dailyActivity =  dailyActivityRepo.findByClassRoomDetail(
//				Helper.notNull(cbClassRoomDetail.getSelectedItem()), 
//				Helper.notNull(cbStudent.getSelectedItem()), 
//				Helper.notNull(dpDate.getValue())
//			);
//						
//			List<DailyActivityItem> dailyActivityItemList = dailyActivityItemRepo.findByDailyActivity(dailyActivity);
//			Map<Integer, Boolean> activityMap = Optional.ofNullable(dailyActivityItemList).orElse(new ArrayList<DailyActivityItem>()).stream().collect(Collectors.toMap(DailyActivityItem::getPkid, DailyActivityItem::getDone));
//
//			for(int x = 0; x < grid.getRows() ; x++) {
//				for(int y = 0; y < grid.getColumns() ; y++) {
//					RadioButtonGroup<RadioItem> radio = (RadioButtonGroup<RadioItem>)((VerticalLayout)grid.getComponent(x, y)).getComponent(0);
//					radio.setValue(activityMap.get(new Integer(radio.getId())) ? RadioItem.DONE : RadioItem.NOT_DONE);
//				}
//			}
//		});

        HorizontalLayout dpLayout = new HorizontalLayout(dpDate, btnRefresh);
        dpLayout.setCaption("Tarikh");
		addComponent(new FormLayout(cbClassRoomDetail, cbStudent, dpLayout));
		
//		btnSave.addClickListener(listener -> {
//			DailyActivity dailyActivity =  dailyActivityRepo.findByClassRoomDetail(
//				Helper.notNull(cbClassRoomDetail.getSelectedItem()), 
//				Helper.notNull(cbStudent.getSelectedItem()), 
//				Helper.notNull(dpDate.getValue())
//			);
//			
//			if(dailyActivity == null) {
//				dailyActivity = new DailyActivity();
//				dailyActivity.setRecords(recordsRepo.findRecordsByClassRoomDetailAndStudent(
//					Helper.notNull(cbClassRoomDetail.getSelectedItem()), 
//					Helper.notNull(cbStudent.getSelectedItem()))
//				);
//				dailyActivity.setRecordUtility(new RecordUtility());
//				dailyActivity.setDate(Helper.notNull(dpDate.getValue()));
//				dailyActivity = dailyActivityRepo.save(dailyActivity);
//			}
//			
//			Integer diciplineIssues = 0; 
//			
//			for(int x = 0; x < grid.getRows() ; x++) {
//				for(int y = 0; y < grid.getColumns() ; y++) {
//					RadioButtonGroup<RadioItem> radio = Helper.notNull((RadioButtonGroup<RadioItem>)((VerticalLayout)grid.getComponent(x, y)).getComponent(0));
//					if(radio != null) {
//						DailyActivityItem item = new DailyActivityItem();
//						item.setDailyActivity(dailyActivity);
//						item.setDailyRecordItem(Helper.notNull(dailyItemRepo.findById(new Integer(radio.getId()))));
//						if(!radio.getValue().equals(RadioItem.NOT_APPLICABLE))item.setDone(radio.getValue().equals(RadioItem.DONE));
//						
//						item = dailyActivityItemRepo.save(item);
//						
//						if(item.getDone() != null && !item.getDone()) {
//							diciplineIssues++;
//						}
//					}
//				}
//			}
//			
//			dailyActivity.setDiciplineIssues(diciplineIssues);
//			dailyActivityRepo.save(dailyActivity);
//		});
		
		Panel mainPanel = new Panel();
		mainPanel.setCaption("Kemaskini Rekod Harian");
		gridLayout.addComponent(grid);
		mainPanel.setContent(gridLayout);
		addComponent(mainPanel);
		addComponent(new HorizontalLayout(btnSave, btnCancel));
	}
}
