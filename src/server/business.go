//个人数据管理系统服务端业务处理程序
package server

import (
	"encoding/json"
	lg "log4go"
	"strings"
)

type GeneralResponse struct {
	TxCode  string `json:"TXCODE"`
	SeqId   string `json:"SEQ_ID"`
	ErrCode string `json:"ERRCODE"`
	ErrMsg  string `json:"ERRMSG"`
}

//业务处理
func BusinessHandler(reqDataMap map[string]string) (string, error) {
	//lg.LogInfo.Error("=================BusinessHandler start ...====================")

	var responseStr string
	var err error

	//交易代码
	txcode := reqDataMap["TXCODE"]

	//glog.Info("txcode: ", txcode)
	//lg.LogInfo.Error("txcode: ", txcode)

	//交易处理
	if strings.EqualFold(txcode, "TX0001") { //用户注册
		responseStr, err = registered(reqDataMap)
	} else if strings.EqualFold(txcode, "TX0002") { //用户登录
		responseStr, err = login(reqDataMap)
	} else if strings.EqualFold(txcode, "TX0003") { //用户退出
		responseStr, err = logout(reqDataMap)
	} else if strings.EqualFold(txcode, "TX0004") { //数据批量查询
		responseStr, err = dataBatchSelect(reqDataMap)
	} else if strings.EqualFold(txcode, "TX0005") { //数据单笔查询
		responseStr, err = singleSelect(reqDataMap)
	} else if strings.EqualFold(txcode, "TX0006") { //数据新增
		responseStr, err = dataAdd(reqDataMap)
	} else if strings.EqualFold(txcode, "TX0007") { //数据修改
		responseStr, err = dataModify(reqDataMap)
	} else if strings.EqualFold(txcode, "TX0008") { //数据删除
		responseStr, err = dataDelete(reqDataMap)
	} else if strings.EqualFold(txcode, "TX0009") { //数据搜索
		responseStr, err = dataSearch(reqDataMap)
	} else if strings.EqualFold(txcode, "TX0010") { //用户密码修改
		responseStr, err = changePassword(reqDataMap)
	} else if strings.EqualFold(txcode, "TX0011") { //用户密码重置
		responseStr, err = resetPassword(reqDataMap)
	} else if strings.EqualFold(txcode, "TX0012") { //用户资料修改
		responseStr, err = updateUser(reqDataMap)
	} else if strings.EqualFold(txcode, "TX0013") { //用户注销
		responseStr, err = cancellationUser(reqDataMap)
	} else if strings.EqualFold(txcode, "TX0014") { //获取验证码
		responseStr, err = getVerifCode(reqDataMap)
	} else if strings.EqualFold(txcode, "TX0015") { //用户资料查询
		responseStr, err = userSelect(reqDataMap)
	} else if strings.EqualFold(txcode, "TX0016") { //联系我们
		responseStr, err = contactUs(reqDataMap)
	} else if strings.EqualFold(txcode, "TX0017") { //登录日志查询
		responseStr, err = historySelect(reqDataMap)
	}

	//通用错误处理
	if err != nil {
		jsonData := GeneralResponse{
			TxCode:  reqDataMap["TXCODE"],
			SeqId:   reqDataMap["SEQ_ID"],
			ErrCode: responseStr,
			ErrMsg:  err.Error(),
		}

		b, err := json.Marshal(jsonData)
		if err != nil {
			lg.LogInfo.Error("===error====BusinessHandler==========json.Marshal error=========")
			lg.LogInfo.Error("err: %s", err.Error())

			return "1000", err
		}

		responseStr = string(b)
	}

	//lg.LogInfo.Error("responseStr: ", responseStr)

	return responseStr, nil
}
