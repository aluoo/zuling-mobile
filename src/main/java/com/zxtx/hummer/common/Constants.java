package com.zxtx.hummer.common;


public class Constants {

    public static final String Level_key = "channel_level";

    //蓝海公司id
    public static Long LAN_HAI_CMP_ID = 100000000000000000L;

    //蓝海管理部门id
    public static Long LAN_HAI_MANDEPT_ID = 100000000000000000L;

    //蓝海管理部门code
    public static String LAN_HAI_MANDEPT_CODE = "1";

    //蓝海队长id
    public static Long LAN_HAI_CAPTAIN_ID = 100000000000000000L;

    //物料中心部门id
    public static Long MAT_CENTER_DEPT_ID = 100000000000000100L;

    //物料中心人员id
    public static Long MAT_CENTER_EMP_ID = 100000000000000100L;

    //物料中心人员名字
    public static String MAT_CENTER_EMP_NAME = "51ETC物料中心";

    //小程序自主推广部门
    public static Long LAN_HAI_USER_DEPT_ID = 100000000000000200L;

    //小程序自主推广部门code
    public static String LAN_HAI_USER_DEPT_CODE = "1-6";

    //小程序自主推广人员ID
    public static Long LAN_HAI_USER_EM_ID = 100000000000000200L;


    //物料中心默认收货地址id
    public static Long MAT_CENTER_DEF_ADDRESS_ID = 200000000000000100L;

    public static final String dict_name_cache_prefix = "dict:name:";

    public static final String bankNotSameKey = "bankNotSame";

    public static final String dict_type_cache_prefix = "dict:type:";

    public static final String cancel_reason_type = "cancel_reason";

    //渠道默认物料上限
    public static Integer CHANNEL_MAT_LIMIT_DEFAULT = 1;

    public static final String lanhai = "lanhai";

    public static final String partner_channel_pengwei = "pengwei";

    public static Integer EXPRESS_HUGE_INSURED_AMOUNT = 1; //TODO

    public static Integer EXPRESS_HUGE_PACKAGE_SIZE = 100;

    public static String EXPRESS_HUGE_ZTO_NAME = "中通";

    public static Long EXPRESS_HUGE_ZTO_ID = 596347732405374979L;

    //不扣物料的obu order订单，用于解决一批有问题的obu编号设备
    public static final String no_stock_obu_order_ids_redis_key = "obuOrder:noStock:ids";

    /*
     * 员工redis缓存token前缀
     */
    public static final String EM_TOKEN_PREFIX = "token:";

    public static final String SZ_CMP_ID_REDIS = "pangu:operator:cmpids";

    public static final String erro_redis_prefix = "erro:posmsg";

    public static final String dic_type_oper = "operator";

    public static final String LOGIN_EROR_COUNT_REDIS = "nvwa:loginerror:";

    public static final String DEPOSIT_REFUND_WXREFUND = "deposit:refund:wxRefund:";

    public static final String DEPOSIT_AFTER_SALE_REFUND = "deposit:afterSale:refund:";
    public static final String WO_CANCEL_ORDER_REFUND = "wo:cancel:order:refund:";
    public static final String WO_CANCEL_ORDER_MARK_REFUND = "wo:cancel:order:mark:refund:";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";
    /**
     * jwtToken
     */
    public static final String LOGIN_USER_JWT_TOKEN = "login:jwt:token:%s:%s";

}