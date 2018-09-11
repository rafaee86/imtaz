package com.mz.imtaz.util;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

@Transactional
@Service
public interface ClassVisitor<T> {
	void visit(T entity);
}
