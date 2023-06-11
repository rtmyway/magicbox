package com.manmande.magicbox.card.constant;

public enum CardItem {
    /**
     * 默认
     */
    DEFAULT("DEFAULT", "至尊88888", "21880001", 199900),


    /**
     * 未知数
     */
    UNKNOWN("UNKNOWN", "", "", 1);


    private String code;
    private String text;
    private String beginNo;
    private Integer price;

    CardItem(String code, String text, String beginNo, Integer price) {
        this.code = code;
        this.text = text;
        this.beginNo = beginNo;
        this.price = price;
    }

    public static CardItem getByCode(String code) {
        CardItem result = CardItem.UNKNOWN;
        for (CardItem item : CardItem.values()) {
            if (item.getCode().equalsIgnoreCase(code)) {
                result = item;
                break;
            }
        }
        return result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getBeginNo() {
        return beginNo;
    }

    public void setBeginNo(String beginNo) {
        this.beginNo = beginNo;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
