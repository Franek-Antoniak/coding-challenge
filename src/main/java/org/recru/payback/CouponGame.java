package org.recru.payback;

import java.util.Random;
import java.util.stream.IntStream;

public class CouponGame {
	private static final int ROWS = 15;
	private static final int COLS = 15;
	private static final int[] ROUNDS = {25, 50, 100};
	private int[][] checkerboard = new int[ROWS][COLS];
	private Random random = new Random();

	public void play() {
		// Initialize checkerboard with 1 Pointee per square
		IntStream.range(0, ROWS).forEach(i -> IntStream.range(0, COLS).forEach(j -> checkerboard[i][j] = 1));

		// Play game rounds
		for (int round = 1; round <= 100; round++) {
			// Make Pointees jump to random adjacent square
			for (int i = 0; i < ROWS; i++) {
				for (int j = 0; j < COLS; j++) {
					int newI = i + random.nextInt(3) - 1;
					int newJ = j + random.nextInt(3) - 1;
					if (newI >= 0 && newI < ROWS && newJ >= 0 && newJ < COLS) {
						checkerboard[newI][newJ]++;
						checkerboard[i][j]--;
					}
				}
			}

			// Check if current round is a redemption round
			for (int redemptionRound : ROUNDS) {
				if (round == redemptionRound) {
					System.out.println("Round " + round + " - results:");
					// Calculate number of points per coupon
					int[] points = new int[ROWS * COLS];
					int index = 0;
					for (int i = 0; i < ROWS; i++) {
						for (int j = 0; j < COLS; j++) {
							points[index++] = checkerboard[i][j];
						}
					}
					// Find coupon(s) with highest number of points
					int maxPoints = 0;
					for (int point : points) {
						if (point > maxPoints) {
							maxPoints = point;
						}
					}
					System.out.println("Coupon(s) with highest points: " + maxPoints);
				}
			}
		}
	}

	public static void main(String[] args) {
		new CouponGame().play();
	}
}