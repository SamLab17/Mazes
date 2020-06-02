import java.awt.image.*;
import javax.imageio.*;
import java.io.File;
import java.util.Scanner;

public class MazeTester {
	public static void main(String[] args) {
		//timeTests();
		interactive();
	}

	public static void interactive() {
		Scanner kb = new Scanner(System.in);
		System.out.print("Maze width: ");
		int width = kb.nextInt();
		System.out.print("Maze height: ");
		int height = kb.nextInt();
		Maze m = new Maze(width, height);
		RenderedImage img = m.getMazeImage();
		try {
			ImageIO.write(img, "bmp", new File("out.bmp"));
		} catch (Exception e) {
			System.out.println("Failed to write image.");
			e.printStackTrace();
		}
		System.out.println("Maze generated.");
		kb.close();
	}

	public static void timeTests() {
		Stopwatch s = new Stopwatch();
		int numTrials = 1;
		for (int N = 100; N < 5_000; N *= 2) {
			double times = 0;
			for (int trial = 0; trial < numTrials; trial++) {
				s.start();
				Maze m = new Maze(N, N);
				s.stop();
				times += s.time();
			}
			double avgTime = times / numTrials;
			System.out.println(N + "x" + N + ": " + avgTime + "s");
		}
	}
}
