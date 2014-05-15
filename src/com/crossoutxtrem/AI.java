package com.crossoutxtrem;

import java.util.*;

import com.crossoutxtrem.SinglePlayerGame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;

public abstract class AI extends Thread
{
	public static int currentDifficulty;
	public Board board;
	public ArrayList<Move> moves;
	public int depth;
	public ArrayList<ArrayList<PreviousBoardMove>> prevMoves;
	private Handler handler;
	private SinglePlayerGame game;
	
	public AI(Board board, Handler handler, SinglePlayerGame game)
	{
		this.board = new Board(board);
		moves = new ArrayList<Move>();
		setDepth();
		this.handler = handler;
		this.game = game;
		prevMoves = new ArrayList<ArrayList<PreviousBoardMove>>();
		for (int i=0;i<=15;i++)
			prevMoves.add(new ArrayList<PreviousBoardMove>());
	}
	
	public abstract void setDepth();
	
	public void run()
	{
		moves = findMoves(new Board(board), true, depth);
		sendMove();
	}
	
	public abstract Move pickMove();
	
	private void sendMove()
	{
		final Move move = pickMove();
		handler.post(new Runnable() {
			public void run()
			{
				game.bar.setVisibility(View.INVISIBLE);
				game.board.makeMove(move.getCircle1(), move.getCircle2(), SinglePlayerGame.computerColor);
				game.prevMoves.add(new Combination(move.getCircle1(), move.getCircle2()));
				game.boardView.drawBoard(game.board);
				game.board.setClickable(true);
				game.playerTurn=true;
				if (game.board.checkDone())
				{
					game.addStat(true);
					AlertDialog messageBox = new AlertDialog.Builder(game).create();
					messageBox.setTitle("Game Over");
					messageBox.setMessage("You Win");
					messageBox.setButton("Ok", game);
					messageBox.show();
				}
				else
				{
					game.setPlayerLabel(true);
				}
			}
		});
	}
	
	public boolean checkAllLosses(ArrayList<Move> moves)
	{
		for (Move move : moves)
		{
			if (move.getUnknown()||!move.checkNoWins())
				return false;
		}
		return true;
	}
	
	public ArrayList<Move> findMoves(Board board, boolean myMove, int level)
	{
		ArrayList<PreviousBoardMove> pMoves = prevMoves.get(board.getNumberOfEmptyCircles());
		for (PreviousBoardMove move : pMoves)
		{
			if (move.isSame(board))
				return move.getMoves(myMove, level);
		}
		ArrayList<Move> moves = new ArrayList<Move>();
		ArrayList<Combination> combos = getCombinations(board);
		int place = depth - level;
		for (Combination combo : combos)
		{
			int[] wins = new int[16];
			int[] losses = new int[16];
			boolean unknown = false;
			Board newBoard = new Board(board);
			newBoard.makeMove(combo.getCircle1(), combo.getCircle2(), Color.GRAY);
			if (newBoard.checkDone())
			{
				if (myMove)
					losses[place]++;
				else
					wins[place]++;
			}
			else
			{
				if (checkTraps(newBoard)&&(currentDifficulty==1||Math.random()>.5))
				{
					if (myMove)
						wins[place]++;
					else
						losses[place]++;
				}
				else if (level>0)
				{
					ArrayList<Move> oldMoves = findMoves(newBoard, !myMove, level-1);
					unknown = true;
					for (Move move : oldMoves)
					{
						int[] prevWins = move.getNumWins();
						int[] prevLosses = move.getNumLosses();
						for (int i=level-1;i>=0;i--)
						{
							wins[i]+=prevWins[i];
							losses[i]+=prevLosses[i];
						}
						if (!move.getUnknown())
							unknown = false;
					}
				}
				else
					unknown = true;
			}
			moves.add(new Move(combo.getCircle1(), combo.getCircle2(), wins, losses, unknown));
		}
		prevMoves.get(board.getNumberOfEmptyCircles()).add(new PreviousBoardMove(board, moves, myMove, level));
		return moves;
	}
	
	public boolean checkTraps(Board board)
	{
		ArrayList<Combination> nextCombos = getCombinations(board);
		int len = nextCombos.size();
		int circles = board.getNumberOfEmptyCircles();
		if (circles==4&&len==6)
			return true;
		else if (checkSquareTrap(board))
			return true;
		else if (checkTriangleTrap(board))
			return true;
		else if (circles==len&&circles%2==1)
			return true;
		else if (checkSmallTriangleTrap(board))
			return true;
		else
			return false;
	}
	
	private boolean checkSmallTriangleTrap(Board board)
	{
		int circles = board.getNumberOfEmptyCircles();
		if (circles!=4)
			return false;
		for (int r=0;r<4;r++)
		{
			for (int c=0;c<=r;c++)
			{
				Circle cir1 = board.getCircleAt(r, c);
				Circle cir2 = board.getCircleAt(r+1, c);
				Circle cir3 = board.getCircleAt(r+1, c+1);
				if (cir1.checkEmpty()&&cir2.checkEmpty()&&cir3.checkEmpty())
				{
					for (int r1=0;r1<5;r1++)
					{
						for (int c1=0;c1<=r1;c1++)
						{
							Circle cir4 = board.getCircleAt(r1, c1);
							if (!cir4.checkEmpty()||cir4.equals(cir1)||cir4.equals(cir2)||cir4.equals(cir3))
								continue;
							if (!board.checkValidMove(cir1, cir4)&&!board.checkValidMove(cir2, cir4)&&!board.checkValidMove(cir3, cir4))
								return true;
							else
								return false;
						}
					}
				}
			}
		}
		return false;
	}
	
	private boolean checkTriangleTrap(Board board)
	{
		int circles = board.getNumberOfEmptyCircles();
		if (circles!=6)
			return false;
		for (int r=0;r<=2;r++)
		{
			for (int c=0;c<=r;c++)
			{
				boolean b = true;
				for (int r1=0;r1<=2;r1++)
				{
					for (int c1=0;c1<=r1;c1++)
					{
						int r2 = r + r1;
						int c2 = c + c1;
						if (!board.getCircleAt(r2, c2).checkEmpty())
						{
							b = false;
							break;
						}
					}
					if (!b)
						break;
				}
				if (b)
					return true;
			}
		}
		return false;
	}
	
	private boolean checkSquareTrap(Board board)
	{
		int circles = board.getNumberOfEmptyCircles();
		if (circles!=4)
			return false;
		if (checkLeftSquare(board)||checkRightSquare(board)||checkDiamond(board))
			return true;
		else
			return false;
	}
	
	private boolean checkLeftSquare(Board board)
	{
		for (int r=1;r<4;r++)
		{
			for (int c=0;c<r;c++)
			{
				boolean b = true;
				for (int r1=0;r1<2;r1++)
				{
					for (int c1=0;c1<2;c1++)
					{
						int r2 = r + r1;
						int c2 = c + c1 + r1;
						if (!board.getCircleAt(r2, c2).checkEmpty())
						{
							b = false;
							break;
						}
					}
					if (!b)
						break;
				}
				if (b)
					return true;
			}
		}
		return false;
	}
	
	private boolean checkRightSquare(Board board)
	{
		for (int r=1;r<4;r++)
		{
			for (int c=0;c<r;c++)
			{
				boolean b = true;
				for (int r1=0;r1<2;r1++)
				{
					for (int c1=0;c1<2;c1++)
					{
						int r2 = r + r1;
						int c2 = c + c1;
						if (!board.getCircleAt(r2, c2).checkEmpty())
						{
							b = false;
							break;
						}
					}
					if (!b)
						break;
				}
				if (b)
					return true;
			}
		}
		return false;
	}
	
	private boolean checkDiamond(Board board)
	{
		for (int r=0;r<3;r++)
		{
			for (int c=0;c<=r;c++)
			{
				boolean b1 = board.getCircleAt(r, c).checkEmpty();
				boolean b2 = board.getCircleAt(r+1, c).checkEmpty();
				boolean b3 = board.getCircleAt(r+1, c+1).checkEmpty();
				boolean b4 = board.getCircleAt(r+2, c+1).checkEmpty();
				if (b1&&b2&&b3&&b4)
					return true;
			}
		}
		return false;
	}
	
	public int maxInLine(Board board)
	{
		int max = 0;
		for (int r=1;r<5;r++)
		{
			int inLine=0;
			for (int c=0;c<=r;c++)
			{
				if (board.getCircleAt(r, c).checkEmpty())
					inLine++;
			}
			if (inLine>max)
				max=inLine;
		}
		for (int c=0;c<4;c++)
		{
			int inLine=0;
			for (int r=4;r>=c;r--)
			{
				if (board.getCircleAt(r, c).checkEmpty())
					inLine++;
			}
			if (inLine>max)
				max=inLine;
		}
		for (int c=4;c>=1;c--)
		{
			int thisC = c;
			int inLine=0;
			for (int r=4;r>=4-c;r--)
			{
				thisC--;
				if (board.getCircleAt(r, thisC).checkEmpty())
					inLine++;
			}
			if (inLine>max)
				max=inLine;
		}
		return max;
	}
	
	public ArrayList<Combination> getCombinations(Board board)
	{
		ArrayList<Combination> combos = new ArrayList<Combination>();
		for (int r1=0;r1<5;r1++)
		{
			for (int c1=0;c1<=r1;c1++)
			{
				Circle circle1 = board.getCircleAt(r1, c1);
				if (!circle1.checkEmpty())
					continue;
				for (int r2=r1;r2<5;r2++)
				{
					for (int c2=0;c2<=r2;c2++)
					{
						if (r2==r1&&c2<c1)
							c2=c1;
						Circle circle2 = board.getCircleAt(r2,c2);
						if (board.checkValidMove(circle1, circle2))
							combos.add(new Combination(circle1, circle2));
					}
				}
			}
		}
		return combos;
	}
}
