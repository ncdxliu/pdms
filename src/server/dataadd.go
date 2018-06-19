//个人数据管理系统-数据新增
package server

import (
	"common"
	"dbopr"
	"encoding/json"
	"fmt"
	lg "log4go"
	"strconv"
)

type TX0006Response struct {
	TxCode  string `json:"TXCODE"`
	SeqId   string `json:"SEQ_ID"`
	ErrCode string `json:"ERRCODE"`
	ErrMsg  string `json:"ERRMSG"`
	DataId  string `json:"DATA_ID"`
}

//数据新增
func dataAdd(data map[string]string) (string, error) {
	//lg.LogInfo.Error("==================dataAdd start ...====================")

	var responseStr string

	//判断数据标题是否存在
	bl, err := titleIsExist(data["TITLE"], data["USER_ID"])
	if err != nil {
		lg.LogInfo.Error("=====error======dataAdd error==========")
		lg.LogInfo.Error("err: %s", err.Error())

		return "1000", err
	}

	if bl {
		return "1004", fmt.Errorf("该标题数据已存在!")
	}

	//数据内容要加密
	//3des加密
	keyData, err := common.DataEncrypt(data["CONTENT"])
	if err != nil {
		lg.LogInfo.Error("======error=======dataAdd=========common.dataEncrypt error===========")
		lg.LogInfo.Error("err: %s", err.Error())

		return "1005", fmt.Errorf("数据加密失败")
	}

	data["CONTENT"] = keyData

	//获取data_id
	dataId := common.GetGuid()

	data["DATA_ID"] = dataId

	//插入数据表
	err1 := insertContent(data)
	if err1 != nil {
		lg.LogInfo.Error("=====error======dataAdd--=====insertContent error==========")
		lg.LogInfo.Error("err: %s", err1.Error())

		return "1000", err1
	}

	//生成应答报文
	jsonData := TX0006Response{
		TxCode:  data["TXCODE"],
		SeqId:   data["SEQ_ID"],
		ErrCode: "0000",
		ErrMsg:  "添加成功",
		DataId:  dataId,
	}

	b, err := json.Marshal(jsonData)
	if err != nil {
		lg.LogInfo.Error("===error====dataAdd==========json.Marshal error=========")
		lg.LogInfo.Error("err: %s", err.Error())

		return "1000", err
	}

	responseStr = string(b)

	//lg.LogInfo.Error("==================dataAdd end====================")

	return responseStr, nil
}

//判断数据标题是否存在
func titleIsExist(title string, userId string) (bool, error) {
	rows, err := dbopr.Dbconn.Query("select count(1) from TB_CONTENT where user_id = ? and title = ? and status = '00'", userId, title)
	if err != nil {
		lg.LogInfo.Error("===error====titleIsExist=========Dbconn.Query error===========")
		lg.LogInfo.Error("err: %s", err.Error())

		return false, err
	}
	defer rows.Close()

	var strCnt string

	for rows.Next() {
		err := rows.Scan(&strCnt)
		if err != nil {
			lg.LogInfo.Error("===error======titleIsExist========rows.Scan error===========")
			lg.LogInfo.Error("err: %s", err.Error())
		}

		//lg.LogInfo.Error("id: ", id, ", name: ", name, ", sex: ", sex, ", role: ", role, ", branch: ", branch)
	}

	err = rows.Err()
	if err != nil {
		lg.LogInfo.Error("===error======titleIsExist=========rows.Err error==========")
		lg.LogInfo.Error("err: %s", err.Error())
	}

	lg.LogInfo.Error("titleIsExist ==> strCnt: ", strCnt)
	n, err := strconv.ParseInt(strCnt, 10, 32)
	if err != nil {
		lg.LogInfo.Error("====error======titleIsExist========strconv.ParseInt error=========")
		lg.LogInfo.Error("err: %s", err.Error())

		return false, err
	}

	if n >= 1 {
		return true, nil
	}

	return false, nil
}

//插入数据表
func insertContent(data map[string]string) error {

	//判断数据长度
	if len(data["CONTENT"]) > 65532 {
		return fmt.Errorf("内容太长，请缩减内容！")
	}

	if len(data["TITLE"]) > 120 {
		return fmt.Errorf("标题太长，请缩减标题")
	}

	nowDate := common.GetNowDate()
	nowTime := common.GetNowDateTime()

	stmt, err := dbopr.Dbconn.Prepare("INSERT INTO TB_CONTENT (DATA_ID, DATA_DATE, USER_ID, DATA_TYPE, TITLE, CONTENT1, STATUS, ADD_DATE, ADD_TIME, MOD_DATE, MOD_TIME) values (?,?,?,?,?,?,?,?,?,?,?)")
	if err != nil {
		lg.LogInfo.Error("====error=====insertContent=======dbopr.Dbconn.Prepare error=======")
		lg.LogInfo.Error("err: %s", err.Error())

		return err
	}

	_, err1 := stmt.Exec(data["DATA_ID"], nowDate, data["USER_ID"], data["DATA_TYPE"], data["TITLE"], data["CONTENT"], "00", nowDate, nowTime, nowDate, nowTime)
	if err1 != nil {
		lg.LogInfo.Error("====error=====insertContent===========stmt.Exec error==========")
		lg.LogInfo.Error("err: %s", err1.Error())

		return err1
	}
	stmt.Close()

	//	//获得插入的id
	//	id, err := rs.LastInsertId()

	//	lg.LogInfo.Error("======insertContent=======saveResult id==", id)
	//	//获得影响行数
	//	affect, err := rs.RowsAffected()
	//	lg.LogInfo.Error("=======insertContent======saveResult affect==", affect)

	//进行数据备份
	go backup()

	return nil
}
