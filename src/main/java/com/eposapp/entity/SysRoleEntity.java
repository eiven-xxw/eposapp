package com.eposapp.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "sys_role", schema = "eposapp", catalog = "")
public class SysRoleEntity extends BaseEntity{
    private String orgId;
    private String remark;


    @Basic
    @Column(name = "orgId", nullable = false, length = 18)
    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }


    @Basic
    @Column(name = "remark", nullable = true, length = 255)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
