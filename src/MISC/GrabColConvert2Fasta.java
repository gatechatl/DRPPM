package misc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import PhosphoTools.MotifTools.MotifTools;

public class GrabColConvert2Fasta {

	public static void main(String[] args) {
		String peptide = "R.ESVSDLAEEPGEGS#PHQTAR.G";
		if (peptide.split("\\.").length > 1) {
			peptide = peptide.split("\\.")[1];
		}
		System.out.println(peptide);
	}
	public static void execute(String[] args) {
		
		try {
			String phospho_inputFile = args[0];
			int phospho_col_num = new Integer(args[1]);
			int phospho_col_name = new Integer(args[2]);
			String total_inputFile = args[3];			
			int total_col_num = new Integer(args[4]);
			int total_col_name = new Integer(args[5]);
			String outputFile_phospho = args[6];
			String outputFile_total = args[7];
			FileWriter fwriter_phospho = new FileWriter(outputFile_phospho);
			BufferedWriter out_phospho = new BufferedWriter(fwriter_phospho);
						
			FileWriter fwriter_total = new FileWriter(outputFile_total);
			BufferedWriter out_total = new BufferedWriter(fwriter_total);
			
			HashMap phospho = new HashMap();
			HashMap total = new HashMap();			
			
			FileInputStream fstream = new FileInputStream(phospho_inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine().trim();
				if (!str.equals("")) {
					String[] split = str.split("\t");
					if (split.length >= phospho_col_num && split.length >= phospho_col_name) {
						String peptide = MotifTools.replaceTag(split[phospho_col_num]);
						if (peptide.split("\\.").length > 1) {
							peptide = peptide.split("\\.")[1];
						}
						
						if (peptide.contains("#")) {
							phospho.put(peptide, split[phospho_col_name]);
						} else {
							total.put(peptide, split[phospho_col_name]);
						}
					}					
				}				
			}
			in.close();
			
			
			fstream = new FileInputStream(total_inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine().trim();
				if (!str.equals("")) {
					String[] split = str.split("\t");
					if (split.length >= total_col_num && split.length >= total_col_name) {
						String peptide = MotifTools.replaceTag(split[total_col_num]);
						if (peptide.split("\\.").length > 1) {
							peptide = peptide.split("\\.")[1];
						}
						if (peptide.contains("#")) {
							phospho.put(peptide, split[total_col_name]);
						} else {
							total.put(peptide, split[total_col_name]);
						}
					}					
				}				
			}
			in.close();
			int count = 1;
			Iterator itr = total.keySet().iterator();
			while (itr.hasNext()) {
				String str = (String)itr.next();
				String name = (String)total.get(str);
				out_total.write(">" + name + "\n" + str + "\n");
				count++;
			}
			out_total.close();
			
			count = 1;
			itr = phospho.keySet().iterator();
			while (itr.hasNext()) {
				String str = (String)itr.next();
				String name = (String)phospho.get(str);
				out_phospho.write(">" + name + "\n" + str + "\n");
				count++;
			}
			out_phospho.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
