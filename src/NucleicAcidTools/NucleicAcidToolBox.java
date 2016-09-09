package NucleicAcidTools;

/**
 * Collection of nucleic acid toolset
 * @author tshaw
 *
 */
public class NucleicAcidToolBox {

	public static String getReverseComplement(String input) {
		String result = "";
		String upper_input = input.toUpperCase();
		for (int i = upper_input.length() - 1; i >= 0; i--) {
			String s = upper_input.substring(i, i + 1);
			if (s.equals("G")) {
				result += "C";
			} else if (s.equals("C")) {
				result += "G";
			} else if (s.equals("A")) {
				result += "T";
			} else if (s.equals("T")) {
				result += "A";
			}
		}
		return result;
	}
}
