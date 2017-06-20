package model.voting;


public class Candidate implements Comparable<Candidate> {
	

	//public static final Comparator<Candidate> comparator = Comparator.comparing(Candidate::getId);
	
	
	public final int id;
	
	
	public Candidate(int id) {
		this.id = id;
	}

	
	
	public int getId() {
		return id;
	}
	
		
	
    /**
    * Compares this pair of elements to the specified pair.
    */
	@Override
	public int compareTo(Candidate comp) {
		//a negative integer, zero, or a positive integer as this Object is less than, equal to, or greater than the given Object.
		if (this.id < comp.id) {
			return -1;
		} else if (this.id > comp.id) {
			return +1;
		} else {
			return 0;
		}
	}
	
	

    /**
    * Check if this pair of elements is equal to the specified object.
    */
    @Override
	public boolean equals(Object comp) {
        if (comp instanceof Candidate) {
        	return (this.id == ((Candidate)comp).id);
        } else {
            return false;
        }
    }


    /**
    * Hash code for this pair of elements.
    */
    @Override
    public int hashCode() {
        return this.id;
    }
	
	

}
