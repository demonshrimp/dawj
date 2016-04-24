package com.demonshrimp.dawj.web.controller.admin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpUtils;

import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.demonshrimp.dawj.model.entity.Site;
import com.demonshrimp.dawj.service.SystemService;

import pers.ksy.common.annotation.SerializationFilter;
import pers.ksy.common.model.Result;
import pers.ksy.common.orm.Conditions;
import pers.ksy.common.orm.MatchMode;
import pers.ksy.common.orm.QueryCondition;
import pers.ksy.common.orm.QueryConditionImpl;

@RestController(value = "siteAdminController")
@RequestMapping(value = "/admin/site")
public class SiteController extends BaseAdminController {
	@Autowired
	private SystemService systemService;

	@RequestMapping(path = "/check-site", method = RequestMethod.GET)
	@SerializationFilter(target = Site.class, fields = { "id", "certificated", "status", "password", "lastLoginTime",
			"token", "createTime", "lastModifyTime" })
	public Object checkSite(String name) {
		Site site = systemService.getSiteByName(name);
		return Result.successResult(site, null);
	}

	@RequestMapping(path = "/login", method = RequestMethod.POST)
	public Object login(String name, String password, HttpServletResponse response) {
		Site site = systemService.siteAdminLogin(name, password);
		return Result.successResult(site, "");
	}

	@RequestMapping(path = "/page", method = RequestMethod.GET)
	@SerializationFilter(target = Site.class, fields = { "password" })
	public Object page(String city, String name, int pageIndex, int pageSize, HttpServletRequest request) {
		QueryCondition queryCondition = new QueryConditionImpl(Site.class, null);
		if (null != city) {
			queryCondition.add(Conditions.like("city", city, MatchMode.ANYWHERE));
		}
		if (null != name) {
			queryCondition.add(Conditions.like("name", name, MatchMode.ANYWHERE));
		}
		System.out.println(request.getParameterNames());
		return systemService.findByPage(queryCondition, pageIndex, pageSize);
	}

	@RequestMapping(path = "/list", method = RequestMethod.GET)
	@SerializationFilter(target = Site.class, fields = { "password" })
	public Object list() {
		QueryCondition queryCondition = new QueryConditionImpl(Site.class, null);
		queryCondition.add(Conditions.ne("id", getCurrentSite().getId()));
		return systemService.findList(queryCondition);
	}

	@RequestMapping(path = "/{siteId}", method = RequestMethod.GET)
	@SerializationFilter(target = Site.class, fields = { "password" })
	public Object get(@PathVariable String siteId) {
		return Result.successResult(systemService.get(siteId), null);
	}

	@RequestMapping(path = "/", method = RequestMethod.POST)
	public Object save(@RequestBody Site site) {
		site = systemService.addSite(site);
		return Result.successResult(site.getId(), "新增成功");
	}

	@RequestMapping(path = "/{siteId}", method = RequestMethod.PUT)
	public Object update(@PathVariable String siteId, @RequestBody Site site) {
		site.setId(siteId);
		systemService.update(site);
		return Result.successResult("更新成功");
	}

	@RequestMapping(path = "/{siteId}", method = RequestMethod.DELETE)
	public Object delete(@PathVariable String siteId) {
		systemService.deleteById(siteId);
		return Result.successResult("删除成功");
	}

	@RequestMapping(path = "/{siteId}/set-status", method = RequestMethod.PUT)
	public Object setStatus(@PathVariable String siteId, Site.Status status) {
		systemService.setSiteStatus(siteId, status);
		return Result.successResult("设置成功");
	}

	@RequestMapping(path = "/file-upload", method = RequestMethod.POST)
	public Object fileUpload(HttpServletRequest request, @RequestParam("file") MultipartFile[] files)
			throws FileNotFoundException, IOException {
		Result result = Result.errorResult("文件不能为空");
		if (files.length > 0) {
			List<ImageInfo> infos = new ArrayList<>(files.length);
			for (MultipartFile file : files) {
				String[] names = file.getOriginalFilename().split("\\.");
				String postfix = names[names.length - 1];
				String root = request.getServletContext().getRealPath("/");
				File webRoot = new File(root);
				File uploadRoot = new File(webRoot, "upload");
				if (!uploadRoot.exists()) {
					uploadRoot.mkdir();
				}
				Site site = getCurrentSite();
				File siteRoot = new File(uploadRoot, site.getId());
				if (!siteRoot.exists()) {
					siteRoot.mkdir();
				}
				String fileName = System.currentTimeMillis() + "." + postfix;
				File uploadFile = new File(siteRoot, fileName);
				FileUtil.copyStream(file.getInputStream(), new FileOutputStream(uploadFile));
				ImageInfo info = new ImageInfo();
				info.setPath(uploadFile.getCanonicalPath().substring(root.length()).replace("\\", "/"));
				info.setFullPath(ImageInfo.buildFullPath(request, info.getPath()));
				infos.add(info);

			}
			result.success(infos, "上传成功，总共" + infos.size() + "个文件");
		}
		return result;
	}

	public static class ImageInfo {
		private String path;
		private String fullPath;

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public String getFullPath() {
			return fullPath;
		}

		public void setFullPath(String fullPath) {
			this.fullPath = fullPath;
		}

		public static String buildFullPath(HttpServletRequest request, String path) {
			String requestUrl = HttpUtils.getRequestURL(request).toString();
			String contextPath = request.getServletContext().getContextPath();
			String fullPath = requestUrl.substring(0, requestUrl.indexOf(contextPath) + contextPath.length()) + "/"
					+ path;
			return fullPath;
		}
	}
}
