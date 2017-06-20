package model.voting;




public class Election_Borda extends Election {
	

	
	
	
	@Override
	public void setDefaultName() {
		strName = "Borda";
	}

	
	
	
	public static ResultList calcElectionResult(CandidateList listCandidates, int[] borda, EnumTieBreakType iTieBreakMode) {
		
		//PREPARE SCORE LIST
		ScoreList listScore = new ScoreList(listCandidates, iTieBreakMode);

		//fill the score list
		for (Candidate c : listCandidates)
			listScore.set(c, borda[c.id]);

		//sort the score list (from greater to smallest)
		listScore.sort();
		
		//PREPARE RESULT LIST
		ResultList listResult = new ResultList(listScore);
		
		//return result
		return listResult;
		

	}
	
		
	@Override
	public ResultList run() {
		
		return calcElectionResult(getCandidates(), objScenario.aiBordaScore, iTieBreakMode);
		
	}
	
}
