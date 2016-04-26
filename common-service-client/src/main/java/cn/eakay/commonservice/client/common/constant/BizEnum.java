package cn.eakay.commonservice.client.common.constant;

/**
 * 业务标识 枚举
 * Created by xugang on 16/4/22.
 */
public enum BizEnum {

    MERCHANT_ORDER("order", 1),
    MERCHANT_CAR("car", 2),

    UNKOWN("unkown", -1);

    private String biz;
    private int code;

    private BizEnum(String biz, int code) {
        this.biz = biz;
        this.code = code;
    }

    public String getBiz() {
        return biz;
    }

    public int getCode() {
        return code;
    }

    public static BizEnum getEnum(String biz) {
        for (BizEnum enum_ : BizEnum.values()) {
            if (enum_.getBiz().equalsIgnoreCase(biz)) {
                return enum_;
            }
        }
        return UNKOWN;
    }

}
