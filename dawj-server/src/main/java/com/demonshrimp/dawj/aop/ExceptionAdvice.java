package com.demonshrimp.dawj.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.demonshrimp.dawj.exception.DAOException;
import com.demonshrimp.dawj.exception.ServiceException;


@Aspect
@Component
public class ExceptionAdvice{
	@Around("com.jpz.dcim.modeling.aop.SystemPointcuts.cutinService()")
    public Object doServiceCall(ProceedingJoinPoint pjp) throws Throwable{		
        try{
        	Object retVal = pjp.proceed();
        	return retVal;
        }catch(DAOException e){
        	throw new ServiceException("数据库访问出错!",e);
        }catch(ServiceException e){
        	throw e;
        }catch(Throwable e){
        	String msg = e.getMessage();
        	if(msg == null){
        		msg = "业务处理出错："+e.getClass().getName();
        	}
        	throw new ServiceException(msg,e);
        }
    }
	
	@Around("com.jpz.dcim.modeling.aop.SystemPointcuts.cutinDao()")
    public Object doDaoCall(ProceedingJoinPoint pjp) throws Throwable{
        try{
        	Object retVal = pjp.proceed();
        	return retVal;
        }catch(DAOException e){
        	throw e;
        }catch(Throwable e){
        	throw new DAOException("数据库访问出错:"+e.getMessage(),e);
        }
    }
}
