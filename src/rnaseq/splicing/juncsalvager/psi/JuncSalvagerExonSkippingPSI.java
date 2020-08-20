package rnaseq.splicing.juncsalvager.psi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Takes in the STAR junction file and calculate the PSI
 * @author tshaw
 *
 */
public class JuncSalvagerExonSkippingPSI {

	public static String type() {
		return "JUNCSALVAGER";
	}
	public static String description() {
		return "Calculate the psi value for each exon in the gtf file using STAR SJ file.\n";
	}
	public static String parameter_info() {
		return "[inputSTARSJ] [gtfFile] [outputFile_SpliceOut] [outputFile_SpliceIn]";
	}
	public static void main(String[] args) {
		int start = 53019384;
		int index = start / 1000000;
		System.out.println(index * 1000000);
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputSTARSJ = args[0];
			String GTFFile = args[1];
			int buffer = new Integer(args[2]);
			String outputFile = args[3];
			String outputFile2 = args[4];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("chr\tstart\tend\tpso\tleft_read\tright_read\tskip_read\n");
			
			FileWriter fwriter2 = new FileWriter(outputFile2);
			BufferedWriter out2 = new BufferedWriter(fwriter2);
			out2.write("chr\tstart\tend\tpsi\tleft_read\tright_read\tskip_read\n");
			
			HashMap exon = new HashMap();
			HashMap exon_left = new HashMap();
			HashMap exon_right = new HashMap();
			HashMap exon_skip = new HashMap();
			
			FileInputStream fstream = new FileInputStream(GTFFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!str.substring(0, 1).equals("#")) {
					if (split[2].equals("exon")) {
						String chr = split[0];
						int start = new Integer(split[3]);
						int end = new Integer(split[4]);
						
							if (exon.containsKey(chr)) {
								HashMap coord = (HashMap)exon.get(chr);
								int index = start / 1000000;
								index = index * 1000000;
								
								if (coord.containsKey(index)) {
									LinkedList list = (LinkedList)coord.get(index);
									list.add(start + "\t" + end);
									coord.put(index, list);
									exon.put(chr, coord);
								} else {
									LinkedList list = new LinkedList();
									list.add(start + "\t" + end);
									coord.put(index, list);
									exon.put(chr, coord);								
								}
	
							} else {
								HashMap coord = new HashMap();
								int index = start / 1000000;
								index = index * 1000000;
								
								LinkedList list = new LinkedList();
								list.add(start + "\t" + end);
								coord.put(index, list);
								exon.put(chr, coord);
							}
						
						exon_left.put(chr + "\t" + start, 0);
						exon_right.put(chr + "\t" + end, 0);
						exon_skip.put(chr + "\t" + start + "\t" + end, 0);
					}			
				}
			}
			in.close();
			
			fstream = new FileInputStream(inputSTARSJ);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");

				String chr = split[0];
				int intron_left = new Integer(split[1]) - 1;
				int intron_right = new Integer(split[2]) + 1;
				int uniq_read = new Integer(split[6]);
				for (int i = 0; i < buffer; i++) {
					if (exon_left.containsKey(chr + "\t" + (intron_right + i))) {
						int prev_read = (Integer)exon_left.get(chr + "\t" + (intron_right + i));
						prev_read += uniq_read;
						exon_left.put(chr + "\t" + (intron_right + i), prev_read);
					}
				}
				for (int i = 0; i < buffer; i++) {
					if (exon_right.containsKey(chr + "\t" + (intron_left - i))) {
						int prev_read = (Integer)exon_right.get(chr + "\t" + (intron_left - i));
						prev_read += uniq_read;
						exon_right.put(chr + "\t" + (intron_left - i), prev_read);
					}
				}
				int index0 = intron_left / 1000000;
				index0 = index0 * 1000000;
				int index1 = (index0 - 1) * 1000000;
				int index2 = (index0 - 1) * 1000000;
				if (exon.containsKey(chr)) {
					HashMap coord = (HashMap)exon.get(chr);
					if (coord.containsKey(index0)) {
						LinkedList list = (LinkedList)coord.get(index0);
						Iterator itr = list.iterator();
						while (itr.hasNext()) {
							String start_end = (String)itr.next();
							int exon_start = new Integer(start_end.split("\t")[0]);
							int exon_end = new Integer(start_end.split("\t")[1]);
							if (intron_left < exon_start && exon_end < intron_right) {
								int count = (Integer)exon_skip.get(chr + "\t" + exon_start + "\t" + exon_end);
								count += uniq_read;
								exon_skip.put(chr + "\t" + exon_start + "\t" + exon_end, count);								
							}
						}
					}
					if (coord.containsKey(index1)) {
						LinkedList list = (LinkedList)coord.get(index1);
						Iterator itr = list.iterator();
						while (itr.hasNext()) {
							String start_end = (String)itr.next();
							int exon_start = new Integer(start_end.split("\t")[0]);
							int exon_end = new Integer(start_end.split("\t")[1]);
							if (intron_left < (exon_start - buffer) && (exon_end + buffer) < intron_right) {
								int count = (Integer)exon_skip.get(chr + "\t" + exon_start + "\t" + exon_end);
								count += uniq_read;
								exon_skip.put(chr + "\t" + exon_start + "\t" + exon_end, count);								
							}
						}
					}
					if (coord.containsKey(index2)) {
						LinkedList list = (LinkedList)coord.get(index2);
						Iterator itr = list.iterator();
						while (itr.hasNext()) {
							String start_end = (String)itr.next();
							int exon_start = new Integer(start_end.split("\t")[0]);
							int exon_end = new Integer(start_end.split("\t")[1]);
							if (intron_left < (exon_start - buffer) && (exon_end + buffer) < intron_right) {
								int count = (Integer)exon_skip.get(chr + "\t" + exon_start + "\t" + exon_end);
								count += uniq_read;
								exon_skip.put(chr + "\t" + exon_start + "\t" + exon_end, count);								
							}
						}
					}
				}
			}
			in.close();
			
			Iterator itr = exon_skip.keySet().iterator();
			while (itr.hasNext()) {
				String coord = (String)itr.next();
				double skip = (Integer)exon_skip.get(coord);
				String[] split_coord = coord.split("\t");
				String chr = split_coord[0];
				String start = split_coord[1];
				String end = split_coord[2];
				double left_count = (Integer)exon_left.get(chr + "\t" + start);
				double right_count = (Integer)exon_right.get(chr + "\t" + end);
				double pso = skip / (((left_count + right_count) / 2) + skip);
				
				double exon_usage = 0.0;
				if (left_count > right_count) {
					exon_usage = right_count;
				} else {
					exon_usage = left_count;
				}
				/*if (left_count > 5 && right_count > 5) {					
					exon_usage = (left_count + right_count) / 2;
				} else {
					if (Math.abs(left_count - right_count) < 5) {
						exon_usage = (left_count + right_count) / 2;
					} else {
						if (left_count > right_count) {
							exon_usage = right_count;
						} else {
							exon_usage = left_count;
						}
					}
				}*/
				double psi = (exon_usage) / (((left_count + right_count) / 2) + skip);
				out.write(chr + "\t" + start + "\t" + end + "\t" + pso + "\t" + left_count + "\t" + right_count + "\t" + skip + "\n");
				out2.write(chr + "\t" + start + "\t" + end + "\t" + psi + "\t" + left_count + "\t" + right_count + "\t" + skip + "\n");
			}
			out.close();
			out2.close();
			
		} catch (Exception e ){
			e.printStackTrace();
		}
	}
}
