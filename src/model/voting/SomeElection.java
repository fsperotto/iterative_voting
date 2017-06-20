package model.voting;
import java.util.Vector;


public interface SomeElection {

	public void outcome();
	public int getWinner();
	public int getScore(int candidato);
	public int getWinnerScore();
	public void printRisultati();
	public SomeElection getNew(Vector<Voter> profile);
	
	public double getAvgPos(int candidato);
	
	public void setClassifica();	
	public int getClassificato(int i);
	
	public void setProfile(Vector<Voter> p);

}
