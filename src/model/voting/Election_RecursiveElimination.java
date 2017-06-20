package model.voting;





public class Election_RecursiveElimination extends Election {
	
	//points
	protected int iPreferredPoints = +1;
	protected int iRejectedPoints = 0;

	
	
	
	
	@Override
	public void setDefaultName() {
		strName = "Recursive Elimination";
	}
	
	
	public void setPointsAsSTV() {
		
		iPreferredPoints = +1;
		iRejectedPoints = 0;
		
	}

	
	public void setPointsAsWorstOut() {
		
		iPreferredPoints = 0;
		iRejectedPoints = -1;
		
	}

	
	public void setPoints( int iPreferredPoints, int iRejectedPoints) {
		
		this.iPreferredPoints = iPreferredPoints; 
		this.iRejectedPoints = iRejectedPoints;
		
	}
	
	
	
	public static ResultList calcElectionResult(CandidateList listCandidates, VoterList listVoters, int iPreferredPoints, int iRejectedPoints, EnumTieBreakType iTieBreakMode) {
		
		int iNumCandidates = listCandidates.size();
		
		//PREPARE REMAINING LIST
		CandidateList listValidCandidates = new CandidateList(listCandidates);


		//PREPARE RESULT LIST
		ResultList listResult = new ResultList(iNumCandidates);
		
		
		//RUN N ELECTIONS RECURSIVELY
		for (int pos=iNumCandidates-1; pos>=0; pos--) {

			//PREPARE SCORE LIST
			ScoreList listScore = new ScoreList(listValidCandidates, iTieBreakMode);
			
			//compute score (get the ballots of each voter)
			listScore.reset();
			for (Voter v: listVoters) {
				listScore.inc(v.getMostPreferred(listValidCandidates), iPreferredPoints);
				listScore.inc(v.getMostRejected(listValidCandidates), iRejectedPoints);
				
			}
			
			//sort the score list (from greater to smallest)
			listScore.sort();

			//elimination of some of the worst
			Candidate eliminated = listScore.getLastCandidate();
			listValidCandidates.remove(eliminated);

			//fill the ordered result list
			listResult.add(pos, eliminated);
			
		}
		
		if (listValidCandidates.size() != 0) {
			System.err.println("ERROR: final list (REC ELIMIN) has more then zero element (" + listValidCandidates.size() + ")." );
		}
		
		return listResult;

	}
	
		
	@Override
	public ResultList run() {
		
		result = calcElectionResult(getCandidates(), getVoters(), iPreferredPoints, iRejectedPoints, iTieBreakMode);
		return result;

	}
	
	
}
