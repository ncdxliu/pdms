//个人数据管理系统-单笔查询
package server

import (
	"common"
	"dbopr"
	"encoding/json"
	"fmt"
	lg "log4go"
)

type TX0005Response struct {
	TxCode   string `json:"TXCODE"`
	SeqId    string `json:"SEQ_ID"`
	ErrCode  string `json:"ERRCODE"`
	ErrMsg   string `json:"ERRMSG"`
	DataId   string `json:"DATA_ID"`
	DataType string `json:"DATA_TYPE"`
	Title    string `json:"TITLE"`
	Content  string `json:"CONTENT"`
	AddTime  string `json:"ADD_TIME"`
	ModTime  string `json:"MOD_TIME"`
}

//单笔查询
func singleSelect(data map[string]string) (string, error) {
	//lg.LogInfo.Error("=======================singleSelect start...======================")

	var responseStr string

	var dataId string
	var dataType string
	var title string
	var content string
	var addTime string
	var modTime string

	rows, err := dbopr.Dbconn.Query("select data_id, data_type, title, content1, add_time, mod_time from TB_CONTENT where data_id = ? and status = '00'", data["DATA_ID"])
	if err != nil {
		lg.LogInfo.Error("===error====singleSelect=========Dbconn.Query error===========")
		lg.LogInfo.Error("err: %s", err.Error())

		return "1000", err
	}
	defer rows.Close()

	for rows.Next() {
		err := rows.Scan(&dataId, &dataType, &title, &content, &addTime, &modTime)
		if err != nil {
			lg.LogInfo.Error("===error======singleSelect========rows.Scan error===========")
			lg.LogInfo.Error("err: %s", err.Error())
		}
	}

	//判断数据是否存在
	if len(dataId) == 0 && len(title) == 0 {
		return "1007", fmt.Errorf("数据不存在")
	}

	err = rows.Err()
	if err != nil {
		lg.LogInfo.Error("===error======singleSelect=========rows.Err error==========")
		lg.LogInfo.Error("err: %s", err.Error())
	}

	var errDecrypt error
	//数据解密
	content, errDecrypt = common.DataDecrypt(content)
	if errDecrypt != nil {
		lg.LogInfo.Error("=======error======singleSelect====common.DataDecrypt error==========")
		lg.LogInfo.Error("err: %s", err.Error())

		return "1006", fmt.Errorf("数据解密失败")
	}

	jsonData := TX0005Response{
		TxCode:   data["TXCODE"],
		SeqId:    data["SEQ_ID"],
		ErrCode:  "0000",
		ErrMsg:   "查询成功",
		DataId:   dataId,
		DataType: dataType,
		Title:    title,
		Content:  content,
		AddTime:  addTime,
		ModTime:  modTime,
	}

	b, err := json.Marshal(jsonData)
	if err != nil {
		lg.LogInfo.Error("===error====singleSelect==========json.Marshal error=========")
		lg.LogInfo.Error("err: %s", err.Error())

		return "1000", err
	}

	responseStr = string(b)

	//lg.LogInfo.Debug("========================singleSelect end======================")

	return responseStr, nil
}
