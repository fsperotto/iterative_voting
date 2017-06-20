package model.voting;





public class Election_TwoRoundsPlurality extends Election {
	
	
	
	
	@Override
	public void setDefaultName() {
		strName = "Two Rounds Plurality";
	}
	
	
	public static ResultList calcElectionResult(CandidateList listCandidates, VoterList listVoters, EnumTieBreakType iTieBreakMode) {
	
		//PREPARE SCORE LIST
		ScoreList listScore = new ScoreList(listCandidates, iTieBreakMode);

		//compute score (get the ballots of each voter)
		for (Voter v: listVoters)
			listScore.inc(v.getMostPreferred(listCandidates));
		
		//sort the score list (from greater to smallest)
		listScore.sort();


		CandidateList listTwo = new CandidateList(2);
		
		listTwo.add(listScore.getCandidate(0));
		listTwo.add(listScore.getCandidate(1));
		
		return Election_OneRoundPlurality.calcElectionResult(listTwo, listVoters, iTieBreakMode);
		
		
	}
	

	@Override
	public ResultList run() {
		
		result = calcElectionResult(getCandidates(), getVoters(), iTieBreakMode);
		return result;

		
	}
	
}
