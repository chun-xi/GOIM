/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50525
Source Host           : localhost:3306
Source Database       : markim

Target Server Type    : MYSQL
Target Server Version : 50525
File Encoding         : 65001

Date: 2019-05-24 02:10:37
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `groups`
-- ----------------------------
DROP TABLE IF EXISTS `groups`;
CREATE TABLE `groups` (
  `id` bigint(8) NOT NULL AUTO_INCREMENT COMMENT '群id',
  `user_id` bigint(8) NOT NULL COMMENT '群主ID',
  `group_name` varchar(32) NOT NULL COMMENT '群名',
  `group_avatar` varchar(128) NOT NULL COMMENT '群头像',
  `group_sign` varchar(125) NOT NULL COMMENT '群签名(简介)',
  `group_status` int(4) NOT NULL DEFAULT '1' COMMENT '群状态 1:启用 0:禁言',
  `create_time` datetime NOT NULL COMMENT '群创建时间',
  `updae_time` datetime DEFAULT NULL COMMENT '群更新时间',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `groups_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of groups
-- ----------------------------

-- ----------------------------
-- Table structure for `group_users`
-- ----------------------------
DROP TABLE IF EXISTS `group_users`;
CREATE TABLE `group_users` (
  `id` bigint(8) NOT NULL AUTO_INCREMENT,
  `group_id` bigint(8) NOT NULL COMMENT '群Id',
  `user_id` bigint(8) NOT NULL COMMENT '用户ID',
  `join_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '加入时间',
  PRIMARY KEY (`id`),
  KEY `group_id` (`group_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `group_users_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `group_users_ibfk_1` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of group_users
-- ----------------------------

-- ----------------------------
-- Table structure for `users`
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` bigint(8) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `user_phone` varchar(32) DEFAULT NULL COMMENT '用户手机号',
  `user_pwd` varchar(64) DEFAULT NULL COMMENT '用户密码',
  `user_avatar` varchar(128) DEFAULT NULL COMMENT '用户头像',
  `user_name` varchar(32) DEFAULT NULL COMMENT '用户名',
  `user_remark` varchar(32) DEFAULT '无' COMMENT '用户备注',
  `user_sign` varchar(128) NOT NULL DEFAULT '' COMMENT '用户签名',
  `user_sex` int(4) DEFAULT '0' COMMENT '性别 1:男 0:女',
  `user_state` int(4) DEFAULT '0' COMMENT '用户状态 0:不可用 1:可用',
  `is_vip` int(4) NOT NULL DEFAULT '0' COMMENT '是否vip 0:不是 1:是',
  `create_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('1', '13055951014', '9a40bf57f7c5c6d317ac689dfd359f89', '/static/imgs/head.png', '王德发', '无', '一个帅字就概括了一生!', '1', '0', '0', '2019-05-24 00:17:57', '2019-05-24 00:58:05');

-- ----------------------------
-- Table structure for `user_friends`
-- ----------------------------
DROP TABLE IF EXISTS `user_friends`;
CREATE TABLE `user_friends` (
  `id` bigint(8) NOT NULL AUTO_INCREMENT COMMENT '朋友关系ID',
  `friend_id` bigint(8) NOT NULL COMMENT '朋友的user_id',
  `user_id` bigint(8) NOT NULL COMMENT '自己user_id',
  `user_group_id` bigint(8) NOT NULL COMMENT '添加分组id',
  `remark` varchar(32) DEFAULT NULL COMMENT '用户备注',
  `create_time` datetime NOT NULL COMMENT '添加时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `user_group_id` (`user_group_id`),
  KEY `friend_id` (`friend_id`),
  CONSTRAINT `user_friends_ibfk_3` FOREIGN KEY (`friend_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_friends_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_friends_ibfk_2` FOREIGN KEY (`user_group_id`) REFERENCES `user_group` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_friends
-- ----------------------------

-- ----------------------------
-- Table structure for `user_group`
-- ----------------------------
DROP TABLE IF EXISTS `user_group`;
CREATE TABLE `user_group` (
  `id` bigint(8) NOT NULL AUTO_INCREMENT COMMENT '分组ID',
  `user_id` bigint(8) NOT NULL COMMENT '用户ID',
  `user_group_name` varchar(32) NOT NULL COMMENT '分组名称',
  `create_time` datetime NOT NULL COMMENT '分组创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '分组更新时间',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `user_group_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_group
-- ----------------------------
