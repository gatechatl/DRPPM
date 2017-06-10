package stude.projects.suzannebaker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;

import specialized.algorithm.SpecializedAlgorithms;
import specialized.algorithm.ValueComparator;

/**
 * Create the python version of the single sample GSEA input files
 * @author tshaw
 *
 */
public class CreatePythonGSEAInputFile {

	public static String description() {
		return "Generate cls and python file for GSEA";
	}
	public static String type() {
		return "GSEA";
	}
	public static String parameter_info() {
		return "[inputMatrix] [gmtFile] [sampleInfo] [outputFolder] [outputPythonFolder] [outputScript]";
	}
	public static void execute(String[] args) {
		try {
			
			String inputFile = args[0];
			String gmtFile = args[1];
			String sampleInfilFile = args[2];
			String outputFolder = args[3];
			String outputPythonFolder = args[4];
			String outputScript = args[5];
			FileWriter fwriter_script = new FileWriter(outputScript);
			BufferedWriter out_script = new BufferedWriter(fwriter_script);
			
			HashMap sample2group = new HashMap();
			HashMap sample2metainfo = new HashMap();
			HashMap sample2cls = new HashMap();
			HashMap group = new HashMap();
			HashMap sample2clsName = new HashMap();
			FileInputStream fstream = new FileInputStream(sampleInfilFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				sample2group.put(split[0], split[4]);
				group.put(split[4], split[4]);
				sample2metainfo.put(split[0], split[1]);
				sample2cls.put(split[0], split[5]);
				sample2clsName.put(split[0], split[6]);
			}
			in.close();
			
			Iterator itr = group.keySet().iterator();
			while (itr.hasNext()) {
				String groupName = (String)itr.next();
				String matrixFile = outputFolder + "/" + groupName + "_matrix.txt";
				FileWriter fwriter_matrix_file = new FileWriter(matrixFile);
				BufferedWriter out_matrix_file = new BufferedWriter(fwriter_matrix_file);
				
				String cls = "";
				fstream = new FileInputStream(inputFile);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));
				header = in.readLine();
				String[] split_header = header.split("\t");
				LinkedList index = new LinkedList();
				LinkedList samples = new LinkedList();
				LinkedList sample_clsName = new LinkedList();
				out_matrix_file.write("NAME\tDESCRIPTION");
				for (int i = 1; i < split_header.length; i++) {
					String sample_group_name = (String)sample2group.get(split_header[i]);
					if (sample_group_name.equals(groupName)) {
						index.add(i);
						out_matrix_file.write("\t" + split_header[i]);
						samples.add(split_header[i]);						
						String addClsName = (String)sample2clsName.get(split_header[i]);
						if (!sample_clsName.contains(addClsName)) {
							sample_clsName.add(addClsName);
						}
						//String cls_index = (String)sample2cls.get(split_header[i]);
						if (cls.equals("")) {
							cls = addClsName;
						} else {
							cls += " " + addClsName;
						}
					}
				}
				out_matrix_file.write("\n");
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					out_matrix_file.write(split[0] + "\tna");
					Iterator itr2 = index.iterator();
					while (itr2.hasNext()) {
						int index_int = (Integer)itr2.next();
						out_matrix_file.write("\t" + split[index_int]);
					}
					out_matrix_file.write("\n");
				}
				in.close();
				out_matrix_file.close();
				
				String clsName = "# ";
				Iterator itr3 = sample_clsName.iterator();
				while (itr3.hasNext()) {
					String name = (String)itr3.next();
					clsName += name + " ";
				}
				String outputDir = outputFolder + "/" + groupName;
				String clsFile = outputFolder + "/" + groupName + "_cls.cls";
				FileWriter fwriter_cls_file = new FileWriter(clsFile);
				BufferedWriter out_cls_file = new BufferedWriter(fwriter_cls_file);
				out_cls_file.write(index.size() + " 2 1\n");
				out_cls_file.write(clsName + "\n");
				out_cls_file.write(cls + "\n");
				out_cls_file.close();
				
				String pythonFile = outputPythonFolder + "/" + groupName + ".py";
				FileWriter BufferedWriter = new FileWriter(pythonFile);
				BufferedWriter out_python = new BufferedWriter(BufferedWriter);
				out_python.write("import gseapy\n");
				out_python.write("gseapy.gsea(data='" + matrixFile + "', gene_sets='" + gmtFile + "',outdir='" + outputDir + "',cls='" + clsFile + "',min_size=1,max_size=20000)\n");
				out_python.close();
				out_script.write("python " + pythonFile + "\n");
				
			}
			
			
			out_script.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

}
