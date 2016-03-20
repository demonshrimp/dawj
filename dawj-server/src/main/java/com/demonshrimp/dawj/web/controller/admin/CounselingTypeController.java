package com.demonshrimp.dawj.web.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demonshrimp.dawj.model.entity.CounselingType;
import com.demonshrimp.dawj.model.entity.Site;
import com.demonshrimp.dawj.service.CounselingService;

import pers.ksy.common.annotation.SerializationFilter;
import pers.ksy.common.model.Result;

@RestController
@RequestMapping(value = "/admin/counseling-type")
public class CounselingTypeController extends BaseAdminController {
	
	@Autowired
	private CounselingService counselingService;

	@RequestMapping(path = "/list", method = RequestMethod.GET)
	@SerializationFilter(target = CounselingType.class, fields = { "site" })
	public Object list() {
		return Result.successResult(counselingService.counselingTypeList(getCurrentSite().getId()),null);
	}
	
	@RequestMapping(path = "/{counselingTypeId}", method = RequestMethod.GET)
	@SerializationFilter(target = CounselingType.class, fields = { "site" })
	public Object get(@PathVariable String counselingTypeId) {
		return Result.successResult(counselingService.getCounselingType(counselingTypeId), null);
	}

	@RequestMapping(path = "/", method = RequestMethod.POST)
	public Object save(@RequestBody CounselingType counselingType) {
		counselingType.setSite(getCurrentSite());
		counselingType = counselingService.addCounselingType(counselingType);
		return Result.successResult(counselingType.getId(), "新增成功");
	}

	@RequestMapping(path = "/{counselingTypeId}", method = RequestMethod.PUT)
	public Object update(@PathVariable String counselingTypeId, @RequestBody CounselingType counselingType) {
		counselingService.updateCounselingType(counselingTypeId, counselingType);
		return Result.successResult("更新成功");
	}

	@RequestMapping(path = "/{counselingTypeId}", method = RequestMethod.DELETE)
	public Object delete(@PathVariable String counselingTypeId) {
		counselingService.deleteCounselingType(counselingTypeId);
		return Result.successResult("删除成功");
	}

}
