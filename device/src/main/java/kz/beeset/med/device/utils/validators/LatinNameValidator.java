package kz.beeset.med.device.utils.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LatinNameValidator {

    private final Pattern pattern;
    private Matcher matcher;

    private static final String NAME_PATTERN
            = "^[a-zA-Z]+\\s[a-zA-Z]+-?[a-zA-Z]+$";

    public LatinNameValidator() {
        pattern = Pattern.compile(NAME_PATTERN);
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
