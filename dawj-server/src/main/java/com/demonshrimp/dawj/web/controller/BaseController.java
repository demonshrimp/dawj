package com.demonshrimp.dawj.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.demonshrimp.dawj.exception.ServiceException;

import pers.ksy.common.model.Result;

public class BaseController {
	Log log = LogFactory.getLog(BaseController.class);

	@ExceptionHandler
	public Result<String> exp(HttpServletRequest request, Throwable ex) {
		ex.printStackTrace();
		log.debug(ex.getMessage(), ex);
		if (ex instanceof ServiceException) {
			ServiceException se = (ServiceException) ex;
			return Result.errorResult(se.getErrorCode(), se.getMessage());
		} else if (ex instanceof DataIntegrityViolationException) {
			Throwable th1 = ex.getCause();
			if (th1 != null) {
				Throwable th2 = th1.getCause();
				if (th2.getMessage().startsWith("Duplicate entry")) {
					String name = this.getDuplicateFieldName(th2.getMessage());
					return Result.errorResult("字段" + name + "的值不允许重复！");
				}else if(th2.getMessage().startsWith("Cannot delete or update a parent row")){
					String name = this.getReferenceTable(th2.getMessage());
					return Result.errorResult("该记录正在被其他表：“" + name + "”中的对象引用，不能删除！");
				}
			}
		}
		return Result.errorResult(ex.getMessage(), "服务器请求异常!");
	}

	private String getDuplicateFieldName(String msg) {
		String name = null;
		String[] ss = msg.split(" ");
		String s = ss[ss.length - 1];
		if (s.startsWith("'UK_")) {
			name = s.substring(4, s.length() - 1);
		}
		return name;
	}
	
	private String getReferenceTable(String msg){
		String table = null;
		String key = "Cannot delete or update a parent row: a foreign key constraint fails";
		String ss[] = msg.substring(key.length()+2).split(",");
		table = ss[0];
		return table;
	}
}
