package com.demonshrimp.dawj.web.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demonshrimp.dawj.service.SystemService;

import pers.ksy.common.annotation.SerializationFilter;
import pers.ksy.common.model.Result;

@RestController(value = "licenseAdminController")
@RequestMapping(value = "/admin/license")
public class LicenseController extends BaseAdminController {

	@Autowired
	private SystemService systemService;

	@RequestMapping(path = "/set-license", method = RequestMethod.GET)
	@SerializationFilter
	public Object list(String license) {
		systemService.setLicense(license);
		return Result.successResult();
	}
}