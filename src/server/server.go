//个人数据管理系统服务端程序
package server

import (
	"common"
	"encoding/json"
	"fmt"
	"io/ioutil"
	lg "log4go"
	"net/http"
	"parse"
	"strings"
)

//读取请求报文
func ReadMessage(r *http.Request, w http.ResponseWriter) (map[string]string, error) {
	//lg.LogInfo.Error("================ReadMessage start ...================")
	//	pathInfo := strings.Trim(r.URL.Path, "/")
	//	parts := strings.Split(pathInfo, "/")

	//	lg.LogInfo.Error("pathInfo: ", pathInfo)
	//	lg.LogInfo.Error("r.URL: ", r.URL)

	//	for k, v := range parts {
	//		lg.LogInfo.Error(k, " - ", v)
	//	}

	//读取请求报文
	body, _ := ioutil.ReadAll(r.Body)
	r.Body.Close()
	body_str := string(body)

	if len(body_str) < 10 {
		return nil, nil
	}

	retMap := make(map[string]string)

	/*---------------session 管理 start-----------------*/
	//lg.LogInfo.Debug("%s", r.RequestURI)
	if !strings.Contains(r.RequestURI, "login") && !strings.Contains(r.RequestURI, "registed") && !strings.Contains(r.RequestURI, "logout") && !strings.Contains(r.RequestURI, "getVerifCode") && !strings.Contains(r.RequestURI, "chgpassworld") {
		var sessionID = common.SessionMgr.CheckCookieValid(w, r)

		if sessionID == "" {
			lg.LogInfo.Error("==============会话超时或未登录============")
			retMap["ERR_CODE"] = "9999"

			//http.Redirect(w, r, "/login", http.StatusFound)
			return retMap, fmt.Errorf("会话超时，请重新登录")
		}
		//lg.LogInfo.Debug("前端请求校验session, sessionID: %s", sessionID)
	}

	//退出
	if strings.Contains(r.RequestURI, "logout") {
		lg.LogInfo.Debug("============用户退出，删除相应的session============")
		//用户退出时删除对应session
		common.SessionMgr.EndSession(w, r)
	}
	/*---------------session 管理 end-------------------*/

	//lg.LogInfo.Error("r.RemoteAddr: ", r.RemoteAddr)
	clientIp, err := common.GetClientIp(r.RemoteAddr)
	if err != nil {
		lg.LogInfo.Error("=====error======ReadMessage========common.GetClientIp error=======")
	}
	//lg.LogInfo.Error("clientIp: ", clientIp)

	//解析请求报文
	reqDataMap, err := parse.ParseReqMessage(body_str)
	if err != nil {
		lg.LogInfo.Error("======error========ReadMessage=============[common.ParseJson error]================")
		lg.LogInfo.Error("err: %s", err.Error())

		return nil, err
	}

	reqDataMap["CLIENT_IP"] = clientIp

	//lg.LogInfo.Error("body_str: ", body_str)

	//	r.ParseForm()

	//	lg.LogInfo.Error(r.Form)

	//	var data []byte
	//	r.Body.Read(data)

	//	lg.LogInfo.Error("data: ", string(data))

	//lg.LogInfo.Error("================ReadMessage end ...================")

	return reqDataMap, nil
}

//写应答报文
func WriteMessage(w http.ResponseWriter, responseStr string) error {

	_, err := w.Write([]byte(responseStr))
	if err != nil {
		lg.LogInfo.Error("===error=====WriteMessage============[w.Write error]=============")
		lg.LogInfo.Error("err: %s", err.Error())
		return err
	}
	//lg.LogInfo.Error("n: ", n)

	return nil
}

//服务端处理程序
func NewHttpServer(w http.ResponseWriter, r *http.Request) {
	//lg.LogInfo.Error("============NewHttpServer start ...==================")

	//解决跨域访问的问题-进行设置
	//允许访问所有域
	//w.Header().Set("Access-Control-Allow-Origin", "*")
	//lg.LogInfo.Error("r.Header.Get(\"Origin\"): ", r.Header.Get("Origin"))
	w.Header().Set("Access-Control-Allow-Origin", r.Header.Get("Origin"))
	//header的类型
	w.Header().Add("Access-Control-Allow-Headers", "Content-Type")
	//方法
	w.Header().Set("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE")
	//返回数据格式是json
	w.Header().Set("content-type", "application/json")
	//设置携带者cookie
	w.Header().Set("Access-Control-Allow-Credentials", "true")

	var responseMsg string

	//读取请求报文
	reqDataMap, err := ReadMessage(r, w)
	if err != nil {
		lg.LogInfo.Error("=====error=====NewHttpServer==========[ReadMessage error]==============")
		lg.LogInfo.Error("err: %s", err.Error())
		//通用错误处理流程
		jsonData := GeneralResponse{
			TxCode:  reqDataMap["TXCODE"],
			SeqId:   reqDataMap["SEQ_ID"],
			ErrCode: reqDataMap["ERR_CODE"],
			ErrMsg:  err.Error(),
		}

		b, err := json.Marshal(jsonData)
		if err != nil {
			lg.LogInfo.Error("====error===NewHttpServer==========json.Marshal error=========")
			lg.LogInfo.Error("err: %s", err.Error())

			return
		}

		responseMsg = string(b)

		err1 := WriteMessage(w, responseMsg)
		if err1 != nil {
			lg.LogInfo.Error("===error=====NewHttpServer========WriteMessage error=======")
			lg.LogInfo.Error("err1: %s", err1.Error())
		}

		return
	}

	//for k, v := range reqDataMap {
	//	lg.LogInfo.Error(k, " - ", v)
	//}

	if len(reqDataMap) == 0 {
		responseStr := ""
		err1 := WriteMessage(w, responseStr)
		if err1 != nil {
			lg.LogInfo.Error("====error====NewHttpServer========WriteMessage error=======")
			lg.LogInfo.Error("err1: %s", err1.Error())
		}
	}

	//业务处理
	responseStr, err := BusinessHandler(reqDataMap)
	if err != nil {
		lg.LogInfo.Error("====error======NewHttpServer==========[BusinessHandler error]==============")
		lg.LogInfo.Error("err: %s", err.Error())
	}

	/*--------------------如果登录校验成功，开启session start---------------*/
	if strings.EqualFold(reqDataMap["TXCODE"], "TX0002") && strings.Contains(responseStr, "登录成功") {
		//开启一个session
		var sessionID = common.SessionMgr.StartSession(w, r)
		lg.LogInfo.Debug("登录开启sessionID: %s", sessionID)
	}
	/*--------------------如果登录校验成功，开启session end-----------------*/

	err1 := WriteMessage(w, responseStr)
	if err1 != nil {
		lg.LogInfo.Error("====error====NewHttpServer========WriteMessage error=======")
		lg.LogInfo.Error("err1: %s", err1.Error())
	}
}
