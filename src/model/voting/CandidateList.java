package model.voting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;



public class CandidateList extends ArrayList<Candidate> {


	
	private static final long serialVersionUID = 1L;

	//random generator
	protected static final Random rnd = new Random();
	
	
	
	//------------------------------------------------------------------------------
	
	public CandidateList() {
		super();
	}
	
	public CandidateList(int iInitialCapacity) {
		super(iInitialCapacity);
	}

	public CandidateList(Collection<? extends Candidate> c) {
		super(c);
	}
	

	//------------------------------------------------------------------------------

	public final void sort() {
	
		//sort the score list lexicographically (from smallest id to greatest id)
		Collections.sort(this);
	
	}

	public final void reverse() {
		
		//reverse the current order
		Collections.reverse(this);
	
	}

	//------------------------------------------------------------------------------
	
	public final Candidate getFirst() {
		return get(0);
	}

	public final Candidate getLast() {
		return get(size()-1);
	}
	
	//------------------------------------------------------------------------------
	
	public final Candidate getRnd() {
		return get(rnd.nextInt(size()));
	}

	
	protected final CandidateList getRndList() {
		CandidateList l = new CandidateList(1);
		l.add(getRnd());
		return l;
	}
	

	//------------------------------------------------------------------------------
	
	public final Candidate getMinId() {
		return Collections.min(this);
	}

	
	public final Candidate getMaxId() {
		return Collections.max(this);
	}

	
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
	
	//------------------------------------------------------------------------------
	
	
	
}
