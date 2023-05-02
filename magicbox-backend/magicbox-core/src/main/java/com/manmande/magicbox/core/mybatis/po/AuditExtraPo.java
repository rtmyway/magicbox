package com.manmande.magicbox.core.mybatis.po;

import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import java.util.Date;

/**
 * 高阶审计实体，增加逻辑删除标记，删除人，删除时间
 */
@Data
public class AuditExtraPo extends AuditPo {
    /**
     * 逻辑删除标记
     */
    private Boolean isDeleted;

    /**
     * 删除人
     */
    private String deletedBy;

    /**
     * 删除时间
     */
    private Date deletedAt;

    /**
     * 版本控制
     */
    @Version
    private Integer opVersion;
}
