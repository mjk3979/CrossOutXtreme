package com.crossoutxtremfree;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

public class BoardMove extends Combination
{
	private int color;
	public BoardMove(Circle circle1, Circle circle2, int color)
	{
		super(circle1, circle2);
		this.color=color;
	}
	
	public void drawMove(Canvas canvas)
	{
		Circle c1 = getCircle1();
		Circle c2 = getCircle2();
		
		Paint paint = new Paint();
	    paint.setColor(color);
	    paint.setStyle(Paint.Style.STROKE);
	    paint.setStrokeWidth(c1.getHeight()/6);
	    
		if (c1.getRow()>c2.getRow()||(c1.getRow()==c2.getRow()&&c1.getColumn()>c2.getColumn()))
		{
			Circle temp = new Circle(c1);
			c1 = new Circle(c2);
			c2 = new Circle(temp);
		}
		
		int status = c1.getStatus();
		Point[] line = new Point[2];
		if (status==Circle.HORZ)
		{
			line[0]=c1.getLeftSide();
			line[1]=c2.getRightSide();
		}
		else if (status==Circle.LEFT)
		{
			line[0]=c1.getUpperLeftCorner();
			line[1]=c2.getBottomRightCorner();
		}
		else if (status==Circle.RIGHT)
		{
			line[0]=c1.getUpperRightCorner();
			line[1]=c2.getBottomLeftCorner();
		}
		else
		{
			return;
		}
		canvas.drawLine(line[0].x, line[0].y, line[1].x, line[1].y, paint);
	}
}
