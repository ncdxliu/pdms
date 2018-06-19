//个人数据管理系统-退出
package server

import (
	"encoding/json"
	lg "log4go"
)

type TX0003Response struct {
	TxCode  string `json:"TXCODE"`
	SeqId   string `json:"SEQ_ID"`
	ErrCode string `json:"ERRCODE"`
	ErrMsg  string `json:"ERRMSG"`
}

//退出
func logout(data map[string]string) (string, error) {
	var responseStr string

	//lg.LogInfo.Error("=================logout start ...===================")

	//记录登录日志
	go insertLoginLog(data, "01")

	//生成应答报文
	jsonData := TX0003Response{
		TxCode:  data["TXCODE"],
		SeqId:   data["SEQ_ID"],
		ErrCode: "0000",
		ErrMsg:  "退出成功",
	}

	b, err := json.Marshal(jsonData)
	if err != nil {
		lg.LogInfo.Error("===error====logout==========json.Marshal error=========")
		lg.LogInfo.Error("err: ", err)

		return "1000", err
	}

	responseStr = string(b)

	//lg.LogInfo.Debug("=================logout end===================")

	return responseStr, nil
}
