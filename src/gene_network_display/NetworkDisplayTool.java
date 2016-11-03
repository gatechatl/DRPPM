package gene_network_display;

public class NetworkDisplayTool {

	public static String generateHTML(String networkName) {
		String script = "";
		script += "<!DOCTYPE html>\n";
		script += "<html>\n";
		script += "<head>\n";
		script += "<link href=\"pic.css\" rel=\"stylesheet\">\n";
		
		script += "<script src=\"http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js\"></script>\n";
		script += "<meta charset=utf-8 />\n";
		script += "<title>Cytoscape.js images</title>\n";
		script += "<script src=\"http://cytoscape.github.io/cytoscape.js/api/cytoscape.js-latest/cytoscape.min.js\"></script>\n";
		script += "<script src=\"https://ajax.googleapis.com/ajax/libs/angularjs/1.2.16/angular.min.js\"></script>\n";
		script += "<script src=\"http://cytoscape.github.io/cytoscape.js/api/cytoscape.js-latest/cytoscape.min.js\"></script>\n";
		script += "<script src=\"http://cdnjs.cloudflare.com/ajax/libs/qtip2/2.2.0/jquery.qtip.min.js\"></script>\n";
		script += "<script src=\"http://cdnjs.cloudflare.com/ajax/libs/qtip2/2.2.0/jquery.qtip.min.js\"></script>\n";
		script += "<link href=\"http://cdnjs.cloudflare.com/ajax/libs/qtip2/2.2.0/jquery.qtip.min.css\" rel=\"stylesheet\" type=\"text/css\" />\n";
		script += "<script src=\"https://cdn.rawgit.com/cpettitt/dagre/v0.7.4/dist/dagre.min.js\"></script>\n";
		script += "<script src=\"https://cdn.rawgit.com/cytoscape/cytoscape.js-dagre/1.1.2/cytoscape-dagre.js\"></script>\n";
		script += "<script src=\"https://cdn.rawgit.com/cytoscape/cytoscape.js-spread/1.0.9/cytoscape-spread.js\"></script>\n";
		
		script += "<script src=\"https://rawgit.com/cytoscape/cytoscape.js-qtip/master/cytoscape-qtip.js\"></script>\n";
		script += "<script src=\"cytoscape.js-cxtmenu.js\"></script>\n";
		script += "<button onclick=\"outputJPG()\">JPG</button>\n";
		script += "<button onclick=\"outputPNG()\">PNG</button>\n";
		script += "<button onclick=\"outputJson()\">JSON</button>\n";
		script += "<input type=\"text\" name=\"txtFontSize\" id=\"txtFontSize\" value=\"0\">\n";
		script += "<button onclick=\"setFontSize()\">SetFontSize</button>\n";
		script += "<a id=\"downloadAnchorElem\" style=\"display:none\"></a>\n";
		
		script += "<button onclick=\"setLayoutCose()\">CoseLayout</button>\n";
		script += "<button onclick=\"setLayoutGrid()\">GridLayout</button>\n";
		//script += "<button onclick=\"setLayoutPreset()\">PresetLayout</button>\n";
		script += "<button onclick=\"setLayoutCircle()\">CircleLayout</button>\n";
		//script += "<button onclick=\"setLayoutDagre()\">DagreLayout</button>\n";
		//script += "<button onclick=\"setLayoutSpread()\">SpreadLayout</button>\n";
		script += "<button onclick=\"setLayoutBreadthFirst()\">BFSLayout</button>\n";
		script += "<button onclick=\"setLayoutConcentric()\">ConcentricLayout</button>\n";
		script += "<button onclick=\"setLayoutRandom()\">RandomLayout</button>\n";
		
		//script += "<a id=\"downloadAnchorElem2\" style=\"display:none\"></a>\n";
		script += "</head>\n";
		script += "<body>\n";
		script += "<h1>" + networkName + "</h1>\n";
		script += "<table style=\"width:100%\">\n";
		script += "<tr>\n";
		script += "<td width=75% height=100%><div id=\"cy\"></div></td>\n";
		//script += "<td width=25% height=100% valign="top"><div id="right"><IMG SRC="../../Network_Legend.png" ALT="some text" WIDTH=300 HEIGHT=240></div></td>\n";
		script += "</tr>\n";
		script += "</table>\n";
		script += "</body>\n";
		script += "<script src=\"output.js\"></script>\n";
		script += "<script type=\"text/javascript\">\n";
		script += "</script>\n";
		script += "</html>\n";

		return script;
	}
	
	public static String generateCSS() {
		String script = "";
		script += "body {\n";
		script += "font: 14px helvetica neue, helvetica, arial, sans-serif;\n";
		script += "}\n";
		script += "#cy {\n";
		script += "width: 100%;\n";
		script += "height: 600px;\n";
		script += "border: 1px solid #888;\n";
		script += "}\n";

		script += "#right {\n";
		script += "border: 1px solid #888\n";
		script += "}\n";
		script += "#eat {\n";
		script += "position: absolute;\n";
		script += "left: 1em;\n";
		script += "top: 1em;\n";
		script += "font-size: 1em;\n";
		script += "z-index: -1;\n";
		script += "color: #c88;\n";
		script += "}\n";
		return script;
	}
	
}
