package cn.eakay.commonservice.client.common.constant;

/**
 * 业务标识下属的key 枚举
 * 如 biz = order key = 取车 还车
 * 如 biz = car key = 还车 车辆
 *
 * Created by xugang on 16/4/22.
 */
public enum BizKeyEnum {

    TAKE_CAR("取车", 1),
    RETURN_CAR("还车", 2),
    VEHICLE("车辆", 3),
    VEHICLE_DAMAGE("车辆损伤", 4),
    USER_CERT("用户证件", 5),
    VEHICLE_FEEDBACK("车况反馈", 6),

    UNKOWN("未知", -1);

    private String key;
    private int code;

    private BizKeyEnum(String key, int code) {
        this.key = key;
        this.code = code;
    }

    public String getKey() {
        return key;
    }

    public int getCode() {
        return code;
    }

    public static BizKeyEnum getEnum(String key) {
        for (BizKeyEnum enum_ : BizKeyEnum.values()) {
            if (enum_.getKey().equalsIgnoreCase(key)) {
                return enum_;
            }
        }
        return UNKOWN;
    }

}
