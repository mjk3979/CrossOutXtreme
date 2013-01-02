package com.crossoutxtremfree;

import java.util.ArrayList;
import java.util.Collections;

import android.os.Handler;

public class HardAI extends AI
{
	public HardAI(Board board, Handler handler, SinglePlayerGame game)
	{
		super(board, handler, game);
	}
	
	public void setDepth()
	{
		int circles = board.getNumberOfEmptyCircles();
		if (circles>=13)
			depth = 0;
		else if (circles>=11)
			depth = 1;
		else if (circles>=8)
			depth = 2;
		else
			depth = 3;
	}
	
	public Move pickMove()
	{
		ArrayList<Move> moves = this.moves;
		if (checkAllLosses(moves))
		{
			int r = (int)(Math.random() * moves.size());
			return moves.get(r);
		}
		ArrayList<Move> unknownMoves = new ArrayList<Move>();
		for (int i=0;i<moves.size();i++)
		{
			Move move = moves.get(i);
			if (move.getUnknown())
			{
				unknownMoves.add(move);
				moves.remove(i);
				i--;
			}
			else if (move.checkNoWins())
			{
				moves.remove(i);
				i--;
			}
		}
		if (moves.size()!=0)
		{
			for (int i=0;i<moves.size();i++)
			{
				for (int j=0;j<moves.size()-1;j++)
				{
					Move move1 = moves.get(j);
					Move move2 = moves.get(j+1);
					if (move1.compareTo(move2)>0)
					{
						moves.set(j+1, move1);
						moves.set(j, move2);
					}
				}
			}
			//if (moves.get(0).checkNoLosses()||unknownMoves.size()==0)
				return moves.get(0);
			/*int r = (int)(Math.random() * (unknownMoves.size()+1));
			if (r==0)
				return moves.get(0);
			else
				return unknownMoves.get(r-1);*/
		}
		else
		{
			int r = (int)(Math.random()*unknownMoves.size());
			return unknownMoves.get(r);
		}
	}
}
