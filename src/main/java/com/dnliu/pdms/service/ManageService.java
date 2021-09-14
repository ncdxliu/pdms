package com.dnliu.pdms.service;

import com.dnliu.pdms.model.*;

import java.util.Map;

/**
 * @author dnliu
 * @date 2021-09-12 22:36
 */
public interface ManageService {
    Map addData(AddData addData);

    Map updateDate(UpdateData updateData);

    Map deleteDate(DeleteData deleteData);

    Map updateContentByTitle(UpdateContentByTitle updateContentByTitle);

    Map getBatch(GetBatch getBatch);

    Map getSingle(GetSingle getSingle);

    Map search(Search search);

}
