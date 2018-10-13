package com.mz.imtaz.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.mz.imtaz.entity.ReportHeader;
import com.mz.imtaz.entity.ReportItem;

public interface ReportItemRepository extends JpaRepository<ReportItem, Integer> {

	List<ReportItem> findByReportHeaderAndActive(@Param("reportHeader") ReportHeader reportHeader, @Param("active") Boolean active, Sort sort);
}
