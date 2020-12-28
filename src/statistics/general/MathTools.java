package statistics.general;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.inference.TTest;
import org.apache.commons.math3.stat.inference.WilcoxonSignedRankTest;
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
		LinkedList test = new LinkedList();
		test.add(99999.0);
		test.add(0.0);
		test.add(0.0);
		test.add(0.0);
		test.add(0.0);
		test.add(0.0);
		
		System.out.println(shannon_entropy(test));
		System.exit(0);
		double[] vals = {-1, -2, -3, 1, 2};
		//System.out.println(standardError(vals));
		//System.out.println(log2(4));
		double[] val1 = {1, 2, 3, 4};
		double[] val2 = {5, 4, 3, 3};
		//double[] val2 = {3, 3, 4, 5};
		//System.out.println(2 - PearsonCorrelPvalue(val1, val2));
		
		//double[] val3 = {0.619229099, -0.796565867, 1.135480384, 0.420745752, -2.290236978, -1.791960526, -2.934336201, -2.823691462};
		//double[] val3_zscore = zscores(val3);
		/*for (double score: val3_zscore) {
			System.out.println(score);
		}*/
		//System.out.println(median(vals));
		//System.out.println();
		
		List<Double> list = new ArrayList<Double>();
		/*list.add(4.0);
		list.add(5.0);
		list.add(3.0);
		list.add(3.0);
		list.add(4.0);
		list.add(4.0);*/
		
		
		list.add(8.0);
		list.add(8.0);
		list.add(8.0);
		list.add(6.0);
		list.add(3.0);
		list.add(3.0);
		list.add(5.0);
		list.add(3.0);
		list.add(33.0);
		list.add(6.0);
		list.add(6.0);
		list.add(9.0);
		list.add(3.0);
		list.add(3.0);
		list.add(3.0);
		list.add(6.0);
		List<Double> out = getOutliers(list);
		System.out.println(out.size());
		Iterator itr = out.iterator();
		while (itr.hasNext()) {
			double value = (Double)itr.next();
			System.out.println(value);
		}
		/*itr = list.iterator();
		while (itr.hasNext()) {
			double value = (Double)itr.next();
			System.out.println(value);
		}*/
		
		System.out.println(embedded(1, 5, 3, 4));
		System.out.println(embedded(5, 1, 3, 4));
		
	}

	public static double thousandth_dec(double val) {
		return new Double(new Double(val * 1000).intValue()) / 1000;
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

	/**
	 * Hn(p)=-SUM{(pi * log(pi))/log(n)}. n is the number of samples
	 * 
	 * @param values
	 * @return
	 */
	public static double shannon_entropy(LinkedList values) {
		
		if (values.size() == 0) {
			return Double.NaN;
		}
		double sum = sum(convertListDouble2Double(values));
		int count_greater_0 = 0;
		Iterator itr = values.iterator();
		while (itr.hasNext()) {
			double value = (Double)itr.next();
			
			if (value >= 0) {
				count_greater_0++;
			}
		}
		double cumulative = 0;
		itr = values.iterator();
		while (itr.hasNext()) {
			double value = (Double)itr.next();
			if (value > 0) {
				double p = value / sum;
				cumulative += - p * Math.log(p) / Math.log(count_greater_0);
			}
		}
		return cumulative;
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
	
	public static double[] convertListDouble2Double(LinkedList list) {
		double[] num = new double[list.size()];
		int i = 0;
		Iterator itr = list.iterator();
		while (itr.hasNext()) {
			double val = (Double)itr.next();
			double n = val;
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
	public static double average(double[] m) {
	    double sum = 0;
	    for (int i = 0; i < m.length; i++) {
	        sum += m[i];
	    }
	    return sum / m.length;
	}
	public static double sum(double[] m) {
	    double sum = 0;
	    for (int i = 0; i < m.length; i++) {
	        sum += m[i];
	    }
	    return sum;
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
	public static double min(double[] m) {
	    double min = m[0];
	    for (int i = 0; i < m.length; i++) {
	        if (m[i] < min) {
	        	min = m[i];
	        }
	    }
	    return min;
	}
	public static double PopulationVariance(double[] set) {
		DescriptiveStatistics stats = new DescriptiveStatistics();
		for (double val: set) {
			stats.addValue(val);
		}
		return stats.getPopulationVariance();
		
	}
	public static double SampleVariance(double[] set) {
		DescriptiveStatistics stats = new DescriptiveStatistics();
		for (double val: set) {
			stats.addValue(val);
		}
		return stats.getVariance();		
	}
	public static double getGeometricMean(double[] set) {
		DescriptiveStatistics stats = new DescriptiveStatistics();
		for (double val: set) {
			stats.addValue(val);
		}
		return stats.getGeometricMean();		
	}
	/**
	 * Check if b1-b2 is embedded inside a1-a2
	 * @param a1
	 * @param a2
	 * @param b1
	 * @param b2
	 * @return
	 */
	public static boolean embedded(int a1, int a2, int b1, int b2) {
		if (a1 <= b1 && b1 <= a2 && a1 <= b2 && b2 <= a2) {
			return true;
		}
		if (a2 <= b1 && b1 <= a1 && a2 <= b2 && b2 <= a1) {
			return true;
		}
		return false;
	}
	/**
	 * Check if a1-a2 intersect with b1-b2
	 * @param a1
	 * @param a2
	 * @param b1
	 * @param b2
	 * @return
	 */
	public static boolean overlap(int a1, int a2, int b1, int b2) {
		if (a1 <= b1 && b1 <= a2) {
			return true;
		}
		if (a1 <= b2 && b2 <= a2) {
			return true;
		}
		if (a2 <= b1 && b1 <= a1) {
			return true;
		}
		if (a2 <= b2 && b2 <= a1) {
			return true;
		}
		
		if (b1 <= a1 && a1 <= b2) {
			return true;
		}
		if (b1 <= a2 && a2 <= b2) {
			return true;
		}
		if (b2 <= a1 && a1 <= b1) {
			return true;
		}
		if (b2 <= a2 && a2 <= b1) {
			return true;
		}
		return false;
	}
	
	/**
	 * Check if a1-a2 intersect with b1-b2
	 * @param a1
	 * @param a2
	 * @param b1
	 * @param b2
	 * @return
	 */
	public static boolean intersect(int a1, int a2, int b1, int b2) {
		if (a1 <= b1 && b1 <= a2) {
			return true;
		}
		if (a1 <= b2 && b2 <= a2) {
			return true;
		}
		if (a2 <= b1 && b1 <= a1) {
			return true;
		}
		if (a2 <= b2 && b2 <= a1) {
			return true;
		}
		
		if (b1 <= a1 && a1 <= b2) {
			return true;
		}
		if (b1 <= a2 && a2 <= b2) {
			return true;
		}
		if (b2 <= a1 && a1 <= b1) {
			return true;
		}
		if (b2 <= a2 && a2 <= b1) {
			return true;
		}
		return false;
	}
	
    public static Double mad(List<Double> inputList) {
        Double[] input = inputList.toArray(new Double[inputList.size()]);
        Double median = median(input);
        arrayAbsDistance(input, median);
        return median(input);
    }
    public static Double mad(double[] input) {        
        Double median = median(input);        
        arrayAbsDistance(input, median);
        return median(input);
    }

    public static void arrayAbsDistance(Double[] array, Double value) {
        for (int i=0; i<array.length;i++) {
            array[i] = Math.abs(array[i] - value);
        }
    }
    public static void arrayAbsDistance(double[] array, double value) {
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
    public static Double median(LinkedList list) {
    	double[] input = MathTools.convertListDouble2Double(list);
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
	public static double MannWhitneyUTest(double[] set1, double[] set2) {
		//WilcoxonSignedRankTest wilcoxTest = new WilcoxonSignedRankTest();
		MannWhitneyUTest mannWhitney = new MannWhitneyUTest();
		double pval = mannWhitney.mannWhitneyUTest(set1, set2);
		return pval;
	}
	public static double WilcoxRankSumTest(double[] set1, double[] set2) {
		//WilcoxonSignedRankTest wilcoxTest = new WilcoxonSignedRankTest();
		MannWhitneyUTest mannWhitney = new MannWhitneyUTest();
		double pval = mannWhitney.mannWhitneyUTest(set1, set2);
		return pval;
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

    public static List<Double> getOutliers(List<Double> input) {
    	List<Double> copy_input = new ArrayList<Double>();
    	Iterator itr = input.iterator();
    	while (itr.hasNext()) {
    		double val = (Double)itr.next();
    		copy_input.add(val);
    	}
    	Collections.sort(copy_input);
        List<Double> output = new ArrayList<Double>();
        List<Double> data1 = new ArrayList<Double>();
        List<Double> data2 = new ArrayList<Double>();
        if (copy_input.size() % 2 == 0) {
            data1 = copy_input.subList(0, copy_input.size() / 2);
            data2 = copy_input.subList(copy_input.size() / 2, copy_input.size());
        } else {
            data1 = copy_input.subList(0, copy_input.size() / 2);
            data2 = copy_input.subList(copy_input.size() / 2 + 1, copy_input.size());
        }
        double q1 = getMedian(data1);
        double q3 = getMedian(data2);
        double iqr = q3 - q1;
        double lowerFence = q1 - 1.5 * iqr;
        double upperFence = q3 + 1.5 * iqr;
        //System.out.println("LowerFence: " + lowerFence);
        //System.out.println("UpperFence: " + upperFence);
        for (int i = 0; i < input.size(); i++) {
            if (copy_input.get(i) < lowerFence || copy_input.get(i) > upperFence)
                output.add(copy_input.get(i));
        }
        return output;
    }

    /**
     * This doesn't get the median unless the data is sorted.
     * @param data
     * @return
     */
    private static double getMedian(List<Double> data) {
        if (data.size() % 2 == 0)
            return (data.get(data.size() / 2) + data.get(data.size() / 2 - 1)) / 2;
        else
            return data.get(data.size() / 2);
    }
    /**
     * Originally from http://www.java2s.com/Code/Java/Collections-Data-Structure/Retrivethequartilevaluefromanarray.htm
     * Retrive the quartile value from an array
     * .
     * @param values THe array of data
     * @param lowerPercent The percent cut off. For the lower quartile use 25,
     *      for the upper-quartile use 75
     * @return
     */
    public static double quartile(double[] values, double lowerPercent) {

        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("The data array either is null or does not contain any data.");
        }

        if (lowerPercent > 99) {
        	lowerPercent = 99;
        }
        // Rank order the values
        double[] v = new double[values.length];
        System.arraycopy(values, 0, v, 0, values.length);
        Arrays.sort(v);

        int n = (int) Math.round(v.length * lowerPercent / 100);
        
        return v[n];

    }
	/** * @(#)qnorm.js * * Copyright (c) 2000 by Sundar Dorai-Raj
	  * * @author Sundar Dorai-Raj
	  * * Email: sdoraira@vt.edu
	  * * This program is free software; you can redistribute it and/or
	  * * modify it under the terms of the GNU General Public License 
	  * * as published by the Free Software Foundation; either version 2 
	  * * of the License, or (at your option) any later version, 
	  * * provided that any use properly credits the author. 
	  * * This program is distributed in the hope that it will be useful,
	  * * but WITHOUT ANY WARRANTY; without even the implied warranty of
	  * * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
	  * * GNU General Public License for more details at http://www.gnu.org * * */

	public static double qnorm(double p) {
		
		// ALGORITHM AS 111, APPL.STATIST., VOL.26, 118-121, 1977.
		    // Computes z=invNorm(p)
		   double r = Double.NaN;
		   double ppnd = Double.NaN;
		   double split=0.42;
		   double  a0=  2.50662823884;
		   double  a1=-18.61500062529;
		   double  a2= 41.39119773534;
		   double  a3=-25.44106049637;
		   double  b1= -8.47351093090;
		   double  b2= 23.08336743743;
		   double  b3=-21.06224101826;
		   double  b4=  3.13082909833;
		   double  c0= -2.78718931138;
		   double  c1= -2.29796479134;
		   double  c2=  4.85014127135;
		   double  c3=  2.32121276858;
		   double  d1=  3.54388924762;
		   double  d2=  1.63706781897;
		   double  q=p-0.5;
		   if (Math.abs(q)<=split) {
		      r=q*q;
		      ppnd=q*(((a3*r+a2)*r+a1)*r+a0)/((((b4*r+b3)*r+b2)*r+b1)*r+1);
		   } else {
		      r=p;
		      if(q>0) r=1-p;
		      if(r>0) {
		        r=Math.sqrt(-Math.log(r));
		        ppnd=(((c3*r+c2)*r+c1)*r+c0)/((d2*r+d1)*r+1);
		        if(q<0) ppnd=-ppnd;
		      }
		      else {
		        ppnd=0;
		      }
		    }
		    return(ppnd);
	}	
}
