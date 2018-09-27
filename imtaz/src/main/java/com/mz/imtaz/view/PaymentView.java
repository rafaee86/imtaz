package com.mz.imtaz.view;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.vaadin.ui.NumberField;

import com.mz.imtaz.entity.ClassRoomDetail;
import com.mz.imtaz.entity.Payment;
import com.mz.imtaz.entity.PaymentDescription;
import com.mz.imtaz.entity.PaymentItem;
import com.mz.imtaz.entity.Records;
import com.mz.imtaz.entity.RunningNumber;
import com.mz.imtaz.entity.Student;
import com.mz.imtaz.enums.PaymentType;
import com.mz.imtaz.enums.RunningNumberCategory;
import com.mz.imtaz.repository.ClassRoomDetailRepository;
import com.mz.imtaz.repository.ClassRoomRepository;
import com.mz.imtaz.repository.PaymentDescriptionRepository;
import com.mz.imtaz.repository.PaymentItemRepository;
import com.mz.imtaz.repository.PaymentRepository;
import com.mz.imtaz.repository.RecordsRepository;
import com.mz.imtaz.repository.RunningNumberRepository;
import com.mz.imtaz.repository.StudentRepository;
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
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = PaymentView.NAME)
public class PaymentView extends VerticalLayout implements View {

	public static final String NAME = "PaymentView";
	private final static float WIDTH = 500f;
	private final static String ERROR_BIGDECIMAL = "Format amaun tidak betul, mesti xxx,xxx.00 dimana x ialah nombor.";

	private ListDataProvider<PaymentItem> dataProvider;

	@Autowired
	private ClassRoomRepository classRoomRepo;
	@Autowired
	private ClassRoomDetailRepository classRoomDetailRepo;
	@Autowired
	private StudentRepository studentRepo;
	@Autowired
	private RecordsRepository recordsRepo;
	@Autowired
	private PaymentRepository paymentRepo;
	@Autowired
	private PaymentItemRepository paymentItemRepo;
	@Autowired
	private PaymentDescriptionRepository paymentDescRepo;
	@Autowired
	private RunningNumberRepository runningNumberRepo;

	@PostConstruct
    public void init() {
		headerSection();
		bodySection();
	}

	private void headerSection() {

		setCaption("<h3>Bayaran</h3>");
		setCaptionAsHtml(true);
		Label label = new Label("Skrin untuk merekod bayaran pelajar. Sila masukkan bayaran baru pelajar. Setiap rekod tidak boleh dikemaskini setelah di kunci masuk.");

		addComponent(label);
	}

	private void bodySection() {

		ComboBox<ClassRoomDetail> cbClassRoomDetail = new ComboBox<>("Kelas");
        cbClassRoomDetail.setWidth(WIDTH, Unit.PIXELS);
        cbClassRoomDetail.setItems(classRoomDetailRepo.findByClassRoom(classRoomRepo.findByNameIgnoreCase("HAFAZAN")));
        cbClassRoomDetail.setItemCaptionGenerator(item -> item.getClassRoom().getName() + " " + item.getName() + " - " + item.getTeacher().getSalutation() + " " + item.getTeacher().getName());
        cbClassRoomDetail.setEmptySelectionAllowed(false);

        ComboBox<Student> cbStudent = new ComboBox<>("Pelajar");
        cbStudent.setWidth(WIDTH, Unit.PIXELS);
        cbStudent.setItemCaptionGenerator(item -> item.getName());
        cbStudent.setEmptySelectionAllowed(false);

        cbClassRoomDetail.addSelectionListener(listener -> {
        	if(listener.getSelectedItem() != null && listener.getSelectedItem().get() != null)
        		cbStudent.setItems(studentRepo.findByClassRoomDetail(listener.getSelectedItem().get()));
        });

        ComboBox<PaymentType> cbPaymentType = new ComboBox<>("Jenis Bayaran");
        cbPaymentType.setWidth(WIDTH, Unit.PIXELS);
        cbPaymentType.setItemCaptionGenerator(item -> item.getDescription());
        cbPaymentType.setEmptySelectionAllowed(false);
        cbPaymentType.setItems(Arrays.asList(PaymentType.values()));

        TextField tfReferenceId = new TextField("No Rujukan Bayaran");
        tfReferenceId.setWidth(WIDTH, Unit.PIXELS);

        Button btnNew = new Button(VaadinIcons.PLUS);
		Button btnDelete = new Button(VaadinIcons.TRASH);
		btnDelete.setEnabled(false);
        HorizontalLayout buttonBar = new HorizontalLayout(btnNew, btnDelete);

        Grid<PaymentItem> grid = new Grid<PaymentItem>();
		dataProvider = DataProvider.ofCollection(new ArrayList<PaymentItem>());
	    grid.setDataProvider(dataProvider);
		grid.setEnabled(true);
		grid.getEditor().setEnabled(true);
		grid.setSizeFull();
		grid.setHeightUndefined();

		ComboBox<PaymentDescription> cbDescription = new ComboBox<>();
		cbDescription.setEmptySelectionAllowed(false);
		cbDescription.setItemCaptionGenerator(itm -> itm != null ? itm.getDescription() + " - RM" + (itm.getAmount() != null ? itm.getAmount().toPlainString() : "0.00") : "");
		cbDescription.setRequiredIndicatorVisible(true);
		cbDescription.setWidth(WIDTH, Unit.PIXELS);
		List<PaymentDescription> paymentDescList = paymentDescRepo.findAll(Sort.by(Sort.Direction.ASC, "description"));
		cbDescription.setItems(paymentDescList);
		grid
			.addColumn(PaymentItem::getDescription, itm -> itm != null ? itm.getDescription() : "").setCaption("Perihal Bayaran")
			.setEditorComponent(cbDescription, PaymentItem::setDescription)
			.setSortable(true);

		NumberField tfAmount = new NumberField();
		tfAmount.setValue("0");
		tfAmount.setRequiredIndicatorVisible(true);
		tfAmount.setWidth(WIDTH, Unit.PIXELS);
		tfAmount.setMaxLength(10);
		tfAmount.setMinimumFractionDigits(2);
		tfAmount.setNegativeAllowed(false);
		tfAmount.setGroupingSeparator(',');
		grid
			.addColumn(PaymentItem::getAmount, item -> item != null ? item.toPlainString() : "0.00").setCaption("Amaun Harga(RM)")
			.setEditorBinding(grid.getEditor().getBinder()
        		.forField(tfAmount)
        		.withValidator((s,a) -> {
        			try {
        				new BigDecimal(s);
        				return ValidationResult.ok();
        			}catch (Exception e) {
						return ValidationResult.error(ERROR_BIGDECIMAL);
					}
        		})
        		.withNullRepresentation("0")
    	    	.withConverter(new StringToBigDecimalConverter(ERROR_BIGDECIMAL))
    	    	.bind(PaymentItem::getAmount, PaymentItem::setAmount))
			.setSortable(true);

		grid.getEditor().addSaveListener(evt -> {
        	try {
        		PaymentItem item = evt.getBean();
        		 dataProvider.getItems().remove(item);
         		if(
         			item != null && item.getDescription() != null && item.getDescription().getAmount() != null
         		) {
         			item.setAmount(item.getDescription().getAmount());
         		}
         		dataProvider.getItems().add(item);
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
	                PaymentItem item = grid.getSelectedItems().iterator().next();
	                dataProvider.getItems().remove(item);
	                dataProvider.refreshAll();
	            }
        	}catch (Exception e) {
        		 Notification.show("Rekod tidak berjaya dipadam.", Notification.Type.ERROR_MESSAGE);
			}
        });

		btnNew.addClickListener(evt -> {
        	PaymentItem paymentItem = new PaymentItem();
        	dataProvider.getItems().add(paymentItem);
            dataProvider.refreshAll();
        });

		Button btnSave = new Button("Simpan");
		Button btnPrint = new Button("Cetak");
		btnPrint.setVisible(false);

		btnSave.addClickListener(listener -> {

			String message = null;

			BigDecimal totalAmount = BigDecimal.ZERO;
			Payment payment = new Payment();
			Records records = recordsRepo.findRecordsByClassRoomDetailAndStudent(
				cbClassRoomDetail.getSelectedItem().get(),
				cbStudent.getSelectedItem().get()
			);

			if(records == null)message = "Maklumat pelajar tidak wujud.";
			if(cbPaymentType.getValue() == null)message = "Maklumat jenis bayaran tidak wujud.";
			if(dataProvider.getItems() == null || dataProvider.getItems().size() == 0)message = "Maklumat item bayaran tidak wujud.";

			if(message != null) {
				Notification.show(message, Notification.Type.ERROR_MESSAGE);
				throw new RuntimeException();
			}

			payment.setRecords(records);
			payment.setPaymentType(cbPaymentType.getValue());
			payment.setTransactionDate(new Date());
			payment.setReferenceId(tfReferenceId.getValue());
			payment.setTransactionId(generateTransactionId());
			payment.setTotalAmount(BigDecimal.ZERO);

			payment = paymentRepo.save(payment);

			for(PaymentItem item : dataProvider.getItems()) {
				totalAmount = totalAmount.add(item.getAmount());
				item.setPayment(payment);
				paymentItemRepo.save(item);
			}

			payment.setTotalAmount(totalAmount);
			payment = paymentRepo.save(payment);

			if(payment != null) {
				Notification.show("Rekod telah berjaya dikemaskini.", Notification.Type.HUMANIZED_MESSAGE);
				btnPrint.setVisible(true);
				cbClassRoomDetail.setEnabled(false);
				cbStudent.setEnabled(false);
				cbPaymentType.setEnabled(false);
				tfReferenceId.setEnabled(false);
				grid.setEnabled(false);
			}else {
				Notification.show("Rekod tidak berjaya dikemaskini.", Notification.Type.ERROR_MESSAGE);
			}
		});


        FormLayout formLayout = new FormLayout();
        formLayout.addComponent(cbClassRoomDetail);
        formLayout.addComponent(cbStudent);
        formLayout.addComponent(cbPaymentType);
        formLayout.addComponent(tfReferenceId);
        addComponent(formLayout);

        addComponent(buttonBar);
        addComponent(grid);
        addComponent(new HorizontalLayout(btnSave, btnPrint));
	}

	private String generateTransactionId() {

		String result = null;
		RunningNumber runningNumber = runningNumberRepo.findByCategory(RunningNumberCategory.PAYMENT);
		if(runningNumber == null) {
			runningNumber = new RunningNumber(RunningNumberCategory.PAYMENT, 1);
		}else {
			if(runningNumber.getRunning() == null) {
				runningNumber.setRunning(1);
			}
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		result =  "P"+sdf.format(new Date())+String.format("%05d", runningNumber.getRunning());
		runningNumber.incrementOne();
		runningNumberRepo.save(runningNumber);

		return result;
	}
}
