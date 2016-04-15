package com.demonshrimp.dawj.web.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demonshrimp.dawj.model.entity.CounselingArticle;
import com.demonshrimp.dawj.service.CounselingService;

import pers.ksy.common.StringUtil;
import pers.ksy.common.annotation.SerializationFilter;
import pers.ksy.common.model.Result;
import pers.ksy.common.orm.Order;
import pers.ksy.common.orm.QueryCondition;
import pers.ksy.common.orm.QueryConditionImpl;

@RestController(value = "counselingArticleUserController")
@RequestMapping(value = "/usr/counseling-article")
public class CounselingArticleController extends BaseUserController {
	@Autowired
	private CounselingService counselingService;

	@RequestMapping(path = "/page", method = RequestMethod.GET)
	@SerializationFilter(target = CounselingArticle.class, fields = { "site", "content" })
	public Object page(@RequestParam(name = "pageIndex") int pageIndex, @RequestParam(name = "length") int pageSize,
			String orderBy, @RequestParam(defaultValue = "false") Boolean desc) {
		QueryCondition qc = new QueryConditionImpl(CounselingArticle.class);
		if (StringUtil.notEmpty(orderBy)) {
			qc.addOrder(Order.add(orderBy, !desc));
		}
		qc.addOrder(Order.desc("createTime"));
		return Result.successResult(
				counselingService.findCounselingArticlePage(getCurrentSite().getId(), qc, pageIndex, pageSize), null);
	}

	@RequestMapping(path = "/{counselingArticleId}", method = RequestMethod.GET)
	@SerializationFilter(target = CounselingArticle.class, fields = { "site" })
	public Object get(@PathVariable String counselingArticleId) {
		return Result.successResult(counselingService.getCounselingArticle(counselingArticleId), null);
	}

}
