package misc;

public class StringManipulationTools {

	public static String removeFirst(String str) {
		String result = "";
		String[] split = str.split("\t");
		if (split.length >= 2) {
			result = split[1];
		}
		for (int i = 2; i < split.length ; i++) {
			result += "\t" + split[i];
		}
		return result;
	}
	public static String repHeader(String str, String tag) {
		String result = "";
		String[] split = str.split("\t");
		if (split.length >= 2) {
			result = tag;
		}
		for (int i = 2; i < split.length ; i++) {
			result += "\t" + tag;
		}
		return result;
	}
}
