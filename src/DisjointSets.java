public interface DisjointSets<E> {

	/**
	 * Creates a new set {data}. No other elements will be in this new set.
	 * @param data != null, cannot already be in a set
	 */
	public void makeSet(E data);

	/**
	 * Joins two elements and their sets together.
	 * Replaces the set d1 is a part of and the set d2 is a part of with
	 * a single set which is the union of the two previous sets.
	 * @param d1 must already be in a set
	 * @param d2 must already be in a set
	 * After this operation, find(d1) == find(d2)
	 */
	public void union(E d1, E d2);

	/**
	 * Find which set "data" is a part of. Will traverse the set tree
	 * until it finds the root and returns the unique id of the root.
	 * @param data must already be in a set
	 * @return A unique int identifer for the set "data" is a part of
	 * find(x) == find(y) iff x and y are part of the same set
	 */
	public int find(E data);

	public double getAverageDepth();

	public int getNumberOfSets();

	public boolean sameSet(E d1, E d2);
}
