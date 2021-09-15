package com.dnliu.pdms.service;

import com.dnliu.pdms.model.*;

import java.util.Map;

/**
 * @author dnliu
 * @date 2021-09-12 22:36
 */
public interface ManageService {
    Map<String, Object> addData(AddData addData);

    Map<String, Object> updateDate(UpdateData updateData);

    Map<String, Object> deleteDate(DeleteData deleteData);

    Map<String, Object> updateContentByTitle(UpdateContentByTitle updateContentByTitle);

    Map<String, Object> getBatch(GetBatch getBatch);

    Map<String, Object> getSingle(GetSingle getSingle);

    Map<String, Object> search(Search search);

}
