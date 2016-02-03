package com.demonshrimp.dawj.aop;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class SystemPointcuts {
	
	@Pointcut("execution(* com.demonshrimp.dawj.*.service.impl.*.*(..))")
	public void cutinService() {}
	
	@Pointcut("within(com.demonshrimp.dawj.model.dao..*)")
	public void cutinDao() {}
	

}
