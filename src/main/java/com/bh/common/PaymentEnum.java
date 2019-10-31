package com.bh.common;

public enum PaymentEnum {
    PAYMENT_ONLINE(1,"在线支付"),
    PAYMENT_OFFLINE(2,"货到付款")
    ;
    private int type;
    private String desc;
    PaymentEnum(int type, String desc){
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static PaymentEnum codeOf(Integer type){
        for (PaymentEnum paymentEnum:values()){
            if (paymentEnum.getType() == type)
                return paymentEnum;
        }
        return null;
    }
}
