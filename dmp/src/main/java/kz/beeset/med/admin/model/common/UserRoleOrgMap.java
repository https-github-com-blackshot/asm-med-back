package kz.beeset.med.admin.model.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/*
 *
 * Bean for create and save org&role map item in user
 * */

@Getter
@Setter
@EqualsAndHashCode
public class UserRoleOrgMap {
    String userId;
    String orgId;
    List<String> roles;
}
