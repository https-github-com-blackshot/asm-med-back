package kz.beeset.med.admin.utils.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameValidator{
 
	  private Pattern pattern;
	  private Matcher matcher;
 
	  public static final String NAME_PATTERN = 
                    "^[-а-яА-ЯёЁәіңғүұқөһӘІҢҒҮҰҚӨҺa-zA-Z\\s\\']{1,50}$";
 
	  public NameValidator(){
		  pattern = Pattern.compile(NAME_PATTERN);
	  }
 
	  /**
	   * Validate hex with regular expression
	   * @param hex hex for validation
	   * @return true valid hex, false invalid hex
	   */
	  public boolean validate(final String hex){
 
		  matcher = pattern.matcher(hex);
		  return matcher.matches();
 
	  }
}
