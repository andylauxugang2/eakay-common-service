package cn.eakay.commonservice.client.common.constant;

/**
 * 业务标识 枚举
 * Created by xugang on 16/4/22.
 */
public enum BizEnum {

    BIZ_ORDER("order", 1, "EAKAYORDER"),
    BIZ_CAR("car", 2, "EAKAYORDER"),
    BIZ_USER("user", 3, "EAKAYUSER"),
    BIZ_MALL("mall", 4, "EAKAYMALL"),

    UNKOWN("unkown", -1, "UNKOWN");

    private String biz;
    private int code;
    private String group;

    private BizEnum(String biz, int code, String group) {
        this.biz = biz;
        this.code = code;
        this.group = group;
    }

    public String getBiz() {
        return biz;
    }

    public int getCode() {
        return code;
    }

    public String getGroup() {
        return group;
    }

    public static BizEnum getEnum(String biz) {
        for (BizEnum enum_ : BizEnum.values()) {
            if (enum_.getBiz().equalsIgnoreCase(biz)) {
                return enum_;
            }
        }
        return UNKOWN;
    }

    public static BizEnum getEnumByCode(int code) {
        for (BizEnum enum_ : BizEnum.values()) {
            if (enum_.getCode() == code) {
                return enum_;
            }
        }
        return UNKOWN;
    }
}
