public class Tester {
	public static void main(String[] args) {
		System.out.println("Trivial Disjoint Sets");
		basicTests(new TrivialDisjointSets<>());

		Stopwatch s = new Stopwatch();
		s.start();
		evenOddTests(new TrivialDisjointSets<>(), 50000);
		s.stop();
		System.out.println("Even/odd test: " + s.time() + " seconds");

		s.start();
		slowEvenOddTests(new TrivialDisjointSets<>(), 50000);
		s.stop();
		System.out.println("Slow Even/odd test: " + s.time() + " seconds");

		System.out.println("\nFaster Disjoint Sets");
		basicTests(new FasterDisjointSets<>());

		s.start();
		evenOddTests(new FasterDisjointSets<>(), 50000);
		s.stop();
		System.out.println("Even/odd test: " + s.time() + " seconds");

		s.start();
		slowEvenOddTests(new FasterDisjointSets<>(), 50000);
		s.stop();
		System.out.println("Slow Even/odd test: " + s.time() + " seconds");
	}

	public static void basicTests(DisjointSets<Integer> ds) {
		ds.makeSet(3);
		ds.makeSet(5);
		test(false, ds.find(3) == ds.find(5));
		test(-1, ds.find(2));
		test(false, ds.find(3) == -1);
		ds.union(3, 5);
		test(true, ds.find(3) == ds.find(5));
	}

	public static void evenOddTests(DisjointSets<Integer> evenOdd, int LIMIT) {
		for (int i = 0; i < LIMIT; i++) {
			evenOdd.makeSet(i);
		}
		for (int i = 2; i + 1 < LIMIT; i += 2) {
			//evenOdd.union(0, i);
			//evenOdd.union(1, i + 1);
			evenOdd.union(i, 0);
			evenOdd.union(i + 1, 1);
		}
		for (int i = 2; i < LIMIT; i += 2) {
			if (evenOdd.find(i) != evenOdd.find(i - 2)) {
				System.out.println(i + " was not in the even set");
			}
		}
		for (int i = 3; i < LIMIT; i += 2) {
			if (evenOdd.find(i) != evenOdd.find(i - 2)) {
				System.out.println(i + " was not in the odd set");
			}
		}
		System.out.println("Average depth: " + evenOdd.getAverageDepth());
	}

	public static void slowEvenOddTests(DisjointSets<Integer> evenOdd, int LIMIT) {
		for (int i = 0; i < LIMIT; i++) {
			evenOdd.makeSet(i);
		}
		for (int i = LIMIT - 1; i >= 2; i--) {
			evenOdd.union(i, i - 2);
		}
		for (int i = 2; i < LIMIT; i += 2) {
			if (evenOdd.find(i) != evenOdd.find(i - 2)) {
				System.out.println(i + " was not in the even set");
			}
		}
		for (int i = 3; i < LIMIT; i += 2) {
			if (evenOdd.find(i) != evenOdd.find(i - 2)) {
				System.out.println(i + " was not in the odd set");
			}
		}
		System.out.println("Average depth: " + evenOdd.getAverageDepth());
	}

	public static void test(Object expected, Object actual) {
		if ((expected == null) != (actual == null)) {
			System.out.println("Failed test, expected = " + expected + ", actual = " + actual);
		} else if (!expected.equals(actual)) {
			System.out.println("Failed test, expected = " + expected + ", actual = " + actual);
		} else {
			System.out.println("Passed test");
		}
	}
}
