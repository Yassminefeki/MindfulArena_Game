package com.example.xo.models;

import java.util.Arrays;

public class GameState {

    public static final int EMPTY  = 0;
    public static final int PLAYER_X = 1;
    public static final int PLAYER_O = 2;

    private int[] board = new int[9];
    private int   currentPlayer;
    private int   winner;        // 0=none, 1=X, 2=O, 3=draw
    private int[] winLine;       // indices of winning cells
    private int   moveCount;
    private boolean gameOver;

    // Win combinations (row, col, diagonal)
    public static final int[][] WIN_COMBOS = {
            {0,1,2},{3,4,5},{6,7,8},   // rows
            {0,3,6},{1,4,7},{2,5,8},   // cols
            {0,4,8},{2,4,6}            // diagonals
    };

    public GameState() {
        reset();
    }

    public void reset() {
        Arrays.fill(board, EMPTY);
        currentPlayer = PLAYER_X;
        winner        = 0;
        winLine       = null;
        moveCount     = 0;
        gameOver      = false;
    }

    /** Returns true if move was valid and applied. */
    public boolean makeMove(int cellIndex) {
        if (gameOver || cellIndex < 0 || cellIndex > 8 || board[cellIndex] != EMPTY) {
            return false;
        }
        board[cellIndex] = currentPlayer;
        moveCount++;
        checkWinner();
        if (!gameOver) {
            currentPlayer = (currentPlayer == PLAYER_X) ? PLAYER_O : PLAYER_X;
        }
        return true;
    }

    private void checkWinner() {
        for (int[] combo : WIN_COMBOS) {
            if (board[combo[0]] != EMPTY
                    && board[combo[0]] == board[combo[1]]
                    && board[combo[1]] == board[combo[2]]) {
                winner   = board[combo[0]];
                winLine  = combo;
                gameOver = true;
                return;
            }
        }
        if (moveCount == 9) {
            winner   = 3; // draw
            gameOver = true;
        }
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public int[] getBoard()        { return board.clone(); }
    public int   getCell(int i)    { return board[i]; }
    public int   getCurrentPlayer(){ return currentPlayer; }
    public int   getWinner()       { return winner; }
    public int[] getWinLine()      { return winLine; }
    public int   getMoveCount()    { return moveCount; }
    public boolean isGameOver()    { return gameOver; }
    public boolean isDraw()        { return winner == 3; }

    /** Returns a deep copy for AI tree search. */
    public GameState copy() {
        GameState gs = new GameState();
        gs.board         = this.board.clone();
        gs.currentPlayer = this.currentPlayer;
        gs.winner        = this.winner;
        gs.winLine       = this.winLine != null ? this.winLine.clone() : null;
        gs.moveCount     = this.moveCount;
        gs.gameOver      = this.gameOver;
        return gs;
    }

    public int[] getAvailableMoves() {
        int[] tmp = new int[9];
        int count = 0;
        for (int i = 0; i < 9; i++) {
            if (board[i] == EMPTY) tmp[count++] = i;
        }
        return Arrays.copyOf(tmp, count);
    }
}