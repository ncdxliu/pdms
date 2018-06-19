//个人数据管理系统-登录
package server

import (
	"common"
	"dbopr"
	"encoding/json"
	"fmt"
	lg "log4go"
	"strings"
)

type TX0002Response struct {
	TxCode   string `json:"TXCODE"`
	SeqId    string `json:"SEQ_ID"`
	ErrCode  string `json:"ERRCODE"`
	ErrMsg   string `json:"ERRMSG"`
	UserId   string `json:"USER_ID"`
	UserName string `json:"USER_NAME"`
}

//登录
func login(data map[string]string) (string, error) {
	var responseStr string

	//lg.LogInfo.Error("=================login start ...===================")

	//检查用户是否存在
	userId, pwd, err := loginUserIsExist(data["USER_NAME"])
	if err != nil {
		lg.LogInfo.Error("=====error======login==============loginUserIsExist==============")
		lg.LogInfo.Error("err: %s", err.Error())

		return "1001", err
	}

	if len(userId) == 0 && len(pwd) == 0 {
		return "1003", fmt.Errorf("用户不存在")
	}

	if !strings.EqualFold(data["USER_PASSWORD"], pwd) {
		return "1002", fmt.Errorf("密码不正确")
	}

	data["USER_ID"] = userId

	//记录登录日志
	go insertLoginLog(data, "00")

	//生成应答报文
	jsonData := TX0002Response{
		TxCode:   data["TXCODE"],
		SeqId:    data["SEQ_ID"],
		ErrCode:  "0000",
		ErrMsg:   "登录成功",
		UserId:   userId,
		UserName: data["USER_NAME"],
	}

	b, err := json.Marshal(jsonData)
	if err != nil {
		lg.LogInfo.Error("===error====registered==========json.Marshal error=========")
		lg.LogInfo.Error("err: %s", err.Error())

		return "1000", err
	}

	responseStr = string(b)

	//lg.LogInfo.Error("=================login end===================")

	return responseStr, nil
}

//检查用户是否存在
func loginUserIsExist(userName string) (string, string, error) {
	var pwd string
	var userId string
	//var num int = 0

	rows, err := dbopr.Dbconn.Query("select user_id, user_password from TB_USER where (user_name = ? or user_phone = ? or user_email = ?) and data_status != '04'", userName, userName, userName)
	if err != nil {
		lg.LogInfo.Error("==error=====loginUserIsExist=========Dbconn.Query error===========")
		lg.LogInfo.Error("err: %s", err.Error())

		return "", "", err
	}
	defer rows.Close()

	for rows.Next() {
		err := rows.Scan(&userId, &pwd)
		if err != nil {
			lg.LogInfo.Error("===error======loginUserIsExist========rows.Scan error===========")
			lg.LogInfo.Error("err: %s", err.Error())
		}
		//num++
		//lg.LogInfo.Error("num: %d", num)
	}
	//lg.LogInfo.Error("userId: ", userId)
	//lg.LogInfo.Error("pwd: ", pwd)

	err = rows.Err()
	if err != nil {
		lg.LogInfo.Error("=====error====loginUserIsExist=========rows.Err error==========")
		lg.LogInfo.Error("err: %s", err.Error())
	}

	return userId, pwd, nil
}

//记录登录日志
func insertLoginLog(data map[string]string, status string) {

	nowDate := common.GetNowDate()
	nowTime := common.GetNowDateTime()
	dataId := common.GetGuid()
	address := common.GetIpAddress(data["CLIENT_IP"])

	/*
		DATA_ID
		DATA_DATE
		USER_ID
		OPER
		OPER_IP
		OPER_DATE
		OPER_TIME
	*/

	stmt, err := dbopr.Dbconn.Prepare("INSERT INTO TB_LOGIN_LOG (DATA_ID, DATA_DATE, USER_ID, OPER, OPER_IP, OPER_DATE, OPER_TIME, REMARK) values (?,?,?,?,?,?,?,?)")
	if err != nil {
		lg.LogInfo.Error("====error=====insertLoginLog=======dbopr.Dbconn.Prepare error=======")
		lg.LogInfo.Error("err: %s", err.Error())

		return
	}

	_, err1 := stmt.Exec(dataId, nowDate, data["USER_ID"], status, data["CLIENT_IP"], nowDate, nowTime, address)
	if err1 != nil {
		lg.LogInfo.Error("====error=====insertLoginLog===========stmt.Exec error==========")
		lg.LogInfo.Error("err: %s", err1.Error())

		return
	}
	stmt.Close()

	//	//获得插入的id
	//	id, err := rs.LastInsertId()

	//	lg.LogInfo.Error("======insertLoginLog=======saveResult id==", id)
	//	//获得影响行数
	//	affect, err := rs.RowsAffected()
	//	lg.LogInfo.Error("=======insertLoginLog======saveResult affect==", affect)
}
