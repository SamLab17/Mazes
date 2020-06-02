import java.awt.image.BufferedImage;

public class Maze {

	private MazeCell[][] cells;

	private static int DIR_COUNT = 2;
	private static int NORTH = 0;
	private static int WEST = 1;
	private static int[][] DIR_VECTORS = { { -1, 0 }, { 0, -1 } };

	public Maze(int width, int height) {
		if (width < 2 || height < 2)
			throw new IllegalArgumentException("width and height must be >= 2");
		cells = new MazeCell[height][width];
		for (int r = 0; r < cells.length; r++) {
			for (int c = 0; c < cells[0].length; c++) {
				cells[r][c] = new MazeCell(r, c);
			}
		}
		generateMaze();
	}

	private void generateMaze() {
		DisjointSets<MazeCell> cellSets = new FasterDisjointSets<>();
		for (int r = 0; r < cells.length; r++)
			for (int c = 0; c < cells[0].length; c++)
				cellSets.makeSet(cells[r][c]);
		// Substitute these line for easier mazes
		//MazeCell start = cells[0][0];
		//MazeCell end = cells[cells.length - 1][cells[0].length - 1];
		//while (!cellSets.sameSet(start, end)) {
		while (cellSets.getNumberOfSets() > 1) {
			MazeCell randomCell = getRandomCell();
			int wallDir = (int)(Math.random() * DIR_COUNT);
			int neighborRow = randomCell.row + DIR_VECTORS[wallDir][0];
			int neighborCol = randomCell.col + DIR_VECTORS[wallDir][1];
			if (isInBounds(neighborRow, neighborCol)) {
				MazeCell neighbor = cells[neighborRow][neighborCol];
				if (!cellSets.sameSet(neighbor, randomCell)) {
					// The two cells are now a part of the same set of reachable cells
					cellSets.union(randomCell, neighbor);
					// Knock down the walls
					randomCell.walls[wallDir] = false;
				}
			}
		}
	}

	/**
	 * Create a bitmap image of the maze
	 * @return A BufferedImage which can then be written to an image
	 * file using ImageIO.write()
	 */
	public BufferedImage getMazeImage() {
		int IMAGE_WIDTH = cells[0].length * 2 + 1;
		int IMAGE_HEIGHT = cells.length * 2 + 1;
		int EMPTY_COLOR = 0xFFFFFF;
		int WALL_COLOR = 0;
		BufferedImage img = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_BYTE_BINARY);

		drawRect(img, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, EMPTY_COLOR);

		for (int r = 0; r < cells.length; r++) {
			for (int c = 0; c < cells[0].length; c++) {
				int x = c * 2;
				int y = r * 2;
				img.setRGB(x, y, WALL_COLOR);
				MazeCell cell = cells[r][c];
				if (cell.walls[NORTH])
					img.setRGB(x + 1, y, WALL_COLOR);
				if (cell.walls[WEST])
					img.setRGB(x, y + 1, WALL_COLOR);
			}
		}

		drawRect(img, IMAGE_WIDTH - 1, 0, 1, IMAGE_HEIGHT, WALL_COLOR);
		drawRect(img, 0, IMAGE_HEIGHT - 1, IMAGE_WIDTH, 1, WALL_COLOR);

		img.setRGB(0, 1, EMPTY_COLOR);
		img.setRGB(IMAGE_WIDTH - 1, IMAGE_HEIGHT - 2, EMPTY_COLOR);

		return img;
	}

	private static void drawRect(BufferedImage img, int x, int y, int width, int height, int color) {
		for (int currX = x; currX < x + width; currX++) {
			for (int currY = y; currY < y + height; currY++) {
				img.setRGB(currX, currY, color);
			}
		}
	}

	private boolean isInBounds(int r, int c) {
		return r >= 0 && r < cells.length && c >= 0 && c < cells[r].length;
	}

	private MazeCell getRandomCell() {
		int row = (int)(Math.random() * cells.length);
		int col = (int)(Math.random() * cells[row].length);
		return cells[row][col];
	}

	private static class MazeCell {
		private int row, col;
		private boolean walls[];

		public MazeCell(int row, int col) {
			this.row = row;
			this.col = col;
			walls = new boolean[2];
			for (int i = 0; i < walls.length; i++)
				walls[i] = true;
		}
	}
}
