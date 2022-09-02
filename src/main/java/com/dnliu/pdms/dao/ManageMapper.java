/**   
 * Copyright © 2019 Liufh. All rights reserved.
 * 
 * @Package: com.ssm.manage.mapper 
 * @author: liufuhua
 * @date: 2019年3月16日 上午10:47:06 
 */
package com.dnliu.pdms.dao;

import java.util.List;
import java.util.Map;

/** 
 * @desc: 数据管理mapper
 * @version 1.0
 * @company: ***
 * @author: liufuhua
 * @since: 2019年3月16日 上午10:47:06 
 *
 */
public interface ManageMapper {
	void insert(Map<String, Object> map);

	void update(Map<String, Object> map);

	void updateByTitle(Map<String, Object> map);

	void delete(Map<String, Object> map);

	List<Map<String, Object>> batch(Map<String, Object> map);

	List<Map<String, Object>> batchAll(Map<String, Object> map);

	Map<String, Object> single(Map<String, Object> map);

	int countData(Map<String, Object> map);

	List<Map<String, Object>> searchTitle(Map<String, Object> map);

	List<Map<String, Object>> searchAll(long userId);
}
