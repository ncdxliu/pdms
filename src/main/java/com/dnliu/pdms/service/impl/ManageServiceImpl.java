package com.dnliu.pdms.service.impl;

import com.alibaba.fastjson.JSON;
import com.dnliu.pdms.common.ResponseUtil;
import com.dnliu.pdms.common.utils.AppUtil;
import com.dnliu.pdms.common.utils.DateUtils;
import com.dnliu.pdms.common.utils.SecretUtils;
import com.dnliu.pdms.dao.ManageMapper;
import com.dnliu.pdms.model.*;
import com.dnliu.pdms.service.ManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dnliu
 * @date 2021-09-12 22:36
 */
@Service
public class ManageServiceImpl implements ManageService {
    private static final Logger logger = LoggerFactory.getLogger(ManageServiceImpl.class);

    private ManageMapper manageMapper;

    @Autowired
    ManageServiceImpl(ManageMapper manageMapper) {
        this.manageMapper = manageMapper;
    }

    /**
     * 新增数据
     * @param addData
     * @return
     */
    @Override
    public Map<String, Object> addData(AddData addData) {
        Map<String, Object> rspMap = new HashMap<>();

        if (addData.getContent().length() > 1677721L) {
            return ResponseUtil.getCommonFailResponse("新增的数据内容太大");
        }

        logger.info(JSON.toJSONString(addData));

        long userId = AppUtil.getUser().getId();

        String content = addData.getContent();
        try {
            content = SecretUtils.encryption(content);
        } catch (Exception e) {
            logger.error("数据加密失败, content: [{}], e: ", content, e);
            return ResponseUtil.getCommonFailResponse("数据加密失败");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("dataType", addData.getDataType());
        map.put("title", addData.getTitle());
        map.put("content1", content);

        int count = manageMapper.countData(map);
        if (count > 0) {
            return ResponseUtil.getCommonFailResponse("该标题的数据已存在");
        }

        //出现异常，会进行全局异常处理
        manageMapper.insert(map);

        rspMap.put("rspCode", "R0000");
        rspMap.put("rspMsg", "添加数据成功");

        return rspMap;
    }

    /**
     * 修改数据
     * @param updateData
     * @return
     */
    @Override
    public Map<String, Object> updateDate(UpdateData updateData) {
        Map<String, Object> rspMap = new HashMap<>();

        if (updateData.getContent().length() > 1677721L) {
            return ResponseUtil.getCommonFailResponse("新增的数据内容太大");
        }

        logger.info(JSON.toJSONString(updateData));

        long userId = AppUtil.getUser().getId();

        String content = updateData.getContent();
        try {
            content = SecretUtils.encryption(content);
        } catch (Exception e) {
            logger.error("数据加密失败, content: [{}], e: ", content, e);
            return ResponseUtil.getCommonFailResponse("数据加密失败");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("dataType", updateData.getDataType());
        map.put("title", updateData.getTitle());
        map.put("id", updateData.getId());
        map.put("content1", content);
        map.put("content2", "");
        map.put("content3", "");
        map.put("content4", "");
        map.put("content5", "");
        map.put("userId", userId);

        //出现异常，会进行全局异常处理
        manageMapper.update(map);

        rspMap.put("rspCode", "R0000");
        rspMap.put("rspMsg", "修改成功");

        return rspMap;
    }

    /**
     * 删除数据
     * @param deleteData
     * @return
     */
    @Override
    public Map<String, Object> deleteDate(DeleteData deleteData) {
        Map<String, Object> rspMap = new HashMap<>();

        logger.info(JSON.toJSONString(deleteData));

        long userId = AppUtil.getUser().getId();

        Map<String, Object> map = new HashMap<>();
        map.put("id", deleteData.getId());
        map.put("userId", userId);

        //出现异常，会进行全局异常处理
        manageMapper.delete(map);

        rspMap.put("rspCode", "R0000");
        rspMap.put("rspMsg", "删除成功");

        return rspMap;
    }

    /**
     * 根据标题修改数据
     * @param updateContentByTitle
     * @return
     */
    @Override
    public Map<String, Object> updateContentByTitle(UpdateContentByTitle updateContentByTitle) {
        Map<String, Object> rspMap = new HashMap<>();

        logger.info(JSON.toJSONString(updateContentByTitle));

        long userId = AppUtil.getUser().getId();

        String content = updateContentByTitle.getContent();
        try {
            content = SecretUtils.encryption(content);
        } catch (Exception e) {
            logger.error("数据加密失败, content: [{}], e: ", content, e);
            return ResponseUtil.getCommonFailResponse("数据加密失败");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("title", updateContentByTitle.getTitle());
        map.put("content1", content);

        //出现异常，会进行全局异常处理
        manageMapper.updateByTitle(map);

        rspMap.put("rspCode", "R0000");
        rspMap.put("rspMsg", "修改成功");

        return rspMap;
    }

    /**
     * 批量查询数据
     * @param getBatch
     * @return
     */
    @Override
    public Map<String, Object> getBatch(GetBatch getBatch) {
        Map<String, Object> rspMap = new HashMap<>();

        logger.info(JSON.toJSONString(getBatch));

        long userId = AppUtil.getUser().getId();

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("dataType", getBatch.getDataType());
        int count = getBatch.getCount();
        int pageNum = getBatch.getPageNum();
        if (pageNum < 0) {
            return ResponseUtil.getCommonFailResponse("前端传入的查询页码错误");
        }

        List<Map<String, Object>> result = null;
        if (pageNum > 0) {
            int startRow = (pageNum - 1) * count;
            map.put("count", count);
            map.put("startRow", startRow);

            result = manageMapper.batch(map);
        } else {
            result = manageMapper.batchAll(map);
        }

        rspMap.put("rspCode", "R0000");
        rspMap.put("rspMsg", "查询成功");
        rspMap.put("count", result.size());
        rspMap.put("data", result);

        return rspMap;
    }

    /**
     * 查询单条数据
     * @param getSingle
     * @return
     */
    @Override
    public Map<String, Object> getSingle(GetSingle getSingle) {
        Map<String, Object> rspMap = new HashMap<>();

        logger.info(JSON.toJSONString(getSingle));

        long userId = AppUtil.getUser().getId();

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("id", getSingle.getId());

        Map<String, Object> singleMap = manageMapper.single(map);
        if (null == singleMap || singleMap.size() == 0) {
            return ResponseUtil.getCommonFailResponse("查询无记录");
        }

        String content = (String) singleMap.get("content1");
        try {
            content = SecretUtils.decrypt(content);
        } catch (Exception e) {
            return ResponseUtil.getCommonFailResponse("解密数据失败");
        }

        String createTime = DateUtils.dateToStr((Timestamp) singleMap.get("create_time"), DateUtils.DATE_TIME_PATTERN);
        String updateTime = DateUtils.dateToStr((Timestamp) singleMap.get("update_time"), DateUtils.DATE_TIME_PATTERN);
        String dataType = (String) singleMap.get("data_type");

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
     * 数据搜索
     * @param search
     * @return
     */
    @Override
    public Map<String, Object> search(Search search) {
        Map<String, Object> rspMap = new HashMap<>();

        logger.info(JSON.toJSONString(search));
        long userId = AppUtil.getUser().getId();

        String searchFlag = search.getSeachFlag();
        String searchStr = search.getSearchStr();

        List<Map<String, Object>> resultList = new ArrayList<>();

        if ("0".equals(searchFlag)) {  //只搜索标题
            Map<String, Object> map = new HashMap<>();
            map.put("userId", userId);
            map.put("searchStr", searchStr);
            resultList = manageMapper.searchTitle(map);
        } else {  //搜索内容和标题
            List<Map<String, Object>> tmpMap = manageMapper.searchAll(userId);
            int size = tmpMap.size();

            for (int i = 0; i < size; i++) {
                Map<String, Object> map = tmpMap.get(i);
                String title = (String) map.get("title");
                if (title.contains(searchStr)) {
                    Map<String, Object> rsMap = new HashMap<>();
                    rsMap.put("id", map.get("id"));
                    rsMap.put("title", map.get("title"));

                    resultList.add(rsMap);
                    continue;
                }

                String content = (String) map.get("content1");
                try {
                    content = SecretUtils.decrypt(content);
                } catch (Exception e) {
                    logger.error("解密失败, id: {}, content: {}, e: ", map.get("id"), content, e);
                    continue;
                }

                if (content.contains(searchStr)) {
                    Map<String, Object> rsMap = new HashMap<>();
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

        return rspMap;
    }
}
