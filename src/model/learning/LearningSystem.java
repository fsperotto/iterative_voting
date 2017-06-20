package model.learning;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Random;

import model.voting.Candidate;
import model.voting.CandidateList;
import model.voting.EnumTieBreakType;
import model.voting.ScoreList;
import model.voting.Voter;

public class LearningSystem  {

	protected static Random rnd = new Random();

	protected final Voter voter;
	
	protected final int iNumActions;
	protected final CandidateList listCandidates;
	
	
	//private double[] Q;
	protected HashMap<Candidate, Double> mapQ;
	
	//current iteration
	protected int iIteration = 0;

	
	//exploration method
	protected EnumExplorationMethod eExplorationMethod = EnumExplorationMethod.greedy;
	
	//initialize q using preference
	protected EnumInitUtilityMethod eInitUtilityMethod = EnumInitUtilityMethod.PreferenceValue;

	//tie breaking
	protected EnumTieBreakType iTieBreakMode = EnumTieBreakType.lexic; 

	//learning parameters
	protected double dAlpha = 0.1;
	//protected double dGamma = 0.9;
	
	//for Epsilon exploration
	protected double dEpsilon = 0.1;

	//for adaptive decay
	protected int iTargetIterations = 2000;
	protected double dPureExplorationFinishTime = 0.05;
	protected double dPureExploitationStartTime = 0.1;
	
	protected int iPureExplorationFinishTime = (int) (dPureExplorationFinishTime * iTargetIterations);
	protected int iPureExploitationStartTime = (int) (dPureExploitationStartTime * iTargetIterations);
	
	//for Boltzmann exploration
	protected double dDeltaTemp = 0.995;
	protected double dInitTemp = 100.0;
	protected double dTemp = dInitTemp;

	//flags
	protected boolean bIsReseted = false;
	
	//ucb
	protected ScoreList listNumberTrials;
	protected CandidateList listNotTried;
	protected HashMap<Candidate, Double> mapMean;
	protected HashMap<Candidate, Double> mapUCB;
	
	
	
	//CONSTRUCTOR
	public LearningSystem(Voter v) {
		
		this.voter = v;
		
		this.listCandidates = v.getListCandidates();
		
		iNumActions = listCandidates.size();
		
		//Q = new double[iNumActions];
		mapQ = new HashMap<Candidate, Double>(iNumActions);

		//ucb
		listNumberTrials = new ScoreList(listCandidates, iTieBreakMode);
		listNumberTrials.reset();
		listNotTried = new CandidateList(listCandidates);
		mapMean = new HashMap<Candidate, Double>(iNumActions);
		mapUCB = new HashMap<Candidate, Double>(iNumActions);
		
		bIsReseted = false;
		
	}
	

	
	//COPY CONSTRUCTOR
	public LearningSystem(Voter v, LearningSystem s) {
		
		this.voter = v;
		
		this.listCandidates = v.getListCandidates();
		
		iNumActions = listCandidates.size();
		
		mapQ = new HashMap<Candidate, Double>(s.mapQ);
		
		bIsReseted = s.bIsReseted;

		iIteration = s.iIteration;
		
		//exploration method
		eExplorationMethod = s.eExplorationMethod;
		
		//initialize q using preference
		eInitUtilityMethod = s.eInitUtilityMethod;

		//tie breaking
		iTieBreakMode = s.iTieBreakMode; 

		//learning parameters
		dAlpha = s.dAlpha;
		//dGamma = s.dGamma;
		
		//for Epsilon exploration
		dEpsilon = s.dEpsilon;

		//for adaptive decay
		iTargetIterations = s.iTargetIterations;
		dPureExplorationFinishTime = s.dPureExplorationFinishTime;
		dPureExploitationStartTime = s.dPureExploitationStartTime;
		
		iPureExplorationFinishTime = s.iPureExplorationFinishTime;
		iPureExploitationStartTime = s.iPureExploitationStartTime;
		
		//for Boltzmann exploration
		dDeltaTemp = s.dDeltaTemp;
		dInitTemp = s.dInitTemp;
		dTemp = s.dTemp;
		
		//for ucb
		listNumberTrials = new ScoreList(listCandidates, iTieBreakMode);
		listNotTried = new CandidateList(s.listNotTried);
		mapMean = new HashMap<Candidate, Double>(s.mapMean);
		mapUCB = new HashMap<Candidate, Double>(s.mapUCB);
		
	}
	
	
	
	
	//exploration method
	public void setExplorationMethod(EnumExplorationMethod eExplorationMethod) {
		this.eExplorationMethod = eExplorationMethod;
	}
	
	//initialize q using preference
	public void setInitUtilityMethod(EnumInitUtilityMethod eInitUtilityMethod) {

		this.eInitUtilityMethod = eInitUtilityMethod;
		
		bIsReseted = false;
		
	}
	

	//tie breaking
	public void setTieBreakMode(EnumTieBreakType iTieBreakMode) {
		this.iTieBreakMode = iTieBreakMode;
	}

	//learning parameters
	public void setAlpha(double dNewValue) {
		dAlpha = dNewValue;
	}

	//public void setGamma(double dNewValue) {
	//	dGamma = dNewValue;
	//}
	
	//for Epsilon exploration
	public void setEpsilon(double dNewValue) {
		dEpsilon = dNewValue;
	}

	//for adaptive decay
	public void setTargetIterations(int iNewValue) {
		iTargetIterations = iNewValue;
	}

	public void setPureExplorationFinishTime(double dNewValue) {
		dPureExplorationFinishTime = dNewValue;
	}

	public void setPureExploitationStartTime(double dNewValue) {
		dPureExploitationStartTime = dNewValue;
	}

	
	//for Boltzmann exploration
	public void setDeltaTemp(double dNewValue) {
		dDeltaTemp = dNewValue;
	}

	public void setInitTemp(double dNewValue) {
		dInitTemp = dNewValue;
	}
	
	
	
	
	
	
	private static double sigmoid(double t, double tmin, double tmax) {
	    
	    //return 1.0 - (1.0 / (1.0 + 100.0 * Math.pow(10.0, -4.0/(tmax-tmin)*(v-tmin))));
	    
	    
	    //lower than 4 approximates linear function
		//bigger than 4 approximates hard sigmoid 
		double dModif = 4.0;   
	    
	    return 1.0 - (1.0 / (1.0 + Math.exp(-dModif/(tmax-tmin)*(t-((tmax-tmin)/2)-tmin))));    
	}

	private static double invexp(double t, double tmin, double tmax) {
		if (t < tmin) {
			return 1.0;
		} else if (t > tmax) {
			return 0.0;
		} else {
			return Math.exp(-t);
		}
	}
	
	private static double linear(double t, double tmin, double tmax) {
		if (t < tmin) {
			return 1.0;
		} else if (t > tmax) {
			return 0.0;
		} else {
			return 1.0 - ((t-tmin)/(tmax-tmin));
		}
	}

	
	
	
	public void reset() {


		iIteration = 0;
		
		
		bIsReseted = true;
		
		
		iPureExplorationFinishTime = (int) (dPureExplorationFinishTime * iTargetIterations);
		iPureExploitationStartTime = (int) (dPureExploitationStartTime * iTargetIterations);
		
		
		switch (eExplorationMethod) {
		
			case softmax_boltzmann_adaptive: {
		
				double dMaxUtility = voter.getValue(voter.getMostPreferred());
				
				double dParamAdapt = 10.0;  //bigger is harder decreasing
				
				dInitTemp = 100.0 * dMaxUtility;
				dDeltaTemp = (1.0*iTargetIterations - dParamAdapt) / iTargetIterations;
				
				break;
			}
		
			default: {
				
				break;
			}
			
		}

		
		//temperature
		dTemp = dInitTemp;

		
		
		//for (int a=0; a<iNumActions; a++) {
		for (Candidate c : listCandidates) {
			
			switch (eInitUtilityMethod) {
				
				case Zero:

					//reset to zero
					//Q[a] = 0.0;
					mapQ.put(c, 0.0);
					
					break;

				case OptimisticUtilityValue:

					//reset to +
					//mapQ.put(c, +1.0);
					mapQ.put(c, voter.getValue(voter.getMostPreferred()) );
					//mapQ.put(c, voter.getValue(voter.getMostPreferred()) / (1.0 - dGamma) );
					
					break;

				case Random:

					//reset to random
					//Q[a] = rnd.nextDouble();
					mapQ.put(c, rnd.nextDouble());
					
					break;

				case PreferenceBorda:

					//reset to borda equivalent
					mapQ.put(c, voter.getLinearValue(c) );
					//mapQ.put(c, voter.getLinearValue(c) / (1.0 - dGamma) );
					
					break;

				case PreferenceValue:

					//reset to utility
					mapQ.put(c, voter.getValue(c) );
					//mapQ.put(c, 3.0 * voter.getValue(c) );
					//mapQ.put(c, voter.getValue(c) / (1.0 - dGamma) );
					
					break;
					
				default:
					break;
					
			}
			

		}
		
	}
	
	
	

	public CandidateList getBestList(CandidateList listValidCandidates) {

		CandidateList listBest = new CandidateList();
		Double dBestValue = -Double.MAX_VALUE;

		for (Candidate c : mapQ.keySet()) {
		//for (Map.Entry<Candidate, Double> entry : mapQ.entrySet()) {
		    
			if (listValidCandidates.contains(c)) {
			
				double v = mapQ.get(c);
				
				if (v > dBestValue) {
		    
					listBest.clear();
			    	listBest.add(c);
			    	dBestValue = v;
		    
				} else if (v == dBestValue) {

					listBest.add(c);
				
				}
				
			}
			
		}		

		return listBest;
		
		/*
		double dBestQ = -Double.MAX_VALUE;
		int iAction = -1;
		
		for (int a=0; a<iNumActions; a++) {
			if (Q[a] > dBestQ) {
				dBestQ = Q[a];
				iAction = a;
			}
		}

		return iAction;
		*/
		
		
	}

	
	public CandidateList getBestListUCB(CandidateList listValidCandidates) {

		CandidateList listBest = new CandidateList();
		Double dBestValue = -Double.MAX_VALUE;

		for (Candidate c : mapUCB.keySet()) {
		    
			if (listValidCandidates.contains(c)) {
			
				double v = mapUCB.get(c);
				
				if (v > dBestValue) {
		    
					listBest.clear();
			    	listBest.add(c);
			    	dBestValue = v;
		    
				} else if (v == dBestValue) {

					listBest.add(c);
				
				}
				
			}
			
		}		

		return listBest;

	}
	
	public CandidateList getBestList() {
		
		return getBestList(listCandidates);
		
	}

	
	
	public Candidate getBest() {
		
		return getBest(listCandidates);
		
	}

	
	public Candidate getBest(CandidateList listValidCandidates) {
		
		return getBestList(listValidCandidates).getTieBreakPreferred(iTieBreakMode);
		
	}
	
	
	//----------------------------------------------------------------------
	
	public CandidateList getBestListUCB() {
		
		return getBestListUCB(listCandidates);
		
	}

	
	public Candidate getBestUCB() {
		
		return getBestUCB(listCandidates);
		
	}

	
	public Candidate getBestUCB(CandidateList listValidCandidates) {
		
		return getBestListUCB(listValidCandidates).getTieBreakPreferred(iTieBreakMode);
		
	}
	

	//----------------------------------------------------------------------
	
	
	
	
	public Candidate getVote(CandidateList listValidCandidates) {

		
		if (!bIsReseted) {
			System.out.println("Must reset learning system.");
		}
		
		
		
		Candidate vote = null;
		
		
		
		switch (eExplorationMethod) {
		
			case greedy: {

				// always return the action with best Q value
				vote = getBest(listValidCandidates);
				
				break;
			}	

			case epsilon_greedy_random_linear_decay:
			case epsilon_greedy_random_exp_decay:
			case epsilon_greedy_random_sigmoid_decay:
			case epsilon_greedy_random: {
			
				boolean bRandomMode;
				if (iIteration < iPureExplorationFinishTime) {
					bRandomMode = true;
				} else if (iIteration >= iPureExploitationStartTime) {
					bRandomMode = true;
				} else {
					bRandomMode = (rnd.nextDouble() < dEpsilon);					
				}
				
				if (bRandomMode) {
					//return some random action (uniform distribution draw)
					vote = listValidCandidates.getRnd();
				} else {
					// return the action with best Q value
					vote = getBest(listValidCandidates);
				} 
				
				break;
			}	



			case epsilon_greedy_pref_linear_decay: 
			case epsilon_greedy_pref_sigmoid_decay: 
			case epsilon_greedy_pref_exp_decay: 
			case epsilon_greedy_pref: {

				// this is epsilon greedy biased
				
				boolean bRandomMode;
				if (iIteration < iPureExplorationFinishTime) {
					bRandomMode = true;
				} else if (iIteration >= iPureExploitationStartTime) {
					bRandomMode = true;
				} else {
					bRandomMode = (rnd.nextDouble() < dEpsilon);					
				}
				
				if (bRandomMode) {
					//return some random action (distribution like preference draw)
					vote = voter.getPonderedRnd(listValidCandidates);
				} else {
					// return the action with best Q value
					vote = getBest(listValidCandidates);
				} 

				break;
			}	

			
			case ucb1: {
				
				
				if (listNotTried.isEmpty()) {
					vote = getBestUCB();
				} else {
					vote = listNotTried.getRnd();
				}
				
				break;
				
			}
			
		    //SoftMax (using Boltzmann/Gibbs distribution)
			case softmax_boltzmann_adaptive:
			case softmax_boltzmann: {
				
				//define probabilities
				double[] dBoltzProb = new double[listValidCandidates.size()];
				double dSumProb = 0.0;

				for (int i=0; i<listValidCandidates.size(); i++) {
					dBoltzProb[i] = 0.0;
				}
				
				double dMinTemp = 0.0000001;
				//double dMinTemp = 0.01;

				if (dTemp >= dMinTemp) {
				
					
					//calculate probabilities
					for (int i=0;i<listValidCandidates.size();i++) {
					//for (int i=0;i<num_actions;i++) {
					//for (Candidate cp : listValidCandidates) {
						//dBoltzProb[i] = Math.exp(Q[i] / temp);
						Candidate cp = listValidCandidates.get(i);
						dBoltzProb[i] = Math.exp(mapQ.get(cp) / dTemp);
						dSumProb += dBoltzProb[i];
					}
				
					
					//equivalent values, take the best possible
					if (dSumProb == 0.0) {
						
						//objChosenCandidate = getRnd(getBest(listValidCandidates));
						vote = getBest(listValidCandidates);
						
					//overflow, take the best	
					} else if (  Double.isInfinite(dSumProb) || Double.isNaN(dSumProb)  ) {
						
						//objChosenCandidate = getRnd(getBest(listValidCandidates));
						vote = getBest(listValidCandidates);
						
					//boltzmann
					} else {
	
						
						/*
						//normalize
						for (int i=0;i<num_actions;i++)
							dBoltzProb[i] /= dSumProb;
						
						//pick an action
						double p = rnd.nextDouble();
						*/
	
						//pick an action
						double p = rnd.nextDouble() * dSumProb;
						
						double dMatchProb = 0.0;
						
						for (int a=0; a<listValidCandidates.size(); a++) {
							dMatchProb += dBoltzProb[a];
							if (p < dMatchProb) {
								vote = listValidCandidates.get(a);
								break;
							}
						}
						
						/*
						while (dSumProb<p){
							dSumProb += dBoltzProb[act];
							act++;
							if (act==num_actions) {
								System.err.println("ERROR IN BOLTZMANN DECISION MAKING (ACTION INDEX AFTER LAST ACTION)");
								act = rnd.nextInt(num_actions);
								break;
							}
						}
						*/
						
						if (vote==null) {
							System.err.println("Error on Boltz.");
							//objChosenCandidate = getRnd(getBest(listValidCandidates));
							vote = getBest(listValidCandidates);
						}
						
					}
					
				//very low temperature, take the best
				} else {
					
					//objChosenCandidate = getRnd(getBest(listValidCandidates));
					vote = getBest(listValidCandidates);
					
				}
				
				break;
			}
			
				
			default: { 
				
				// should not be default!

				System.err.println("Undefined exploration method.");
				
			}
				
				
		}
		
		
		return vote;

	}


	
	public Candidate getVote() {
		
		return getVote(listCandidates);
		
	}
	
	
	

	public void update(Candidate vote, double reward) {
		
		
		//increment iteration counter
		iIteration++;

		
		//update variable parameters
		switch (eExplorationMethod) {
		
			case epsilon_greedy_random_exp_decay: 
			case epsilon_greedy_pref_exp_decay: {
				
				dEpsilon = invexp(iIteration, iPureExplorationFinishTime, iPureExploitationStartTime); 
	
				break;
			}	
	
			case epsilon_greedy_random_sigmoid_decay:
			case epsilon_greedy_pref_sigmoid_decay: {
				
				dEpsilon = sigmoid(iIteration, iPureExplorationFinishTime, iPureExploitationStartTime); 
				
				break;
			}
	
			case epsilon_greedy_random_linear_decay: 
			case epsilon_greedy_pref_linear_decay: {
				
				dEpsilon = linear(iIteration, iPureExplorationFinishTime, iPureExploitationStartTime); 
				
				break;
			}
			
			
			case softmax_boltzmann_adaptive: 
			case softmax_boltzmann: {
				
				//define temperature
				//dTemp *= dDeltaTemp;
				dTemp = dInitTemp * (Math.pow(dDeltaTemp, iIteration));
				
				break;
			}
			
			case ucb1: {

				int n;
				double dNewMean;
				
				if (listNotTried.remove(vote)) {

					n = 1;
					dNewMean = reward;
					
				} else {

					n = listNumberTrials.inc(vote);
					double dOldMean = mapMean.get(vote);

					dNewMean = dOldMean + ((reward-dOldMean) / n);
					//dNewMean = (mapMean.get(vote) * (n-1) / n) + (reward / n) ;
					
				};

				double dUCB = dNewMean + Math.sqrt(2.0 * Math.log(iIteration) / n);
				
				mapMean.put(vote, dNewMean);  
				mapUCB.put(vote, dUCB);
				
				break;
			}
				
			case greedy: 
			case epsilon_greedy_random: 
			case epsilon_greedy_pref: 
			default: {
				break;
			}
				
				
		}
		
		
		//update utility
		mapQ.put(vote, (dAlpha * reward) + ((1.0 - dAlpha) * mapQ.get(vote)) );

		//update utility
		//mapQ.put(vote, mapQ.get(vote) + (dAlpha * (reward - mapQ.get(vote)));

		
		
		
	}
	
	
	
	public void show(){
		
		DecimalFormat df = new DecimalFormat("#.##");
		
		System.out.print("Q: ");
		
		//for (int i=0;i<num_actions;i++)
		//	System.out.print(df.format(Q[i])+" ");

		for (Candidate c : listCandidates) {
			System.out.print(df.format(mapQ.get(c)) + " ");
		}
		
		System.out.println();
	}




}
