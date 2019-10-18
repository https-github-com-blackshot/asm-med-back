package kz.beeset.med.gateway2.util.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneValidatorBrackets {

    private Pattern pattern;
    private Matcher matcher;
    private static final String PHONE_PATTERN =
            "^\\(d{3}\\)\\d{7}";

    public PhoneValidatorBrackets() {
        pattern = Pattern.compile(PHONE_PATTERN);
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
