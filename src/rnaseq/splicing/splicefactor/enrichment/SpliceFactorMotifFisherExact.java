package rnaseq.splicing.splicefactor.enrichment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import statistics.general.MathTools;
import functional.pathway.enrichment.MapUtilsBig2Small;
import functional.pathway.enrichment.MapUtilsSmall2Big;

/**
 * Take output from SpliceFactorMotifScanner and compare two groups
 * @author tshaw
 *
 */
public class SpliceFactorMotifFisherExact {

	public static String parameter_info() {
		return "[spliceMotifHitFile1] [spliceMotifHitFile2] [outputFile]";
	}
	
	public static void execute(String[] args) {
		
		try {
			HashMap splicing1 = new HashMap();
			HashMap splicing2 = new HashMap();
			String inputFile = args[0];				
			String inputFile2 = args[1];
			String outputFile = args[2];
			int splice1_total = 0;
			int splice2_total = 0;
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split("\t");
				splicing1.put(split[0], str);
				splice1_total = new Integer(split[2]);
			}
			in.close();
			
			fstream = new FileInputStream(inputFile2);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split("\t");
				if (splicing1.containsKey(split[0])) {
					splicing2.put(split[0], str);
				}
				splice2_total = new Integer(split[2]);
			}
			in.close();
			
			LinkedList list = new LinkedList();
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Name\tPval\tEnrichment\tSkippedHit#\tSkippedNoHit#\tKeptHit#\tKeptNoHit\n");
			Iterator itr = splicing1.keySet().iterator();
			while (itr.hasNext()) {
				String name = (String)itr.next();
				String line1 = (String)splicing1.get(name);
				String[] split1 = line1.split("\t");				

				int a = new Integer(split1[1]);
				int b = new Integer(split1[2]) - a;
				int c = 0;
				int d = splice2_total;
				if (splicing2.containsKey(name)) {
					String line2 = (String)splicing2.get(name);
					String[] split2 = line2.split("\t");
					c = new Integer(split2[1]);
					d = new Integer(split2[2]) - c;
				}
				System.out.println(name + "\t" + split1[1] + "\t" + split1[2] + "\t" + c + "\t" + (c + d));
				double enrichment = (new Double(a) / (b + a)) / (new Double(c) / (d + c));
				double pvalue = MathTools.fisherTest(a, b, c, d);
				if (pvalue > 1) {
					pvalue = 1.0;
				}
				if ((new Double(c) / d) == 0) {
					enrichment = Double.NaN;
				}
				list.add(name + "\t" + pvalue + "\t" + enrichment + "\t" + split1[1] + "\t" + split1[2] + "\t" + c + "\t" + (c + d));
				//out.write(name + "\t" + pvalue + "\t" + enrichment + "\t" + split1[1] + "\t" + split1[2] + "\t" + split2[1] + "\t" + split2[2] + "\n");
			}
			//out.close();
			
			HashMap raw_map = new HashMap();
			HashMap sort_map = new HashMap();
			int num = 1;
			itr = list.iterator();							
			while (itr.hasNext()) {
				String line = (String)itr.next();
				String[] split2 = line.split("\t");								
				double enrichment = new Double(split2[2]);
				raw_map.put(num, line);
				sort_map.put(num, enrichment);					
				num++;						
			}			
			sort_map = (HashMap) MapUtilsBig2Small.sortByValue(sort_map);
			itr = sort_map.keySet().iterator();
			while (itr.hasNext()) {
				num = (Integer)itr.next();
				String line = (String)raw_map.get(num);
				out.write(line + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
