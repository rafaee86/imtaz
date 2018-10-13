package com.mz.imtaz.view;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.mz.imtaz.entity.ClassRoomDetail;
import com.mz.imtaz.entity.DailyActivity;
import com.mz.imtaz.entity.DailyRecordItem;
import com.mz.imtaz.entity.Student;
import com.mz.imtaz.repository.ClassRoomDetailRepository;
import com.mz.imtaz.repository.DailyActivityItemRepository;
import com.mz.imtaz.repository.DailyActivityRepository;
import com.mz.imtaz.repository.DailyRecordItemRepository;
import com.mz.imtaz.repository.StudentRepository;
import com.mz.imtaz.util.Helper;
import com.vaadin.addon.pagination.Pagination;
import com.vaadin.addon.pagination.PaginationResource;
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
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.VerticalLayout;

import lombok.Getter;

@SpringView(name = DailyActivityRecordView.NAME)
public class DailyActivityRecordView extends VerticalLayout implements View{

	@Getter
	enum RadioItem{

		DONE("Selesai"), NOT_DONE("Belum Selesai"),NOT_APPLICABLE("N/A"), IS_DICIPLINE_OCCUR("Disiplin");

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

	private ListDataProvider<DailyActivity> dataProvider;
	private Long total = 0L;

	@Autowired
	private DailyActivityRepository dailyActivityRepo;
	@Autowired
	private DailyActivityItemRepository dailyActivityItemRepo;
	@Autowired
	private DailyRecordItemRepository dailyItemRepo;
	@Autowired
	private ClassRoomDetailRepository classRoomDetailRepo;
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
		setDescription("Skrin untuk merekod aktiviti harian pelajar. Sila tekan butang Tambah untuk menambah rekod baru.");
	}

	private void bodySection() {

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
			radio.setId("radio" + item.getPkid());
			radio.setValue(RadioItem.NOT_DONE);
			layout.addComponent(radio);
			grid.addComponent(layout);
		    index++;            
		}

        HorizontalLayout dpLayout = new HorizontalLayout(dpDate, btnRefresh);
        dpLayout.setCaption("Tarikh");
		addComponent(new FormLayout(cbClassRoomDetail, cbStudent, dpLayout));
		
		Panel mainPanel = new Panel();
		mainPanel.setCaption("Kemaskini Rekod Harian");
		mainPanel.setContent(grid);
		addComponent(mainPanel);
	}
}
