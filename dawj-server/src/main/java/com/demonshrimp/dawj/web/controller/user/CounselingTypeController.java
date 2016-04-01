package com.demonshrimp.dawj.web.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demonshrimp.dawj.model.entity.CounselingType;
import com.demonshrimp.dawj.service.CounselingService;

import pers.ksy.common.annotation.SerializationFilter;
import pers.ksy.common.model.Result;

@RestController(value = "counselingTypeUserController")
@RequestMapping(value = "/usr/counseling-type")
public class CounselingTypeController extends BaseUserController {

	@Autowired
	private CounselingService counselingService;

	@RequestMapping(path = "/list", method = RequestMethod.GET)
	@SerializationFilter(target = CounselingType.class, fields = { "site", "content" })
	public Object list() {
		return Result.successResult(counselingService.counselingTypeList(getCurrentSite().getId()), null);
	}
	
	@RequestMapping(path = "/{counselingTypeId}", method = RequestMethod.GET)
	@SerializationFilter(target = CounselingType.class, fields = { "site" })
	public Object get(@PathVariable String counselingTypeId) {
		return Result.successResult(counselingService.getCounselingType(counselingTypeId), null);
	}

}
