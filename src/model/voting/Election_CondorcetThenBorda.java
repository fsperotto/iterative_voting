package model.voting;





public class Election_CondorcetThenBorda extends Election {
	

	
	
	@Override
	public void setDefaultName() {
		strName = "Condorcet then Borda";
	}
	
	
	
	public static ResultList calcElectionResult(CandidateList listCandidates, Candidate candCondorcetWinner, int[] borda, EnumTieBreakType iTieBreakMode) {
		

		//PREPARE SCORE LIST
		ScoreList listScore = new ScoreList(listCandidates, iTieBreakMode);

		//fill the score list
		for (Candidate c : listCandidates) {
			if (candCondorcetWinner == c) {
				listScore.set(c, Integer.MAX_VALUE);
			} else {
				listScore.set(c, borda[c.id]);
			}
		}

		//sort the score list (from greater to smallest)
		listScore.sort();
		
		//PREPARE RESULT LIST
		ResultList listResult = new ResultList(listScore);
		
		//return result
		return listResult;
		
	}
	
	
	@Override
	public ResultList run() {

		result = calcElectionResult(getCandidates(), objScenario.candCondorcetWinner, objScenario.aiBordaScore, iTieBreakMode);
		return result;

		
	}
	
}
