package DifferentialExpression;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

public class CalculateLIMMA {

	public static void main(String[] args) {
		Example1(args);		
	}
	public static void Example1(String[] args) {
		try {
			String inputFile = args[0];
			String groupFile = args[1];			
			LinkedList groupA_list = readGroupList(groupFile);
			String groupA = list2str(groupA_list);
			LinkedList groupOther_list = separateGroup(inputFile, groupA_list);
			String groupOther = list2str(groupOther_list);
			
			String outputFileUp = args[2];
			String outputFileDown = args[3];
			String outputFileAll = args[4];
			String type = args[5];
			boolean takeLog = Boolean.valueOf(args[6]);
			boolean quanNorm = false;
			if (args.length >= 8) {
				quanNorm = Boolean.valueOf(args[7]);
			}
			if (type.toUpperCase().equals("ALL")) {
				System.out.println(createScriptLimma(inputFile, outputFileUp, outputFileDown, outputFileAll, groupA, groupOther, 1.0, 0, takeLog, quanNorm));
			} else if (type.toUpperCase().equals("PVALUE")){
				System.out.println(createScriptLimma(inputFile, outputFileUp, outputFileDown, outputFileAll, groupA, groupOther, 0.05, 0, takeLog, quanNorm));
			} else if (type.toUpperCase().equals("FOLDCHANGE")) {
				System.out.println(createScriptLimma(inputFile, outputFileUp, outputFileDown, outputFileAll, groupA, groupOther, 1.0, 1.0, takeLog, quanNorm));
			} else {
				System.out.println(createScriptLimma(inputFile, outputFileUp, outputFileDown, outputFileAll, groupA, groupOther, 0.05, 1.0, takeLog, quanNorm));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String Example1_Warning() {
		String warning = "The program should be executed as the following";
		return warning;
	}
	public static void CompareOneGroup(String[] args) {
		try {
			String inputFile = args[0];
			String groupFile = args[1];			
			LinkedList groupA_list = readGroupList(groupFile);
			String groupA = list2str(groupA_list);
			LinkedList groupOther_list = separateGroup(inputFile, groupA_list);
			String groupOther = list2str(groupOther_list);
			String outputFileUp = args[2];
			String outputFileDown = args[3];
			String outputFileAll = args[4];			
			String type = args[5];
			boolean takeLog = Boolean.valueOf(args[6]);
			boolean quanNorm = false;
			if (args.length >= 8) {
				quanNorm = Boolean.valueOf(args[7]);
			}
			if (type.toUpperCase().equals("ALL")) {
				System.out.println(createScriptLimma(inputFile, outputFileUp, outputFileDown, outputFileAll, groupA, groupOther, 1.0, 0, takeLog, quanNorm));
			} else if (type.toUpperCase().equals("PVALUE")){
				System.out.println(createScriptLimma(inputFile, outputFileUp, outputFileDown, outputFileAll, groupA, groupOther, 0.05, 0, takeLog, quanNorm));
			} else if (type.toUpperCase().equals("FOLDCHANGE")) {
				System.out.println(createScriptLimma(inputFile, outputFileUp, outputFileDown, outputFileAll, groupA, groupOther, 1.0, 1.0, takeLog, quanNorm));
			} else {
				System.out.println(createScriptLimma(inputFile, outputFileUp, outputFileDown, outputFileAll, groupA, groupOther, 0.05, 1.0, takeLog, quanNorm));
			}
			//System.out.println(createScriptLimma(inputFile, outputFileUp, outputFileDown, groupA, groupOther, 0, 0));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void CompareTwoGroup(String[] args) {
		try {
			String inputFile = args[0];
			String groupFile1 = args[1];
			String groupFile2 = args[2];			
			LinkedList groupA_list = readGroupList(groupFile1);
			String groupA = list2str(groupA_list);
			LinkedList groupOther_list = readGroupList(groupFile2);
			String groupOther = list2str(groupOther_list);
			String outputFileUp = args[3];
			String outputFileDown = args[4];
			String outputFileAll = args[5];
			String type = args[6];
			boolean takeLog = Boolean.valueOf(args[7]);
			boolean quanNorm = false;
			if (args.length >= 9) {
				quanNorm = Boolean.valueOf(args[8]);
			}
			if (type.toUpperCase().equals("ALL")) {
				System.out.println(createScriptLimma(inputFile, outputFileUp, outputFileDown, outputFileAll, groupA, groupOther, 1.0, 0, takeLog, quanNorm));
			} else if (type.toUpperCase().equals("PVALUE")){
				System.out.println(createScriptLimma(inputFile, outputFileUp, outputFileDown, outputFileAll, groupA, groupOther, 0.05, 0, takeLog, quanNorm));
			} else if (type.toUpperCase().equals("FOLDCHANGE")) {
				System.out.println(createScriptLimma(inputFile, outputFileUp, outputFileDown, outputFileAll, groupA, groupOther, 1.0, 1.0, takeLog, quanNorm));
			} else {
				System.out.println(createScriptLimma(inputFile, outputFileUp, outputFileDown, outputFileAll, groupA, groupOther, 0.05, 1.0, takeLog, quanNorm));
			}
			//System.out.println(createScriptLimma(inputFile, outputFileUp, outputFileDown, groupA, groupOther, 0.05, 1.0));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void CompareTwoGroupFlex(String[] args) {
		try {
			String inputFile = args[0];
			String groupFile1 = args[1];
			String groupFile2 = args[2];			
			LinkedList groupA_list = readGroupList(groupFile1);
			String groupA = list2str(groupA_list);
			LinkedList groupOther_list = readGroupList(groupFile2);
			String groupOther = list2str(groupOther_list);
			String outputFileUp = args[3];
			String outputFileDown = args[4];
			String outputFileAll = args[5];			
			double pvalue = new Double(args[6]);
			double foldchange = new Double(args[7]);
			boolean takeLog = Boolean.valueOf(args[8]);
			boolean quanNorm = false;
			if (args.length >= 10) {
				quanNorm = Boolean.valueOf(args[9]);
			}
			/*boolean takeLog = Boolean.valueOf(args[7]);
			if (type.toUpperCase().equals("ALL")) {
				System.out.println(createScriptLimma(inputFile, outputFileUp, outputFileDown, outputFileAll, groupA, groupOther, 1.0, 0, takeLog));
			} else if (type.toUpperCase().equals("PVALUE")){
				System.out.println(createScriptLimma(inputFile, outputFileUp, outputFileDown, outputFileAll, groupA, groupOther, 0.05, 0, takeLog));
			} else if (type.toUpperCase().equals("FOLDCHANGE")) {
				System.out.println(createScriptLimma(inputFile, outputFileUp, outputFileDown, outputFileAll, groupA, groupOther, 1.0, 1.0, takeLog));
			} else {
				System.out.println(createScriptLimma(inputFile, outputFileUp, outputFileDown, outputFileAll, groupA, groupOther, 0.05, 1.0, takeLog));
			}*/
			System.out.println(createScriptLimma(inputFile, outputFileUp, outputFileDown, outputFileAll, groupA, groupOther, pvalue, foldchange, takeLog, quanNorm));
			//System.out.println(createScriptLimma(inputFile, outputFileUp, outputFileDown, groupA, groupOther, 0.05, 1.0));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void Example2(String[] args) {
		try {
			String inputFile = args[0];
			String groupFile1 = args[1];
			String groupFile2 = args[2];			
			LinkedList groupA_list = readGroupList(groupFile1);
			String groupA = list2str(groupA_list);
			LinkedList groupOther_list = readGroupList(groupFile2);
			String groupOther = list2str(groupOther_list);
			
			String outputFileUp = args[3];
			String outputFileDown = args[4];
			String outputFileAll = args[5];
			boolean takeLog = Boolean.valueOf(args[6]);
			boolean quanNorm = false;
			if (args.length >= 8) {
				quanNorm = Boolean.valueOf(args[7]);
			}
			System.out.println(createScriptLimma(inputFile, outputFileUp, outputFileDown, outputFileAll, groupA, groupOther, 0.05, 1.0, takeLog, quanNorm));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String list2str(LinkedList list) {
		LinkedList groupA_list = new LinkedList();
		String groupA = "";
		boolean first = true;
		Iterator itr = list.iterator();
		while (itr.hasNext()) {
			String str = (String)itr.next();
			str = str.replaceAll("-", ".");
			if (first) {
				groupA += "'" + str + "'";					
				first = false;
				
			} else {
				groupA += ",'" + str + "'";
				
			}
		}
		return groupA;
	}
	public static LinkedList readGroupList(String groupFile) {
		LinkedList groupA_list = new LinkedList();
		String groupA = "";
		String groupOther = "";
		try {

			boolean first = true;
			FileInputStream fstream = new FileInputStream(groupFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine().trim();
				if (first) {
					groupA += "'" + str + "'";					
					first = false;
					groupA_list.add(str);
				} else {
					groupA += ",'" + str + "'";
					groupA_list.add(str);
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return groupA_list;
	}
	public static LinkedList separateGroup(String fileName, LinkedList list) {
		LinkedList groupB = new LinkedList();
		try {
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String tag = in.readLine();
			String[] split = tag.split("\t");
			for (int i = 1; i < split.length; i++) {
				String str = split[i];
				if (!list.contains(str)) {
					groupB.add(str);
				}
			}
			in.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return groupB;
	}
	public static String createScriptLimma(String inputFile, String outputFileUp, String outputFileDown, String outputFileAll, String listA, String listB, double pvalue, double logFold, boolean takeLog, boolean quanNorm) {
		String script = "";
		script += "library(limma);\n";
		script += "library(edgeR)\n";
		script += "data=read.csv(\"" + inputFile + "\", sep=\"\\t\", header=T, as.is=T);\n";
		script += "gene=data[,1]\n";
		script += "allDat = data;\n";
		script += "selection = allDat;\n";
		script += "genenames = selection[,1];\n";
		script += "col_labels = colnames(allDat[1,]);\n";
		script += "sampleNames = col_labels[2:length(col_labels)];\n";
		script += "colnames(selection) = col_labels;\n";
		script += "rownames(selection) = genenames;\n";
		script += "A = c(" + listA + ")\n";
		script += "B = c(" + listB + ")\n";
		script += "mat = selection[,c(A, B)];\n";
		//script += "numTop = 50;\n";
		script += "rownames(mat)=genenames\n";

		// need to make this a parameter
		script += "isexpr <- rowSums(mat>1) >= 1\n";
		script += "mat <- mat[isexpr,]\n";		
		if (takeLog) {
			script += "mat = log2(mat + 0.1);\n";
		}

		if (quanNorm) {
			script += "quantile_normalisation <- function(df){\n";
			script += "df_rank <- apply(df,2,rank,ties.method=\"min\")\n";
			script += "df_sorted <- data.frame(apply(df, 2, sort))\n";
			script += "df_mean <- apply(df_sorted, 1, mean)\n";			   
			script += "index_to_mean <- function(my_index, my_mean){\n";
			script += "return(my_mean[my_index])\n";
			script += "}\n";			   
			script += "df_final <- apply(df_rank, 2, index_to_mean, my_mean=df_mean)\n";
			script += "rownames(df_final) <- rownames(df)\n";
			script += "return(df_final)\n";
			script += "}\n";
			script += "mat = quantile_normalisation(mat);\n";
		}
		
		script += "groupAOther = factor(c(rep(\"A\", length(A)), rep(\"B\", length(B))));\n";
		script += "designA = model.matrix(~0 + groupAOther);\n";
		
		script += "fit <- lmFit(mat,design = designA)\n";
		script += "contrast.matrix <- makeContrasts(groupAOtherA - groupAOtherB, levels=designA)\n";
		script += "fit2 <- contrasts.fit(fit, contrast.matrix)\n";
		script += "fit2 <- eBayes(fit2);\n";
		script += "options(digits=4)\n";
		script += "top1 = topTable(fit2,coef=1,n=30000,sort=\"p\", p.value=1.0, adjust.method=\"BH\")\n";
		script += "top1Pos = top1$logFC > " + logFold + "\n";
		script += "top1 = top1[top1Pos,]\n";
		script += "top1Pos = top1$P.Value < " + pvalue + "\n";
		script += "top1 = top1[top1Pos,]\n";
		script += "top1Pval = head(sort(top1$P.Value,decreasing=FALSE), n = 30000)\n";
		script += "write.table(top1, file=\"" + outputFileUp + "\", sep=\"\t\");\n";
		
		
		script += "contrast.matrix <- makeContrasts(groupAOtherB - groupAOtherA, levels=designA)\n";
		script += "fit2 <- contrasts.fit(fit, contrast.matrix)\n";
		script += "fit2 <- eBayes(fit2);\n";
		script += "options(digits=4)\n";
		script += "top1 = topTable(fit2,coef=1,n=30000,sort=\"p\", p.value=1.0, adjust.method=\"BH\")\n";
		script += "top1Pos = top1$logFC > " + logFold + "\n";
		script += "top1 = top1[top1Pos,]\n";
		script += "top1Pos = top1$P.Value < " + pvalue + "\n";
		script += "top1 = top1[top1Pos,]\n";
		script += "top1Pval = head(sort(top1$P.Value,decreasing=FALSE), n = 30000)\n";
		script += "write.table(top1, file=\"" + outputFileDown + "\", sep=\"\t\");\n";
		
		script += "contrast.matrix <- makeContrasts(groupAOtherA - groupAOtherB, levels=designA)\n";
		script += "fit2 <- contrasts.fit(fit, contrast.matrix)\n";
		script += "fit2 <- eBayes(fit2);\n";
		script += "options(digits=4)\n";
		script += "top1 = topTable(fit2,coef=1,n=30000,sort=\"p\", p.value=1.0, adjust.method=\"BH\")\n";
		script += "top1Pval = head(sort(top1$P.Value,decreasing=FALSE), n = 30000)\n";
		script += "write.table(top1, file=\"" + outputFileAll + "\", sep=\"\t\");\n";
		script += "library(ggplot2)\n";
		script += "png(\"MAPLOT_" + outputFileAll + ".png\")\n";
		script += "p=ggplot(top1, aes(AveExpr,logFC)) + geom_hline(yintercept=0,alpha=0.5) +geom_point(aes(colour=adj.P.Val),alpha=0.5)+scale_colour_gradient(low=\"blue\",high=\"black\")\n";
		script += "p+xlab(\"Average Log Expression[A]\")+ylab(\"logFC[M]\");\n";
		script += "dev.off();\n";
		return script;
	}
	public static String createSampleList() {
		return "";	}
}
