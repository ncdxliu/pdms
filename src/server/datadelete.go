//个人数据管理系统-数据删除
package server

import (
	"dbopr"
	"encoding/json"
	lg "log4go"
)

type TX0008Response struct {
	TxCode  string `json:"TXCODE"`
	SeqId   string `json:"SEQ_ID"`
	ErrCode string `json:"ERRCODE"`
	ErrMsg  string `json:"ERRMSG"`
}

//数据删除
func dataDelete(data map[string]string) (string, error) {
	//lg.LogInfo.Error("=================dataDelete start ...==================")
	var responseStr string

	//数据库数据标记为删除状态
	err := delContent(data)
	if err != nil {
		lg.LogInfo.Error("======error======dataDelete=======delContent===========")
		lg.LogInfo.Error("err: %s", err.Error())

		return "1000", err
	}

	//生成应答报文
	jsonData := TX0008Response{
		TxCode:  data["TXCODE"],
		SeqId:   data["SEQ_ID"],
		ErrCode: "0000",
		ErrMsg:  "删除成功",
	}

	b, err := json.Marshal(jsonData)
	if err != nil {
		lg.LogInfo.Error("===error====dataDelete==========json.Marshal error=========")
		lg.LogInfo.Error("err: %s", err.Error())

		return "1000", err
	}

	responseStr = string(b)

	//lg.LogInfo.Error("===================dataDelete end======================")

	return responseStr, nil
}

//数据库删除数据
func delContent(data map[string]string) error {

	stmt, err := dbopr.Dbconn.Prepare("UPDATE TB_CONTENT SET STATUS = '01' WHERE DATA_ID = ?")
	if err != nil {
		lg.LogInfo.Error("====error=====modContent=======dbopr.Dbconn.Prepare error=======")
		lg.LogInfo.Error("err: %s", err.Error())

		return err
	}

	_, err1 := stmt.Exec(data["DATA_ID"])
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
