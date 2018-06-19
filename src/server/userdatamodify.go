//个人数据管理系统-用户资料修改
package server

import (
	"common"
	"dbopr"
	"encoding/json"
	lg "log4go"
)

type TX0012Response struct {
	TxCode  string `json:"TXCODE"`
	SeqId   string `json:"SEQ_ID"`
	ErrCode string `json:"ERRCODE"`
	ErrMsg  string `json:"ERRMSG"`
}

//用户资料修改
func updateUser(data map[string]string) (string, error) {
	//lg.LogInfo.Error("======================updateUser start ...===================")

	//更新用户资料
	err1 := updateUserData(data)
	if err1 != nil {
		lg.LogInfo.Error("======error========updateUser========updateUserData error=============")
		lg.LogInfo.Error("err: %s", err1.Error())

		return "1000", err1
	}

	//生成应答报文
	jsonData := TX0012Response{
		TxCode:  data["TXCODE"],
		SeqId:   data["SEQ_ID"],
		ErrCode: "0000",
		ErrMsg:  "修改成功",
	}

	b, err := json.Marshal(jsonData)
	if err != nil {
		lg.LogInfo.Error("===error====updateUser==========json.Marshal error=========")
		lg.LogInfo.Error("err: %s", err.Error())

		return "1000", err
	}

	responseStr := string(b)

	//lg.LogInfo.Error("======================updateUser end===================")

	return responseStr, nil
}

//更新用户资料
func updateUserData(data map[string]string) error {
	nowDate := common.GetNowDate()
	nowTime := common.GetNowDateTime()

	stmt, err := dbopr.Dbconn.Prepare("update TB_USER set USER_EMAIL = ?, USER_PHONE = ?, REMARKS = ?, DATA_MOD_DATE = ?, DATA_MOD_TIME = ? where USER_ID = ? and DATA_STATUS != '04'")
	if err != nil {
		lg.LogInfo.Error("====error=====updateNewPassword=======dbopr.Dbconn.Prepare error=======")
		lg.LogInfo.Error("err: %s", err.Error())

		return err
	}

	_, err1 := stmt.Exec(data["USER_EMAIL"], data["USER_PHONE"], data["REMARKS"], nowDate, nowTime, data["USER_ID"])
	if err1 != nil {
		lg.LogInfo.Error("====error=====updateNewPassword===========stmt.Exec error==========")
		lg.LogInfo.Error("err: %s", err1.Error())

		return err1
	}
	stmt.Close()

	return nil
}
