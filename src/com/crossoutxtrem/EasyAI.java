package com.crossoutxtrem;

import java.util.ArrayList;
import java.util.Collections;

import android.os.Handler;

public class EasyAI extends AI
{
	public EasyAI(Board board, Handler handler, SinglePlayerGame game)
	{
		super(board, handler, game);
	}
	
	public void setDepth()
	{
		depth = 1;
	}
	
	public Move pickMove()
	{
		ArrayList<Move> moves = this.moves;
		if (checkAllLosses(moves))
			return moves.get(0);
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
			int weight = 0;
			Collections.sort(moves);
			if (moves.get(0).checkNoLosses())
				return moves.get(0);
			int[] weights = new int[moves.size()];
			for (int i=0;i<moves.size();i++)
			{
				Move move = moves.get(i);
				int thisWeight = 1;
				if (thisWeight < 1)
					thisWeight = 1;
				weight+=thisWeight;
				weights[i]=weight;
			}
			int r = (int)(Math.random() * (weight + unknownMoves.size()));
			if (r<weight)
			{
				r = (int)(Math.random() * weight);
				int i = 0;
				for (i=0;weights[i]<r;i++) {}
				return moves.get(i);
			}
			else
			{
				r = (int)(Math.random()*unknownMoves.size());
				return unknownMoves.get(r);
			}
		}
		else
		{
			int r = (int)(Math.random()*unknownMoves.size());
			return unknownMoves.get(r);
		}
	}
}
