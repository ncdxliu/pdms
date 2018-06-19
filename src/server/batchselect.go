//个人数据管理系统-批量查询
package server

import (
	"dbopr"
	"encoding/json"
	lg "log4go"
)

type TX0004Response struct {
	TxCode  string                 `json:"TXCODE"`
	SeqId   string                 `json:"SEQ_ID"`
	ErrCode string                 `json:"ERRCODE"`
	ErrMsg  string                 `json:"ERRMSG"`
	Count   int                    `json:"COUNT"`
	Detail  []TX0004DetailResponse `json:"DATA"`
}

type TX0004DetailResponse struct {
	DataId string `json:"DATA_ID"`
	Title  string `json:"TITLE"`
}

//数据批量查询
func dataBatchSelect(data map[string]string) (string, error) {
	//lg.LogInfo.Error("===================dataBatchSelect start ...====================")
	var responseStr string

	batchDetailArray, err := detailBSelect(data)
	if err != nil {
		lg.LogInfo.Error("=========error=====dataBatchSelect=======detailBSelect error===============")
		lg.LogInfo.Error("err: %s", err.Error())

		return "1000", err
	}

	jsonData := new(TX0004Response)

	jsonData.TxCode = data["TXCODE"]
	jsonData.SeqId = data["SEQ_ID"]
	jsonData.ErrCode = "0000"
	jsonData.ErrMsg = "查询成功"
	jsonData.Count = len(batchDetailArray)
	jsonData.Detail = batchDetailArray

	b, err := json.Marshal(jsonData)
	if err != nil {
		lg.LogInfo.Error("============error====dataBatchSelect========json.Marshal(jsonData) error============")
		lg.LogInfo.Error("err: %s", err.Error())

		return "1000", err
	}

	responseStr = string(b)

	//lg.LogInfo.Error("======================dataBatchSelect end========================")

	return responseStr, nil
}

//批量查询
func detailBSelect(data map[string]string) ([]TX0004DetailResponse, error) {
	//lg.LogInfo.Error(data["USER_ID"], " - ", data["DATA_TYPE"])

	rows, err := dbopr.Dbconn.Query("select data_id, title from TB_CONTENT where user_id = ? and data_type = ? and status = '00' order by add_time desc", data["USER_ID"], data["DATA_TYPE"])
	if err != nil {
		lg.LogInfo.Error("===error====detailBSelect=========Dbconn.Query error===========")
		lg.LogInfo.Error("err: %s", err.Error())

		return nil, err
	}
	defer rows.Close()

	var dataId string
	var title string

	detailArray := make([]TX0004DetailResponse, 0)

	for rows.Next() {
		err := rows.Scan(&dataId, &title)
		if err != nil {
			lg.LogInfo.Error("===error======detailBSelect========rows.Scan error===========")
			lg.LogInfo.Error("err: %s", err.Error())
		}

		//lg.LogInfo.Error("dataId: ", dataId)
		//lg.LogInfo.Error("title: ", title)
		//lg.LogInfo.Error("======================")
		detailData := TX0004DetailResponse{dataId, title}

		detailArray = append(detailArray, detailData)
	}

	err = rows.Err()
	if err != nil {
		lg.LogInfo.Error("===error======detailBSelect=========rows.Err error==========")
		lg.LogInfo.Error("err: %s", err.Error())
	}

	return detailArray, nil
}
