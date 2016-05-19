package com.demonshrimp.dawj.web.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demonshrimp.dawj.model.entity.Counselor;
import com.demonshrimp.dawj.service.CounselingService;

import pers.ksy.common.annotation.SerializationFilter;
import pers.ksy.common.model.Result;

@RestController(value = "counselorAdminController")
@RequestMapping(value = "/admin/counselor")
public class CounselorController extends BaseAdminController {
	@Autowired
	private CounselingService counselingService;

	@RequestMapping(path = "/list", method = RequestMethod.GET)
	@SerializationFilter(target = Counselor.class, fields = { "site", "about" })
	public Object list() {
		return Result.successResult(counselingService.counselorList(getCurrentSite().getId()), null);
	}

	@RequestMapping(path = "/all-list", method = RequestMethod.GET)
	@SerializationFilter(target = Counselor.class, fields = { "site", "about" })
	public Object allList() {
		return Result.successResult(counselingService.counselorList(null), null);
	}

	@RequestMapping(path = "/{counselorId}", method = RequestMethod.GET)
	@SerializationFilter(target = Counselor.class, fields = { "site" })
	public Object get(@PathVariable String counselorId) {
		return Result.successResult(counselingService.getCounselor(counselorId), null);
	}

	@RequestMapping(path = "/", method = RequestMethod.POST)
	public Object save(@RequestBody Counselor counselor) {
		counselor.setSite(getCurrentSite());
		counselor = counselingService.addCounselor(counselor);
		return Result.successResult(counselor.getId(), "新增成功");
	}

	@RequestMapping(path = "/{counselorId}", method = RequestMethod.PUT)
	public Object update(@PathVariable String counselorId, @RequestBody Counselor counselor) {
		counselingService.updateCounselor(counselorId, counselor);
		return Result.successResult("更新成功");
	}

	@RequestMapping(path = "/{counselorId}", method = RequestMethod.DELETE)
	public Object delete(@PathVariable String counselorId) {
		counselingService.deleteCounselor(counselorId);
		return Result.successResult("删除成功");
	}

}
