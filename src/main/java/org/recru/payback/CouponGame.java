package org.recru.payback;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;

public class CouponGame {
	private final Board board = new Board();
	private int roundCounter = 0;

	public static void main(String[] args) {
		new CouponGame().play();
	}

	public void play() {
		simulate(25);
		simulate(50);
		simulate(100);
	}

	private void simulate(int finalSimulationRound) {
		assert finalSimulationRound > roundCounter;
		int rounds = finalSimulationRound - roundCounter;
		System.out.println("Simulating game to round " + finalSimulationRound);
		IntStream.range(0, rounds)
				.forEach(i -> simulateRound());
		roundCounter = finalSimulationRound;
		System.out.println("* Cupon(s) with most pointees: " + board.getMaxPointees());
	}

	private void simulateRound() {
		board.map.entrySet()
				.stream()
				.filter(entry -> entry.getValue().pointees > 0)
				.forEach(entry -> entry.getValue()
						.transferPointee(board.map.get(board.getRandomNeighbour(entry.getKey()))));
	}

	private static class Board {
		private static final int ROWS = 15;
		private static final int COLS = 15;
		private final Random random = new Random();
		private final Map<Coordinate, Coupon> map = new HashMap<>();

		public Board() {
			IntStream.range(0, ROWS)
					.forEach(i -> IntStream.range(0, COLS)
							.forEach(j -> map.put(new Coordinate(i, j), new Coupon())));
		}

		public int getMaxPointees() {
			return map.values()
					.stream()
					.max(Coupon::compareTo)
					.orElse(new Coupon()).pointees;
		}

		public Coordinate getRandomNeighbour(Coordinate coord) {
			int neighbors = getNumberOfNeighbours(coord);
			return IntStream.rangeClosed(-1, 1)
					.mapToObj(i -> IntStream.rangeClosed(-1, 1)
							.mapToObj(j -> new Coordinate(coord.row + i, coord.col + j)))
					.flatMap(Function.identity())
					.filter(map::containsKey)
					.filter(coordinate -> !coordinate.equals(coord))
					.skip(random.nextInt(neighbors))
					.findFirst()
					.orElseThrow();
		}

		private int getNumberOfNeighbours(Coordinate coord) {
			// on corners
			if ((coord.row == 0 || coord.row == 14) && (coord.col == 0 || coord.col == 14)) {
				return 3;
			}
			// on edges
			if (coord.row == 0 || coord.row == 14 || coord.col == 0 || coord.col == 14) {
				return 5;
			}
			// in the middle
			return 8;
		}

		record Coordinate(int row, int col) {
		}
	}

	private static class Coupon implements Comparable<Coupon> {
		private int pointees = 1;

		private void transferPointee(Coupon to) {
			this.pointees--;
			to.pointees++;
		}

		@Override
		public int compareTo(Coupon o) {
			return Integer.compare(pointees, o.pointees);
		}
	}
}