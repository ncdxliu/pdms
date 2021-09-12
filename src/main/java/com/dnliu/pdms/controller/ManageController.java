/**   
 * Copyright © 2019 Liufh. All rights reserved.
 * 
 * @Package: com.ssm.manage.controller 
 * @author: liufuhua
 * @date: 2019年3月15日 下午5:42:20 
 */
package com.dnliu.pdms.controller;

import com.dnliu.pdms.common.ResponseUtil;
import com.dnliu.pdms.common.utils.DateUtils;
import com.dnliu.pdms.common.utils.SecretUtils;
import com.dnliu.pdms.model.*;
import com.dnliu.pdms.service.ManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	public @ResponseBody Map addData(@RequestBody AddData addData) {
		if (addData == null) {
			return ResponseUtil.getCommonFailResponse("请输入新增数据参数");
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
	public @ResponseBody Map updateDate(@RequestBody UpdateData updateData) {
		if (updateData == null) {
			return ResponseUtil.getCommonFailResponse("请输入修改数据参数");
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
	public @ResponseBody Map deleteDate(@RequestBody DeleteData deleteData) {
		if (deleteData == null) {
			return ResponseUtil.getCommonFailResponse("请输入删除数据参数");
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
	public @ResponseBody Map updateContentByTitle(@RequestBody UpdateContentByTitle updateContentByTitle) {
		if (updateContentByTitle == null) {
			return ResponseUtil.getCommonFailResponse("请输入修改数据参数");
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(ManageConstant.getBatch)
	public @ResponseBody Map getBatch(@RequestBody GetBatch getBatch, HttpSession session) {
		Map rspMap = new HashMap<>();
		
		log.info(getBatch.toString());
		
		long userId = TokenManane.getUserIdFromToken(session);
		
		Map map = new HashMap<>();
		map.put("userId", userId);
		map.put("dataType", getBatch.getDataType());
		int count = getBatch.getCount();
		int pageNum = getBatch.getPageNum();
		if (pageNum < 0) {
			return errorHandle(rspMap, null, "前端传入的查询页码不对, pageNum: " + pageNum);
		}
		
		List<Map> result = null;
		if (pageNum > 0) {
			int startRow = (pageNum - 1) * count;
			map.put("count", count);
			map.put("startRow", startRow);
			
			result = manageMapper.batch(map);
			if (result.isEmpty()) {
				return errorHandle(rspMap, null, "查询无记录");
			}
		} else {
			result = manageMapper.batchAll(map);
			if (result.isEmpty()) {
				return errorHandle(rspMap, null, "查询无记录");
			}
		}
		
		rspMap.put("rspCode", "R0000");
		rspMap.put("rspMsg", "查询成功");
		rspMap.put("count", result.size());
		rspMap.put("data", result);
		
		return rspMap;
	}
	
	/**
	 * @desc: 单笔查询接口
	 * @author: liufuhua
	 * @since: 2019年3月16日 上午10:54:24 
	 * @param
	 * @return
	 * @throws
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(ManageConstant.getSingle)
	public @ResponseBody Map getSingle(@RequestBody GetSingle getSingle, HttpSession session) {
		Map rspMap = new HashMap<>();
		
		log.info(getSingle.toString());
		
		long userId = TokenManane.getUserIdFromToken(session);
		
		Map map = new HashMap<>();
		map.put("userId", userId);
		map.put("id", getSingle.getId());
		
		Map singleMap = manageMapper.single(map);
		if (null == singleMap || singleMap.size() == 0) {
			return errorHandle(rspMap, null, "查询无记录!");
		}
		
		String content = (String) singleMap.get("content1");
		try {
			content = SecretUtils.decrypt(content);
		} catch (Exception e) {
			return errorHandle(rspMap, e, "解密数据失败");
		}
		
		String createTime = DateUtils.dateToStr((Timestamp) singleMap.get("create_time"), DateUtils.DATE_TIME_PATTERN);
		String updateTime = DateUtils.dateToStr((Timestamp) singleMap.get("update_time"), DateUtils.DATE_TIME_PATTERN);
		String dataType = (String) singleMap.get("data_type");
//		if ("0000".equals(dataType)) {
//			dataType = "普通数据";
//		} else {
//			dataType = "重要数据";
//		}
		
		rspMap.put("rspCode", "R0000");
		rspMap.put("rspMsg", "查询成功!");
		rspMap.put("id", singleMap.get("id"));
		rspMap.put("title", singleMap.get("title"));
		rspMap.put("content", content);
		rspMap.put("createTime", createTime);
		rspMap.put("updateTime", updateTime);
		rspMap.put("dataType", dataType);
		
		return rspMap;
	}
	
	/**
	 * @desc: 数据搜索
	 * @author: liufuhua
	 * @since: 2019年3月26日 下午3:37:39 
	 * @param
	 * @return
	 * @throws
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(ManageConstant.search)
	public @ResponseBody Map search(@RequestBody Search search, HttpSession session) {
		Map rspMap = new HashMap<>();
		
		log.info(search.toString());
		long userId = TokenManane.getUserIdFromToken(session);
		
		String searchFlag = search.getSeachFlag();
		String searchStr = search.getSearchStr();
		
		List<Map> resultList = new ArrayList<>();
		
		if ("0".equals(searchFlag)) {  //只搜索标题
			Map map = new HashMap<>();
			map.put("userId", userId);
			map.put("searchStr", searchStr);
			resultList = manageMapper.searchTitle(map);
		} else {  //搜索内容和标题
			List<Map> tmpMap = manageMapper.searchAll(userId);
			int size = tmpMap.size();
			
			for (int i = 0; i < size; i++) {
				Map map = tmpMap.get(i);
				String title = (String) map.get("title");
				if (title.contains(searchStr)) {
					Map rsMap = new HashMap<>();
					rsMap.put("id", map.get("id"));
					rsMap.put("title", map.get("title"));
					
					resultList.add(rsMap);
					continue;
				}
				
				String content = (String) map.get("content1");
				try {
					content = SecretUtils.decrypt(content);
				} catch (Exception e) {
					log.error("解密失败, error: " + e.getMessage() + ", id: " + map.get("id"));
					e.printStackTrace();
					continue;
				}
				
				if (content.contains(searchStr)) {
					Map rsMap = new HashMap<>();
					rsMap.put("id", map.get("id"));
					rsMap.put("title", map.get("title"));
					
					resultList.add(rsMap);
				}
			}
		}
		
		rspMap.put("rspCode", "R0000");
		rspMap.put("rspMsg", "查询成功!");
		int count = resultList.size();
		rspMap.put("count", count);
		rspMap.put("data", resultList);
		
		//System.out.println("rspMap: " + rspMap);
		
		return rspMap;
	}
	
	/**
	 * @desc: 出错后生成应答报文
	 * @author: liufuhua
	 * @since: 2019年3月18日 下午10:24:36 
	 * @param
	 * @return
	 * @throws
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map errorHandle(Map map, Exception e, String errMsg) {
		map.put("rspCode", "R9999");
		if (null == e) {
			map.put("rspMsg", errMsg);
		} else {
			map.put("rspMsg", errMsg + ", error: " + e.getMessage());
			
			log.error(errMsg + ", error: " + e.getMessage());
			e.printStackTrace();
		}
		
		return map;
	}
	
}
