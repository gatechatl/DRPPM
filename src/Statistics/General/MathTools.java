package statistics.general;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.inference.TTest;
import org.apache.commons.math3.special.Erf;


public class MathTools {
	public static void main(String[] args) {
		/*ArrayList<Double> pval = new ArrayList<Double>();
		pval.add(0.001);
		pval.add(0.001);
		pval.add(0.001);
		ArrayList<Double> new_pval = Bonferroni_correction(pval);
		Iterator itr = new_pval.iterator();
		while (itr.hasNext()) {
			double val = (Double)itr.next();
			System.out.println(val);
		}*/
		double[] vals = {-1, -2, -3, 1, 2};
		//System.out.println(standardError(vals));
		//System.out.println(log2(4));
		double[] val1 = {1, 2, 3, 4};
		double[] val2 = {5, 4, 3, 3};
		//double[] val2 = {3, 3, 4, 5};
		//System.out.println(2 - PearsonCorrelPvalue(val1, val2));
		
		double[] val3 = {0.619229099, -0.796565867, 1.135480384, 0.420745752, -2.290236978, -1.791960526, -2.934336201, -2.823691462};
		double[] val3_zscore = zscores(val3);
		/*for (double score: val3_zscore) {
			System.out.println(score);
		}*/
		System.out.println(median(vals));
		//System.out.println();
	}
	
	public static double[] zscores(double[] values) {
		double mean = mean(values);
		double sd = standardDeviation(values);
		double[] zscores = new double[values.length];
		for (int i = 0; i < values.length; i++) {
			zscores[i] = (values[i] - mean) / sd; 
		}
		return zscores;
	}
	
	public static double[] log2zscores(double[] values) {
		double[] new_values = new double[values.length];
		for (int i = 0; i < values.length; i++) {
			new_values[i] = log2(values[i] + 0.000001);
		}
		double mean = mean(new_values);
		double sd = standardDeviation(new_values);
		double[] zscores = new double[values.length];
		for (int i = 0; i < new_values.length; i++) {
			zscores[i] = (new_values[i] - mean) / sd; 
		}
		return zscores;
	}
	
	/** 
	 * Check if str is numeric
	 * https://stackoverflow.com/questions/1102891/how-to-check-if-a-string-is-nuameric-in-java
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str)
	{
	  NumberFormat formatter = NumberFormat.getInstance();
	  ParsePosition pos = new ParsePosition(0);
	  formatter.parse(str, pos);
	  return str.length() == pos.getIndex();
	}
	/**
	 * Convert 2 log2
	 * @param x
	 * @return
	 */
	public static double log2(double x) {
	     return Math.log(x) / Math.log(2.0d);
	}
    public static ArrayList<Double> Bonferroni_correction(ArrayList<Double> pval)
    {

            ArrayList<Double> toret = new ArrayList<Double>();
            for(double f: pval)
            {
                    double novel = f * pval.size();
                    if (novel > 1.0) novel = 1.0;
                    toret.add(novel);
            }
            return toret;
    }
	public static double fisherTest(int a, int b, int c, int d) {
		int m = a + b + c + d + 1;
		double[] logFactorial = new double[m+1];
	    
		int FISHER_1TAILED = 2;
	    int FISHER_2TAILED = 4;
        
	    logFactorial[0] = 0.0;
        for (int i = 1; i <= m; i++) {
            logFactorial[i] = logFactorial[i-1] + Math.log(i);
        }
        double pval = StatTests.fisher(a, b, c, d, FISHER_2TAILED, logFactorial);
        return pval;
	}

	
	public static double[] convertListStr2Double(LinkedList list) {
		double[] num = new double[list.size()];
		int i = 0;
		Iterator itr = list.iterator();
		while (itr.hasNext()) {
			String str = (String)itr.next();
			double n = new Double(str);
			num[i] = n;
			i++;
		}
		return num;
	}
	
	public static double median(double[] m) {
		double[] temp = m;
		Arrays.sort(temp);
	    int middle = temp.length/2;
	    if (temp.length%2 == 1) {
	        return temp[middle];
	    } else {
	        return (temp[middle-1] + temp[middle]) / 2.0;
	    }
	}
	public static double mean(double[] m) {
	    double sum = 0;
	    for (int i = 0; i < m.length; i++) {
	        sum += m[i];
	    }
	    return sum / m.length;
	}
	public static double max(double[] m) {
	    double max = m[0];
	    for (int i = 0; i < m.length; i++) {
	        if (m[i] > max) {
	        	max = m[i];
	        }
	    }
	    return max;
	}
	public static double PopulationVariance(double[] set) {
		DescriptiveStatistics stats = new DescriptiveStatistics();
		for (double val: set) {
			stats.addValue(val);
		}
		return stats.getPopulationVariance();
	}
	
	
    public static Double mad(List<Double> inputList) {
        Double[] input = inputList.toArray(new Double[inputList.size()]);
        Double median = median(input);
        arrayAbsDistance(input, median);
        return median(input);
    }

    public static void arrayAbsDistance(Double[] array, Double value) {
        for (int i=0; i<array.length;i++) {
            array[i] = Math.abs(array[i] - value);
        }
    }

    public static Double median(Double[] input) {
        if (input.length==0) {
            throw new IllegalArgumentException("to calculate median we need at least 1 element");
        }
        Arrays.sort(input);
        if (input.length%2==0) {
            return (input[input.length/2-1] + input[input.length/2])/2;
        } 
        return input[input.length/2];
    }
	public static double standardDeviation(double[] set) {
		DescriptiveStatistics stats = new DescriptiveStatistics();
		for (double val: set) {
			stats.addValue(val);
		}
		return stats.getStandardDeviation();
	}
	public static double standardError(double[] set) {
		DescriptiveStatistics stats = new DescriptiveStatistics();
		for (double val: set) {
			stats.addValue(val);
		}
		double SD = stats.getStandardDeviation();
		double sqrtN = Math.sqrt(new Double(set.length));
		return SD / sqrtN;				
	}
	public static double SpearmanRank(double[] set1, double[] set2) {
		SpearmansCorrelation sc = new SpearmansCorrelation();		
		
		return sc.correlation(set1, set2);
	}
	public static double SpearmanRankPvalue(double[] set1, double[] set2) {
		SpearmansCorrelation sc = new SpearmansCorrelation();	
		double r = sc.correlation(set1, set2);
		double df = (set1.length + set2.length) / 2 - 2;
		double tvalue = -1;
		if (r == 1) {
			tvalue = Integer.MAX_VALUE;
		} else {
			tvalue = r * Math.sqrt(df / (1 - r * r));
		}
		tvalue = r * Math.sqrt(df / (1 - r * r));
		TTest ttest = new TTest();
		TDistribution tdistr = new TDistribution(df);		
		//return df;
		return (1 - tdistr.cumulativeProbability(tvalue)) * 2;
	}
	public static double PearsonCorrel(double[] set1, double[] set2) {
		PearsonsCorrelation pc = new PearsonsCorrelation();
		return pc.correlation(set1,  set2);
	}
	public static double PearsonCorrelPvalue(double[] set1, double[] set2) {
		PearsonsCorrelation pc = new PearsonsCorrelation();
		double r = pc.correlation(set1,  set2);
		double df = (set1.length + set2.length) / 2 - 2;
		double tvalue = -1;
		if (r == 1) {
			tvalue = Integer.MAX_VALUE;
		} else {
			tvalue = r * Math.sqrt(df / (1 - r * r));
		}
		tvalue = r * Math.sqrt(df / (1 - r * r));
		TTest ttest = new TTest();
		TDistribution tdistr = new TDistribution(df);		
		//return df;
		return (1 - tdistr.cumulativeProbability(tvalue)) * 2;
		//return Math.sqrt(df / (1 - r * r));
	}
	public static double pval2zscore(double pval) {
		return StatisticsConversion.inverseCumulativeProbability(pval / 2) * -1;
	}
	public static double stouffer_meta_analysis(LinkedList Zis, LinkedList weights) {
		
		double score = 0;
		Iterator itr = Zis.iterator();
		Iterator itr_weight = weights.iterator();
		while (itr.hasNext()) {
			double Zi = (Double)itr.next();
			double weight = (Double)itr_weight.next();
			score += weight * Zi;
		}		
		double den = 0;
		
		itr = Zis.iterator();
		itr_weight = weights.iterator();
		while (itr.hasNext()) {
			double Zi = (Double)itr.next();
			double weight = (Double)itr_weight.next();
			den += weight * weight;
		}
		return score / Math.sqrt(den);
	}
}
