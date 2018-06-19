//个人数据管理系统数据库操作函数
package dbopr

import (
	"crypto/cipher"
	"crypto/des"
	"database/sql"
	"encoding/base64"
	"log"
	lg "log4go"
	"strings"

	_ "github.com/go-sql-driver/mysql"
)

//数据库连接
var Dbconn *sql.DB

//数据加解密密钥
var dataKey string = "lfh-20170223-zaiguangfa*"

func pkcS5UnPadding(origData []byte) []byte {

	length := len(origData)

	// 去掉最后一个字节 unpadding 次
	unpadding := int(origData[length-1])

	return origData[:(length - unpadding)]
}

//3DES解密
func tripleDesDecrypt(crypted, key []byte) ([]byte, error) {

	block, err := des.NewTripleDESCipher(key)
	if err != nil {
		return nil, err
	}

	blockMode := cipher.NewCBCDecrypter(block, key[:8])
	origData := make([]byte, len(crypted))
	// origData := crypted
	blockMode.CryptBlocks(origData, crypted)
	origData = pkcS5UnPadding(origData)
	// origData = ZeroUnPadding(origData)

	return origData, nil
}

//数据base64解码并解密
func dataDecrypt(encodeString string) (string, error) {
	//进行base64解码
	decodeBytes, err := base64.StdEncoding.DecodeString(encodeString)
	if err != nil {
		lg.LogInfo.Error("======error====dataDecrypt=====base64.StdEncoding.DecodeString error===========")
		lg.LogInfo.Error("err: %s", err.Error())

		return "", err
	}

	b2, err := tripleDesDecrypt(decodeBytes, []byte(dataKey))
	if err != nil {
		lg.LogInfo.Error("=====eror=====dataDecrypt========TripleDesDecrypt error===========")
		lg.LogInfo.Error("err: %s", err.Error())
		return "", err
	}

	//fmt.Println("解密后: ", string(b2))

	return string(b2), nil
}

//获取mysql连接
func GetDbConn(dbMap map[string]string) (*sql.DB, error) {
	//获取数据库配置
	dbName := dbMap["DBName"]
	ip := dbMap["IP"]
	port := dbMap["Port"]
	userName := dbMap["UserName"]

	pass, err := dataDecrypt(dbMap["UserPass"])
	if err != nil {
		lg.LogInfo.Error("====error======GetDbConn======dataDecrypt error=======")
		return nil, err
	}

	userPass := pass
	charSet := dbMap["Charset"]

	lg.LogInfo.Debug("开始连接mysql数据库...")

	//数据库连接url
	url := userName + ":" + userPass + "@tcp(" + ip + ":" + port + ")/" + dbName + "?charset=" + charSet
	//log.Println("==SaveDB url==" + url)
	//打开mysql
	db, err := sql.Open("mysql", url)
	if err != nil {
		lg.LogInfo.Error("======error=====sql.Open error==========")
		lg.LogInfo.Error("err: %s", err.Error())

		return nil, err
	}

	//defer db.Close()

	err = db.Ping()
	if err != nil {
		lg.LogInfo.Error("========error==========db.Ping error==========")
		lg.LogInfo.Error("err: %s", err.Error())

		return nil, err
	}

	lg.LogInfo.Debug("连接mysql数据库成功!")

	return db, nil
}

func QueryData() {
	//	dbName := "lfhdb"
	//	ip := "10.211.55.3"
	//	port := "3306"
	//	userName := "root"
	//	userPass := "mysql"
	//	charSet := "utf8"

	//	url := userName + ":" + userPass + "@tcp(" + ip + ":" + port + ")/" + dbName + "?charset=" + charSet

	//	log.Println("==QueryDB url==", url)
	//	db, err := sql.Open("mysql", url)
	//	if err != nil {
	//		log.Println(err)
	//	}

	//	defer db.Close()
	//	log.Println("succ")

	//	err = db.Ping()
	//	if err != nil {
	//		log.Println(err)
	//	}

	var id string
	var name string
	var sex string
	var role string
	var branch string

	rows, err := Dbconn.Query("select id, name, sex, role, branch from xchain_user")
	if err != nil {
		log.Println(err)
	}
	defer rows.Close()

	for rows.Next() {
		err := rows.Scan(&id, &name, &sex, &role, &branch)
		if err != nil {
			log.Println(err)
		}

		log.Println("id: ", id, ", name: ", name, ", sex: ", sex, ", role: ", role, ", branch: ", branch)
	}
	err = rows.Err()
	if err != nil {
		log.Println(err)
	}

}

func saveResult(dbMap map[string]string, data map[string]string) error {
	dbName := dbMap["DBName"]
	ip := dbMap["IP"]
	port := dbMap["Port"]
	userName := dbMap["UserName"]
	userPass := dbMap["UserPass"]
	charSet := dbMap["Charset"]
	url := userName + ":" + userPass + "@tcp(" + ip + ":" + port + ")/" + dbName + "?charset=" + charSet
	log.Println("==SaveDB url==" + url)
	db, err := sql.Open("mysql", url)
	if err != nil {
		return err
	}

	defer db.Close()
	log.Println("succ")
	err = db.Ping()
	if err != nil {
		return err
	}

	stmt, err := db.Prepare("INSERT ChainResult (ProcID,MsgID,ReferResult,ProcTime) values (?,?,?,?)")
	if err != nil {
		return err
	}
	referStr := strings.Replace(data["ReferResult"], "\"", "", -1)
	rs, err := stmt.Exec(data["ProcID"], data["MsgID"], referStr, data["ProcTime"])
	if err != nil {
		return err
	}
	stmt.Close()
	//获得插入的id
	id, err := rs.LastInsertId()
	log.Println("==saveResult id==", id)
	//获得影响行数
	affect, err := rs.RowsAffected()
	log.Println("==saveResult affect==", affect)

	return err
}

func queryResult(dbaMap map[string]string, data map[string]string) (string, string) {
	dbName := dbaMap["DBName"]
	ip := dbaMap["IP"]
	port := dbaMap["Port"]
	userName := dbaMap["UserName"]
	userPass := dbaMap["UserPass"]
	charSet := dbaMap["Charset"]
	url := userName + ":" + userPass + "@tcp(" + ip + ":" + port + ")/" + dbName + "?charset=" + charSet

	log.Println("==QueryDB url==", url)
	db, err := sql.Open("mysql", url)
	if err != nil {
		log.Println(err)
	}

	defer db.Close()
	log.Println("succ")
	err = db.Ping()
	if err != nil {
		log.Println(err)
	}

	var referResult string
	var msgID string
	rows, err := db.Query("select MsgID,ReferResult from ChainResult where ProcID = ? ", data["OriProcID"])
	if err != nil {
		log.Println(err)
	}
	defer rows.Close()
	for rows.Next() {
		err := rows.Scan(&msgID, &referResult)
		if err != nil {
			log.Println(err)
		}
	}
	err = rows.Err()
	if err != nil {
		log.Println(err)
	}

	log.Println("==QueryDB result==", referResult)
	return msgID, referResult

}
