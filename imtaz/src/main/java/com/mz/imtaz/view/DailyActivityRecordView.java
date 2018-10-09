package com.mz.imtaz.view;

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
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.VerticalLayout;

import lombok.Getter;

@SpringView(name = DailyActivityRecordView.NAME)
public class DailyActivityRecordView extends VerticalLayout implements View{

	@Getter
	enum RadioItem{

		DONE("OK"), NOT_DONE("X"), IS_DICIPLINE_OCCUR("Disiplin");

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
	final int limit = 1;

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
		Label label = new Label("Skrin untuk merekod aktiviti harian pelajar. Sila tekan butang Tambah untuk menambah rekod baru.");

		addComponent(label);
	}

	private void bodySection() {

		Button btnNew = new Button(VaadinIcons.PLUS);
		Button btnDelete = new Button(VaadinIcons.TRASH);
		btnDelete.setEnabled(false);
        HorizontalLayout buttonBar = new HorizontalLayout(btnNew, btnDelete);

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

		Map<Integer, String> mapItem = dailyItemList.stream().collect(Collectors.toMap(DailyRecordItem::getPkid, DailyRecordItem::getDescription));

		final Grid<Map<Integer, String>> grid = new Grid<Map<Integer, String>>();
		grid.setEnabled(true);
		grid.setSizeFull();
		grid.setHeightUndefined();

		for (Map.Entry<Integer, String> entry : mapItem.entrySet()) {
			RadioButtonGroup<RadioItem> radio = new RadioButtonGroup<>();
			radio.setItems(Arrays.asList(RadioItem.values()));
			radio.setItemCaptionGenerator(item2 -> item2.getDescription());
			radio.setId("radio" + entry.getKey());
		    grid
		    	.addColumn(h -> "gridColumn" + entry.getKey())
		    	.setId("gridId" + entry.getKey())
		    	.setCaption(entry.getValue());
		}

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
//	                DailyActivity item = grid.getSelectedItems().iterator().next();
//	                item.getRecordUtility().disabled();
//	                if(item.getPkid() != null)dailyActivityRepo.save(item);
//	                dataProvider.getItems().remove(item);
//	                dataProvider.refreshAll();
	            }
        	}catch (Exception e) {
        		 Notification.show("Rekod tidak berjaya dipadam.", Notification.Type.ERROR_MESSAGE);
			}
        });

        grid.addSelectionListener(listener -> {

        });

        cbStudent.addSelectionListener(listener -> {
        	ClassRoomDetail classRoomDetail = cbClassRoomDetail.getSelectedItem() != null && cbClassRoomDetail.getSelectedItem().get() != null ? cbClassRoomDetail.getSelectedItem().get() : null;
        	Student student = listener.getSelectedItem() != null && listener.getSelectedItem().get() != null ? listener.getSelectedItem().get() : null;

        	if(classRoomDetail != null && student != null) {
				List<DailyActivity> targetList = dailyActivityRepo.findByClassRoomDetail(classRoomDetail, student);
				List<DailyActivity> subTargetList = targetList != null && targetList.size() > limit ? targetList.subList(0, limit) : targetList;
				total = Long.valueOf(subTargetList != null ? targetList.size() : 0);
		        dataProvider = DataProvider.ofCollection(subTargetList != null ? subTargetList : new ArrayList<DailyActivity>());
//		        grid.setDataProvider(dataProvider);
			}
		});

		Pagination pagination = new Pagination(PaginationResource.newBuilder().setTotal(total).setPage(page).setLimit(limit).build());
	    pagination.setItemsPerPage(10, 20, 50, 100);
		pagination.addPageChangeListener(event -> {
			Pageable pageable = PageRequest.of(event.pageIndex(), event.limit());
			List<DailyActivity> pageTargetAllList = null;
			ClassRoomDetail classRoomDetail = Helper.notNull(cbClassRoomDetail.getSelectedItem()) != null ? cbClassRoomDetail.getSelectedItem().get() : null;
        	Student student = Helper.notNull(cbStudent.getSelectedItem()) != null ? cbStudent.getSelectedItem().get() : null;
			if(classRoomDetail != null && student != null)
				pageTargetAllList = dailyActivityRepo.findByClassRoomDetail(classRoomDetail, student);
			Long totalAll = Long.valueOf(pageTargetAllList != null ? pageTargetAllList.size() : 0);
			List<DailyActivity> pageRecordsSubList = null;
			if(classRoomDetail != null && student != null)
				pageRecordsSubList = dailyActivityRepo.findByClassRoomDetailPageable(classRoomDetail, student, pageable);
			pagination.setTotalCount(totalAll);
//			grid.setItems(pageRecordsSubList);
		});

        btnNew.addClickListener(evt -> {
        	ClassRoomDetail detail = Helper.notNull(cbClassRoomDetail.getSelectedItem()) != null ? cbClassRoomDetail.getSelectedItem().get() : null;
        	Student student = Helper.notNull(cbStudent.getSelectedItem()) != null ? cbStudent.getSelectedItem().get() : null;


        });

        HorizontalLayout dpLayout = new HorizontalLayout(dpDate, btnRefresh);
        dpLayout.setCaption("Tarikh");
		addComponent(new FormLayout(cbClassRoomDetail, cbStudent, dpLayout));
		addComponent(buttonBar);
		addComponent(grid);
	}
}
