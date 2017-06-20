package view.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import com.mortennobel.imagescaling.experimental.ImprovedMultistepRescaleOp;





public class JPanelApplicationButtons extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 1L;

	protected final JToolBar toolBar = new JToolBar("Controls");
	
	protected JComboBox<String> cmbListScenarios = new JComboBox<String>();
	
	protected JButton btnRun, btnSkip, btnStop, btnStep, btnDefault, btnReset, btnRestart, btnClean;
	protected JTextField tfSkipTime, lblTime;
	//private Label lblTime;
	
	protected List<ApplicationButtonsListener> listeners = new ArrayList<ApplicationButtonsListener>();

	
	
    public void addListener(ApplicationButtonsListener toAdd) {
        listeners.add(toAdd);
    }

    public void clickPlay() {
        // Notify everybody that may be interested.
        for (ApplicationButtonsListener l : listeners)
            l.clickPlay();
    }

    public void clickPause() {
        // Notify everybody that may be interested.
        //for (ApplicationButtonsListener l : listeners)
           // l.clickPause();
    }

    public void clickStep() {
        // Notify everybody that may be interested.
        //for (ApplicationButtonsListener l : listeners)
           // l.clickStep();
    }
    
    public void clickSkip(int iNumSteps) {
        // Notify everybody that may be interested.
        //for (ApplicationButtonsListener l : listeners)
        	//l.clickSkip(iNumSteps);
    }

    public void clickDefault() {
        // Notify everybody that may be interested.
        //for (ApplicationButtonsListener l : listeners)
            //l.clickDefault();
    }

    public void clickReset() {
        // Notify everybody that may be interested.
        for (ApplicationButtonsListener l : listeners)
            l.clickReset();
    }

    public void clickRestart() {
        // Notify everybody that may be interested.
        //for (ApplicationButtonsListener l : listeners)
            //l.clickRestart();
    }
    
    public void clickClean() {
        // Notify everybody that may be interested.
        //for (ApplicationButtonsListener l : listeners)
            //l.clickClean();
    }
    
    
    
    
    public JPanelApplicationButtons() {

    	super(new BorderLayout());
        
    	toolBar.add(cmbListScenarios);
    	toolBar.addSeparator();

	    //cmbListScenarios.addActionListener(this);
    	cmbListScenarios.setMaximumSize(new Dimension(120,40));
    	cmbListScenarios.setSize(new Dimension(10,40));
    	
    	//***
    	cmbListScenarios.setVisible(false);
    	
        btnRun = createButton("Play", "Button Play-01.png");
        btnStop = createButton("Pause", "Button Pause-01.png");
        btnStep = createButton("Step", "Button Skip-01.png");

        btnSkip = createButton("Skip", "Button Forward-01.png");
        tfSkipTime = new JTextField("1000");
        tfSkipTime.setMaximumSize(new Dimension(150, 50));
        tfSkipTime.setMinimumSize(new Dimension(15, 10));
        tfSkipTime.setPreferredSize(new Dimension(100, 30));
        
        btnReset = createButton("Reset", "Button Reload-01.png");
        btnDefault = createButton("Default", "Button Turn On-01.png");       
        btnRestart = createButton("Restart", "Button Log Off-01.png");
        btnClean = createButton("Clean", "Button Turn Off-01.png");
    	
    	//lblTime    = new Label(null);
        lblTime = new JTextField("");
        lblTime.setMaximumSize(new Dimension(150, 50));
        lblTime.setMinimumSize(new Dimension(15, 10));
        lblTime.setPreferredSize(new Dimension(100, 30));
        lblTime.setEditable(false);

        toolBar.add(btnRun);
        toolBar.add(btnStop);
        //toolBar.add(btnStep);
        //toolBar.add(btnSkip);
        //toolBar.add(tfSkipTime);
        toolBar.add(btnReset);
        //toolBar.add(btnDefault);
        //toolBar.add(btnRestart);
        //toolBar.add(btnClean);
        //toolBar.add(lblTime);

        btnRun.addActionListener(this);
        btnStop.addActionListener(this);
        btnStep.addActionListener(this);
        btnSkip.addActionListener(this); 
        btnDefault.addActionListener(this); 
        btnReset.addActionListener(this); 
        btnRestart.addActionListener(this); 
        btnClean.addActionListener(this); 

        add(toolBar, BorderLayout.CENTER);
        
        /*
        ScrollPane scroll = new ScrollPane();
        
        add(scroll, BorderLayout.CENTER);
        
        setPreferredSize(new Dimension(450, 40));
        
        */    	
    	
        
        
    }
    
    public void addScenario(String sName) {
    	cmbListScenarios.addItem(sName);
    }

    public String getScenario() {
    	return cmbListScenarios.getItemAt(cmbListScenarios.getSelectedIndex());
    }

    public void setScenario(String sName) {
    	cmbListScenarios.setSelectedItem(sName);
    }
    

    
    protected JButton createButton(String sText, String sImageFileName) {
    	return createButton(sText, sImageFileName, null, null);
    }
    
    protected JButton createButton(String sText, String sImageFileName, String sActionCommand, String sToolTip) {
			
			//Create the button.
			JButton button = new JButton();
			
			//initialize the button.
			button.setText(sText);
			button.setActionCommand(sActionCommand);
			button.setToolTipText(sToolTip);

			button.addActionListener(this);
			
            ImageIcon ico = null;	
            
	    	try { 
				//look for the image
				String sImgLocation = "/images/icons/" + sImageFileName;
				URL url = this.getClass().getResource(sImgLocation);
				BufferedImage img = ImageIO.read(url); 
				//resize image
	            ImprovedMultistepRescaleOp resampleOp = new ImprovedMultistepRescaleOp(25, 25);
	            img = resampleOp.filter(img, null);
	            ico = new ImageIcon(img);	
	    	} catch (IllegalArgumentException e) {
				System.err.println("Resource not found : image for button.");
	    	} catch (IOException e) {
				System.err.println("Resource not found : image for button.");
	    	}    	

			if (ico != null) {
				button.setIcon(ico);
			}

			return button;
			
		}    
    
    
    
    
    
    @Override
	public void actionPerformed(java.awt.event.ActionEvent e) {
        Object s=e.getSource();
        if  (s==btnRun)   clickPlay();
        if  (s==btnStop)  clickPause();
        if  (s==btnStep)  clickStep();
        if  (s==btnDefault) clickDefault();
        if  (s==btnReset) clickReset();
        if  (s==btnRestart) clickRestart();
        if  (s==btnClean) clickClean();
        if  (s==btnSkip)  clickSkip(Integer.parseInt(tfSkipTime.getText()));
    }
    
    public void setTime(long iTime) {
    	lblTime.setText(String.valueOf(iTime)); 
    }
    
    
}
