package com.crossoutxtrem;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.widget.ImageView;

public class Board
{
	private Circle[][] board;
	private int numberOfEmptyCircles;
	private ArrayList<BoardMove> boardMoves;
	
	public Board(Circle[][] board)
	{
		this.board = board;
		calcNumberOfEmptyCircles();
		boardMoves = new ArrayList<BoardMove>();
	}
	
	public Board (Board board1)
	{
		board = new Circle[5][5];
		for (int r=0;r<5;r++)
		{
			for (int c=0;c<=r;c++)
			{
				board[r][c] = new Circle(board1.getCircleAt(r,c));
			}
		}
		calcNumberOfEmptyCircles();
		boardMoves = new ArrayList<BoardMove>(board1.boardMoves);
	}
	
	public Circle getCircleAt(int r, int c)
	{
		if (r<5&&c<=r)
			return board[r][c];
		else
			return null;
	}
	
	public Circle getCircleAt(int num)
	{
		int r;
		if (num==0)
			r=0;
		else if (num<=2)
			r=1;
		else if (num<=5)
			r=2;
		else if (num<=9)
			r=3;
		else
			r=4;
		
		int c = num-(r*(r+1))/2;
		return getCircleAt(r,c);
	}
	
	public Circle findSelected(ImageView view)
	{
		for (int r=0;r<5;r++)
		{
			for (int c=0;c<=r;c++)
			{
				if (board[r][c].getImageViewId()==view.getId())
					return board[r][c];
			}
		}
		return null;
	}
	
	public void makeMove(Circle circle1, Circle circle2, int color)
	{
		if (!checkValidMove(circle1, circle2))
			return;
		if (circle1.getRow()==circle2.getRow())
		{
			int status = Circle.HORZ;
			if (circle1.getColumn()>circle2.getColumn())
			{
				Circle temp = circle1;
				circle1=circle2;
				circle2=temp;
			}
			int r = circle1.getRow();
			for (int c=circle1.getColumn();c<=circle2.getColumn();c++)
				board[r][c].changeStatus(status);
		}
		else if (circle1.getColumn()==circle2.getColumn())
		{
			int status = Circle.RIGHT;
			if (circle1.getRow()>circle2.getRow())
			{
				Circle temp = circle1;
				circle1=circle2;
				circle2=temp;
			}
			int c = circle1.getColumn();
			for (int r=circle1.getRow();r<=circle2.getRow();r++)
				board[r][c].changeStatus(status);
		}
		else
		{
			int status = Circle.LEFT;
			if (circle1.getRow()>circle2.getRow())
			{
				Circle temp = circle1;
				circle1=circle2;
				circle2=temp;
			}
			int c = circle1.getColumn();
			for (int r=circle1.getRow();r<=circle2.getRow();r++)
			{
				board[r][c].changeStatus(status);
				c++;
			}
		}
		Circle c1 = this.getCircleAt(circle1.getRow(), circle1.getColumn());
		Circle c2 = this.getCircleAt(circle2.getRow(), circle2.getColumn());
		boardMoves.add(new BoardMove(c1, c2, color));
		calcNumberOfEmptyCircles();
	}
	
	public void undoMove(Circle circle1, Circle circle2)
	{
		int status = Circle.EMPTY;
		boardMoves.remove(boardMoves.size()-1);
		if (circle1.getRow()==circle2.getRow())
		{
			if (circle1.getColumn()>circle2.getColumn())
			{
				Circle temp = circle1;
				circle1=circle2;
				circle2=temp;
			}
			int r = circle1.getRow();
			for (int c=circle1.getColumn();c<=circle2.getColumn();c++)
				board[r][c].changeStatus(status);
		}
		else if (circle1.getColumn()==circle2.getColumn())
		{
			if (circle1.getRow()>circle2.getRow())
			{
				Circle temp = circle1;
				circle1=circle2;
				circle2=temp;
			}
			int c = circle1.getColumn();
			for (int r=circle1.getRow();r<=circle2.getRow();r++)
				board[r][c].changeStatus(status);
		}
		else
		{
			if (circle1.getRow()>circle2.getRow())
			{
				Circle temp = circle1;
				circle1=circle2;
				circle2=temp;
			}
			int c = circle1.getColumn();
			for (int r=circle1.getRow();r<=circle2.getRow();r++)
			{
				board[r][c].changeStatus(status);
				c++;
			}
		}
		calcNumberOfEmptyCircles();
	}
	
	public boolean checkValidMove(Circle circle1, Circle circle2)
	{
		if (!circle1.checkEmpty()||!circle2.checkEmpty())
			return false;
		if (circle1.getRow()==circle2.getRow())
		{
			if (circle1.getColumn()>circle2.getColumn())
			{
				Circle temp = circle1;
				circle1=circle2;
				circle2=temp;
			}
			int r = circle1.getRow();
			for (int c=circle1.getColumn();c<=circle2.getColumn();c++)
			{
				if (!board[r][c].checkEmpty())
					return false;
			}
			return true;
		}
		else if (circle1.getColumn()==circle2.getColumn())
		{
			if (circle1.getRow()>circle2.getRow())
			{
				Circle temp = circle1;
				circle1=circle2;
				circle2=temp;
			}
			int c = circle1.getColumn();
			for (int r=circle1.getRow();r<=circle2.getRow();r++)
			{
				if (!board[r][c].checkEmpty())
					return false;
			}
			return true;
		}
		else if (circle2.getRow()-circle1.getRow()==circle2.getColumn()-circle1.getColumn())
		{
			if (circle1.getRow()>circle2.getRow())
			{
				Circle temp = circle1;
				circle1=circle2;
				circle2=temp;
			}
			int c = circle1.getColumn();
			for (int r=circle1.getRow();r<=circle2.getRow();r++)
			{
				if (!board[r][c].checkEmpty())
					return false;
				c++;
			}
			return true;
		}
		else
			return false;
	}
	
	public void drawBoard(Canvas canvas)
	{
		for (int r=0;r<5;r++)
		{
			for (int c=0;c<=r;c++)
				board[r][c].draw();
		}
		for (BoardMove boardMove : boardMoves)
		{
			boardMove.drawMove(canvas);
		}
	}
	
	public boolean checkDone()
	{
		return numberOfEmptyCircles==0;
	}
	
	private void calcNumberOfEmptyCircles()
	{
		numberOfEmptyCircles=0;
		for (int r=0;r<5;r++)
		{
			for (int c=0;c<=r;c++)
			{
				if (board[r][c].checkEmpty())
					numberOfEmptyCircles++;
			}
		}
	}
	
	public int getNumberOfEmptyCircles()
	{
		return numberOfEmptyCircles;
	}
	
	public boolean equals(Object obj)
	{
		Board board2 = (Board)(obj);
		if (board2.getNumberOfEmptyCircles()!=numberOfEmptyCircles)
			return false;
		for (int r=0;r<5;r++)
		{
			for (int c=0;c<=r;c++)
			{
				if (board2.board[r][c].checkEmpty()!=board[r][c].checkEmpty())
					return false;
			}
		}
		return true;
	}
	
	public void setClickable(boolean b)
	{
		for (int r=0;r<5;r++)
		{
			for (int c=0;c<=r;c++)
			{
				board[r][c].setClickable(b);
			}
		}
	}
	
	public int toInteger()
	{
		int sum=0;
		for (int row=0;row<5;row++)
		{
			for (int col=0;col<=row;col++)
			{
				int i = (row*(row+1))/2+col;
				if (!getCircleAt(row,col).checkEmpty())
					sum+=Math.pow(2,i);
			}
		}
		return (int) (sum+Math.pow(2, 15));
	}
}
