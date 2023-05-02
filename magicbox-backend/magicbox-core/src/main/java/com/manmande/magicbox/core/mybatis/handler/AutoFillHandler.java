package com.manmande.magicbox.core.mybatis.handler;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.manmande.magicbox.core.mybatis.po.AuditPo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AutoFillHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        if (metaObject.getOriginalObject() instanceof AuditPo) {
            // 新增记录时,AuditPo实体,创建人,创建时间
            this.setFieldValByName("createdBy", "", metaObject);
            this.setFieldValByName("createdAt", DateUtil.date(), metaObject);

            // 设置备注默认值
            Object description = this.getFieldValByName("description", metaObject);
            if (description == null) {
                this.setFieldValByName("description","", metaObject);
            }

            // 设置有效默认值true
            Object isActive = this.getFieldValByName("isActive", metaObject);
            if (isActive == null) {
                this.setFieldValByName("isActive", Boolean.TRUE, metaObject);
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject.getOriginalObject() instanceof AuditPo) {
            // 更新记录时,AuditPo实体,补全更新人和更新时间
            this.setFieldValByName("updatedBy", "none", metaObject);
            this.setFieldValByName("updatedAt", DateUtil.date(), metaObject);
        }
    }
}
