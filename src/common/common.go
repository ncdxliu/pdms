/*
 * 个人数据管理系统公共方法
 */
package common

import (
	"bytes"
	"crypto/cipher"
	"crypto/des"
	"crypto/md5"
	"crypto/rand"
	"encoding/base64"
	"encoding/hex"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"log"
	"net/http"
	"os"
	"path/filepath"
	"runtime"
	"strconv"
	"strings"

	"bufio"
	"errors"
	"io"
	//"math/rand"
	"net"
	"time"

	//"github.com/ethereum/go-ethereum/rpc"
	"github.com/larspensjo/config"

	_ "github.com/go-sql-driver/mysql"
)

/*
 * 查询智能合约返回的结构体
 */
type ApiResponse struct {
	JSONRPC string          `json:"jsonrpc"` // Version of the JSON RPC protocol, always set to 2.0
	ID      int             `json:"id"`      // Auto incrementing ID number for this request
	Error   *apiFailure     `json:"error"`   // Any error returned by the remote side
	Result  json.RawMessage `json:"result"`  // Whatever the remote side sends us in reply
}

type apiFailure struct {
	Code    int    `json:"code"`    // JSON RPC error code associated with the failure
	Message string `json:"message"` // Specific error message of the failure
}

/*
 * 读取ini配置文件
 */
func MakeIni(ini string, sec string) map[string]map[string]string {

	//log.Println("filepath: ", filepath.Dir(os.Args[0]))
	dir, err := filepath.Abs(filepath.Dir(os.Args[0]))
	if err != nil {
		log.Fatal(err)
	}

	path := strings.Replace(dir, "\\", "/", -1)
	path = strings.Replace(path, "bin", "etc", -1)
	var iniMap = make(map[string]map[string]string)
	runtime.GOMAXPROCS(runtime.NumCPU())

	//log.Println(path + "/" + ini)
	cfg, err := config.ReadDefault(path + "/" + ini)
	if err != nil {
		log.Fatalf("Fail to find [", ini, "], err: ", err)
	}

	if sec == "" {
		sections := cfg.Sections()
		for i := range sections {
			var sectionOption string
			sectionOption = sections[i]
			//log.Println("==sectionOption==", sectionOption)

			section, err := cfg.SectionOptions(sectionOption)
			if err == nil {
				iniSecMap := make(map[string]string)
				for _, v := range section {
					options, err := cfg.String(sectionOption, v)
					if err == nil {
						iniSecMap[v] = options
					}
				}
				iniMap[sectionOption] = iniSecMap
			}

		}
	} else {
		if cfg.HasSection(sec) {
			iniSecMap := make(map[string]string)
			section, err := cfg.SectionOptions(sec)
			if err == nil {
				for _, v := range section {
					options, err := cfg.String(sec, v)
					if err == nil {
						iniSecMap[v] = options
					}
				}
			}
			iniMap[sec] = iniSecMap
		}
	}

	//log.Println(iniMap)
	return iniMap
}

/*
 * 获取ini配置文件中单个配置
 */
func GetSingleConfig(ini string, sectionsStr string, sectionStr string) (string, error) {
	dir, err := filepath.Abs(filepath.Dir(os.Args[0]))
	if err != nil {
		log.Printf("获取文件路径错，文件", os.Args[0])
		log.Println(err)
		return "", err
	}
	path := strings.Replace(dir, "\\", "/", -1)

	cfg, err := config.ReadDefault(path + "/" + ini)
	if err != nil {
		log.Println("Fail to find", ini, err)
		log.Println("读取配置文件错[", (path + "/" + ini), "]")
		return "", err
	}

	sections := cfg.Sections()
	for i := range sections {
		var sectionOption string
		sectionOption = sections[i]
		//log.Println("==sectionOption==", sectionOption)
		if sectionOption != sectionsStr {
			continue
		}

		section, err := cfg.SectionOptions(sectionOption)
		if err == nil {
			for _, v := range section {
				options, err := cfg.String(sectionOption, v)
				if err == nil {
					if strings.EqualFold(sectionStr, v) {
						return options, nil
					}
				}
			}
		}

	}

	//log.Println(iniMap)
	return "", fmt.Errorf("dont`t found ", sectionStr, " value")
}

/*
 * 将智能合约函数所有参数转换为16进制并拼接
 */
func MakePara(data map[string]string, contractMap map[string]string) string {
	log.Println("====makePara begin====")
	log.Println("==data==", data)
	log.Println("==contractMap==", contractMap)
	b := bytes.Buffer{}

	para := contractMap["para"]
	paraType := contractMap["paraType"]
	paraLen := contractMap["paraLen"]
	paras := strings.Split(para, ",")
	paraTypes := strings.Split(paraType, ",")
	paraLens := strings.Split(paraLen, ",")
	for i, v := range paras {
		//log.Println("key: ", i, ", value: ", v)
		length, error := strconv.Atoi(paraLens[i])
		if error != nil {
			log.Println("字符串转换成整数失败")
			return fmt.Sprintf("%s", error)
		}
		if paraTypes[i] == "string" {
			hexV := fmt.Sprintf("%x", data[v])
			b.WriteString(hexV)
			b.WriteString(FillZero(length - len(hexV)))
			//log.Println()
			//log.Println("value: ", v, ", hexV: ", hexV)
		} else if paraTypes[i] == "uint" {
			//将字符串转为整数
			intV, err := strconv.ParseInt(data[v], 10, 64)
			if err != nil {
				log.Println("字符串转换成整数失败")
				return fmt.Sprintf("%s", err)
			}

			//将整型转换为16进制
			hexV := fmt.Sprintf("%x", intV)

			//b.WriteString(fillZero(length - len(data[v])))
			//b.WriteString(data[v])
			b.WriteString(FillZero(length - len(hexV)))
			b.WriteString(hexV)
			//log.Println()
			//log.Println("value: ", v, "hex: ", hexV)
		} else if paraTypes[i] == "hex" {
			var hexV string
			if len(data[v]) > 0 {
				n, _ := strconv.Atoi(data[v])
				hexV = fmt.Sprintf("%x", n)
			} else {
				hexV = data[v]
			}
			b.WriteString(FillZero(length - len(hexV)))
			b.WriteString(hexV)

		}

	}
	s := b.String()
	//log.Println("==", s)
	log.Println("====makePara end====")
	return s
}

/*
 * 对参数进行补零
 */
func FillZero(length int) string {
	b := bytes.Buffer{}
	for i := 0; i < length; i++ {
		b.WriteString("0")
	}
	return b.String()
}

/*
 * 复制map
 */
func CopyMap(fromData map[string]string, toData map[string]string) {
	for k, v := range fromData {
		toData[k] = v
	}
}

/*
 * 复制并新建map
 */
func DeepCopy(value map[string]string) map[string]string {

	newMap := make(map[string]string)
	for k, v := range value {
		newMap[k] = v
	}

	return newMap
}

/*
 * 合并并新建map
 */
func MapAddMap(map1 map[string]string, map2 map[string]string) map[string]string {
	newMap := make(map[string]string)
	for k, v := range map1 {
		newMap[k] = v
	}

	for k, v := range map2 {
		newMap[k] = v
	}

	return newMap
}

/*
 * 创建socket连接并发报文
 */
func SocketClient(bankMap map[string]string, msg string) (string, error) {
	bankIP := bankMap["BankIP"]
	bankPort := bankMap["BankPort"]
	log.Println("==socketClient url==", bankIP+":"+bankPort)

	conn, err := net.DialTimeout("tcp", bankIP+":"+bankPort, 2*time.Second)
	if err != nil {
		log.Println("=========error=========[建立connect错]==============")
		log.Println(err)
		return "", err
	}
	defer conn.Close()

	fmt.Fprintf(conn, msg+"\n")

	data, err := bufio.NewReader(conn).ReadString('\n')
	if err != nil {
		if err == io.EOF {
			log.Println("应答报文:[", data, "]")
			return data, nil
		}
		log.Println("=========error=========[socket读取返回报文错]==============")
		log.Println("==socket error==", err.Error())
	}
	log.Println("应答报文:[", data, "]")

	return data, err
}

/*
 * 解析从区块链智能合约中获取的16进制结果
 */
func ParseQueryResult(result string, resultMap map[string]string) error {
	if len(result) < 66 {
		log.Println("input Hexadecimal string is error")
		return errors.New("input Hexadecimal string is error")
	}

	result = strings.Replace(result, "\"", "", -1)

	src := result[2:]

	if len(src)%64 != 0 {
		log.Println("input Hexadecimal string is error (len(src)%64 != 1)")
		return errors.New("input Hexadecimal string is error (len(src)%64 != 1)")
	}

	flag := src[63:64]

	if flag == "1" {
		return errors.New("user does not exist")
	}

	len := len(src) / 64

	for i := 0; i < len; i++ {
		temp := src[(i * 64):((i + 1) * 64)]
		//log.Println("temp:[", temp, "]")
		key := fmt.Sprintf("result_%d", i)
		if temp[0:5] == "00000" {
			nRet, err := strconv.ParseInt(temp, 16, 64)
			if err != nil {
				log.Println(err)
			}
			//fmt.Println("nRet = ", nRet)
			value := fmt.Sprintf("%d", nRet)
			resultMap[key] = value
		} else {
			var str string
			for j := 0; j < 32; j++ {
				ch := temp[j*2 : (j+1)*2]
				n, _ := strconv.ParseInt(ch, 16, 64)
				str += string(fmt.Sprintf("%c", n))
			}
			//fmt.Println(str)
			resultMap[key] = str
		}
	}

	return nil
}

/*
 * 发送http请求调用智能合约
 */
/*
func BusinessToApi(request interface{}, configMap map[string]string) (string, error) {
	var httpClient rpc.Client
	var err error
	var body []byte
	rpcIP := configMap["RPCIP"]
	rpcPort := configMap["RPCPort"]

	if httpClient, err = rpc.NewHTTPClient("http://" + rpcIP + ":" + rpcPort); err != nil {
		return "", err
	}

	if err = httpClient.Send(request); err != nil {
		return "", err
	}

	res := new(ApiResponse)
	if err = httpClient.Recv(res); err != nil {
		return "", err
	}
	body = res.Result

	return string(body), err
}
*/

/*
 * 获取当前日期 yyyymmdd
 */
func GetNowDate() string {
	//Format内的值不能修改
	return time.Now().Format("20060102")
}

/*
 * 获取当前时间 HHMMSS
 */
func GetNowTime() string {
	//Format内的值不能修改
	return time.Now().Format("150405")
}

/*
 * 获取当前日期时间 yyyymmddHHMMSS
 */
func GetNowDateTime() string {
	//Format内的值不能修改
	return time.Now().Format("20060102150405")
}

/*
 * 字符串拼接
 */
func stringSplice(strs ...string) string {
	var buf bytes.Buffer

	for _, str := range strs {
		buf.WriteString(str)
	}

	return buf.String()
}

/*
 * 获取20位的流水
 * 每秒10万个内不重复
 * 只能单线程/协程使用
 */
/*func GetSsn() string {
	strNum := strconv.Itoa(rand.Intn(99999))
	return stringSplice(time.Now().Format("20060102150405"), FillZero(6-len(strNum)), strNum)
}*/

/*
 * JSON格式数据解析
 */
func ParseJson(srcStr string) (map[string]string, error) {
	//b := []byte(`{"user": "zhangdesheng", "password": "123456"}`)
	//b := []byte(`{"code":"200", "msg":"成功或者失败", "data":[{"username":"zhangdesheng", "test":"xxx"}, {"data-test":"liufuhua"}, "data-test1"]}`)
	//b := []byte(`{"code": "200", "user": {"username": "admin", "id": 1, "name": "admin", "avatar": "", "password": ""}, "msg": "\u767b\u5f55\u6210\u529f!"}`)

	//log.Println("[", srcStr, "]")
	//	str := "`" + srcStr + "`"

	//log.Printf("%d, %q\n", len(srcStr), srcStr)
	//str := srcStr[:len(srcStr)]
	//log.Printf("====[%x]====\n", strings.Replace(str, "\n", "", -1))

	b := GetValidByte([]byte(srcStr))
	var result interface{}

	err := json.Unmarshal(b, &result)
	if err != nil {
		fmt.Println("json.Unmarshal error")
		fmt.Println("err: ", err)
		return nil, err
	}

	//fmt.Println(result)

	dataMap := make(map[string]string)
	jsondata, ok := result.(map[string]interface{}) //类型查询，判断result是否为 map[string]interface{}类型

	if ok {
		for k, v := range jsondata {
			//fmt.Println("k: ", k, "v: ", v)
			switch v1 := v.(type) {
			case string:
				//fmt.Println("-->", k, "is string", v2)
				dataMap[k] = v1
			case int:
				//fmt.Println(k, "is int", v1)
				dataMap[k] = strconv.Itoa(v1)
			case float64:
				fmt.Println(k, "is float64", v1)
				fmt.Println("strconv.FormatFloat(v1, 'g', 1, 64): ", strconv.FormatFloat(v1, 'g', 1, 64))
				//dataMap[k] = fmt.Sprintf("%0.f", v1)
				dataMap[k] = strconv.FormatFloat(v1, 'f', -1, 64)
			case bool:
				//fmt.Println(k, "is bool", v1)
			case []interface{}:
				//fmt.Println(k, "is an array:")
				//panic: interface conversion: interface {} is map[string]interface {}, not map[string]string

				for i, iv := range v1 {
					//fmt.Println(i, iv)

					switch jv := iv.(type) {
					case string:
						fmt.Println(i, " - is string ", jv)
					case map[string]interface{}:
						//fmt.Println(i, " is map[string]string", jv)
						for mapk, mapv := range jv {
							switch mapv1 := mapv.(type) {
							case string:
								//fmt.Println(mapk, " is string ", mapv1)
								dataMap[mapk] = mapv1
							case float64:
								//dataMap[mapk] = fmt.Sprintf("%.0f", mapv1)
								dataMap[mapk] = strconv.FormatFloat(mapv1, 'g', 1, 64)
							default:
								fmt.Println("=====error======", "mapk: ", mapk, " mapv1: ", mapv1, " is another type not handler yet")
							}
						}

					}
				}
			case map[string]interface{}:
				//fmt.Println(k, "is map[string]interface{}")

				for mapk, mapv := range v1 {
					//fmt.Println(mapk, mapv)
					switch mapv1 := mapv.(type) {
					case string:
						//fmt.Println("k2: ", k2, "v2: ", v2)
						dataMap[mapk] = mapv1
					case float64:
						//dataMap[mapk] = fmt.Sprintf("%.0f", mapv1)
						dataMap[mapk] = strconv.FormatFloat(mapv1, 'g', 1, 64)
					default:
						fmt.Println("=====error======", "mapk: ", mapk, " mapv1: ", mapv1, " is another type not handler yet")
					}
				}

			default:
				fmt.Println("=====error======", k, "is another type not handler yet")
			}
		}
	}

	//	for k, v := range dataMap {
	//		fmt.Println("key: ", k, "value: ", v)
	//	}

	return dataMap, nil
}

/*
 * 去除字符串尾部的/0x00
 */
func GetValidByte(src []byte) []byte {
	var str_buf []byte
	for _, v := range src {
		if v != 0 {
			str_buf = append(str_buf, v)
		}
	}

	//另一种方法
	//b := a[:]
	// 去除切片尾部的所有0
	//c := bytes.TrimRight(b, "\x00")

	return str_buf
}

func GetMd5String(s string) string {
	h := md5.New()
	h.Write([]byte(s))

	return hex.EncodeToString(h.Sum(nil))
}

/*
 * 获取GUID
 */
func GetGuid() string {
	b := make([]byte, 48)
	if _, err := io.ReadFull(rand.Reader, b); err != nil {
		log.Println("============error==========[GetGuid error]============")
		log.Println("err: ", err)

		return ""
	}

	return GetMd5String(base64.URLEncoding.EncodeToString(b))
}

func PKCS5Padding(ciphertext []byte, blockSize int) []byte {
	padding := blockSize - len(ciphertext)%blockSize
	padtext := bytes.Repeat([]byte{byte(padding)}, padding)

	return append(ciphertext, padtext...)
}

func PKCS5UnPadding(origData []byte) []byte {

	length := len(origData)

	// 去掉最后一个字节 unpadding 次
	unpadding := int(origData[length-1])

	return origData[:(length - unpadding)]
}

//3DES加密
func TripleDesEncrypt(origData, key []byte) ([]byte, error) {

	block, err := des.NewTripleDESCipher(key)
	if err != nil {
		return nil, err
	}

	origData = PKCS5Padding(origData, block.BlockSize())
	// origData = ZeroPadding(origData, block.BlockSize())
	blockMode := cipher.NewCBCEncrypter(block, key[:8])
	crypted := make([]byte, len(origData))
	blockMode.CryptBlocks(crypted, origData)

	return crypted, nil
}

//3DES解密
func TripleDesDecrypt(crypted, key []byte) ([]byte, error) {

	block, err := des.NewTripleDESCipher(key)
	if err != nil {
		return nil, err
	}

	blockMode := cipher.NewCBCDecrypter(block, key[:8])
	origData := make([]byte, len(crypted))
	// origData := crypted
	blockMode.CryptBlocks(origData, crypted)
	origData = PKCS5UnPadding(origData)
	// origData = ZeroUnPadding(origData)

	return origData, nil
}

//数据加密并base64编码
func DataEncrypt(srcStr string) (string, error) {
	//3des加密
	b1, err := TripleDesEncrypt([]byte(srcStr), []byte(DataKey))
	if err != nil {
		log.Println("=====error=====dataEncrypt======TripleDesEncrypt error==========")
		log.Println("err: ", err)
		return "", err
	}

	//base64编码
	encodeString := base64.StdEncoding.EncodeToString(b1)
	//fmt.Println("base64编码: ", encodeString)

	return encodeString, nil
}

//数据base64解码并解密
func DataDecrypt(encodeString string) (string, error) {
	//进行base64解码
	decodeBytes, err := base64.StdEncoding.DecodeString(encodeString)
	if err != nil {
		log.Println("======error====dataDecrypt=====base64.StdEncoding.DecodeString error===========")
		log.Println("err: ", err)

		return "", err
	}

	b2, err := TripleDesDecrypt(decodeBytes, []byte(DataKey))
	if err != nil {
		log.Println("=====eror=====dataDecrypt========TripleDesDecrypt error===========")
		log.Println("err: ", err)
		return "", err
	}

	//fmt.Println("解密后: ", string(b2))

	return string(b2), nil
}

//获取IP
func GetClientIp(remoteAddr string) (string, error) {
	rs := []rune(remoteAddr)
	n := 0
	i := 0

	for i = 0; i < len(rs); i++ {
		if rs[i] == 58 {
			n++
			if n == 1 {
				break
			}
		}
	}

	return string(rs[0:i]), nil
}

//获取IP所属地址
func GetIpAddress(ip string) string {
	resp, err := http.Get("http://www.ip.cn/index.php?ip=" + ip)
	//resp, err := http.Get("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js&ip=10.135.35.222")
	if err != nil {
		log.Println("http.Get 获取IP所属地址网页错")
		return ""
	}

	defer resp.Body.Close()
	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		log.Println("ioutil.ReadAll 读取IP所属地址网页错")
		return ""
	}

	src := string(body)

	srclen := len(src)
	n := strings.Index(src, "<code>")
	n1 := strings.Index(src, "</code>")

	if n < 0 || n1 < 0 {
		return ""
	}

	if srclen < (n1 + 7) {
		return ""
	}

	//bs := []byte(src)[n+7 : n1]
	//截取到ip
	//fmt.Println(string(bs))

	src1 := string([]byte(src)[n1+7 : srclen])

	n = strings.Index(src1, "<code>")
	n1 = strings.Index(src1, "</code>")

	if n < 0 || n1 < 0 {
		return ""
	}

	if n1 < (n + 6) {
		return ""
	}

	//截取ip所属地址
	bs := []byte(src1)[n+6 : n1]

	//fmt.Println("n = ", n)
	//fmt.Println(string(bs))

	return string(bs)
}
