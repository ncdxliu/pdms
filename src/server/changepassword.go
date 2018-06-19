//个人数据管理平台-用户密码修改
package server

import (
	"common"
	"dbopr"
	"encoding/json"
	"fmt"
	lg "log4go"
	"strings"
)

type TX0010Response struct {
	TxCode  string `json:"TXCODE"`
	SeqId   string `json:"SEQ_ID"`
	ErrCode string `json:"ERRCODE"`
	ErrMsg  string `json:"ERRMSG"`
}

//用户密码修改
func changePassword(data map[string]string) (string, error) {
	//lg.LogInfo.Error("======================changePassword start ...===================")

	//判断旧密码是否正确
	bl, err := oldPasswordIsTrue(data)
	if err != nil {
		lg.LogInfo.Error("=====error========changePassword=======oldPasswordIsTrue error===========")
		lg.LogInfo.Error("err: %s", err.Error())

		return "1000", err
	}

	if !bl {
		return "1008", fmt.Errorf("旧密码不正确")
	}

	//更新新密码
	err1 := updateNewPassword(data)
	if err1 != nil {
		lg.LogInfo.Error("======error========changePassword========updateNewPassword error=============")
		lg.LogInfo.Error("err: %s", err1.Error())

		return "1000", err1
	}

	//生成应答报文
	jsonData := TX0010Response{
		TxCode:  data["TXCODE"],
		SeqId:   data["SEQ_ID"],
		ErrCode: "0000",
		ErrMsg:  "修改成功",
	}

	b, err := json.Marshal(jsonData)
	if err != nil {
		lg.LogInfo.Error("===error====registered==========json.Marshal error=========")
		lg.LogInfo.Error("err: %s", err.Error())

		return "1000", err
	}

	responseStr := string(b)

	//lg.LogInfo.Error("======================changePassword end===================")

	return responseStr, nil
}

//判断旧密码是否正确
func oldPasswordIsTrue(data map[string]string) (bool, error) {

	rows, err := dbopr.Dbconn.Query("select USER_PASSWORD from TB_USER where user_id = ? and data_status != '04'", data["USER_ID"])
	if err != nil {
		lg.LogInfo.Error("===error====oldPasswordIsTrue=========Dbconn.Query error===========")
		lg.LogInfo.Error("err: %s", err.Error())

		return false, err
	}
	defer rows.Close()

	var oldPassword string
	for rows.Next() {
		err := rows.Scan(&oldPassword)
		if err != nil {
			lg.LogInfo.Error("===error======oldPasswordIsTrue========rows.Scan error===========")
			lg.LogInfo.Error("err: %s", err.Error())
		}
	}

	err = rows.Err()
	if err != nil {
		lg.LogInfo.Error("===error======singleSelect=========rows.Err error==========")
		lg.LogInfo.Error("err: %s", err.Error())
	}

	if strings.EqualFold(oldPassword, data["OLD_PASSWORD"]) {
		return true, nil
	}

	return false, nil
}

//更新密码
func updateNewPassword(data map[string]string) error {
	nowDate := common.GetNowDate()
	nowTime := common.GetNowDateTime()

	stmt, err := dbopr.Dbconn.Prepare("update TB_USER set USER_PASSWORD = ?, PASSWORD_LAST_CHANGE_DAY = ?, DATA_MOD_DATE = ?, DATA_MOD_TIME = ? where USER_ID = ? and DATA_STATUS != '04'")
	if err != nil {
		lg.LogInfo.Error("====error=====updateNewPassword=======dbopr.Dbconn.Prepare error=======")
		lg.LogInfo.Error("err: %s", err.Error())

		return err
	}

	_, err1 := stmt.Exec(data["NEW_PASSWORD"], nowDate, nowDate, nowTime, data["USER_ID"])
	if err1 != nil {
		lg.LogInfo.Error("====error=====updateNewPassword===========stmt.Exec error==========")
		lg.LogInfo.Error("err: %s", err1.Error())

		return err1
	}
	stmt.Close()

	return nil
}
