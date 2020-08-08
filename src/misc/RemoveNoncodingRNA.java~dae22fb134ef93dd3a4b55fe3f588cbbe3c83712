package misc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class RemoveNoncodingRNA {

	public static void main(String[] args) {
		
		try {
			
			//HashMap noncoding = noncoding_genelist("C:\\Users\\tshaw\\Desktop\\RNASEQ\\Mus_musculus.noncoding.txt");
			String fileName = args[0]; //"C:\\Users\\tshaw\\Desktop\\RNASEQ\\Mckinnon\\FPKM_07282014\\SJMMHGG\\SJMMHGG_RNAseq_Exon_Read_Count_gene_fpkm_uniq.txt";
			HashMap noncoding = noncoding_genelist(args[1]);
			
			HashMap SJMMHGG = new HashMap();
			
			String outputFile = args[2]; //"C:\\Users\\tshaw\\Desktop\\RNASEQ\\Mckinnon\\FPKM_07282014\\SJMMHGG\\SJMMHGG_RNAseq_Exon_Read_Count_gene_fpkm_uniq_NRremove.txt";;
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String SJMMHGG_Tag = in.readLine();
			out.write(SJMMHGG_Tag + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0].split(",")[0].split(";")[0];
				if (!noncoding.containsKey(geneName)) {
					out.write(str + "\n");
				}
				
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String description() {
		return "Remove genes annotated as noncoding RNAs";
	}
	public static String type() {
		return "MISC";
	}
	public static String parameter_info() {
		return "[inputMatrixFile] [noncoding gene Ref File] [outputFileredMatrixFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String noncodingFile = args[1];
			HashMap noncoding = noncoding_genelist(noncodingFile);
			
			HashMap SJMMHGG = new HashMap();
			
			String outputFile = args[2]; //"C:\\Users\\tshaw\\Desktop\\RNASEQ\\Mckinnon\\FPKM_07282014\\SJMMHGG\\SJMMHGG_RNAseq_Exon_Read_Count_gene_fpkm_uniq_NRremove.txt";;
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String fileName = args[0]; //"C:\\Users\\tshaw\\Desktop\\RNASEQ\\Mckinnon\\FPKM_07282014\\SJMMHGG\\SJMMHGG_RNAseq_Exon_Read_Count_gene_fpkm_uniq.txt";
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String SJMMHGG_Tag = in.readLine();
			out.write(SJMMHGG_Tag + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0].split(",")[0].split(";")[0];
				if (!noncoding.containsKey(geneName)) {
					out.write(str + "\n");
				}
				
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	public static void MCKINNON_EXAMPLE(String[] args) {
		
			
		try {
			
			HashMap noncoding = noncoding_genelist("C:\\Users\\tshaw\\Desktop\\RNASEQ\\Mus_musculus.noncoding.txt");
			
			HashMap SJMMHGG = new HashMap();
			
			String outputFile = "C:\\Users\\tshaw\\Desktop\\RNASEQ\\Mckinnon\\FPKM_07282014\\SJMMHGG\\SJMMHGG_RNAseq_Exon_Read_Count_gene_fpkm_uniq_NRremove.txt";;
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String fileName = "C:\\Users\\tshaw\\Desktop\\RNASEQ\\Mckinnon\\FPKM_07282014\\SJMMHGG\\SJMMHGG_RNAseq_Exon_Read_Count_gene_fpkm_uniq.txt";
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String SJMMHGG_Tag = in.readLine();
			out.write(SJMMHGG_Tag + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0].split(",")[0].split(";")[0];
				if (!noncoding.containsKey(geneName)) {
					out.write(str + "\n");
				}
				
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	public static HashMap noncoding_genelist(String fileName) {
		HashMap map = new HashMap();
		try {
			
			
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0], split[0]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
