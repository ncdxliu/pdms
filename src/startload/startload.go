//个人数据管理系统-启动加载项
package startload

import (
	"common"
	"dbopr"
	lg "log4go"
)

//启动加载项
func StartLoad() error {
	var err error

	//加载配置文件
	common.INIConfig = common.MakeIni(common.ConfigFile, "")

	//获取mysql数据库配置
	common.DBAMap = common.INIConfig["mysql"]

	//获取email邮箱配置
	common.EmailMap = common.INIConfig["email"]

	//获取数据备份配置
	common.BackupMap = common.INIConfig["backup"]

	//获取数据库连接
	dbopr.Dbconn, err = dbopr.GetDbConn(common.DBAMap)
	if err != nil {
		lg.LogInfo.Error("======error====StartLoad=====GetDbConn error=========")
		lg.LogInfo.Error("err: %s", err.Error())

		return err
	}

	return nil
}
