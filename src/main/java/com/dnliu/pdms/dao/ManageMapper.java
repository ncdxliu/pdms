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
	void insert(Map map);

	void update(Map map);

	void updateByTitle(Map map);

	void delete(Map map);

	List<Map> batch(Map map);

	List<Map> batchAll(Map map);

	Map single(Map map);

	int countData(Map map);

	List<Map> searchTitle(Map map);

	List<Map> searchAll(long userId);
}
