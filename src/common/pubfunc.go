/*
 * 个人数据管理系统公共方法
 */
package common

import (
	"dbopr"
	"log"
	"math/rand"
	"net/smtp"
	"strconv"
	"strings"
	"time"
)

//用户邮箱结构
type EmailUser struct {
	User     string
	Password string
	Host     string
	To       string
	MailType string
	Subject  string
	Body     string
}

//新建
func NewEmailUser(serverEmail string, serverEmailPwd string, emailHost string, toEmail string) *EmailUser {
	return &EmailUser{
		User:     serverEmail,    //自己的邮箱
		Password: serverEmailPwd, //这个密码是smtp服务的密码，在邮箱中打开pop3/smtp服务，设立独立密码即可
		Host:     emailHost,
		To:       toEmail, //要发送到哪个邮箱
	}
}

//smtp服务发送邮件, mailType表示邮件格式是普通文件还是html或其他
func SendToMail(host, user, password, to, mailType, subject, body string) error {
	hp := strings.Split(host, ":")
	auth := smtp.PlainAuth("", user, password, hp[0])
	content_type := "Content-Type: text/" + mailType + "; charset=UTF-8"
	msg := []byte("To: " + to + "\r\nFrom: " + user + "\r\nSubject: " + subject + "\r\n" + content_type + "\r\n\r\n" + body)
	send_to := strings.Split(to, ";")
	log.Println("send_to: ", send_to)
	log.Println("===========smtp.SendMail start ...=================")
	err := smtp.SendMail(host, auth, user, send_to, msg)
	if err != nil {
		log.Println("======error======SendToMail=====发送邮件到用户邮箱错=========")
		log.Println("SendMail error: ", err)

		return err
	}
	log.Println("===========smtp.SendMail end=======================")

	return nil
}

/*
 * 获取20位的流水
 * 每秒10万个内不重复
 * 只能单线程/协程使用
 */
func GetSsn() string {
	//strNum := strconv.Itoa(rand.Intn(99999))
	r := rand.New(rand.NewSource(time.Now().UnixNano()))
	strNum := strconv.Itoa(r.Intn(999999))
	return stringSplice(time.Now().Format("20060102150405"), FillZero(6-len(strNum)), strNum)
}

/*
 * 获取6位随机数
 */
func GetRandString() string {
	/*strNum := strconv.Itoa(rand.Intn(999999))

	return stringSplice(strNum, FillZero(6-len(strNum)))*/

	r := rand.New(rand.NewSource(time.Now().UnixNano()))
	strNum := strconv.Itoa(r.Intn(999999))

	return stringSplice(strNum, FillZero(6-len(strNum)))
}

//判断用户是否存在
func JudgeUserIsExist(data map[string]string) (bool, error) {
	rows, err := dbopr.Dbconn.Query("select count(1) from TB_USER where (user_name = ? or user_email = ? or user_phone = ?) and data_status != '04'", data["USER_NAME"], data["USER_NAME"], data["USER_NAME"])
	if err != nil {
		log.Println("===error====judgeUserIsExist=========Dbconn.Query error===========")
		log.Println("err: ", err)

		return false, err
	}
	defer rows.Close()

	var strCnt string

	for rows.Next() {
		err := rows.Scan(&strCnt)
		if err != nil {
			log.Println("===error======judgeUserIsExist========rows.Scan error===========")
			log.Println("err: ", err)
		}

		//log.Println("id: ", id, ", name: ", name, ", sex: ", sex, ", role: ", role, ", branch: ", branch)
	}

	err = rows.Err()
	if err != nil {
		log.Println("===error======judgeUserIsExist=========rows.Err error==========")
		log.Println("err: ", err)
	}

	log.Println("judgeUserIsExist ==> strCnt: ", strCnt)
	n, err := strconv.ParseInt(strCnt, 10, 32)
	if err != nil {
		log.Println("====error======judgeUserIsExist========strconv.ParseInt error=========")
		log.Println("err: ", err)

		return false, err
	}

	if n >= 1 {
		return true, nil
	}

	return false, nil
}

//获取用户邮箱
func GetEmail(data map[string]string) (string, error) {
	rows, err := dbopr.Dbconn.Query("select USER_EMAIL from TB_USER where (user_name = ? or user_email = ? or user_phone = ?) and data_status != '04'", data["USER_NAME"], data["USER_NAME"], data["USER_NAME"])
	if err != nil {
		log.Println("===error====GetEmail=========Dbconn.Query error===========")
		log.Println("err: ", err)

		return "", err
	}
	defer rows.Close()

	var email string

	for rows.Next() {
		err := rows.Scan(&email)
		if err != nil {
			log.Println("===error======GetEmail========rows.Scan error===========")
			log.Println("err: ", err)
		}

		//log.Println("id: ", id, ", name: ", name, ", sex: ", sex, ", role: ", role, ", branch: ", branch)
	}

	err = rows.Err()
	if err != nil {
		log.Println("===error======GetEmail=========rows.Err error==========")
		log.Println("err: ", err)
	}

	return email, nil
}

//获取用户手机号
func GetPhone(data map[string]string) (string, error) {
	rows, err := dbopr.Dbconn.Query("select USER_PHONE from TB_USER where (user_name = ? or user_email = ? or user_phone = ?) and data_status != '04'", data["USER_NAME"], data["USER_NAME"], data["USER_NAME"])
	if err != nil {
		log.Println("===error====GetEmail=========Dbconn.Query error===========")
		log.Println("err: ", err)

		return "", err
	}
	defer rows.Close()

	var phone string

	for rows.Next() {
		err := rows.Scan(&phone)
		if err != nil {
			log.Println("===error======GetEmail========rows.Scan error===========")
			log.Println("err: ", err)
		}

		//log.Println("id: ", id, ", name: ", name, ", sex: ", sex, ", role: ", role, ", branch: ", branch)
	}

	err = rows.Err()
	if err != nil {
		log.Println("===error======GetEmail=========rows.Err error==========")
		log.Println("err: ", err)
	}

	return phone, nil
}
