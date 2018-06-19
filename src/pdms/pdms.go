//个人数据管理系统主程序
package main

import (
	"common"
	"dbopr"
	"flag"
	"glog"
	"log"
	lg "log4go"
	"net/http"
	"os"
	"path/filepath"
	"runtime"
	"server"
	"session"
	"startload"
	"strings"
	"time"
)

var lPort *string = flag.String("p", "listenPort", "server http listen port")

var Usage = func() {
	lg.LogInfo.Error("Usage: pdms -p 8080")
}

//系统停止时关闭资源
func deinitAll() {
	//关闭日志
	if nil != lg.LogInfo {
		lg.LogInfo.Close()
		//os.Stdout.Sync() //try manually flush, but can not fix log4go's flush bug

		lg.LogInfo = nil
	}

	//关闭数据库连接
	dbopr.Dbconn.Close()
}

//日志初始化
func initLogger() {
	//var filenameOnly string = GetCurFilename()

	dir, err := filepath.Abs(filepath.Dir(os.Args[0]))
	if err != nil {
		log.Printf("err: %s", err.Error())
	}

	path := strings.Replace(dir, "\\", "/", -1)
	path = strings.Replace(path, "bin", "log", -1)
	//lg.LogInfo.Error(path)
	nowDate := common.GetNowDate()

	var logFilename string = path + "/pdms_" + nowDate + ".log"
	//lg.LogInfo.Error(logFilename)

	lg.LogInfo = nil

	//所有的level，依次是：Finest, Fine, Debug, Trace, Info, Warning, Error, Critical
	//这里的log4go.CRITICAL，控制的是控制台显示的日志级别
	lg.LogInfo = lg.NewDefaultLogger(lg.CRITICAL)

	//for log file
	/*if _, err := os.Stat(logFilename); err == nil {
		os.Remove(logFilename)
	}*/

	//这里的log4go.DEBUG，控制的是只打印级别打印这个的日志
	lg.LogInfo.AddFilter("log", lg.DEBUG, lg.NewFileLogWriter(logFilename, false))
	lg.LogInfo.Debug("Current time is : %s", time.Now().Format("15:04:05 MST 2006/01/02"))

	return
}

//初始化
func initAll() {
	//日志初始化
	initLogger()

	//创建session管理器,"StssCookieName"是浏览器中cookie的名字，3600是浏览器cookie的有效时间（秒）
	common.SessionMgr = session.NewSessionMgr("StssCookieName", common.SessionTimeOut)

}

//主程序
func main() {
	lg.LogInfo.Error("================= 个人数据管理系统启动 ... ===============")

	//flag.String("log_dir", "", "If non-empty, write log files in this directory")

	//判断参数个数
	if len(os.Args) < 3 {
		lg.LogInfo.Error("==error====main=======[input params is error]=========")
		Usage()

		return
	}

	//lg.LogInfo.Error("os.TempDir(): ", os.TempDir())

	//解析输入的参数
	flag.Parse()

	//退出时调用，确保日志写入文件中
	defer glog.Flush()

	//退出时调用，确保数据库连接关闭
	defer dbopr.Dbconn.Close()

	//glog.Info("hello, glog")

	//设置CPU数量
	runtime.GOMAXPROCS(runtime.NumCPU())

	//初始化
	initAll()

	//加载启动项
	err := startload.StartLoad()
	if err != nil {
		lg.LogInfo.Error("===error=====main===========StartLoad error===========")
		lg.LogInfo.Error("err: ", err)

		return
	}

	//加载http服务
	http.HandleFunc("/", server.NewHttpServer)

	//服务器要监听的主机地址和端口号
	err = http.ListenAndServe("0.0.0.0:"+*lPort, nil)
	if err != nil {
		lg.LogInfo.Error("===error====main=======[http.ListenAndServe error port:%s]=========", *lPort)
		lg.LogInfo.Error("err: %s", err.Error())

		return
	}

	//http.HandleFunc("/", SayHello)
	/*err = http.ListenAndServeTLS("0.0.0.0:"+*lPort, "cert.pem", "key.pem", nil)
	if err != nil {
		log.Printf("===error====main=======[http.ListenAndServeTLS error port:%s]=========", *lPort)
		lg.LogInfo.Error("err: ", err)

		return
	}*/

	//系统停止，关闭资源
	deinitAll()

	lg.LogInfo.Debug("================= 个人数据管理系统退出 ... ===============")
}
