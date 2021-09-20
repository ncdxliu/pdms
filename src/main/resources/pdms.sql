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

CREATE TABLE `tb_login_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键序号',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `user_name` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户姓名',
  `login_time` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '登录时间yyyy-MM-dd HH:mm:ss',
  `ip_address` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '登录IP',
  `ip_address_parse` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '登录IP地址归属',
  `remark` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SET FOREIGN_KEY_CHECKS = 1;
