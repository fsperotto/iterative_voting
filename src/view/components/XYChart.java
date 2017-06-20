package view.components;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;

import com.mortennobel.imagescaling.experimental.ImprovedMultistepRescaleOp;


public class XYChart extends JPanel {


	private static final long serialVersionUID = 1L;

	protected JCheckBox cb = new JCheckBox();
	
    protected ChartPanel frmChart = null;

    protected JFreeChart objChart = null;
    protected XYSeriesCollection objDataSet = null;
    protected XYPlot objPlot = null;
    protected XYLineAndShapeRenderer objRenderer = null;
    //protected XYSeries objSerie = null;
    
    //protected final float[][] data = new float[2][10000000];
    
    protected String sChartName = "";
    
    public XYChart(String sName) {

        
    	super();
        
    	//setBackground(Color.LIGHT_GRAY);
    	setBackground(Color.WHITE);
    	
        sChartName = sName;
        
		/*
        
		//Create the button.
		JButton button = new JButton();
		
		button.setText(sText);
		button.setActionCommand(sActionCommand);
		button.setToolTipText(sToolTip);

		button.addActionListener(this);
		*/
		
        ImageIcon icoOn = null;	
        ImageIcon icoOff = null;	
        
    	try { 

    		//look for the image
    		String sImageFileName = "Button Turn On-01.png";
			String sImgLocation = "/images/icons/" + sImageFileName;
			URL url = this.getClass().getResource(sImgLocation);
			BufferedImage img = ImageIO.read(url); 
			//resize image
            ImprovedMultistepRescaleOp resampleOp = new ImprovedMultistepRescaleOp(25, 25);
            img = resampleOp.filter(img, null);
            icoOn = new ImageIcon(img);

    		//look for the image
    		sImageFileName = "Button Turn Off-01.png";
			sImgLocation = "/images/icons/" + sImageFileName;
			url = this.getClass().getResource(sImgLocation);
			img = ImageIO.read(url); 
			//resize image
            resampleOp = new ImprovedMultistepRescaleOp(25, 25);
            img = resampleOp.filter(img, null);
            icoOff = new ImageIcon(img);
            
    	} catch (IllegalArgumentException e) {
			System.err.println("Resource not found : image for button.");
    	} catch (IOException e) {
			System.err.println("Resource not found : image for button.");
    	}    	

		if (icoOff != null) {
	    	cb.setIcon(icoOff);
	    	cb.setDisabledIcon(icoOff);
	    	cb.setRolloverIcon(icoOff);
		}

		if (icoOn != null) {
	    	cb.setSelectedIcon(icoOn);
	    	cb.setDisabledSelectedIcon(icoOn);
	    	cb.setRolloverSelectedIcon(icoOn);
	    	//cb.setPressedIcon(icoOn);
		}

		cb.setBackground(Color.LIGHT_GRAY);
		//cb.setBackground(Color.WHITE);

		cb.setSelected(true);
		
		
        // create the chart...

        objDataSet = new XYSeriesCollection();

        /*
        StandardChartTheme theme = (StandardChartTheme)org.jfree.chart.StandardChartTheme.createJFreeTheme();

        theme.setTitlePaint( Color.BLACK );
        theme.setPlotBackgroundPaint( Color.WHITE );
        theme.setChartBackgroundPaint( Color.WHITE );
        theme.setAxisLabelPaint( Color.BLACK );
        theme.setWallPaint(Color.WHITE );
        
    	ChartFactory.setChartTheme(theme);
		*/
        
    	
    	objChart = ChartFactory.createXYLineChart(
    		sName,      			  // chart title
            "",                      // x axis label
            "",                      // y axis label
            objDataSet,                  // data
            PlotOrientation.VERTICAL,
            true,                     // include legend
            false,                    // tooltips
            false                     // urls
        );

    	
        //theme.apply( objChart );
    	
    	
    	String strFontName = "Helvetica";
    	
    	
        objChart.setAntiAlias(true);

        //objChart.setBackgroundPaint(Color.LIGHT_GRAY);
        objChart.setBackgroundPaint(Color.WHITE);
        
        //objChart.setBorderPaint(Color.WHITE);
        

        //objChart.setLegend(null);        
        LegendTitle legend = objChart.getLegend();
        legend.setItemFont(new Font(strFontName, Font.PLAIN, 18));

        
        TextTitle title = objChart.getTitle();
        title.setFont(new Font(strFontName, Font.PLAIN, 20));
        
        
        
        // get a reference to the plot for further customisation...
        objPlot = objChart.getXYPlot();
        
        
        objPlot.setBackgroundPaint(Color.WHITE);
        objPlot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        objPlot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        
        //objPlot.setSeriesRenderingOrder(order);

        
        
        
        
        objRenderer = new XYLineAndShapeRenderer();
        objPlot.setRenderer(objRenderer);


        DecimalFormatSymbols objDecimalFormatSymbols = new DecimalFormatSymbols();
        objDecimalFormatSymbols.setGroupingSeparator(' ');
        objDecimalFormatSymbols.setDecimalSeparator('.');
        
        DecimalFormat objDecimalFormat = new DecimalFormat();
        objDecimalFormat.setDecimalFormatSymbols(objDecimalFormatSymbols);

        
        // change the auto tick unit selection to integer units only...
        NumberAxis rangeAxis = (NumberAxis) objPlot.getRangeAxis();
        
        rangeAxis.setAutoRange(true);
        rangeAxis.setAutoRangeIncludesZero(true);
        
        rangeAxis.setLabelFont(new Font(strFontName, Font.BOLD, 20));
        rangeAxis.setTickLabelFont(new Font(strFontName, Font.PLAIN, 18));

        rangeAxis.setNumberFormatOverride(objDecimalFormat); 

        
        // change the auto tick unit selection to integer units only...
        NumberAxis domainAxis = (NumberAxis) objPlot.getDomainAxis();
        
        //rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        domainAxis.setAutoRange(true);
        domainAxis.setAutoRangeIncludesZero(false);
        
        domainAxis.setLabelFont(new Font(strFontName, Font.BOLD, 20));
        domainAxis.setTickLabelFont(new Font(strFontName, Font.PLAIN, 18));
        
        domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
        domainAxis.setNumberFormatOverride(objDecimalFormat); 
        
        
        
        
        frmChart = new ChartPanel(objChart);
        
        frmChart.setPreferredSize(new Dimension(500, 300));
        
		frmChart.setBackground(Color.WHITE);
		
        
        //chartPanel.setHorizontalZoom(true);
        //chartPanel.setVerticalZoom(true);
        
        //frmChart.setMinimumDrawHeight(10);
        //frmChart.setMaximumDrawHeight(2000);
        //frmChart.setMinimumDrawWidth(20);
        //frmChart.setMaximumDrawWidth(2000);        

    	setLayout(new BorderLayout());
        add(frmChart, BorderLayout.CENTER);
        add(cb, BorderLayout.LINE_START);
        
        //initialize first serie
        clear();
        
        objChart.setNotify(false);
        
    }
    
    
    @Override
	public void setName(String name) {
    	super.setName(name);
    	sChartName = name;
    	objChart.setTitle(name);
    	
    }

	public void setDomainLabel(String label) {
    	objPlot.getDomainAxis().setLabel(label);
	}
    
	public void setRangeLabel(String label) {
    	objPlot.getRangeAxis().setLabel(label);
	}
    
    public void addPoint(int iSerie, double dReference, double dValue) { 

    	if (cb.isSelected()) {
	    
    		objDataSet.getSeries(iSerie).add(dReference, dValue);
	 
    	}
	     
    }


    
    public void addSerie(String strSerieName) { 

        // create the serie...
    	XYSeries objSerie = new XYSeries(strSerieName);
        objDataSet.addSeries(objSerie);
        
        objRenderer.setSeriesLinesVisible(objDataSet.getSeriesCount()-1, true);
        objRenderer.setSeriesPaint(objDataSet.getSeriesCount()-1, Color.BLACK);

        Shape shape;
        
        switch ((objDataSet.getSeriesCount()-1) % 7) {
			case 0:
	        	shape = ShapeUtilities.createDiagonalCross(6, 0);
	            objRenderer.setSeriesShape(objDataSet.getSeriesCount()-1, shape);
	            objRenderer.setSeriesShapesVisible(objDataSet.getSeriesCount()-1, true);
	            objRenderer.setSeriesShapesFilled(objDataSet.getSeriesCount()-1, false);
				break;
			case 1:
	        	shape = ShapeUtilities.createRegularCross(6, 0);
	            objRenderer.setSeriesShape(objDataSet.getSeriesCount()-1, shape);
	            objRenderer.setSeriesShapesVisible(objDataSet.getSeriesCount()-1, true);
	            objRenderer.setSeriesShapesFilled(objDataSet.getSeriesCount()-1, false);
				break;
			case 2:
	        	shape = new Ellipse2D.Float(-5,-5,10,10);
	            objRenderer.setSeriesShape(objDataSet.getSeriesCount()-1, shape);
	            objRenderer.setSeriesShapesVisible(objDataSet.getSeriesCount()-1, true);
	            objRenderer.setSeriesShapesFilled(objDataSet.getSeriesCount()-1, false);
				break;
			case 3:
	        	shape = new Rectangle2D.Float(-4,-4,8,8);
	            objRenderer.setSeriesShape(objDataSet.getSeriesCount()-1, shape);
	            objRenderer.setSeriesShapesVisible(objDataSet.getSeriesCount()-1, true);
	            objRenderer.setSeriesShapesFilled(objDataSet.getSeriesCount()-1, false);
				break;
			case 4:
	        	shape = ShapeUtilities.createDownTriangle(5);
	            objRenderer.setSeriesShape(objDataSet.getSeriesCount()-1, shape);
	            objRenderer.setSeriesShapesVisible(objDataSet.getSeriesCount()-1, true);
	            objRenderer.setSeriesShapesFilled(objDataSet.getSeriesCount()-1, false);
				break;
			case 5:
	        	shape = ShapeUtilities.createUpTriangle(5);
	            objRenderer.setSeriesShape(objDataSet.getSeriesCount()-1, shape);
	            objRenderer.setSeriesShapesVisible(objDataSet.getSeriesCount()-1, true);
	            objRenderer.setSeriesShapesFilled(objDataSet.getSeriesCount()-1, false);
				break;
			default:
			case 6:
	        	shape = ShapeUtilities.createDiamond(4);
	            objRenderer.setSeriesShape(objDataSet.getSeriesCount()-1, shape);
	            objRenderer.setSeriesShapesVisible(objDataSet.getSeriesCount()-1, true);
	            objRenderer.setSeriesShapesFilled(objDataSet.getSeriesCount()-1, true);
				break;
		}


        
        
        Stroke stroke;

        switch (((objDataSet.getSeriesCount()-1)/7) % 3) {
			
        	case 0:
	        	stroke = new BasicStroke(1.0f);
				break;
			
			case 1:
	        	stroke = new BasicStroke( 
						1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
	        			1.0f, new float[] {10.0f, 3.0f}, 0.0f);
				break;

			default:
			case 2:
				stroke = new BasicStroke( 
	                    1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
	        			1.0f, new float[] {5.0f, 5.0f}, 0.0f);
        }

        objRenderer.setSeriesStroke(objDataSet.getSeriesCount()-1, stroke);
        
        
        
   }


	public void clear() {
        
		objDataSet.removeAllSeries();
		
	}
    
	
	public void refresh() {

		objChart.setNotify(true);
		objChart.fireChartChanged();
		objChart.setNotify(false);
		
	}

}
