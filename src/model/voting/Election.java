package model.voting;


import java.io.File;
import java.io.FileWriter;
import java.util.Random;



public abstract class Election {

	
	//name of the rule
	protected String strName = "Election Rule";
	
	
	
	//print console details
	public boolean verbose_mode = false;
	
	//print file details
	public boolean output_file = false;
	

	//scenario
	protected Scenario objScenario = null;

	
	//tie break mode
	protected EnumTieBreakType iTieBreakMode = EnumTieBreakType.lexic;
	
	
	
	//random rnd
	protected static final Random rnd = new Random();
	

	//output file
	File outputFile;
	FileWriter out;
	
	
	//ELECTION RESULT (after RUN)
	public ResultList result;
	

	
	
	
	//CONSTRUCTOR
	public Election(Scenario objScenario, EnumTieBreakType iTieBreakMode) {

		this.objScenario = objScenario;
		
		this.iTieBreakMode = iTieBreakMode;
		
		setDefaultName();
		
	}

	
	
	//CONSTRUCTOR
	public Election(EnumTieBreakType iTieBreakMode) {

		this(null, iTieBreakMode);
		
	}
	

	
	//CONSTRUCTOR
	public Election() {

		this(null, EnumTieBreakType.lexic);
		
	}
	
	
	
	
	
	
	
	public boolean isScenarioDefined() {
		return (objScenario != null);
	}

	
	public void setScenario(Scenario s) {
		objScenario = s;
	}

	
	public void setTieBreakMode(EnumTieBreakType iTieBreakMode) {
		this.iTieBreakMode = iTieBreakMode;
	}
	
	
	
	
	
	//RUN ELECTION
	public abstract ResultList run();
	
	
	
	
	public VoterList getVoters() {
		if (isScenarioDefined()) {
			return objScenario.listVoters;
		} else {
			return null;
		}
	}

	public CandidateList getCandidates() {
		if (isScenarioDefined()) {
			return objScenario.listCandidates;
		} else {
			return null;
		}
	}

	public int getNumVoters() {
		if (isScenarioDefined()) {
			return objScenario.iNumVoters;
		} else {
			return 0;
		}
	}

	public int getNumCandidates() {
		if (isScenarioDefined()) {
			return objScenario.iNumCandidates;
		} else {
			return 0;
		}
	}

	
	
	
	
	public void setDefaultName() {
		strName = "Election";
	}

	public final void setName(String strNewName) {
		strName = strNewName;
	}

	public final String getName() {
		return strName;
	}
	
	
}
