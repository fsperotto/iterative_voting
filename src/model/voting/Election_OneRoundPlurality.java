package model.voting;





public class Election_OneRoundPlurality extends Election {
	

	
	
	@Override
	public void setDefaultName() {
		strName = "One Round Plurality";
	}
	
	
	public static ResultList calcElectionResult(CandidateList listCandidates, VoterList listVoters, EnumTieBreakType iTieBreakMode) {
	
		//PREPARE SCORE LIST
		ScoreList listScore = new ScoreList(listCandidates, iTieBreakMode);

		//compute score (get the ballots of each voter)
		for (Voter v: listVoters)
			listScore.inc(v.getMostPreferred(listCandidates));
		
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
