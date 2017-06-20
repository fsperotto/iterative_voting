package model.voting;



public class Election_Tournement extends Election {
	
	//points
	protected int iVictoryPoints = +1; 
	protected int iTiePoints = 0;
	protected int iDefeatPoints = 0;

	
	
	
	@Override
	public void setDefaultName() {
		strName = "Tournement";
	}
	
	
	public static ResultList calcElectionResult(CandidateList listCandidates, VoterList listVoters, int iVictoryPoints, int iTiePoints, int iDefeatPoints, EnumTieBreakType iTieBreakMode) {
		
		//int iNumCandidates = listCandidates.size();
		int iNumVoters = listVoters.size();
		
		//PREPARE SCORE
		//int[] aiPoints = new int[iNumCandidates];
		ScoreList listScore = new ScoreList(listCandidates, iTieBreakMode);

		
		double dHalf = ((double)iNumVoters) / 2;

		//compute score (get the ballots of each voter)
		for (Candidate ci : listCandidates) {
			
			//reset points
			//aiPoints[ci.id]=0;
			
			//duel tournament i vs j
			for (Candidate cj : listCandidates) {

				int score=0;
				
				if (ci != cj) {
					
					for (Voter v: listVoters) {
						if (v.getPosition(ci) < v.getPosition(cj)) {
							score++;
						}
					}
					
					if (score > dHalf) {
						
						//i beats j
						//aiPoints[ci.id] += iVictoryPoints;
						listScore.inc(ci, iVictoryPoints);
						
						
					} else if (score < dHalf) {

						//j beats i
						//aiPoints[ci.id] += iDefeatPoints;
						listScore.inc(ci, iDefeatPoints);
						
					} else {

						//tie
						//aiPoints[ci.id] += iTiePoints;
						listScore.inc(ci, iTiePoints);
						
					}
				}
			}
			
		}

		
		
		//sort the score list (from greater to smallest)
		listScore.sort();
		
		//PREPARE RESULT LIST
		ResultList listResult = new ResultList(listScore);
		
		
		/*
		//sort the score list (from greater to smallest)
		listScore.sort();
		

		//PREPARE RESULT LIST
		ResultList listResult = new ResultList(iNumCandidates);
		
		//fill the ordered result list
		int cur_score = listScore.get(0).getValue();
		int cur_pos = 0;
		listResult.get(cur_pos).add(listScore.get(0).getKey());
		for (int i=1; i<iNumCandidates; i++) {
			int new_score = listScore.get(i).getValue();
			if (new_score < cur_score) {
				cur_pos = i;
			}
			listResult.add(cur_pos, listScore.get(i).getKey());
		}
		*/
		
		//return result
		return listResult;

	}
	

	public void setPointsAsCopeland() {
		
		iVictoryPoints = +1; 
		iTiePoints = 0;
		iDefeatPoints = 0;
		
	}

	public void setPointsAsSoccer() {
		
		iVictoryPoints = +3; 
		iTiePoints = +1;
		iDefeatPoints = 0;
		
	}

	public void setPoints(int iVictoryPoints, int iTiePoints, int iDefeatPoints) {
		
		this.iVictoryPoints = iVictoryPoints; 
		this.iTiePoints = iTiePoints;
		this.iDefeatPoints = iDefeatPoints;
		
	}
	
	
	@Override
	public ResultList run() {
		
		result = calcElectionResult(getCandidates(), getVoters(), iVictoryPoints, iTiePoints, iDefeatPoints, iTieBreakMode);
		return result;
		
	}
	
	
	
	
}
