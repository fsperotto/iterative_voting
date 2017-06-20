package model.voting;

import java.util.AbstractMap.SimpleEntry;
import java.util.Random;




public class ScorePair implements Comparable<ScorePair> {


	//public static final Comparator<ScorePair> comparator = Comparator.comparing(ScorePair::getValue);

	private static final Random rnd = new Random();
	
	private final SimpleEntry<Candidate, Integer> entry;
	
	private final EnumTieBreakType iTieBreakMode;
	
	public ScorePair(Candidate key, Integer value, EnumTieBreakType iTieBreakMode) {
		this.entry = new SimpleEntry<Candidate, Integer>(key, value);
		this.iTieBreakMode = iTieBreakMode;
	}

	
	public int getScore() {
		return entry.getValue();
	}

	public void setScore(int iNewScore) {
		entry.setValue(iNewScore);
	}

	public int incScore(int v) {
		int iNewValue = getScore() + v;
		entry.setValue(iNewValue);
		return iNewValue;
	}

	public int incScore() {
		return incScore(+1);
	}
	
	public Candidate getCandidate() {
		return entry.getKey();
	}
	
    /**
    * Compares this pair of elements to the specified pair.
    */
	@Override
	public int compareTo(ScorePair comp) {
		//a negative integer, zero, or a positive integer as this Object is less than, equal to, or greater than the given Object.

		//initially use score
		if (getScore() < comp.getScore()) {
			return -1;

		} else if (getScore() > comp.getScore()) {
			return +1;
		
		//otherwise, use tie break
		} else {
			if (iTieBreakMode == EnumTieBreakType.lexic) {
				return getCandidate().compareTo(comp.getCandidate());
			} else {
				if (rnd.nextInt(2) == 0) {
					return +1;
				} else {
					return -1;
				}
			}
		}
	}
	
	

    /**
    * Check if this pair of elements is equal to the specified object.
    */
    @Override
    public boolean equals(Object comp) {
        if (comp instanceof ScorePair) {
        	if (getScore() == ((ScorePair)comp).getScore()) {
				return getCandidate().equals(((ScorePair)comp).getCandidate());
        	} else {
                return false;
        	}
        } else {
            return false;
        }
    }


    /**
    * Hash code for this pair of elements.
    */
    @Override
    public int hashCode() {
        return getScore();
    }


}
