/*
 Navicat Premium Data Transfer

 Source Server         : 47.108.139.22
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : 47.108.139.22:3306
 Source Schema         : WsMS

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 03/05/2022 16:17:21
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `admin_id` bigint NOT NULL AUTO_INCREMENT,
  `admin_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`admin_id`),
  UNIQUE KEY `unique_admin_name` (`admin_name`) USING BTREE COMMENT '管理员名不能重复'
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of admin
-- ----------------------------
BEGIN;
INSERT INTO `admin` VALUES (9, 'admin', 'admin');
INSERT INTO `admin` VALUES (14, 'admin7', 'admin7');
INSERT INTO `admin` VALUES (15, 'admin8', 'admin8');
INSERT INTO `admin` VALUES (16, 'admin9', 'admin9');
INSERT INTO `admin` VALUES (17, 'admin5', 'admin5');
INSERT INTO `admin` VALUES (18, 'admin10', 'admin10');
INSERT INTO `admin` VALUES (19, 'admin1', 'admin1');
COMMIT;

-- ----------------------------
-- Table structure for group
-- ----------------------------
DROP TABLE IF EXISTS `group`;
CREATE TABLE `group` (
  `group_id` bigint NOT NULL AUTO_INCREMENT,
  `group_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `max_count` int NOT NULL,
  `admin_id` bigint DEFAULT NULL,
  `creator_id` bigint DEFAULT NULL,
  `is_admin_created` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`group_id`),
  UNIQUE KEY `unique_group_name` (`group_name`) USING BTREE COMMENT '组名不能重复'
) ENGINE=InnoDB AUTO_INCREMENT=148 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of group
-- ----------------------------
BEGIN;
INSERT INTO `group` VALUES (91, 'hx3', 12, 14, NULL, 1);
INSERT INTO `group` VALUES (147, 'GroupWang', 20, 9, 134, 0);
COMMIT;

-- ----------------------------
-- Table structure for group_admin
-- ----------------------------
DROP TABLE IF EXISTS `group_admin`;
CREATE TABLE `group_admin` (
  `group_id` bigint NOT NULL,
  `admin_id` bigint NOT NULL,
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of group_admin
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for public_group
-- ----------------------------
DROP TABLE IF EXISTS `public_group`;
CREATE TABLE `public_group` (
  `group_id` bigint NOT NULL AUTO_INCREMENT,
  `group_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `max_count` int NOT NULL,
  `is_admin_created` tinyint NOT NULL,
  `admin_id` bigint DEFAULT NULL,
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of public_group
-- ----------------------------
BEGIN;
INSERT INTO `public_group` VALUES (25, 'pg15', 10, 1, 16);
INSERT INTO `public_group` VALUES (26, 'whlpublicgroup', 10, 1, 9);
COMMIT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `show_status` tinyint NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `unique_user_name` (`user_name`) USING BTREE COMMENT '用户名不能重复'
) ENGINE=InnoDB AUTO_INCREMENT=136 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES (121, 'hxy1', 'hxy1', 0);
INSERT INTO `user` VALUES (123, 'xyh', 'xyh', 0);
INSERT INTO `user` VALUES (124, '12321', '12321', 0);
INSERT INTO `user` VALUES (125, '4-14-1', '123', 0);
INSERT INTO `user` VALUES (126, '14-1', '123', 0);
INSERT INTO `user` VALUES (127, '14-2', '123', 0);
INSERT INTO `user` VALUES (128, '4-15', '123', 0);
INSERT INTO `user` VALUES (130, 'mt', '123', 0);
INSERT INTO `user` VALUES (131, 'mt2', '123', 0);
INSERT INTO `user` VALUES (132, '418', '123', 0);
INSERT INTO `user` VALUES (133, 'hxy', 'hxy', 0);
INSERT INTO `user` VALUES (134, 'wanghanlin', '123', 0);
INSERT INTO `user` VALUES (135, 'test4-27', '123', 0);
COMMIT;

-- ----------------------------
-- Table structure for user_admin
-- ----------------------------
DROP TABLE IF EXISTS `user_admin`;
CREATE TABLE `user_admin` (
  `user_id` bigint NOT NULL COMMENT '用户id',
  `admin_id` bigint DEFAULT NULL COMMENT '管理员id',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of user_admin
-- ----------------------------
BEGIN;
INSERT INTO `user_admin` VALUES (121, 9);
INSERT INTO `user_admin` VALUES (123, 9);
INSERT INTO `user_admin` VALUES (124, 9);
INSERT INTO `user_admin` VALUES (125, 16);
INSERT INTO `user_admin` VALUES (126, 16);
INSERT INTO `user_admin` VALUES (127, 16);
INSERT INTO `user_admin` VALUES (128, 16);
INSERT INTO `user_admin` VALUES (130, 16);
INSERT INTO `user_admin` VALUES (131, 16);
INSERT INTO `user_admin` VALUES (132, 9);
INSERT INTO `user_admin` VALUES (133, 14);
INSERT INTO `user_admin` VALUES (134, 9);
INSERT INTO `user_admin` VALUES (135, 9);
COMMIT;

-- ----------------------------
-- Table structure for user_group
-- ----------------------------
DROP TABLE IF EXISTS `user_group`;
CREATE TABLE `user_group` (
  `user_id` bigint NOT NULL,
  `group_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of user_group
-- ----------------------------
BEGIN;
INSERT INTO `user_group` VALUES (135, 147);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
