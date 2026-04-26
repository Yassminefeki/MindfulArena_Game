package com.example.xo.AI;

import com.example.xo.models.GameState;
import java.util.Random;

/**
 * Minimax-based AI with three difficulty levels.
 */
public class TicTacToeAI {

    public enum Difficulty {
        EASY, MEDIUM, HARD
    }

    private int aiPlayer;
    private int humanPlayer;
    private final Random random = new Random();

    public TicTacToeAI() {
        // Default values, can be updated if needed
        this.aiPlayer = GameState.PLAYER_X;
        this.humanPlayer = GameState.PLAYER_O;
    }

    public TicTacToeAI(int aiSymbol) {
        this.aiPlayer    = aiSymbol;
        this.humanPlayer = (aiSymbol == GameState.PLAYER_X) ? GameState.PLAYER_O : GameState.PLAYER_X;
    }

    /** Returns the best cell index for the AI to play. */
    public int getBestMove(GameState state, Difficulty difficulty) {
        int[] available = state.getAvailableMoves();
        if (available.length == 0) return -1;

        switch (difficulty) {
            case EASY: return getRandomMove(available);
            case MEDIUM: return getMediumMove(state, available);
            case HARD:
            default: return getHardMove(state);
        }
    }

    // ── Hint: the best move for the CURRENT player ───────────────────────────
    public int getHintMove(GameState state) {
        return getHardMove(state);
    }

    // ── Difficulty strategies ─────────────────────────────────────────────────

    private int getRandomMove(int[] available) {
        return available[random.nextInt(available.length)];
    }

    private int getMediumMove(GameState state, int[] available) {
        // First check if AI can win immediately
        int winning = findWinningMove(state, aiPlayer);
        if (winning != -1) return winning;

        // Then block human win
        int blocking = findWinningMove(state, humanPlayer);
        if (blocking != -1) return blocking;

        // Otherwise random
        return getRandomMove(available);
    }

    private int findWinningMove(GameState state, int player) {
        for (int move : state.getAvailableMoves()) {
            GameState copy = state.copy();
            copy.makeMove(move);
            if (copy.getWinner() == player) return move;
        }
        return -1;
    }

    private int getHardMove(GameState state) {
        int bestScore = Integer.MIN_VALUE;
        int bestMove  = -1;
        for (int move : state.getAvailableMoves()) {
            GameState copy = state.copy();
            copy.makeMove(move);
            int score = minimax(copy, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (score > bestScore) {
                bestScore = score;
                bestMove  = move;
            }
        }
        return bestMove;
    }

    // ── Alpha-beta pruned minimax ─────────────────────────────────────────────

    private int minimax(GameState state, boolean isMaximising, int alpha, int beta) {
        int winner = state.getWinner();
        if (winner == aiPlayer)    return 10 - state.getMoveCount();
        if (winner == humanPlayer) return state.getMoveCount() - 10;
        if (state.isGameOver())    return 0;

        if (isMaximising) {
            int best = Integer.MIN_VALUE;
            for (int move : state.getAvailableMoves()) {
                GameState copy = state.copy();
                copy.makeMove(move);
                int score = minimax(copy, false, alpha, beta);
                best  = Math.max(best, score);
                alpha = Math.max(alpha, best);
                if (beta <= alpha) break;
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int move : state.getAvailableMoves()) {
                GameState copy = state.copy();
                copy.makeMove(move);
                int score = minimax(copy, true, alpha, beta);
                best = Math.min(best, score);
                beta = Math.min(beta, best);
                if (beta <= alpha) break;
            }
            return best;
        }
    }
}