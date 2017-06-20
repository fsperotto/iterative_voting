package model.voting;


import java.io.IOException;

import model.learning.EnumExplorationMethod;
import model.learning.EnumInitUtilityMethod;



public class Election_IterativeLearning extends Election {
	

	
	
	
	@Override
	public void setDefaultName() {
		strName = "Iterative Learning";
	}
	
	
	//exploration method
	protected EnumExplorationMethod eLearningVoterExplorationMethod = EnumExplorationMethod.greedy;
	
	//initialize q using preference
	protected EnumInitUtilityMethod eLearningVoterInitUtilityMethod = EnumInitUtilityMethod.PreferenceValue;

	//learning parameters
	protected double dLearningVoterAlpha = 0.1;
	protected double dLearningVoterGamma = 0.9;
	
	//for Epsilon exploration
	protected double dLearningVoterEpsilon = 0.1;

	//for adaptive decay
	protected int iLearningVoterTargetIterations = 2000;
	protected double dLearningVoterPureExplorationFinishTime = 0.05;
	protected double dLearningVoterPureExploitationStartTime = 0.05;
	
	//for Boltzmann exploration
	protected double dLearningVoterDeltaTemp = 0.99;
	protected double dLearningVoterInitTemp = 100.0;
	
	
	protected int iNumIterations = 2000;
	
	
	
	
	
	public static ResultList calcElectionResult(CandidateList listCandidates, VoterList listVoters, int iNumIterations, EnumTieBreakType iTieBreakMode) {
	
		//PREPARE SCORE LIST
		ScoreList listScore = new ScoreList(listCandidates, iTieBreakMode);

		
		//RUN ELECTIONS ITERATIVELY
		for (int it=0; it<iNumIterations; it++) {
			
			//compute score (get the ballots of each voter)
			listScore.reset();
			for (Voter v: listVoters)
				listScore.inc(v.getVote());
			
			//sort the score list (from greater to smallest)
			listScore.sort();
			
			
			//selection of some of the winners 
			Candidate winner = listScore.getWinner(iTieBreakMode);
			
			
			// send results to agents	
			for (Voter v: listVoters)
				v.update(winner);
			
			// stats
			//computeStats(it, winner, score);
			//**************
			//**************
			
			
		}
		
		
		//compute final score 
		listScore.reset();
		for (Voter v: listVoters)
			listScore.inc(v.getBest());

		listScore.sort();
		

		//PREPARE RESULT LIST
		ResultList listResult = new ResultList(listScore);

		
		//return result
		return listResult;
		
		
		
	}
		
	
	//learning parameters
	public void setNumIterations(int iNewValue) {
		
		iNumIterations = iNewValue;
		
		setLearningVoterTargetIterations(iNewValue);
		
	}


	
	//exploration method
	public void setLearningVoterExplorationMethod(EnumExplorationMethod eExplorationMethod) {
		this.eLearningVoterExplorationMethod = eExplorationMethod;
	}
	
	//initialize q using preference
	public void setLearningVoterInitUtilityMethod(EnumInitUtilityMethod eInitUtilityMethod) {
		this.eLearningVoterInitUtilityMethod = eInitUtilityMethod;
	}
	

	//learning parameters
	public void setLearningVoterAlpha(double dNewValue) {
		dLearningVoterAlpha = dNewValue;
	}

	public void setLearningVoterGamma(double dNewValue) {
		dLearningVoterGamma = dNewValue;
	}
	
	//for Epsilon exploration
	public void setLearningVoterEpsilon(double dNewValue) {
		dLearningVoterEpsilon = dNewValue;
	}

	//for adaptive decay
	public void setLearningVoterTargetIterations(int iNewValue) {
		iLearningVoterTargetIterations = iNewValue;
	}

	public void setLearningVoterPureExplorationFinishTime(double dNewValue) {
		dLearningVoterPureExplorationFinishTime = dNewValue;
	}

	public void setLearningVoterPureExploitationStartTime(double dNewValue) {
		dLearningVoterPureExploitationStartTime = dNewValue;
	}

	
	//for Boltzmann exploration
	public void setLearningVoterDeltaTemp(double dNewValue) {
		dLearningVoterDeltaTemp = dNewValue;
	}

	public void setLearningVoterInitTemp(double dNewValue) {
		dLearningVoterInitTemp = dNewValue;
	}
	
	
	
	
	
	private void formatLearningVoters() {
		
		for (Voter v : getVoters()) {

			v.objLearningSystem.setExplorationMethod(eLearningVoterExplorationMethod);
			v.objLearningSystem.setInitUtilityMethod(eLearningVoterInitUtilityMethod);
			
			v.objLearningSystem.setAlpha(dLearningVoterAlpha);

			v.objLearningSystem.setInitTemp(dLearningVoterInitTemp);
			v.objLearningSystem.setDeltaTemp(dLearningVoterDeltaTemp);

			v.objLearningSystem.setEpsilon(dLearningVoterEpsilon);
			
			v.objLearningSystem.setTargetIterations(iNumIterations);

			v.objLearningSystem.setPureExploitationStartTime(dLearningVoterPureExploitationStartTime);
			v.objLearningSystem.setPureExplorationFinishTime(dLearningVoterPureExplorationFinishTime);
			
			v.objLearningSystem.reset();
			
		}
		
	}
	

	
	
	
	
	
	
	
	
	@Override
	public ResultList run() {
		
		
		formatLearningVoters();
		
		
		result = calcElectionResult(getCandidates(), getVoters(), iNumIterations, iTieBreakMode);
		
		return result;

		
	}
	
	
	
	
	
	public void computeStats(int it, Candidate winner, int[] score){

		// social welfare
		int usw = objScenario.aiBordaScore[winner.id];
		
		try {
			
			//WRITE CSV FILE
			if (output_file) {
				out.write(winner + "; " + usw + "; ");
				for (int i=0;i<getNumCandidates();i++)
					out.write(score[i] + "; ");
				out.write("\n");
				out.flush();
			}
			
			//WRITE ON CONSOLE
			if (verbose_mode && (it>0) && (it%100==0) ) {

				// for now, write on the console
				//outputStream.print("winner: " + winner + "\t Borda: " + usw + "\t score: ");
				
				System.out.print(it+ " ~~~~~~~~~~~~~~~~~~ winner: " +winner + " borda: " + usw );
				if (objScenario.candCondorcetWinner == null)
					System.out.print("NO condorcet winner ");
				else if (objScenario.candCondorcetWinner == winner)
					System.out.print(" is Condorcet Winner ");
				else
					System.out.print(" fails to elect a Condorcet Winner ");	
				System.out.print(" votes: " );
				for (int i=0;i<getNumCandidates();i++)
					System.out.print(score[i] + "; ");
				System.out.println();
				for (Voter v: getVoters())
					v.show();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	
	
}
