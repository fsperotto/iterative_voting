package model.voting;







public class Election_IterativeBestResponse extends Election {
	

	
	
	@Override
	public void setDefaultName() {
		strName = "Iterative Best Response";
	}

	
	
	
	public static ResultList calcElectionResult(CandidateList listCandidates, VoterList listVoters, EnumTieBreakType iTieBreakMode) {


		//INITIALLY IS ONE ROUND PLURALITY 
		for (Voter v : listVoters) {
			v.ballot = v.getMostPreferred(listCandidates);
		}
		
		//PREPARE SCORE LIST
		ScoreList listScore = new ScoreList(listCandidates, iTieBreakMode);

		//CALCULATE ELECTION RESULT
		for (Voter v: listVoters)
			listScore.inc(v.ballot);

		//sort the score list (from greater to smallest)
		listScore.sort();

		
		//Candidate winner = listScore.getFirstCandidate();
		Candidate winner = listScore.getWinner(iTieBreakMode);
		int iWinnerScore = listScore.getMaxScore();
		
		VoterList listManipulators = new VoterList();
		
		
		//UPDATE MANIPULATORS
		listManipulators.clear();
		for (Voter v : listVoters) {
			
			for (int i=0; i<v.getPosition(winner); i++) {
				
				Candidate preferred = v.getPreferred(i);
				int iPreferredScore = listScore.getScore(preferred); 

				if ( (preferred != v.ballot) && (preferred != winner)) {
					
					if (iPreferredScore == iWinnerScore) { 
						
						listManipulators.add(v);
						break;
					
					} else if (iPreferredScore == iWinnerScore-1) {
						
						CandidateList listDuo = new CandidateList(2);
						listDuo.add(winner);
						listDuo.add(preferred);

						//if (preferred.getId() < winner.getId()) {
						if (listDuo.getTieBreakPreferred(iTieBreakMode) == preferred) {
							listManipulators.add(v);
							break;
						}
						
					}
					
				}
			
			}
		}

		
		while (!listManipulators.isEmpty()) {
		
			
			//CHOSE A MANIPULATOR AT RANDOM
			Voter manipulator =  listManipulators.get(rnd.nextInt(listManipulators.size()));
			
			//BEST RESPONSE
			for (int i=0; i<manipulator.getPosition(winner); i++) {
				
				Candidate preferred = manipulator.getPreferred(i);
				int iPreferredScore = listScore.getScore(preferred); 

				if ( (preferred != manipulator.ballot) && (preferred != winner)) {
					if (iPreferredScore == iWinnerScore) { 
						manipulator.ballot = preferred;
						break;
					} else if ( (iPreferredScore == iWinnerScore-1) && (preferred.getId() < winner.getId()) ) {
						manipulator.ballot = preferred;
						break;
					}
					
				}
			}
			

			//RECALCULATE ELECTION RESULT
			listScore.reset();
			//listScore = new ScoreList(listCandidates, iTieBreakMode);
			for (Voter v: listVoters)
				listScore.inc(v.ballot);

			//sort the score list (from greatest to smallest)
			listScore.sort();

			
			//winner = listScore.getFirstCandidate();
			winner = listScore.getWinner(iTieBreakMode);
			iWinnerScore = listScore.getMaxScore();
			
			
			
			//UPDATE MANIPULATORS
			listManipulators.clear();
			for (Voter v : listVoters) {
				
				for (int i=0; i<v.getPosition(winner); i++) {
					
					Candidate preferred = v.getPreferred(i);
					int iPreferredScore = listScore.getScore(preferred); 

					if ( (preferred != v.ballot) && (preferred != winner)) {
						
						if (iPreferredScore == iWinnerScore) { 
							
							listManipulators.add(v);
							break;
						
						} else if (iPreferredScore == iWinnerScore-1) {
							
							CandidateList listDuo = new CandidateList(2);
							listDuo.add(winner);
							listDuo.add(preferred);

							//if (preferred.getId() < winner.getId()) {
							if (listDuo.getTieBreakPreferred(iTieBreakMode) == preferred) {
								listManipulators.add(v);
								break;
							}
							
						}
						
					}
				
				}
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
