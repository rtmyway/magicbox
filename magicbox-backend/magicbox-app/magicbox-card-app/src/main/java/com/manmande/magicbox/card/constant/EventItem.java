package com.manmande.magicbox.card.constant;

public enum EventItem {
    APPLY("APPLY", "提交", 1),
    CONFIRM("CONFIRM", "确认", 2),
    DELIVER("DELIVER", "已发货", 3),
    FINISH("FINISH", "已完成", 4),

    /**
     * 未知数
     */
    UNKNOWN("UNKNOWN", "", 0);

    private String code;
    private String text;
    private Integer order;

    EventItem(String code, String text, Integer order) {
        this.code = code;
        this.text = text;
        this.order = order;
    }

    public static EventItem getByCode(String code) {
        EventItem result = EventItem.UNKNOWN;
        for (EventItem item : EventItem.values()) {
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

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
