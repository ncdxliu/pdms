package com.dnliu.pdms.service;

import com.dnliu.pdms.model.AddData;
import com.dnliu.pdms.model.DeleteData;
import com.dnliu.pdms.model.UpdateContentByTitle;
import com.dnliu.pdms.model.UpdateData;

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

}
