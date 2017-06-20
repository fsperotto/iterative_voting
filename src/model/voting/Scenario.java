package model.voting;



public class Scenario {
	

	
	//CANDIDATES
	public CandidateList listCandidates = null;
	
	//VOTERS
	public VoterList listVoters = null;
	
	
	//number of candidates
	protected int iNumCandidates = 0;
	
	//number of voters
	protected int iNumVoters = 0;

	
	
	protected boolean bMustDefineCandidates = true;
	protected boolean bMustDefineVoters = true;
	protected boolean bMustReset = false;

	
	
	
	//CALCULATED PROPERTIES
	
	//CONDORCET
	Candidate candCondorcetWinner = null;
	
	//BORDA scores
	int[] aiBordaScore = null;
	int iMaxBordaScore;
	CandidateList listBordaWinners;
	
	//USW scores
	double[] adUSW = null;
	double dMaxUSW;
	CandidateList listUSWwinners;
	
	//ESW scores
	double[] adESW = null;
	double dMaxESW;
	CandidateList listESWwinners;

	
	
	//print console details
	public boolean verbose_mode = false;
	
	//print file details
	public boolean output_file = false;

	
	
	
	public Scenario() {
		
		listCandidates = null;
		listVoters = null;
		
		iNumCandidates = 0;
		iNumVoters = 0;
		
		bMustDefineCandidates = true;
		bMustDefineVoters = true;
		bMustReset = false;
		
	}
	
	
	public Scenario(CandidateList listCandidates) {
		
		super();
		
		setCandidates(listCandidates);
		
	}
	
	
	public Scenario(CandidateList listCandidates, VoterList listVoters) {
		
		this(listCandidates);
		
		setVoters(listVoters);
		
	}
	

	public void setCandidates(CandidateList listCandidates) {
		
		this.listCandidates = listCandidates;
		this.iNumCandidates = listCandidates.size();
		
		bMustReset = true;
		bMustDefineCandidates = false;

	}
	
	public void setVoters(VoterList listVoters) {
		
		this.listVoters = listVoters;
		this.iNumVoters = listVoters.size();

		bMustReset = true;
		bMustDefineVoters = false;
		
	}
	
	
	public void reset() {

		//undefined scenario
		if (bMustDefineCandidates || bMustDefineVoters) {
		
			System.err.println("Trying to reset an incomplete scenario.");
		
		//defined scenario
		} else {
			
			if (bMustReset) {
				
				bMustReset = false;
		
				candCondorcetWinner = null;
				
				listBordaWinners = new CandidateList(iNumCandidates);
				listUSWwinners = new CandidateList(iNumCandidates);
				listESWwinners = new CandidateList(iNumCandidates);
				
				aiBordaScore = new int[iNumCandidates];
				adUSW = new double[iNumCandidates];
				adESW = new double[iNumCandidates];
				
				computeBorda();
				computeCondorcet();
				computeESW();
				computeUSW();
				
			}
			
		}
		
	}
	
	public void reset(CandidateList listCandidates, VoterList listVoters) {

		setCandidates(listCandidates);
		setVoters(listVoters);
		reset();

	}

	public void reset(VoterList listVoters) {

		setVoters(listVoters);
		reset();

	}

	public void reset(CandidateList listCandidates) {

		setCandidates(listCandidates);
		reset();

	}
	

	//-------------------------------------------------------------------------------------


	public boolean isDefined() {
		return !(bMustDefineCandidates || bMustDefineVoters);
	}

	public boolean isReady() {
		return !(bMustReset);
	}
	
	//-------------------------------------------------------------------------------------
	
	
	//EGALITARIAN SOCIAL WELFARE (the voter less happy)
	protected void computeESW() {

		for (Candidate c : listCandidates) {
			adESW[c.id] = +Double.MAX_VALUE;
			for (Voter v: listVoters) {
				if (v.getValue(c) < adESW[c.id]) {
					adESW[c.id] = v.getValue(c);
				}
			}
		}
		
		dMaxESW = -Double.MAX_VALUE;
		for (Candidate c : listCandidates) {
			if (adESW[c.id] > dMaxESW) {
				dMaxESW = adESW[c.id];
			}
		}

		listESWwinners.clear();
		for (Candidate c : listCandidates) {
			if (adESW[c.id] == dMaxESW) {
				listESWwinners.add(c);
			}
		}

		if (verbose_mode) {
			System.out.println("ESW:");
			for (Candidate c : listCandidates) {
				System.out.println(c.id + " " + adESW[c.id]);
			}
		}
	}

	
	//UTILITARIAN SOCIAL WELFARE (happiness following utility)
	protected void computeUSW() {

		for (Candidate c : listCandidates) {
			adUSW[c.id] = 0.0;
			for (Voter v: listVoters) {
				adUSW[c.id] += v.getValue(c);
			}
		}

		for (Candidate c : listCandidates) {
			adUSW[c.id] /= iNumVoters;
		}
		
		dMaxUSW = -Double.MAX_VALUE;
		for (Candidate c : listCandidates) {
			if (adUSW[c.id] > dMaxUSW) {
				dMaxUSW=adUSW[c.id];
			}
		}

		listUSWwinners.clear();
		for (Candidate c : listCandidates) {
			if (adUSW[c.id] == dMaxUSW) {
				listUSWwinners.add(c);
			}
		}

		if (verbose_mode) {
			System.out.println("USW");
			for (int i=0;i<iNumCandidates;i++)
				System.out.println(i + " " + adUSW[i]);
		}
	}
	
	
	//BORDA SCORE (fairness following orders of preference)
	protected void computeBorda(){

		for (Candidate c : listCandidates) {
			aiBordaScore[c.id] = 0;
			for (Voter v: listVoters) {
				aiBordaScore[c.id] += v.getBordaValue(c);
			}
		}
		
		iMaxBordaScore = Integer.MIN_VALUE;
		for (int i=0; i<iNumCandidates; i++) {
			if (aiBordaScore[i] > iMaxBordaScore) {
				iMaxBordaScore=aiBordaScore[i];
			}
		}

		listBordaWinners.clear();
		for (Candidate c : listCandidates) {
			if (aiBordaScore[c.id] == iMaxBordaScore) {
				listBordaWinners.add(c);
			}
		}

		if (verbose_mode) {
			System.out.println("Borda Score");
			for (Candidate c : listCandidates) {
				System.out.println(c.id + " " + aiBordaScore[c.id]);
			}
		}
	}
	
	
	protected void computeCondorcet() {

		candCondorcetWinner = null;
		
		for (Candidate ci : listCandidates) {
			
			// check if candidate i is a Condorcet Winner
			int wins=0;
			
			for (Candidate cj : listCandidates) {

				int score=0;
				
				if (ci != cj) {
					
					for (Voter v: listVoters) {
						if (v.getPosition(ci) < v.getPosition(cj)) {
						//if (v.getBorda(i) > v.getBorda(j)) {
							score++;
						}
					}
					
					if (score > iNumVoters/2) {
						wins++;
					}
				}
			}
			
			if (wins == iNumCandidates-1) {
				candCondorcetWinner = ci;
				break;
			}
			
		}
	}
	
	
	//-------------------------------------------------------------------------------------
	
	
	
	
}
