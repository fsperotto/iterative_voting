package model.learning;

public enum EnumExplorationMethod {

	greedy,
	epsilon_greedy_random,
	epsilon_greedy_random_sigmoid_decay,
	epsilon_greedy_random_exp_decay,
	epsilon_greedy_random_linear_decay,
	epsilon_greedy_pref,
	epsilon_greedy_pref_sigmoid_decay,
	epsilon_greedy_pref_exp_decay,
	epsilon_greedy_pref_linear_decay,
	softmax_boltzmann,
	softmax_boltzmann_adaptive,
	ucb1;
	
}
