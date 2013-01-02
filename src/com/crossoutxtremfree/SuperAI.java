package com.crossoutxtremfree;

import java.io.BufferedReader;
import java.util.ArrayList;

import android.os.Handler;

public class SuperAI extends AI
{
	private BufferedReader reader;
	public SuperAI(Board board, Handler handler, SinglePlayerGame game, BufferedReader reader)
	{
		super(board, handler, game);
		this.reader=reader;
	}
	@Override
	public Move pickMove()
	{
		try
		{
			String boardState=this.board.toInteger()+"";
			String line = reader.readLine();
			while (!line.contains(boardState))
			{
				line = reader.readLine();
			}
			reader.close();
			int start = Integer.parseInt(line.substring(line.indexOf("|")+1,line.indexOf("|",line.indexOf("|")+1)));
			int end = Integer.parseInt(line.substring(line.indexOf("|",line.indexOf("|")+1)+1));
			return new Move(this.board.getCircleAt(start),this.board.getCircleAt(end),null,null,false);
		}
		catch (Exception ex)
		{
			
		}
		return null;
		
	}

	@Override
	public void setDepth()
	{
		this.depth=0;
	}
	
	public ArrayList<Move> findMoves(Board board, boolean myMove, int level)
	{
		return new ArrayList<Move>();
	}

}