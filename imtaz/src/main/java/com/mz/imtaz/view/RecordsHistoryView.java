package com.mz.imtaz.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.mz.imtaz.entity.ClassRoomDetail;
import com.mz.imtaz.entity.RecordsHistory;
import com.mz.imtaz.entity.Student;
import com.mz.imtaz.repository.ClassRoomDetailRepository;
import com.mz.imtaz.repository.RecordsHistoryRepository;
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
import com.vaadin.ui.VerticalLayout;

@SpringView(name = RecordsHistoryView.NAME)
public class RecordsHistoryView extends VerticalLayout implements View {

	public static final String NAME = "StudentHistoryView";
	private final static float WIDTH = 500f;
	
	final int page = 0;
	final int limit = 10;
	private ListDataProvider<RecordsHistory> dataProvider;
	private Long total = 0L;
	
	@Autowired
	private RecordsHistoryRepository recordsHistoryRepository;
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

		setCaption("<h3>Semakan Sejarah Pelajar</h3>");
		setCaptionAsHtml(true);
		setDescription("Skrin untuk melihat sejarah pelajar. ");
	}
	
	private void bodySection() {
		
		ComboBox<ClassRoomDetail> cbClassRoomDetail = new ComboBox<>("Kelas");
		cbClassRoomDetail.setEmptySelectionAllowed(false);
		cbClassRoomDetail.setRequiredIndicatorVisible(true);
        cbClassRoomDetail.setWidth(WIDTH, Unit.PIXELS);
        cbClassRoomDetail.setItems(classRoomDetailRepo.findAllWithOrder());
        cbClassRoomDetail.setItemCaptionGenerator(item -> item.getClassRoom().getName() + " " + item.getName() + " - " + item.getTeacher().getSalutation() + " " + item.getTeacher().getName());
        cbClassRoomDetail.setEmptySelectionAllowed(false);

        ComboBox<Student> cbStudent = new ComboBox<>("Pelajar");
        cbStudent.setEmptySelectionAllowed(false);
        cbStudent.setRequiredIndicatorVisible(true);
        cbStudent.setWidth(WIDTH, Unit.PIXELS);
        cbStudent.setItemCaptionGenerator(item -> item.getName());
        cbStudent.setEmptySelectionAllowed(false);

        cbClassRoomDetail.addSelectionListener(listener -> {
        	if(Helper.notNull(listener.getSelectedItem()) != null)
        		cbStudent.setItems(studentRepo.findByClassRoomDetail(listener.getSelectedItem().get()));
        });
        
        DateField dfFromDate = new DateField("Tarikh Dari");
        dfFromDate.setRangeEnd(LocalDate.now());
        
        DateField dfToDate = new DateField();
        dfToDate.setRangeEnd(LocalDate.now());
        
        Button btnSearch = new Button(VaadinIcons.SEARCH);
        Button btnClear = new Button("Kosongkan");
        
        HorizontalLayout hlDfToDate = new HorizontalLayout(dfToDate, btnSearch, btnClear);
        hlDfToDate.setCaption("Tarikh Hingga");
        
        Grid<RecordsHistory> grid = new Grid<>();
		dataProvider = DataProvider.ofCollection(new ArrayList<RecordsHistory>());
	    grid.setDataProvider(dataProvider);
		grid.getEditor().setEnabled(false);
		grid.setSizeFull();
		grid.setHeightUndefined();

		grid.addColumn(RecordsHistory::getTransactionDate, item -> Helper.dateFormat(item, "dd/MM/yyyy hh:mm:ss a"))
		.setSortable(true)
		.setWidth(250)
		.setCaption("Tarikh");

		grid.addColumn(RecordsHistory::getClassRoomDescription)
		.setWidth(300)
		.setCaption("Kelas");
		
		grid.addColumn(RecordsHistory::getDescription)
		.setCaption("Keterangan");
		
		btnSearch.addClickListener(listener -> {
        	Student student = Helper.notNull(cbStudent.getSelectedItem());
        	List<RecordsHistory> historyList = null;
        	if(student != null) {
        		Date dateFrom = Helper.notNull(dfFromDate.getValue()) != null ? Helper.localDateToDate(Helper.notNull(dfFromDate.getValue())) : null;
        		Date dateTo = Helper.notNull(dfToDate.getValue()) != null ? Helper.localDateToDate(Helper.notNull(dfToDate.getValue())) : null;
        		if(dateFrom == null && dateTo == null) {
        			historyList = recordsHistoryRepository.findByStudent(student.getPkid());
        		}else {
        			if(dateTo == null)dateTo = dateFrom;
        			if(dateFrom == null)dateFrom = dateTo;
        			historyList = recordsHistoryRepository.findByStudent(student.getPkid(), dateFrom, dateTo);
        		}
				
        		if(historyList != null && historyList.size() > 0) {
					List<RecordsHistory> subTargetList = historyList != null && historyList.size() > limit ? historyList.subList(0, limit) : historyList;
					total = Long.valueOf(historyList != null ? historyList.size() : 0);
			        dataProvider = DataProvider.ofCollection(subTargetList != null ? subTargetList : new ArrayList<>());
			        grid.setDataProvider(dataProvider);
        		}
			}
		});
		
		btnClear.addClickListener(listener -> {
			cbClassRoomDetail.setValue(null);
			cbStudent.setValue(null);
			grid.setItems(new ArrayList<RecordsHistory>());
		});
		
		Pagination pagination = new Pagination(PaginationResource.newBuilder().setTotal(total).setPage(page).setLimit(limit).build());
	    pagination.setItemsPerPage(10, 20, 50, 100);
		pagination.addPageChangeListener(event -> {
			Pageable pageable = PageRequest.of(event.pageIndex(), event.limit());
			List<RecordsHistory> pageTargetAllList = null;
			List<RecordsHistory> pageTargetList = null;
			Student student = Helper.notNull(cbStudent.getSelectedItem()) != null ? cbStudent.getSelectedItem().get() : null;
			if(student != null) {
        		Date dateFrom = Helper.notNull(dfFromDate.getValue()) != null ? Helper.localDateToDate(Helper.notNull(dfFromDate.getValue())) : null;
        		Date dateTo = Helper.notNull(dfToDate.getValue()) != null ? Helper.localDateToDate(Helper.notNull(dfToDate.getValue())) : null;
        		if(dateFrom == null && dateTo == null) {
        			pageTargetList = recordsHistoryRepository.findByStudentPageable(student.getPkid(), pageable);
        			pageTargetAllList = recordsHistoryRepository.findByStudent(student.getPkid());
        		}else {
        			if(dateTo == null)dateTo = dateFrom;
        			if(dateFrom == null)dateFrom = dateTo;
        			pageTargetList = recordsHistoryRepository.findByStudent(student.getPkid(), dateFrom, dateTo);
        			pageTargetAllList = recordsHistoryRepository.findByStudentPageable(student.getPkid(), dateFrom, dateTo, pageable);
        		}
			}
			if(pageTargetList != null && pageTargetAllList != null) {
				 dfFromDate.setValue(LocalDate.now());
				 dfToDate.setValue(LocalDate.now());
				Long totalAll = Long.valueOf(pageTargetAllList != null ? pageTargetAllList.size() : 0);
				pagination.setTotalCount(totalAll);
				grid.setItems(pageTargetList);
			}
		});
        
        addComponent(new FormLayout(cbClassRoomDetail, cbStudent, dfFromDate, hlDfToDate));
        addComponent(grid);
        addComponent(pagination);
	}
}
