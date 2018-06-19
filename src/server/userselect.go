//个人数据管理系统-单笔查询
package server

import (
	"dbopr"
	"encoding/json"
	"fmt"
	lg "log4go"
)

type TX0015Response struct {
	TxCode                string `json:"TXCODE"`
	SeqId                 string `json:"SEQ_ID"`
	ErrCode               string `json:"ERRCODE"`
	ErrMsg                string `json:"ERRMSG"`
	UserName              string `json:"USER_NAME"`
	UserEmail             string `json:"USER_EMAIL"`
	UserPhone             string `json:"USER_PHONE"`
	RegistedTime          string `json:"REGISTERED_TIME"`
	Remarks               string `json:"REMARKS"`
	PasswordLastChangeDay string `json:"PASSWORD_LAST_CHANGE_DAY"`
	DataStatus            string `json:"DATA_STATUS"`
}

//用户资料查询
func userSelect(data map[string]string) (string, error) {
	//lg.LogInfo.Error("=======================userSelect start...======================")

	var responseStr string

	var userName string
	var userEmail string
	var userPhone string
	var registedTime string
	var remarks string
	var passwordLastChangeDay string
	var dataStatus string

	rows, err := dbopr.Dbconn.Query("select USER_NAME, USER_EMAIL, USER_PHONE, REGISTERED_TIME, REMARKS, PASSWORD_LAST_CHANGE_DAY, DATA_STATUS from TB_USER where user_id = ? and data_status != '04'", data["USER_ID"])
	if err != nil {
		lg.LogInfo.Error("===error====userSelect=========Dbconn.Query error===========")
		lg.LogInfo.Error("err: %s", err.Error())

		return "1000", err
	}
	defer rows.Close()

	for rows.Next() {
		err := rows.Scan(&userName, &userEmail, &userPhone, &registedTime, &remarks, &passwordLastChangeDay, &dataStatus)
		if err != nil {
			lg.LogInfo.Error("===error======userSelect========rows.Scan error===========")
			lg.LogInfo.Error("err: %s", err.Error())
		}
	}

	//判断数据是否存在
	if len(userName) == 0 && len(userEmail) == 0 {
		return "1003", fmt.Errorf("用户不存在")
	}

	err = rows.Err()
	if err != nil {
		lg.LogInfo.Error("===error======userSelect=========rows.Err error==========")
		lg.LogInfo.Error("err: %s", err.Error())
	}

	jsonData := TX0015Response{
		TxCode:                data["TXCODE"],
		SeqId:                 data["SEQ_ID"],
		ErrCode:               "0000",
		ErrMsg:                "查询成功",
		UserName:              userName,
		UserEmail:             userEmail,
		UserPhone:             userPhone,
		RegistedTime:          registedTime,
		Remarks:               remarks,
		PasswordLastChangeDay: passwordLastChangeDay,
		DataStatus:            dataStatus,
	}

	b, err := json.Marshal(jsonData)
	if err != nil {
		lg.LogInfo.Error("===error====userSelect==========json.Marshal error=========")
		lg.LogInfo.Error("err: %s", err.Error())

		return "1000", err
	}

	responseStr = string(b)

	//lg.LogInfo.Error("========================userSelect end======================")

	return responseStr, nil
}
