package kz.beeset.med.admin.model.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/*
*
* Bean for create or delete role item in user org group
* */

@Getter
@Setter
@EqualsAndHashCode
public class UserOrgRoleUnit {
    private String userId;
    private String orgId;
    private String roleId;
}
