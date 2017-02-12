package hu.bme.aut.moodernize.c2j;

public class TransformUtil {
	public static String capitalizeFirst(String s) {
		if (s.isEmpty())
			return s;
		else if (s.length() < 2)
			return s.toUpperCase();
		else
			return Character.toUpperCase(s.charAt(0)) + s.substring(1);
	}

}
