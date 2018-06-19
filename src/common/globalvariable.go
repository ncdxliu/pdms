//个人数据管理系统-全局变量
package common

import "session"

//数据加解密密钥
var DataKey string = "lfh-20170223-zaiguangfa*"

//配置文件map
var INIConfig map[string]map[string]string

//配置文件名
var ConfigFile string = "config.ini"

//mysql数据库配置
var DBAMap map[string]string

//邮箱配置
var EmailMap map[string]string

//数据备份配置
var BackupMap map[string]string

//session管理器
var SessionMgr *session.SessionMgr = nil

//session超时时间(单位:秒)
var SessionTimeOut int64 = 1800

//客户端cookie过期时间
var CookieTimeOUt int = 36000
