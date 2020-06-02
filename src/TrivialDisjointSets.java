import java.util.*;

public class TrivialDisjointSets<E> implements DisjointSets<E> {

	private HashMap<E, SetElement<E>> elements;
	private SetIDGenerator idGen;
	private int numSets;

	public TrivialDisjointSets() {
		elements = new HashMap<>();
		idGen = new SetIDGenerator();
		numSets = 0;
	}

	public TrivialDisjointSets(Collection<E> objs) {
		this();

		for (E x : objs) {
			makeSet(x);
		}
	}

	/**
	 * Creates a new set {data}. No other elements will be in this new set.
	 * @param data != null, cannot already be in a set
	 */
	public void makeSet(E data) {
		if (data == null) {
			throw new IllegalArgumentException("Cannot add null elements to a set.");
		} else if (elements.containsKey(data)) {
			throw new IllegalArgumentException("Data element is already in a set.");
		}
		elements.put(data, new SetElement<E>(data, idGen.getNextID()));
		numSets++;
	}

	/**
	 * Joins two elements and their sets together.
	 * Replaces the set d1 is a part of and the set d2 is a part of with
	 * a single set which is the union of the two previous sets.
	 * @param d1 must already be in a set
	 * @param d2 must already be in a set
	 * After this operation, find(d1) == find(d2)
	 */
	public void union(E d1, E d2) {
		if (!elements.containsKey(d1) || !elements.containsKey(d2)) {
			throw new IllegalArgumentException("Both elements must already be in a set.");
		}
		if (getRoot(elements.get(d1)) == getRoot(elements.get(d2))) {
			throw new IllegalStateException("Both elements are already in the same set.");
		}
		SetElement<E> set1 = getRoot(elements.get(d1));
		set1.parent = elements.get(d2);
		numSets--;
	}

	/**
	 * Find which set "data" is a part of. Will traverse the set tree
	 * until it finds the root and returns the unique id of the root.
	 * @param data must already be in a set
	 * @return A unique int identifer for the set "data" is a part of
	 * find(x) == find(y) iff x and y are part of the same set
	 */
	public int find(E data) {
		if (!elements.containsKey(data)) {
			return -1;
		}
		SetElement<E> root = getRoot(elements.get(data));
		return root.id;
	}

	private SetElement<E> getRoot(SetElement<E> curr) {
		while (curr.parent != null) {
			curr = curr.parent;
		}
		return curr;
	}

	public boolean sameSet(E d1, E d2) {
		return find(d1) == find(d2);
	}

	public int getNumberOfSets() {
		return numSets;
	}

	public double getAverageDepth() {
		if (elements.size() == 0) {
			return 0;
		}
		double sum = 0;
		for (SetElement<E> s : elements.values()) {
			while (s.parent != null) {
				s = s.parent;
				sum++;
			}
		}
		return sum / elements.size();
	}

	private static class SetElement<E> {
		private E data;
		private int id;
		private SetElement<E> parent;

		public SetElement(E data, int setID) {
			this.data = data;
			this.id = setID;
			this.parent = null;
		}

		public String toString() {
			return "{" + data + ", " + parent + "}";
		}
	}

	private class SetIDGenerator {
		int currID;
		private int getNextID() {
			return currID++;
		}
	}
}
