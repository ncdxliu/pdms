package com.dnliu.pdms.service.impl;

import com.alibaba.fastjson.JSON;
import com.dnliu.pdms.common.ResponseUtil;
import com.dnliu.pdms.common.utils.AppUtil;
import com.dnliu.pdms.common.utils.SecretUtils;
import com.dnliu.pdms.dao.ManageMapper;
import com.dnliu.pdms.model.AddData;
import com.dnliu.pdms.model.DeleteData;
import com.dnliu.pdms.model.UpdateContentByTitle;
import com.dnliu.pdms.model.UpdateData;
import com.dnliu.pdms.service.ManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dnliu
 * @date 2021-09-12 22:36
 */
@Service
public class ManageServiceImpl implements ManageService {
    private static final Logger logger = LoggerFactory.getLogger(ManageServiceImpl.class);

    @Autowired
    private ManageMapper manageMapper;

    /**
     * 新增数据
     * @param addData
     * @return
     */
    @Override
    public Map addData(AddData addData) {
        Map rspMap = new HashMap<>();

        logger.info(JSON.toJSONString(addData));

        long userId = AppUtil.getUser().getId();

        String content = addData.getContent();
        try {
            content = SecretUtils.encryption(content);
        } catch (Exception e) {
            logger.error("数据加密失败, content: [{}], e: ", content, e);
            return ResponseUtil.getCommonFailResponse("数据加密失败");
        }

        Map map = new HashMap<>();
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
    public Map updateDate(UpdateData updateData) {
        Map rspMap = new HashMap<>();

        logger.info(JSON.toJSONString(updateData));

        long userId = AppUtil.getUser().getId();

        String content = updateData.getContent();
        try {
            content = SecretUtils.encryption(content);
        } catch (Exception e) {
            logger.error("数据加密失败, content: [{}], e: ", content, e);
            return ResponseUtil.getCommonFailResponse("数据加密失败");
        }

        Map map = new HashMap<>();
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
    public Map deleteDate(DeleteData deleteData) {
        Map rspMap = new HashMap<>();

        logger.info(JSON.toJSONString(deleteData));

        long userId = AppUtil.getUser().getId();

        Map map = new HashMap<>();
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
    public Map updateContentByTitle(UpdateContentByTitle updateContentByTitle) {
        Map rspMap = new HashMap<>();

        logger.info(JSON.toJSONString(updateContentByTitle));

        long userId = AppUtil.getUser().getId();

        String content = updateContentByTitle.getContent();
        try {
            content = SecretUtils.encryption(content);
        } catch (Exception e) {
            logger.error("数据加密失败, content: [{}], e: ", content, e);
            return ResponseUtil.getCommonFailResponse("数据加密失败");
        }

        Map map = new HashMap<>();
        map.put("userId", userId);
        map.put("title", updateContentByTitle.getTitle());
        map.put("content1", content);

        //出现异常，会进行全局异常处理
        manageMapper.updateByTitle(map);

        rspMap.put("rspCode", "R0000");
        rspMap.put("rspMsg", "修改成功");

        return rspMap;
    }
}
