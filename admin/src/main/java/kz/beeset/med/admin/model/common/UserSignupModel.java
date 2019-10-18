package kz.beeset.med.admin.model.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/*
 *
 * user signup model
 * */

@Getter
@Setter
@EqualsAndHashCode
public class UserSignupModel {
    String username;
    private String iin;
    private String email;
    private String mobilePhone;
    private String firstname;
    private String lastname;
    private String middlename;
    private String password;
}
