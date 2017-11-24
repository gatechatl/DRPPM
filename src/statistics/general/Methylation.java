package statistics.general;

public class Methylation {

	public static double Beta2M(double beta) {
		return MathTools.log2((beta)/(1 - beta) + 0.0000000001);
	}
}
