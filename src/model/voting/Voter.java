package model.voting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import model.learning.LearningSystem;



public class Voter {
	
	
	//candidates
	private final CandidateList listCandidates;
	protected final int iNumCandidates;
	
	
	protected final EnumUtilityGenerationMode iPrefType;
	
	
	//random generator
	protected static final Random rnd = new Random();

	
	protected EnumTieBreakType iTieBreakMode;
	

	public final LearningSystem objLearningSystem;
	
	
	private Candidate vote = null;

	public Candidate ballot = null;

	
	//print details
	public boolean verbose_mode = false;
	

	// the array represents the value of each candidate
	public final Map<Candidate, Double> mapValue;
	
	//the array represents the order pref[0] > pref[1] > .... > pref[iNumCandidates-1] 
	public final List<CandidateList> listPreference;

	//the array represents the position of each candidate
	public final Map<Candidate, Integer> mapPosition;
	
	
	//------------------------------------------------------------------------------

	
	
	//CONSTRUCTOR
	public Voter(CandidateList listCandidates, EnumUtilityGenerationMode iPrefType) {
	
		
		this.listCandidates = listCandidates;
		this.iNumCandidates = listCandidates.size();
		
		
		this.iPrefType = iPrefType;

		
		//initialize value vector
		mapValue = new HashMap<Candidate, Double>(iNumCandidates);
		for (Candidate c : listCandidates) {
			mapValue.put(c, 0.0);
		}

		//initialize preference vector
		listPreference = new ArrayList<CandidateList>(iNumCandidates);
		for (int i=0; i<iNumCandidates; i++) {
			listPreference.add(new CandidateList());
		}

		//initialize position vector
		mapPosition = new HashMap<Candidate, Integer>(iNumCandidates);
		for (Candidate c : listCandidates) {
			mapPosition.put(c, 0);
		}
		
		// generate random preferences
		switch (iPrefType) {
			case RandomLinear:
			
				generatePreferences_Linear_UniformDistribution();
				break;

			case RandomExponential:
				
				generatePreferences_Exponential_UniformDistribution();
				break;

			case RandomSigmoid:
				
				generatePreferences_Sigmoid_UniformDistribution();
				break;
				
			case GivenOrderLinear:
				
				//for each position i
				for (int i=0; i<iNumCandidates; i++) {
					
					//get the selected candidate
					Candidate c = listCandidates.get(i);
					
					//set the value of each candidate in a linear way
					mapValue.put(c, ((double)(iNumCandidates-i-1)) / (iNumCandidates-1));
					
					//set the candidate to the preference table in order
					listPreference.get(i).add(c);
					//preference[i] = c;
					
					//set the position to the candidate table
					mapPosition.put(c, i);
					
				}
				
				break;
				
			case GivenOrderExponential:

				//for each position i
				for (int i=0; i<iNumCandidates; i++) {
					
					//get the selected candidate
					Candidate c = listCandidates.get(i);
					
					//set the value of each candidate in a linear way
					mapValue.put(c, 1.0 / Math.pow(2.0, i) );
					
					//set the candidate to the preference table in order
					listPreference.get(i).add(c);
					//preference[i] = c;
					
					//set the position to the candidate table
					mapPosition.put(c, i);
					
				}
				break;
				
			case None:
			default:
				
				break;
		}
		
		
		//learning system
		objLearningSystem = new LearningSystem(this);
		
		
		//default tie break mode
		setTieBreakMode(EnumTieBreakType.lexic);
		
		
	}
	

	
	//COPY CONSTRUCTOR
	public Voter(Voter v) {
		
		this.listCandidates = v.listCandidates;
		this.iNumCandidates = v.iNumCandidates;
		
		this.iPrefType = v.iPrefType;

		
		//initialize value vector
		mapValue = new HashMap<Candidate, Double>(v.mapValue);

		//initialize preference vector
		listPreference = new ArrayList<CandidateList>(v.listPreference);

		//initialize position vector
		mapPosition = new HashMap<Candidate, Integer>(v.mapPosition);

		iTieBreakMode = v.iTieBreakMode;
		
		//learning system
		objLearningSystem = new LearningSystem(this, v.objLearningSystem);
		
		
	}
	
	
	
	public void setTieBreakMode(EnumTieBreakType iTieBreakMode) {

		this.iTieBreakMode = iTieBreakMode;		
		
		objLearningSystem.setTieBreakMode(iTieBreakMode);
		
	}
	
	
	
	//------------------------------------------------------------------------------

	public Candidate getTieBreakPreferred(CandidateList listComparedCandidates) {
		return listComparedCandidates.getTieBreakPreferred(iTieBreakMode);
	}

	public Candidate getTieBreakRejected(CandidateList listComparedCandidates) {
		return listComparedCandidates.getTieBreakRejected(iTieBreakMode);
	}
	
	//------------------------------------------------------------------------------
	
	
	//Preferred in a given position (if tie, use tie break)
	public Candidate getPreferred(int iPosition) {
		return getTieBreakPreferred(getPreferredList(iPosition));
	}
	
	public Candidate getPreferred(int iPosition, CandidateList listValidCandidates) {
		return getTieBreakPreferred(getPreferredList(iPosition, listValidCandidates));
	}
	
	
	//The best (first position) (if tie, use tie break)
	public Candidate getMostPreferred() {
		return getTieBreakPreferred(getMostPreferredList());
	}

	public Candidate getMostPreferred(CandidateList listValidCandidates) {
		return getTieBreakPreferred(getMostPreferredList(listValidCandidates));
	}

	
	//The worst (last position) (if tie, use tie break)
	public Candidate getMostRejected() {
		return getTieBreakRejected(getMostRejectedList());
	}

	public Candidate getMostRejected(CandidateList listValidCandidates) {
		return getTieBreakRejected(getMostRejectedList(listValidCandidates));
	}
	
	
	
	//------------------------------------------------------------------------------
	

	/**
	 * Get the borda score of a candidate i
	 * @param i
	 * @return the Borda score of candidate i according to the preference of the agent
	 */
	public final int getBordaValue(Candidate objCandidate) {
		//return borda value of candidate i
		return iNumCandidates - getPosition(objCandidate) - 1;
	}
	
	
	public final double getLinearValue(Candidate objCandidate) {
		//return linear value of candidate i in [1.0, 0.0]
		return (double) getBordaValue(objCandidate) / (iNumCandidates-1)  ;
	}

	
	public final double getValue(Candidate objCandidate) {
		//return the utility value of candidate
		return mapValue.get(objCandidate);
	}
	
	//------------------------------------------------------------------------------
	
	protected final Candidate getRnd() {
		return listCandidates.getRnd();
	}
	
	protected final CandidateList getRndList() {
		CandidateList l = new CandidateList(1);
		l.add(getRnd());
		return l;
	}

	//------------------------------------------------------------------------------

	protected final Candidate getPonderedRnd() {
		return getPonderedRnd(listCandidates);
	}

	public final Candidate getPonderedRnd(CandidateList listValidCandidates) {
		
		//define probabilities
		double dSumProb = 0.0;

		//calculate probabilities
		for (Candidate cp : listValidCandidates) {
			dSumProb += getValue(cp);
		}
			
		//equivalent values or overflow
		if ( (dSumProb == 0.0) || Double.isInfinite(dSumProb) || Double.isNaN(dSumProb) ) {
				
			return getMostPreferred(listValidCandidates);
				
		//pondered random
		} else {

			double p = rnd.nextDouble() * dSumProb;
				
			double dMatchProb = 0.0;
				
			for (Candidate cp : listValidCandidates) {
				dMatchProb += getValue(cp);
				if (p < dMatchProb) {
					return cp;
				}
			}
			
		}
		
		//if not found, there is some error...
		System.err.println("Error on Boltz.");
		return getBest(listValidCandidates);
				
		
	}
	
	
	
	//------------------------------------------------------------------------------

	
	/*

	public final Candidate getLex() {
		return getLex(listCandidates);
	}
	
	//------------------------------------------------------------------------------
	*/
	
	protected void generatePreferences_Linear_UniformDistribution() {

		CandidateList listRemainingCandidates = new CandidateList(listCandidates);
		
		//for each position i
		for (int i=0; i<iNumCandidates; i++) {
			
			//choose an index from the remaining candidate list
			int j = rnd.nextInt(iNumCandidates-i);
			
			//get the selected candidate
			Candidate c = listRemainingCandidates.get(j);
			
			//set the value of each candidate in a linear way
			mapValue.put(c, ((double)(iNumCandidates-i-1)) / (iNumCandidates-1));
			
			//set the candidate to the preference table in order
			listPreference.get(i).add(c);
			//preference[i] = c;
			
			//set the position to the candidate table
			mapPosition.put(c, i);
			
			//remove the candidate of the remaining list
			listRemainingCandidates.remove(j);
		}

		
		if (verbose_mode) {
			
			System.out.print("pref: ");
			
			for (List<Candidate> listPosition : listPreference) {
				for (Candidate c : listPosition) {
					System.out.print(c.id + " ");
				}
			}

			System.out.print(" -> ");
			for (Candidate c : listCandidates) {
				System.out.print(mapPosition.get(c) + " ");
			}
			
			System.out.println();
			
		}
		
	}
	

	
	protected void generatePreferences_Sigmoid_UniformDistribution() {

		CandidateList listRemainingCandidates = new CandidateList(listCandidates);
		
		//for each position i
		for (int i=0; i<iNumCandidates; i++) {
			
			//choose an index from the remaining candidate list
			int j = rnd.nextInt(iNumCandidates-i);
			
			//get the selected candidate
			Candidate c = listRemainingCandidates.get(j);

			double dSigmoidParam = 10.0;
			double dMax = iNumCandidates-1;
			double dHalf = dMax / 2;
			double dPos = i;
			
			double dValue = 1.0-(1.0/(1.0+Math.exp(-dSigmoidParam/dMax*(dPos-dHalf))));
			
			//set the value of each candidate in a sigmoid way
			mapValue.put(c, dValue);
			
					
			//set the candidate to the preference table in order
			listPreference.get(i).add(c);
			//preference[i] = c;
			
			//set the position to the candidate table
			mapPosition.put(c, i);
			
			//remove the candidate of the remaining list
			listRemainingCandidates.remove(j);
		}

		
		if (verbose_mode) {
			
			System.out.print("pref: ");
			
			for (List<Candidate> listPosition : listPreference) {
				for (Candidate c : listPosition) {
					System.out.print(c.id + " ");
				}
			}

			System.out.print(" -> ");
			for (Candidate c : listCandidates) {
				System.out.print(mapPosition.get(c) + " ");
			}
			
			System.out.println();
			
		}
		
	}
	
	protected void generatePreferences_Exponential_UniformDistribution() {
		
		CandidateList listRemainingCandidates = new CandidateList(listCandidates);
		
		//for each position i
		for (int i=0; i<iNumCandidates; i++) {
			
			//choose an index from the remaining candidate list
			int j = rnd.nextInt(iNumCandidates-i);
			
			//get the selected candidate
			Candidate c = listRemainingCandidates.get(j);
			
			//set the value of each candidate in a exponential way
			mapValue.put(c, 1.0 / Math.pow(2.0, i) );
			//mapValue.put(c, ((double)(iNumCandidates-i-1)) / (iNumCandidates-1));
			
			//set the candidate to the preference table in order
			listPreference.get(i).add(c);
			//preference[i] = c;
			
			//set the position to the candidate table
			mapPosition.put(c, i);
			
			//remove the candidate of the remaining list
			listRemainingCandidates.remove(j);
		}

		
		if (verbose_mode) {
			
			System.out.print("pref: ");
			
			for (CandidateList listPosition : listPreference) {
				for (Candidate c : listPosition) {
					System.out.print(c.id + " ");
				}
			}

			System.out.print(" -> ");
			for (Candidate c : listCandidates) {
				System.out.print(mapPosition.get(c) + " ");
			}
			
			System.out.println();
			
		}
		
	}
	
	
	public void copyPreferencesFrom(Voter v) {
	
		//for each position
		for (int iPosition=0; iPosition<iNumCandidates; iPosition++) {
			
			//get the selected candidate
			Candidate c = v.getPreferred(iPosition);
			
			//set the value of each candidate
			mapValue.put(c, v.getValue(c));
			
			//set the candidate to the preference table in order
			listPreference.get(iPosition).add(c);
			
			//set the position to the candidate table
			mapPosition.put(c, iPosition);
			
		}
	
	}

	
	public int getPosition(Candidate objCandidate) {
		//return the position in order of preference of the candidate i 
		return mapPosition.get(objCandidate);
	}
	
	
	public CandidateList getPreferredList(int iPosition) {
		//return the i preferred candidate 
		return listPreference.get(iPosition);
	}

	
	public CandidateList getPreferredList(int iPosition, CandidateList listValidCandidates) {
		
		CandidateList listResult = new CandidateList();
		
		for (Candidate c : listPreference.get(iPosition)) {
			if (listValidCandidates.contains(c)) {
				listResult.add(c);
			}
		}
		
		return listResult;
		
	}

	
	
	
	
	//------------------------------------------------------------------------------
	
	
	//Vote
	public Candidate getVote() {
		
		vote = objLearningSystem.getVote();
		return vote;
	}
	
	
	public Candidate getVote(CandidateList listValidCandidates) {

		vote = objLearningSystem.getVote(listValidCandidates);
		return vote;
		
	}

	
	
	//------------------------------------------------------------------------------

	

	
	public CandidateList getBestList() {
		return objLearningSystem.getBestList();
	}

	
	public CandidateList getBestList(CandidateList listValidCandidates) {
		return objLearningSystem.getBestList(listValidCandidates);
	}


	public Candidate getBest() {
		return getTieBreakPreferred(getBestList());
	}

	
	public Candidate getBest(CandidateList listValidCandidates) {
		return getTieBreakPreferred(getBestList(listValidCandidates));
	}
	
	
	
	public CandidateList getMostPreferredList() {
		return listPreference.get(0);
	}

	
	public CandidateList getMostRejectedList() {
		return listPreference.get(iNumCandidates-1);
	}
	
	
	
	public CandidateList getMostPreferredList(CandidateList listValidCandidates) {
		
		CandidateList listPreferred = new CandidateList();
		
		for (int i=0; i<iNumCandidates; i++) {
				
			List<Candidate> listPosition = listPreference.get(i);
		
			for (Candidate c : listPosition) {
				if (listValidCandidates.contains(c)) {
					listPreferred.add(c);
				}
			}
			
			if (!listPreferred.isEmpty()) {
				break;
			}
		}
		
		return listPreferred;
	}
	

	public CandidateList getMostRejectedList(CandidateList listValidCandidates) {

		CandidateList listRejected = new CandidateList();
		
		for (int i=iNumCandidates-1; i>=0; i--) {
		
			CandidateList listPosition = listPreference.get(i);
			
			for (Candidate c : listPosition) {
				if (listValidCandidates.contains(c)) {
					listRejected.add(c);
				}
			}
			
			if (!listRejected.isEmpty()) {
				break;
			}
		}
		
		return listRejected;
		
	}
	

	
	//public void update(Candidate candWinner, int[] aiScore) {
	public void update(Candidate candWinner) {
		
		objLearningSystem.update(vote, getValue(candWinner));

	}
	
	
	
	public void show() {
		
		objLearningSystem.show();
		
	}



	/**
	 * @return the listCandidates
	 */
	public CandidateList getListCandidates() {
		return listCandidates;
	}




	
	
	
}
