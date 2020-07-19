/*
Navicat MySQL Data Transfer

Source Server         : mbiger
Source Server Version : 80016
Source Host           : 127.0.0.1:3306
Source Database       : huawuxiangmu_test

Target Server Type    : MYSQL
Target Server Version : 80016
File Encoding         : 65001

Date: 2020-07-19 21:33:54
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for product_other_attach_file
-- ----------------------------
DROP TABLE IF EXISTS `product_other_attach_file`;
CREATE TABLE `product_other_attach_file` (
  `ID` varchar(50) NOT NULL,
  `PRODUCT_ID` varchar(50) NOT NULL,
  `TYPE` varchar(50) DEFAULT NULL,
  `ATTACH_FILE` varchar(4000) DEFAULT NULL,
  `REMARK` varchar(500) DEFAULT NULL,
  `DATA_STATUS` char(1) NOT NULL,
  `CREATE_TIME` varchar(19) NOT NULL,
  `CREATOR_ID` varchar(50) NOT NULL,
  `CREATOR_NAME` varchar(50) NOT NULL,
  `EDIT_TIME` varchar(19) DEFAULT NULL,
  `EDITOR_ID` varchar(50) DEFAULT NULL,
  `EDITOR_NAME` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
