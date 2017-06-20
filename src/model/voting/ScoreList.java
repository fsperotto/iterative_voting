package model.voting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ScoreList {

	private final ArrayList<ScorePair> listScore;
	private final HashMap<Candidate, ScorePair> mapScore;
	
	private boolean isSorted = true;
	
	//private final EnumTieBreakType iTieBreakMode;

	
	//------------------------------------------------------------------------------
	
	public ScoreList(CandidateList listCandidates, EnumTieBreakType iTieBreakMode) {
		
		this.listScore = new ArrayList<ScorePair>(listCandidates.size());
		this.mapScore = new HashMap<Candidate, ScorePair>(listCandidates.size());
		
		for (Candidate c : listCandidates) {
			ScorePair p = new ScorePair(c, 0, iTieBreakMode);
			listScore.add(p);
			mapScore.put(c, p);
		}
		
		isSorted = true;
		
		//this.iTieBreakMode = iTieBreakMode;
		
	}
	
	/*
	public ScoreList(int iInitialCapacity) {
		listScore = new ArrayList<ScorePair>(iInitialCapacity);
	}

	public ScoreList(Collection<? extends ScorePair> c) {
		listScore = new ArrayList<ScorePair>(c);
	}
	*/

	//------------------------------------------------------------------------------

	public final int size() {
		
		return listScore.size();
		
	}
	
	
	public final void sort() {
	
		//sort the score list by score (from greatest score to smallest)
		Collections.sort(listScore);
		Collections.reverse(listScore);

		isSorted = true;
		
	}
	
	public boolean isSorted() {
		return isSorted;
	}

	public void reset() {

		for (ScorePair p : listScore) {
			p.setScore(0);
		}

		isSorted = true;
	}

	public int inc(Candidate c) {
		return inc(c, 1);
	}

	public int inc(Candidate c, int v) {
		ScorePair p = mapScore.get(c);
		return p.incScore(v);
	}

	public void set(Candidate c, int v) {
		mapScore.get(c).setScore(v);
	}
	
	
	/*
	public void add(ScorePair p) {
		listScore.add(p);
	}
	
	public void add(Candidate c, int iScore) {
		listScore.add(new ScorePair(c, iScore));
	}
	

	public ScorePair get(int index) {
		return listScore.get(index);
	}
	
	*/
	
	//------------------------------------------------------------------------------
	
	public final Candidate getFirstCandidate() {
		return listScore.get(0).getCandidate();
	}

	public final Candidate getLastCandidate() {
		return listScore.get(listScore.size()-1).getCandidate();
	}

	public final Candidate getCandidate(int index) {
		return listScore.get(index).getCandidate();
	}
	
	
	public final int getFirstScore() {
		return listScore.get(0).getScore();
	}

	public final int getLastScore() {
		return listScore.get(listScore.size()-1).getScore();
	}

	public final int getScore(int index) {
		return listScore.get(index).getScore();
	}

	public final int getScore(Candidate c) {
		return mapScore.get(c).getScore();
	}
	
	//------------------------------------------------------------------------------
	
	
	
	public final int getMinScore() {
		if (isSorted) {
			return getLastScore();
		} else {
			return Collections.min(listScore).getScore();
		}
	}

	
	public final int getMaxScore() {
		if (isSorted) {
			return getFirstScore();
		} else {
			return Collections.max(listScore).getScore();
		}
	}


	public final CandidateList getWinners() {

		//PREPARE WINNERS LIST
		CandidateList listWinners = new CandidateList();
		
		// find the max score (most voted) 
		int max_score = getMaxScore();
		
		//fill the winners list
		for (int i=0; i<size(); i++) {
			if (getScore(i) == max_score) {
				listWinners.add(getCandidate(i));
			} else {
				if (isSorted) break;
			}
		}
		
		return listWinners;

	}
	

	public final Candidate getWinner(EnumTieBreakType iTieBreakMode) {

		return getWinners().getTieBreakPreferred(iTieBreakMode);

	}
	
	
	/*
	 
	//------------------------------------------------------------------------------
	
	

	public Candidate getTieBreakPreferred(EnumTieBreakType iTieBreakMode) {
		if (iTieBreakMode == EnumTieBreakType.lexic) {
			return this.getMinId();
		} else {
			return this.getRnd();
		}
	}

	public Candidate getTieBreakRejected(EnumTieBreakType iTieBreakMode) {
		if (iTieBreakMode == EnumTieBreakType.lexic) {
			return this.getMaxId();
		} else {
			return this.getRnd();
		}
	}
	
	*/
	
	//------------------------------------------------------------------------------
	
}
