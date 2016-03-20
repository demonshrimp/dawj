package com.demonshrimp.dawj.web.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demonshrimp.dawj.model.entity.CounselingArticle;
import com.demonshrimp.dawj.model.entity.CounselingType;
import com.demonshrimp.dawj.model.entity.Site;
import com.demonshrimp.dawj.service.CounselingService;

import pers.ksy.common.annotation.SerializationFilter;
import pers.ksy.common.model.Result;
import pers.ksy.common.orm.QueryCondition;
import pers.ksy.common.orm.QueryConditionImpl;

@RestController
@RequestMapping(value = "/admin/counseling-article")
public class CounselingArticleController extends BaseAdminController {
	@Autowired
	private CounselingService counselingService;

	@RequestMapping(path = "/page", method = RequestMethod.GET)
	@SerializationFilter(target = CounselingArticle.class, fields = { "site" })
	public Object page(@RequestParam(name="start")int pageIndex,@RequestParam(name="length")int pageSize) {
		QueryCondition qc = new QueryConditionImpl(CounselingArticle.class);
		return Result.successResult(counselingService.findCounselingArticlePage(getCurrentSite().getId(),qc, pageIndex, pageSize), null);
	}
	
	@RequestMapping(path = "/{counselingArticleId}", method = RequestMethod.GET)
	@SerializationFilter(target = CounselingArticle.class, fields = { "site" })
	public Object get(@PathVariable String counselingArticleId) {
		return Result.successResult(counselingService.getCounselingArticle(counselingArticleId), null);
	}

	@RequestMapping(path = "/", method = RequestMethod.POST)
	public Object save(@RequestBody CounselingArticle counselingArticle) {
		counselingArticle.setSite(getCurrentSite());
		counselingArticle = counselingService.addCounselingArticle(counselingArticle);
		return Result.successResult(counselingArticle.getId(), "新增成功");
	}

	@RequestMapping(path = "/{counselingArticleId}", method = RequestMethod.PUT)
	public Object update(@PathVariable String counselingArticleId, @RequestBody CounselingArticle counselingArticle) {
		counselingService.updateCounselingArticle(counselingArticleId, counselingArticle);
		return Result.successResult("更新成功");
	}

	@RequestMapping(path = "/{counselingArticleId}", method = RequestMethod.DELETE)
	public Object delete(@PathVariable String counselingArticleId) {
		counselingService.deleteCounselingArticle(counselingArticleId);
		return Result.successResult("删除成功");
	}

}
