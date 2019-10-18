package kz.beeset.med.admin.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProfileInfo {
    private String idn;
    private String fullName;
    private String email;
    private String sex;
    private String phoneNumber;
    private LocalDate birthday;
}
