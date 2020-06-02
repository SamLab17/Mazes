import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
import java.util.*;

public class SolverTester {

	public static void main(String[] args) throws IOException {
		// Get Maze parameters
		Scanner kb = new Scanner(System.in);
		System.out.print("Maze width: ");
		int width = kb.nextInt();
		System.out.print("Maze height: ");
		int height = kb.nextInt();

		Stopwatch s = new Stopwatch();

		// Generate a Maze
		s.start();
		Maze m = new Maze(width, height);
		s.stop();
		System.out.println("Maze generated in " + s.time() + " seconds.");
		BufferedImage mazeImage = m.getMazeImage();
		saveImage(mazeImage, "maze.bmp");

		// Solve the Maze
		MazeSolver solver = new MazeSolver(mazeImage);
		s.start();
		BufferedImage solved = solver.solve();
		s.stop();
		System.out.println("Maze solved in " + s.time() + " seconds.");
		if (solved == null) {
			System.out.println("Failed to find a solution");
			return;
		}
		saveImage(solved, "maze_solved.bmp");
	}

	public static void saveImage(BufferedImage img, String name) throws IOException {
		ImageIO.write(img, "bmp", new File(name));
	}
}
