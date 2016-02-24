package com.demonshrimp.dawj.web.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import pers.ksy.common.model.Result;

@Component( "restAuthenticationEntryPoint" )
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint{

   @Override
   public void commence( HttpServletRequest request, HttpServletResponse response, 
    AuthenticationException authException ) throws IOException{
	   response.getWriter().write(new Gson().toJson(Result.errorResult("no privilege", "请登录")));
      //response.sendError( HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized" );
   }
}