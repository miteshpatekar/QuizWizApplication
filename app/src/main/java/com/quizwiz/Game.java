package com.quizwiz;

/**
 * Created by Mitesh on 4/5/2015.
 */
public class Game {
    private String player1;
    private String player2;
    private String winner;
    private String gameStatus;
    private String category;
    private int player1Points;
    private int player2Points;



    public Game() {}
    public Game(String player1,String status,String category)
    {
        this.player1 = player1;
        this.player2 = null;
        this.winner  = null;
        this.gameStatus=status;
        this.category=category;
    }

    public Game(String player1,String player2,String status,String category)
    {
        this.player1 = player1;
        this.player2 = player2;
        this.winner  = null;
        this.gameStatus=status;
        this.category=category;
    }

    public Game(String player1,String player2,String status,String category,
                int player1Points, int player2Points)
    {
        this.player1 = player1;
        this.player2 = player2;
        this.winner  = null;
        this.gameStatus=status;
        this.category=category;
        this.player1Points=player1Points;
        this.player2Points=player2Points;
    }

    public String getPlayer1() {
        return player1;
    }
    public String getPlayer2() {
        return player2;
    }
    public String getWinner() {
        return winner;
    }
    public String getGameStatus() {
        return gameStatus;
    }
    public String getCategory() {
        return category;
    }
    public int getPlayer1Points() {
        return player1Points;
    }
    public int getPlayer2Points() {
        return player2Points;
    }

}
