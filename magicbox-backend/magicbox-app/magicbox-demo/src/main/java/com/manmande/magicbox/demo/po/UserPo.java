package com.manmande.magicbox.demo.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.manmande.magicbox.core.mybatis.po.AuditPo;
import lombok.Data;

@Data
@TableName("ts_user")
public class UserPo extends AuditPo {
    private String username;
    private String password;
}
