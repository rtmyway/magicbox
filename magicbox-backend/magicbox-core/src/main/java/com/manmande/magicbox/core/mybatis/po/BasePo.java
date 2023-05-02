package com.manmande.magicbox.core.mybatis.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

/**
 * mybatis实体顶层基础类,仅包含id属性
 */
public abstract class BasePo implements Serializable {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
