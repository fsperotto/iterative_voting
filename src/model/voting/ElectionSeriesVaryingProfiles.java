package model.voting;


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;



public class ElectionSeriesVaryingProfiles {


	//NUMBER OF RUNS IN A SERIES
    //------------------------------------------

	//number of profiles (simulations)
	protected final int iNumRuns;

	
	//CONSTANT PARAMETERS ON A SERIES (FOR ALL RUNS)
    //------------------------------------------
	
	//candidates
	protected final CandidateList listCandidates;

	//number of candidates
	protected final int iNumCandidates;
	
	//number of voters
	protected final int iNumVoters;
	
	//election rules
	protected final List<Election> listElectionRules;

	//number of rules
	protected final int iNumElectionRules;
	
	//voters preference type
	public EnumUtilityGenerationMode eUtilityGenerationMode = EnumUtilityGenerationMode.RandomExponential;

	//tie break
	public EnumTieBreakType iTieBreakMode = EnumTieBreakType.lexic;
	
	
	//PARAMETERS VARYING AT EACH RUN ON A SERIES
    //------------------------------------------
	
	//scenario
	protected final Scenario scenario;

	//voters
	protected VoterList listVoters = null;
	

	// SYNTHESIS OF THE SERIES (AFTER ALL RUNS)
    //------------------------------------------

	//results
	ElectionSeriesData data;

	
	//SUPPORT OBJECTS
    //------------------------------------------

	//interface for property listeners
	PropertyChangeSupport objPropertyChangeSupport;


	
	
	//CONSTRUCTOR
	public ElectionSeriesVaryingProfiles(List<Election> listElectionRules, CandidateList listCandidates, int iNumVoters, int iNumRuns) {
		
    	//interface for property listeners
		objPropertyChangeSupport = new PropertyChangeSupport(this);

		this.listCandidates = listCandidates;
		this.iNumCandidates = listCandidates.size();

		this.iNumVoters = iNumVoters;

		this.iNumRuns = iNumRuns;

		this.listElectionRules = listElectionRules;
		this.iNumElectionRules = listElectionRules.size();

		scenario = new Scenario(listCandidates);
		
		data = new ElectionSeriesData(iNumRuns, iNumVoters, iNumRuns, /*iNumIterations,*/ iNumElectionRules);

	}
	

	//PROPERTY CHANGE 
	
	public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
		objPropertyChangeSupport.addPropertyChangeListener(listener);
	}

	public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
		objPropertyChangeSupport.removePropertyChangeListener(listener);
	}	
	

	
	

/*	
	public void setProgressBar(JProgressBar objProgressBar) {

		if (objProgressBar != null) {
			
			objProgressBar.setMinimum(0);
			objProgressBar.setMaximum(iNumRuns-1);
		
			objProgressBar.setValue(0);
			
			this.objProgressBar = objProgressBar;
		}
		
	}
	
	private void refreshProgressBar(int iRun) {
		
		if (objProgressBar != null) {
			objProgressBar.setValue(iRun);
		}
		
	}
*/	
	
	
	
	
	//RUN SIMULATION
	public void run() {

		
		System.out.println("Running : " + iNumCandidates + " candidates, " + iNumVoters + " voters, " + iNumRuns + " runs.");

		
		
		scenario.setCandidates(listCandidates);
		
		
		
		
		for ( int iRun=0; iRun<iNumRuns; iRun++ ) {
		
			
			switch (eUtilityGenerationMode) {

				case UrnModelLinear:
	
					listVoters = PreferenceGenerator_UrnModel.doDefinePreferences(listCandidates, iNumVoters, 0.1, EnumUtilityGenerationMode.RandomLinear);
					
					break;
	
				case UrnModelExponential:
	
					listVoters = PreferenceGenerator_UrnModel.doDefinePreferences(listCandidates, iNumVoters, 0.1, EnumUtilityGenerationMode.RandomExponential);
					
					break;

				case UrnModelSigmoid:
					
					listVoters = PreferenceGenerator_UrnModel.doDefinePreferences(listCandidates, iNumVoters, 0.1, EnumUtilityGenerationMode.RandomSigmoid);
					
					break;
					
				case RandomLinear:
				case RandomExponential:
				case RandomSigmoid:
				case GivenOrderLinear:
				case GivenOrderExponential:
				case None:
	
					listVoters = new VoterList(iNumVoters);
					
					for (int i=0; i<iNumVoters; i++) {
						listVoters.add(new Voter(listCandidates, eUtilityGenerationMode));
					}
	
					break;
	
				default:
					
					break;
				
			}

			
			scenario.setVoters(listVoters);
			
			scenario.reset();
				
			
			data.agg_condorcet_existence +=  ( (scenario.candCondorcetWinner == null) ? 0 : 1 );
		

			ResultList[] result;
			CandidateList[] winners;
			Candidate[] winner;

			result = new ResultList[listElectionRules.size()];
			winners = new CandidateList[listElectionRules.size()];
			winner = new Candidate[listElectionRules.size()];
			
			
			for (int i=0; i<listElectionRules.size(); i++ ) {

				
				Election e = listElectionRules.get(i); 
				
				e.setScenario(scenario);
				
				
				//DECISION
				result[i] = e.run();
				winners[i] = result[i].get(0);
				winner[i] = winners[i].getTieBreakPreferred(iTieBreakMode);

				
			}

			//AGREGATE RESULTS
			data.agregate(winner, scenario);

			
			/*
			//OPEN OUTPUT CSV FILE
			if (output_file) {
				outputFile = new File("result.csv");
				try {
					out = new FileWriter(outputFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			//CLOSE OUTPUT CSV FILE
			if (output_file) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			*/
			
			//trigger progress event
			objPropertyChangeSupport.firePropertyChange("task_progress", null, iRun);
			
			
		}
		
	}
	
	
	@Override
	public String toString() { 
		
		String output = "";
		
		output += "~~~~~~~~~~~~~~~~~~~~~\n\n";

		output += "#Candidates                    " + iNumCandidates +"\n";
		output += "#Voters                        " + iNumVoters +"\n";
		output += "\n";
		output += "#Simulations (profiles)        " + iNumRuns +"\n";

		output += "\n~~~~~~~~~~~~~~~~~~~~~\n\n";

		output += "Rate of Condorcet winners                                   " + (1.0*data.agg_condorcet_existence ) / iNumRuns +"\n";
		output += "\n";
		
		for (int i=0; i<listElectionRules.size(); i++) {
			Election e = listElectionRules.get(i);
			output += "Condorcet efficiency for " + e.strName + " voting : " + (1.0*data.agg_condorcet_winner[i]) / data.agg_condorcet_existence +"\n";
		}

		output += "\n~~~~~~~~~~~~~~~~~~~~~\n\n";
		
		for (int i=0; i<listElectionRules.size(); i++) {
			Election e = listElectionRules.get(i);
			output += "Average Borda score of " + e.strName + " winners : " + (1.0*data.agg_borda_score[i])  / (iNumRuns) +"\n";
			output += "Average Borda ratio of " + e.strName + " winners : " + (1.0*data.agg_borda_score[i])  / (iNumVoters*iNumRuns) +"\n";
			output += "Borda efficiency for " + e.strName + " voting : " + (1.0*data.agg_borda_winner[i])  / (iNumRuns) +"\n";
		}
		
		output += "\n~~~~~~~~~~~~~~~~~~~~~\n\n";

		for (int i=0; i<listElectionRules.size(); i++) {
			Election e = listElectionRules.get(i);
			output += "Average USW of " + e.strName + " winners : " + (1.0*data.agg_usw_score[i])  / (iNumRuns) +"\n";
			output += "USW efficiency for " + e.strName + " voting : " + (1.0*data.agg_usw_winner[i])  / (iNumRuns) +"\n";
		}
		
		output += "\n~~~~~~~~~~~~~~~~~~~~~\n\n";
		
		return output;
		
	}


	public Object getData() {
		return data;
	}


	
	
	
	//-------------------------------------------------------------------------------------
	
		
	
	
	
}
