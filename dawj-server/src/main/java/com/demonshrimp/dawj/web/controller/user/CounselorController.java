package com.demonshrimp.dawj.web.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demonshrimp.dawj.model.entity.Counselor;
import com.demonshrimp.dawj.service.CounselingService;

import pers.ksy.common.StringUtil;
import pers.ksy.common.annotation.SerializationFilter;
import pers.ksy.common.model.Result;
import pers.ksy.common.orm.Order;
import pers.ksy.common.orm.QueryCondition;
import pers.ksy.common.orm.QueryConditionImpl;

@RestController(value = "counselorUserController")
@RequestMapping(value = "/usr/counselor")
public class CounselorController extends BaseUserController {
	@Autowired
	private CounselingService counselingService;

	@RequestMapping(path = "/list", method = RequestMethod.GET)
	@SerializationFilter(target = Counselor.class, fields = { "site", "about", "consultingCase" })
	public Object list() {
		QueryCondition qc = new QueryConditionImpl(Counselor.class);
		return Result.successResult(counselingService.counselorList(getCurrentSite().getId()), null);
	}
	
	@RequestMapping(path = "/page", method = RequestMethod.GET)
	@SerializationFilter(target = Counselor.class, fields = { "site", "about", "consultingCase" })
	public Object page(@RequestParam(name = "start") int pageIndex, @RequestParam(name = "length") int pageSize,
			String orderBy, @RequestParam(defaultValue = "false") Boolean desc) {
		QueryCondition qc = new QueryConditionImpl(Counselor.class);
		if (StringUtil.notEmpty(orderBy)) {
			qc.addOrder(Order.add(orderBy, desc));
		}
		qc.addOrder(Order.desc("createTime"));
		return Result.successResult(
				counselingService.findCounselorPage(getCurrentSite().getId(), qc, pageIndex, pageSize), null);
	}

	@RequestMapping(path = "/{counselorId}", method = RequestMethod.GET)
	@SerializationFilter(target = Counselor.class, fields = { "site" })
	public Object get(@PathVariable String counselorId) {
		return Result.successResult(counselingService.getCounselor(counselorId), null);
	}

}
