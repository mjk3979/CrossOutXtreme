package com.crossoutxtremfree;

public class Move extends Combination implements Comparable
{
	private int[] numWins;
	private int[] numLosses;
	private boolean unknown;
	
	public Move(Circle circle1, Circle circle2, int numWins[], int numLosses[], boolean unknown)
	{
		super(circle1, circle2);
		this.numWins = numWins;
		this.numLosses=numLosses;
		this.unknown=unknown;
	}
	
	public int[] getNumWins()
	{
		return numWins;
	}
	
	public void setNumWins(int[] wins)
	{
		numWins = wins;
	}
	
	public int[] getNumLosses()
	{
		return numLosses;
	}
	
	public void setNumLosses(int[] losses)
	{
		numLosses = losses;
	}
	
	public boolean getUnknown()
	{
		return unknown;
	}
	
	public boolean checkNoWins()
	{
		for (int i=0;i<numWins.length;i++)
		{
			if (numWins[i]!=0)
				return false;
		}
		return true;
	}
	
	public boolean checkNoLosses()
	{
		for (int i=0;i<numLosses.length;i++)
		{
			if (numLosses[i]!=0)
				return false;
		}
		return true;
	}
	
	public int compareTo(Object obj)
	{
		Move move = (Move) obj;
		for (int i=0;i<numWins.length;i++)
		{
			int loss = numLosses[i];
			int moveLoss = move.numLosses[i];
			
			if (loss>moveLoss)
				return 1;
			else if (loss<moveLoss)
				return -1;
		}
		for (int i=0;i<numWins.length;i++)
		{
			int wins = numWins[i];
			int moveWins = move.numWins[i];
			
			if (wins<moveWins)
				return 1;
			else if (wins>moveWins)
				return -1;
		}
		return 0;
	}
	
	public String toString()
	{
		return "(" + getCircle1().getRow() + ", " + getCircle1().getColumn() + "), (" + getCircle2().getRow() + ", " + getCircle2().getColumn() + ") wins: " + numWins + ", losses: " + numLosses;
	}
}
