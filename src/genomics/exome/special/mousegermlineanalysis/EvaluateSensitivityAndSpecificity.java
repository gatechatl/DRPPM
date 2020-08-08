package genomics.exome.special.mousegermlineanalysis;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class EvaluateSensitivityAndSpecificity {

	public static void main(String[] args) {
		
		try {
			LinkedList samples = new LinkedList();
			samples.add("SJMMHGG010850");
			samples.add("SJMMHGG010853");
			samples.add("SJMMHGG010466");
			samples.add("SJMMHGG010852");
			samples.add("SJMMHGG010854");
			samples.add("SJMMHGG010851");
			samples.add("SJMMHGG010469");
			samples.add("SJMMHGG010465");			
			
			HashMap map = new HashMap();
			String predFile = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\Reorganize\\SNV_History\\PsuedoPair\\New_PseudoNormal_tier1_putative_somatic_goodsingleton.txt";
			String refFile = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\Reorganize\\SNV_History\\PsuedoPair\\2013_SNV_SJHQ.txt";
			FileInputStream fstream = new FileInputStream(refFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();				
				String[] split = str.split("\t");
				if (split[1].equals("SJHQ")) {
					String sample = split[2];
					if (samples.contains(sample)) {
						String chr = split[3];
						String loc = split[4];
						String mutclass = split[5];
						String aachange = split[6];
						String key = sample + chr + loc + mutclass + aachange;
						map.put(key, str);
					}
				}				
			}
			in.close();
			
			HashMap truePos = new HashMap();
			HashMap falsePos = new HashMap();
			HashMap falseNeg = new HashMap();
			HashMap pred = new HashMap();
			fstream = new FileInputStream(predFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();				
				String[] split = str.split("\t");
				if (split[1].equals("SJHQ")) {
					String sample = split[2];
					if (samples.contains(sample)) {
						String chr = split[3];
						String loc = split[4];
						String mutclass = split[5];
						String aachange = split[6];
						String key = sample + chr + loc + mutclass + aachange;
						pred.put(key, str);
						if (map.containsKey(key)) {
							if (truePos.containsKey(sample)) {
								double val = (Double)truePos.get(sample);
								val++;
								truePos.put(sample, val);
							} else {
								truePos.put(sample, 1.0);
							}
						} else {
							if (falsePos.containsKey(sample)) {
								double val = (Double)falsePos.get(sample);
								val++;
								falsePos.put(sample, val);
							} else {
								falsePos.put(sample, 1.0);
							}
						}
						
						//map.put(sample + chr + loc + mutclass + aachange, "");
					}
				}				
			}
			in.close();
			
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();				
				if (!(pred.containsKey(key))) {
					String str = (String)map.get(key);
					String[] split = str.split("\t");
					if (split[1].equals("SJHQ")) {
						String sample = split[2];					
						String chr = split[3];
						String loc = split[4];
						String mutclass = split[5];
						String aachange = split[6];
						//String key = sample + chr + loc + mutclass + aachange;
						if (falseNeg.containsKey(sample)) {
							double val = (Double)falseNeg.get(sample);
							val++;
							falseNeg.put(sample, val);
						} else {
							falseNeg.put(sample, 1.0);
						}
					}
				}				
			}
			
			itr = samples.iterator();
			while (itr.hasNext()) {
				String sample = (String)itr.next();
				double tp = 0;
				double fp = 0;
				double fn = 0;
				if (truePos.containsKey(sample)) {
					tp = (Double)truePos.get(sample);
				}
				if (falsePos.containsKey(sample)) {
					fp = (Double)falsePos.get(sample);
				}
				if (falseNeg.containsKey(sample)) {
					fn = (Double)falseNeg.get(sample);
				}
				System.out.println(sample + "\t" + tp + "\t" + fp + "\t" + fn);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
