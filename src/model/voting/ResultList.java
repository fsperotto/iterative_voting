package model.voting;

import java.util.ArrayList;

public class ResultList extends ArrayList<CandidateList> {

	private static final long serialVersionUID = 1L;

	
	public ResultList(int iNumCandidates) {

		super(iNumCandidates);
		
		for (int i=0; i<iNumCandidates; i++) {
			CandidateList listPosition = new CandidateList();
			add(listPosition);
		}
		
	}
	

	//public ResultList(Collection<? extends CandidateList> c) {
	public ResultList(ResultList c) {
		
		super(c);
		
	}
	
	
	
	
	public ResultList(ScoreList listScore) {

		this(listScore.size());
		
		if (listScore.isSorted()) {

			//fill the ordered result list
			int cur_score = listScore.getFirstScore();
			int cur_pos = 0;
			add(cur_pos, listScore.getFirstCandidate());
			
			for (int i=1; i<size(); i++) {
				int new_score = listScore.getScore(i);
				if (new_score < cur_score) {
					cur_pos = i;
				}
				add(cur_pos, listScore.getCandidate(i));
			}
			
		} else {
			
			System.err.println("You must sort the score list before create a result list.");
			
		}
		
		
	}
	
	
	public void add(int iPosition, Candidate candidate) {
		
		get(iPosition).add(candidate);
		
	}

	
	
	@Override
	public void clear() {
		
		for (CandidateList l : this) {
			l.clear();
		}
		
	}
	
}
