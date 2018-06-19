//个人数据管理系统-获取验证码
package server

import (
	"common"
	"dbopr"
	"encoding/json"
	"fmt"
	lg "log4go"
)

type TX0014Response struct {
	TxCode  string `json:"TXCODE"`
	SeqId   string `json:"SEQ_ID"`
	ErrCode string `json:"ERRCODE"`
	ErrMsg  string `json:"ERRMSG"`
}

//用户获取验证码
func getVerifCode(reqDataMap map[string]string) (string, error) {
	//lg.LogInfo.Error("===================getVerifCode start ...=================")

	var responseStr string

	//判断用户名是否正确
	bl, err := common.JudgeUserIsExist(reqDataMap)
	if err != nil {
		lg.LogInfo.Error("====error====getVerifCode=========JudgeUserIsExist error=========")
		lg.LogInfo.Error("err: %s", err.Error())

		return "1000", err
	}

	if !bl {
		return "1013", fmt.Errorf("不存在该用户，请重新输入用户名！")
	}

	//获取用户邮箱
	email, err := common.GetEmail(reqDataMap)
	if err != nil {
		lg.LogInfo.Error("====error====getVerifCode=========GetEmail error=========")
		lg.LogInfo.Error("err: %s", err.Error())

		return "1000", err
	}

	//解密配置的Email密码
	emailPwd, err := common.DataDecrypt(common.EmailMap["EmailPassword"])
	if err != nil {
		lg.LogInfo.Error("====error======getVerifCode========DataDecrypt error============")
		lg.LogInfo.Error("err: %s", err.Error())

		return "1000", err
	}

	//获取6位随机数
	randString := common.GetRandString()

	mailContent := "你正在对云脑数据账户进行密码重置，请输入以下验证码进行重置：\n" + randString + "\n\n如有疑问，可以直接在该邮件中进行回复。"

	//获取邮箱结构体
	u := common.NewEmailUser(common.EmailMap["EmailUser"], emailPwd, common.EmailMap["EmailHost"], email)

	//mailType表示邮件格式是普通文件还是html或其他
	u.Subject = "云脑数据--密码重置验证码"
	u.MailType = "plain"
	u.Body = mailContent

	/*lg.LogInfo.Error(u.Host)
	lg.LogInfo.Error(u.User)
	lg.LogInfo.Error(u.Password)
	lg.LogInfo.Error(u.To)
	lg.LogInfo.Error(u.MailType)
	lg.LogInfo.Error(u.Subject)
	lg.LogInfo.Error(u.Body)*/
	//发送验证码到用户邮箱
	emailSendErr := common.SendToMail(u.Host, u.User, u.Password, u.To, u.MailType, u.Subject, u.Body)
	if emailSendErr != nil {
		lg.LogInfo.Error("====error=======getVerifCode=========SendToMail error==========")
		lg.LogInfo.Error("err: ", emailSendErr)

		return "1000", emailSendErr
	}

	reqDataMap["VERIF_CODE"] = randString

	//记录验证码
	errReset := setVerifCode(reqDataMap)
	if errReset != nil {
		lg.LogInfo.Error("====error====resetPassword=========resetPwd error=========")
		lg.LogInfo.Error("err: ", errReset)

		return "1000", errReset
	}

	//生成应答报文
	jsonData := TX0014Response{
		TxCode:  "TX0014",
		SeqId:   reqDataMap["SEQ_ID"],
		ErrCode: "0000",
		ErrMsg:  "验证码已发送到你的邮箱，请输入邮箱中的验证码！",
	}

	b, err := json.Marshal(jsonData)
	if err != nil {
		lg.LogInfo.Error("===error====resetPassword==========json.Marshal error=========")
		lg.LogInfo.Error("err: %s", err.Error())

		return "1000", err
	}

	responseStr = string(b)

	//lg.LogInfo.Error("===================getVerifCode end=================")

	return responseStr, nil
}

//设置验证码
func setVerifCode(data map[string]string) error {

	stmt, err := dbopr.Dbconn.Prepare("UPDATE TB_USER SET VERIF_CODE = ? WHERE (USER_NAME = ? or USER_EMAIL = ? or USER_PHONE = ?) and DATA_STATUS != '04'")
	if err != nil {
		lg.LogInfo.Error("====error=====setVerifCode=======dbopr.Dbconn.Prepare error=======")
		lg.LogInfo.Error("err: %s", err.Error())

		return err
	}

	rs, err := stmt.Exec(data["VERIF_CODE"], data["USER_NAME"], data["USER_NAME"], data["USER_NAME"])
	if err != nil {
		lg.LogInfo.Error("====error=====setVerifCode===========stmt.Exec error==========")
		lg.LogInfo.Error("err: %s", err.Error())

		return err
	}
	stmt.Close()

	//获得插入的id
	id, err := rs.LastInsertId()

	lg.LogInfo.Error("======setVerifCode=======saveResult id==", id)
	//获得影响行数
	affect, err := rs.RowsAffected()
	lg.LogInfo.Error("=======setVerifCode======saveResult affect==", affect)

	return nil
}
