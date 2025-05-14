package gameserver;

import java.io.Serializable;
import java.util.List;

public class GameState implements Serializable {
    private List<ChessPiece> pieces;
    private int currentPlayer;

    public GameState(List<ChessPiece> pieces,int player){
        this.pieces=pieces;
        this.currentPlayer=player;
    }

    public List<ChessPiece> getPieces() {
        return pieces;
    }

    public void setPieces(List<ChessPiece> pieces) {
        this.pieces = pieces;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void update(GameState newState) {
        this.pieces=newState.getPieces();
        this.currentPlayer=newState.currentPlayer;
    }
}