package model.voting;


import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JProgressBar;

import view.components.XYChart;



public class ElectionSeriesVaryingIterations {



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
	
	//number of iterations for learning
	protected final List<Integer> listNumIterations;

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
	protected ElectionSeriesData data[];

	
	//SUPPORT OBJECTS
    //------------------------------------------

	//interface for property listeners
	protected PropertyChangeSupport objPropertyChangeSupport;


	/*
	//print console details
	public boolean verbose_mode = false;
	
	//print file details
	public boolean output_file = false;
	
	//output file
	File outputFile;
	FileWriter out;

	//progress bar
	protected JProgressBar objTaskProgressBar = null;
	
	//charts
	protected XYChart chartCondorcet = null;
	protected XYChart chartBordaScore = null; 
	protected XYChart chartBordaNormalizedScore = null; 
	protected XYChart chartBordaRatioToMax = null; 
	protected XYChart chartBordaEfficiency = null;
	protected XYChart chartEgalitarian = null;
	protected XYChart chartUtilitarian = null;
	protected XYChart chartVoterSatisfaction = null;
	
	//console
	protected PrintStream outputStream;	
	*/
	

	//results
	int agg_condorcet_winner[][];
	
	double agg_copeland_score[][];
	int agg_copeland_winner[][];
	
	double agg_borda_score[][];
	double agg_borda_normscore[][];
	double agg_borda_ratio[][];
	int agg_borda_winner[][];
	
	double agg_usw_score[][];
	int agg_usw_winner[][];
	
	double agg_esw_score[][];
	int agg_esw_winner[][];

	//int agg_max_borda_score = 0;
	int agg_condorcet_existence = 0;

	double agg_voter_satisfaction[][][];
	
	
	
	
	//CONSTRUCTOR
	public ElectionSeriesVaryingIterations(List<Election> listElectionRules, CandidateList listCandidates, int iNumVoters, int iNumRuns, List<Integer> listNumIterations) {

    	//interface for property listeners
		objPropertyChangeSupport = new PropertyChangeSupport(this);

		this.listCandidates = listCandidates;
		this.iNumCandidates = listCandidates.size();

		this.iNumVoters = iNumVoters;

		this.iNumRuns = iNumRuns;

		this.listElectionRules = listElectionRules;
		this.iNumElectionRules = listElectionRules.size();
		

		scenario = new Scenario(listCandidates);
		
		
		this.listNumIterations = listNumIterations; 
		
		
	}
	
	
	/*
	
	public void setProgressBar(JProgressBar objTaskProgressBar) {

		if (objTaskProgressBar != null) {
			
			objTaskProgressBar.setMinimum(0);
			objTaskProgressBar.setMaximum(iNumRuns-1);
		
			objTaskProgressBar.setValue(0);
			
			this.objTaskProgressBar = objTaskProgressBar;
		}
		
	}
	
	
	*/
	
	
	//RUN SIMULATION
	public void run() {

		
		

		System.out.println("Running : " + iNumCandidates + " candidates, " + iNumVoters + " voters, " + iNumRuns + " runs.");

		
		
		//election result (order)

		//ResultList[][] result = new ResultList[listElectionRules.size()][listNumIterations.size()];
		//CandidateList winners[][] = new CandidateList[listElectionRules.size()][listNumIterations.size()];
		//Candidate winner[][] = new Candidate[listElectionRules.size()][listNumIterations.size()];
		
		//ResultList[] result = new ResultList[listElectionRules.size()];
		//CandidateList winners[] = new CandidateList[listElectionRules.size()];
		//Candidate winner[] = new Candidate[listElectionRules.size()];

		ResultList result;
		CandidateList winners;
		Candidate winner;

		//data = new ElectionSeriesData[listNumIterations.size()];
		
		/*
		agg_condorcet_winner = new int[listElectionRules.size()][listNumIterations.size()];

		//agg_copeland_score = new double[listElectionRules.size()];
		//agg_copeland_winner = new int[listElectionRules.size()];

		//agg_borda_max_score = new int[listElectionRules.size()];
		agg_borda_score = new double[listElectionRules.size()][listNumIterations.size()];
		agg_borda_normscore = new double[listElectionRules.size()][listNumIterations.size()];
		agg_borda_ratio = new double[listElectionRules.size()][listNumIterations.size()];
		agg_borda_winner = new int[listElectionRules.size()][listNumIterations.size()];

		//agg_usw_max_score = new double[listElectionRules.size()];
		agg_usw_score = new double[listElectionRules.size()][listNumIterations.size()];
		agg_usw_winner = new int[listElectionRules.size()][listNumIterations.size()];

		//agg_esw_max_score = new double[listElectionRules.size()];
		agg_esw_score = new double[listElectionRules.size()][listNumIterations.size()];
		agg_esw_winner = new int[listElectionRules.size()][listNumIterations.size()];

		*/
		
		agg_voter_satisfaction = new double[listElectionRules.size()][listNumIterations.size()][iNumVoters];
		

		for (int iElectionRule=0; iElectionRule<listElectionRules.size(); iElectionRule++ ) {
			for (int iNumIterationsCase=0; iNumIterationsCase<listNumIterations.size(); iNumIterationsCase++ ) {

				/*
				data[iNumIterationsCase].agg_condorcet_winner[iElectionRule] = 0;
	
				data[iNumIterationsCase].agg_borda_score[iElectionRule] = 0;
				data[iNumIterationsCase].agg_borda_normscore[iElectionRule] = 0;
				data[iNumIterationsCase].agg_borda_ratio[iElectionRule] = 0;
				data[iNumIterationsCase].agg_borda_winner[iElectionRule] = 0;
	
				data[iNumIterationsCase].agg_usw_score[iElectionRule] = 0;
				data[iNumIterationsCase].agg_usw_winner[iElectionRule] = 0;
	
				data[iNumIterationsCase].agg_esw_score[iElectionRule] = 0;
				data[iNumIterationsCase].agg_esw_winner[iElectionRule] = 0;
   			    */
				
				for (int iVoter=0; iVoter<iNumVoters; iVoter++ ) {
					agg_voter_satisfaction[iElectionRule][iNumIterationsCase][iVoter] = 0;
				}
				
			}
		}
		
		agg_condorcet_existence = 0;
		
		/*
		scenario.setCandidates(listCandidates);
		*/
		
		/*
		for (Election e : listElectionRules) {
			e.setCandidates(listCandidates);
		}
		*/
		
		
		for ( int iRun=0; iRun<iNumRuns; iRun++ ) {
		
			
			//trigger progress event
			objPropertyChangeSupport.firePropertyChange("task_progress", null, iRun);

			
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
				
			
			agg_condorcet_existence +=  ( (scenario.candCondorcetWinner == null) ? 0 : 1 );

			
			//NON ITERATIVE LEARNING RULES
			for (int iElectionRule=0; iElectionRule<listElectionRules.size(); iElectionRule++ ) {

				
				Election e = listElectionRules.get(iElectionRule); 

				if (!(e instanceof Election_IterativeLearning)) {
				
					e.setScenario(scenario);
					
					
					//DECISION
					result = e.run();
					winners = result.get(0);
					winner = winners.getTieBreakPreferred(iTieBreakMode);

					
					//AGREGATE RESULTS
					
					//agg_copeland_score[i];
					//agg_copeland_winner[i];

					
					//USW SCORE
					agg_usw_score[iElectionRule][0] += scenario.adUSW[winner.id] ;   //USW score of winner
					
					/*
					//USW SCORE RATE
					if (scenario.dMaxUSW > 0.0) {
						agg_usw_score[iElectionRule][0] += scenario.adUSW[winner.id] / scenario.dMaxUSW;   //USW score rate of winner
					} else {
						agg_usw_score[iElectionRule][0] += 1.0;   //USW score rate of winner
					}
					*/
					
					//agg_usw_max_score[i] += scenario.dMaxUSW;   //USW score of max

					agg_usw_winner[iElectionRule][0] += ( (scenario.listUSWwinners.contains(winner)) ? 1 : 0 );   //Number of times a USW winner is elected
					

					//ESW SCORE
					agg_esw_score[iElectionRule][0] += scenario.adESW[winner.id] ;   //ESW score rate of winner
					
					//ESW SCORE RATE
					/*
					if (scenario.dMaxESW > 0.0) {
						agg_esw_score[i] += scenario.adESW[winner[i].id] / scenario.dMaxESW;   //ESW score rate of winner
					} else {
						agg_esw_score[iElectionRule][0] += 1.0;   //ESW score rate of winner
					}
					*/
					//agg_esw_max_score[i] += scenario.dMaxESW;   //ESW score of max

					//ESW EFFICIENCY
					agg_esw_winner[iElectionRule][0] += ( (scenario.listESWwinners.contains(winner)) ? 1 : 0 );   //Number of times a ESW winner is elected
					
					
					//BORDA
					
					//agg_borda_max_score[i] += scenario.iMaxBordaScore;   //Borda score of max
					agg_borda_score[iElectionRule][0] += scenario.aiBordaScore[winner.id];   //Borda score of winner
					agg_borda_normscore[iElectionRule][0] += (1.0*scenario.aiBordaScore[winner.id]) / (iNumVoters * (iNumCandidates-1));   //normalized Borda score of winner
					agg_borda_ratio[iElectionRule][0] += (1.0*scenario.aiBordaScore[winner.id]) / scenario.iMaxBordaScore;   //Borda score ratio of winner
					agg_borda_winner[iElectionRule][0] += ( (scenario.listBordaWinners.contains(winner)) ? 1 : 0 );   //Number of times a Borda winner is elected
					
					
					//CONDORCET
					
					agg_condorcet_winner[iElectionRule][0] += ( (scenario.candCondorcetWinner == winner) ? 1 : 0 );   //Number of times a Condorcet winner is elected

					
					//SATISFACTION
					
					for (int iVoter=0; iVoter<iNumVoters; iVoter++ ) {
						Voter v = listVoters.get(iVoter);
						agg_voter_satisfaction[iElectionRule][0][iVoter] += v.getValue(winner);
					}
					
					
				}
					
			}

			
			//ITERATIVE LEARNING RULES
			for (int iElectionRule=0; iElectionRule<listElectionRules.size(); iElectionRule++ ) {

				Election e = listElectionRules.get(iElectionRule); 

				if (e instanceof Election_IterativeLearning) {

					e.setScenario(scenario);
					
					
					for (int iNumIterationsCase=0; iNumIterationsCase<listNumIterations.size(); iNumIterationsCase++ ) {
						
						int iNumIterations = listNumIterations.get(iNumIterationsCase);
						
						((Election_IterativeLearning)(e)).setNumIterations(iNumIterations);
						
						//DECISION
						result = e.run();
						winners = result.get(0);
						winner = winners.getTieBreakPreferred(iTieBreakMode);

						
						//AGREGATE RESULTS
						
						//agg_copeland_score[i];
						//agg_copeland_winner[i];

						
						//USW SCORE
						agg_usw_score[iElectionRule][iNumIterationsCase] += scenario.adUSW[winner.id] ;   //USW score of winner
						
						//USW SCORE RATE
						/*
						if (scenario.dMaxUSW > 0.0) {
							agg_usw_score[iElectionRule][iNumIterationsCase] += scenario.adUSW[winner.id] / scenario.dMaxUSW;   //USW score rate of winner
						} else {
							agg_usw_score[iElectionRule][iNumIterationsCase] += 1.0;   //USW score rate of winner
						}
						*/
						//agg_usw_max_score[i] += scenario.dMaxUSW;   //USW score of max

						agg_usw_winner[iElectionRule][iNumIterationsCase] += ( (scenario.listUSWwinners.contains(winner)) ? 1 : 0 );   //Number of times a USW winner is elected
						
						
						//ESW SCORE 
						agg_esw_score[iElectionRule][iNumIterationsCase] += scenario.adESW[winner.id] ;   //ESW score rate of winner

						//ESW SCORE RATE
						/*
						if (scenario.dMaxESW > 0.0) {
							agg_esw_score[i] += scenario.adESW[winner[i].id] / scenario.dMaxESW;   //ESW score rate of winner
						} else {
							agg_esw_score[iElectionRule][iNumIterationsCase] += 1.0;   //ESW score rate of winner
						}
						*/
						//agg_esw_max_score[i] += scenario.dMaxESW;   //ESW score of max

						//ESW EFFICIENCY
						agg_esw_winner[iElectionRule][iNumIterationsCase] += ( (scenario.listESWwinners.contains(winner)) ? 1 : 0 );   //Number of times a ESW winner is elected
						
						
						//BORDA
						
						//agg_borda_max_score[i] += scenario.iMaxBordaScore;   //Borda score of max
						agg_borda_score[iElectionRule][iNumIterationsCase] += scenario.aiBordaScore[winner.id];   //Borda score of winner
						agg_borda_normscore[iElectionRule][iNumIterationsCase] += (1.0*scenario.aiBordaScore[winner.id]) / (iNumVoters * (iNumCandidates-1));   //Borda score of winner
						agg_borda_ratio[iElectionRule][iNumIterationsCase] += (1.0*scenario.aiBordaScore[winner.id]) / scenario.iMaxBordaScore;   //Borda score retio of winner
						agg_borda_winner[iElectionRule][iNumIterationsCase] += ( (scenario.listBordaWinners.contains(winner)) ? 1 : 0 );   //Number of times a Borda winner is elected
						
						
						//CONDORCET
						
						agg_condorcet_winner[iElectionRule][iNumIterationsCase] += ( (scenario.candCondorcetWinner == winner) ? 1 : 0 );   //Number of times a Condorcet winner is elected
						

						//SATISFACTION
						
						for (int iVoter=0; iVoter<iNumVoters; iVoter++ ) {
							Voter v = listVoters.get(iVoter);
							agg_voter_satisfaction[iElectionRule][iNumIterationsCase][iVoter] += v.getValue(winner);
						}
						
					}
					
					
				}
				
					
					
			}
			
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
			
		}
		
		
	}
	
	
	
	@Override
	public String toString() { 
		
		String output = "";
		
		output += "~~~~~~~~~~~~~~~~~~~~~\n\n";
		output += "#Candidates                    " + iNumCandidates + "\n";
		output += "#Voters                        " + iNumVoters  + "\n\n";
		output += "#Simulations (profiles)        " + iNumRuns  + "\n\n";
		output += "~~~~~~~~~~~~~~~~~~~~~\n\n";

		for (int iElectionRule=0; iElectionRule<listElectionRules.size(); iElectionRule++ ) {
			
			Election e = listElectionRules.get(iElectionRule);

			//ITERATIVE LEARNING
			if (e instanceof Election_IterativeLearning) {
				
				for (int iNumIterationsCase=0; iNumIterationsCase<listNumIterations.size(); iNumIterationsCase++ ) {
				
					output += "Average Borda score of " + e.strName + " winners : " + (1.0*agg_borda_score[iElectionRule][iNumIterationsCase])  / (iNumRuns) + "\n";
					
				}
			
			//OTHER RULES
			} else {
				
				output += "Average Borda score of " + e.strName + " winners : " + (1.0*agg_borda_score[iElectionRule][0])  / (iNumRuns) + "\n";

			}
			
		}
		
		
		output += "\n~~~~~~~~~~~~~~~~~~~~~\n\n";

		
		for (int iElectionRule=0; iElectionRule<listElectionRules.size(); iElectionRule++ ) {
			
			Election e = listElectionRules.get(iElectionRule);
			
			//ITERATIVE LEARNING
			if (e instanceof Election_IterativeLearning) {
				
				for (int iNumIterationsCase=0; iNumIterationsCase<listNumIterations.size(); iNumIterationsCase++ ) {
				
					output += "Borda efficiency for " + e.strName + " voting : " + (1.0*agg_borda_winner[iElectionRule][iNumIterationsCase])  / (iNumRuns) + "\n";
					
				}
			
			//OTHER RULES
			} else {
				
				output += "Borda efficiency for " + e.strName + " voting : " + (1.0*agg_borda_winner[iElectionRule][0])  / (iNumRuns) + "\n";

			}
			
		}
		
		
		output += "\n~~~~~~~~~~~~~~~~~~~~~\n\n";
		
		output += "Rate of Condorcet winners                                   " + (1.0*agg_condorcet_existence ) / iNumRuns+ "\n\n";
		
		
		for (int iElectionRule=0; iElectionRule<listElectionRules.size(); iElectionRule++ ) {
			
			Election e = listElectionRules.get(iElectionRule);
			
			//ITERATIVE LEARNING
			if (e instanceof Election_IterativeLearning) {
				
				for (int iNumIterationsCase=0; iNumIterationsCase<listNumIterations.size(); iNumIterationsCase++ ) {
				
					output += "Condorcet efficiency for " + e.strName + " voting : " + (1.0*agg_condorcet_winner[iElectionRule][iNumIterationsCase]) / agg_condorcet_existence + "\n";
					
				}
			
			//OTHER RULES
			} else {
				
				output += "Condorcet efficiency for " + e.strName + " voting : " + (1.0*agg_condorcet_winner[iElectionRule][0]) / agg_condorcet_existence + "\n";

			}
		}
		
		return output;
		
	}
	
	
	/*
	public void plotx() {
		

		Election objSatisfactionPlotElection = null;
		
		for (int iElectionRule=0; iElectionRule<listElectionRules.size(); iElectionRule++ ) {

			Election e = listElectionRules.get(iElectionRule);

			
			for (int iNumIterationsCase=0; iNumIterationsCase<listNumIterations.size(); iNumIterationsCase++ ) {
				
				double dReference = listNumIterations.get(iNumIterationsCase);

				int iCase;
				
				//ITERATIVE LEARNING
				if (e instanceof Election_IterativeLearning) {
					
					iCase = iNumIterationsCase;
				
					if (objSatisfactionPlotElection == null) {
						objSatisfactionPlotElection = e;
					}
						
						
				//OTHER RULES
				} else { 
				
					iCase = 0;
				
				}


				if (e == objSatisfactionPlotElection) {
					Arrays.sort(agg_voter_satisfaction[iElectionRule][iCase]);
					for (int iVoter=0; iVoter<iNumVoters; iVoter++ ) {
						chartVoterSatisfaction.addPoint(iVoter, dReference, (1.0*agg_voter_satisfaction[iElectionRule][iCase][iVoter])/iNumRuns);
					}
				}
				
				
				chartCondorcet.addPoint(iElectionRule, dReference, (1.0*agg_condorcet_winner[iElectionRule][iCase])/agg_condorcet_existence);
					
				chartBordaScore.addPoint(iElectionRule, dReference, (1.0*agg_borda_score[iElectionRule][iCase])/iNumRuns);
				chartBordaNormalizedScore.addPoint(iElectionRule, dReference, (1.0*agg_borda_normscore[iElectionRule][iCase])/iNumRuns);
				chartBordaRatioToMax.addPoint(iElectionRule, dReference, (1.0*agg_borda_ratio[iElectionRule][iCase])/iNumRuns);
				chartBordaEfficiency.addPoint(iElectionRule, dReference, (1.0*agg_borda_winner[iElectionRule][iCase])/iNumRuns);

				chartEgalitarian.addPoint(iElectionRule, dReference, (1.0*agg_esw_score[iElectionRule][iCase])/iNumRuns);

				chartUtilitarian.addPoint(iElectionRule, dReference, (1.0*agg_usw_score[iElectionRule][iCase])/iNumRuns);
			
			}
			
			
		}

		
		chartCondorcet.refresh();

		chartBordaScore.refresh();
		chartBordaNormalizedScore.refresh();
		chartBordaRatioToMax.refresh();
		chartBordaEfficiency.refresh();

		chartEgalitarian.refresh();

		chartUtilitarian.refresh();
		
		chartVoterSatisfaction.refresh();
		
	}
	*/
	
	//-------------------------------------------------------------------------------------
	
		
	
	
	
}
