package PhosphoTools.MotifTools.Stats;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import misc.FileManipulation;
import misc.FileTools;

public class PhosphoKinaseCorrelationDistributionAll {

	public static void execute(String[] args) {
		
		try {
			String fileName = args[0];
			String kinaseFile = args[1];
			int index1 = new Integer(args[2]);
			int index2 = new Integer(args[3]);
			String outputFileFolder = args[4];
			
			HashMap kinase_list = new HashMap();
			FileInputStream fstreamKinase = new FileInputStream(kinaseFile);
			DataInputStream dinKinase = new DataInputStream(fstreamKinase);
			BufferedReader inKinase = new BufferedReader(new InputStreamReader(dinKinase));
			while (inKinase.ready()) {
				String strKinase = inKinase.readLine();
				if (!strKinase.equals("")) {
					String kinase = strKinase;
					kinase_list.put(kinase, kinase);
				}
			}
			inKinase.close();
			
			
			LinkedList list = FileTools.readFileList(fileName);
			/*FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {*/
			int totalLine = kinase_list.size();
			int lineNum = 0;
			Iterator itr_kinase = kinase_list.keySet().iterator();
			while (itr_kinase.hasNext()) {
				String kinase = (String)itr_kinase.next();
				
				lineNum++;
				if (lineNum % (totalLine / 10 + 1) == 0) {
					System.out.println(new Double(new Double(lineNum) / totalLine * 100).intValue() + "% Finished");
				}
				HashMap map = new HashMap();
				Iterator itr_list = list.iterator();
				while (itr_list.hasNext()) {
					//String str = in.readLine();
					String str = (String)itr_list.next();
					String[] split = str.split("\t");
					
					String[] sub_name_split = split[0].split("\\|");
					String sub_name = sub_name_split[sub_name_split.length - 1];
					String peptide = split[index1];
					//String peptide = split[11];
					//String kinase_name = split[16];
					String kinase_peptide = split[index2];
					String kinase_name = kinase_peptide.split("_")[0];
										
					String pearson = split[9];
					String spearman = split[10];
					
	
					if (kinase_name.toUpperCase().equals(kinase.toUpperCase())) {						
						map.put(kinase_peptide + "_" + sub_name + "_" + peptide, pearson + "\t" + spearman);
					} else if (kinase.toUpperCase().equals("ALL")) {
						map.put(kinase_peptide + "_" + sub_name + "_" + peptide, pearson + "\t" + spearman);
					}
				
				}
				//in.close();
				
				FileWriter fwriter = new FileWriter(outputFileFolder + "/" + kinase + ".txt");
				BufferedWriter out = new BufferedWriter(fwriter);
				out.write("Name\tPearsonCor\tSpearmanCor\n");
				Iterator itr = map.keySet().iterator();
				while (itr.hasNext()) {
					String key = (String)itr.next();
					String cor = (String)map.get(key);
					out.write(key + "\t" + cor + "\n");
				}
				out.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
