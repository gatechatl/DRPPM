package GSEATools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class GenerateGSEAInputCLSFile {

	public static String parameter_info() {
		return "[inputMETAGroup] [outputCLSFile] [morethan2]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputMetaFile = args[0];
			String outputCLSFile = args[1];
			String flag = args[2];
			
			boolean morethan2 = false;
			if (flag.equals("true")) {
				morethan2 = true;
			}
			generateCLSFile(inputMetaFile, outputCLSFile, morethan2);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void generateCLSFile(String inputFile, String outputFile, boolean flag) {
		
		try {
			HashMap type = new HashMap();
			LinkedList list = new LinkedList();
			
        	FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);
                        
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				list.add(split[1]);
				type.put(split[1], split[1]);
			}
			in.close();
			
			String allType = "";
			HashMap num = new HashMap();
			int count = 0;
			Iterator itr = type.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				allType += " " + key;
				num.put(key, count);
				count++;
			}
			String listType = "";
			itr = list.iterator();
			while (itr.hasNext()) {
				String name = (String)itr.next();
				if (flag) {
					if (listType.equals("")) {
						listType = name;
					} else {
						listType += " " + name;
					}
				} else {
					if (listType.equals("")) {
						listType = num.get(name) + "";
					} else {
						listType += " " + num.get(name);
					}
				}
				
			}
			out.write(list.size() + " " + type.size() + " 1\n");
			out.write("#" + allType + "\n");
			out.write(listType + "\n");
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
