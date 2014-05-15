package com.crossoutxtrem;

import java.util.*;

public class PreviousBoardMove
{
	private Board board;
	private ArrayList<Move> moves;
	private boolean myTurn;
	private int level;
	
	public PreviousBoardMove(Board board, ArrayList<Move> moves, boolean myTurn, int level)
	{
		this.board=new Board(board);
		this.moves = new ArrayList<Move>(moves);
		this.myTurn=myTurn;
		this.level=level;
	}
	
	public boolean isSame(Board board2)
	{
		return board.equals(board2);
	}
	
	public ArrayList<Move> getMoves(boolean turn, int newLevel)
	{
		ArrayList<Move> moves = new ArrayList<Move>(this.moves);
		if (turn!=myTurn)
		{
			for (Move move : moves)
			{
				int[] temp = move.getNumLosses();
				move.setNumLosses(move.getNumWins());
				move.setNumWins(temp);
			}
		}
		if (level!=newLevel)
		{
			int change = newLevel - level;
			for (Move move : moves)
			{
				int[] newWins = new int[move.getNumWins().length];
				int[] newLosses = new int[move.getNumLosses().length];
				for (int i=0;i<move.getNumLosses().length;i++)
				{
					int j = i+change;
					if (j<0||j>=move.getNumLosses().length)
					{
						newWins[i]=0;
						newLosses[i]=0;
					}
					else
					{
						newWins[i]=move.getNumWins()[j];
						newLosses[i]=move.getNumLosses()[j];
					}
				}
			}
		}
		return moves;
	}
}
