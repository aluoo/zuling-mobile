/*
 Navicat MySQL Dump SQL

 Source Server         : 47.98.124.98
 Source Server Type    : MySQL
 Source Server Version : 50740 (5.7.40)
 Source Host           : 47.98.124.98:3306
 Source Schema         : mobile

 Target Server Type    : MySQL
 Target Server Version : 50740 (5.7.40)
 File Encoding         : 65001

 Date: 04/01/2026 16:49:17
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for address
-- ----------------------------
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address`  (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `emp_id` bigint(20) NULL DEFAULT NULL COMMENT '员工ID',
  `address` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
  `detail` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '门牌号',
  `contact` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人',
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人电话',
  `creator` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `updator` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_emp_id`(`emp_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '收货地址' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cms_article
-- ----------------------------
DROP TABLE IF EXISTS `cms_article`;
CREATE TABLE `cms_article`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `publish_time` datetime NULL DEFAULT NULL COMMENT '发布时间',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `activated` tinyint(1) NULL DEFAULT 0 COMMENT '是否显示 0隐藏 1显示',
  `sort` int(11) NULL DEFAULT 0 COMMENT '排序',
  `category_id` bigint(20) NULL DEFAULT NULL COMMENT '分类ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `sub_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '副标题',
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封面',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `is_popular` tinyint(1) NULL DEFAULT 0 COMMENT '是否热门',
  `is_top` tinyint(1) NULL DEFAULT 0 COMMENT '是否置顶',
  `views` bigint(11) UNSIGNED NULL DEFAULT 0 COMMENT '阅读数',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sort`(`sort`) USING BTREE,
  INDEX `idx_publish_time`(`publish_time`) USING BTREE,
  INDEX `idx_activated`(`activated`) USING BTREE,
  INDEX `idx_category_id`(`category_id`) USING BTREE,
  INDEX `idx_is_popular`(`is_popular`) USING BTREE,
  INDEX `idx_is_top`(`is_top`) USING BTREE,
  INDEX `idx_views`(`views`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1240362579256946690 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '内容管理-文章表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cms_category
-- ----------------------------
DROP TABLE IF EXISTS `cms_category`;
CREATE TABLE `cms_category`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `activated` tinyint(1) NULL DEFAULT 0 COMMENT '是否显示 0隐藏 1显示',
  `sort` int(11) NULL DEFAULT 0 COMMENT '排序',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型',
  `biz_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '上级分类ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类名称',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类图标',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类描述',
  `ancestors` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '层级路径',
  `level` int(11) NULL DEFAULT 0 COMMENT '层级深度',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sort`(`sort`) USING BTREE,
  INDEX `idx_activated`(`activated`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id`) USING BTREE,
  INDEX `idx_type`(`type`) USING BTREE,
  INDEX `idx_biz_type`(`biz_type`) USING BTREE,
  INDEX `idx_level`(`level`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '内容管理-分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cms_index_icon
-- ----------------------------
DROP TABLE IF EXISTS `cms_index_icon`;
CREATE TABLE `cms_index_icon`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `activated` tinyint(1) NULL DEFAULT 0 COMMENT '是否显示 0隐藏 1显示',
  `sort` int(11) NULL DEFAULT 0 COMMENT '排序',
  `place` int(5) NULL DEFAULT NULL COMMENT '展示位置',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图标',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `link_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跳转链接',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for commission_plan
-- ----------------------------
DROP TABLE IF EXISTS `commission_plan`;
CREATE TABLE `commission_plan`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '方案名称(可为空)',
  `type_id` bigint(20) NULL DEFAULT NULL COMMENT '方案类型ID',
  `source_type` tinyint(2) NULL DEFAULT NULL COMMENT '创建来源(0-后台、1-app)',
  `employee_id` bigint(20) NULL DEFAULT NULL COMMENT '创建员工id',
  `sys_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建后台人员id',
  `level` int(11) NULL DEFAULT NULL COMMENT '层级(从0开始)',
  `is_leaf` tinyint(1) NULL DEFAULT NULL COMMENT '是否叶子节点(0-不是,1-是)',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 109 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '佣金方案表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for commission_plan_conf
-- ----------------------------
DROP TABLE IF EXISTS `commission_plan_conf`;
CREATE TABLE `commission_plan_conf`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `plan_id` bigint(20) NULL DEFAULT NULL COMMENT '方案id',
  `type_package_id` bigint(20) NULL DEFAULT NULL COMMENT '套餐id',
  `super_divide` bigint(20) NULL DEFAULT NULL COMMENT '上级分成',
  `child_divide` bigint(20) NULL DEFAULT NULL COMMENT '下级分成',
  `self_divide` bigint(20) NULL DEFAULT NULL COMMENT '自收分成',
  `super_scale` decimal(6, 4) NULL DEFAULT NULL COMMENT '上级分成比例',
  `child_scale` decimal(6, 4) NULL DEFAULT NULL COMMENT '下级分成比例',
  `self_scale` decimal(6, 4) NULL DEFAULT NULL COMMENT '自收分成比例',
  `ancestors` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组织层级编码',
  `level` int(11) NULL DEFAULT NULL COMMENT '层级',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 295 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '发行佣金配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for commission_plan_log
-- ----------------------------
DROP TABLE IF EXISTS `commission_plan_log`;
CREATE TABLE `commission_plan_log`  (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `plan_id` bigint(20) NOT NULL COMMENT '方案id',
  `employee_id` bigint(20) NOT NULL COMMENT '创建人id',
  `content` json NOT NULL COMMENT '方案内容（方案，成员，配置）',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 455 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分佣方案记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for commission_plan_members
-- ----------------------------
DROP TABLE IF EXISTS `commission_plan_members`;
CREATE TABLE `commission_plan_members`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `plan_id` bigint(20) NULL DEFAULT NULL COMMENT '方案id',
  `type_id` bigint(20) NULL DEFAULT NULL COMMENT '佣金类型ID',
  `child_employee_id` bigint(20) NULL DEFAULT NULL COMMENT '下级员工id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 357 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for commission_settle
-- ----------------------------
DROP TABLE IF EXISTS `commission_settle`;
CREATE TABLE `commission_settle`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `employee_id` bigint(20) NULL DEFAULT NULL COMMENT '员工id',
  `batch_no` bigint(20) NULL DEFAULT NULL COMMENT '结算批次号',
  `commission_type` bigint(20) NULL DEFAULT NULL COMMENT '分佣方案ID',
  `commission_package` bigint(20) NULL DEFAULT NULL COMMENT '分佣套餐ID',
  `settle_balance` int(11) NULL DEFAULT NULL COMMENT '结算金额（分）',
  `settle_status` int(11) NULL DEFAULT NULL COMMENT '结算状态(0-不结算、1-待结算、2-已结算)',
  `correlation_id` bigint(20) NULL DEFAULT NULL COMMENT '结算关联扩展id',
  `gain_type` tinyint(2) NULL DEFAULT NULL COMMENT '获得类型(1-直接做单获得,2-下级贡献)',
  `gain_time` datetime NULL DEFAULT NULL COMMENT '获得时间',
  `child_employee_id` bigint(20) NULL DEFAULT NULL COMMENT '佣金来源的员工id',
  `settle_time` datetime NULL DEFAULT NULL COMMENT '结算时间',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '结算说明',
  `level` int(11) NULL DEFAULT NULL COMMENT '层级',
  `ancestors` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组织层级编码',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1260914366895755566 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统结算单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for commission_settle_check
-- ----------------------------
DROP TABLE IF EXISTS `commission_settle_check`;
CREATE TABLE `commission_settle_check`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `commission_type` bigint(20) NULL DEFAULT NULL COMMENT '分佣方案ID',
  `commission_package` bigint(20) NULL DEFAULT NULL COMMENT '分佣套餐ID',
  `bd_id` bigint(20) NULL DEFAULT NULL COMMENT '合伙人ID',
  `region_id` bigint(20) NULL DEFAULT NULL COMMENT '区域经理ID',
  `settle_balance` int(11) NULL DEFAULT NULL COMMENT '合伙人区域经理金额',
  `pay_amount` int(11) NULL DEFAULT NULL COMMENT '门店金额',
  `all_amount` int(11) NULL DEFAULT NULL COMMENT '总金额',
  `correlation_id` bigint(20) NULL DEFAULT NULL COMMENT '结算关联扩展id',
  `settle_time` datetime NULL DEFAULT NULL COMMENT '结算时间',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '结算说明',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1803727735166648396 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '合伙人对账单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for commission_settle_data_daily_base
-- ----------------------------
DROP TABLE IF EXISTS `commission_settle_data_daily_base`;
CREATE TABLE `commission_settle_data_daily_base`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `employee_id` bigint(20) NULL DEFAULT NULL COMMENT '员工id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `day` datetime NULL DEFAULT NULL COMMENT '每天0点',
  `start_time` datetime(3) NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime(3) NULL DEFAULT NULL COMMENT '结束时间',
  `biz_type` tinyint(2) NULL DEFAULT NULL COMMENT '结算业务类型(1-推广、2-服务费、3-奖金)',
  `gain_type` tinyint(2) NULL DEFAULT NULL COMMENT '获得类型(1-直接做单获得,2-下级贡献,3-吞并下级获得)',
  `value` int(11) NULL DEFAULT 0 COMMENT '统计金额（分）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_employee_id`(`employee_id`) USING BTREE,
  INDEX `idx_biz_type`(`biz_type`) USING BTREE,
  INDEX `idx_gain_type`(`gain_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 253 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '佣金统计每日基础表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for commission_settle_data_daily_detail
-- ----------------------------
DROP TABLE IF EXISTS `commission_settle_data_daily_detail`;
CREATE TABLE `commission_settle_data_daily_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `employee_id` bigint(20) NULL DEFAULT NULL COMMENT '员工id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `day` datetime NULL DEFAULT NULL COMMENT '每天0点',
  `biz_type` tinyint(2) NULL DEFAULT NULL COMMENT '结算业务类型(1-推广、2-服务费、3-奖金)',
  `gain_type` tinyint(2) NULL DEFAULT NULL COMMENT '获得类型(1-直接做单获得,2-下级贡献,3-吞并下级获得)',
  `value` int(11) NULL DEFAULT 0 COMMENT '统计金额（分）',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '结算说明',
  `daily_base_id` bigint(20) NULL DEFAULT NULL COMMENT '佣金统计每日基础表ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_employee_id`(`employee_id`) USING BTREE,
  INDEX `idx_biz_type`(`biz_type`) USING BTREE,
  INDEX `idx_gain_type`(`gain_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 418 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '佣金统计每日基础详情表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for commission_settle_data_daily_total
-- ----------------------------
DROP TABLE IF EXISTS `commission_settle_data_daily_total`;
CREATE TABLE `commission_settle_data_daily_total`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `employee_id` bigint(20) NULL DEFAULT NULL COMMENT '员工id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `day` datetime NULL DEFAULT NULL COMMENT '每天0点',
  `value` int(11) NULL DEFAULT 0 COMMENT '统计金额（分）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_employee_id`(`employee_id`) USING BTREE,
  INDEX `idx_day`(`day`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 231 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '佣金统计每日总表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for commission_type
-- ----------------------------
DROP TABLE IF EXISTS `commission_type`;
CREATE TABLE `commission_type`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(55) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分佣类型名称',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除 0否1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分佣类型名称表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for commission_type_package
-- ----------------------------
DROP TABLE IF EXISTS `commission_type_package`;
CREATE TABLE `commission_type_package`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(55) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '方案套餐名称',
  `type_id` bigint(20) NULL DEFAULT NULL COMMENT '方案类型ID',
  `type` tinyint(5) NULL DEFAULT NULL COMMENT '1数值2比例',
  `max_commission_fee` int(11) NULL DEFAULT NULL COMMENT '最大推广分佣费限制(单位：分)',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除 0否1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分佣类型名称表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for company
-- ----------------------------
DROP TABLE IF EXISTS `company`;
CREATE TABLE `company`  (
  `id` bigint(20) NOT NULL COMMENT 'ID，蓝海ID=100000000000000000',
  `p_id` bigint(20) NULL DEFAULT NULL COMMENT '上级公司id,-1表示最上层公司',
  `type` int(11) NULL DEFAULT NULL COMMENT '1-公司,2-门店3连锁4服务商5租机商',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '门店名称',
  `code` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公司code',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态（1-待审核， 2-正常， 3-审核失败， 4-注销，5-下线）',
  `contact` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '负责人',
  `contact_mobile` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人电话',
  `province` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '省',
  `city` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '市',
  `region` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '详细地址',
  `province_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '省编码',
  `city_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '市编码',
  `region_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区编码',
  `front_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '门店照片',
  `bus_license` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '营业执照',
  `id_url_up` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证照片-正面',
  `id_url_down` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证照片-反面',
  `id_card` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证',
  `id_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证名字',
  `apl_id` bigint(20) NULL DEFAULT NULL COMMENT '发起申请人ID',
  `employee_id` bigint(20) NULL DEFAULT NULL COMMENT '员工ID',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户表ID,关联OPENID用',
  `invoice_able` tinyint(1) NULL DEFAULT 0 COMMENT '是否可开票',
  `login_able` tinyint(1) NULL DEFAULT 0 COMMENT '是否可登录设置',
  `commission_able` tinyint(1) NULL DEFAULT 0 COMMENT '是否佣金系统',
  `p_dept_id` bigint(20) NULL DEFAULT NULL COMMENT '父部门ID',
  `exchange_type` tinyint(2) NULL DEFAULT 3 COMMENT '3换机4一键拉新',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `creator` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `updator` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `mbr_type` tinyint(2) NULL DEFAULT 0 COMMENT '租机门店结算0线下1线上',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_code`(`code`) USING BTREE,
  INDEX `fk_bt_p_id`(`p_id`) USING BTREE,
  INDEX `fk_bt_name`(`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '门店表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for company_data_daily_base
-- ----------------------------
DROP TABLE IF EXISTS `company_data_daily_base`;
CREATE TABLE `company_data_daily_base`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `trans_num` int(10) NULL DEFAULT 0 COMMENT '交易数量',
  `order_num` int(10) NULL DEFAULT 0 COMMENT '询价数量',
  `price_num` int(10) NULL DEFAULT 0 COMMENT '报价数量',
  `final_price_amount` bigint(11) NULL DEFAULT 0 COMMENT '成交金额',
  `commission_amount` bigint(11) NULL DEFAULT 0 COMMENT '收益金额',
  `cancel_num` int(10) NULL DEFAULT 0 COMMENT '取消订单数',
  `overtime_num` int(10) NULL DEFAULT 0 COMMENT '作废订单数',
  `exchange_num` int(10) NULL DEFAULT 0 COMMENT '拉新晒单数目',
  `exchange_pass_num` int(10) NULL DEFAULT 0 COMMENT '拉新晒单成功数',
  `insurance_any_num` int(10) NULL DEFAULT 0 COMMENT '数保全保投保数目',
  `insurance_sp_num` int(10) NULL DEFAULT 0 COMMENT '数保碎屏投保数目',
  `insurance_yb_num` int(10) NULL DEFAULT 0 COMMENT '数保延长保数目',
  `insurance_care_num` int(10) NULL DEFAULT 0 COMMENT '数保CARE投保数目',
  `insurance_az_num` int(10) NULL DEFAULT 0 COMMENT '数保安卓终身保投保数目',
  `employee_id` bigint(20) NULL DEFAULT NULL COMMENT '员工ID',
  `day` datetime NULL DEFAULT NULL COMMENT '每天0点',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '门店统计日看板表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dept
-- ----------------------------
DROP TABLE IF EXISTS `dept`;
CREATE TABLE `dept`  (
  `id` bigint(20) NOT NULL COMMENT 'ID,蓝海管理部门=100000000000000000',
  `company_id` bigint(20) NULL DEFAULT NULL COMMENT '公司ID',
  `company_type` int(11) NULL DEFAULT NULL COMMENT '1-公司,2-渠道',
  `p_dept_id` bigint(20) NULL DEFAULT NULL COMMENT '上级部门id, -1表示直属公司',
  `status` int(11) NULL DEFAULT 1 COMMENT '状态 1正常 2注销 3下线',
  `type` int(11) NULL DEFAULT NULL COMMENT '1-管理部门2-普通部门',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `code` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门code',
  `creator` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `updator` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `fk_uq_code`(`code`) USING BTREE,
  INDEX `fk_bt_pid`(`p_dept_id`) USING BTREE,
  INDEX `fk_bt_com_id`(`company_id`) USING BTREE,
  INDEX `fk_bt_name`(`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '组织（公司，渠道）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for di_company_account
-- ----------------------------
DROP TABLE IF EXISTS `di_company_account`;
CREATE TABLE `di_company_account`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `company_id` bigint(20) NULL DEFAULT NULL COMMENT '门店ID',
  `able_balance` bigint(20) NULL DEFAULT 0 COMMENT '钱包可用余额(分)',
  `temp_frozen_balance` bigint(20) NULL DEFAULT 0 COMMENT '钱包临时冻结金额(分)',
  `frozen_balance` bigint(20) NULL DEFAULT 0 COMMENT '钱包永久冻结金额(分)',
  `accumulate_income` bigint(20) NULL DEFAULT 0 COMMENT '钱包累计入账(分)',
  `acc_award_income` bigint(20) NULL DEFAULT 0 COMMENT '奖金累计收入',
  `acc_withdraw` bigint(20) NULL DEFAULT 0 COMMENT '累计提现金额',
  `ancestors` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组织层级编码',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uqidx_employee_id`(`company_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1230935638644895755 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数保门店账户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for di_company_account_log
-- ----------------------------
DROP TABLE IF EXISTS `di_company_account_log`;
CREATE TABLE `di_company_account_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '说明',
  `company_account_id` bigint(20) NULL DEFAULT NULL COMMENT '员工id',
  `company_id` bigint(20) NULL DEFAULT NULL COMMENT '门店ID',
  `ancestors` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组织层级编码',
  `change_main_type` int(11) NULL DEFAULT NULL COMMENT '主变动类型(收入、支出、不变)',
  `change_detail_type` bigint(20) NULL DEFAULT NULL COMMENT '详细变动类型',
  `change_balance` bigint(20) NULL DEFAULT NULL COMMENT '变动金额',
  `able_balance_before` bigint(20) NULL DEFAULT NULL COMMENT '钱包可用余额(分)_变动前',
  `able_balance_change` bigint(20) NULL DEFAULT NULL COMMENT '钱包可用余额(分)_变动金额',
  `able_balance_after` bigint(20) NULL DEFAULT NULL COMMENT '钱包可用余额(分)_变动后',
  `temp_frozen_balance_before` bigint(20) NULL DEFAULT NULL COMMENT '钱包冻结金额(分)_变动前',
  `temp_frozen_balance_change` bigint(20) NULL DEFAULT NULL COMMENT '钱包冻结金额(分)_变动金额',
  `temp_frozen_balance_after` bigint(20) NULL DEFAULT NULL COMMENT '钱包冻结金额(分)_变动后',
  `frozen_balance_before` bigint(20) NULL DEFAULT NULL COMMENT '钱包永久冻结金额(分)_变动前',
  `frozen_balance_change` bigint(20) NULL DEFAULT NULL COMMENT '钱包永久冻结金额(分)_变动金额',
  `frozen_balance_after` bigint(20) NULL DEFAULT NULL COMMENT '钱包永久冻结金额(分)_变动后',
  `accumulate_income_before` bigint(20) NULL DEFAULT NULL COMMENT '钱包累计入账(分)_变动前',
  `accumulate_income_change` bigint(20) NULL DEFAULT NULL COMMENT '钱包累计入账(分)',
  `accumulate_income_after` bigint(20) NULL DEFAULT NULL COMMENT '钱包累计入账(分)',
  `acc_award_income_before` bigint(20) NULL DEFAULT 0 COMMENT '奖金累计收入-变动前',
  `acc_award_income_change` bigint(20) NULL DEFAULT 0 COMMENT '奖金累计收入-变动金额',
  `acc_award_income_after` bigint(20) NULL DEFAULT 0 COMMENT '奖金累计收入-变动后',
  `acc_withdraw_before` bigint(20) NULL DEFAULT 0 COMMENT '累计提现金额-变动前',
  `acc_withdraw_change` bigint(20) NULL DEFAULT 0 COMMENT '累计提现金额-变动金额',
  `acc_withdraw_after` bigint(20) NULL DEFAULT 0 COMMENT '累计提现金额-变动后',
  `correlation_id` bigint(20) NULL DEFAULT NULL COMMENT '扩展id',
  `remark` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '变动说明',
  `user_focus` tinyint(1) NULL DEFAULT NULL COMMENT '是否用户可见(0-否,1-是)',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 579 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数保门店账户变动明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for di_company_recharge_log
-- ----------------------------
DROP TABLE IF EXISTS `di_company_recharge_log`;
CREATE TABLE `di_company_recharge_log`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `company_id` bigint(20) NOT NULL COMMENT '门店ID',
  `recharge_amount` bigint(20) NULL DEFAULT NULL COMMENT '充值金额',
  `image_url` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '打款凭证',
  `status` tinyint(2) NULL DEFAULT 0 COMMENT '0待审核1拒绝2通过',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数保门店账户充值记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for di_insurance
-- ----------------------------
DROP TABLE IF EXISTS `di_insurance`;
CREATE TABLE `di_insurance`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `type_id` bigint(20) NULL DEFAULT NULL COMMENT '险种ID',
  `package_id` bigint(20) NULL DEFAULT NULL COMMENT '套餐ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '产品名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '产品描述',
  `period` int(5) NULL DEFAULT NULL COMMENT '年限',
  `status` tinyint(2) NULL DEFAULT NULL COMMENT '1正常2下线3删除',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '保险产品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for di_insurance_fix_order
-- ----------------------------
DROP TABLE IF EXISTS `di_insurance_fix_order`;
CREATE TABLE `di_insurance_fix_order`  (
  `id` bigint(20) NOT NULL,
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '保险单ID',
  `mobile` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报险用户手机',
  `status` int(5) NULL DEFAULT NULL COMMENT '订单状态',
  `service_type` bigint(20) NULL DEFAULT 1 COMMENT '关联di_option_id',
  `claim_item` bigint(20) NULL DEFAULT NULL COMMENT '关联di_option_id',
  `imei_read` tinyint(1) NULL DEFAULT 0 COMMENT '是否读取串号',
  `break_down` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机故障',
  `up_product` tinyint(2) NULL DEFAULT NULL COMMENT '迭代升级1同款同型2升级迭代',
  `settle_amount` int(11) NULL DEFAULT NULL COMMENT '理赔金额',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `fix_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '维修收款账号姓名',
  `fix_alipay` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '维修收款支付宝账号',
  `fix_bank_account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '维修银行卡号',
  `product_sku_id` bigint(20) NULL DEFAULT NULL COMMENT '迭代升级商品SKUID',
  `product_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '迭代升级手机商品名称',
  `product_spec` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '迭代升级手机规格名称',
  `product_sku_retail_price` int(11) NULL DEFAULT 0 COMMENT '迭代升级手机市场零售价',
  `new_product_sku_stall_price` int(11) NULL DEFAULT NULL COMMENT '新机档口价格',
  `old_sku_retail_price` int(11) NULL DEFAULT 0 COMMENT '旧机手机市场零售价',
  `supple_amount` int(11) NULL DEFAULT 0 COMMENT '补交金额',
  `discount_amount` int(11) NULL DEFAULT 0 COMMENT '折抵价格',
  `order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单编码',
  `fix_city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '维修城市',
  `store_employee_id` bigint(20) NULL DEFAULT NULL COMMENT '门店员工ID',
  `store_company_id` bigint(20) NULL DEFAULT NULL COMMENT '门店ID',
  `contact_mobile` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系人电话',
  `new_imei` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '换机新的IMEI',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数保报修订单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for di_insurance_fix_order_image
-- ----------------------------
DROP TABLE IF EXISTS `di_insurance_fix_order_image`;
CREATE TABLE `di_insurance_fix_order_image`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fix_order_id` bigint(20) NULL DEFAULT NULL COMMENT '数保报修订单ID',
  `image_url` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片地址',
  `type` tinyint(2) NULL DEFAULT NULL COMMENT '1.报险资料2换机补充材料3.理赔上传材料',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_insurance_id`(`fix_order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1826185455035023363 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数保报修订单选项表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for di_insurance_fix_order_option
-- ----------------------------
DROP TABLE IF EXISTS `di_insurance_fix_order_option`;
CREATE TABLE `di_insurance_fix_order_option`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单ID',
  `option_id` bigint(20) NULL DEFAULT NULL COMMENT '选项ID',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '选项code',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '选项标题',
  `value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '选项值',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE,
  INDEX `idx_option_id`(`option_id`) USING BTREE,
  INDEX `idx_deleted`(`deleted`) USING BTREE,
  INDEX `idx_code`(`code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1862045047476256770 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单选项信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for di_insurance_fix_order_settlement
-- ----------------------------
DROP TABLE IF EXISTS `di_insurance_fix_order_settlement`;
CREATE TABLE `di_insurance_fix_order_settlement`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `apply_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '结算单号',
  `fix_order_id` bigint(20) NULL DEFAULT NULL COMMENT '报险单号',
  `bd_id` bigint(20) NULL DEFAULT NULL,
  `area_id` bigint(20) NULL DEFAULT NULL,
  `amount` bigint(20) NULL DEFAULT NULL COMMENT '提现金额(分)',
  `tax_amount` bigint(20) NULL DEFAULT NULL COMMENT '代扣税额',
  `in_amount` bigint(20) NULL DEFAULT NULL COMMENT '到手金额',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型(1-银行卡、2-支付宝、3-对公账户)',
  `account_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '银行名称',
  `account_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账号',
  `owner_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `id_card` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  `status` int(11) NULL DEFAULT NULL COMMENT '申请状态',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `pay_time` datetime(3) NULL DEFAULT NULL COMMENT '打款时间',
  `ancestors` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组织层级编码',
  `state_snapshot` json NULL COMMENT '状态快照',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1309539585114247169 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数保理赔打款结算表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for di_insurance_option
-- ----------------------------
DROP TABLE IF EXISTS `di_insurance_option`;
CREATE TABLE `di_insurance_option`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `insurance_id` bigint(20) NULL DEFAULT NULL COMMENT '数保产品ID',
  `option_id` bigint(20) NULL DEFAULT NULL COMMENT '选项ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_insurance_id`(`insurance_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1225016679844296931 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数保产品关联选项表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for di_insurance_order
-- ----------------------------
DROP TABLE IF EXISTS `di_insurance_order`;
CREATE TABLE `di_insurance_order`  (
  `id` bigint(20) NOT NULL,
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `store_employee_id` bigint(20) NULL DEFAULT NULL COMMENT '门店员工ID',
  `store_company_id` bigint(20) NULL DEFAULT NULL COMMENT '门店ID',
  `product_sku_id` bigint(20) NULL DEFAULT NULL COMMENT '商品SKUID',
  `product_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机商品名称',
  `product_spec` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机规格名称',
  `product_sku_retail_price` int(11) NULL DEFAULT NULL COMMENT '手机市场零售价',
  `product_sku_purchase_invoice_price` int(11) NULL DEFAULT NULL COMMENT '手机发票购机价',
  `product_sku_purchase_invoice_time` datetime(3) NULL DEFAULT NULL COMMENT '手机发票购机时间',
  `insurance_id` bigint(20) NULL DEFAULT NULL COMMENT '数保产品ID',
  `insurance_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数保产品名称',
  `insurance_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数保产品类型',
  `insurance_period` int(5) NULL DEFAULT NULL COMMENT '数保产品年限',
  `status` int(5) NULL DEFAULT NULL COMMENT '订单状态',
  `sub_status` int(5) NULL DEFAULT NULL COMMENT '订单子状态',
  `custom_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户姓名',
  `custom_phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户手机号',
  `id_card` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户身份证',
  `imei_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机串号',
  `price` bigint(20) NULL DEFAULT NULL COMMENT '成交价',
  `insurance_normal_price` bigint(20) NULL DEFAULT NULL COMMENT '数保产品门店进价',
  `insurance_down_price` bigint(20) NULL DEFAULT NULL COMMENT '数保产品平台底价',
  `insurance_status` int(5) NULL DEFAULT NULL COMMENT '保险服务单状态',
  `insurance_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '保险服务单号',
  `remark` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `effective_date` datetime(3) NULL DEFAULT NULL COMMENT '保险服务生效时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数保订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for di_insurance_order_payment
-- ----------------------------
DROP TABLE IF EXISTS `di_insurance_order_payment`;
CREATE TABLE `di_insurance_order_payment`  (
  `id` bigint(20) NOT NULL,
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `insurance_order_id` bigint(20) NULL DEFAULT NULL COMMENT '数保订单ID',
  `open_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户openId',
  `out_trade_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '平台交易订单号',
  `transaction_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信支付订单号',
  `refund_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '平台退款订单号',
  `refund_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信退款订单号',
  `amount` bigint(20) NULL DEFAULT NULL COMMENT '金额',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态',
  `type` tinyint(1) NULL DEFAULT NULL COMMENT '支付方式',
  `pay_time` datetime(3) NULL DEFAULT NULL COMMENT '支付时间',
  `qr_code_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跳转连接二维码图片地址',
  `refund_time` datetime(3) NULL DEFAULT NULL COMMENT '退款时间',
  `refund_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '退款原因',
  `refund_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '退款备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数保支付订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for di_insurance_order_picture
-- ----------------------------
DROP TABLE IF EXISTS `di_insurance_order_picture`;
CREATE TABLE `di_insurance_order_picture`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `insurance_order_id` bigint(20) NULL DEFAULT NULL COMMENT '数保订单ID',
  `sort` int(10) NULL DEFAULT 0 COMMENT '排序',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片地址',
  `type` tinyint(2) NULL DEFAULT NULL COMMENT '类型 1图片 2视频',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1861608732364439555 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数保订单图片表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for di_insurance_user_account
-- ----------------------------
DROP TABLE IF EXISTS `di_insurance_user_account`;
CREATE TABLE `di_insurance_user_account`  (
  `id` bigint(20) NOT NULL,
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `mobile` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `id_card` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  `pass_word` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `status` tinyint(2) NULL DEFAULT NULL COMMENT '1正常2注销3下线',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名字',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数保用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for di_option
-- ----------------------------
DROP TABLE IF EXISTS `di_option`;
CREATE TABLE `di_option`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `sort` int(10) NULL DEFAULT 0 COMMENT '排序',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `type` tinyint(2) NULL DEFAULT NULL COMMENT '1单选 2多选 3图片 4视频',
  `required` tinyint(1) NULL DEFAULT 0 COMMENT '是否必填0否1是',
  `level` int(10) NULL DEFAULT 0 COMMENT '层级',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '上级ID',
  `ancestors` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '层级路径',
  `sample_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '示例图',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '选项code',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_deleted`(`deleted`) USING BTREE,
  INDEX `idx_sort`(`sort`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id`) USING BTREE,
  INDEX `idx_level`(`level`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1382418565504454657 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数保产品选项表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for di_package
-- ----------------------------
DROP TABLE IF EXISTS `di_package`;
CREATE TABLE `di_package`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `type_id` bigint(20) NULL DEFAULT NULL COMMENT '险种ID',
  `name` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '套餐名称',
  `status` tinyint(2) NULL DEFAULT NULL COMMENT '1正常2下线3删除',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '保险套餐表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for di_product_insurance
-- ----------------------------
DROP TABLE IF EXISTS `di_product_insurance`;
CREATE TABLE `di_product_insurance`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `insurance_id` bigint(20) NULL DEFAULT NULL COMMENT '数保产品ID',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `activated` tinyint(1) NULL DEFAULT NULL COMMENT '数保产品上下线标识0否1是',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE,
  INDEX `idx_insurance_id`(`insurance_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2000 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '手机-数保产品关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for di_product_insurance_copy1
-- ----------------------------
DROP TABLE IF EXISTS `di_product_insurance_copy1`;
CREATE TABLE `di_product_insurance_copy1`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `insurance_id` bigint(20) NULL DEFAULT NULL COMMENT '数保产品ID',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `activated` tinyint(1) NULL DEFAULT NULL COMMENT '数保产品上下线标识0否1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 74 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '手机-数保产品关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for di_product_insurance_price
-- ----------------------------
DROP TABLE IF EXISTS `di_product_insurance_price`;
CREATE TABLE `di_product_insurance_price`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `insurance_id` bigint(20) NULL DEFAULT NULL COMMENT '数保产品ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `sale_price` int(11) NULL DEFAULT NULL COMMENT '门店初始售价(单位：分)',
  `normal_price` int(11) NULL DEFAULT NULL COMMENT '门店进货价格(单位：分)',
  `price_low` int(11) NULL DEFAULT NULL COMMENT '价格区间下限(单位：分)',
  `price_high` int(11) NULL DEFAULT NULL COMMENT '价格区间上限(单位：分)',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '1启用 0禁用',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 147 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数保产品售价价格区间设置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for di_sku_insurance
-- ----------------------------
DROP TABLE IF EXISTS `di_sku_insurance`;
CREATE TABLE `di_sku_insurance`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `sku_id` bigint(20) NULL DEFAULT NULL COMMENT '手机SKUID',
  `insurance_id` bigint(20) NULL DEFAULT NULL COMMENT '数保产品ID',
  `down_price` int(11) NULL DEFAULT NULL COMMENT 'SKU底价',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sku_id`(`sku_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数保产品SKU底价表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for di_sku_insurance_copy1
-- ----------------------------
DROP TABLE IF EXISTS `di_sku_insurance_copy1`;
CREATE TABLE `di_sku_insurance_copy1`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `sku_id` bigint(20) NULL DEFAULT NULL COMMENT '手机SKUID',
  `insurance_id` bigint(20) NULL DEFAULT NULL COMMENT '数保产品ID',
  `down_price` int(11) NULL DEFAULT NULL COMMENT 'SKU底价',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数保产品SKU底价表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for di_type
-- ----------------------------
DROP TABLE IF EXISTS `di_type`;
CREATE TABLE `di_type`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '产品类型名称',
  `package_able` tinyint(1) NULL DEFAULT NULL COMMENT '是否套餐',
  `status` tinyint(2) NULL DEFAULT NULL COMMENT '1正常2下线3删除',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '险种类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for di_user_login
-- ----------------------------
DROP TABLE IF EXISTS `di_user_login`;
CREATE TABLE `di_user_login`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `login_time` datetime NULL DEFAULT NULL COMMENT '登录时间',
  `token` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'token',
  `token_expire` datetime NULL DEFAULT NULL COMMENT 'token过期时间',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for employee
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee`  (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `company_id` bigint(20) NULL DEFAULT NULL COMMENT '公司ID',
  `company_type` int(11) NULL DEFAULT NULL COMMENT '1-公司,2门店3.连锁4服务商',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门id',
  `dept_type` int(11) NULL DEFAULT NULL COMMENT '1-管理部门2-普通部门',
  `type` int(11) NULL DEFAULT NULL COMMENT '1-公司或者渠道管理部门管理员2-管理部门员工3-普通部门管理员4-普通部门员工',
  `mobile_number` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `dept_code` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门code',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态 （1 正常 2 已注销 3下线）',
  `province` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '省-未使用',
  `city` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '市-未使用',
  `head_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像地址',
  `withdraw_pwd` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提现密码',
  `ancestors` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '人员组织层级编码',
  `level` int(11) NULL DEFAULT NULL COMMENT '人员组织层级编码',
  `is_leaf` tinyint(1) NULL DEFAULT 1 COMMENT '是否叶子节点(0-否,1-是)',
  `public_flag` tinyint(1) NULL DEFAULT 0 COMMENT '对公标识',
  `creator` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `updator` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_bt_com_id`(`company_id`) USING BTREE,
  INDEX `fk_bt_d_id`(`dept_id`) USING BTREE,
  INDEX `fk_bt_mobile`(`mobile_number`) USING BTREE,
  INDEX `fk_bt_code`(`dept_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '职员' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for employee_account
-- ----------------------------
DROP TABLE IF EXISTS `employee_account`;
CREATE TABLE `employee_account`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `employee_id` bigint(20) NULL DEFAULT NULL COMMENT '员工id',
  `able_balance` bigint(20) NULL DEFAULT 0 COMMENT '钱包可用余额(分)',
  `temp_frozen_balance` bigint(20) NULL DEFAULT 0 COMMENT '钱包临时冻结金额(分)',
  `frozen_balance` bigint(20) NULL DEFAULT 0 COMMENT '钱包永久冻结金额(分)',
  `accumulate_income` bigint(20) NULL DEFAULT 0 COMMENT '钱包累计入账(分)',
  `acc_award_income` bigint(20) NULL DEFAULT 0 COMMENT '奖金累计收入',
  `acc_withdraw` bigint(20) NULL DEFAULT 0 COMMENT '累计提现金额',
  `invented_withdraw_flag` tinyint(1) NULL DEFAULT 0 COMMENT '是否子后台提现(0-否,1-是)',
  `ancestors` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组织层级编码',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uqidx_employee_id`(`employee_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1453040881291628545 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '个人账户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for employee_account_log
-- ----------------------------
DROP TABLE IF EXISTS `employee_account_log`;
CREATE TABLE `employee_account_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '说明',
  `employee_account_id` bigint(20) NULL DEFAULT NULL COMMENT '员工id',
  `employee_id` bigint(20) NULL DEFAULT NULL COMMENT '员工id',
  `ancestors` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组织层级编码',
  `change_main_type` int(11) NULL DEFAULT NULL COMMENT '主变动类型(收入、支出、不变)',
  `change_detail_type` bigint(20) NULL DEFAULT NULL COMMENT '详细变动类型',
  `change_balance` bigint(20) NULL DEFAULT NULL COMMENT '变动金额',
  `able_balance_before` bigint(20) NULL DEFAULT NULL COMMENT '钱包可用余额(分)_变动前',
  `able_balance_change` bigint(20) NULL DEFAULT NULL COMMENT '钱包可用余额(分)_变动金额',
  `able_balance_after` bigint(20) NULL DEFAULT NULL COMMENT '钱包可用余额(分)_变动后',
  `temp_frozen_balance_before` bigint(20) NULL DEFAULT NULL COMMENT '钱包冻结金额(分)_变动前',
  `temp_frozen_balance_change` bigint(20) NULL DEFAULT NULL COMMENT '钱包冻结金额(分)_变动金额',
  `temp_frozen_balance_after` bigint(20) NULL DEFAULT NULL COMMENT '钱包冻结金额(分)_变动后',
  `frozen_balance_before` bigint(20) NULL DEFAULT NULL COMMENT '钱包永久冻结金额(分)_变动前',
  `frozen_balance_change` bigint(20) NULL DEFAULT NULL COMMENT '钱包永久冻结金额(分)_变动金额',
  `frozen_balance_after` bigint(20) NULL DEFAULT NULL COMMENT '钱包永久冻结金额(分)_变动后',
  `accumulate_income_before` bigint(20) NULL DEFAULT NULL COMMENT '钱包累计入账(分)_变动前',
  `accumulate_income_change` bigint(20) NULL DEFAULT NULL COMMENT '钱包累计入账(分)',
  `accumulate_income_after` bigint(20) NULL DEFAULT NULL COMMENT '钱包累计入账(分)',
  `acc_award_income_before` bigint(20) NULL DEFAULT 0 COMMENT '奖金累计收入-变动前',
  `acc_award_income_change` bigint(20) NULL DEFAULT 0 COMMENT '奖金累计收入-变动金额',
  `acc_award_income_after` bigint(20) NULL DEFAULT 0 COMMENT '奖金累计收入-变动后',
  `acc_withdraw_before` bigint(20) NULL DEFAULT 0 COMMENT '累计提现金额-变动前',
  `acc_withdraw_change` bigint(20) NULL DEFAULT 0 COMMENT '累计提现金额-变动金额',
  `acc_withdraw_after` bigint(20) NULL DEFAULT 0 COMMENT '累计提现金额-变动后',
  `correlation_id` bigint(20) NULL DEFAULT NULL COMMENT '扩展id',
  `remark` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '变动说明',
  `user_focus` tinyint(1) NULL DEFAULT NULL COMMENT '是否用户可见(0-否,1-是)',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1083 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '个人账户变动明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for employee_history
-- ----------------------------
DROP TABLE IF EXISTS `employee_history`;
CREATE TABLE `employee_history`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '员工id',
  `device` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备信息',
  `login_time` datetime NULL DEFAULT NULL COMMENT '登录时间',
  `out_time` datetime NULL DEFAULT NULL COMMENT '登出时间',
  `token` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'token',
  `token_expire` datetime NULL DEFAULT NULL COMMENT 'token过期时间',
  `os` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统 （android, ios）',
  `os_version` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统版本',
  `app_version` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'app版本',
  `out_resaon` tinyint(4) NULL DEFAULT NULL COMMENT '退出原因 -1 token过期，2 被挤下线 3-主动退出',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_bt_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '员工登录记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for employee_login
-- ----------------------------
DROP TABLE IF EXISTS `employee_login`;
CREATE TABLE `employee_login`  (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `device` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备信息',
  `login_time` datetime NULL DEFAULT NULL COMMENT '登录时间',
  `token` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'token',
  `token_expire` datetime NULL DEFAULT NULL COMMENT 'token过期时间',
  `os` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统 （android, ios）',
  `os_version` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统版本',
  `app_version` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'app版本',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_bt_user_id`(`user_id`) USING BTREE,
  INDEX `idx_token`(`token`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '员工登录记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for employee_operate_log
-- ----------------------------
DROP TABLE IF EXISTS `employee_operate_log`;
CREATE TABLE `employee_operate_log`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `employee_id` bigint(20) NOT NULL COMMENT '员工ID',
  `type` tinyint(2) NULL DEFAULT NULL COMMENT '1上线2下线',
  `bd_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'BD名称',
  `employee_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '员工姓名',
  `mobile_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '员工手机号',
  `company_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '渠道名称',
  `dept_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `dept_manage_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门管理员',
  `dept_manage_mobile` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '管理员手机号',
  `reason` tinyint(2) NULL DEFAULT NULL COMMENT '1虚假宣传2其他',
  `remark` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `download_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '凭证图片地址',
  `creator` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除 0否1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '员工上下线操作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for employee_real_name_verification
-- ----------------------------
DROP TABLE IF EXISTS `employee_real_name_verification`;
CREATE TABLE `employee_real_name_verification`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除 0否1是',
  `employee_id` bigint(20) NULL DEFAULT NULL COMMENT '员工ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `id_card` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  `id_url_up` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证正面',
  `id_url_down` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证背面',
  `remote_resp` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '接口返回日志',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_employee_id`(`employee_id`) USING BTREE,
  INDEX `idx_deleted`(`deleted`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1412127711668867074 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '员工账户实名认证表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for exchange_device_file_info
-- ----------------------------
DROP TABLE IF EXISTS `exchange_device_file_info`;
CREATE TABLE `exchange_device_file_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `employee_id` bigint(20) NULL DEFAULT NULL COMMENT '门店员工ID',
  `sort` int(10) NULL DEFAULT 0 COMMENT '排序',
  `order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单编码',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片照片',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_employee_id`(`employee_id`) USING BTREE,
  INDEX `idx_order_no`(`order_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1847175339648286723 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for hk_apply_order
-- ----------------------------
DROP TABLE IF EXISTS `hk_apply_order`;
CREATE TABLE `hk_apply_order`  (
  `id` bigint(20) NOT NULL,
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `fetch_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品编码',
  `fetch_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `third_order_sn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '三方订单号',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `id_card` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证',
  `mobile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `province_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省份',
  `city_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '市',
  `town_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '详细地址',
  `plan_mobile_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '预约手机号',
  `employee_id` bigint(20) NULL DEFAULT NULL COMMENT '员工ID',
  `company_id` bigint(20) NULL DEFAULT NULL COMMENT '门店ID',
  `ancestors` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '员工层级',
  `status` int(3) NULL DEFAULT NULL COMMENT '状态1待处理，3下单成功，4已写卡，5已发货，6已签收，7已激活，8激活失败，9失败订单，10提单失败，12订购失败，13已拒收，16已充值(大于100)、17已充值(大于50)、18已充值(小于50)',
  `active_time` datetime(3) NULL DEFAULT NULL COMMENT '激活时间',
  `reason` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '理由',
  `express_bill` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物流单号',
  `operator_id` bigint(20) NULL DEFAULT NULL COMMENT '运营商ID',
  `supplier_id` bigint(20) NULL DEFAULT NULL COMMENT '供应商ID',
  `express` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物流公司',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '产品ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '号卡订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for hk_notify_log
-- ----------------------------
DROP TABLE IF EXISTS `hk_notify_log`;
CREATE TABLE `hk_notify_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `order_sn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单号',
  `third_order_sn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '三方订单号',
  `status` int(10) NULL DEFAULT NULL COMMENT '状态',
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '理由',
  `express_bill` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物流单号',
  `express` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物流公司',
  `is_actived` int(2) NULL DEFAULT NULL COMMENT '激活状态：1未激活，2已激活',
  `active_time` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '激活时间',
  `plan_mobile_number` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '预约号码',
  `is_return_url` int(10) NULL DEFAULT NULL COMMENT '是否返回链接：1不返回，2返回链接',
  `num` int(10) NULL DEFAULT NULL COMMENT '推送次数',
  `data` varchar(3000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '原JSON',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1965223158951260163 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '号卡回调表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for hk_operator
-- ----------------------------
DROP TABLE IF EXISTS `hk_operator`;
CREATE TABLE `hk_operator`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `sort` tinyint(11) NULL DEFAULT 0 COMMENT '排序',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '状态 0下架 1上架',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '号卡运营商表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for hk_product
-- ----------------------------
DROP TABLE IF EXISTS `hk_product`;
CREATE TABLE `hk_product`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `sort` tinyint(11) NULL DEFAULT 0 COMMENT '排序',
  `operator_id` bigint(20) NULL DEFAULT NULL COMMENT '运营商ID',
  `supplier_id` bigint(20) NULL DEFAULT NULL COMMENT '供应商ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编码',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '状态 0下架 1上架',
  `price` bigint(20) NULL DEFAULT NULL COMMENT '价格 单位分',
  `require_select_mobile` tinyint(1) NULL DEFAULT 0 COMMENT '是否选号 0否1是',
  `commission_status` int(3) NULL DEFAULT NULL COMMENT '分佣条件（订单状态）',
  `master_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '列表主图',
  `detail_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '详情图',
  `sell_point_one` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '卖点1',
  `sell_point_two` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '卖点2',
  `sell_point_three` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '卖点3',
  `tag_one` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签1',
  `tag_two` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签2',
  `tag_three` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签3',
  `province` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '归属地-省',
  `city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '归属地-市',
  `commission_type_package_id` bigint(20) NULL DEFAULT NULL COMMENT '分佣套餐ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1426571435145179137 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '号卡产品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for hk_product_employee
-- ----------------------------
DROP TABLE IF EXISTS `hk_product_employee`;
CREATE TABLE `hk_product_employee`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '号卡套餐ID',
  `employee_id` bigint(20) NULL DEFAULT NULL COMMENT '员工ID',
  `company_id` bigint(20) NULL DEFAULT NULL COMMENT '门店ID',
  `bd_id` bigint(20) NULL DEFAULT NULL COMMENT '合伙人ID',
  `bd_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '合伙人名称',
  `area_id` bigint(20) NULL DEFAULT NULL COMMENT '区域经理ID',
  `area_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域经理名称',
  `agent_id` bigint(20) NULL DEFAULT NULL COMMENT '代理ID',
  `agent_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '代理名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1963077969362554883 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '号卡门店关联' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for hk_supplier
-- ----------------------------
DROP TABLE IF EXISTS `hk_supplier`;
CREATE TABLE `hk_supplier`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `sort` tinyint(11) NULL DEFAULT 0 COMMENT '排序',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '状态 0下架 1上架',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1405217332293029889 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '号卡供应商表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for img_config
-- ----------------------------
DROP TABLE IF EXISTS `img_config`;
CREATE TABLE `img_config`  (
  `id` bigint(20) NOT NULL,
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `type` int(11) NULL DEFAULT NULL,
  `sort` int(11) NULL DEFAULT NULL,
  `length` int(11) NULL DEFAULT NULL,
  `width` int(11) NULL DEFAULT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `descr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for latest_use_address
-- ----------------------------
DROP TABLE IF EXISTS `latest_use_address`;
CREATE TABLE `latest_use_address`  (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `biz` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务类型',
  `address_id` bigint(20) NULL DEFAULT NULL COMMENT '地址id',
  `req_emp_id` bigint(20) NULL DEFAULT NULL COMMENT '发起人id',
  `rsp_emp_id` bigint(20) NULL DEFAULT -1 COMMENT '响应人id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_req_emp_id`(`req_emp_id`) USING BTREE,
  INDEX `idx_rsp_emp_id`(`rsp_emp_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '最近使用地址' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mb_brand
-- ----------------------------
DROP TABLE IF EXISTS `mb_brand`;
CREATE TABLE `mb_brand`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `sort` int(10) NULL DEFAULT 0 COMMENT '排序',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌名称',
  `logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌Logo',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sort`(`sort`) USING BTREE,
  INDEX `idx_deleted`(`deleted`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1751906994402156547 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '品牌表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mb_brand_copy1
-- ----------------------------
DROP TABLE IF EXISTS `mb_brand_copy1`;
CREATE TABLE `mb_brand_copy1`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `sort` int(10) NULL DEFAULT 0 COMMENT '排序',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌名称',
  `logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌Logo',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sort`(`sort`) USING BTREE,
  INDEX `idx_deleted`(`deleted`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1751906994402156546 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '品牌表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mb_category
-- ----------------------------
DROP TABLE IF EXISTS `mb_category`;
CREATE TABLE `mb_category`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `sort` int(10) NULL DEFAULT 0 COMMENT '排序',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类名称',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类图标',
  `level` int(10) NULL DEFAULT 0 COMMENT '分类层级',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '上级分类ID',
  `ancestors` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类层级路径',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id`) USING BTREE,
  INDEX `idx_level`(`level`) USING BTREE,
  INDEX `idx_sort`(`sort`) USING BTREE,
  INDEX `idx_deleted`(`deleted`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1445434877209751553 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mb_category_copy1
-- ----------------------------
DROP TABLE IF EXISTS `mb_category_copy1`;
CREATE TABLE `mb_category_copy1`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `sort` int(10) NULL DEFAULT 0 COMMENT '排序',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类名称',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类图标',
  `level` int(10) NULL DEFAULT 0 COMMENT '分类层级',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '上级分类ID',
  `ancestors` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类层级路径',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id`) USING BTREE,
  INDEX `idx_level`(`level`) USING BTREE,
  INDEX `idx_sort`(`sort`) USING BTREE,
  INDEX `idx_deleted`(`deleted`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1202995418788278274 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mb_exchange_custom
-- ----------------------------
DROP TABLE IF EXISTS `mb_exchange_custom`;
CREATE TABLE `mb_exchange_custom`  (
  `id` bigint(20) NOT NULL,
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单ID',
  `store_employee_id` bigint(20) NULL DEFAULT NULL COMMENT '员工ID',
  `store_company_id` bigint(20) NULL DEFAULT NULL COMMENT '员工公司',
  `employee_phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '换机助手员工登陆的手机号',
  `exchange_phone_id` bigint(20) NULL DEFAULT NULL COMMENT '换机包Id',
  `custom_phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户手机号',
  `install_id` bigint(20) NULL DEFAULT NULL COMMENT '安装包id',
  `install_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '安装包名称',
  `install_channel_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '安装包渠道编码',
  `open_time` int(11) NULL DEFAULT NULL COMMENT '打开时长秒',
  `order_sn` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单号',
  `oaid` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '安卓广告标识',
  `market_flag` tinyint(2) NULL DEFAULT NULL COMMENT '应用市场打开标识',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '客户安装换机包' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mb_exchange_device
-- ----------------------------
DROP TABLE IF EXISTS `mb_exchange_device`;
CREATE TABLE `mb_exchange_device`  (
  `id` bigint(20) NOT NULL,
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单ID',
  `factory_date` datetime NULL DEFAULT NULL COMMENT '出厂日期',
  `open_time` bigint(20) NULL DEFAULT NULL COMMENT '开机时长秒',
  `custom_phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户手机号',
  `install_date` datetime NULL DEFAULT NULL COMMENT '第一次打开换机助手时间',
  `oaid` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '安卓手机广告标识',
  `ip` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '机器IP',
  `brand` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌',
  `model` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '型号',
  `sys_version` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '系统版本号',
  `android_version` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '安卓版本号',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '晒单客户机基础信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mb_exchange_employee
-- ----------------------------
DROP TABLE IF EXISTS `mb_exchange_employee`;
CREATE TABLE `mb_exchange_employee`  (
  `id` bigint(20) NOT NULL,
  `exchange_phone_id` bigint(20) NULL DEFAULT NULL COMMENT '换机包ID',
  `exchange_phone_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '换机包名称',
  `employee_id` bigint(20) NULL DEFAULT NULL COMMENT '员工ID',
  `company_id` bigint(20) NULL DEFAULT NULL COMMENT '门店ID',
  `bd_id` bigint(20) NULL DEFAULT NULL COMMENT '合伙人ID',
  `bd_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '合伙人名称',
  `agent_id` bigint(20) NULL DEFAULT NULL COMMENT '代理ID',
  `agent_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '代理名称',
  `area_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域经理名称',
  `area_id` bigint(20) NULL DEFAULT NULL COMMENT '区域经理ID',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '合伙人换机包' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mb_exchange_employee_info
-- ----------------------------
DROP TABLE IF EXISTS `mb_exchange_employee_info`;
CREATE TABLE `mb_exchange_employee_info`  (
  `id` bigint(20) NOT NULL,
  `employee_id` bigint(20) NULL DEFAULT NULL COMMENT '代理ID',
  `id_card` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '负责地区',
  `business` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '已开通业务',
  `plat_check` tinyint(1) NULL DEFAULT 1 COMMENT '平台审单1门店审单0',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '二手机代理拓展信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mb_exchange_install
-- ----------------------------
DROP TABLE IF EXISTS `mb_exchange_install`;
CREATE TABLE `mb_exchange_install`  (
  `id` bigint(20) NOT NULL,
  `install_id` bigint(20) NULL DEFAULT NULL COMMENT '安装包ID',
  `exchange_phone_id` bigint(20) NULL DEFAULT NULL COMMENT '换机包ID',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '拉新换机安装包关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mb_exchange_order
-- ----------------------------
DROP TABLE IF EXISTS `mb_exchange_order`;
CREATE TABLE `mb_exchange_order`  (
  `id` bigint(20) NOT NULL,
  `store_company_id` bigint(20) NULL DEFAULT NULL COMMENT '门店ID',
  `store_employee_id` bigint(20) NULL DEFAULT NULL COMMENT '店员ID',
  `type` tinyint(2) NULL DEFAULT NULL COMMENT '订单类型3换机4拉新5快手绿洲6苹果抖音',
  `source` tinyint(2) NULL DEFAULT NULL COMMENT '来源',
  `bd_id` bigint(20) NULL DEFAULT NULL COMMENT '合伙人ID',
  `area_id` bigint(20) NULL DEFAULT NULL COMMENT '区域经理ID',
  `agent_id` bigint(20) NULL DEFAULT NULL COMMENT '代理ID',
  `sys_mobile` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机系统',
  `imei_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'IMEI号',
  `oaid` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '安卓广告标识',
  `exchange_phone_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '换机包编码',
  `custom_phone` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户手机号',
  `status` tinyint(2) NULL DEFAULT NULL COMMENT '审核状态',
  `settle_status` tinyint(2) NULL DEFAULT NULL COMMENT '结算状态',
  `trial_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `sys_status` tinyint(2) NULL DEFAULT NULL COMMENT '机审状态',
  `sys_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '机审备注',
  `plat_check` tinyint(1) NULL DEFAULT 1 COMMENT '1平台审核0店长审核',
  `illegal` tinyint(1) NULL DEFAULT 0 COMMENT '异常标识',
  `pass_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核通过备注',
  `channel_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '渠道编码',
  `channel_order_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '渠道订单号',
  `channel_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '渠道名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '换机晒单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mb_exchange_phone
-- ----------------------------
DROP TABLE IF EXISTS `mb_exchange_phone`;
CREATE TABLE `mb_exchange_phone`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '换机包名称',
  `type` tinyint(2) NULL DEFAULT NULL COMMENT '使用场景',
  `channel_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '包编码',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `status` tinyint(2) NULL DEFAULT NULL COMMENT '状态',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '拉新换机包' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mb_exchange_pic
-- ----------------------------
DROP TABLE IF EXISTS `mb_exchange_pic`;
CREATE TABLE `mb_exchange_pic`  (
  `id` bigint(20) NOT NULL,
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单ID',
  `install_channel_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '安装包渠道编码',
  `uid` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类似抖音UID',
  `did` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类似抖音DID',
  `act_time` datetime NULL DEFAULT NULL COMMENT '类似抖音ACTTIME',
  `install_channel` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类似抖音CHANNEL',
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片照片',
  `install_id` bigint(20) NULL DEFAULT NULL COMMENT '拉新安装包ID',
  `install_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '拉新安装包名称',
  `imei_no` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'IEMI',
  `serino` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '序列号',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '晒单图片信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mb_exchange_verify_employee
-- ----------------------------
DROP TABLE IF EXISTS `mb_exchange_verify_employee`;
CREATE TABLE `mb_exchange_verify_employee`  (
  `id` bigint(20) NOT NULL,
  `exchange_verify_id` bigint(20) NULL DEFAULT NULL COMMENT '验新包ID',
  `exchange_verify_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '验新包名称',
  `verify_type_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '验新包编码',
  `employee_id` bigint(20) NULL DEFAULT NULL COMMENT '员工ID',
  `company_id` bigint(20) NULL DEFAULT NULL COMMENT '门店ID',
  `bd_id` bigint(20) NULL DEFAULT NULL COMMENT '合伙人ID',
  `bd_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '合伙人名称',
  `area_id` bigint(20) NULL DEFAULT NULL COMMENT '区域经理ID',
  `area_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域经理名称',
  `agent_id` bigint(20) NULL DEFAULT NULL COMMENT '代理ID',
  `agent_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '代理名称',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '门店验新包' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mb_install
-- ----------------------------
DROP TABLE IF EXISTS `mb_install`;
CREATE TABLE `mb_install`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '安装包名称',
  `application_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用安装需要',
  `type` tinyint(2) NULL DEFAULT NULL COMMENT '使用场景',
  `channel_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '渠道号',
  `channel_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '渠道Token',
  `open_time` int(11) NULL DEFAULT NULL COMMENT '打开时长',
  `verify_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '验新码地址',
  `down_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下载地址',
  `icon_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图标URL',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态',
  `cover_flag` tinyint(1) NULL DEFAULT NULL COMMENT '覆盖标识',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '拉新安装包' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mb_option
-- ----------------------------
DROP TABLE IF EXISTS `mb_option`;
CREATE TABLE `mb_option`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `sort` int(10) NULL DEFAULT 0 COMMENT '排序',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `type` tinyint(2) NULL DEFAULT NULL COMMENT '1单选 2多选 3图片',
  `required` tinyint(1) NULL DEFAULT 0 COMMENT '是否必填0否1是',
  `level` int(10) NULL DEFAULT 0 COMMENT '层级',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '上级ID',
  `ancestors` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '层级路径',
  `sample_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '示例图',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '选项code',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_deleted`(`deleted`) USING BTREE,
  INDEX `idx_sort`(`sort`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id`) USING BTREE,
  INDEX `idx_level`(`level`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1263879117423251458 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商品附加选项表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mb_option_copy1
-- ----------------------------
DROP TABLE IF EXISTS `mb_option_copy1`;
CREATE TABLE `mb_option_copy1`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `sort` int(10) NULL DEFAULT 0 COMMENT '排序',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `type` tinyint(2) NULL DEFAULT NULL COMMENT '1单选 2多选 3图片',
  `required` tinyint(1) NULL DEFAULT 0 COMMENT '是否必填0否1是',
  `level` int(10) NULL DEFAULT 0 COMMENT '层级',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '上级ID',
  `ancestors` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '层级路径',
  `sample_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '示例图',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '选项code',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_deleted`(`deleted`) USING BTREE,
  INDEX `idx_sort`(`sort`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id`) USING BTREE,
  INDEX `idx_level`(`level`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1245742422454710273 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商品附加选项表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mb_order
-- ----------------------------
DROP TABLE IF EXISTS `mb_order`;
CREATE TABLE `mb_order`  (
  `id` bigint(20) NOT NULL,
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `store_employee_id` bigint(20) NULL DEFAULT NULL COMMENT '门店员工ID',
  `store_company_id` bigint(20) NULL DEFAULT NULL COMMENT '门店ID',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `product_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单编码',
  `status` int(5) NULL DEFAULT NULL COMMENT '订单状态',
  `imei_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'IMEI号',
  `remark` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `original_quote_price` bigint(20) NULL DEFAULT NULL COMMENT '原始报价',
  `final_price` bigint(20) NULL DEFAULT NULL COMMENT '成交价',
  `commission` bigint(20) NULL DEFAULT NULL COMMENT '门店抽成金额',
  `platform_subsidy_price` bigint(20) NULL DEFAULT 0 COMMENT '平台补贴价格',
  `recycler_employee_id` bigint(20) NULL DEFAULT NULL COMMENT '回收商员工ID',
  `recycler_company_id` bigint(20) NULL DEFAULT NULL COMMENT '回收商ID',
  `quote_price_log_id` bigint(20) NULL DEFAULT NULL COMMENT '确认报价详情ID',
  `finish_quote_time` datetime(3) NULL DEFAULT NULL COMMENT '确认报价时间',
  `bound` tinyint(1) NULL DEFAULT 0 COMMENT '是否绑码',
  `verified` tinyint(1) NULL DEFAULT 0 COMMENT '是否核验',
  `quotable` tinyint(1) NULL DEFAULT 1 COMMENT '是否可报价（超时将关闭报价功能）',
  `shipping_overdue_time` datetime(3) NULL DEFAULT NULL COMMENT '发货超时时间',
  `confirm_receipt_time` datetime(3) NULL DEFAULT NULL COMMENT '确认收货时间',
  `order_no_type` tinyint(1) NULL DEFAULT 0 COMMENT '订单码类型 0普通码 1享转码',
  `can_apply_refund` tinyint(1) NULL DEFAULT 0 COMMENT '是否可申请退款',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_deleted`(`deleted`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE,
  INDEX `idx_update_time`(`update_time`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '报价订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mb_order_customer_receive_payment
-- ----------------------------
DROP TABLE IF EXISTS `mb_order_customer_receive_payment`;
CREATE TABLE `mb_order_customer_receive_payment`  (
  `id` bigint(20) NOT NULL,
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单ID',
  `out_biz_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付宝商户订单号',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收款人姓名',
  `mobile` varchar(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收款人手机号',
  `id_card` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收款人身份证号',
  `amount` bigint(20) NULL DEFAULT NULL COMMENT '收款金额',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '收款状态',
  `type` tinyint(1) NULL DEFAULT NULL COMMENT '收款方式',
  `receive_time` datetime(3) NULL DEFAULT NULL COMMENT '收款时间',
  `open_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付宝open_id',
  `alipay_order_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付宝转账订单号',
  `pay_fund_order_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付宝支付资金流水号',
  `qr_code_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跳转连接二维码图片地址',
  `remote_resp` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '接口返回日志',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_deleted`(`deleted`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE,
  INDEX `idx_out_biz_no`(`out_biz_no`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '报价订单-用户收款信息表（平台打钱给用户）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mb_order_customer_refund_payment
-- ----------------------------
DROP TABLE IF EXISTS `mb_order_customer_refund_payment`;
CREATE TABLE `mb_order_customer_refund_payment`  (
  `id` bigint(20) NOT NULL,
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单ID',
  `receive_payment_id` bigint(20) NULL DEFAULT NULL COMMENT '收款订单ID',
  `open_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户openId',
  `out_trade_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '平台交易订单号',
  `transaction_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信支付订单号',
  `amount` bigint(20) NULL DEFAULT NULL COMMENT '收款金额',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '收款状态',
  `type` tinyint(1) NULL DEFAULT NULL COMMENT '收款方式',
  `pay_time` datetime(3) NULL DEFAULT NULL COMMENT '支付时间',
  `qr_code_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跳转连接二维码图片地址',
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '退款原因',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '报价订单-用户退款信息表（用户主动支付退款给平台）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mb_order_log
-- ----------------------------
DROP TABLE IF EXISTS `mb_order_log`;
CREATE TABLE `mb_order_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单ID',
  `status` int(11) NULL DEFAULT NULL COMMENT '订单状态',
  `operation_status` int(11) NULL DEFAULT NULL COMMENT '操作状态',
  `operation` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作描述',
  `remark` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_deleted`(`deleted`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_operation_status`(`operation_status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1426571435233259521 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单操作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mb_order_option
-- ----------------------------
DROP TABLE IF EXISTS `mb_order_option`;
CREATE TABLE `mb_order_option`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单ID',
  `option_id` bigint(20) NULL DEFAULT NULL COMMENT '选项ID',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '选项code',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '选项标题',
  `value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '选项值',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE,
  INDEX `idx_option_id`(`option_id`) USING BTREE,
  INDEX `idx_deleted`(`deleted`) USING BTREE,
  INDEX `idx_code`(`code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1421178306212007942 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单选项信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mb_order_option_snapshot
-- ----------------------------
DROP TABLE IF EXISTS `mb_order_option_snapshot`;
CREATE TABLE `mb_order_option_snapshot`  (
  `id` bigint(20) NOT NULL,
  `order_id` bigint(20) NULL DEFAULT NULL,
  `detail` json NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单选项快照信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mb_order_quote_price_log
-- ----------------------------
DROP TABLE IF EXISTS `mb_order_quote_price_log`;
CREATE TABLE `mb_order_quote_price_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '报价订单ID',
  `employee_id` bigint(20) NULL DEFAULT NULL COMMENT '回收商员工ID',
  `company_id` bigint(20) NULL DEFAULT NULL COMMENT '回收商ID',
  `original_quote_price` bigint(20) NULL DEFAULT NULL COMMENT '原始报价',
  `final_price` bigint(20) NULL DEFAULT NULL COMMENT '成交价',
  `actual_payment_price` bigint(20) NULL DEFAULT NULL COMMENT '实际付款价格=原始报价+平台抽成金额',
  `platform_commission_rule` decimal(6, 4) NULL DEFAULT NULL COMMENT '平台抽成规则',
  `platform_commission` bigint(20) NULL DEFAULT NULL COMMENT '平台抽成金额',
  `commission_rule` decimal(6, 4) NULL DEFAULT NULL COMMENT '门店抽成规则',
  `commission` bigint(20) NULL DEFAULT NULL COMMENT '门店抽成金额',
  `platform_subsidy_price` bigint(20) NULL DEFAULT 0 COMMENT '平台补贴价格',
  `quoted` tinyint(1) NULL DEFAULT 0 COMMENT '是否已报价，默认为否',
  `quote_time` datetime(3) NULL DEFAULT NULL COMMENT '报价时间',
  `quote_time_spent` bigint(13) NULL DEFAULT NULL COMMENT '总体报价用时=报价时间-报价订单创建时间 单位毫秒',
  `quote_time_spent_real` bigint(13) NULL DEFAULT NULL COMMENT '实际报价用时=报价时间-抢单时间 单位毫秒',
  `status` int(5) NULL DEFAULT NULL COMMENT '状态',
  `sub_status` int(5) NULL DEFAULT NULL COMMENT '子状态',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_deleted`(`deleted`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE,
  INDEX `idx_company_id`(`company_id`) USING BTREE,
  INDEX `idx_quoted`(`quoted`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1421178306534969349 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '回收商报价记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mb_product
-- ----------------------------
DROP TABLE IF EXISTS `mb_product`;
CREATE TABLE `mb_product`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `activated` tinyint(1) NULL DEFAULT 0 COMMENT '是否上架 0否1是',
  `sort` int(10) NULL DEFAULT 0 COMMENT '排序',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `category_id` bigint(20) NULL DEFAULT NULL COMMENT '分类ID',
  `category_full_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类全称',
  `brand_id` bigint(20) NULL DEFAULT NULL COMMENT '品牌ID',
  `digital_insurance_able` tinyint(1) NULL DEFAULT 0 COMMENT '是否售卖数保 0否1是',
  `mobile_rental_able` tinyint(1) NULL DEFAULT 0 COMMENT '是否参与租机 0否1是',
  `type` tinyint(2) NULL DEFAULT 0 COMMENT 'ios/android',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_deleted`(`deleted`) USING BTREE,
  INDEX `idx_sort`(`sort`) USING BTREE,
  INDEX `idx_name`(`name`(191)) USING BTREE,
  INDEX `idx_category_id`(`category_id`) USING BTREE,
  INDEX `idx_activated`(`activated`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1389565039431237633 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mb_product_copy1
-- ----------------------------
DROP TABLE IF EXISTS `mb_product_copy1`;
CREATE TABLE `mb_product_copy1`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `activated` tinyint(1) NULL DEFAULT 0 COMMENT '是否上架 0否1是',
  `sort` int(10) NULL DEFAULT 0 COMMENT '排序',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `category_id` bigint(20) NULL DEFAULT NULL COMMENT '分类ID',
  `category_full_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类全称',
  `brand_id` bigint(20) NULL DEFAULT NULL COMMENT '品牌ID',
  `digital_insurance_able` tinyint(1) NULL DEFAULT 0 COMMENT '是否售卖数保 0否1是',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_deleted`(`deleted`) USING BTREE,
  INDEX `idx_sort`(`sort`) USING BTREE,
  INDEX `idx_name`(`name`) USING BTREE,
  INDEX `idx_category_id`(`category_id`) USING BTREE,
  INDEX `idx_activated`(`activated`) USING BTREE,
  INDEX `idx_digital_insurance_able`(`digital_insurance_able`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1225016679756214274 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mb_product_option
-- ----------------------------
DROP TABLE IF EXISTS `mb_product_option`;
CREATE TABLE `mb_product_option`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `option_id` bigint(20) NULL DEFAULT NULL COMMENT '选项ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE,
  INDEX `idx_option_id`(`option_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1263904592161935471 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商品选项关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mb_product_option_copy1
-- ----------------------------
DROP TABLE IF EXISTS `mb_product_option_copy1`;
CREATE TABLE `mb_product_option_copy1`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `option_id` bigint(20) NULL DEFAULT NULL COMMENT '选项ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE,
  INDEX `idx_option_id`(`option_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1225016679844294781 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商品选项关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mb_product_sku
-- ----------------------------
DROP TABLE IF EXISTS `mb_product_sku`;
CREATE TABLE `mb_product_sku`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `activated` tinyint(1) NULL DEFAULT 0 COMMENT '是否上架 0否1是',
  `sort` int(10) NULL DEFAULT 0 COMMENT '排序',
  `category_id` bigint(20) NULL DEFAULT NULL COMMENT '分类ID',
  `brand_id` bigint(20) NULL DEFAULT NULL COMMENT '品牌ID',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `spec` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规格名称',
  `retail_price` bigint(20) NULL DEFAULT NULL COMMENT '零售价',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1389565043713625671 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '手机商品sku表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mb_product_sku_copy1
-- ----------------------------
DROP TABLE IF EXISTS `mb_product_sku_copy1`;
CREATE TABLE `mb_product_sku_copy1`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `activated` tinyint(1) NULL DEFAULT 0 COMMENT '是否上架 0否1是',
  `sort` int(10) NULL DEFAULT 0 COMMENT '排序',
  `category_id` bigint(20) NULL DEFAULT NULL COMMENT '分类ID',
  `brand_id` bigint(20) NULL DEFAULT NULL COMMENT '品牌ID',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `spec` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规格名称',
  `retail_price` bigint(20) NULL DEFAULT NULL COMMENT '零售价',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1247584494993020105 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '手机商品sku表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mb_shipping_order
-- ----------------------------
DROP TABLE IF EXISTS `mb_shipping_order`;
CREATE TABLE `mb_shipping_order`  (
  `id` bigint(20) NOT NULL,
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `store_employee_id` bigint(20) NULL DEFAULT NULL COMMENT '门店发货员工ID',
  `store_company_id` bigint(20) NULL DEFAULT NULL COMMENT '门店ID',
  `recycler_employee_id` bigint(20) NULL DEFAULT NULL COMMENT '回收商确认收货员工ID',
  `recycler_company_id` bigint(20) NULL DEFAULT NULL COMMENT '回收商ID',
  `status` int(5) NULL DEFAULT NULL COMMENT '订单状态',
  `shipping_type` int(2) NULL DEFAULT NULL COMMENT '寄件类型 1线上 2线下',
  `track_company_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '快递公司CODE',
  `track_company_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '快递公司名称',
  `track_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '快递物流单号',
  `apply_logistics_time` datetime(3) NULL DEFAULT NULL COMMENT '物流下单时间',
  `confirm_receipt_time` datetime(3) NULL DEFAULT NULL COMMENT '确认收货时间',
  `pickup_start_time` datetime(3) NULL DEFAULT NULL COMMENT '期望揽收开始时间',
  `pickup_end_time` datetime(3) NULL DEFAULT NULL COMMENT '期望揽收结束时间',
  `price` bigint(20) NULL DEFAULT NULL COMMENT '实际物流运费 单位分',
  `discount_rate` decimal(6, 4) NULL DEFAULT NULL COMMENT '物流月结折扣费率',
  `discount_price` bigint(20) NULL DEFAULT NULL COMMENT '折扣金额 = 原始物流费用-实际物流费用 单位分',
  `original_price` bigint(20) NULL DEFAULT NULL COMMENT '折扣前原始物流费用 = 实际物流费用 / 折扣费率 单位分',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_deleted`(`deleted`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE,
  INDEX `idx_update_time`(`update_time`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '发货订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mb_shipping_order_address
-- ----------------------------
DROP TABLE IF EXISTS `mb_shipping_order_address`;
CREATE TABLE `mb_shipping_order_address`  (
  `id` bigint(20) NOT NULL,
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `shipping_order_id` bigint(20) NULL DEFAULT NULL COMMENT '发货订单ID',
  `type` tinyint(2) NULL DEFAULT NULL COMMENT '1寄出方 2收货方',
  `address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省市区',
  `detail` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '详细地址',
  `contact` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系人',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电话',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_deleted`(`deleted`) USING BTREE,
  INDEX `idx_shipping_order_id`(`shipping_order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '发货订单收寄地址信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mb_shipping_order_image
-- ----------------------------
DROP TABLE IF EXISTS `mb_shipping_order_image`;
CREATE TABLE `mb_shipping_order_image`  (
  `id` bigint(20) NOT NULL,
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `shipping_order_id` bigint(20) NULL DEFAULT NULL COMMENT '发货订单ID',
  `sort` int(10) NULL DEFAULT 0 COMMENT '排序',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片地址',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_deleted`(`deleted`) USING BTREE,
  INDEX `idx_shipping_order_id`(`shipping_order_id`) USING BTREE,
  INDEX `idx_sort`(`sort`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '发货订单图片信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mb_shipping_order_rel
-- ----------------------------
DROP TABLE IF EXISTS `mb_shipping_order_rel`;
CREATE TABLE `mb_shipping_order_rel`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `shipping_order_id` bigint(20) NULL DEFAULT NULL COMMENT '发货订单ID',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '报价订单ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_shipping_order_id`(`shipping_order_id`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1399708488908804098 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '发货订单-报价订单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mb_verify_install
-- ----------------------------
DROP TABLE IF EXISTS `mb_verify_install`;
CREATE TABLE `mb_verify_install`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '安装包名称',
  `application_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用安装需要',
  `type_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型CODE',
  `type_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型名称',
  `channel_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '渠道号',
  `verify_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '验新码地址',
  `down_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下载地址',
  `icon_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图标URL',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '验新包' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mbr_notify_log
-- ----------------------------
DROP TABLE IF EXISTS `mbr_notify_log`;
CREATE TABLE `mbr_notify_log`  (
  `id` bigint(20) NOT NULL,
  `res_body` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '回调报文',
  `third_order_id` bigint(20) NULL DEFAULT NULL COMMENT '第三方订单号',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mbr_order
-- ----------------------------
DROP TABLE IF EXISTS `mbr_order`;
CREATE TABLE `mbr_order`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `store_employee_id` bigint(20) NULL DEFAULT NULL COMMENT '门店员工ID',
  `store_company_id` bigint(20) NULL DEFAULT NULL COMMENT '门店ID',
  `third_order_id` bigint(40) NULL DEFAULT NULL COMMENT '三方订单号',
  `product_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机商品名称',
  `product_spec` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机规格名称',
  `product_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '新机二手机',
  `period` int(11) NULL DEFAULT NULL COMMENT '期数',
  `custom_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户姓名',
  `custom_phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户手机号',
  `id_card` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户身份证',
  `status` int(5) NULL DEFAULT NULL COMMENT '订单状态1待发送2待处理3已处理10已拒绝11标记异常',
  `sub_status` int(5) NULL DEFAULT NULL COMMENT '订单子状态',
  `settle_amount` int(11) NULL DEFAULT NULL COMMENT '商品成本',
  `plan_amount` int(11) NULL DEFAULT NULL COMMENT '方案价格',
  `deposit_amount` int(11) NULL DEFAULT NULL COMMENT '押金价格',
  `ancestors` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组织层级编码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '租机单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mbr_pre_order
-- ----------------------------
DROP TABLE IF EXISTS `mbr_pre_order`;
CREATE TABLE `mbr_pre_order`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `store_employee_id` bigint(20) NULL DEFAULT NULL COMMENT '门店员工ID',
  `store_company_id` bigint(20) NULL DEFAULT NULL COMMENT '门店ID',
  `product_sku_id` bigint(20) NULL DEFAULT NULL COMMENT '商品SKUID',
  `product_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机商品名称',
  `product_spec` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机规格名称',
  `product_type` tinyint(1) NULL DEFAULT NULL COMMENT '1新机2二手机',
  `period` int(11) NULL DEFAULT NULL COMMENT '期数',
  `custom_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户姓名',
  `custom_phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户手机号',
  `id_card` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户身份证',
  `status` int(5) NULL DEFAULT NULL COMMENT '订单状态1待发送2待处理3已处理4已关闭',
  `sub_status` int(5) NULL DEFAULT NULL COMMENT '订单子状态',
  `recycler_employee_id` bigint(20) NULL DEFAULT NULL COMMENT '回收商员工ID',
  `recycler_company_id` bigint(20) NULL DEFAULT NULL COMMENT '回收商ID',
  `quote_log_id` bigint(20) NULL DEFAULT NULL COMMENT '确认报价详情ID',
  `finish_quote_time` datetime(3) NULL DEFAULT NULL COMMENT '确认报价时间',
  `quotable` tinyint(1) NULL DEFAULT 1 COMMENT '是否可报价（超时将关闭报价功能）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '租机下单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mbr_pre_order_quote_log
-- ----------------------------
DROP TABLE IF EXISTS `mbr_pre_order_quote_log`;
CREATE TABLE `mbr_pre_order_quote_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '租机进件订单ID',
  `status` int(5) NULL DEFAULT NULL COMMENT '状态',
  `sub_status` int(5) NULL DEFAULT NULL COMMENT '子状态',
  `quote_time` datetime(3) NULL DEFAULT NULL COMMENT '审核时间',
  `recycler_employee_id` bigint(20) NULL DEFAULT NULL COMMENT '回收商员工ID',
  `recycler_company_id` bigint(20) NULL DEFAULT NULL COMMENT '回收商ID',
  `quoted` tinyint(1) NULL DEFAULT 0 COMMENT '是否已报价，默认为否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1916391430625640450 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '租机平台方报价表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mbr_rental_product_sku
-- ----------------------------
DROP TABLE IF EXISTS `mbr_rental_product_sku`;
CREATE TABLE `mbr_rental_product_sku`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `activated` tinyint(1) NULL DEFAULT 0 COMMENT '是否上架 0否1是',
  `sort` int(10) NULL DEFAULT 0 COMMENT '排序',
  `category_id` bigint(20) NULL DEFAULT NULL COMMENT '分类ID',
  `brand_id` bigint(20) NULL DEFAULT NULL COMMENT '品牌ID',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `spec` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规格名称',
  `retail_price` bigint(20) NULL DEFAULT NULL COMMENT '零售价',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1364988812635951105 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '租机业务手机商品sku表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mbr_shop_code
-- ----------------------------
DROP TABLE IF EXISTS `mbr_shop_code`;
CREATE TABLE `mbr_shop_code`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `shop_id` bigint(40) NULL DEFAULT NULL COMMENT '第三方店铺ID',
  `employee_id` bigint(20) NULL DEFAULT NULL COMMENT '员工ID',
  `qr_code_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '二维码地址',
  `shop_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '店铺名称',
  `settle_link` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店结算地址',
  `license_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '营业执照店铺名称',
  `license_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '营业执照编号',
  `shop_legal_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '营业执照上的经营者姓名',
  `payee` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收款人完整姓名',
  `account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收款人支付宝账号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '门店二维码' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '广播内容',
  `biz_type` int(11) NULL DEFAULT NULL COMMENT '业务类型(1:首页)',
  `user_type` int(11) NULL DEFAULT NULL COMMENT '用户类型(1:所有用户)',
  `link_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '链接地址(保留字段)',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '状态(1:启用,停用)',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '有效截至时间',
  `publish_time` datetime NULL DEFAULT NULL COMMENT '发送时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `sort_index` int(11) NULL DEFAULT NULL COMMENT '排序字段',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '广播公告' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for notice_employee_msg
-- ----------------------------
DROP TABLE IF EXISTS `notice_employee_msg`;
CREATE TABLE `notice_employee_msg`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `employee_id` bigint(20) NOT NULL COMMENT '员工id',
  `biz_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息类型(comm-普通消息,withdraw-提现,sysnotice-系统公告)',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `digest` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '摘要',
  `push_time` datetime NULL DEFAULT NULL COMMENT '推送时间',
  `has_read` tinyint(1) NULL DEFAULT 0 COMMENT '是否已读(0-未读,1-已读)',
  `read_time` datetime NULL DEFAULT NULL COMMENT '已读时间',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '消息内容',
  `picture` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上传文件地址',
  `biz_id` bigint(20) NULL DEFAULT NULL COMMENT '对象id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for notice_user_msg
-- ----------------------------
DROP TABLE IF EXISTS `notice_user_msg`;
CREATE TABLE `notice_user_msg`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '车主id',
  `biz_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息类型(comm-普通消息)',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `push_time` datetime NULL DEFAULT NULL COMMENT '推送时间',
  `has_read` tinyint(1) NULL DEFAULT 0 COMMENT '是否已读(0-未读,1-已读)',
  `read_time` datetime NULL DEFAULT NULL COMMENT '已读时间',
  `content` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息内容',
  `biz_id` bigint(20) NULL DEFAULT NULL COMMENT '对象id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ocpx_notify
-- ----------------------------
DROP TABLE IF EXISTS `ocpx_notify`;
CREATE TABLE `ocpx_notify`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `exc_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'exchange_custom_id',
  `action` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1820287185184161794 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_commission_rule
-- ----------------------------
DROP TABLE IF EXISTS `order_commission_rule`;
CREATE TABLE `order_commission_rule`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单id',
  `commission_type` bigint(20) NULL DEFAULT NULL COMMENT '佣金方案ID',
  `commission_package` bigint(20) NULL DEFAULT NULL COMMENT '佣金套餐方案ID',
  `rule_version` json NULL COMMENT '规则版本',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 310 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for package_info
-- ----------------------------
DROP TABLE IF EXISTS `package_info`;
CREATE TABLE `package_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `biz_type_id` bigint(20) NULL DEFAULT NULL COMMENT '佣金方案类型ID',
  `company_id` bigint(20) NULL DEFAULT NULL COMMENT '公司ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '套餐编码',
  `platform_subsidy_price` int(11) NULL DEFAULT NULL COMMENT '平台补贴',
  `plat_commission_scale` decimal(6, 4) NULL DEFAULT NULL COMMENT '默认分佣比例',
  `plat_commission_fee` int(11) NULL DEFAULT NULL COMMENT '默认最大分佣金额(单位：分)',
  `real_commission_scale` decimal(6, 4) NULL DEFAULT NULL COMMENT '实际分佣比例',
  `real_commission_fee` int(11) NULL DEFAULT NULL COMMENT '实际最大分佣金额(单位：分)',
  `price_low` int(11) NULL DEFAULT NULL COMMENT '价格区间下限(单位：分)',
  `price_high` int(11) NULL DEFAULT NULL COMMENT '价格区间上限(单位：分)',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '1启用 0禁用',
  `order_no` int(11) NULL DEFAULT 0 COMMENT '排序号',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 821 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '套餐信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for package_info_default
-- ----------------------------
DROP TABLE IF EXISTS `package_info_default`;
CREATE TABLE `package_info_default`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `biz_type_id` bigint(20) NULL DEFAULT NULL COMMENT '佣金方案类型ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '套餐编码',
  `platform_subsidy_price` int(11) NULL DEFAULT NULL COMMENT '平台补贴金额(单位：分)',
  `plat_commission_scale` decimal(6, 4) NULL DEFAULT NULL COMMENT '分佣比例',
  `plat_commission_fee` int(11) NULL DEFAULT NULL COMMENT '最大分佣金额(单位：分)',
  `price_low` int(11) NULL DEFAULT NULL COMMENT '价格区间下限(单位：分)',
  `price_high` int(11) NULL DEFAULT NULL COMMENT '价格区间上限(单位：分)',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '1启用 0禁用',
  `order_no` int(11) NULL DEFAULT 0 COMMENT '排序号',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '套餐信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for poster
-- ----------------------------
DROP TABLE IF EXISTS `poster`;
CREATE TABLE `poster`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `poster_type_id` bigint(20) NOT NULL COMMENT '海报类型id',
  `company_id` bigint(20) NULL DEFAULT NULL COMMENT '公司id',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '海报名',
  `poster_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '海报地址',
  `qr_coord_x` float(11, 0) NULL DEFAULT NULL COMMENT '二维码在海报中的X坐标位置',
  `qr_coord_y` float(11, 0) NULL DEFAULT NULL COMMENT '二维码在海报中的Y坐标位置',
  `qr_width` int(11) NULL DEFAULT NULL COMMENT '二维码宽度',
  `qr_height` int(11) NULL DEFAULT NULL COMMENT '二维码高度',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '海报' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for poster_type
-- ----------------------------
DROP TABLE IF EXISTS `poster_type`;
CREATE TABLE `poster_type`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型名称',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `udpate_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for receive_cfg
-- ----------------------------
DROP TABLE IF EXISTS `receive_cfg`;
CREATE TABLE `receive_cfg`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `obj_type` int(11) NULL DEFAULT NULL COMMENT '1:渠道2员工',
  `obj_id` bigint(20) NULL DEFAULT NULL COMMENT '渠道/员工id',
  `pick_self` int(11) NULL DEFAULT NULL COMMENT '1:支持自提 2:不支持自提',
  `pick_express` int(11) NULL DEFAULT NULL COMMENT '1:支持快递2:不支持快递',
  `creator` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `updator` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `def_pick_type` int(11) NULL DEFAULT NULL COMMENT '默认收货方式 1-自提 2-邮寄',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_type_id`(`obj_type`, `obj_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单收货方式' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for receive_deposit
-- ----------------------------
DROP TABLE IF EXISTS `receive_deposit`;
CREATE TABLE `receive_deposit`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `pick_type` int(11) NULL DEFAULT NULL COMMENT '取货方式(1-自提 2-邮寄)',
  `price` int(11) NULL DEFAULT NULL COMMENT '价格',
  `obj_type` int(11) NULL DEFAULT NULL COMMENT '1:渠道2员工',
  `obj_id` bigint(20) NULL DEFAULT NULL COMMENT '渠道/员工id',
  `third_prg` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `refund_amount` int(11) NULL DEFAULT NULL COMMENT '退款金额',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for recycle_data_daily_base
-- ----------------------------
DROP TABLE IF EXISTS `recycle_data_daily_base`;
CREATE TABLE `recycle_data_daily_base`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `trans_amount` bigint(11) NULL DEFAULT 0 COMMENT '交易金额',
  `trans_num` int(10) NULL DEFAULT 0 COMMENT '交易数量',
  `refund_amount` bigint(11) NULL DEFAULT 0 COMMENT '退款金额',
  `quote_price_num` int(10) NULL DEFAULT 0 COMMENT '出价次数',
  `quote_time_spent` bigint(13) NULL DEFAULT 0 COMMENT '报价时长(毫秒)',
  `order_confirm_num` int(10) NULL DEFAULT 0 COMMENT '确认收货量',
  `employee_id` bigint(20) NULL DEFAULT NULL COMMENT '员工ID',
  `day` datetime NULL DEFAULT NULL COMMENT '每天0点',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '回收商统计日看板表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for recycle_recharge_log
-- ----------------------------
DROP TABLE IF EXISTS `recycle_recharge_log`;
CREATE TABLE `recycle_recharge_log`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `company_id` bigint(20) NOT NULL COMMENT '服务商ID',
  `recharge_amount` bigint(20) NULL DEFAULT NULL COMMENT '充值金额',
  `image_url` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '打款凭证',
  `status` tinyint(2) NULL DEFAULT 0 COMMENT '0待审核1拒绝2通过',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_audit
-- ----------------------------
DROP TABLE IF EXISTS `sys_audit`;
CREATE TABLE `sys_audit`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户账号名',
  `operation` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作',
  `old_value` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '旧值',
  `new_value` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '新值',
  `create_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_content
-- ----------------------------
DROP TABLE IF EXISTS `sys_content`;
CREATE TABLE `sys_content`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告标题',
  `digest` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '副标题摘要',
  `type` tinyint(1) NOT NULL DEFAULT 2 COMMENT '1重要2普通',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容详情',
  `picture` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上传文件地址',
  `pulish_date` datetime NULL DEFAULT NULL COMMENT '发布日期',
  `status` tinyint(2) NULL DEFAULT 1 COMMENT '状态1待发布2已发布3已停用',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除 0否1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统发布公告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `dept_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '上级部门ID，一级部门为0',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `order_num` int(11) NULL DEFAULT NULL COMMENT '排序',
  `del_flag` tinyint(4) NULL DEFAULT 0 COMMENT '是否删除  -1：已删除  0：正常',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '标签名',
  `value` varchar(1000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '数据值',
  `type` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '类型',
  `description` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '描述',
  `sort` decimal(10, 0) NULL DEFAULT NULL COMMENT '排序（升序）',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父级编号',
  `create_by` int(11) NULL DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `update_date` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `sys_dict_label`(`name`) USING BTREE,
  INDEX `sys_dict_del_flag`(`del_flag`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1272 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_file
-- ----------------------------
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` int(11) NULL DEFAULT NULL COMMENT '文件类型',
  `url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'URL地址',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 145 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '文件上传' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `operation` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户操作',
  `time` int(11) NULL DEFAULT NULL COMMENT '响应时间',
  `method` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求方法',
  `params` varchar(5000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求参数',
  `ip` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `gmt_create` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 42173 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单URL',
  `perms` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `order_num` int(11) NULL DEFAULT NULL COMMENT '排序',
  `gmt_create` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 646 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_projects
-- ----------------------------
DROP TABLE IF EXISTS `sys_projects`;
CREATE TABLE `sys_projects`  (
  `project_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `project_code` int(11) NULL DEFAULT NULL COMMENT '项目标识',
  `project_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '项目名称',
  `device` int(11) NULL DEFAULT NULL COMMENT '设备（1.Android/2.iOS）',
  `build_code` int(11) NULL DEFAULT NULL COMMENT 'Build号',
  `version_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '版本号',
  `online_time` datetime NULL DEFAULT NULL COMMENT '上线时间',
  `status` int(11) NULL DEFAULT 1 COMMENT '状态（1未开启2当前版本3已过期）',
  `download_url` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '下载地址',
  `readme` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '更新说明',
  `forced_updating` int(11) NULL DEFAULT 1 COMMENT '是否强制更新（1是2否）',
  `creat_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`project_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 192 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `role_sign` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色标识',
  `remark` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `user_id_create` bigint(20) NULL DEFAULT NULL COMMENT '创建用户id',
  `gmt_create` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NULL DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30784 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色与菜单对应关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_task
-- ----------------------------
DROP TABLE IF EXISTS `sys_task`;
CREATE TABLE `sys_task`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cron_expression` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'cron表达式',
  `method_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务调用的方法名',
  `is_concurrent` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务是否有状态',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务描述',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `bean_class` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务执行时调用哪个类的方法 包名+类名',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `job_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务状态',
  `job_group` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务分组',
  `update_date` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `spring_bean` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Spring bean',
  `job_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `work_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工号',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `employee_id` bigint(20) NULL DEFAULT NULL COMMENT '员工id',
  `dept_id` bigint(20) NULL DEFAULT NULL,
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态 0:禁用，1:正常',
  `user_id_create` bigint(20) NULL DEFAULT NULL COMMENT '创建用户id',
  `gmt_create` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `sex` bigint(20) NULL DEFAULT NULL COMMENT '性别',
  `birth` datetime NULL DEFAULT NULL COMMENT '出身日期',
  `pic_id` bigint(20) NULL DEFAULT NULL,
  `live_address` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '现居住地',
  `hobby` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '爱好',
  `province` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '省份',
  `city` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所在城市',
  `district` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所在地区',
  `channel` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '渠道',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 214 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 414 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户与角色对应关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for temporary_employee
-- ----------------------------
DROP TABLE IF EXISTS `temporary_employee`;
CREATE TABLE `temporary_employee`  (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `company_id` bigint(20) NULL DEFAULT NULL COMMENT '公司ID',
  `company_type` int(11) NULL DEFAULT NULL COMMENT '1-公司,2-渠道',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门id',
  `dept_type` int(11) NULL DEFAULT NULL COMMENT '1-管理部门2-普通部门',
  `type` int(11) NULL DEFAULT NULL COMMENT '1-公司或者渠道管理部门管理员2-管理部门员工3-普通部门管理员4-普通部门员工',
  `mobile_number` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `dept_code` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门code',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态 （1 正常 2 已注销 3下线）',
  `head_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像地址',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_mobile_number`(`mobile_number`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '临时员工-游客账号表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for temporary_employee_login
-- ----------------------------
DROP TABLE IF EXISTS `temporary_employee_login`;
CREATE TABLE `temporary_employee_login`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `login_time` datetime NULL DEFAULT NULL COMMENT '登录时间',
  `token` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'token',
  `token_expire` datetime NULL DEFAULT NULL COMMENT 'token过期时间',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for track_company
-- ----------------------------
DROP TABLE IF EXISTS `track_company`;
CREATE TABLE `track_company`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '快递公司',
  `code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '快递公司编码',
  `odr_idx` int(11) NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for verify_code
-- ----------------------------
DROP TABLE IF EXISTS `verify_code`;
CREATE TABLE `verify_code`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `mobile_number` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `code` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '验证码',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '0 未使用 1 已使用',
  `expire_time` datetime NULL DEFAULT NULL COMMENT '过期时间',
  `biz` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_bt_mobile_biz_code`(`mobile_number`, `biz`, `code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for video
-- ----------------------------
DROP TABLE IF EXISTS `video`;
CREATE TABLE `video`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `activated` tinyint(1) NULL DEFAULT 0 COMMENT '是否显示 0隐藏 1显示',
  `publish_time` datetime NULL DEFAULT NULL COMMENT '发布时间',
  `sort` int(11) NULL DEFAULT 0 COMMENT '排序',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型 1办理端app 2车主小程序',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `sub_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '副标题',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封面',
  `video` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '视频',
  `video_length` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '视频时长',
  `video_size` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '视频大小',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_deleted`(`deleted`) USING BTREE,
  INDEX `idx_activated`(`activated`) USING BTREE,
  INDEX `idx_sort`(`sort`) USING BTREE,
  INDEX `idx_type`(`type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '视频表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for withdraw_check_log
-- ----------------------------
DROP TABLE IF EXISTS `withdraw_check_log`;
CREATE TABLE `withdraw_check_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `apply_id` bigint(20) NULL DEFAULT NULL COMMENT '提现申请id',
  `sys_user_id` bigint(20) NULL DEFAULT NULL COMMENT '审核人id',
  `employee_id` bigint(20) NULL DEFAULT NULL COMMENT '审核人id',
  `to_employee_id` bigint(20) NULL DEFAULT NULL COMMENT '归属审核人id(平台审核类型为渠道子后台时有值)',
  `old_status` int(11) NULL DEFAULT NULL COMMENT '审核前状态',
  `new_status` int(11) NULL DEFAULT NULL COMMENT '审核后状态',
  `remark` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '提现审核记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for withdraw_employee_apply
-- ----------------------------
DROP TABLE IF EXISTS `withdraw_employee_apply`;
CREATE TABLE `withdraw_employee_apply`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `apply_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提现单号',
  `employee_id` bigint(20) NULL DEFAULT NULL COMMENT '员工id',
  `amount` bigint(20) NULL DEFAULT NULL COMMENT '提现金额(分)',
  `tax_amount` bigint(20) NULL DEFAULT NULL COMMENT '代扣税额',
  `in_amount` bigint(20) NULL DEFAULT NULL COMMENT '到手金额',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型(1-银行卡、2-支付宝、3-对公账户)',
  `account_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '银行名称',
  `account_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账号',
  `owner_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `id_card` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  `to_public` tinyint(1) NULL DEFAULT NULL COMMENT '是否对公(1-是,0-否)',
  `company_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公司名称(对公)',
  `company_tax_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公司税号(对公)',
  `address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地址(对公)',
  `invoice_flag` tinyint(1) NULL DEFAULT NULL COMMENT '是否需要发票',
  `invoice_type` tinyint(2) NULL DEFAULT NULL COMMENT '发票类型(电子纸质)',
  `invoice_imgs` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '发票图片url(多张)',
  `invoice_upload_time` datetime NULL DEFAULT NULL COMMENT '发票上传时间',
  `express_company` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物流公司',
  `express_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物流单号',
  `invoice_status` int(11) NULL DEFAULT NULL COMMENT '发票审核状态',
  `invoice_check_time` datetime NULL DEFAULT NULL COMMENT '发票审核时间',
  `status` int(11) NULL DEFAULT NULL COMMENT '申请状态',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `to_platform` tinyint(1) NULL DEFAULT 0 COMMENT '平台审核类型(0-后台、1-渠道子后台)',
  `to_employee_id` bigint(20) NULL DEFAULT NULL COMMENT '归属审核人id(平台审核类型为渠道子后台时有值)',
  `ancestors` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组织层级编码',
  `state_snapshot` json NULL COMMENT '状态快照',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1412130217971027969 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '提现申请表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for withdraw_employee_card
-- ----------------------------
DROP TABLE IF EXISTS `withdraw_employee_card`;
CREATE TABLE `withdraw_employee_card`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `employee_id` bigint(20) NULL DEFAULT NULL COMMENT '员工id',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型(1-银行卡、2-支付宝、3-对公账户)',
  `account_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '银行名称',
  `account_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账号',
  `owner_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `id_card` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  `default_flag` tinyint(1) NULL DEFAULT 0 COMMENT '是否设为默认(0-否,1-默认)',
  `latest_time` datetime NULL DEFAULT NULL COMMENT '最近使用时间',
  `company_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公司名称(对公)',
  `company_tax_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公司税号(对公)',
  `address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地址(对公)',
  `status` tinyint(2) NULL DEFAULT NULL COMMENT '状态(1-正常、0-待审核、2-异常)',
  `illegal_types` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '异常类型',
  `pass_time` datetime NULL DEFAULT NULL COMMENT '审核通过时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 115 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '账户提现方式绑定' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for withdraw_trans_log
-- ----------------------------
DROP TABLE IF EXISTS `withdraw_trans_log`;
CREATE TABLE `withdraw_trans_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `apply_id` bigint(20) NULL DEFAULT NULL,
  `trans_type` int(11) NULL DEFAULT NULL COMMENT '打款类型(1-全款、2-首款、3-尾款)',
  `amount` bigint(20) NULL DEFAULT NULL,
  `sys_user_id` bigint(20) NULL DEFAULT NULL,
  `trans_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '打款记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for wm_feedback
-- ----------------------------
DROP TABLE IF EXISTS `wm_feedback`;
CREATE TABLE `wm_feedback`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '意见反馈ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `user_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '用户账号',
  `mobile` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '联系电话',
  `content` varchar(300) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '问题描述',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '状态（0待处理 1已处理）',
  `type` tinyint(1) NOT NULL DEFAULT 0 COMMENT '反馈类型（0其他问题 1开通ETC）',
  `dispose_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '处理者',
  `dispose_type` int(11) NULL DEFAULT NULL COMMENT '处理类型',
  `dispose_content` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '处理内容',
  `lng` decimal(11, 6) NULL DEFAULT NULL COMMENT '经度',
  `lat` decimal(11, 6) NULL DEFAULT NULL COMMENT '纬度',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '意见反馈表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for wm_pay_apply_record
-- ----------------------------
DROP TABLE IF EXISTS `wm_pay_apply_record`;
CREATE TABLE `wm_pay_apply_record`  (
  `id` bigint(20) NOT NULL,
  `create_time` datetime(3) NULL DEFAULT NULL,
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `actual_mch_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '实际商家',
  `pay_enter` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付入口',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '通行订单ID',
  `out_trade_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户系统内部的订单号',
  `biz_type` int(11) NULL DEFAULT NULL COMMENT '业务类型',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE,
  INDEX `idx_trip_order_id`(`order_id`) USING BTREE,
  INDEX `idx_biz_type`(`biz_type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '微信支付用户请求日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for wm_pay_callback_record
-- ----------------------------
DROP TABLE IF EXISTS `wm_pay_callback_record`;
CREATE TABLE `wm_pay_callback_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `original_info` varchar(2550) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信回调原始数据',
  `actual_mchid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '实际商家',
  `pay_enter` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付入口',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `decrypted_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '回调解密后的数据',
  `out_trade_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户系统内部的订单号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 84 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '微信支付回调记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for wm_pay_order
-- ----------------------------
DROP TABLE IF EXISTS `wm_pay_order`;
CREATE TABLE `wm_pay_order`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `etc_order_id` bigint(20) NULL DEFAULT NULL COMMENT 'etc订单id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `open_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户openid',
  `amount` int(11) NULL DEFAULT NULL COMMENT '支付金额（分）',
  `credit` int(11) NULL DEFAULT NULL COMMENT '获得的积分',
  `trade_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '平台交易订单号',
  `trade_no_bak` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '平台交易订单号(备份)',
  `transaction_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信支付订单号',
  `refund_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '平台退款单号',
  `refund_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信退款单号',
  `pay_status` tinyint(4) NULL DEFAULT NULL COMMENT '支付状态（0, 待支付，5, 已支付，10,退款中，15, 已退款，20, 退款失败，25, 已取消）',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `refund_time` datetime NULL DEFAULT NULL COMMENT '退款时间',
  `refund_reason` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '退款原因',
  `operator` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '退款操作人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户办理付款订单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for wm_problem
-- ----------------------------
DROP TABLE IF EXISTS `wm_problem`;
CREATE TABLE `wm_problem`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '常见问题ID',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '标题',
  `detail` longtext CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '详情',
  `order_num` int(11) NOT NULL COMMENT '显示顺序',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '状态（0显示 1不显示）',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '常见问题表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for wm_user_account
-- ----------------------------
DROP TABLE IF EXISTS `wm_user_account`;
CREATE TABLE `wm_user_account`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `open_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'wx openid',
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户头像',
  `nickname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `mobile_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `official_open_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公众号openid',
  `union_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户unionId',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `id_union_id`(`union_id`) USING BTREE,
  UNIQUE INDEX `idx_open_id`(`open_id`) USING BTREE,
  INDEX `idx_mobile_number`(`mobile_number`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for wm_user_login
-- ----------------------------
DROP TABLE IF EXISTS `wm_user_login`;
CREATE TABLE `wm_user_login`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `login_time` datetime NULL DEFAULT NULL COMMENT '登录时间',
  `token` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'token',
  `token_expire` datetime NULL DEFAULT NULL COMMENT 'token过期时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  INDEX `fk_bt_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户登陆' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for wx_gzh_menu
-- ----------------------------
DROP TABLE IF EXISTS `wx_gzh_menu`;
CREATE TABLE `wx_gzh_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '按钮类型(view网页类型、miniprogram小程序类型click点击类型)',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `menu_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单KEY值，用于消息接口推送,click等点击类型必须',
  `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '网页链接,不超过1024字节',
  `media_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `article_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `app_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '小程序appid',
  `page_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '小程序的页面路径',
  `level` int(11) NULL DEFAULT NULL COMMENT '层级',
  `pid` int(11) NULL DEFAULT NULL COMMENT '父按钮id',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态(0-不可用,1-可用)',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '自定义菜单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for wy_notify
-- ----------------------------
DROP TABLE IF EXISTS `wy_notify`;
CREATE TABLE `wy_notify`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '创建者',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `exc_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'exchange_custom_id',
  `action` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1833079148392284162 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
