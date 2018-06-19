//个人数据管理系统-数据搜索
package server

import (
	"common"
	"dbopr"
	"encoding/json"
	"fmt"
	lg "log4go"
	"strings"
)

type TX0009Response struct {
	TxCode  string                 `json:"TXCODE"`
	SeqId   string                 `json:"SEQ_ID"`
	ErrCode string                 `json:"ERRCODE"`
	ErrMsg  string                 `json:"ERRMSG"`
	Count   int                    `json:"COUNT"`
	Detail  []TX0009DetailResponse `json:"DATA"`
}

type TX0009DetailResponse struct {
	DataId string `json:"DATA_ID"`
	Title  string `json:"TITLE"`
}

//数据搜索
func dataSearch(data map[string]string) (string, error) {
	//lg.LogInfo.Error("===========dataSearch start ...=============")

	var responseStr string

	searchDetailArray, err := detailDataSearch(data)
	if err != nil {
		lg.LogInfo.Error("=========error=====dataSearch=======importantDataSearch error===============")
		lg.LogInfo.Error("err: %s", err.Error())

		return "1000", err
	}

	jsonData := new(TX0009Response)

	jsonData.TxCode = data["TXCODE"]
	jsonData.SeqId = data["SEQ_ID"]
	jsonData.ErrCode = "0000"
	jsonData.ErrMsg = "搜索成功"
	jsonData.Count = len(searchDetailArray)
	jsonData.Detail = searchDetailArray

	b, err := json.Marshal(jsonData)
	if err != nil {
		lg.LogInfo.Error("============error====dataBatchSelect========json.Marshal(jsonData) error============")
		lg.LogInfo.Error("err: %s", err.Error())

		return "1000", err
	}

	responseStr = string(b)

	//lg.LogInfo.Error("============dataSearch end==============")

	return responseStr, nil
}

//数据搜索
func detailDataSearch(data map[string]string) ([]TX0009DetailResponse, error) {
	rows, err := dbopr.Dbconn.Query("select data_id, data_type, title, content1 from TB_CONTENT where user_id = ? and status = '00'", data["USER_ID"])
	if err != nil {
		lg.LogInfo.Error("===error====detailBSelect=========Dbconn.Query error===========")
		lg.LogInfo.Error("err: %s", err.Error())

		return nil, err
	}
	defer rows.Close()

	var dataId string
	var dataType string
	var title string
	var content string
	var errDecrypt error
	var searchStr string = data["SEARCH"]

	detailArray := make([]TX0009DetailResponse, 0)

	//输入的搜索内容转换为大写
	searchStr = strings.ToUpper(searchStr)

	for rows.Next() {
		err := rows.Scan(&dataId, &dataType, &title, &content)
		if err != nil {
			lg.LogInfo.Error("===error======detailBSelect========rows.Scan error===========")
			lg.LogInfo.Error("err: %s", err.Error())
		}

		if strings.EqualFold(data["SELFLAG"], "0") {
			//标题转换为大写再比较
			title1 := title
			title = strings.ToUpper(title)

			if strings.Contains(title, searchStr) {
				detailData := TX0009DetailResponse{dataId, title1}

				detailArray = append(detailArray, detailData)
			}
		} else {
			//数据要解密
			content, errDecrypt = common.DataDecrypt(content)
			if errDecrypt != nil {
				lg.LogInfo.Error("=======error======singleSelect====common.DataDecrypt error==========")
				lg.LogInfo.Error("err: %s", err.Error())

				return nil, fmt.Errorf("数据解密失败")
			}

			//标题和内容转换为大写再比较
			title1 := title
			title = strings.ToUpper(title)
			content = strings.ToUpper(content)

			if strings.Contains(title, searchStr) || strings.Contains(content, searchStr) {
				detailData := TX0009DetailResponse{dataId, title1}

				detailArray = append(detailArray, detailData)
			}
		}

	}

	err = rows.Err()
	if err != nil {
		lg.LogInfo.Error("===error======detailBSelect=========rows.Err error==========")
		lg.LogInfo.Error("err: %s", err.Error())
	}

	return detailArray, nil
}
