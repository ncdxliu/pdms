/**   
 * Copyright © 2019 Liufh. All rights reserved.
 * 
 * @Package: com.ssm.manage.controller 
 * @author: liufuhua
 * @date: 2019年3月15日 下午5:42:20 
 */
package com.dnliu.pdms.controller;

import com.dnliu.pdms.common.ResponseUtil;
import com.dnliu.pdms.common.utils.StringUtil;
import com.dnliu.pdms.model.*;
import com.dnliu.pdms.service.ManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/** 
 * @desc: 数据管理接口
 * @version 1.0
 * @company: ***
 * @author: liufuhua
 * @since: 2019年3月15日 下午5:42:20 
 *
 */
@Controller
@RequestMapping("/pdms/manageApi")
public class ManageController {

	private ManageService manageService;

	/**
	 * 注入引用服务类
	 * @param manageService
	 */
	@Autowired
	ManageController(ManageService manageService) {
		this.manageService = manageService;
	}
	
	/**
	 * @desc: 数据新增接口
	 * @author: liufuhua
	 * @since: 2019年3月16日 上午10:49:52 
	 * @param
	 * @return
	 * @throws
	 */
	@RequestMapping("/addData")
	public @ResponseBody Map<String, Object> addData(@RequestBody AddData addData) {
		if (addData == null
				|| StringUtil.isBlank(addData.getTitle())
				|| StringUtil.isBlank(addData.getContent())
				|| StringUtil.isBlank(addData.getDataType())) {
			return ResponseUtil.getCommonFailResponse("输入参数有误");
		}

		return manageService.addData(addData);
	}
	
	/**
	 * @desc: 数据修改接口
	 * @author: liufuhua
	 * @since: 2019年3月16日 上午10:54:24 
	 * @param
	 * @return
	 * @throws
	 */
	@RequestMapping("/updateData")
	public @ResponseBody Map<String, Object> updateDate(@RequestBody UpdateData updateData) {
		if (updateData == null
				|| updateData.getId() == null
				|| StringUtil.isBlank(updateData.getTitle())
				|| StringUtil.isBlank(updateData.getContent())
				|| StringUtil.isBlank(updateData.getDataType())) {
			return ResponseUtil.getCommonFailResponse("输入参数有误");
		}

		return manageService.updateDate(updateData);
	}
	
	/**
	 * @desc: 数据删除接口
	 * @author: liufuhua
	 * @since: 2019年3月16日 上午10:54:24 
	 * @param
	 * @return
	 * @throws
	 */
	@RequestMapping("/deleteData")
	public @ResponseBody Map<String, Object> deleteDate(@RequestBody DeleteData deleteData) {
		if (deleteData == null || deleteData.getId() == null) {
			return ResponseUtil.getCommonFailResponse("输入参数有误");
		}

		return manageService.deleteDate(deleteData);
	}
	
	/**
	 * @desc: 根据标题修改数据接口
	 * @author: liufuhua
	 * @since: 2019年3月16日 上午10:54:24 
	 * @param
	 * @return
	 * @throws
	 */
	@RequestMapping("/updateContentByTitle")
	public @ResponseBody Map<String, Object> updateContentByTitle(@RequestBody UpdateContentByTitle updateContentByTitle) {
		if (updateContentByTitle == null
				|| StringUtil.isBlank(updateContentByTitle.getTitle())) {
			return ResponseUtil.getCommonFailResponse("输入参数有误");
		}

		return manageService.updateContentByTitle(updateContentByTitle);
	}
	
	/**
	 * @desc: 批量查询接口
	 * @author: liufuhua
	 * @since: 2019年3月16日 上午10:54:24 
	 * @param
	 * @return
	 * @throws
	 */
	@RequestMapping("/getBatch")
	public @ResponseBody Map<String, Object> getBatch(@RequestBody GetBatch getBatch) {
		if (getBatch == null || StringUtil.isBlank(getBatch.getDataType())) {
			return ResponseUtil.getCommonFailResponse("输入参数有误");
		}

		return manageService.getBatch(getBatch);
	}
	
	/**
	 * @desc: 单笔查询接口
	 * @author: liufuhua
	 * @since: 2019年3月16日 上午10:54:24 
	 * @param
	 * @return
	 * @throws
	 */
	@RequestMapping("/getSingle")
	public @ResponseBody Map<String, Object> getSingle(@RequestBody GetSingle getSingle) {
		if (getSingle == null || getSingle.getId() == null) {
			return ResponseUtil.getCommonFailResponse("输入参数有误");
		}
		return manageService.getSingle(getSingle);
	}
	
	/**
	 * @desc: 数据搜索
	 * @author: liufuhua
	 * @since: 2019年3月26日 下午3:37:39 
	 * @param
	 * @return
	 * @throws
	 */
	@RequestMapping("/search")
	public @ResponseBody Map<String, Object> search(@RequestBody Search search) {
		if (search == null || StringUtil.isBlank(search.getSearchStr())) {
			return ResponseUtil.getCommonFailResponse("输入参数有误");
		}
		return manageService.search(search);
	}

	/**
	 * 登录日志批量查询接口
	 * @param getBatch
	 * @return
	 */
	@RequestMapping("/getLoginLogBatch")
	public @ResponseBody Map<String, Object> getLoginLogBatch(@RequestBody GetBatch getBatch) {
		if (getBatch == null) {
			return ResponseUtil.getCommonFailResponse("输入参数有误");
		}

		return manageService.getLoginLogBatch(getBatch);
	}
}
