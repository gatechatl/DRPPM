package statistics.general;

public class QRankOrderStatistics {

	public static void main(String[] args) {
		
		double[] rank = {3, 1, 2, 1, 3};
		double[] rank2 = {1000, 1, 3, 2, 1};
		//double[] rank2 = {1000, 1, 2, 1};
		double qstat = factorial(rank2.length) * V(rank2.length, rank2);
		System.out.println("Q-statistics: " + qstat);
		
	}
	public static double V(int k, double[] rank) {
		if (k == 0) {
			return 1.0;
		}
		double return_value = 0.0;
		for (int i = 1; i <= k; i++) {
			
			return_value += Math.pow(-1, (i - 1)) * (V(k - 1, rank) / factorial(i)) * rank[rank.length - k];
			System.out.println(Math.pow(-1, (i - 1)));
			System.out.println("Factorial: " + factorial(i));
			System.out.println(i + "\t" + return_value);
		}
		return return_value;
	}
	public static double factorial(int number) {
		int fact = 1;
		 for (int i = 1;i <= number;i++){    
		      fact = fact * i;    
		 }
		 return fact;
	}
}
