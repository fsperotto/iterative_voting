package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.PrintStream;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import view.components.ApplicationButtonsListener;
import view.components.CustomOutputStream;
import view.components.JPanelApplicationButtons;
import view.components.XYChart;
import model.VotingSimulator;
import model.voting.Election;
import model.voting.ElectionSeriesData;


/**
* MAIN WINDOW OF THE SIMULATOR (VIEW).
* 
* It knows the simulator engine (model).
*  
* It presents : 
*  - a Menu Bar
*  - a Progress Bar
*  - a Tabbed Pane with
*    - resulting charts (data from simulation)
*    - configuration options
*    - console (text output from simulation)
*  
* @author Filipo S. Perotto / Stéphane Airiau / Umberto Grandi
* @version 1.0
*/


public class SimulatorViewForm extends JFrame {

	
	private static final long serialVersionUID = 1L;
	
	
	//VIEW OBJECTS
	
	protected JPanel pnlTopBar;

	protected JPanelApplicationButtons frmBar; 
	protected JProgressBar objSimulationProgressBar;
	protected JProgressBar objTaskProgressBar;
	
	protected JTabbedPane pnlMain;
	
	protected JTextArea textArea;
	protected CustomOutputStream outputStream;
	protected PrintStream printStream;
	
	protected JScrollPane pnlConsole;

	//protected JAccordionPanel pnlCharts;
	protected JTabbedPane pnlCharts;
	
	protected XYChart chartCondorcetEfficiency; 
	
	protected XYChart chartBordaScore; 
	protected XYChart chartBordaNormalizedScore; 
	protected XYChart chartBordaRatioToMax; 
	protected XYChart chartBordaEfficiency; 

	protected XYChart chartUtilitarian; 
	protected XYChart chartEgalitarian; 

	protected XYChart chartVoterSatisfaction; 

	
	protected JPanel pnlConfig;
	
    protected JTextField txtCandidates;
    protected JTextField txtVoters;
    protected JTextField txtLearning;
    protected JTextField txtRuns;

    
    protected JCheckBox chkVerbose;
    protected JCheckBox chkFile;
	
    protected JCheckBox chkRandomRule;
    protected JCheckBox chkPluralityRule;
    protected JCheckBox chkTwoRoundRule;
    protected JCheckBox chkSTVRule;
    protected JCheckBox chkRecVetoRule;
    protected JCheckBox chkBordaRule;
    protected JCheckBox chkUSWRule;
    protected JCheckBox chkESWRule;
    protected JCheckBox chkCopelandRule;
    protected JCheckBox chkIterativeBestResponseRule;
    protected JCheckBox chkIterative3PragmatistRule;
    protected JCheckBox chkIterativeSecondChanceRule;
    protected JCheckBox chkIterativeLearningSoftmaxRule;
    protected JCheckBox chkIterativeLearningEGreedyRule;
    protected JCheckBox chkIterativeLearningUCBRule;
    protected JCheckBox chkIterativeLearningOptimisticRule;
    
    protected JPanel pnlParameters = new JPanel();
	
    protected JLabel lblCandidates = new JLabel("Candidates : ");
    protected JLabel lblVoters = new JLabel("Voters : ");
    protected JLabel lblLearning = new JLabel("Learning Iterations : ");
    protected JLabel lblRuns = new JLabel("Runs : ");

    protected JPanel pnlCandidates;
    protected JPanel pnlVoters;
    protected JPanel pnlLearning;
    protected JPanel pnlRuns;
    
    protected JPanel pnlOptions;

    protected JPanel pnlRules;
    
    
    //MODEL OBJECTS
    
    protected Thread objWorkingThread;
	protected final VotingSimulator refVotingSimulator;

    
	//CONSTRUCTOR
    
	public SimulatorViewForm(VotingSimulator objSim) {

		
		super("Voting Simulator");

		refVotingSimulator = objSim;

		createComponents();
		designComponents();
		defineControllers();
		
		updateModelFromView();
		
	    
	}


	
	//Instantiate VIEW components
	protected void createComponents() {
		
		pnlTopBar = new JPanel();

		frmBar = new JPanelApplicationButtons(); 
		objSimulationProgressBar = new JProgressBar(0, 100);
		objTaskProgressBar = new JProgressBar(0, 100);
		
		pnlMain = new JTabbedPane();
		
		textArea = new JTextArea();
		outputStream = new CustomOutputStream(textArea);
		printStream = new PrintStream(outputStream);
		
		pnlConsole = new JScrollPane(textArea);

		pnlCharts = new JTabbedPane();
		
		chartCondorcetEfficiency = new XYChart(""); 
		
		chartBordaScore = new XYChart(""); 
		chartBordaNormalizedScore = new XYChart(""); 
		chartBordaRatioToMax = new XYChart(""); 
		chartBordaEfficiency = new XYChart(""); 

		chartUtilitarian = new XYChart(""); 
		chartEgalitarian = new XYChart(""); 

		chartVoterSatisfaction = new XYChart(""); 
		
		
		
		pnlConfig = new JPanel();
		
	    txtCandidates = new JTextField(20);
	    txtVoters = new JTextField(20);
	    txtLearning = new JTextField(20);
	    txtRuns = new JTextField(10);

	    
	    chkRandomRule = new JCheckBox("Random Rule");
	    chkPluralityRule = new JCheckBox("Plurality Rule (One Round)");
	    chkTwoRoundRule = new JCheckBox("Two Rounds (Run Off) Rule");
	    chkSTVRule = new JCheckBox("STV Rule");
	    chkRecVetoRule = new JCheckBox("Recursive Veto Rule");
	    chkBordaRule = new JCheckBox("Borda Rule");
	    chkUSWRule = new JCheckBox("USW Rule");
	    chkESWRule = new JCheckBox("ESW Rule");
	    chkCopelandRule = new JCheckBox("Copeland Rule");
	    chkIterativeBestResponseRule = new JCheckBox("Iterative Best Response Rule");
	    chkIterative3PragmatistRule = new JCheckBox("Iterative 3 Pragmatist Rule");
	    chkIterativeSecondChanceRule = new JCheckBox("Iterative Second Chance Rule");
	    chkIterativeLearningSoftmaxRule = new JCheckBox("Iterative Learning Softmax Adaptive Rule");
	    chkIterativeLearningEGreedyRule = new JCheckBox("Iterative Learning E-Greedy Adaptive Rule");
	    chkIterativeLearningUCBRule = new JCheckBox("Iterative Learning UCB1 Rule");
	    chkIterativeLearningOptimisticRule = new JCheckBox("Iterative Learning Optimistic Rule");

	    
	    pnlParameters = new JPanel();
		
	    lblCandidates = new JLabel("Candidates : ");
	    lblVoters = new JLabel("Voters : ");
	    lblLearning = new JLabel("Learning Iterations : ");
	    lblRuns = new JLabel("Runs : ");

	    
	    pnlCandidates = new JPanel();
	    pnlVoters = new JPanel();
	    pnlLearning = new JPanel();
	    pnlRuns = new JPanel();
	    
	    pnlOptions = new JPanel();
	    pnlRules = new JPanel();
	    
	}
	

	//Design VIEW components
	protected void designComponents() {
	
		
		//FRAME
		
		setLayout(new BorderLayout());

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setPreferredSize(new Dimension(1100, 1000));
		setSize(1100, 1000);

		setLocationRelativeTo(null);	// centers on screen


		//PROGRESS BARS
	    
	    objSimulationProgressBar.setValue(0);
        objSimulationProgressBar.setStringPainted(true);
        objSimulationProgressBar.setBorder(BorderFactory.createTitledBorder("Simulation Progress..."));        

        objTaskProgressBar.setValue(0);
        objTaskProgressBar.setStringPainted(true);
        objTaskProgressBar.setBorder(BorderFactory.createTitledBorder("Scenario Progress..."));        
        
		pnlTopBar.setLayout(new BoxLayout(pnlTopBar, BoxLayout.PAGE_AXIS));
		
		pnlTopBar.add(frmBar);
		pnlTopBar.add(objSimulationProgressBar);
		pnlTopBar.add(objTaskProgressBar);

	    add(pnlTopBar, BorderLayout.NORTH);

	    
		//MAIN PANEL

	    add(pnlMain, BorderLayout.CENTER);
		

		//CHARTS TAB
		
		chartCondorcetEfficiency.setRangeLabel("Condorcet Efficiency");
		chartBordaScore.setRangeLabel("Borda Score of Winner");
		chartBordaNormalizedScore.setRangeLabel("Normalized Borda Score of Winner");
		chartBordaRatioToMax.setRangeLabel("Borda Score Ratio of Winner To Max Borda");
		chartBordaEfficiency.setRangeLabel("Borda Efficiency");
		chartEgalitarian.setRangeLabel("Egalitarian Social Welfare");
		chartUtilitarian.setRangeLabel("Utilitarian Social Welfare");
		chartVoterSatisfaction.setRangeLabel("Voter Satisfaction");
		
		chartCondorcetEfficiency.setDomainLabel("Number");
		chartBordaScore.setDomainLabel("Number");
		chartBordaNormalizedScore.setDomainLabel("Number");
		chartBordaRatioToMax.setDomainLabel("Number");
		chartBordaEfficiency.setDomainLabel("Number");
		chartEgalitarian.setDomainLabel("Number");
		chartUtilitarian.setDomainLabel("Number");
		chartVoterSatisfaction.setDomainLabel("Number");
		
		
	    pnlMain.addTab("Charts", null, pnlCharts, "Charts");
		pnlMain.setMnemonicAt(0, KeyEvent.VK_1);
		
	    pnlCharts.addTab("Condorcet Efficiency", null, chartCondorcetEfficiency, "Condorcet Efficiency");
	    pnlCharts.addTab("Borda Score of Winner", null, chartBordaScore, "Borda Score of Winner");
	    pnlCharts.addTab("Normalized Borda Score of Winner", null, chartBordaNormalizedScore, "Normalized Borda Score of Winner");
	    pnlCharts.addTab("Borda Score Ratio of Winner to Max Borda", null, chartBordaRatioToMax, "Borda Score Ratio of Winner to Max Borda");
	    pnlCharts.addTab("Borda Efficiency", null, chartBordaEfficiency, "Borda Efficiency");
	    pnlCharts.addTab("Utilitarian Social Welfare", null, chartUtilitarian, "Utilitarian Social Welfare");
	    pnlCharts.addTab("Egalitarian Social Welfare", null, chartEgalitarian, "Egalitarian Social Welfare");
	    pnlCharts.addTab("Voter Safisfaction", null, chartVoterSatisfaction, "Voter Safisfaction");
		


		//CONFIG TAB
		
		pnlConfig.setLayout(new BoxLayout(pnlConfig, BoxLayout.PAGE_AXIS));	    

	    pnlParameters.setBorder(BorderFactory.createTitledBorder("Parameters"));	    
	    pnlParameters.setLayout(new BoxLayout(pnlParameters, BoxLayout.PAGE_AXIS));
	    pnlParameters.setAlignmentX( Component.LEFT_ALIGNMENT );		

		//
	    txtCandidates.setText("3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101");
	    txtVoters.setText("7");
	    txtLearning.setText("200"); //0, 100, 200, 500, 1000, 2000");
	    txtRuns.setText("200");
	    
	    /*
	    Dimension dimText = new Dimension(1000, 30);
	    txtCandidates.setMaximumSize(dimText);
	    txtVoters.setMaximumSize(dimText);
	    txtLearning.setMaximumSize(dimText);
	    txtRuns.setMaximumSize(dimText);
	    */
	    
	    lblCandidates.setDisplayedMnemonic(KeyEvent.VK_U);
	    lblCandidates.setLabelFor(txtCandidates);
	    lblVoters.setDisplayedMnemonic(KeyEvent.VK_P);
	    lblVoters.setLabelFor(txtVoters);
	    lblLearning.setDisplayedMnemonic(KeyEvent.VK_U);
	    lblLearning.setLabelFor(txtLearning);
	    lblRuns.setDisplayedMnemonic(KeyEvent.VK_U);
	    lblRuns.setLabelFor(txtRuns);

	    pnlCandidates.setLayout(new FlowLayout(FlowLayout.LEFT));
	    //pnlCandidates.setMaximumSize(dim);
	    pnlCandidates.add(lblCandidates);
	    pnlCandidates.add(txtCandidates);

	    pnlVoters.setLayout(new FlowLayout(FlowLayout.LEFT));	    
	    //pnlVoters.setMaximumSize(dim);
	    pnlVoters.add(lblVoters);
	    pnlVoters.add(txtVoters);

	    pnlLearning.setLayout(new FlowLayout(FlowLayout.LEFT));	    
	    //pnlLearning.setMaximumSize(dim);
	    pnlLearning.add(lblLearning);
	    pnlLearning.add(txtLearning);

	    pnlRuns.setLayout(new FlowLayout(FlowLayout.LEFT));	    
	    //pnlRuns.setMaximumSize(dim);
	    pnlRuns.add(lblRuns);
	    pnlRuns.add(txtRuns);
	    
	    pnlParameters.setMaximumSize(new Dimension(1000, 200));
	    
	    pnlParameters.add(pnlCandidates);
	    pnlParameters.add(pnlVoters);
	    pnlParameters.add(pnlLearning);
	    pnlParameters.add(pnlRuns);

	    pnlConfig.add(pnlParameters);
	    
	    pnlOptions.setBorder(BorderFactory.createTitledBorder("Options"));	    
	    pnlOptions.setLayout(new BoxLayout(pnlOptions, BoxLayout.PAGE_AXIS));	    
	    pnlOptions.setAlignmentX( Component.LEFT_ALIGNMENT );		
	    
	    chkVerbose = new JCheckBox("Verbose mode");
	    chkVerbose.setMnemonic(KeyEvent.VK_V); 
	    chkVerbose.setSelected(false);

	    chkFile = new JCheckBox("Write to output file");
	    chkFile.setMnemonic(KeyEvent.VK_F); 
	    chkFile.setSelected(false);
	    
	    pnlOptions.add(chkVerbose);
	    pnlOptions.add(chkFile);		
		
	    //pnlConfig.add(pnlOptions);		

	    pnlMain.addTab("Config", null, pnlConfig, "Config");
	    pnlMain.setMnemonicAt(1, KeyEvent.VK_2);
	    
		
	    chkRandomRule.setSelected(true);
	    chkPluralityRule.setSelected(true);
	    chkTwoRoundRule.setSelected(true);
	    chkSTVRule.setSelected(true);
	    chkRecVetoRule.setSelected(true);
	    chkBordaRule.setSelected(true);
	    chkUSWRule.setSelected(false);
	    chkESWRule.setSelected(false);
	    chkCopelandRule.setSelected(true);
	    chkIterativeBestResponseRule.setSelected(true);
	    chkIterative3PragmatistRule.setSelected(true);
	    chkIterativeSecondChanceRule.setSelected(false);
	    chkIterativeLearningSoftmaxRule.setSelected(false);
	    chkIterativeLearningEGreedyRule.setSelected(false);
	    chkIterativeLearningUCBRule.setSelected(false);
	    chkIterativeLearningOptimisticRule.setSelected(true);
		
	    
	    pnlRules.setBorder(BorderFactory.createTitledBorder( "Rules"));	    

	    pnlRules.setLayout(new BoxLayout(pnlRules, BoxLayout.PAGE_AXIS));	    
	    
	    
	    pnlRules.add(chkRandomRule);		
	    pnlRules.add(chkPluralityRule);		
	    pnlRules.add(chkTwoRoundRule);		
	    pnlRules.add(chkSTVRule);		
	    pnlRules.add(chkRecVetoRule);		
	    pnlRules.add(chkBordaRule);		
	    //pnlRules.add(chkUSWRule);		
	    //pnlRules.add(chkESWRule);		
	    pnlRules.add(chkCopelandRule);		
	    pnlRules.add(chkIterativeBestResponseRule);		
	    pnlRules.add(chkIterative3PragmatistRule);		
	    pnlRules.add(chkIterativeSecondChanceRule);		
	    pnlRules.add(chkIterativeLearningSoftmaxRule);		
	    pnlRules.add(chkIterativeLearningEGreedyRule);		
	    pnlRules.add(chkIterativeLearningUCBRule);		
	    pnlRules.add(chkIterativeLearningOptimisticRule);		

	    pnlRules.setAlignmentX( Component.LEFT_ALIGNMENT );		
	    
	    pnlConfig.add(pnlRules);		
	    
		chartCondorcetEfficiency.setName("");
		chartBordaScore.setName("");
		chartBordaNormalizedScore.setName("");
		chartBordaRatioToMax.setName("");
		chartBordaEfficiency.setName("");
		chartEgalitarian.setName("");
		chartUtilitarian.setName("");
		chartVoterSatisfaction.setName("");
	    
		chartCondorcetEfficiency.setRangeLabel("Condorcet Efficiency");
		chartBordaScore.setRangeLabel("Borda Score of Winner");
		chartBordaNormalizedScore.setRangeLabel("Normalized Borda Score of Winner");
		chartBordaRatioToMax.setRangeLabel("Borda Score Ratio of Winner to Max Borda");
		chartBordaEfficiency.setRangeLabel("Borda Efficiency");
		chartEgalitarian.setRangeLabel("Egalitarian Social Welfare");
		chartUtilitarian.setRangeLabel("Utilitarian Social Welfare");
		chartVoterSatisfaction.setRangeLabel("Voter Satisfaction");

		
	    //CONSOLE TAB
		
		pnlConsole.setPreferredSize(new Dimension(1200, 400));

		textArea.setBackground(Color.BLACK);
		textArea.setFont(new Font("Courier New", Font.PLAIN, 12));
		textArea.setForeground(Color.WHITE);
		textArea.setEditable(false);
		textArea.setEnabled(true);
		textArea.setVisible(true);
		
		pnlMain.addTab("Console", null, pnlConsole, "Console");
		pnlMain.setMnemonicAt(2, KeyEvent.VK_3);
		
	}
	
	
	//Associate event handlers
	protected void defineControllers() {
		
		frmBar.addListener(new ApplicationButtonsListener() {
			
			@Override
			public void clickReset() {
				
				clearView();
				
				refreshView();

			}
			
			@Override
			public void clickPlay() {

				clearView();
				
				refreshView();
				
				updateModelFromView();
				
				clearView();
				configureViewFromModel();
				refreshView();
				
				
				//Create the working thread and start simulations on
		    	if (objWorkingThread == null) {
					objWorkingThread = new Thread(refVotingSimulator); 
		    		objWorkingThread.start();
		    		objWorkingThread = null;
		        }
			
		    	
			}
			
		});
		
		
		//LISTENER FOR THE SIMULATOR 
		refVotingSimulator.addPropertyChangeListener(new PropertyChangeListener() {
			
			public void propertyChange(PropertyChangeEvent evt) {

				//PROGRESS BAR
				if (evt.getPropertyName().equalsIgnoreCase("progress")) {

					objSimulationProgressBar.setValue((int)evt.getNewValue());
					
				//NEW DATA POINTS
				} else if (evt.getPropertyName().equalsIgnoreCase("data")) {
				
					ElectionSeriesData data = (ElectionSeriesData) evt.getNewValue();
					
					double dReference = (Integer) evt.getOldValue();
							
					for (int i=0; i<data.iNumElectionRules; i++) {
	
						//Election e = listElectionRules.get(i);
						
						chartCondorcetEfficiency.addPoint(i, dReference, data.getCondorcetEfficiency(i));
						chartBordaScore.addPoint(i, dReference, data.getAverageBordaScore(i));
						chartBordaNormalizedScore.addPoint(i, dReference, data.getNormalizedBordaScore(i));
						chartBordaRatioToMax.addPoint(i, dReference, data.getRatioBordaScore(i));
						chartBordaEfficiency.addPoint(i, dReference, data.getBordaEfficiency(i));
						chartEgalitarian.addPoint(i, dReference, data.getEgalitarianEfficiency(i));
						chartUtilitarian.addPoint(i, dReference, data.getUtilitarianEfficiency(i));
						
					}

				    refreshView();
				    
				//NEW TEXT OUTPUT
				} else if (evt.getPropertyName().equalsIgnoreCase("console")) {
					
					String output = (String) evt.getNewValue();
					
					textArea.append(output);
					
				}

			}
		});
		
	};

	
	
	
	//refresh view (data on buffers...)
	public void refreshView() {

		//outputStream.refresh();
		
		chartCondorcetEfficiency.refresh();
		chartBordaScore.refresh();
		chartBordaNormalizedScore.refresh();
		chartBordaRatioToMax.refresh();
		chartBordaEfficiency.refresh();
		chartEgalitarian.refresh();
		chartUtilitarian.refresh();
		chartVoterSatisfaction.refresh();
		
	}
    
	
	//clear view
	public void clearView() {

		//clean console
		textArea.setText("");

		//remove all series from all charts
		chartCondorcetEfficiency.clear();
		chartBordaScore.clear();
		chartBordaNormalizedScore.clear();
		chartBordaRatioToMax.clear();
		chartBordaEfficiency.clear();
		chartEgalitarian.clear();
		chartUtilitarian.clear();
		chartVoterSatisfaction.clear();
		
	}

	
	//configure view using model parameters
	public void configureViewFromModel() {
		
		//add series on charts
		for (Election e : refVotingSimulator.getElectionRules()) {
	
			chartCondorcetEfficiency.addSerie(e.getName());
			chartBordaScore.addSerie(e.getName());
			chartBordaNormalizedScore.addSerie(e.getName());
			chartBordaRatioToMax.addSerie(e.getName());
			chartBordaEfficiency.addSerie(e.getName());
			chartUtilitarian.addSerie(e.getName());
			chartEgalitarian.addSerie(e.getName());
			
		}

		String sDomainLabel = "";
		
		//if simulator is varying candidates
		if (refVotingSimulator.isVaryingCandidates()) { 
			
			objSimulationProgressBar.setMaximum(refVotingSimulator.getCandidatesSetSize());

			sDomainLabel = "Number of Candidates";

			//number of voters
			for (int i=0; i<refVotingSimulator.getNumVoters(); i++ ) {
				chartVoterSatisfaction.addSerie(String.valueOf(i));
			}
			
		//if simulator is varying voters
		} else if (refVotingSimulator.isVaryingVoters()) {
			
			objSimulationProgressBar.setMaximum(refVotingSimulator.getVotersSetSize());

			sDomainLabel = "Number of Voters";

			for (int i=0; i<refVotingSimulator.getMaxVoters(); i++ ) {
				chartVoterSatisfaction.addSerie(String.valueOf(i));
			}
			
		//varying number of iterations
		} else {

			
			objSimulationProgressBar.setMaximum(refVotingSimulator.getIterationsSetSize());
			
			sDomainLabel = "Number of Iterations";

			for (int i=0; i<refVotingSimulator.getNumVoters(); i++ ) {
				chartVoterSatisfaction.addSerie(String.valueOf(i));
			}
		}

		chartCondorcetEfficiency.setDomainLabel(sDomainLabel);
		chartBordaScore.setDomainLabel(sDomainLabel);
		chartBordaNormalizedScore.setDomainLabel(sDomainLabel);
		chartBordaRatioToMax.setDomainLabel(sDomainLabel);
		chartBordaEfficiency.setDomainLabel(sDomainLabel);
		chartEgalitarian.setDomainLabel(sDomainLabel);
		chartUtilitarian.setDomainLabel(sDomainLabel);
		chartVoterSatisfaction.setDomainLabel(sDomainLabel);
		
	}

	
	//set the parameters defined on view to the model
	public void updateModelFromView() {

		
		//get num runs
		
	    int iNumRuns;

	    try {
	    	iNumRuns = Integer.parseInt(txtRuns.getText().trim());
	    } catch (NumberFormatException nfe) {
	    	iNumRuns = 1000;
	    	System.err.println("Error parsing parameter NumRuns. Set to 1000.");
	    }
		
	    refVotingSimulator.setNumRuns(iNumRuns);
				
	    
	    //parse candidates, voters, iterations
	    
		String[] strCandidates;
		String[] strVoters;
		String[] strLearning;
		
		strCandidates = txtCandidates.getText().split(",");
		strVoters = txtVoters.getText().split(",");
		strLearning = txtLearning.getText().split(",");
		
		int[] aiCandidates;
		int[] aiVoters;
		int[] aiLearning;
		
		aiCandidates = new int[strCandidates.length];
		aiVoters = new int[strVoters.length];
		aiLearning = new int[strLearning.length];

		for (int i=0; i<strCandidates.length; i++) {
		    try {
		    	aiCandidates[i] = Integer.parseInt(strCandidates[i].trim());
		    } catch (NumberFormatException nfe) {
		    	aiCandidates[i] = 1;
		    	System.err.println("Error parsing parameter.");
		    }
		}		

		for (int i=0; i<strVoters.length; i++) {
		    try {
		    	aiVoters[i] = Integer.parseInt(strVoters[i].trim());
		    } catch (NumberFormatException nfe) {
		    	aiVoters[i] = 1;
		    	System.err.println("Error parsing parameter.");
		    }
		}		
		
		for (int i=0; i<strLearning.length; i++) {
		    try {
		    	aiLearning[i] = Integer.parseInt(strLearning[i].trim());
		    } catch (NumberFormatException nfe) {
		    	aiLearning[i] = 1;
		    	System.err.println("Error parsing parameter.");
		    }
		}		
		
	    refVotingSimulator.setParams(aiCandidates, aiVoters, aiLearning);
	
		objSimulationProgressBar.setMinimum(0);
		objSimulationProgressBar.setValue(0);
		
		//rules
		refVotingSimulator.bRandomRule = chkRandomRule.isSelected();
		refVotingSimulator.bPluralityRule = chkPluralityRule.isSelected();
		refVotingSimulator.bCopelandRule = chkCopelandRule.isSelected();
		refVotingSimulator.bBordaRule = chkBordaRule.isSelected();
		refVotingSimulator.bSTVRule = chkSTVRule.isSelected();
		refVotingSimulator.bRecVetoRule = chkRecVetoRule.isSelected();
		refVotingSimulator.bIterativeSecondChanceRule = chkIterativeSecondChanceRule.isSelected();
		refVotingSimulator.bIterative3PragmatistRule = chkIterative3PragmatistRule.isSelected();
		refVotingSimulator.bIterativeBestResponseRule = chkIterativeBestResponseRule.isSelected();
		refVotingSimulator.bTwoRoundRule = chkTwoRoundRule.isSelected();
		refVotingSimulator.bIterativeLearningUCBRule = chkIterativeLearningUCBRule.isSelected();
		refVotingSimulator.bIterativeLearningEGreedyRule = chkIterativeLearningEGreedyRule.isSelected();
		refVotingSimulator.bIterativeLearningSoftmaxRule = chkIterativeLearningSoftmaxRule.isSelected();
		refVotingSimulator.bIterativeLearningOptimisticRule = chkIterativeLearningOptimisticRule.isSelected();
		
		//chartCondorcet.setRangeLabel("Condorcet Efficiency (" + nc + " candidates, " + ni + " iterations, " + iNumRuns +" runs)");
		//chartBorda.setRangeLabel("Borda Score Ratio of Winner (" + nc + " candidates, " + ni + " iterations, " + iNumRuns +" runs)");
		//chartEgalitarian.setRangeLabel("Egalitarian Social Welfare (" + nc + " candidates, " + ni + " iterations, " + iNumRuns +" runs)");
		//chartUtilitarian.setRangeLabel("Utilitarian Social Welfare (" + nc + " candidates, " + ni + " iterations, " + iNumRuns +" runs)");

		refVotingSimulator.bVerboseMode = chkVerbose.isSelected();
		refVotingSimulator.bWriteOutputFile = chkFile.isSelected();
		
		refVotingSimulator.initialize();
		
	}
	
	
	
	
	



    
	
	//-------------------------------------------------------------------------------------
	


}