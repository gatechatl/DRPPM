package protein.features.charge;

public class CalculateCharge {

	public static double comprehensive_charge(String seq) {
		double total = 0;
		for (int i = 0; i < seq.length(); i++) {
			total += calculate_charge(seq.substring(i, i + 1));
		}
		return total;
	}
	public static double calculate_charge(String s) {
		if (s.equals("R")) {
			return 1.0;
		}
		if (s.equals("H")) {
			return 0.5;
		}
		if (s.equals("K")) {
			return 1.0;
		}
		if (s.equals("D")) {
			return -1.0;
		}
		if (s.equals("E")) {
			return -1.0;
		}
		return 0.0;
	}
}
