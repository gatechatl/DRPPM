package PhosphoTools.MotifTools.Stats;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Calculate the correlation based on the autophosphorylation peptide
 * @author tshaw
 *
 */
public class KSCorrelationPhosphoPeptide {



	public static void execute(String[] args) {
		
		try {
			String fileName = args[0];
			String kinase = args[1];
			String outputFile = args[2];
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				String[] sub_name_split = split[0].split("\\|");
				String sub_name = sub_name_split[sub_name_split.length - 1];
				String peptide = split[11];
				String kinase_name = split[22];
				
				String pearson = split[9];
				String spearman = split[10];
				if (kinase_name.toUpperCase().equals(kinase.toUpperCase())) {
					
					map.put(kinase_name + "_" + sub_name + "_" + peptide, pearson + "\t" + spearman);
				} else if (kinase.toUpperCase().equals("ALL")) {
					map.put(kinase_name + "_" + sub_name + "_" + peptide, pearson + "\t" + spearman);
				}
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Name\tPearsonCor\tSpearmanCor\n");
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				String cor = (String)map.get(key);
				out.write(key + "\t" + cor + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

