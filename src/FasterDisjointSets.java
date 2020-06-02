import java.util.*;

public class FasterDisjointSets<E> implements DisjointSets<E> {

	private HashMap<E, SetElement<E>> elements;
	private SetIDGenerator idGen;
	private int numSets;

	public FasterDisjointSets() {
		elements = new HashMap<>();
		idGen = new SetIDGenerator();
		numSets = 0;
	}

	public FasterDisjointSets(Collection<E> objs) {
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
		SetElement<E> root1 = getRoot(elements.get(d1));
		SetElement<E> root2 = getRoot(elements.get(d2));
		if (root1 == root2) {
			throw new IllegalStateException("Both elements are already in the same set.");
		}
		if (root1.rank > root2.rank) {
			root2.parent = root1;
		} else if (root1.rank < root2.rank) {
			root1.parent = root2;
		} else {
			if (Math.random() < 0.5) {
				SetElement<E> temp = root1;
				root1 = root2;
				root2 = temp;
			}
			root1.parent = root2;
			root2.rank++;
		}
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

	public boolean sameSet(E d1, E d2) {
		return find(d1) == find(d2);
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

	public int getNumberOfSets() {
		return numSets;
	}

	private SetElement<E> getRoot(SetElement<E> curr) {
		while (curr.parent != null) {
			SetElement<E> next = curr.parent;
			if (curr.parent.parent != null)
				curr.parent = curr.parent.parent;
			curr = next;
		}
		return curr;
	}

	private static class SetElement<E> {
		private E data;
		private int id;
		private int rank;
		private SetElement<E> parent;

		public SetElement(E data, int setID) {
			this.data = data;
			this.id = setID;
			this.parent = null;
			this.rank = 0;
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
