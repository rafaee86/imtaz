//package com.mz.imtaz;
//
//import java.util.List;
//import java.util.logging.Logger;
//
//import javax.annotation.PostConstruct;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.domain.Sort;
//
//import com.mz.imtaz.entity.DailyRecordItem;
//import com.mz.imtaz.repository.DailyRecordItemRepository;
//
//import javassist.ClassPool;
//import javassist.CtClass;
//import javassist.CtField;
//import javassist.CtNewMethod;
//
//@Configuration
//public class GenerateDailyRecordDTO {
//
//	final Logger logger = Logger.getLogger(GenerateDailyRecordDTO.class.getName());
//	final String CLASSNAME = "com.mz.imtaz.dto.DailyRecordDTO";
//	
//	@Autowired
//	private DailyRecordItemRepository dailyRecordItemRepo;
//	
//	@PostConstruct
//	public void getDailyRecordDTO() {
//		
//		try {
//			
//			List<DailyRecordItem> dailyItemList = dailyRecordItemRepo.findAllActive(Sort.by(Sort.Direction.ASC, "sequence"));
//			ClassPool classPool = ClassPool.getDefault();
//			CtClass ctClass = classPool.get(CLASSNAME);
//			for(DailyRecordItem dailyRecordItem : dailyItemList) {
//				ctClass.addField(CtField.make("private String field" + dailyRecordItem.getPkid() + ";", ctClass));
//				ctClass.addMethod(CtNewMethod.make(getSet(dailyRecordItem.getPkid().toString()), ctClass));
//				ctClass.addMethod(CtNewMethod.make(getGet(dailyRecordItem.getPkid().toString()), ctClass));
//			}
//			
//			ctClass.writeFile();
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.info(e.getMessage());
//		}
//        
//	}
//	
//	String getSet(String id) {
//		return "public void setField" + id + "(String field" + id + "){this.field" + id + " = field" + id + ";}";
//	}
//	
//	String getGet(String id) {
//		return "public String getField" + id + "(){return this.field" + id + ";}";
//	}
//}
