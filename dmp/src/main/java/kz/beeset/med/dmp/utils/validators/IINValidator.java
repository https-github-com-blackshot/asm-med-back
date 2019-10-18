package kz.beeset.med.dmp.utils.validators;

import java.util.regex.Pattern;

public class IINValidator {

	public static void main(String[] args) {
//		System.out.println(isValidINN("911017300421"));
//		System.out.println(isValidINN("911017300423"));
//		System.out.println(isValidINN("910918300477"));
//		System.out.println(isValidINN("980614301039"));
//		System.out.println(isValidINN("123321123321"));
	}

	private static final Pattern innPatter = Pattern.compile("\\d{10}|\\d{12}");
	public static boolean isValidINN(String inn) {
		inn = inn.trim();
		if (!innPatter.matcher(inn).matches()) {
			return false;
		}
		int length = inn.length();
		if (length == 12) {
			return INNStep(inn, 2, 1) && INNStep(inn, 1, 0);
		} else {
			return INNStep(inn, 1, 2);
		}
	}

	private static final int[] checkArr = new int[] {3,7,2,4,10,3,5,9,4,6,8};

	private static boolean INNStep(String inn, int offset, int arrOffset) {
		int sum = 0;
		int length = inn.length();
		for (int i = 0; i < length - offset; i++) {
			sum += (inn.charAt(i) - '0') * checkArr[i + arrOffset];
		}
		return (sum % 11) % 10 == inn.charAt(length - offset) - '0';
	}
}
