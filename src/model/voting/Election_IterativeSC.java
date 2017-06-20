package model.voting;




public class Election_IterativeSC extends Election {
	

	
	
	

	@Override
	public void setDefaultName() {
		strName = "Iterative Second Chance";
	}
	
	
	
	public static ResultList calcElectionResult(CandidateList listCandidates, VoterList listVoters, EnumTieBreakType iTieBreakMode) {

		
		//INITIALLY IS ONE ROUND PLURALITY 
		for (Voter v : listVoters) {
			//mapBallots.put(v, v.getMostPreferred());
			v.ballot=v.getMostPreferred(listCandidates);
		}
		

		//PREPARE SCORE LIST
		ScoreList listScore = new ScoreList(listCandidates, iTieBreakMode);

		//CALCULATE ELECTION RESULT
		//compute score (get the ballots of each voter)
		for (Voter v: listVoters)
			listScore.inc(v.ballot);

		//sort the score list (from greater to smallest)
		listScore.sort();
		
		
		//ITERATIONS
		
		for (Voter manipulator : listVoters) {

			Candidate winner = listScore.getWinner(iTieBreakMode);
			
			//MANIPULATE
			
			if ( (winner != manipulator.getPreferred(0, listCandidates)) && ((winner != manipulator.getPreferred(1, listCandidates))) ) {
				
				manipulator.ballot = manipulator.getPreferred(1, listCandidates);

				//RE-CALCULATE ELECTION RESULT
				//compute score (get the ballots of each voter)
				listScore.reset();
				for (Voter v: listVoters)
					listScore.inc(v.ballot);

				//sort the score list (from greater to smallest)
				listScore.sort();
				
			}
			
		}

		
		//PREPARE RESULT LIST
		ResultList listResult = new ResultList(listScore);
		
		//return result
		return listResult;

	}
	
		
	@Override
	public ResultList run() {
		
		result = calcElectionResult(getCandidates(), getVoters(), iTieBreakMode);
		return result;

	}
	
	
}
