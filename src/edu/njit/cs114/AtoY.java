package edu.njit.cs114;

import java.util.Scanner;

/**
 * Author: Chris Lombardi
 * Date created: 2/6/2024
 */
public class AtoY {

    public static void printTable(char[][] t) {
        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 5; ++j)
                System.out.print(t[i][j]);
            System.out.println();
        }
    }

    /**
     * Check if we can fill characters in the grid starting from ch in the cell (row,col)
     * The grid may already have some characters which should not be changed
     *
     * @param t                      grid
     * @param row
     * @param col
     * @param ch
     * @param allowDiagonalNeighbors
     * @return true if characters can be filled in else false
     */
    private static boolean solve(char[][] t, int row, int col, char ch,
                                 boolean allowDiagonalNeighbors) {
        if (ch == 'y') {
            return true;
        }
        if (row < 0 || col < 0) {
            return false;
        }
        if (row > 4 || col > 4) {
            return false;
        }
        char nextChar = (char) ((int) ch + 1);
        if (!allowDiagonalNeighbors) {
            if (row > 0 && t[row - 1][col] == nextChar) {
                return solve(t, row - 1, col, nextChar, allowDiagonalNeighbors);
            }
            if (row < 4 && t[row + 1][col] == nextChar) {
                return solve(t, row + 1, col, nextChar, allowDiagonalNeighbors);
            }
            if (col > 0 && t[row][col - 1] == nextChar) {
                return solve(t, row, col - 1, nextChar, allowDiagonalNeighbors);
            }
            if (col < 4 && t[row][col + 1] == nextChar) {
                return solve(t, row, col + 1, nextChar, allowDiagonalNeighbors);
            }
            boolean answer;
            if (row > 0 && t[row - 1][col] == 'z') {
                t[row - 1][col] = nextChar;
                answer = solve(t, row - 1, col, nextChar, allowDiagonalNeighbors);
                if (answer)
                    return true;
                else
                    t[row - 1][col] = 'z';
            }
            if (row < 4 && t[row + 1][col] == 'z') {
                t[row + 1][col] = nextChar;
                answer = solve(t, row + 1, col, nextChar, allowDiagonalNeighbors);
                if (answer)
                    return true;
                else
                    t[row + 1][col] = 'z';
            }
            if (col > 0 && t[row][col - 1] == 'z') {
                t[row][col - 1] = nextChar;
                answer = solve(t, row, col - 1, nextChar, allowDiagonalNeighbors);
                if (answer)
                    return true;
                else
                    t[row][col - 1] = 'z';
            }
            if (col < 4 && t[row][col + 1] == 'z') {
                t[row][col + 1] = nextChar;
                answer = solve(t, row, col + 1, nextChar, allowDiagonalNeighbors);
                if (answer)
                    return true;
                else
                    t[row][col + 1] = 'z';
            }
        } else {
            if (row > 0 && t[row - 1][col] == nextChar) {
                return solve(t, row - 1, col, nextChar, allowDiagonalNeighbors);
            }
            if (row < 4 && t[row + 1][col] == nextChar) {
                return solve(t, row + 1, col, nextChar, allowDiagonalNeighbors);
            }
            if (col > 0 && t[row][col - 1] == nextChar) {
                return solve(t, row, col - 1, nextChar, allowDiagonalNeighbors);
            }
            if (col < 4 && t[row][col + 1] == nextChar) {
                return solve(t, row, col + 1, nextChar, allowDiagonalNeighbors);
            }
            if (col < 4 && row < 4 && t[row + 1][col + 1] == nextChar) {
                return solve(t, row + 1, col + 1, nextChar, allowDiagonalNeighbors);
            }
            if (col < 4 && row > 0 && t[row - 1][col + 1] == nextChar) {
                return solve(t, row - 1, col + 1, nextChar, allowDiagonalNeighbors);
            }
            if (col > 0 && row > 0 && t[row - 1][col - 1] == nextChar) {
                return solve(t, row - 1, col - 1, nextChar, allowDiagonalNeighbors);
            }
            if (col > 0 && row < 4 && t[row + 1][col - 1] == nextChar) {
                return solve(t, row + 1, col - 1, nextChar, allowDiagonalNeighbors);
            }

            boolean answer;
            if (row > 0 && t[row - 1][col] == 'z') {
                t[row - 1][col] = nextChar;
                answer = solve(t, row - 1, col, nextChar, allowDiagonalNeighbors);
                if (answer)
                    return true;
                else
                    t[row - 1][col] = 'z';
            }
            if (row < 4 && t[row + 1][col] == 'z') {
                t[row + 1][col] = nextChar;
                answer = solve(t, row + 1, col, nextChar, allowDiagonalNeighbors);
                if (answer)
                    return true;
                else
                    t[row + 1][col] = 'z';
            }
            if (col > 0 && t[row][col - 1] == 'z') {
                t[row][col - 1] = nextChar;
                answer = solve(t, row, col - 1, nextChar, allowDiagonalNeighbors);
                if (answer)
                    return true;
                else
                    t[row][col - 1] = 'z';
            }
            if (col < 4 && t[row][col + 1] == 'z') {
                t[row][col + 1] = nextChar;
                answer = solve(t, row, col + 1, nextChar, allowDiagonalNeighbors);
                if (answer)
                    return true;
                else
                    t[row][col + 1] = 'z';
            }
            if (row > 0 && col > 0 && t[row - 1][col - 1] == 'z') {
                t[row - 1][col - 1] = nextChar;
                answer = solve(t, row - 1, col - 1, nextChar, allowDiagonalNeighbors);
                if (answer)
                    return true;
                else
                    t[row - 1][col - 1] = 'z';
            }
            if (row > 0 && col < 4 && t[row - 1][col + 1] == 'z') {
                t[row - 1][col + 1] = nextChar;
                answer = solve(t, row - 1, col + 1, nextChar, allowDiagonalNeighbors);
                if (answer)
                    return true;
                else
                    t[row - 1][col + 1] = 'z';
            }
            if (row < 4 && col < 4 && t[row + 1][col + 1] == 'z') {
                t[row + 1][col + 1] = nextChar;
                answer = solve(t, row + 1, col + 1, nextChar, allowDiagonalNeighbors);
                if (answer)
                    return true;
                else
                    t[row + 1][col + 1] = 'z';
            }
            if (row < 4 && col > 0 && t[row + 1][col - 1] == 'z') {
                t[row + 1][col-1] = nextChar;
                answer = solve(t, row + 1, col-1, nextChar, allowDiagonalNeighbors);
                if (answer)
                    return true;
                else
                    t[row + 1][col-1] = 'z';
            }

        }return false;
    }


    public static boolean solve(char [] [] t, boolean allowDiagonalNeighbors) {
        int row = -1, col = -1;
        for (int i = 0; i < t.length; i++) {
            for (int j = 0; j < t[i].length; j++) {
                if (t[i][j] == 'a') {
                    row = i;
                    col = j;
                }
            }
        }
        if (row >= 0 && col >= 0) {
            return solve(t, row, col, 'a', allowDiagonalNeighbors);
        } else {
            for (int i = 0; i < t.length; i++) {
                for (int j = 0; j < t[i].length; j++) {
                    if (t[i][j] == 'z') {
                        t[i][j] = 'a';
                        if (solve(t, i, j, 'a', allowDiagonalNeighbors)) {
                            return true;
                        } else {
                            t[i][j] = 'z';
                        }
                    }
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println("Enter 5 rows of lower-case letters a to z below. Note z indicates empty cell");
        Scanner sc = new Scanner(System.in);
        char[][] tbl = new char[5][5];
        String inp;
        for (int i = 0; i < 5; ++i) {
            inp = sc.next();
            for (int j = 0; j < 5; ++j) {
                tbl[i][j] = inp.charAt(j);
            }
        }
        System.out.println("Are diagonal neighbors included ? (true or false)");
        if (solve(tbl,sc.nextBoolean())) {
            System.out.println("Printing the solution...");
            printTable(tbl);
        } else {
            System.out.println("There is no solution");
        }
    }
}
