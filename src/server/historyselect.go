//个人数据管理系统-登录日志查询
package server

import (
	"dbopr"
	"encoding/json"
	lg "log4go"
)

type TX0017Response struct {
	TxCode  string                 `json:"TXCODE"`
	SeqId   string                 `json:"SEQ_ID"`
	ErrCode string                 `json:"ERRCODE"`
	ErrMsg  string                 `json:"ERRMSG"`
	Count   int                    `json:"COUNT"`
	Detail  []TX0017DetailResponse `json:"DATA"`
}

type TX0017DetailResponse struct {
	Oper        string `json:"OPER"`
	OperIp      string `json:"OPER_IP"`
	OperDate    string `json:"OPER_DATE"`
	OperTime    string `json:"OPER_TIME"`
	OperAddress string `json:"OPER_ADDRESS`
}

//登录日志查询
func historySelect(data map[string]string) (string, error) {
	//lg.LogInfo.Error("===================historySelect start ...====================")
	var responseStr string

	batchDetailArray, err := hdetailBSelect(data)
	if err != nil {
		lg.LogInfo.Error("=========error=====historySelect=======hdetailBSelect error===============")
		lg.LogInfo.Error("err: %s", err.Error())

		return "1000", err
	}

	jsonData := new(TX0017Response)

	jsonData.TxCode = data["TXCODE"]
	jsonData.SeqId = data["SEQ_ID"]
	jsonData.ErrCode = "0000"
	jsonData.ErrMsg = "查询成功"
	jsonData.Count = len(batchDetailArray)
	jsonData.Detail = batchDetailArray

	b, err := json.Marshal(jsonData)
	if err != nil {
		lg.LogInfo.Error("============error====historySelect========json.Marshal(jsonData) error============")
		lg.LogInfo.Error("err: %s", err.Error())

		return "1000", err
	}

	responseStr = string(b)

	//lg.LogInfo.Error("======================historySelect end========================")

	return responseStr, nil
}

//登录日志批量查询
func hdetailBSelect(data map[string]string) ([]TX0017DetailResponse, error) {

	rows, err := dbopr.Dbconn.Query("select OPER, OPER_IP, OPER_DATE, OPER_TIME, REMARK from TB_LOGIN_LOG where user_id = ? order by OPER_TIME desc", data["USER_ID"])
	if err != nil {
		lg.LogInfo.Error("===error====hdetailBSelect=========Dbconn.Query error===========")
		lg.LogInfo.Error("err: %s", err.Error())

		return nil, err
	}
	defer rows.Close()

	var oper string
	var operIp string
	var operDate string
	var operTime string
	var address *string
	var addr string

	detailArray := make([]TX0017DetailResponse, 0)

	for rows.Next() {
		err := rows.Scan(&oper, &operIp, &operDate, &operTime, &address)
		if err != nil {
			lg.LogInfo.Error("===error======hdetailBSelect========rows.Scan error===========")
			lg.LogInfo.Error("err: %s", err.Error())
		}

		if address != nil {
			addr = *address
		} else {
			addr = ""
		}

		detailData := TX0017DetailResponse{oper, operIp, operDate, operTime, addr}

		detailArray = append(detailArray, detailData)
	}

	err = rows.Err()
	if err != nil {
		lg.LogInfo.Error("===error======hdetailBSelect=========rows.Err error==========")
		lg.LogInfo.Error("err: %s", err.Error())
	}

	return detailArray, nil
}
