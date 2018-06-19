//个人数据管理系统-联系我们
package server

import (
	"common"
	"dbopr"
	"encoding/json"
	lg "log4go"
)

type TX0016Response struct {
	TxCode  string `json:"TXCODE"`
	SeqId   string `json:"SEQ_ID"`
	ErrCode string `json:"ERRCODE"`
	ErrMsg  string `json:"ERRMSG"`
}

//联系我们
func contactUs(data map[string]string) (string, error) {
	//lg.LogInfo.Error("======================contactUs start ...===================")

	//记录反馈意见
	err1 := contactUsRecording(data)
	if err1 != nil {
		lg.LogInfo.Error("======error========contactUs========cancellationUserData error=============")
		lg.LogInfo.Error("err: %s", err1.Error())

		return "1000", err1
	}

	//生成应答报文
	jsonData := TX0016Response{
		TxCode:  data["TXCODE"],
		SeqId:   data["SEQ_ID"],
		ErrCode: "0000",
		ErrMsg:  "我们已收到您的意见，请耐心等待我们的邮件回复！",
	}

	b, err := json.Marshal(jsonData)
	if err != nil {
		lg.LogInfo.Error("===error====contactUs==========json.Marshal error=========")
		lg.LogInfo.Error("err: %s", err.Error())

		return "1000", err
	}

	responseStr := string(b)

	//lg.LogInfo.Error("======================contactUs end===================")

	return responseStr, nil
}

//记录反馈意见
func contactUsRecording(data map[string]string) error {
	dataId := common.GetGuid()
	nowDate := common.GetNowDate()

	stmt, err := dbopr.Dbconn.Prepare("insert into TB_CONTACT(DATA_ID, DATA_DATE, USER_ID, TITLE, CONTACT) values(?, ?, ?, ?, ?)")
	if err != nil {
		lg.LogInfo.Error("====error=====updateNewPassword=======dbopr.Dbconn.Prepare error=======")
		lg.LogInfo.Error("err: %s", err.Error())

		return err
	}

	_, err1 := stmt.Exec(dataId, nowDate, data["USER_ID"], data["TITLE"], data["CONTACT"])
	if err1 != nil {
		lg.LogInfo.Error("====error=====updateNewPassword===========stmt.Exec error==========")
		lg.LogInfo.Error("err: %s", err1.Error())

		return err1
	}
	stmt.Close()

	return nil
}
