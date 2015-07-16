package PhosphoTools.MotifTools.Stats;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * based on the kinase to substrate data
 * generate the scatter plot for each one
 * @author tshaw
 *
 */
public class GenerateKSScatterPlots {

	public static void main(String[] args) {
		double[] var1 = {1, 2, 3, 4};
		double[] var2 = {2, 3, 4, 5};
		System.out.println(add_library());		
		System.out.println(Math.round(new Double("-0.7097102621538689") * 100) / 100);
		//System.out.println(create_scatter_plot("Var1", var1, "Var2", var2, 1));
		
	}
	public static void execute(String[] args) {
		
		try {
			String inputFile = args[0];
			String kinase_str = args[1];
			String outputFile = args[2];
			String legend_flag_str = args[3];
			boolean legend_flag = false;
			if (legend_flag_str.equals("yes")) {
				legend_flag = true;
			}
			
			
			String script = add_library();
			int id = 1;
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String substrate_name = "Sub_" + split[0].split("\\|")[split[0].split("\\|").length - 1] + "_" + split[1].replaceAll("\\*", "");
				//String substrate_name = split[11];
				
				String pearson = new Double(Math.round(new Double(split[9]) * 100)) / 100 + "";
				String spearman = new Double(Math.round(new Double(split[10]) * 100)) / 100 + "";
				String main = "r=" + pearson + ",rho=" + spearman;
				double[] sub = new double[4];
				sub[0] = new Double(split[12]);
				sub[1] = new Double(split[13]);
				sub[2] = new Double(split[14]);
				sub[3] = new Double(split[15]);
				
				String kinase_name = "Kinase_" + split[16].split("\\|")[split[16].split("\\|").length - 1].split("_")[0];
				double[] kin = new double[4];
				kin[0] = new Double(split[17]);
				kin[1] = new Double(split[18]);
				kin[2] = new Double(split[19]);
				kin[3] = new Double(split[20]);		
				
				if (kinase_name.contains(kinase_str)) {
					
					script += create_scatter_plot(substrate_name, sub, kinase_name, kin, id, main, legend_flag);
					
					id++;
				}
			}
			in.close();
			script += print_image(outputFile, id);
			System.out.println(script);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String print_image(String outputFileName, int total) {
		String script = "";
		script += "png(file = \"" + outputFileName + "\", width=800,height=600);\n";

		script += "grid.arrange(";
		for (int i = 1; i < total; i++) {
			script += "p" + i + ",";
		}
		
		int num = 4;
		if (total <= 9) {
			num = 3;
		}
		if (total <= 4) {
			num = 2;
		}
		
		script += "ncol= " + num + ");\n";
		script += "dev.off();\n";
		return script;
	}
	public static String add_library() {
		String script = "library(ggplot2);\n";
		script += "library(gridExtra);\n";
		return script;
	}
	public static String create_scatter_plot(String substrate_name, double[] sub, String kinase_name, double[] kin, int id, String main, boolean legend_flag) {
		
		String script = "";
		
		String substrate = "substrate = c(" + sub[0];
		for (int i = 1; i < sub.length; i++) {
			substrate += "," + sub[i];
		}
		substrate += ");\n";
		
		String kinase = "kinase = c(" + kin[0];
		for (int i = 1; i < kin.length; i++) {
			kinase += "," + kin[i];
		}
		kinase += ");\n";
		
		String type= "type = c('PDGFRA','NTRK1_1','NTRK1_2','CNTRL');\n";
		
		script += substrate + kinase + type;
		
		String legend = "";
		if (legend_flag) {
			legend = "+ theme(legend.position='none')";
		}
		script += "data = cbind(data.frame(substrate), data.frame(kinase), data.frame(type));\n";
		script += "p" + id + " <- ggplot(data, aes(x=substrate, y=kinase, color=type)) + geom_point(shape=19, alpha=3/4) + labs(x='" + substrate_name + "',y='" + kinase_name + "', title='" + main + "') " + legend + ";\n";
		
		return script;
	}
}
