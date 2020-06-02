import java.awt.image.*;
import java.awt.Graphics;

import java.util.*;

public class MazeSolver {

	private BufferedImage mazeImage;
	private static final int SOLVE_COLOR = 0xFFFF0000;
	private static final int EMPTY_COLOR = 0xFFFFFFFF;

	public MazeSolver(BufferedImage mazeImage) {
		this.mazeImage = mazeImage;
	}

	public BufferedImage solve() {
		int mazeWidth = mazeImage.getWidth();
		int mazeHeight = mazeImage.getHeight();
		CellPosition exitPosition = new CellPosition(mazeWidth - 1, mazeHeight - 2);
		Comparator<Cell> heuristic = new DistanceHeuristic(exitPosition);

		HashMap<CellPosition, Cell> cells = new HashMap<>();
		CellPosition startPosition = new CellPosition(0, 1);
		Cell start = new Cell(startPosition, 0);
		cells.put(startPosition, start);

		PriorityQueue<Cell> pq = new PriorityQueue<Cell>(heuristic);
		pq.add(start);

		while (!pq.isEmpty()) {
			Cell current = pq.remove();
			current.isInQueue = false;

			if (current.position.equals(exitPosition)) {
				// Found the exit
				return drawPath(current);
			}

			for (Cell neighbor : getNeighbors(current, cells)) {
				double newCostHere = current.costToHere + 1;
				if (newCostHere < neighbor.costToHere) {
					neighbor.previous = current;
					neighbor.costToHere = newCostHere;
					if (!neighbor.isInQueue) {
						neighbor.isInQueue = true;
						pq.add(neighbor);
					}
				}
			}
		}

		return null;
	}

	private ArrayList<Cell> getNeighbors(Cell current, HashMap<CellPosition, Cell> cells) {
		ArrayList<Cell> result = new ArrayList<Cell>();
		final int[][] DIR_VECTORS = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };
		int currX = current.position.x;
		int currY = current.position.y;
		for (int[] dir : DIR_VECTORS) {
			CellPosition neighborPos = new CellPosition(currX + dir[0], currY + dir[1]);
			if (neighborPos.isInBounds(mazeImage)) {
				int color = mazeImage.getRGB(neighborPos.x, neighborPos.y);
				if (color == EMPTY_COLOR) {
					Cell neighbor = cells.get(neighborPos);
					if (neighbor == null) {
						neighbor = new Cell(neighborPos, Double.POSITIVE_INFINITY);
						cells.put(neighborPos, neighbor);
					}
					result.add(neighbor);
				}
			}
		}
		return result;
	}

	private BufferedImage drawPath(Cell lastCell) {
		BufferedImage result = createCopy(mazeImage);
		result.setRGB(0, 1, SOLVE_COLOR);
		Cell curr = lastCell;
		while (curr != null) {
			result.setRGB(curr.position.x, curr.position.y, SOLVE_COLOR);
			curr = curr.previous;
		}
		return result;
	}

	private BufferedImage createCopy(BufferedImage img) {
		BufferedImage copy = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = copy.getGraphics();
		g.drawImage(img, 0, 0, null);
		g.dispose();
		return copy;
	}

	private static class Cell {
		private Cell previous;
		private CellPosition position;

		private double costToHere;
		private boolean isInQueue;

		public Cell(CellPosition pos, double costToHere) {
			this.position = pos;
			this.costToHere = costToHere;
		}

		public double distanceTo(CellPosition other) {
			return Math.sqrt(Math.pow(position.x - other.x, 2) + Math.pow(position.y - other.y, 2));
		}

		public boolean equals(Object o) {
			if (o instanceof Cell)
				return ((Cell)o).position.equals(position);
			return false;
		}
	}

	private static class CellPosition {
		int x, y;
		public CellPosition(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public boolean equals(Object o) {
			if (o instanceof CellPosition) {
				CellPosition pos = (CellPosition)o;
				return pos.x == x && pos.y == y;
			}
			return false;
		}

		public boolean isInBounds(BufferedImage img) {
			return x >= img.getMinX() && x < img.getWidth()
			    && y >= img.getMinY() && y < img.getHeight();
		}

		public int hashCode() {
			return (x << 16) ^ y;
		}
	}

	private static class DistanceHeuristic implements Comparator<Cell> {
		CellPosition exit;
		public DistanceHeuristic(CellPosition exit) {
			this.exit = exit;
		}

		public int compare(Cell c1, Cell c2) {
			double costSum1 = c1.costToHere + c1.distanceTo(exit);
			double costSum2 = c2.costToHere + c2.distanceTo(exit);
			return (int)(costSum1 - costSum2);
		}
	}
}
