package com.zxtx.hummer.common.exception;

public enum BizError implements IError {

    OUTER_ERROR(5001, "外部错误"),
    SYSTEM_INTERNAL_ERROR(500, "服务器错误"),
    INVALID_PARAMETER(400, "请输入合法的参数"),
    INVALID_USER(401, "用户名密码错误"),
    STATUS_CHANGED(999, "状态发生变化,请刷新重试"),
    AUTH_NOT_ENOUGH(10000, "系统权限不足"),
    TOKEN_NOT_EXIST(100110, "token已过期"),
    BC_REFUND_ERROR(100111, "北辰退款请求失败"),
    MAT_NAME_EXIST(11000, "物料名称已存在"),

    NOTICE_EXPIRED(12000, "公告已过期"),
    STA_SZ_DAYRep_ERRO(13000, "统计苏州工行业绩日报出错"),
    STA_NJ_DAYRep_ERRO(13001, "统计南京工行业绩日报出错"),


    ORDER_CHANGE_OBU_ERRO(13000, "切换OBU设备失败"),
    ORDER_OBUORDER_NOTEXIST(13001, "OBU订单不存在"),
    ORDER_CHANGE_TYPENOTSUPPORT(13002, "设备切换暂时只支持苏通卡"),
    ORDER_NOT_ACTIVE(13003, "只有obu激活过才能更换设备"),
    USER_LOGIN_COUNT_ERROR(13004, "连续输入错误{0}次验证码，账号锁定24小时"),
    USER_IP_LOGIN_COUNT_ERROR(13005, "同个IP连续请求{0}次账号密码错误，IP被锁定1小时"),

    JS_PERF_CMP_CFGED(14001, "渠道已经配置过业绩归属"),
    JS_PERF_ISNULL_CMP(14002, "渠道公司不能为空"),
    JS_PERF_ISNULL_OPER(14003, "业绩归属不能为空"),

    ORDER_ALREADY_CONFIRM(16005, "订单已确认"),
    ORDER_ALREADY_REFUSE_RECEIVE(16006, "订单已拒绝收货"),
    ORDER_ALREADY_REFUSE_SEND(16007, "订单已拒绝出货"),

    Company_not_exist(18801, "公司不存在"),
    COMPANY_STATUS_CHANGE(18802, "公司状态变更，请刷新后重试"),
    EM_EXIST(18803, "该手机号已经注册，不能成为代理管理员。请更换手机号，或者咨询客服电话：17311253600"),
    CREATE_DEPT_ERROR(18804, "创建部门失败，请稍后重试或联系管理员"),
    COMAPNY_PAY_STATUS_ERRO(18805, "当前渠道状态，不支持更新押金"),
    CHANNEL_STOCK_STATUS_CHANGE(18806, "渠道物料上限发生变化，更新物料上限失败，请刷新重试"),
    COMPANY_SN_EXIST(18807, "该渠道编号已经存在，请修改后再提交"),
    MAT_LIMT_FORMAT_ERRO(18808, "物料上限必须为大于0的整数"),
    MAT_LOG_STAUS_CHANGE(18809, "待处理货运单列表状态发生变化"),


    COMPANY_STATUS_HAVING_AUDIT(18900, "公司已经审核通过，不能再操作!!"),

    USER_EXIST(19001, "手机号已注册"),
    DEPT_NOT_EXIST(19002, "部门不存在"),

    DEPT_EXIST(19003, "该部门名称已经存在，建议在名称后面加数字"),
    ID_NAME_NOT_MATCH(19004, "该手机号已注册，你填写的姓名和手机号不匹配"),
    REPLACE_MANAGER(19005, "由于该员工同时管理其他部门，暂时无法调整"),
    COMPANY_EXIST(19006, "该渠道名称已经存在，请修改后再提交"),

    EM_CANT_CANNEL(19007, "该手机号已经注册，不能成为代理管理员。请更换手机号，或者咨询客服电话：17311253600"),
    Name_empty(19008, "请填写管理员姓名"),
    Mobile_EMPTY(19009, "请填写管理员手机号"),

    COMPANY_NOT_EXIST(19010, "渠道不存在!"),
    NO_MANAGER(19018, "该部门没有管理员,不能创建子部门"),
    MOBILE_LENGTH_ERROR(19019, "请输入正确的手机号长度"),
    ERROR_PARENT_DEPT(19120, "不能将自己设置为自己的所属部门"),


    DPT_SUBCMP_EXIST(19200, "部门下包含渠道,不能移到其它渠道部门下"),
    EM_FREEZE(19209, "该账号已被冻结，暂不能成为管理员"),
    DEPT_MAGER_CANNOT_MOV(19210, "管理部门不能移动"),

    ORDER_NOT_EXIST(21000, "订单不存在"),
    CLOSE_ORDER_ALREADY_CLOSED_ERROR(21002, "订单已关闭"),
    WX_PAY_ERROR(50000, "微信支付出错，请稍后重试"),
    WX_REFUND_ERROR(50001, "微信退款出错，请稍后重试"),

    ORDER_HAS_BEEN_AUDITED(21001, "订单审核状态已变更，请重新刷新"),
    IF_EXP_HUG_ZTO_ROUT_ERRO(60001, "中通快运路由接口请求失败"),
    IF_EXP_HUG_ZTO_ADDORDER_ERRO(60002, "中通快运生成运单接口请求失败"),
    IF_EXP_HUG_DEST_NOT_MATCH(60003, "目的地址找不到匹配网点"),
    SIGN_ERROR(12016, "签名错误!"),

    IF_EXP_SMALL_ZTO_BILLCODE_ERRO(61001, "中通快递生成运单接口请求失败"),
    IF_EXP_SMALL_ZTO_BAGAddrMark_ERRO(61002, "中通快递集包地大头笔接口请求失败"),
    IF_EXP_SMALL_ZTO_PRINT_ERRO(61003, "中通快递打印接口请求失败"),
    IMPORT_EXP_SMALL_ERRO(62001, "导入快递运单号失败"),
    IMPORT_FILE_PATTERN_ERRO(62002, "导入文件格式错误"),
    IMPORT_FAILED(62003, "导入失败"),
    WITHDRAW_IMPORT_FAILED(62004, "导入青蚨对账单失败"),

    CONF_DEVICE_UPLOAD_ERROR(70001, "配置设备上报失败！请确认用户在个人中心是否绑定了签约的手机号"),
    IMPORT_BLACK_LIST_ERROR(70001, "导入黑名单处理失败"),

    VEH_EXIST(80001, "车牌号已存在"),
    Account_not_exist(80002, "账户不存在!"),
    OBU_ORDER_NOT_EXIST(80003, "obu订单不存在"),
    ACCOUNT_EXIST(80004, "账户已存在"),
    PLATE_LENGTH_ERROR(80005, "车牌长度异常"),
    PLATE_RULE_ERROR(80006, "车牌规则不合法"),
    ORDER_STATUS_ERROR(80007, "订单状态异常，请刷新重试"),
    PLATE_EXIST(80008, "该车辆已完成银行签约!"),

    SEC_DEVICE_ALREADY_USED(80009, "设备已使用"),
    SEC_VEH_ALREADY_USED(80010, "车辆已使用"),
    SEC_OVER_MAX_TOTAL_RES_NUM(80011, "当日总资源分配已达上限"),
    SEC_OVER_MAX_ACC_RES_NUM(80012, "所有账户资源分配已达上限"),
    SEC_ACC_ERROR(80013, "无可用账户或所有账户总签约次数已达上限"),
    SEC_TIME_RANGE_ERROR(80014, "在{0}时间段才能申请"),

    IMPORT_QL_VEHLIC_ERROR(30001, "导入齐鲁车辆行驶证失败"),

    DEVICE_EN(30002, "设备不足！"),
    NO_ACCOUNT(90002, "无账户可用！"),
    CANNOT_EXEC(30003, "进行中，请稍后重试!"),
    IP_IN_PROCESS(30004, "当前ip进行中，请重试"),
    CANT_EXEC(30005, "无法执行"),
    FILE_UPLOAD_ERROR(30006, "文件上传失败"),
    IMPORT_JH_DATA_ERROR(30007, "导入交行数据失败"),
    RUNNING(30008, "运行中请稍后！"),
    NET_ERROR(30009, "加解密失败！"),
    HIGHWAY_NET_ERROR(30010, "高速网络异常！"),
    SEC_IDCARD_ERROR(30011, "身份证号已存在！"),
    SEC_EMP_ERROR(30012, "只能新增一次账户信息"),

    CHANNEL_EXIST(90001, "渠道已存在!"),
    DEVICE_IMPORT_FAILED(90002, "设备码导入失败"),
    CHANNEL_NOT_EXIST(90003, "渠道名称不存在"),

    ETC_NOT_EXIST(40001, "ETC不存在!"),
    ETC_CANCELED(40002, "设备已注销"),
    REMOTE_ERROR(40003, "{0}"),
    OBU_NOT_EXIST(40004, "OBU不存在！"),


    ACTIVE_APPLY_EXIST_WAIT_OR_HASDEAL(40005, "该etc订单编号已存在待处理申诉单或已处理申诉单！"),
    ACTIVE_APPLY_NOT_EXIST(40006, "该申诉单不存在！"),

    ACTIVE_APPLY_DEAL_STATUS_CHANGES(40007, "该申诉单状态已发生变化，请刷新！"),

    ACTIVE_APPLY_DEAL_STATUS_ETCORDER_HAS_ACTIVE(40008, "该ETC办理订单已激活，不能再提交激活申诉！"),


    ACTIVE_APPLY_DEAL_NOT_PAY_REFUND(40009, "该ETC办理订单不是已退款订单，不支持再输入支付商户流水号！"),

    ACTIVE_APPLY_DEAL_HAS_PAY(40010, "该ETC办理订单是已支付订单，不支持再输入支付商户流水号！"),

    ACTIVE_APPLY_DEAL_HASNOT_CREATE_PAY_ORDER(40011, "该ETC办理订单未创建支付订单，不支持输入支付商户流水号！"),
    ACTIVE_APPLY_DEAL_HASNOT_CREATE_PAY_ORDER_2(40012, "该ETC办理订单未创建支付订单！"),

    ACTIVE_APPLY_DEAL_NOT_HAS_PAY(40013, "该ETC办理订单是未支付订单！"),

    //分佣
    COMMISSION_MEMBER_REPEATE(40000, "用户已经配置过方案了"),
    COMMISSION_MEMBER_MATCH(40001, "只能查看自己创建的方案"),
    COMMISSION_MEMBER_DELETE(40002, "只能删除自己创建的方案"),

    COMMISSION_MEMBER_NOT_MATCH(40003, "不是自己创建的方案"),

    COMMISSION_MEMBER_NOT_DIRECT(40004, "不是自己的直系下级"),

    COMMISSION_CONF_MAX(40005, "分佣不得超过上级分配的数额"),

    COMMISSION_CONF_NOT_EXIST(40006, "不存在可供配置的套餐"),

    COMMISSION_PLANNAME_REPEAT(40007, "方案名称重复了"),

    COMMISSION_MEMBER_PLAN_NOT_EXIST(40008, "请联系上级配置方案"),

    COMMISSION_NOT_ADMIN(40009, "需要平台账户"),

    COMMISSION_CONF_MAX_LIMIT(40010, "推广分佣不得超过最大数额"),

    COMMISSION_CONF_SERVICE_MAX_LIMIT(40011, "服务费分佣不得超过最大数额"),

    COMMISSION_MEMBER_PLAN_PUBLISH_NOT_EXIST(40012, "请联系上级配置推广佣金方案"),

    ;


    int code;
    String message;

    BizError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}