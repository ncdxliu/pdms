//个人数据管理系统-用户注册
package server

import (
	"common"
	"dbopr"
	"encoding/json"
	"fmt"
	lg "log4go"
	"strconv"
)

type TX0001Response struct {
	TxCode   string `json:"TXCODE"`
	SeqId    string `json:"SEQ_ID"`
	ErrCode  string `json:"ERRCODE"`
	ErrMsg   string `json:"ERRMSG"`
	UserId   string `json:"USER_ID"`
	UserName string `json:"USER_NAME"`
}

//用户注册
func registered(reqDataMap map[string]string) (string, error) {
	//lg.LogInfo.Error("===================registered start ...=================")

	var responseStr string

	//判断用户名是否已注册
	bl, err := userIsExist(reqDataMap)
	if err != nil {
		lg.LogInfo.Error("====error====registered=========userIsExist error=========")
		lg.LogInfo.Error("err: %s", err.Error())

		return "1000", err
	}

	if bl {
		return "1001", fmt.Errorf("该用户名已注册")
	}

	//判断邮箱是否已注册
	bl1, err1 := emailIsExist(reqDataMap)
	if err1 != nil {
		lg.LogInfo.Error("====error====registered=========emailIsExist error=========")
		lg.LogInfo.Error("err: %s", err1.Error())

		return "1000", err1
	}

	if bl1 {
		return "1009", fmt.Errorf("该邮箱已注册")
	}

	//判断手机号是否已注册
	if len(reqDataMap["USER_PHONE"]) > 0 {
		bl2, err2 := phoneIsExist(reqDataMap)
		if err2 != nil {
			lg.LogInfo.Error("====error====registered=========phoneIsExist error=========")
			lg.LogInfo.Error("err: ", err2)

			return "1000", err2
		}

		if bl2 {
			return "1010", fmt.Errorf("该手机号已注册")
		}
	}

	//生成USER_ID
	userId := common.GetGuid()

	reqDataMap["USER_ID"] = userId

	//登记到用户表
	errAdd := userAdd(reqDataMap)
	if errAdd != nil {
		lg.LogInfo.Error("====error====registered=========userAdd error=========")
		lg.LogInfo.Error("err: ", errAdd)

		return "1000", errAdd
	}

	//生成应答报文
	jsonData := TX0001Response{
		TxCode:   "TX0001",
		SeqId:    reqDataMap["SEQ_ID"],
		ErrCode:  "0000",
		ErrMsg:   "注册成功",
		UserId:   userId,
		UserName: reqDataMap["USER_NAME"],
	}

	b, err := json.Marshal(jsonData)
	if err != nil {
		lg.LogInfo.Error("===error====registered==========json.Marshal error=========")
		lg.LogInfo.Error("err: %s", err.Error())

		return "1000", err
	}

	responseStr = string(b)

	//lg.LogInfo.Debug("===================registered end=================")

	return responseStr, nil
}

//判断用户是否已存在-用户名
func userIsExist(data map[string]string) (bool, error) {
	rows, err := dbopr.Dbconn.Query("select count(1) from TB_USER where user_name = ? and data_status != '04'", data["USER_NAME"])
	if err != nil {
		lg.LogInfo.Error("===error====userIsExist=========Dbconn.Query error===========")
		lg.LogInfo.Error("err: %s", err.Error())

		return false, err
	}
	defer rows.Close()

	var strCnt string

	for rows.Next() {
		err := rows.Scan(&strCnt)
		if err != nil {
			lg.LogInfo.Error("===error======userIsExist========rows.Scan error===========")
			lg.LogInfo.Error("err: %s", err.Error())
		}

		//lg.LogInfo.Debug("id: ", id, ", name: ", name, ", sex: ", sex, ", role: ", role, ", branch: ", branch)
	}

	err = rows.Err()
	if err != nil {
		lg.LogInfo.Error("===error======userIsExist=========rows.Err error==========")
		lg.LogInfo.Error("err: %s", err.Error())
	}

	lg.LogInfo.Debug("userIsExist ==> strCnt: ", strCnt)
	n, err := strconv.ParseInt(strCnt, 10, 32)
	if err != nil {
		lg.LogInfo.Error("====error======userIsExist========strconv.ParseInt error=========")
		lg.LogInfo.Error("err: %s", err.Error())

		return false, err
	}

	if n >= 1 {
		return true, nil
	}

	return false, nil
}

//判断用户是否已存在-邮箱
func emailIsExist(data map[string]string) (bool, error) {
	rows, err := dbopr.Dbconn.Query("select count(1) from TB_USER where USER_EMAIL = ? and data_status != '04'", data["USER_EMAIL"])
	if err != nil {
		lg.LogInfo.Error("===error====userIsExist=========Dbconn.Query error===========")
		lg.LogInfo.Error("err: %s", err.Error())

		return false, err
	}
	defer rows.Close()

	var strCnt string

	for rows.Next() {
		err := rows.Scan(&strCnt)
		if err != nil {
			lg.LogInfo.Error("===error======userIsExist========rows.Scan error===========")
			lg.LogInfo.Error("err: %s", err.Error())
		}

		//lg.LogInfo.Error("id: ", id, ", name: ", name, ", sex: ", sex, ", role: ", role, ", branch: ", branch)
	}

	err = rows.Err()
	if err != nil {
		lg.LogInfo.Error("===error======userIsExist=========rows.Err error==========")
		lg.LogInfo.Error("err: %s", err.Error())
	}

	lg.LogInfo.Debug("userIsExist ==> strCnt: ", strCnt)
	n, err := strconv.ParseInt(strCnt, 10, 32)
	if err != nil {
		lg.LogInfo.Error("====error======userIsExist========strconv.ParseInt error=========")
		lg.LogInfo.Error("err: %s", err.Error())

		return false, err
	}

	if n >= 1 {
		return true, nil
	}

	return false, nil
}

//判断用户是否已存在-手机
func phoneIsExist(data map[string]string) (bool, error) {
	rows, err := dbopr.Dbconn.Query("select count(1) from TB_USER where USER_PHONE = ? and data_status != '04'", data["USER_PHONE"])
	if err != nil {
		lg.LogInfo.Error("===error====userIsExist=========Dbconn.Query error===========")
		lg.LogInfo.Error("err: %s", err.Error())

		return false, err
	}
	defer rows.Close()

	var strCnt string

	for rows.Next() {
		err := rows.Scan(&strCnt)
		if err != nil {
			lg.LogInfo.Error("===error======userIsExist========rows.Scan error===========")
			lg.LogInfo.Error("err: %s", err.Error())
		}

		//lg.LogInfo.Error("id: ", id, ", name: ", name, ", sex: ", sex, ", role: ", role, ", branch: ", branch)
	}

	err = rows.Err()
	if err != nil {
		lg.LogInfo.Error("===error======userIsExist=========rows.Err error==========")
		lg.LogInfo.Error("err: %s", err.Error())
	}

	lg.LogInfo.Debug("userIsExist ==> strCnt: ", strCnt)
	n, err := strconv.ParseInt(strCnt, 10, 32)
	if err != nil {
		lg.LogInfo.Error("====error======userIsExist========strconv.ParseInt error=========")
		lg.LogInfo.Error("err: %s", err.Error())

		return false, err
	}

	if n >= 1 {
		return true, nil
	}

	return false, nil
}

//登记到用户表
func userAdd(data map[string]string) error {

	nowDate := common.GetNowDate()
	nowTime := common.GetNowDateTime()

	stmt, err := dbopr.Dbconn.Prepare("INSERT INTO TB_USER (USER_ID, USER_NAME, USER_PASSWORD, USER_EMAIL, USER_PHONE, REGISTERED_TIME, REMARKS, PASSWORD_LAST_CHANGE_DAY, DATA_STATUS, DATA_ADD_DATE, DATA_ADD_TIME, DATA_MOD_DATE, DATA_MOD_TIME) values (?,?,?,?,?,?,?,?,?,?,?,?,?)")
	if err != nil {
		lg.LogInfo.Error("====error=====userAdd=======dbopr.Dbconn.Prepare error=======")
		lg.LogInfo.Error("err: %s", err.Error())

		return err
	}

	rs, err := stmt.Exec(data["USER_ID"], data["USER_NAME"], data["USER_PASSWORD"], data["USER_EMAIL"], data["USER_PHONE"], nowTime, data["REMARKS"], nowDate, "00", nowDate, nowTime, nowDate, nowTime)
	if err != nil {
		lg.LogInfo.Error("====error=====userAdd===========stmt.Exec error==========")
		lg.LogInfo.Error("err: %s", err.Error())

		return err
	}
	stmt.Close()

	//获得插入的id
	id, err := rs.LastInsertId()

	lg.LogInfo.Debug("======userAdd=======saveResult id==", id)
	//获得影响行数
	affect, err := rs.RowsAffected()
	lg.LogInfo.Debug("=======userAdd======saveResult affect==", affect)

	return nil
}
