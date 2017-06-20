package model.voting;

import java.util.ArrayList;
import java.util.Collection;


public class VoterList extends ArrayList<Voter> {

	private static final long serialVersionUID = 1L;

	public VoterList() {
		super();
	}
	
	public VoterList(int iInitialCapacity) {
		super(iInitialCapacity);
	}

	public VoterList(Collection<? extends Voter> c) {
		super(c);
	}
	
}
