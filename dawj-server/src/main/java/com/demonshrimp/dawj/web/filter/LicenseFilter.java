package com.demonshrimp.dawj.web.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import com.demonshrimp.dawj.service.SystemService;
import com.google.gson.Gson;

import pers.ksy.common.model.Result;
import pers.ksy.common.spring4.SpringUtil;

/**
 * Servlet Filter implementation class LicenseFilter
 */
@WebFilter("/LicenseFilter")
public class LicenseFilter implements Filter {
	
	private SystemService systemService;
	private Gson gson = new Gson();
	
    /**
     * Default constructor. 
     */
    public LicenseFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		if(!req.getRequestURI().contains("api/admin/license/set-license")&&!getSystemService().checkLicense()){
			response.setContentType("application/json");
			response.getWriter().println(gson.toJson(Result.errorResult("expired!")));
			return;
		}
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	
	public SystemService getSystemService() {
		if (null == systemService) {
			systemService = SpringUtil.getBean(SystemService.class);
		}
		return systemService;
	}
	
}
