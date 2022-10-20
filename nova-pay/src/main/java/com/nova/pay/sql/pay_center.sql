/*
 Navicat Premium Data Transfer

 Source Server         : xxl
 Source Server Type    : MySQL
 Source Server Version : 50719 (5.7.19-1-log)
 Source Host           : 172.30.0.143:3306
 Source Schema         : pay_center

 Target Server Type    : MySQL
 Target Server Version : 50719 (5.7.19-1-log)
 File Encoding         : 65001

 Date: 20/10/2022 15:13:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for fk_pay_config
-- ----------------------------
DROP TABLE IF EXISTS `fk_pay_config`;
CREATE TABLE `fk_pay_config` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT,
  `pay_type` varchar(8) DEFAULT NULL COMMENT '体育红单支付类型：对应老红单uc_paytype_info表',
  `source` varchar(128) DEFAULT NULL,
  `sid` varchar(4096) DEFAULT NULL COMMENT '渠道号',
  `pay_way` tinyint(2) DEFAULT NULL COMMENT '1支付宝 2微信 3苹果 4易宝 5谷歌支付 99兑换 ',
  `app_id` varchar(32) DEFAULT NULL COMMENT '微信appId/支付宝appId/易宝appKey/谷歌clientId\n',
  `app_secret` varchar(255) DEFAULT NULL COMMENT '微信应用秘钥/谷歌应用秘钥(clientSecret)\n',
  `mch_id` varchar(32) DEFAULT NULL COMMENT '微信商户号/易宝商户编号/谷歌account_id',
  `pay_secret` varchar(255) DEFAULT NULL COMMENT '微信支付秘钥',
  `serial_no` varchar(64) DEFAULT NULL COMMENT '证书号',
  `api_v3_key` varchar(64) DEFAULT NULL COMMENT '商户支付v3key，有值微信v3支付，空就是v2支付',
  `public_key` varchar(512) DEFAULT NULL COMMENT '支付宝公钥/易宝公钥',
  `private_key` varchar(2048) DEFAULT NULL COMMENT '支付宝应用私钥/易宝平台私钥',
  `key_path` varchar(128) DEFAULT NULL COMMENT '微信p12证书地址/支付宝appCertPublicKey.crt/谷歌p12证书\n',
  `private_key_path` varchar(128) DEFAULT NULL COMMENT '微信v3支付apiclient_key.pem证书地址/支付宝alipayCertPublicKey_RSA2.crt证书地址',
  `private_cert_path` varchar(128) DEFAULT NULL COMMENT '微信v3支付apiclient_cert.pem证书地址/支付宝alipayRootCert.crt证书地址',
  `notify_url` varchar(64) DEFAULT NULL COMMENT '通知地址',
  `weight` int(8) NOT NULL DEFAULT '1' COMMENT '权重',
  `status` int(1) DEFAULT '0' COMMENT '是否生效  0不生效 1生效  -1 作废',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  `operator` varchar(64) DEFAULT NULL COMMENT '操作人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `PAY_CONFIG_INDX01` (`source`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='支付配置表';

-- ----------------------------
-- Table structure for fk_pay_list
-- ----------------------------
DROP TABLE IF EXISTS `fk_pay_list`;
CREATE TABLE `fk_pay_list` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT,
  `source` varchar(64) DEFAULT NULL,
  `sid` varchar(64) NOT NULL COMMENT 'sid',
  `ali_h5` tinyint(2) NOT NULL DEFAULT '0' COMMENT '支付宝h5 0关闭 1显示 备注：支付宝h5和app都走h5支付',
  `ali_app` tinyint(2) NOT NULL DEFAULT '0' COMMENT '支付宝app 0关闭 1显示',
  `ali_applet` tinyint(2) NOT NULL DEFAULT '0' COMMENT '支付宝小程序 0关闭 1显示',
  `ali_logo_url` varchar(128) DEFAULT NULL COMMENT '支付宝logo',
  `wechat_h5` tinyint(2) NOT NULL DEFAULT '0' COMMENT '微信h5 0关闭 1显示',
  `wechat_app` tinyint(2) NOT NULL DEFAULT '0' COMMENT '微信客户端 0关闭 1显示',
  `wechat_jsapi` tinyint(2) NOT NULL DEFAULT '0' COMMENT '微信内原生、小程序 0关闭 1显示',
  `wechat_logo_url` varchar(128) DEFAULT NULL COMMENT '微信logo',
  `apple` tinyint(2) NOT NULL DEFAULT '0' COMMENT '苹果支付 0关闭 1显示',
  `apple_logo_url` varchar(128) DEFAULT NULL COMMENT '苹果logo',
  `yee_pay_quick` tinyint(2) NOT NULL DEFAULT '0' COMMENT '易宝快捷 0关闭 1显示',
  `yee_pay_wallet` tinyint(2) NOT NULL DEFAULT '0' COMMENT '易宝钱包 0关闭 1显示',
  `yee_pay_logo_url` varchar(128) DEFAULT NULL COMMENT '易宝logo',
  `ball_coin` tinyint(2) NOT NULL DEFAULT '0' COMMENT '球币兑换 0关闭 1显示',
  `ball_coin_logo_url` varchar(128) DEFAULT NULL COMMENT '球币兑换logo',
  `google_pay` tinyint(2) NOT NULL DEFAULT '0' COMMENT '谷歌支付',
  `google_logo_url` varchar(128) DEFAULT NULL COMMENT '谷歌支付logo',
  `operator` varchar(64) DEFAULT NULL COMMENT '操作人',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `UNION_KEY` (`source`,`sid`) USING BTREE,
  KEY `PAY_LIST_INDX01` (`source`),
  KEY `PAY_LIST_INDX02` (`sid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='支付列表';

-- ----------------------------
-- Table structure for fk_pay_order
-- ----------------------------
DROP TABLE IF EXISTS `fk_pay_order`;
CREATE TABLE `fk_pay_order` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT,
  `source` varchar(64) DEFAULT NULL,
  `sid` varchar(64) DEFAULT NULL COMMENT '渠道号',
  `pay_config_id` bigint(19) DEFAULT NULL COMMENT '支付配置表id',
  `product_id` varchar(64) DEFAULT NULL COMMENT '产品id',
  `user_name` varchar(64) DEFAULT NULL COMMENT '用户名',
  `order_id` varchar(64) NOT NULL COMMENT '订单id、聚合订单id(nt_pay_master_order)',
  `trade_no` varchar(64) DEFAULT NULL COMMENT '交易订单号 支付宝同字段、微信和苹果：transaction_id',
  `trade_status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '0默认，1成功，2失败，3退款, 4处理中',
  `fee` decimal(10,2) DEFAULT '0.00' COMMENT '价格 单位元',
  `type` tinyint(2) NOT NULL COMMENT '1h5 2小程序 3app 4微信原生jsapi 5沙盒 6钱包 7快捷 8球币兑换',
  `pay_way` tinyint(2) NOT NULL COMMENT '1支付宝 2微信 3苹果 4yeePay',
  `sign` blob COMMENT '苹果支付 sign、易宝支付token',
  `remark` varchar(64) DEFAULT NULL COMMENT '备注\n1.易宝1级收款商户\n2.易宝2级默认收款方式 银行卡、钱包\n3.苹果支付验签',
  `operator` varchar(64) DEFAULT NULL COMMENT '操作人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `business_code` tinyint(2) NOT NULL DEFAULT '0' COMMENT '对应BusinessEnum枚举类\n0:默认\n1:充值\n2:包月会员\n3:包季会员\n4:包年会员\n5:连续包月会员\n6:连续包季会员\n7:连续包年会员\n8:订阅1周\n9:订阅2周\n10:订阅1个月\n11:套餐卡周\n12:套餐卡月\n13:购买方案\n14:开通尊享白银会员\n15:开通尊享黄金会员\n16:尊享会员升级\n17:大数据\n18:红单数据模型\n19:赛事锦囊',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNION_KEY` (`order_id`,`pay_way`) USING BTREE COMMENT '订单id、支付方式联合索引',
  KEY `PAY_ORDER_INDX01` (`trade_status`) USING BTREE,
  KEY `PAY_ORDER_INDX02` (`user_name`) USING BTREE,
  KEY `PAY_ORDER_INDX03` (`trade_no`) USING BTREE,
  KEY `PAY_ORDER_INDX04` (`create_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='支付订单表明细表';

SET FOREIGN_KEY_CHECKS = 1;
