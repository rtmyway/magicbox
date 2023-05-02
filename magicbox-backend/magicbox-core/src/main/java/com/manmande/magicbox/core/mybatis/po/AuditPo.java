package com.manmande.magicbox.core.mybatis.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * 最基本的审计实体，包含状态, 备注,创建人,创建时间,更新人,更新时间
 */
@Data
public class AuditPo extends BasePo {
    /**
     * 有效状态
     */
    @TableField(fill = FieldFill.INSERT) //创建时自动填充
    private Boolean isActive;

    /**
     * 备注
     */
    @TableField(fill = FieldFill.INSERT) //创建时自动填充
    private String description;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT) //创建时自动填充
    private String createdBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT) //创建时自动填充
    private Date createdAt;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.UPDATE) //更新时自动填充
    private String updatedBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE) //更新时自动填充
    private Date updatedAt;
}
