//个人数据管理系统-数据修改
package server

import (
	"common"
	"dbopr"
	"encoding/json"
	"fmt"
	lg "log4go"
	"strconv"
)

type TX0007Response struct {
	TxCode  string `json:"TXCODE"`
	SeqId   string `json:"SEQ_ID"`
	ErrCode string `json:"ERRCODE"`
	ErrMsg  string `json:"ERRMSG"`
}

//数据修改
func dataModify(data map[string]string) (string, error) {
	//lg.LogInfo.Error("=================dataModify start ...==================")
	var responseStr string

	//判断标题是否为空
	if len(data["DATA_TYPE"]) == 0 {
		return "1014", fmt.Errorf("数据类型不能为空!")
	}

	//判断修改的标题是否存在
	bl, err := modTitleIsExist(data["TITLE"], data["DATA_ID"], data["USER_ID"])
	if err != nil {
		lg.LogInfo.Error("=====error======dataModify error==========")
		lg.LogInfo.Error("err: %s", err.Error())

		return "1000", err
	}

	if bl {
		return "1004", fmt.Errorf("该标题数据已存在!")
	}

	//数据内容加密
	//3des加密
	keyData, err := common.DataEncrypt(data["CONTENT"])
	if err != nil {
		lg.LogInfo.Error("======error=======dataModify=========common.dataEncrypt error===========")
		lg.LogInfo.Error("err: %s", err.Error())

		return "1005", fmt.Errorf("数据加密失败")
	}

	data["CONTENT"] = keyData

	//数据库修改数据
	err = modContent(data)
	if err != nil {
		lg.LogInfo.Error("======error======dataModify=======modContent===========")
		lg.LogInfo.Error("err: %s", err.Error())

		return "1000", err
	}

	//生成应答报文
	jsonData := TX0007Response{
		TxCode:  data["TXCODE"],
		SeqId:   data["SEQ_ID"],
		ErrCode: "0000",
		ErrMsg:  "修改成功",
	}

	b, err := json.Marshal(jsonData)
	if err != nil {
		lg.LogInfo.Error("===error====dataModify==========json.Marshal error=========")
		lg.LogInfo.Error("err: %s", err.Error())

		return "1000", err
	}

	responseStr = string(b)

	//lg.LogInfo.Error("===================dataModify end======================")

	return responseStr, nil
}

//判断数据标题是否存在
func modTitleIsExist(title string, dataId string, userId string) (bool, error) {
	rows, err := dbopr.Dbconn.Query("select count(1) from TB_CONTENT where data_id != ? and title = ? and user_id = ? and status = '00'", dataId, title, userId)
	if err != nil {
		lg.LogInfo.Error("===error====modTitleIsExist=========Dbconn.Query error===========")
		lg.LogInfo.Error("err: %s", err.Error())

		return false, err
	}
	defer rows.Close()

	var strCnt string

	for rows.Next() {
		err := rows.Scan(&strCnt)
		if err != nil {
			lg.LogInfo.Error("===error======modTitleIsExist========rows.Scan error===========")
			lg.LogInfo.Error("err: %s", err.Error())
		}

		//lg.LogInfo.Error("id: ", id, ", name: ", name, ", sex: ", sex, ", role: ", role, ", branch: ", branch)
	}

	err = rows.Err()
	if err != nil {
		lg.LogInfo.Error("===error======modTitleIsExist=========rows.Err error==========")
		lg.LogInfo.Error("err: %s", err.Error())
	}

	lg.LogInfo.Debug("modTitleIsExist ==> strCnt: %s", strCnt)
	n, err := strconv.ParseInt(strCnt, 10, 32)
	if err != nil {
		lg.LogInfo.Error("====error======modTitleIsExist========strconv.ParseInt error=========")
		lg.LogInfo.Error("err: %s", err.Error())

		return false, err
	}

	if n >= 1 {
		return true, nil
	}

	return false, nil
}

//数据库修改数据
func modContent(data map[string]string) error {
	nowDate := common.GetNowDate()
	nowTime := common.GetNowDateTime()

	/*
		DATA_ID
		DATA_DATE
		USER_ID
		DATA_TYPE
		TITLE
		CONTENT1
		CONTENT2
		CONTENT3
		CONTENT4
		CONTENT5
		STATUS
		ADD_DATE
		ADD_TIME
		MOD_DATE
		MOD_TIME
	*/

	stmt, err := dbopr.Dbconn.Prepare("UPDATE TB_CONTENT SET DATA_TYPE = ?, TITLE = ?, CONTENT1 = ?, MOD_DATE = ?, MOD_TIME = ? WHERE DATA_ID = ?")
	if err != nil {
		lg.LogInfo.Error("====error=====modContent=======dbopr.Dbconn.Prepare error=======")
		lg.LogInfo.Error("err: %s", err.Error())

		return err
	}

	_, err1 := stmt.Exec(data["DATA_TYPE"], data["TITLE"], data["CONTENT"], nowDate, nowTime, data["DATA_ID"])
	if err1 != nil {
		lg.LogInfo.Error("====error=====modContent===========stmt.Exec error==========")
		lg.LogInfo.Error("err: %s", err1.Error())

		return err1
	}
	stmt.Close()

	//进行数据备份
	go backup()

	return nil
}
