//数据增删改进行备份
package server

import (
	"common"
	lg "log4go"
	"os/exec"
)

//进行数据备份
func backup() {
	//获取备份路径
	path := common.BackupMap["path"]
	//获取数据库ip
	ip := common.BackupMap["ip"]
	//获取数据库名
	dbname, err := common.DataDecrypt(common.BackupMap["dbname"])
	if err != nil {
		lg.LogInfo.Error("backup，数据备份获取数据库名错，数据备份失败")
		lg.LogInfo.Error("err: %s", err.Error())
		return
	}
	//获取数据库用户
	dbuser, err1 := common.DataDecrypt(common.BackupMap["dbuser"])
	if err1 != nil {
		lg.LogInfo.Error("backup，数据备份获取数据库用户，数据备份失败")
		lg.LogInfo.Error("err: %s", err1.Error())
		return
	}
	//获取数据库密码
	dbpassword, err2 := common.DataDecrypt(common.BackupMap["dbpassword"])
	if err2 != nil {
		lg.LogInfo.Error("backup，数据备份获取数据库用户，数据备份失败")
		lg.LogInfo.Error("err: %s", err2.Error())
		return
	}

	//执行备份脚本
	//dbuser, dbpassword, dbname, ip, path
	f, err := exec.Command("/bin/sh", path+"/backup.sh", dbuser, dbpassword, dbname, ip, path).Output()
	if err != nil {
		lg.LogInfo.Error("===========执行数据备份脚本失败============")
		return
	}
	lg.LogInfo.Debug("%s", string(f))
	lg.LogInfo.Debug("==================数据备份成功===============")
}

//执行【ls /】并输出返回文本
/*
   f, err := exec.Command("/bin/sh", "./backup.sh", "lfhmysql", "ncdxLiu#408209", "lfhdb").Output()
   if err != nil {
           fmt.Println(err.Error())
   }
   fmt.Println(string(f))
*/
