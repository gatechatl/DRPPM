package genomics.exome.special.mousegermlineanalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class AppendGangSNVAnnotation {

	public static void main(String[] args) {
		
		try {
			String recurrent_inputFile = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\GermlineAnalysis\\RemoveGermlineSNV\\GangSuspiciousRecurrent.txt";
			String singleTon_inputFile = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\GermlineAnalysis\\RemoveGermlineSNV\\GangSingletonSNV.txt";
			
			LinkedList singleton_list = new LinkedList();
			HashMap singleTon = new HashMap();
			FileInputStream fstream = new FileInputStream(singleTon_inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			//String[] headers = in.readLine().split("\t");			 
			while (in.ready()) {
				String str = in.readLine();
				singleton_list.add(str);
				singleTon.put(str, str);
				
			}
			in.close();
			
			LinkedList recurrent_list = new LinkedList();
			HashMap recurrent = new HashMap();
			fstream = new FileInputStream(recurrent_inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			//headers = in.readLine().split("\t");			 
			while (in.ready()) {
				String str = in.readLine();
				recurrent_list.add(str);
				recurrent.put(str, str);				
			}
			in.close();
			

			String outputFile = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\GermlineAnalysis\\RemoveGermlineSNV\\New_batch4_somatic_snvs_matrix_all_annotation.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap items = new HashMap();
			String snv_file = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\GermlineAnalysis\\RemoveGermlineSNV\\New_batch4_somatic_snvs_matrix.txt";
			fstream = new FileInputStream(snv_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\tTag\n");
			//headers = in.readLine().split("\t");			 
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String tag = split[6];
				if (singleTon.containsKey(tag)) {
					out.write(str + "\tSingleton\n");
					System.out.println(str + "\t" + "Singleton");
					items.put(tag, str);
				} else if (recurrent.containsKey(tag)) {
					out.write(str + "\tPutativeRecurrent\n");
					System.out.println(str + "\t" + "Putative Recurrent");
					//items.put(tag, str);
				}
				/*if (recurrent.containsKey(tag)) {
					out.write(str + "\tPutativeRecurrent\n");
					System.out.println(str + "\t" + "Putative Recurrent");
					
				}*/
			}
			in.close();
			out.close();
			
			outputFile = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\GermlineAnalysis\\RemoveGermlineSNV\\New_batch4_somatic_snvs_matrix_recurrent_annotation.txt";
			fwriter = new FileWriter(outputFile);
			out = new BufferedWriter(fwriter);
			out.write(header + "\n");
			Iterator itr = recurrent_list.iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				out.write(items.get(key) + "\n");
			}
			out.close();
			
			outputFile = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\GermlineAnalysis\\RemoveGermlineSNV\\New_batch4_somatic_snvs_matrix_singleton_annotation.txt";
			fwriter = new FileWriter(outputFile);
			out = new BufferedWriter(fwriter);
			out.write(header + "\n");
			itr = singleton_list.iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				out.write(items.get(key) + "\n");
			}
			out.close();
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
