package kz.beeset.med.device.utils.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {

    public static void main(String[] args) {
//        System.out.println(new EmailValidator().validate("ads@asd.qw"));
//        System.out.println(new EmailValidator().validate("@asd.qw"));
//        System.out.println(new EmailValidator().validate("ads@.qw"));
//        System.out.println(new EmailValidator().validate("ads@asd."));
//        System.out.println(new EmailValidator().validate("ads@asqw"));
//        System.out.println(new EmailValidator().validate("adsasqw"));
//        System.out.println(new EmailValidator().validate("ads@as..qw"));
    }

    private Pattern pattern;
    private Matcher matcher;
    private static final String EMAIL_PATTERN =
            //            "^[_A-Za-z0-9]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-_]+(\\.[A-Za-z0-9-_]+)*(\\.[A-Za-z]{2,})$";
            //            "^([0-9a-zA-Z]([-\\.\\w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-\\w]*[0-9a-zA-Z]\\.)+[a-zA-Z]{2,9})$";
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-_]{1,}+(\\.[A-Za-z0-9-_]+)*(\\.[A-Za-z]{2,})$";

    public EmailValidator() {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    /**
     * Validate hex with regular expression
     *
     * @param hex hex for validation
     * @return true valid hex, false invalid hex
     */
    public boolean validate(final String hex) {

        matcher = pattern.matcher(hex);
        return matcher.matches();

    }
}
