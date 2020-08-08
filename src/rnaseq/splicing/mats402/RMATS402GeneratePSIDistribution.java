package rnaseq.splicing.mats402;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import misc.CommandLine;

public class RMATS402GeneratePSIDistribution {

	public static String type() {
		return "rMATS";
	}
	public static String description() {
		return "rMATS 4.0.2 generate PSI distribution.";
	}
	public static String parameter_info() {
		return "[rMATS outputFolder]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String folder = args[0];
			String A3SS_JCEC = folder + "/A3SS.MATS.JCEC.txt";
			String A3SS_JC = folder + "/A3SS.MATS.JC.txt";
			String A5SS_JCEC = folder + "/A5SS.MATS.JCEC.txt";
			String A5SS_JC = folder + "/A5SS.MATS.JC.txt";
			String MXE_JCEC = folder + "/MXE.MATS.JCEC.txt";
			String MXE_JC = folder + "/MXE.MATS.JC.txt";
			String RI_JCEC = folder + "/RI.MATS.JCEC.txt";
			String RI_JC = folder + "/RI.MATS.JC.txt";
			String SE_JCEC = folder + "/SE.MATS.JCEC.txt";
			String SE_JC = folder + "/SE.MATS.JC.txt";
			String A3SS_JCEC_script = generate_distribution(A3SS_JCEC);
			String A3SS_JC_script = generate_distribution(A3SS_JC);
			String A5SS_JCEC_script = generate_distribution(A5SS_JCEC);
			String A5SS_JC_script = generate_distribution(A5SS_JC);
			String MXE_JCEC_script = generate_distribution(MXE_JCEC);
			String MXE_JC_script = generate_distribution(MXE_JC);
			String RI_JCEC_script = generate_distribution(RI_JCEC);
			String RI_JC_script = generate_distribution(RI_JC);
			String SE_JCEC_script = generate_distribution(SE_JCEC);
			String SE_JC_script = generate_distribution(SE_JC);
			CommandLine.writeFile(folder + "/A3SS.MATS.JCEC.txt.r", A3SS_JCEC_script);
			CommandLine.executeCommand("R --vanilla < " + folder + "/A3SS.MATS.JCEC.txt.r");
			
			CommandLine.writeFile(folder + "/A3SS.MATS.JC.txt.r", A3SS_JCEC_script);
			CommandLine.executeCommand("R --vanilla < " + folder + "/A3SS.MATS.JCEC.txt.r");
			
			CommandLine.writeFile(folder + "/A5SS.MATS.JCEC.txt.r", A5SS_JCEC_script);
			CommandLine.executeCommand("R --vanilla < " + folder + "/A5SS.MATS.JCEC.txt.r");
			
			CommandLine.writeFile(folder + "/A5SS.MATS.JC.txt.r", A5SS_JCEC_script);
			CommandLine.executeCommand("R --vanilla < " + folder + "/A5SS.MATS.JCEC.txt.r");
			
			CommandLine.writeFile(folder + "/MXE.MATS.JCEC.txt.r", MXE_JCEC_script);
			CommandLine.executeCommand("R --vanilla < " + folder + "/MXE.MATS.JCEC.txt.r");

			CommandLine.writeFile(folder + "/MXE.MATS.JC.txt.r", MXE_JC_script);
			CommandLine.executeCommand("R --vanilla < " + folder + "/MXE.MATS.JC.txt.r");

			CommandLine.writeFile(folder + "/RI.MATS.JCEC.txt.r", RI_JCEC_script);
			CommandLine.executeCommand("R --vanilla < " + folder + "/RI.MATS.JCEC.txt.r");

			CommandLine.writeFile(folder + "/RI.MATS.JC.txt.r", RI_JC_script);
			CommandLine.executeCommand("R --vanilla < " + folder + "/RI.MATS.JC.txt.r");

			CommandLine.writeFile(folder + "/SE.MATS.JCEC.txt.r", SE_JCEC_script);
			CommandLine.executeCommand("R --vanilla < " + folder + "/SE.MATS.JCEC.txt.r");

			CommandLine.writeFile(folder + "/SE.MATS.JC.txt.r", SE_JC_script);
			CommandLine.executeCommand("R --vanilla < " + folder + "/SE.MATS.JC.txt.r");
						
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String generate_distribution(String inputFile) {
		String script = "#args=(commandArgs(TRUE))\n";
		script += "#infile=args[1]\n";

		script += "#infile<-\"HS.Control/A3SS.MATS.JC.txt\"\n";
		script += "infile = '" + inputFile + "'\n";
		script += "dat<-read.table(infile,header=T)\n";
		script += "sig<-dat[dat$FDR<0.05 & abs(dat$IncLevelDifference)>0.1,]\n";
		script += "file<-sub(\"txt\",\"sig.tsv\",infile)\n";
		script += "write.table(sig, file,row.names=F,col.names=T,sep=\"\\t\",quote=F)\n";

		script += "file<-sub(\"txt\",\"png\",infile)\n";
		script += "png(file,height=400,width=400,type=\"cairo\")\n";

		script += "plot(density(sig$IncLevelDifference),main=paste(\"sig events (n= \",nrow(sig),\")\",sep=\"\"),,xlab=\"delta PSI\",xlim=c(-1,1),ylim=c(0,3.5))\n";
		script += "polygon(density(sig$IncLevelDifference),col=\"grey\")\n";
		script += "dev.off()\n";

		script += "file<-sub(\"txt\",\"pdf\",infile)\n";
		script += "pdf(file,height=4,width=4)\n";

		script += "plot(density(sig$IncLevelDifference),main=paste(\"sig events (n= \",nrow(sig),\")\",sep=\"\"),,xlab=\"delta PSI\",xlim=c(-1,1),ylim=c(0,3.5))\n";
		script += "polygon(density(sig$IncLevelDifference),col=\"grey\")\n";
		script += "abline(v=0,lty=2)\n";
		script += "dev.off()\n";

		return script;

	}
}
