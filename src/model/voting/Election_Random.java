package model.voting;





public class Election_Random extends Election {
	
	
	
	
	@Override
	public void setDefaultName() {
		strName = "Random";
	}
	
	
	public static ResultList calcElectionResult(CandidateList listCandidates) {

		int iNumCandidates = listCandidates.size();
		
		//PREPARE REMAINING LIST
		CandidateList listValidCandidates = new CandidateList(listCandidates);
		
		//PREPARE RESULT LIST
		ResultList listResult = new ResultList(iNumCandidates);
		
		for (int i=0; i<iNumCandidates; i++) {

			int idx = rnd.nextInt(listValidCandidates.size());
			Candidate c = listValidCandidates.remove(idx);
			listResult.add(i, c);
			
		}
		
		return listResult;
		
	}

	
	@Override
	public ResultList run() {
		
		result = calcElectionResult(getCandidates());
		return result;

		
	}
	
}
