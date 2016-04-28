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
import pers.ksy.common.orm.Conditions;
import pers.ksy.common.orm.JoinType;
import pers.ksy.common.orm.MatchMode;
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
		return Result.successResult(counselingService.counselorList(getCurrentSite().getId()), null);
	}

	@RequestMapping(path = "/page", method = RequestMethod.GET)
	@SerializationFilter(target = Counselor.class, fields = { "site", "about", "consultingCase" })
	public Object page(@RequestParam(name = "pageIndex") int pageIndex, @RequestParam(name = "length") int pageSize,
			String orderBy, @RequestParam(defaultValue = "false") Boolean desc, String keywords,
			String counselingTypeId) {
		QueryCondition qc = new QueryConditionImpl(Counselor.class);
		if (StringUtil.notEmpty(orderBy)) {
			qc.addOrder(Order.add(orderBy, desc));
		}
		if (StringUtil.notEmpty(keywords)) {
			qc.or(Conditions.like("name", keywords, MatchMode.ANYWHERE),
					Conditions.like("title", keywords, MatchMode.ANYWHERE),
					Conditions.like("about", keywords, MatchMode.ANYWHERE),
					Conditions.like("consultingCase", keywords, MatchMode.ANYWHERE));
		}
		if (StringUtil.notEmpty(counselingTypeId)) {
			qc.createAlias("counselingTypes", "counselingTypes", JoinType.INNER_JOIN);
			qc.eq("counselingTypes.id", counselingTypeId);
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
