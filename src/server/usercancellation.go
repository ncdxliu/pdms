//个人数据管理系统-用户注销
package server

import (
	"common"
	"dbopr"
	"encoding/json"
	lg "log4go"
)

type TX0013Response struct {
	TxCode  string `json:"TXCODE"`
	SeqId   string `json:"SEQ_ID"`
	ErrCode string `json:"ERRCODE"`
	ErrMsg  string `json:"ERRMSG"`
}

//用户注销
func cancellationUser(data map[string]string) (string, error) {
	//lg.LogInfo.Error("======================cancellationUser start ...===================")

	//更新新密码
	err1 := cancellationUserData(data)
	if err1 != nil {
		lg.LogInfo.Error("======error========cancellationUser========cancellationUserData error=============")
		lg.LogInfo.Error("err: %s", err1.Error())

		return "1000", err1
	}

	//生成应答报文
	jsonData := TX0013Response{
		TxCode:  data["TXCODE"],
		SeqId:   data["SEQ_ID"],
		ErrCode: "0000",
		ErrMsg:  "注销成功",
	}

	b, err := json.Marshal(jsonData)
	if err != nil {
		lg.LogInfo.Error("===error====cancellationUser==========json.Marshal error=========")
		lg.LogInfo.Error("err: %s", err.Error())

		return "1000", err
	}

	responseStr := string(b)

	//lg.LogInfo.Error("======================cancellationUser end===================")

	return responseStr, nil
}

//删除数据库数据
func cancellationUserData(data map[string]string) error {
	nowDate := common.GetNowDate()
	nowTime := common.GetNowDateTime()

	stmt, err := dbopr.Dbconn.Prepare("update TB_USER set data_status = '04', REMARKS = ?, DATA_MOD_DATE = ?, DATA_MOD_TIME = ? where USER_ID = ?")
	if err != nil {
		lg.LogInfo.Error("====error=====updateNewPassword=======dbopr.Dbconn.Prepare error=======")
		lg.LogInfo.Error("err: %s", err.Error())

		return err
	}

	_, err1 := stmt.Exec(data["REMARK"], nowDate, nowTime, data["USER_ID"])
	if err1 != nil {
		lg.LogInfo.Error("====error=====updateNewPassword===========stmt.Exec error==========")
		lg.LogInfo.Error("err: %s", err1.Error())

		return err1
	}
	stmt.Close()

	stmt, err2 := dbopr.Dbconn.Prepare("update TB_CONTENT set status = '01' where USER_ID = ?")
	if err2 != nil {
		lg.LogInfo.Error("====error=====updateNewPassword=======dbopr.Dbconn.Prepare error=======")
		lg.LogInfo.Error("err: %s", err2.Error())

		return err2
	}

	_, err3 := stmt.Exec(data["USER_ID"])
	if err3 != nil {
		lg.LogInfo.Error("====error=====updateNewPassword===========stmt.Exec error==========")
		lg.LogInfo.Error("err: %s", err3.Error())

		return err3
	}
	stmt.Close()

	return nil
}
