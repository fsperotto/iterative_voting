package model.voting;

import java.util.Random;

import util.Permutations_Generator;

public class PreferenceGenerator_UrnModel {

	
	//random rnd
	protected static final Random rnd = new Random();

	
	
	
    private static int factorial(int n) {
        int fact = 1; // this  will be the result
        for (int i = 1; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }
    
    
	public static VoterList doDefinePreferences(CandidateList listCandidates, int iNumVoters, double dCorrelation, EnumUtilityGenerationMode eUtilityGenerationMode) {
		
		/*
		//creates the urn
		//and initially put one ballot for each possibility
		//VoterList listUrn = createAllPermutations(listCandidates);
		VoterList listUrn = createAllPermutationsUsingSwaping(listCandidates);

		int iNumCopies = (int) ((1.0*factorial(listCandidates.size())) /   ((1.0 / dCorrelation)-1)   );
		
		//for each voter to define
		for (Voter v : listVoters) {
			
			//take a preference from the urn at random
			Voter p = listUrn.get(rnd.nextInt(listUrn.size()));
			
			//copy such preference to the voter
			v.copyPreferencesFrom(p);
			
			//create n copies of the preference
			for (int i=0; i<iNumCopies; i++) {
				listUrn.add(p);
			}
			
		}
		*/

		VoterList listVoters = new VoterList(iNumVoters);
		
		double dFactor = (1.0 / dCorrelation) - 1.0;		

		//for each voter to define
		for (int i=0; i<iNumVoters; i++) {
			
			Voter v;

			//CREATE A NEW RANDOM VOTER
			if ( rnd.nextDouble() < (dFactor / (dFactor + listVoters.size())) ) {

				v = new Voter(listCandidates, eUtilityGenerationMode);
				
			//CREATE A COPY FROM AN ALREADY DEFINED VOTER
			} else {

				v = new Voter(listVoters.get(rnd.nextInt(listVoters.size())));
				
			}
			
			listVoters.add(v);
		}
		
		
		return listVoters;
		
	}
	
	
	
	
	//-------------------------------------------------------------------------------------------

	
	private static VoterList createAllPermutationsUsingSwaping(CandidateList listCandidates) { 

		VoterList listVoters = new VoterList();
		
		int iNumCandidates = listCandidates.size();
		int iNumPermutations = factorial(iNumCandidates);
		
		
		System.out.println(iNumPermutations);
		
		Candidate[] order = new Candidate[iNumCandidates];
		for (int i= 0; i<iNumCandidates; i++) {
			order[i] = listCandidates.get(i);
		}
		
		Permutations_Generator<Candidate> pg = new Permutations_Generator<Candidate>(order);

		for (int p=0; p<iNumPermutations; p++) {
		
			Voter v = new Voter(listCandidates, EnumUtilityGenerationMode.None);
		
			//for each position i
			for (int i=0; i<iNumCandidates; i++) {
				
				//get the candidate given by the order
				Candidate c = order[i];
				
				//set the value of each candidate in a linear way
				//v.mapValue.put(c, ((double)(iNumCandidates-i-1)) / (iNumCandidates-1));
				
				//set the value of each candidate in an exponential way
				v.mapValue.put(c, 1.0 / Math.pow(2.0, i) );
				
				//set the candidate to the preference table in order
				v.listPreference.get(i).add(c);
				//preference[i] = c;
				
				//set the position to the candidate table
				v.mapPosition.put(c, i);
				
			}
			
			listVoters.add(v);
			
			order = pg.next();
			
			//System.out.print(p + " ");


		}
		
	    
		return listVoters;
	    
	}
	

	
	
	
	private static VoterList createAllPermutations(CandidateList listCandidates) { 
		
		CandidateList listEmptyCandidates = new CandidateList();
		CandidateList listRemainingCandidates = new CandidateList(listCandidates);
		
		VoterList listVoters = new VoterList();
		
		createNextPermutationSet(listVoters, listEmptyCandidates, listRemainingCandidates); 
	    
		return listVoters;
	    
	}

	private static void createNextPermutationSet(VoterList listVoters, CandidateList listDefinedCandidates, CandidateList listRemainingCandidates) {
	    
		Voter v = null;
		
		if (listRemainingCandidates.size() == 0) {
			
			//v = new Voter(listDefinedCandidates, EnumUtilityGenerationMode.GivenOrderLinear);
			v = new Voter(listDefinedCandidates, EnumUtilityGenerationMode.GivenOrderExponential);
			listVoters.add(v);
	    
		} else {
	    
			for (int i=0; i<listRemainingCandidates.size(); i++) {
				
				CandidateList listInternalDefinedCandidates = new CandidateList(listDefinedCandidates);
				CandidateList listInternalRemainingCandidates = new CandidateList(listRemainingCandidates);			
	        	
				listInternalDefinedCandidates.add(listInternalRemainingCandidates.remove(i));
	        	
				createNextPermutationSet(listVoters, listInternalDefinedCandidates, listInternalRemainingCandidates);
	        }
			
	    }
		
	}
	
	
}
	
	
