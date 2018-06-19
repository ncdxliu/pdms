//个人数据管理系统报文处理程序
package parse

import (
	"common"
)

func ParseReqMessage(reqStr string) (map[string]string, error) {
	return common.ParseJson(reqStr)
}
