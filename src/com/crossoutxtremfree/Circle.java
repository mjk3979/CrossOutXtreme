package com.crossoutxtremfree;

import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.widget.ImageView;

public class Circle
{
	public static final int EMPTY = 0;
	public static final int HORZ = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	public static final int CLICKED = 4;
	
	private static int[] imageIds;
	private static boolean imagesLoaded = false;
	public static int rootID;
	
	private int row;
	private int column;
	private int status;
	private ImageView imageView;
	
	public Circle (int row, int column, ImageView imageView)
	{
		this.row = row;
		this.column = column;
		status = 0;
		this.imageView = imageView;
		
		if (!imagesLoaded)
			setImages();
	}
	
	public Circle (Circle circle1)
	{
		this.row=circle1.row;
		this.column=circle1.column;
		this.status = circle1.status;
		this.imageView=circle1.imageView;
	}
	
	public int getRow()
	{
		return row;
	}
	
	public int getColumn()
	{
		return column;
	}
	
	public int getStatus()
	{
		return status;
	}
	
	public void changeStatus(int status)
	{
		this.status = status;
	}
	
	public int getImageViewId()
	{
		return imageView.getId();
	}
	
	public void draw()
	{
		if (status==CLICKED)
			imageView.setImageResource(imageIds[status]);
		else
			imageView.setImageResource(imageIds[EMPTY]);
	}
	
	public boolean checkEmpty()
	{
		if (status==0||status==4)
			return true;
		else
			return false;
	}
	
	private static void setImages()
	{
		imageIds = new int[5];
		imageIds[0]=R.drawable.blank;
		imageIds[1]=R.drawable.horiz;
		imageIds[2]=R.drawable.left;
		imageIds[3]=R.drawable.right;
		imageIds[4]=R.drawable.clicked;
		imagesLoaded=true;
	}
	
	public boolean equals(Circle c)
	{
		return row==c.row&&column==c.column&&status==c.status;
	}
	
	public void setClickable(boolean b)
	{
		imageView.setClickable(b);
	}
	
	public Point getLeftSide()
	{
		int x = getRelativeLeft(imageView);
		int y = getRelativeTop(imageView) + (getHeight()/2);
		return new Point(x,y);
	}
	
	public Point getRightSide()
	{
		int x = getRelativeLeft(imageView) + getWidth();
		int y = getRelativeTop(imageView) + (getHeight()/2);
		return new Point(x,y);
	}
	
	public Point getUpperLeftCorner()
	{
		int radius = getWidth()/2;
		int centerX = getRelativeLeft(imageView) + radius;
		int centerY = getRelativeTop(imageView) + radius;
		int x = centerX-(int)(radius*Math.cos(Math.PI/3.0));
		int y = centerY-(int)(radius*Math.sin(Math.PI/3.0));
		return new Point(x,y);
	}
	
	public Point getUpperRightCorner()
	{
		int radius = getWidth()/2;
		int centerX = getRelativeLeft(imageView) + radius;
		int centerY = getRelativeTop(imageView) + radius;
		int x = centerX+(int)(radius*Math.cos(Math.PI/3.0));
		int y = centerY-(int)(radius*Math.sin(Math.PI/3.0));
		return new Point(x,y);
	}
	
	public Point getBottomLeftCorner()
	{
		int radius = getWidth()/2;
		int centerX = getRelativeLeft(imageView) + radius;
		int centerY = getRelativeTop(imageView) + radius;
		int x = centerX-(int)(radius*Math.cos(Math.PI/3.0));
		int y = centerY+(int)(radius*Math.sin(Math.PI/3.0));
		return new Point(x,y);
	}
	
	public Point getBottomRightCorner()
	{
		int radius = getWidth()/2;
		int centerX = getRelativeLeft(imageView) + radius;
		int centerY = getRelativeTop(imageView) + radius;
		int x = centerX+(int)(radius*Math.cos(Math.PI/3.0));
		int y = centerY+(int)(radius*Math.sin(Math.PI/3.0));
		return new Point(x,y);
	}
	
	private int getRelativeLeft(View myView){
	    if(myView.getId()==rootID)
	        return 0;
	    else
	        return myView.getLeft() + getRelativeLeft((View)myView.getParent());
	}


	private int getRelativeTop(View myView){
	    if(myView.getId()==rootID)
	        return -5;
	    else
	        return myView.getTop() + getRelativeTop((View)myView.getParent());
	}

	public int getWidth()
	{
		return imageView.getWidth();
	}
	
	public int getHeight()
	{
		return imageView.getHeight() + 10;
	}
}
