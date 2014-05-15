package com.crossoutxtrem;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.LinearLayout;

public class BoardView extends LinearLayout
{
	private Bitmap bitmap;
	private int _width;
	private int _height;
	private Board board;
	
	public BoardView(Context context, View view)
	{
		super(context);
		view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		addView(view);
		this.setWillNotDraw(false);
		Circle.rootID=getId();
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        _height = View.MeasureSpec.getSize(heightMeasureSpec);
        _width = View.MeasureSpec.getSize(widthMeasureSpec);
        
        setMeasuredDimension(_width, _height);
        
        if (board!=null)
        	drawBoard(board);
    }
	
	public void drawBoard(Board board)
	{
		this.board = board;
		if (_width==0||_height==0)
			return;
		bitmap = Bitmap.createBitmap(_width, _height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        
        if (board!=null)
        	board.drawBoard(canvas);
        invalidate();
	}
	
	protected void dispatchDraw(Canvas canvas)
	{
		super.dispatchDraw(canvas);
		if (bitmap!=null)
			canvas.drawBitmap(bitmap, 0, 0, null);
	}

}
