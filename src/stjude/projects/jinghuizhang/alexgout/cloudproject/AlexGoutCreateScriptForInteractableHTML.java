package stjude.projects.jinghuizhang.alexgout.cloudproject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class AlexGoutCreateScriptForInteractableHTML {

	
	public static void main(String[] args) {
		
		try {
			
			LinkedList list = new LinkedList();
			//String outputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\AlexGoutStJudeCloudAnalysis\\solid_tumor_expr\\step5_geneName_expr.sh";
			
			
			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\AlexGoutStJudeCloudAnalysis\\solid_tumor_expr\\step6_expr_html.sh";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String inputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\AlexGoutStJudeCloudAnalysis\\solid_tumor_expr\\PanCancer142Genes.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				//out.write("drppm -AppendExpressionColorAsMetaData Tim_classes_tSNE_combined_colorGroup.txt Tim_NormBatchCorrExprMatrix_geneName_PanCancer142.txt " + str + " 90 Tim_classes_tSNE_combined_colorGroup_" + str + ".txt\n");
				out.write("drppm -GenerateScatterPlotJavaScriptUserInputInitializeColor Tim_classes_tSNE_combined_colorGroup_" + str + ".txt 0 1 2 3,4,5,7,8 9999 9999 TSNE_1 TSNE_2 Subtype,Color,Opacity," + str + "_Opacity," + str + "_Expr," + str + "_Color " + str + "_Opacity " + str + "_Color 4 true false > Tim_classes_tSNE_combined_colorGroup_" + str + ".txt.html\n");
				
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
