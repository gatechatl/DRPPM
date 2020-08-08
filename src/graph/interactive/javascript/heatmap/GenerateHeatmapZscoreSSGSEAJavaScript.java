package graph.interactive.javascript.heatmap;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class GenerateHeatmapZscoreSSGSEAJavaScript {

	public static String description() {
		return "Generate html-javascript file for displaying Heatmaps";
	}
	public static String type() {
		return "JAVASCRIPT";
	}
	public static String parameter_info() {
		return "[inputMatrix] [sampleInfo]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputMatrixFile = args[0];
			String sampleInfoFile = args[1];
			int width = new Integer(args[2]);
			int height = new Integer(args[3]);
			
			int gene_index = 0;
			int total = 0;
			FileInputStream fstream = new FileInputStream(inputMatrixFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			String header = in.readLine();
			//String genotype_line = in.readLine();
			String[] split_header = header.split("\t");
			String[] col_header = new String[split_header.length - 1];
			for (int i = 1 ; i < split_header.length; i++) {
				col_header[i - 1] = split_header[i];
			}						
			while (in.ready()) {
				String str = in.readLine();
				gene_index++;
				String[] split = str.split("\t");
				for (int i = 1; i < split.length; i++) {
					total++;
				}
			}
			in.close();
			
			double[] genotype_value_simple = new double[total];
			String[] genotype_simple = new String[total];
			fstream = new FileInputStream(sampleInfoFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			//genotype_line = in.readLine();
			int count = 0;
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				genotype_simple[count] = split[1];
				genotype_value_simple[count] = new Double(split[2]);
				count++;
			}
			in.close();
			gene_index++;
			
			
			String[] row_header = new String[gene_index];
			int[] row = new int[total + count];
			int[] col = new int[total + count];
			double[] zscore = new double[total + count + 1];
			double[] fdr = new double[total + count + 1];
			double[] nes = new double[total + count + 1];
			//double[] orig_expr = new double[total + count + 1];
			gene_index = 1;
			total = 0;
			row_header[0] = "Condition";
			for (int i = 0; i < count; i++) {
				col[total] = i + 1;
				row[total] = gene_index;
				zscore[total] = genotype_value_simple[i];
				fdr[total] = genotype_value_simple[i];		
				nes[total] = genotype_value_simple[i];
				total++;
			}
			
			fstream = new FileInputStream(inputMatrixFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			//genotype_line = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");				
				row_header[gene_index] = split[0];
				gene_index++;
				for (int i = 1; i < split.length; i++) {
					col[total] = i;
					row[total] = gene_index;
					zscore[total] = Math.floor(new Double(split[i].split(",")[0]) * 100) / 100;
					fdr[total] = Math.floor(new Double(split[i].split(",")[1]) * 100) / 100;
					double nes_value = Double.NaN;
					if (!split[i].split(",")[2].equals("NA")) {
						nes_value = new Double(split[i].split(",")[2]);
					}
					nes[total] = Math.floor(nes_value * 100) / 100;
					total++;
				}				
			}
			in.close();
			

			System.out.println(generate_heatmap_script(row, col, zscore, fdr, nes, row_header, col_header, genotype_simple, width, height));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String generate_heatmap_script(int[] row, int[] col, double[] zscore, double[] fdr, double[] nes, String[] row_header, String[] col_header, String[] genotype, int width, int height) {
		StringBuffer script = new StringBuffer();
		script.append("<meta charset='utf-8'>\n");
		script.append("     <style>\n");
		script.append("      /* disable text selection */\n");
		script.append("      svg *::selection {\n");
		script.append("         background : transparent;\n");
		script.append("      }\n");
		script.append("     \n");
		script.append("      svg *::-moz-selection {\n");
		script.append("         background:transparent;\n");
		script.append("      } \n");
		script.append("     \n");
		script.append("      svg *::-webkit-selection {\n");
		script.append("         background:transparent;\n");
		script.append("      }\n");
		script.append("      rect.selection {\n");
		script.append("        stroke          : #333;\n");
		script.append("        stroke-dasharray: 4px;\n");
		script.append("        stroke-opacity  : 0.5;\n");
		script.append("        fill            : transparent;\n");
		script.append("      }\n");
		script.append("\n");
		script.append("      rect.cell-border {\n");
		script.append("        stroke: #eee;\n");
		script.append("        stroke-width:0.3px;   \n");
		script.append("      }\n");
		script.append("\n");
		script.append("      rect.cell-selected {\n");
		script.append("        stroke: rgb(51,102,153);\n");
		script.append("        stroke-width:0.5px;   \n");
		script.append("      }\n");
		script.append("\n");
		script.append("      rect.cell-hover {\n");
		script.append("        stroke: #F00;\n");
		script.append("        stroke-width:0.3px;   \n");
		script.append("      }\n");
		script.append("\n");
		script.append("      text.mono {\n");
		script.append("        font-size: 9pt;\n");
		script.append("        font-family: Consolas, courier;\n");
		script.append("        fill: #aaa;\n");
		script.append("      }\n");
		script.append("\n");
		script.append("      text.text-selected {\n");
		script.append("        fill: #000;\n");
		script.append("      }\n");
		script.append("\n");
		script.append("      text.text-highlight {\n");
		script.append("        fill: #c00;\n");
		script.append("      }\n");
		script.append("      text.text-hover {\n");
		script.append("        fill: #00C;\n");
		script.append("      }\n");
		script.append("      #tooltip {\n");
		script.append("        position: absolute;\n");
		script.append("        width: 200px;\n");
		script.append("        height: auto;\n");
		script.append("        padding: 10px;\n");
		script.append("        background-color: white;\n");
		script.append("        -webkit-border-radius: 10px;\n");
		script.append("        -moz-border-radius: 10px;\n");
		script.append("        border-radius: 10px;\n");
		script.append("        -webkit-box-shadow: 4px 4px 10px rgba(0, 0, 0, 0.4);\n");
		script.append("        -moz-box-shadow: 4px 4px 10px rgba(0, 0, 0, 0.4);\n");
		script.append("        box-shadow: 4px 4px 10px rgba(0, 0, 0, 0.4);\n");
		script.append("        pointer-events: none;\n");
		script.append("      }\n");
		script.append("\n");
		script.append("      #tooltip.hidden {\n");
		script.append("        display: none;\n");
		script.append("      }\n");
		script.append("\n");
		script.append("      #tooltip p {\n");
		script.append("        margin: 0;\n");
		script.append("        font-family: sans-serif;\n");
		script.append("        font-size: 12px;\n");
		script.append("        line-height: 20px;\n");
		script.append("      }\n");
		script.append("    </style>\n");
		script.append("\n");
		script.append("</head>\n");
		script.append("<div id='tooltip' class='hidden'>\n");
		script.append("        <p><span id='value'></p>\n");
		script.append("</div>\n");
		script.append("<script src='http://d3js.org/d3.v3.min.js'></script>\n");
		script.append("Order: \n");
		/*script.append("  <select id='order'>\n");
		script.append("  <option value='hclust'>by cluster</option>\n");
		script.append("  <option value='probecontrast'>by probe name and contrast name</option>\n");
		script.append("  <option value='probe'>by probe name</option>\n");
		script.append("  <option value='contrast'>by contrast name</option>\n");
		script.append("  <option value='custom'>by log2 ratio</option>\n");
		script.append("  </select>\n");
		script.append("  </select>\n");*/
		script.append("<div id='chart' style='overflow:auto; width:" + width + "px; height:" + height + "px;'></div>\n");
		script.append("\n");
		script.append("<script type='text/javascript'>\n");
		script.append("var margin = { top: 250, right: 10, bottom: 50, left: 300 },\n");
		script.append("\n");
		script.append("  col_number=" + col_header.length + ";\n");
		script.append("  row_number=" + row_header.length + ";\n");
		script.append("  cellSize=15;\n");
		script.append("  width = cellSize*col_number + 250, // - margin.left - margin.right,\n");
		script.append("  height = cellSize*row_number + 450, // - margin.top - margin.bottom,\n");
		script.append("  //gridSize = Math.floor(width / 24),\n");
		script.append("  legendElementWidth = 14,\n");
		script.append("  colorBuckets = 21,\n");
		//script.append("  colors = ['#005824','#1A693B','#347B53','#4F8D6B','#699F83','#83B09B','#9EC2B3','#B8D4CB','#D2E6E3','#EDF8FB','#FFFFFF','#F1EEF6','#E6D3E1','#DBB9CD','#D19EB9','#C684A4','#BB6990','#B14F7C','#A63467','#9B1A53','#91003F'];\n");
		script.append("  colors = ['#1A693B','#347B53','#4F8D6B','#699F83','#83B09B','#9EC2B3','#B8D4CB','#D2E6E3','#EDF8FB','#FFFFFF','#F1EEF6','#E6D3E1','#DBB9CD','#D19EB9','#C684A4','#BB6990','#B14F7C','#A63467','#9B1A53','#91003F'];\n");
		String hcrow = "  hcrow = [1";
		for (int i = 2; i <= row.length; i++) {
			hcrow += "," + i;
		}
		hcrow += "],\n";
		script.append(hcrow);
		
		String hccol = "  hccol = [1";
		for (int i = 2; i <= row.length; i++) {
			hccol += "," + i;
		}
		hccol += "],\n";
		script.append(hccol);
		//script.append("  hccol = [1,2],\n");
		//script.append("  //rowLabel = ['1759080_s_at','1759302_s_at','1759502_s_at','1759540_s_at','1759781_s_at','1759828_s_at','1759829_s_at','1759906_s_at','1760088_s_at','1760164_s_at','1760453_s_at','1760516_s_at','1760594_s_at','1760894_s_at','1760951_s_at','1761030_s_at','1761128_at','1761145_s_at','1761160_s_at','1761189_s_at','1761222_s_at','1761245_s_at','1761277_s_at','1761434_s_at','1761553_s_at','1761620_s_at','1761873_s_at','1761884_s_at','1761944_s_at','1762105_s_at','1762118_s_at','1762151_s_at','1762388_s_at','1762401_s_at','1762633_s_at','1762701_s_at','1762787_s_at','1762819_s_at','1762880_s_at','1762945_s_at','1762983_s_at','1763132_s_at','1763138_s_at','1763146_s_at','1763198_s_at','1763383_at','1763410_s_at','1763426_s_at','1763490_s_at','1763491_s_at'], // change to gene name or probe id\n");
		//script.append("  rowLabel = ['1759080_s_at','1759302_s_at'],\n");
		
		String rowLabel = "  rowLabel = ['" + row_header[0] + "'";
		for (int i = 1; i < row_header.length; i++) {
			rowLabel += ",'" + row_header[i] + "'";
		}
		rowLabel += "],\n";
		
		script.append(rowLabel);
		
		String genotypeLabel = "  genotypeLabel = ['" + genotype[0] + "'";
		for (int i = 1; i < genotype.length; i++) {
			genotypeLabel += ",'" + genotype[i] + "'";
		}
		genotypeLabel += "],\n";
		
		script.append(genotypeLabel);
		//script.append("  //colLabel = ['con1027','con1028','con1029','con103','con1030','con1031','con1032','con1033','con1034','con1035','con1036','con1037','con1038','con1039','con1040','con1041','con108','con109','con110','con111','con112','con125','con126','con127','con128','con129','con130','con131','con132','con133','con134','con135','con136','con137','con138','con139','con14','con15','con150','con151','con152','con153','con16','con17','con174','con184','con185','con186','con187','con188','con189','con191','con192','con193','con194','con199','con2','con200','con201','con21']; // change to contrast name\n");
		//script.append("  colLabel = ['con1027','con1028'];\n");
		
		String colLabel = "  colLabel = ['" + col_header[0] + "'";
		for (int i = 1; i < col_header.length; i++) {
			colLabel += ",'" + col_header[i] + "'";
		}
		colLabel += "];\n";
		script.append(colLabel);
		
		script.append("\n");
		StringBuffer data = new StringBuffer();
		data.append("  var data = [\n");
		for (int i = 0; i < row.length; i++) {
			data.append("{row:" + row[i] + ", col:" + col[i] + ", value:'" + zscore[i] + "', fdr:'" + fdr[i] + "', nes:'" + nes[i] + "'},\n");
		}
		data.append("];\n");
		script.append(data.toString());
		//script.append("  var data = [{row:1,col:1,value:6},{row:1,col:2,value:-10},{row:2,col:1,value:5},{row:2,col:2,value:-3}];\n");
		script.append("//function(error, data) {\n");
		script.append("  var colorScale = d3.scale.quantile()\n");
		script.append("      .domain([ -3 , 0, 3])\n");
		script.append("      .range(colors);\n");
		script.append("  \n");
		script.append("  var svg = d3.select('#chart').append('svg')\n");
		script.append("      .attr('width', width + margin.left + margin.right)\n");
		script.append("      .attr('height', height + margin.top + margin.bottom)\n");
		script.append("      .append('g')\n");
		script.append("      .attr('transform', 'translate(' + margin.left + ',' + margin.top + ')')\n");
		script.append("      ;\n");
		script.append("  var rowSortOrder=false;\n");
		script.append("  var colSortOrder=false;\n");
		script.append("  var rowLabels = svg.append('g')\n");
		script.append("      .selectAll('.rowLabelg')\n");
		script.append("      .data(rowLabel)\n");
		script.append("      .enter()\n");
		script.append("      .append('text')\n");
		script.append("      .text(function (d) { return d; })\n");
		script.append("      .attr('x', 0)\n");
		script.append("      .attr('y', function (d, i) { return hcrow.indexOf(i+1) * cellSize; })\n");
		script.append("      .style('text-anchor', 'end')\n");
		script.append("      .attr('transform', 'translate(-6,' + cellSize / 1.5 + ')')\n");
		script.append("      .attr('class', function (d,i) { return 'rowLabel mono r'+i;} ) \n");
		script.append("      .on('mouseover', function(d) {d3.select(this).classed('text-hover',true);})\n");
		script.append("      .on('mouseout' , function(d) {d3.select(this).classed('text-hover',false);})\n");
		script.append("      .on('click', function(d,i) {rowSortOrder=!rowSortOrder; sortbylabel('r',i,rowSortOrder);d3.select('#order').property('selectedIndex', 4).node().focus();;})\n");
		script.append("      ;\n");
		script.append("\n");
		script.append("  var colLabels = svg.append('g')\n");
		script.append("      .selectAll('.colLabelg')\n");
		script.append("      .data(colLabel)\n");
		script.append("      .enter()\n");
		script.append("      .append('text')\n");
		script.append("      .text(function (d) { return d; })\n");
		script.append("      .attr('x', 3)\n");
		script.append("      .attr('y', function (d, i) { return hccol.indexOf(i+1) * cellSize; })\n");
		script.append("      .style('text-anchor', 'left')\n");
		script.append("      .attr('transform', 'translate('+cellSize/2 + ',-6) rotate (-90)')\n");
		script.append("      .attr('class',  function (d,i) { return 'colLabel mono c'+i;} )\n");
		script.append("      .on('mouseover', function(d) {d3.select(this).classed('text-hover',true);})\n");
		script.append("      .on('mouseout' , function(d) {d3.select(this).classed('text-hover',false);})\n");
		script.append("      .on('click', function(d,i) {colSortOrder=!colSortOrder;  sortbylabel('c',i,colSortOrder);d3.select('#order').property('selectedIndex', 4).node().focus();;})\n");
		script.append("      ;\n");
		script.append("\n");
		script.append("  var heatMap = svg.append('g').attr('class','g3')\n");
		script.append("        .selectAll('.cellg')\n");
		script.append("        .data(data,function(d){return d.row+':'+d.col;})\n");
		script.append("        .enter()\n");
		script.append("        .append('rect')\n");
		script.append("        .attr('x', function(d) { return hccol.indexOf(d.col) * cellSize; })\n");
		script.append("        .attr('y', function(d) { return hcrow.indexOf(d.row) * cellSize; })\n");
		script.append("        .attr('class', function(d){return 'cell cell-border cr'+(d.row-1)+' cc'+(d.col-1);})\n");
		script.append("        .attr('width', cellSize)\n");
		script.append("        .attr('height', cellSize)\n");
		script.append("        .style('fill', function(d) { return colorScale(d.value); })\n");
		script.append("        /* .on('click', function(d) {\n");
		script.append("               var rowtext=d3.select('.r'+(d.row-1));\n");
		script.append("               if(rowtext.classed('text-selected')==false){\n");
		script.append("                   rowtext.classed('text-selected',true);\n");
		script.append("               }else{\n");
		script.append("                   rowtext.classed('text-selected',false);\n");
		script.append("               }\n");
		script.append("        })*/\n");
		script.append("        .on('mouseover', function(d){\n");
		script.append("               //highlight text\n");
		script.append("               d3.select(this).classed('cell-hover',true);\n");
		script.append("               d3.selectAll('.rowLabel').classed('text-highlight',function(r,ri){ return ri==(d.row-1);});\n");
		script.append("               d3.selectAll('.colLabel').classed('text-highlight',function(c,ci){ return ci==(d.col-1);});\n");
		script.append("        \n");
		script.append("               //Update the tooltip position and value\n");
		script.append("               d3.select('#tooltip')\n");
		script.append("                 .style('left', (d3.event.pageX+10) + 'px')\n");
		script.append("                 .style('top', (d3.event.pageY+20) + 'px')\n");
		script.append("                 .select('#value')\n");
		//script.append("                 .text('lables:'+rowLabel[d.row-1]+','+colLabel[d.col-1]+'\\ndata:'+d.value+'\\nrow-col-idx:'+d.col+','+d.row+'\\ncell-xy '+this.x.baseVal.value+', '+this.y.baseVal.value);  \n");
		script.append("                 .html(colLabel[d.col-1]+'<br>MetaInfo: '+genotypeLabel[d.col-1]+'<br>'+rowLabel[d.row-1]+'<br>NES: '+d.nes+'<br>fdr: '+d.fdr);  \n");
		script.append("               //Show the tooltip\n");
		script.append("               d3.select('#tooltip').classed('hidden', false);\n");
		script.append("        })\n");
		script.append("        .on('mouseout', function(){\n");
		script.append("               d3.select(this).classed('cell-hover',false);\n");
		script.append("               d3.selectAll('.rowLabel').classed('text-highlight',false);\n");
		script.append("               d3.selectAll('.colLabel').classed('text-highlight',false);\n");
		script.append("               d3.select('#tooltip').classed('hidden', true);\n");
		script.append("        })\n");
		script.append("        ;\n");
		script.append("\n");
		script.append("  var legend = svg.selectAll('.legend')\n");
		//script.append("      .data([-3.0,-2.6,-2.3,-2,-1.6,-1.3,-1.0,-0.6,-0.3,0,0.3,0.6,1.0,1.3,1.6,2.0,2.3,2.6,3.0])\n");
		script.append("      .data(['-3','','','-2','','','-1','','','0','','','1','','','2','','','3'])\n");
		script.append("      .enter().append('g')\n");
		script.append("      .attr('class', 'legend');\n");
		script.append(" \n");
		script.append("  legend.append('rect')\n");
		script.append("    .attr('x', function(d, i) { return legendElementWidth * i; })\n");
		//script.append("    .attr('y', height+(cellSize*2))\n");
		script.append("    .attr('y', height - margin.top)\n");
		script.append("    .attr('width', legendElementWidth)\n");
		script.append("    .attr('height', cellSize)\n");
		script.append("    .style('fill', function(d, i) { return colors[i]; });\n");
		script.append(" \n");
		script.append("  legend.append('text')\n");
		script.append("    .attr('class', 'mono')\n");
		script.append("    .text(function(d) { return d; })\n");
		script.append("    .style(\"font-size\",\"12px\")\n");
		script.append("    .attr('width', legendElementWidth)\n");
		script.append("    .attr('x', function(d, i) { return legendElementWidth * i; })\n");
		script.append("    .attr('y', height - margin.top - 15);\n");
		script.append("    //.attr('y', height + (cellSize*4));\n");
		script.append("\n");
		
        // user controls and input area 
		// save image as svg file
		script.append("    function downloadSVG() {\n");
		script.append("    	  const svg=document.querySelector('svg')\n");
		script.append("    	  const styles = window.getComputedStyle(svg)\n");
		script.append("    	  const d3svg=d3.select(svg)\n");
		script.append("    	  for(const s of styles) {\n");
		script.append("    	    d3svg.style(s,styles.getPropertyValue(s))\n");
		script.append("    	  }\n");

		script.append("    	  const a=document.createElement('a')\n");
		script.append("    	  document.body.appendChild(a)\n");
		script.append("    	  a.addEventListener('click',function(){\n");
		script.append("    	    const serializer = new XMLSerializer()\n");
		script.append("    	    const svg_blob = new Blob([serializer.serializeToString(svg)], {'type': \"image/svg+xml\"})\n");
		script.append("    	    a.download='chord.svg'\n");
		script.append("    	    a.href=URL.createObjectURL(svg_blob)\n");
		script.append("    	    document.body.removeChild(a)\n");
		script.append("    	  },false)\n");
		script.append("    	  a.click()\n");
		script.append("    	}\n");

		script.append("    	    var textDiv=d3.select(\"body\").append(\"div\")\n");
		script.append("    	    var controlsDiv=textDiv.append('div')\n");
		script.append("    	    var downloadBtn=controlsDiv.append('button')\n");
		script.append("    	      .html('Download SVG')\n");
		script.append("    	      .on('click',downloadSVG)\n");
		/*script.append("    	    var submitBtn=controlsDiv.append('button')\n");
		script.append("    	      .html('Update')\n");
		script.append("    	      .on('click',()=>{\n");
		script.append("    	        main.selectAll('g').remove()\n");
		script.append("    	        var line=textarea.property('value')\n");
		script.append("    	        renderImage(line)\n");      
		script.append("    	      })\n");*/
		
		// end save image as svg file
		
		script.append("// Change ordering of cells\n");
		script.append("\n");
		script.append("  function sortbylabel(rORc,i,sortOrder){\n");
		script.append("       var t = svg.transition().duration(3000);\n");
		script.append("       var log2r=[];\n");
		script.append("       var sorted; // sorted is zero-based index\n");
		script.append("       d3.selectAll('.c'+rORc+i) \n");
		script.append("         .filter(function(ce){\n");
		script.append("            log2r.push(ce.value);\n");
		script.append("          })\n");
		script.append("       ;\n");
		script.append("       if(rORc=='r'){ // sort log2ratio of a gene\n");
		script.append("         sorted=d3.range(col_number).sort(function(a,b){ if(sortOrder){ return log2r[b]-log2r[a];}else{ return log2r[a]-log2r[b];}});\n");
		script.append("         t.selectAll('.cell')\n");
		script.append("           .attr('x', function(d) { return sorted.indexOf(d.col-1) * cellSize; })\n");
		script.append("           ;\n");
		script.append("         t.selectAll('.colLabel')\n");
		script.append("          .attr('y', function (d, i) { return sorted.indexOf(i) * cellSize; })\n");
		script.append("         ;\n");
		script.append("       }else{ // sort log2ratio of a contrast\n");
		script.append("         sorted=d3.range(row_number).sort(function(a,b){if(sortOrder){ return log2r[b]-log2r[a];}else{ return log2r[a]-log2r[b];}});\n");
		script.append("         t.selectAll('.cell')\n");
		script.append("           .attr('y', function(d) { return sorted.indexOf(d.row-1) * cellSize; })\n");
		script.append("           ;\n");
		script.append("         t.selectAll('.rowLabel')\n");
		script.append("          .attr('y', function (d, i) { return sorted.indexOf(i) * cellSize; })\n");
		script.append("         ;\n");
		script.append("       }\n");
		script.append("  }\n");
		script.append("\n");
		script.append("  d3.select('#order').on('change',function(){\n");
		script.append("    order(this.value);\n");
		script.append("  });\n");
		script.append("  \n");
		script.append("  function order(value){\n");
		script.append("   if(value=='hclust'){\n");
		script.append("    var t = svg.transition().duration(3000);\n");
		script.append("    t.selectAll('.cell')\n");
		script.append("      .attr('x', function(d) { return hccol.indexOf(d.col) * cellSize; })\n");
		script.append("      .attr('y', function(d) { return hcrow.indexOf(d.row) * cellSize; })\n");
		script.append("      ;\n");
		script.append("\n");
		script.append("    t.selectAll('.rowLabel')\n");
		script.append("      .attr('y', function (d, i) { return hcrow.indexOf(i+1) * cellSize; })\n");
		script.append("      ;\n");
		script.append("\n");
		script.append("    t.selectAll('.colLabel')\n");
		script.append("      .attr('y', function (d, i) { return hccol.indexOf(i+1) * cellSize; })\n");
		script.append("      ;\n");
		script.append("\n");
		script.append("   }else if (value=='probecontrast'){\n");
		script.append("    var t = svg.transition().duration(3000);\n");
		script.append("    t.selectAll('.cell')\n");
		script.append("      .attr('x', function(d) { return (d.col - 1) * cellSize; })\n");
		script.append("      .attr('y', function(d) { return (d.row - 1) * cellSize; })\n");
		script.append("      ;\n");
		script.append("\n");
		script.append("    t.selectAll('.rowLabel')\n");
		script.append("      .attr('y', function (d, i) { return i * cellSize; })\n");
		script.append("      ;\n");
		script.append("\n");
		script.append("    t.selectAll('.colLabel')\n");
		script.append("      .attr('y', function (d, i) { return i * cellSize; })\n");
		script.append("      ;\n");
		script.append("\n");
		script.append("   }else if (value=='probe'){\n");
		script.append("    var t = svg.transition().duration(3000);\n");
		script.append("    t.selectAll('.cell')\n");
		script.append("      .attr('y', function(d) { return (d.row - 1) * cellSize; })\n");
		script.append("      ;\n");
		script.append("\n");
		script.append("    t.selectAll('.rowLabel')\n");
		script.append("      .attr('y', function (d, i) { return i * cellSize; })\n");
		script.append("      ;\n");
		script.append("   }else if (value=='contrast'){\n");
		script.append("    var t = svg.transition().duration(3000);\n");
		script.append("    t.selectAll('.cell')\n");
		script.append("      .attr('x', function(d) { return (d.col - 1) * cellSize; })\n");
		script.append("      ;\n");
		script.append("    t.selectAll('.colLabel')\n");
		script.append("      .attr('y', function (d, i) { return i * cellSize; })\n");
		script.append("      ;\n");
		script.append("   }\n");
		script.append("  }\n");
		script.append("  // \n");
		script.append("  var sa=d3.select('.g3')\n");
		script.append("      .on('mousedown', function() {\n");
		script.append("          if( !d3.event.altKey) {\n");
		script.append("             d3.selectAll('.cell-selected').classed('cell-selected',false);\n");
		script.append("             d3.selectAll('.rowLabel').classed('text-selected',false);\n");
		script.append("             d3.selectAll('.colLabel').classed('text-selected',false);\n");
		script.append("          }\n");
		script.append("         var p = d3.mouse(this);\n");
		script.append("         sa.append('rect')\n");
		script.append("         .attr({\n");
		script.append("             rx      : 0,\n");
		script.append("             ry      : 0,\n");
		script.append("             class   : 'selection',\n");
		script.append("             x       : p[0],\n");
		script.append("             y       : p[1],\n");
		script.append("             width   : 1,\n");
		script.append("             height  : 1\n");
		script.append("         })\n");
		script.append("      })\n");
		script.append("      .on('mousemove', function() {\n");
		script.append("         var s = sa.select('rect.selection');\n");
		script.append("      \n");
		script.append("         if(!s.empty()) {\n");
		script.append("             var p = d3.mouse(this),\n");
		script.append("                 d = {\n");
		script.append("                     x       : parseInt(s.attr('x'), 10),\n");
		script.append("                     y       : parseInt(s.attr('y'), 10),\n");
		script.append("                     width   : parseInt(s.attr('width'), 10),\n");
		script.append("                     height  : parseInt(s.attr('height'), 10)\n");
		script.append("                 },\n");
		script.append("                 move = {\n");
		script.append("                     x : p[0] - d.x,\n");
		script.append("                     y : p[1] - d.y\n");
		script.append("                 }\n");
		script.append("             ;\n");
		script.append("      \n");
		script.append("             if(move.x < 1 || (move.x*2<d.width)) {\n");
		script.append("                 d.x = p[0];\n");
		script.append("                 d.width -= move.x;\n");
		script.append("             } else {\n");
		script.append("                 d.width = move.x;       \n");
		script.append("             }\n");
		script.append("      \n");
		script.append("             if(move.y < 1 || (move.y*2<d.height)) {\n");
		script.append("                 d.y = p[1];\n");
		script.append("                 d.height -= move.y;\n");
		script.append("             } else {\n");
		script.append("                 d.height = move.y;       \n");
		script.append("             }\n");
		script.append("             s.attr(d);\n");
		script.append("      \n");
		script.append("                 // deselect all temporary selected state objects\n");
		script.append("             d3.selectAll('.cell-selection.cell-selected').classed('cell-selected', false);\n");
		script.append("             d3.selectAll('.text-selection.text-selected').classed('text-selected',false);\n");
		script.append("\n");
		script.append("             d3.selectAll('.cell').filter(function(cell_d, i) {\n");
		script.append("                 if(\n");
		script.append("                     !d3.select(this).classed('cell-selected') && \n");
		script.append("                         // inner circle inside selection frame\n");
		script.append("                     (this.x.baseVal.value)+cellSize >= d.x && (this.x.baseVal.value)<=d.x+d.width && \n");
		script.append("                     (this.y.baseVal.value)+cellSize >= d.y && (this.y.baseVal.value)<=d.y+d.height\n");
		script.append("                 ) {\n");
		script.append("      \n");
		script.append("                     d3.select(this)\n");
		script.append("                     .classed('cell-selection', true)\n");
		script.append("                     .classed('cell-selected', true);\n");
		script.append("\n");
		script.append("                     d3.select('.r'+(cell_d.row-1))\n");
		script.append("                     .classed('text-selection',true)\n");
		script.append("                     .classed('text-selected',true);\n");
		script.append("\n");
		script.append("                     d3.select('.c'+(cell_d.col-1))\n");
		script.append("                     .classed('text-selection',true)\n");
		script.append("                     .classed('text-selected',true);\n");
		script.append("                 }\n");
		script.append("             });\n");
		script.append("         }\n");
		script.append("      })\n");
		script.append("      .on('mouseup', function() {\n");
		script.append("            // remove selection frame\n");
		script.append("         sa.selectAll('rect.selection').remove();\n");
		script.append("      \n");
		script.append("             // remove temporary selection marker class\n");
		script.append("         d3.selectAll('.cell-selection').classed('cell-selection', false);\n");
		script.append("         d3.selectAll('.text-selection').classed('text-selection',false);\n");
		script.append("      })\n");
		script.append("      .on('mouseout', function() {\n");
		script.append("         if(d3.event.relatedTarget.tagName=='html') {\n");
		script.append("                 // remove selection frame\n");
		script.append("             sa.selectAll('rect.selection').remove();\n");
		script.append("                 // remove temporary selection marker class\n");
		script.append("             d3.selectAll('.cell-selection').classed('cell-selection', false);\n");
		script.append("             d3.selectAll('.rowLabel').classed('text-selected',false);\n");
		script.append("             d3.selectAll('.colLabel').classed('text-selected',false);\n");
		script.append("         }\n");
		script.append("      })\n");
		script.append("      ;\n");
		

		script.append("//} // );\n");
		script.append("</script>\n");
		script.append("\n");

		return script.toString();
	}
}
