package model.voting;






public class Election_Iterative3Pragmatist extends Election {
	

	
	
	@Override
	public void setDefaultName() {
		strName = "Iterative 3 Pragmatist";
	}

	
	
	
	public static ResultList calcElectionResult(CandidateList listCandidates, VoterList listVoters, EnumTieBreakType iTieBreakMode) {

		
		//PREPARE BALLOTS
		//HashMap<Voter, Candidate> mapBallots = new HashMap<Voter, Candidate>(listVoters.size()); 
		
		//INITIALLY IS ONE ROUND PLURALITY 
		for (Voter v : listVoters) {
			//mapBallots.put(v, v.getMostPreferred());
			v.ballot = v.getMostPreferred(listCandidates);
		}
		

		//PREPARE SCORE LIST
		ScoreList listScore = new ScoreList(listCandidates, iTieBreakMode);

		//CALCULATE ELECTION RESULT
		//compute score (get the ballots of each voter)
		for (Voter v: listVoters)
			listScore.inc(v.ballot);

		//sort the score list (from greater to smallest)
		listScore.sort();

		//get k bests
		int k = 3;
		
		CandidateList listWinners = new CandidateList(k);

		for (int i=0; i<k; i++) {
			listWinners.add(listScore.getCandidate(i));
		}
		
		
		//ITERATIONS
		for (Voter manipulator : listVoters) {

			//MANIPULATE
			
			manipulator.ballot = manipulator.getMostPreferred(listWinners);
			
		}

		
		//RECALCULATE ELECTION RESULT
		listScore.reset();
		for (Voter v: listVoters)
			listScore.inc(v.ballot);

		//sort the score list (from greater to smallest)
		listScore.sort();
		
		
		
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
