package model.voting;



public class ElectionSeriesData {

	
	//data description

	public final int iNumRuns;
	public final int iNumVoters;
	public final int iNumCandidates;
	//public final int iNumIterations;

	public final int iNumElectionRules;
	
	
	//aggregate data for each rule

	int agg_condorcet_winner[];
	
	double agg_copeland_score[];
	int agg_copeland_winner[];
	
	double agg_borda_score[];
	double agg_borda_ratio[];
	double agg_borda_normscore[];
	int agg_borda_winner[];
	
	double agg_usw_score[];
	int agg_usw_winner[];
	
	double agg_esw_score[];
	int agg_esw_winner[];

	int agg_condorcet_existence;

	
	
	//constructor
	public ElectionSeriesData(int iNumRuns,	int iNumVoters,	int iNumCandidates,	/*int iNumIterations,*/	int iNumElectionRules) {

		
		this.iNumRuns = iNumRuns;
		this.iNumVoters = iNumVoters;
		this.iNumCandidates = iNumCandidates;
		//this.iNumIterations = iNumIterations;

		this.iNumElectionRules = iNumElectionRules;
		
		agg_condorcet_existence = 0;
		
				
		agg_condorcet_winner = new int[iNumElectionRules];

		//agg_copeland_score = new double[n];
		//agg_copeland_winner = new int[n];

		//agg_borda_max_score = new int[n];
		agg_borda_score = new double[iNumElectionRules];
		agg_borda_normscore = new double[iNumElectionRules];
		agg_borda_ratio = new double[iNumElectionRules];
		agg_borda_winner = new int[iNumElectionRules];

		//agg_usw_max_score = new double[n];
		agg_usw_score = new double[iNumElectionRules];
		agg_usw_winner = new int[iNumElectionRules];

		//agg_esw_max_score = new double[iNumElectionRules];
		agg_esw_score = new double[iNumElectionRules];
		agg_esw_winner = new int[iNumElectionRules];
		
		for (int i=0; i<iNumElectionRules; i++ ) {

			agg_condorcet_winner[i] = 0;

			agg_borda_score[i] = 0;
			agg_borda_normscore[i] = 0;
			agg_borda_ratio[i] = 0;
			agg_borda_winner[i] = 0;

			agg_usw_score[i] = 0;
			agg_usw_winner[i] = 0;

			agg_esw_score[i] = 0;
			agg_esw_winner[i] = 0;
		}
		
		
	}

	
	
	//reset data
	public void reset() {

		agg_condorcet_existence = 0;
		
	}
	
	
	//AGREGATE RESULTS
	public void agregate(Candidate[] winner, Scenario scenario) {
		
		for (int i=0; i<iNumElectionRules; i++ ) {
		
			//USW SCORE
			agg_usw_score[i] += scenario.adUSW[winner[i].id] ;   //USW score of winner
		
			//USW SCORE RATE
			agg_usw_winner[i] += ( (scenario.listUSWwinners.contains(winner[i])) ? 1 : 0 );   //Number of times a USW winner is elected
			
			
			//ESW SCORE
			agg_esw_score[i] += scenario.adESW[winner[i].id] ;   //ESW score of winner
		
			//ESW EFFICIENCY
			agg_esw_winner[i] += ( (scenario.listESWwinners.contains(winner[i])) ? 1 : 0 );   //Number of times a ESW winner is elected
		
			
			//BORDA
			
			agg_borda_score[i] += scenario.aiBordaScore[winner[i].id];   //Borda score of winner
			agg_borda_ratio[i] += (1.0*scenario.aiBordaScore[winner[i].id]) / scenario.iMaxBordaScore;   //Borda ratio of winner
			agg_borda_normscore[i] += (1.0*scenario.aiBordaScore[winner[i].id]) / (iNumVoters * (iNumCandidates-1));   //Borda score normalized of winner
			
			agg_borda_winner[i] += ( (scenario.listBordaWinners.contains(winner[i])) ? 1 : 0 );   //Number of times a Borda winner is elected
			
			
			//CONDORCET
			
			agg_condorcet_winner[i] += ( (scenario.candCondorcetWinner == winner[i]) ? 1 : 0 );   //Number of times a Condorcet winner is elected
	

		}
		
	}



	public double getCondorcetEfficiency(int i) {
		return 1.0 * agg_condorcet_winner[i] / agg_condorcet_existence;
	}

	public double getAverageBordaScore(int i) {
		return 1.0 * agg_borda_score[i] / iNumRuns;
	}

	public double getNormalizedBordaScore(int i) {
		return 1.0 * agg_borda_normscore[i] / iNumRuns;
	}

	public double getRatioBordaScore(int i) {
		return 1.0 * agg_borda_ratio[i] / iNumRuns;
	}

	public double getBordaEfficiency(int i) {
		return 1.0 * agg_borda_winner[i] / iNumRuns;
	}

	public double getEgalitarianEfficiency(int i) {
		return 1.0 * agg_esw_score[i] / iNumRuns;
	}

	public double getUtilitarianEfficiency(int i) {
		return 1.0 * agg_usw_score[i] / iNumRuns;
	}

		
}
