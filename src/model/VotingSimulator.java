package model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.SwingPropertyChangeSupport;

import model.learning.EnumExplorationMethod;
import model.learning.EnumInitUtilityMethod;
import model.voting.Candidate;
import model.voting.CandidateList;
import model.voting.Election;
import model.voting.ElectionSeriesVaryingIterations;
import model.voting.ElectionSeriesVaryingProfiles;
import model.voting.Election_Borda;
import model.voting.Election_Iterative3Pragmatist;
import model.voting.Election_IterativeBestResponse;
import model.voting.Election_IterativeLearning;
import model.voting.Election_IterativeSC;
import model.voting.Election_OneRoundPlurality;
import model.voting.Election_Random;
import model.voting.Election_RecursiveElimination;
import model.voting.Election_Tournement;
import model.voting.Election_TwoRoundsPlurality;
import model.voting.EnumUtilityGenerationMode;


/**
* SIMULATOR ENGINE (MODEL).
* 
* This class can execute a series of voting scenarios.
* The user can configure the number of voters, candidates, iterations and election methods
*  
* @author Filipo S. Perotto / Stéphane Airiau / Umberto Grandi
* @version 1.0
*/


public class VotingSimulator implements Runnable {

	
	//interface for property listeners
	SwingPropertyChangeSupport objPropertyChangeSupport;
	

	//progress bar value
	protected int v = 0;

	
	protected int iNumRuns;

	protected int[] aiCandidates;
	protected int[] aiVoters;
	protected int[] aiLearning;
	
	//EnumUtilityGenerationMode eUtilityGenerationMode = EnumUtilityGenerationMode.UrnModelLinear;
	//EnumUtilityGenerationMode eUtilityGenerationMode = EnumUtilityGenerationMode.UrnModelSigmoid;
	protected EnumUtilityGenerationMode eUtilityGenerationMode = EnumUtilityGenerationMode.UrnModelExponential;

	protected ArrayList<Election> listElectionRules = new ArrayList<Election>();

	
	//FLAGS 
	
	public boolean bRandomRule;
	public boolean bPluralityRule;
	public boolean bTwoRoundRule;
	public boolean bSTVRule;
	public boolean bRecVetoRule;
	public boolean bBordaRule;
	public boolean bUSWRule;
	public boolean bESWRule;
	public boolean bCopelandRule;
	public boolean bIterativeBestResponseRule;
	public boolean bIterative3PragmatistRule;
	public boolean bIterativeSecondChanceRule;
	public boolean bIterativeLearningSoftmaxRule;
	public boolean bIterativeLearningEGreedyRule;
	public boolean bIterativeLearningUCBRule;
	public boolean bIterativeLearningOptimisticRule;
    
	public boolean bVerboseMode;
	public boolean bWriteOutputFile;

	
	
	//CONSTRUCTOR
	
    public VotingSimulator() {

    	//interface for property listeners
		objPropertyChangeSupport = new SwingPropertyChangeSupport(this);
		
	}

	
	//PROPERTY CHANGE 
	
	public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
		objPropertyChangeSupport.addPropertyChangeListener(listener);
	}

	public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
		objPropertyChangeSupport.removePropertyChangeListener(listener);
	}	
	
	
	//GETTERS AND SETTERS
	
	public int getNumVoters() {
	  return aiVoters[0];
	}

	public int getNumVoters(int i) {
	  return aiVoters[i];
	}
	
	public int getMaxVoters() {
		return aiVoters[aiVoters.length-1];
	}
	
	public int getNumCandidates() {
	  return aiCandidates[0];
    }

	public int getNumCandidates(int i) {
	  return aiCandidates[i];
    }
	
	public int getMaxCandidates() {
		return aiCandidates[aiCandidates.length-1];
	}
	
	public int getNumIterations() {
	  return aiLearning[0];
	}

	public int getNumIterations(int i) {
	  return aiLearning[i];
    }
	
	public int getMaxIterations() {
		return aiLearning[aiLearning.length-1];
	}
	
	public boolean isVaryingVoters() {
       return (aiVoters.length > 1);
	}
	
	public boolean isVaryingCandidates() {
       return (aiCandidates.length > 1);
	}

	public boolean isVaryingIterations() {
       return (aiLearning.length > 1);
	}
	
	public int getVotersSetSize() {
       return (aiVoters.length);
	}

	public int getCandidatesSetSize() {
       return (aiCandidates.length);
	}

	public int getIterationsSetSize() {
       return (aiLearning.length);
	}

	
	
	// INIT
	
	public void initialize() {

		
		//reset the list
		listElectionRules.clear();
		
		
		//RANDOM
		if (bRandomRule) {
			Election objElectionRandom = new Election_Random();
			listElectionRules.add(objElectionRandom);
		}
		
		//PLURALITY
		if (bPluralityRule) {
			Election_OneRoundPlurality objElectionPluralityOneRound = new Election_OneRoundPlurality();
			listElectionRules.add(objElectionPluralityOneRound);
		}
		
		
		//COPELAND
		if (bCopelandRule) {
			Election_Tournement objElectionCopeland = new Election_Tournement();
			objElectionCopeland.setPointsAsCopeland();
			objElectionCopeland.setName("Copeland");
			listElectionRules.add(objElectionCopeland);
		}

		
		//BORDA
		if (bBordaRule) {
			Election_Borda objElectionBorda = new Election_Borda();
			listElectionRules.add(objElectionBorda);
		}
		
		
		//STV
		if (bSTVRule) {
			Election_RecursiveElimination objElectionSTV = new Election_RecursiveElimination();
			objElectionSTV.setPointsAsSTV();
			objElectionSTV.setName("STV");
			listElectionRules.add(objElectionSTV);
		}

		
		//RECURSIVE VETO
		if (bRecVetoRule) {
			Election_RecursiveElimination objElectionWorstOut = new Election_RecursiveElimination();
			objElectionWorstOut.setPointsAsWorstOut();
			objElectionWorstOut.setName("Recursive Veto");
			listElectionRules.add(objElectionWorstOut);
		}
		
		
		//SECOND CHANCE
		if (bIterativeSecondChanceRule) {
			Election_IterativeSC objElectionIterativeSC = new Election_IterativeSC();
			listElectionRules.add(objElectionIterativeSC);
		}
		

		//3 PRAGMATIST
		if (bIterative3PragmatistRule) {
			Election_Iterative3Pragmatist objElectionIterative3P = new Election_Iterative3Pragmatist();
			listElectionRules.add(objElectionIterative3P);
		}

		//BEST RESPONSE
		if (bIterativeBestResponseRule) {
			Election_IterativeBestResponse objElectionIterativeBR = new Election_IterativeBestResponse();
			listElectionRules.add(objElectionIterativeBR);
		}
		
		
		//TWO ROUNDS
		if (bTwoRoundRule) {
			Election_TwoRoundsPlurality objElectionTwoRoundsPlurality = new Election_TwoRoundsPlurality();
			listElectionRules.add(objElectionTwoRoundsPlurality);
		}
		
		
		//ITERATIVE LEARNING
		Election_IterativeLearning objElectionIterativeLearning;

		
		//ITERATIVE LEARNING (UCB)
		if (bIterativeLearningUCBRule) {
			objElectionIterativeLearning = new Election_IterativeLearning();
			objElectionIterativeLearning.setName("Iterative Learning UCB1");
			objElectionIterativeLearning.setLearningVoterExplorationMethod(EnumExplorationMethod.ucb1);
			listElectionRules.add(objElectionIterativeLearning);
		}

		//ITERATIVE LEARNING (EPS LIN DECAY ZERO)
		if (bIterativeLearningEGreedyRule) {
			objElectionIterativeLearning = new Election_IterativeLearning();
			objElectionIterativeLearning.setName("Iterative Learning e-greedy linear-decay");
			objElectionIterativeLearning.setLearningVoterExplorationMethod(EnumExplorationMethod.epsilon_greedy_pref_linear_decay);
			//objElectionIterativeLearning.setLearningVoterExplorationMethod(EnumExplorationMethod.epsilon_greedy_pref_sigmoid_decay);
			//objElectionIterativeLearning.setLearningVoterExplorationMethod(EnumExplorationMethod.epsilon_greedy_pref_exp_decay);
			objElectionIterativeLearning.setLearningVoterInitUtilityMethod(EnumInitUtilityMethod.Zero);
			//objElectionIterativeLearning.setLearningVoterInitUtilityMethod(EnumInitUtilityMethod.PreferenceValue);
			//objElectionIterativeLearning.setLearningVoterInitUtilityMethod(EnumInitUtilityMethod.OptimisticUtilityValue);
			//objElectionIterativeLearning.setLearningVoterInitUtilityMethod(EnumInitUtilityMethod.Random);
			objElectionIterativeLearning.setLearningVoterPureExplorationFinishTime(0.0);
			objElectionIterativeLearning.setLearningVoterPureExploitationStartTime(0.9);
			listElectionRules.add(objElectionIterativeLearning);
		}
		
		
		//ITERATIVE LEARNING (ADAPT BOLTZMANN)
		if (bIterativeLearningSoftmaxRule) {
			objElectionIterativeLearning = new Election_IterativeLearning();
			objElectionIterativeLearning.setName("Iterative Learning softMax self-adapted");
			objElectionIterativeLearning.setLearningVoterExplorationMethod(EnumExplorationMethod.softmax_boltzmann_adaptive);
			objElectionIterativeLearning.setLearningVoterInitUtilityMethod(EnumInitUtilityMethod.Zero);
			listElectionRules.add(objElectionIterativeLearning);
		}

		
		//ITERATIVE LEARNING (OPT GREEDY)
		if (bIterativeLearningOptimisticRule) {
			objElectionIterativeLearning = new Election_IterativeLearning();
			objElectionIterativeLearning.setName("Iterative Learning Optimistic Greedy");
			objElectionIterativeLearning.setLearningVoterExplorationMethod(EnumExplorationMethod.greedy);
			objElectionIterativeLearning.setLearningVoterInitUtilityMethod(EnumInitUtilityMethod.PreferenceValue);
			//objElectionIterativeLearning.setLearningVoterInitUtilityMethod(EnumInitUtilityMethod.OptimisticUtilityValue);
			listElectionRules.add(objElectionIterativeLearning);
		}
		

		/*
		//ITERATIVE LEARNING (BOLTZMANN 2000 ZERO)
		objElectionIterativeLearning = new Election_IterativeLearning();
		objElectionIterativeLearning.setName("Iterative Learning softMax T=100 d=0.995");
		objElectionIterativeLearning.setLearningVoterExplorationMethod(EnumExplorationMethod.softmax_boltzmann);
		objElectionIterativeLearning.setLearningVoterInitUtilityMethod(EnumInitUtilityMethod.Zero);
		objElectionIterativeLearning.setLearningVoterInitTemp(100.0);
		objElectionIterativeLearning.setLearningVoterDeltaTemp(0.995);
		listElectionRules.add(objElectionIterativeLearning);
		*/
		
		
	}
	
	
	//RUN
	
    @Override
	public void run() {

		
		
		//VARYING CANDIDATES
		if (isVaryingCandidates()) {

			//number of voters
			int nv = aiVoters[0];

			//number of iterations
			int ni = aiLearning[0];

			for (int i=0; i<listElectionRules.size(); i++ ) {

				Election e = listElectionRules.get(i);
				
				if (e instanceof Election_IterativeLearning) {

					((Election_IterativeLearning)(e)).setNumIterations(ni);
					
				}
				
			}

			if (aiVoters.length > 1) {
				System.err.println("Parameter list for number of voters will not be taken into account.");
			}

			if (aiLearning.length > 1) {
				System.err.println("Parameter list for number of learning iterations will not be taken into account.");
			}
			
			
			//varying number of candidates
			for (int nc : aiCandidates ) {
				
				//create a linear list of candidates
				CandidateList listCandidates = new CandidateList(nc);
				for (int i=0; i<nc; i++) {
					listCandidates.add(new Candidate(i));
				}
				
				//NEW ELECTION SCENARIO
				ElectionSeriesVaryingProfiles es = new ElectionSeriesVaryingProfiles(listElectionRules, listCandidates, nv, iNumRuns);

				es.eUtilityGenerationMode = eUtilityGenerationMode;
				
				/*
				es.bVerboseMode = bVerboseMode;
				es.bWriteOutputFile = bWriteOutputFile;
				*/
				
				es.addPropertyChangeListener(new PropertyChangeListener() {
					
					public void propertyChange(PropertyChangeEvent evt) {

						//TASK PROGRESS BAR
						if (evt.getPropertyName().equalsIgnoreCase("task_progress")) {

							//just propagate the event
							objPropertyChangeSupport.firePropertyChange(evt);
							
						}
						
					}
				});
				
				
				//RUN ELECTION SCENARIO
				es.run();
				

				//trigger output stream event
				objPropertyChangeSupport.firePropertyChange("console", null, es.toString());

				//trigger new data event
				objPropertyChangeSupport.firePropertyChange("data", nc, es.getData());

				//update progress
				v++; 

				//trigger progress event
				objPropertyChangeSupport.firePropertyChange("progress", null, v);
				
			}
			
			
		//VARYING NUMBER OF VOTERS
		} else if (aiVoters.length > 1) {

			//number of candidates
			int nc = aiCandidates[0];

			//create a linear list of candidates
			CandidateList listCandidates = new CandidateList(nc);
			for (int i=0; i<nc; i++) {
				listCandidates.add(new Candidate(i));
			}
			
			//number of iterations
			int ni = aiLearning[0];

			for (int i=0; i<listElectionRules.size(); i++ ) {

				Election e = listElectionRules.get(i);
				
				if (e instanceof Election_IterativeLearning) {

					((Election_IterativeLearning)(e)).setNumIterations(ni);
					
				}
				
			}
			
			if (aiLearning.length > 1) {
				System.err.println("Parameter list for number of learning iterations will not be taken into account.");
			}

			
			//varying number of voters
			for (int nv : aiVoters) {

				//NEW ELECTION SCENARIO
				ElectionSeriesVaryingProfiles es = new ElectionSeriesVaryingProfiles(listElectionRules, listCandidates, nv, iNumRuns);

				es.eUtilityGenerationMode = eUtilityGenerationMode;
				
				
				//RUN ELECTION SCENARIO
				es.run();

				//trigger output stream event
				objPropertyChangeSupport.firePropertyChange("console", null, es.toString());

				//trigger new data event
				objPropertyChangeSupport.firePropertyChange("data", nv, es.getData());
				
				//progress bar
				v++; 

				objPropertyChangeSupport.firePropertyChange("progress", null, v);
				
			}
			
		//varying number of iterations
		} else {

			//number of candidates
			int nc = aiCandidates[0];

			//create a linear list of candidates
			CandidateList listCandidates = new CandidateList(nc);
			for (int i=0; i<nc; i++) {
				listCandidates.add(new Candidate(i));
			}

			//number of voters
			int nv = aiVoters[0];
			
			List<Integer> listNumIterations = new ArrayList<Integer>(aiLearning.length);
			for (int ni : aiLearning) {
				listNumIterations.add(ni);
			}

			//NEW ELECTION SERIES
			ElectionSeriesVaryingIterations es = new ElectionSeriesVaryingIterations(listElectionRules, listCandidates, nv, iNumRuns, listNumIterations);
			

			es.eUtilityGenerationMode = eUtilityGenerationMode;

				
			//RUN ELECTION SCENARIO
			es.run();

			//trigger output stream event
			objPropertyChangeSupport.firePropertyChange("console", null, es.toString());

			//trigger new data event
			//objPropertyChangeSupport.firePropertyChange("data", nv, es.getData());
			
		}

	}


	public List<Election> getElectionRules() {
		return listElectionRules;
	}


	public void setNumRuns(int iNumRuns) {
       this.iNumRuns=iNumRuns;		
	}


	public void setParams(int[] aiCandidates, int[] aiVoters, int[] aiLearning) {
		this.aiCandidates = aiCandidates.clone();
		this.aiVoters = aiVoters.clone();
		this.aiLearning = aiLearning.clone();	
	}
	
	

}
