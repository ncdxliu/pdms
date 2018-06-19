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

type TX0011Response struct {
	TxCode  string `json:"TXCODE"`
	SeqId   string `json:"SEQ_ID"`
	ErrCode string `json:"ERRCODE"`
	ErrMsg  string `json:"ERRMSG"`
}

//用户密码重置
func resetPassword(reqDataMap map[string]string) (string, error) {
	//lg.LogInfo.Debug("===================resetPassword start ...=================")

	var responseStr string

	//判断用户名是否正确
	bl, err := common.JudgeUserIsExist(reqDataMap)
	if err != nil {
		lg.LogInfo.Error("====error====resetPassword=========judgeUserIsExist error=========")
		lg.LogInfo.Error("err: %s", err.Error())

		return "1000", err
	}

	if !bl {
		return "1011", fmt.Errorf("输入的用户名错")
	}

	//判断验证码是否正确
	bl1, err1 := judgeVerifCodeIsExist(reqDataMap)
	if err1 != nil {
		lg.LogInfo.Error("====error====resetPassword=========judgeVerifCodeIsExist error=========")
		lg.LogInfo.Error("err: %s", err1.Error())

		return "1000", err1
	}

	if !bl1 {
		return "1012", fmt.Errorf("验证码错误")
	}

	//重置密码
	errReset := resetPwd(reqDataMap)
	if errReset != nil {
		lg.LogInfo.Error("====error====resetPassword=========resetPwd error=========")
		lg.LogInfo.Error("err: ", errReset)

		return "1000", errReset
	}

	//生成应答报文
	jsonData := TX0011Response{
		TxCode:  "TX0011",
		SeqId:   reqDataMap["SEQ_ID"],
		ErrCode: "0000",
		ErrMsg:  "密码重置成功",
	}

	b, err := json.Marshal(jsonData)
	if err != nil {
		lg.LogInfo.Error("===error====resetPassword==========json.Marshal error=========")
		lg.LogInfo.Error("err: %s", err.Error())

		return "1000", err
	}

	responseStr = string(b)

	//lg.LogInfo.Debug("===================resetPassword end=================")

	return responseStr, nil
}

//判断用户是否存在
/*
func judgeUserIsExist(data map[string]string) (bool, error) {
	rows, err := dbopr.Dbconn.Query("select count(1) from TB_USER where (user_name = ? or user_email = ? or user_phone = ?) and data_status != '04'", data["USER_NAME"], data["USER_NAME"], data["USER_NAME"])
	if err != nil {
		lg.LogInfo.Error("===error====judgeUserIsExist=========Dbconn.Query error===========")
		lg.LogInfo.Error("err: %s", err.Error())

		return false, err
	}
	defer rows.Close()

	var strCnt string

	for rows.Next() {
		err := rows.Scan(&strCnt)
		if err != nil {
			lg.LogInfo.Error("===error======judgeUserIsExist========rows.Scan error===========")
			lg.LogInfo.Error("err: %s", err.Error())
		}

		//lg.LogInfo.Error("id: ", id, ", name: ", name, ", sex: ", sex, ", role: ", role, ", branch: ", branch)
	}

	err = rows.Err()
	if err != nil {
		lg.LogInfo.Error("===error======judgeUserIsExist=========rows.Err error==========")
		lg.LogInfo.Error("err: %s", err.Error())
	}

	lg.LogInfo.Debug("judgeUserIsExist ==> strCnt: ", strCnt)
	n, err := strconv.ParseInt(strCnt, 10, 32)
	if err != nil {
		lg.LogInfo.Error("====error======judgeUserIsExist========strconv.ParseInt error=========")
		lg.LogInfo.Error("err: %s", err.Error())

		return false, err
	}

	if n >= 1 {
		return true, nil
	}

	return false, nil
}
*/

//判断验证码是否存在
func judgeVerifCodeIsExist(data map[string]string) (bool, error) {
	rows, err := dbopr.Dbconn.Query("select count(1) from TB_USER where (user_name = ? or user_email = ? or user_phone = ?) and data_status != '04' and VERIF_CODE = ?", data["USER_NAME"], data["USER_NAME"], data["USER_NAME"], data["VERIF_CODE"])
	if err != nil {
		lg.LogInfo.Error("===error====judgeUserIsExist=========Dbconn.Query error===========")
		lg.LogInfo.Error("err: %s", err.Error())

		return false, err
	}
	defer rows.Close()

	var strCnt string

	for rows.Next() {
		err := rows.Scan(&strCnt)
		if err != nil {
			lg.LogInfo.Error("===error======judgeUserIsExist========rows.Scan error===========")
			lg.LogInfo.Error("err: %s", err.Error())
		}

		//lg.LogInfo.Error("id: ", id, ", name: ", name, ", sex: ", sex, ", role: ", role, ", branch: ", branch)
	}

	err = rows.Err()
	if err != nil {
		lg.LogInfo.Error("===error======judgeUserIsExist=========rows.Err error==========")
		lg.LogInfo.Error("err: %s", err.Error())
	}

	lg.LogInfo.Error("judgeUserIsExist ==> strCnt: ", strCnt)
	n, err := strconv.ParseInt(strCnt, 10, 32)
	if err != nil {
		lg.LogInfo.Error("====error======judgeUserIsExist========strconv.ParseInt error=========")
		lg.LogInfo.Error("err: %s", err.Error())

		return false, err
	}

	if n >= 1 {
		return true, nil
	}

	return false, nil
}

//重置密码
func resetPwd(data map[string]string) error {

	stmt, err := dbopr.Dbconn.Prepare("UPDATE TB_USER SET USER_PASSWORD = ? WHERE (USER_NAME = ? or USER_EMAIL = ? or USER_PHONE = ?) and VERIF_CODE = ?")
	if err != nil {
		lg.LogInfo.Error("====error=====resetPwd=======dbopr.Dbconn.Prepare error=======")
		lg.LogInfo.Error("err: %s", err.Error())

		return err
	}

	rs, err := stmt.Exec(data["NEW_PASSWORD"], data["USER_NAME"], data["USER_NAME"], data["USER_NAME"], data["VERIF_CODE"])
	if err != nil {
		lg.LogInfo.Error("====error=====resetPwd===========stmt.Exec error==========")
		lg.LogInfo.Error("err: %s", err.Error())

		return err
	}
	stmt.Close()

	//获得插入的id
	id, err := rs.LastInsertId()

	lg.LogInfo.Debug("======resetPwd=======saveResult id==", id)
	//获得影响行数
	affect, err := rs.RowsAffected()
	lg.LogInfo.Debug("=======resetPwd======saveResult affect==", affect)

	return nil
}
