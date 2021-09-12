/*
 Navicat Premium Data Transfer

 Source Server         : aliy-lfhdb
 Source Server Type    : MySQL
 Source Server Version : 50724
 Source Host           : 47.107.126.3:3306
 Source Schema         : lfhdb

 Target Server Type    : MySQL
 Target Server Version : 50724
 File Encoding         : 65001

 Date: 11/09/2021 19:25:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_content
-- ----------------------------
DROP TABLE IF EXISTS `tb_content`;
CREATE TABLE `tb_content` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键序号',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `data_type` char(4) DEFAULT NULL COMMENT '数据类型 0000-普通数据  0001-重要数据',
  `title` varchar(200) DEFAULT NULL COMMENT '标题',
  `content1` mediumtext COMMENT '内容1',
  `content2` mediumtext COMMENT '内容2',
  `content3` mediumtext COMMENT '内容3',
  `content4` mediumtext COMMENT '内容4',
  `content5` mediumtext COMMENT '内容5',
  `status` char(1) DEFAULT NULL COMMENT '状态 0-正常 1-删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `pk_tb_content_index1` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=545 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_content1
-- ----------------------------
DROP TABLE IF EXISTS `tb_content1`;
CREATE TABLE `tb_content1` (
  `DATA_ID` char(32) NOT NULL,
  `DATA_DATE` char(8) DEFAULT NULL,
  `USER_ID` char(32) DEFAULT NULL,
  `DATA_TYPE` varchar(4) DEFAULT NULL,
  `TITLE` varchar(120) DEFAULT NULL,
  `CONTENT1` mediumtext,
  `CONTENT2` mediumtext,
  `CONTENT3` mediumtext,
  `CONTENT4` mediumtext,
  `CONTENT5` mediumtext,
  `STATUS` varchar(2) DEFAULT NULL,
  `ADD_DATE` char(8) DEFAULT NULL,
  `ADD_TIME` char(14) DEFAULT NULL,
  `MOD_DATE` char(8) DEFAULT NULL,
  `MOD_TIME` char(14) DEFAULT NULL,
  `RSV1` varchar(180) DEFAULT NULL,
  `RSV2` varchar(180) DEFAULT NULL,
  `RSV3` varchar(180) DEFAULT NULL,
  `RSV4` varchar(180) DEFAULT NULL,
  `RSV5` varchar(180) DEFAULT NULL,
  PRIMARY KEY (`DATA_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for tb_dict
-- ----------------------------
DROP TABLE IF EXISTS `tb_dict`;
CREATE TABLE `tb_dict` (
  `DATA_ID` char(32) NOT NULL,
  `DATA_DATE` char(8) DEFAULT NULL,
  `GOUP_ID` varchar(10) DEFAULT NULL,
  `GOUP_NAME` varchar(64) DEFAULT NULL,
  `DICT_KEY` varchar(64) DEFAULT NULL,
  `DICT_VALUE` varchar(90) DEFAULT NULL,
  `RSV1` varchar(180) DEFAULT NULL,
  `RSV2` varchar(180) DEFAULT NULL,
  `RSV3` varchar(180) DEFAULT NULL,
  `RSV4` varchar(180) DEFAULT NULL,
  `RSV5` varchar(180) DEFAULT NULL,
  PRIMARY KEY (`DATA_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for tb_login_log
-- ----------------------------
DROP TABLE IF EXISTS `tb_login_log`;
CREATE TABLE `tb_login_log` (
  `DATA_ID` char(32) NOT NULL,
  `DATA_DATE` char(8) DEFAULT NULL,
  `USER_ID` char(32) DEFAULT NULL,
  `OPER` varchar(2) DEFAULT NULL,
  `OPER_IP` varchar(20) DEFAULT NULL,
  `OPER_DATE` char(8) DEFAULT NULL,
  `OPER_TIME` char(14) DEFAULT NULL,
  `REMARK` varchar(512) DEFAULT NULL,
  `RSV1` varchar(180) DEFAULT NULL,
  `RSV2` varchar(180) DEFAULT NULL,
  `RSV3` varchar(180) DEFAULT NULL,
  `RSV4` varchar(180) DEFAULT NULL,
  `RSV5` varchar(180) DEFAULT NULL,
  PRIMARY KEY (`DATA_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for tb_sysparams
-- ----------------------------
DROP TABLE IF EXISTS `tb_sysparams`;
CREATE TABLE `tb_sysparams` (
  `DATA_ID` char(32) NOT NULL,
  `DATA_DATE` char(8) DEFAULT NULL,
  `PARA_NAME` varchar(64) DEFAULT NULL,
  `PARA_VALUE` varchar(512) DEFAULT NULL,
  `RSV1` varchar(180) DEFAULT NULL,
  `RSV2` varchar(180) DEFAULT NULL,
  `RSV3` varchar(180) DEFAULT NULL,
  `RSV4` varchar(180) DEFAULT NULL,
  `RSV5` varchar(180) DEFAULT NULL,
  PRIMARY KEY (`DATA_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键序号',
  `user_name` varchar(32) DEFAULT NULL COMMENT '用户名称(字母、数字)',
  `user_password` varchar(32) DEFAULT NULL COMMENT '登录密码',
  `openid` varchar(64) DEFAULT NULL COMMENT '用户微信唯一标识',
  `user_email` varchar(64) DEFAULT NULL COMMENT '用户邮箱',
  `user_phone` varchar(20) DEFAULT NULL COMMENT '电话号码',
  `registered_time` char(14) DEFAULT NULL COMMENT '注册时间',
  `remark` varchar(60) DEFAULT NULL COMMENT '备注',
  `password_last_change_day` char(8) DEFAULT NULL COMMENT '密码最后修改日期',
  `online_status` char(1) DEFAULT NULL COMMENT '在线状态 0-在线 1-下线',
  `status` char(1) DEFAULT NULL COMMENT '数据状态 0-正常 1-已注销',
  `verif_code` varchar(10) DEFAULT NULL COMMENT '验证码',
  `check_pwd` char(1) DEFAULT NULL COMMENT '0-需要校验登录密码 1-不需要校验登录密码',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `pk_tb_user_index4` (`user_name`),
  KEY `pk_tb_user_index1` (`openid`),
  KEY `pk_tb_user_index2` (`user_phone`),
  KEY `pk_tb_user_index3` (`user_email`)
) ENGINE=InnoDB AUTO_INCREMENT=178 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_user1
-- ----------------------------
DROP TABLE IF EXISTS `tb_user1`;
CREATE TABLE `tb_user1` (
  `USER_ID` char(32) NOT NULL,
  `USER_NAME` varchar(64) DEFAULT NULL,
  `USER_PASSWORD` varchar(32) DEFAULT NULL,
  `USER_EMAIL` varchar(64) DEFAULT NULL,
  `USER_PHONE` varchar(20) DEFAULT NULL,
  `REGISTERED_TIME` char(14) DEFAULT NULL,
  `REMARKS` varchar(512) DEFAULT NULL,
  `PASSWORD_LAST_CHANGE_DAY` char(8) DEFAULT NULL,
  `DATA_STATUS` varchar(2) DEFAULT NULL,
  `DATA_ADD_DATE` char(8) DEFAULT NULL,
  `DATA_ADD_TIME` char(14) DEFAULT NULL,
  `DATA_MOD_DATE` char(8) DEFAULT NULL,
  `DATA_MOD_TIME` char(14) DEFAULT NULL,
  `VERIF_CODE` varchar(10) DEFAULT NULL,
  `RSV1` varchar(180) DEFAULT NULL,
  `RSV2` varchar(180) DEFAULT NULL,
  `RSV3` varchar(180) DEFAULT NULL,
  `RSV4` varchar(180) DEFAULT NULL,
  `RSV5` varchar(180) DEFAULT NULL,
  `OPENID` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`USER_ID`),
  KEY `pk_tb_user_openid` (`OPENID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;
