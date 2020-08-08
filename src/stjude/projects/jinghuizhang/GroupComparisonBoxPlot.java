package stjude.projects.jinghuizhang;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Take a matrix and generate boxplots comparing two groups
 * @author tshaw
 *
 */
public class GroupComparisonBoxPlot {

	public static String description() {
		return "Generate a folder that generates the boxplots";
	}
	public static String type() {
		return "JinghuiZhang";
	}
	public static String parameter_info() {
		return "[inputFileMatrix] [groupFile] [unit: Activity/FPKM] [outputFolder]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputFileMatrix = args[0];
			String groupFile = args[1];
			String unit = args[2];
			String outputFolder = args[3];
			
			HashMap group_info = new HashMap();
			FileInputStream fstream = new FileInputStream(groupFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split("\t");
				group_info.put(split[0], split[1]);				
			}
			in.close();
			

			FileWriter fwriter_sh = new FileWriter(outputFolder + "/runAll.sh");
			BufferedWriter out_sh = new BufferedWriter(fwriter_sh);
			
			fstream = new FileInputStream(inputFileMatrix);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] sample_headers = header.split("\t");			
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split("\t");
				String gene = split[0].replaceAll("_", ".");				
				FileWriter fwriter = new FileWriter(outputFolder + "/" + gene + ".txt");
				BufferedWriter out = new BufferedWriter(fwriter);			
				out.write("GN" + "\t" + unit + "\t" + "Type\n");
				for (int i = 1; i < split.length; i++) {
					if (group_info.containsKey(sample_headers[i])) {
						String title = (String)group_info.get(sample_headers[i]);
						out.write(sample_headers[i] + "\t" + split[i] + "\t" + title + "\n");
					}
					
				}
				out.close();
				
				FileWriter fwriter_r = new FileWriter(outputFolder + "/" + gene + ".r");
				BufferedWriter out_r = new BufferedWriter(fwriter_r);			
				out_r.write("library(ggplot2);\n");
				out_r.write("data = read.table(\"" + gene + ".txt\", sep=\"\\t\",header=T);\n");
				//out_r.write("Group2 = data[which(data$Type==\"" + group1_title + "\"),\"" + unit + "\"]\n");
				//out_r.write("Group1 = data[which(data$Type==\"" + group2_title + "\"),\"" + unit + "\"]\n");
				//out_r.write("Group2_vs_Group1 = wilcox.test(Group2, Group1)\n");
				out_r.write("text = paste(\"" + gene + "\");\n");
				out_r.write("p1 = ggplot(data, aes(factor(Type), " + unit + ")) + geom_boxplot() + theme(plot.title=element_text(size=20), axis.text.x = element_text(size=20), axis.text.y = element_text(size=20), axis.title = element_text(size=20), legend.text = element_text(size=20), legend.title = element_text(size=20)) + theme_bw(base_size = 20) + ggtitle(text);\n");
				out_r.write("png(file = \"" + gene + ".txt.png\", width=600,height=750)\n");
				out_r.write("p1\n");
				out_r.write("dev.off();\n");
				out_r.close();
				
				out_sh.write("R --vanilla < " + gene + ".r\n");
			}
			in.close();
			out_sh.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
