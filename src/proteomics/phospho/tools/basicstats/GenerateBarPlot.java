package proteomics.phospho.tools.basicstats;

public class GenerateBarPlot {

	public static void execute(String[] args) {
		
		try {
			String sample_names_str = args[1];
			String[] sample_names = sample_names_str.split(",");
			System.out.println(create_barplot_script(args[0], sample_names, args[2]));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String create_barplot_script(String inputDataFile, String[] tag, String outputFile) {
		String script = "library(ggplot2);\n";
		script += "data=read.csv(\"" + inputDataFile + "\", sep=\"\\t\", header=F);\n";
		script += "PDGFRA_1a = data[,15];\n";
		script += "PDGFRA_1b = data[,16];\n";
		script += "PDGFRA_2 = data[,17];\n";
		script += "PDGFRA_3 = data[,18];\n";
		script += "NTRK1_1 = data[,19];\n";
		script += "NTRK1_2 = data[,20];\n";
		script += "NTRK1_3 = data[,21];\n";
		script += "CNTRL_1 = data[,22];\n";
		script += "CNTRL_2 = data[,23];\n";
		script += "CNTRL_3 = data[,24];\n";


		script += "data1 = rbind(mean(PDGFRA_1a), mean(PDGFRA_1b));\n";
		script += "data1 = rbind(data1, mean(PDGFRA_2));\n";
		script += "data1 = rbind(data1, mean(PDGFRA_3));\n";
		script += "data1 = rbind(data1, mean(NTRK1_1));\n";
		script += "data1 = rbind(data1, mean(NTRK1_2));\n";
		script += "data1 = rbind(data1, mean(NTRK1_3));\n";
		script += "data1 = rbind(data1, mean(CNTRL_1));\n";
		script += "data1 = rbind(data1, mean(CNTRL_2));\n";
		script += "data1 = rbind(data1, mean(CNTRL_3));\n";


		script += "sem = rbind(sd(PDGFRA_1a) / sqrt(length(PDGFRA_1a)), sd(PDGFRA_1b) / sqrt(length(PDGFRA_1b)));\n";
		script += "sem = rbind(sem, sd(PDGFRA_2) / sqrt(length(PDGFRA_2)));\n";
		script += "sem = rbind(sem, sd(PDGFRA_3) / sqrt(length(PDGFRA_3)));\n";
		script += "sem = rbind(sem, sd(NTRK1_1) / sqrt(length(NTRK1_1)));\n";
		script += "sem = rbind(sem, sd(NTRK1_2) / sqrt(length(NTRK1_2)));\n";
		script += "sem = rbind(sem, sd(NTRK1_3) / sqrt(length(NTRK1_3)));\n";
		script += "sem = rbind(sem, sd(CNTRL_1) / sqrt(length(CNTRL_1)));\n";
		script += "sem = rbind(sem, sd(CNTRL_2) / sqrt(length(CNTRL_1)));\n";
		script += "sem = rbind(sem, sd(CNTRL_3) / sqrt(length(CNTRL_1)));\n";

		script += "rownames = rbind('" + tag[0] + "', '" + tag[1] + "');\n";
		script += "rownames = rbind(rownames, '" + tag[2] + "');\n";
		script += "rownames = rbind(rownames, '" + tag[3] + "');\n";
		script += "rownames = rbind(rownames, '" + tag[4] + "');\n";
		script += "rownames = rbind(rownames, '" + tag[5] + "');\n";
		script += "rownames = rbind(rownames, '" + tag[6] + "');\n";
		script += "rownames = rbind(rownames, '" + tag[7] + "');\n";
		script += "rownames = rbind(rownames, '" + tag[8] + "');\n";
		script += "rownames = rbind(rownames, '" + tag[9] + "');\n";

		script += "data = as.data.frame(as.table(data1))\n";
		script += "data$Var2 = rownames;\n";
		script += "colnames(data) = c(\"ID\", \"Group\", \"Intensity\");\n";

		script += "data$sem = sem\n";

		script += "png(file = \"" + outputFile + "\", width=800,height=600);\n";

		script += "qplot(x=Group, y=Intensity,\n";
		script += "data=data, geom=\"bar\", stat=\"identity\",\n";
		script += "position=\"dodge\") + geom_errorbar(aes(ymin=Intensity-sem, ymax=Intensity+sem), width=0.5) + geom_bar(fill=\"lightblue\", colour=\"black\")\n";

		script += "dev.off();\n";
		return script;
	}
}
