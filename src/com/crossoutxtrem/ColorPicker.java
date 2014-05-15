package com.crossoutxtrem;

import java.lang.reflect.Method;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.LinearLayout.LayoutParams;

public class ColorPicker extends TableLayout implements OnClickListener
{
	private ImageButton redButton;
	private ImageButton blueButton;
	private ImageButton greenButton;
	private ImageButton magButton;
	private ImageButton cyanButton;
	private ImageButton yellowButton;
	
	private int color;
	public ColorPicker(Context context, int color)
	{
		super(context);
		
		this.setGravity(Gravity.CENTER_HORIZONTAL);
		this.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
		redButton = new ImageButton(context);
		redButton.setOnClickListener(this);
		redButton.setImageResource(R.drawable.red);
		redButton.setPadding(10, 20, 20, 20);
		
		blueButton = new ImageButton(context);
		blueButton.setOnClickListener(this);
		blueButton.setImageResource(R.drawable.blue);
		blueButton.setPadding(20, 20, 20, 20);
		
		greenButton = new ImageButton(context);
		greenButton.setOnClickListener(this);
		greenButton.setImageResource(R.drawable.green);
		greenButton.setPadding(20, 20, 20, 20);
		
		yellowButton = new ImageButton(context);
		yellowButton.setOnClickListener(this);
		yellowButton.setImageResource(R.drawable.yellow);
		yellowButton.setPadding(20, 20, 20, 20);
		
		magButton = new ImageButton(context);
		magButton.setOnClickListener(this);
		magButton.setImageResource(R.drawable.magenta);
		magButton.setPadding(20, 20, 20, 20);
		
		cyanButton = new ImageButton(context);
		cyanButton.setOnClickListener(this);
		cyanButton.setImageResource(R.drawable.cyan);
		cyanButton.setPadding(20, 20, 20, 20);
		
		TableRow tr1 = new TableRow(context);
		tr1.setGravity(Gravity.CENTER_HORIZONTAL);
		tr1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		tr1.addView(redButton);
		tr1.addView(blueButton);
		tr1.addView(greenButton);
		
		TableRow tr2 = new TableRow(context);
		tr2.setGravity(Gravity.CENTER_HORIZONTAL);
		tr2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		tr2.addView(yellowButton);
		tr2.addView(magButton);
		tr2.addView(cyanButton);
		
		this.addView(tr1);
		this.addView(tr2);
		
		setColor(color);
	}
	
	public int getColor()
	{
		return color;
	}
	
	public void setColor(int color)
	{
		this.color=color;
		if (color==Color.RED)
		{
			redButton.setEnabled(false);
			redButton.setBackgroundColor(Color.DKGRAY);
			blueButton.setEnabled(true);
			blueButton.setBackgroundColor(Color.GRAY);
			greenButton.setEnabled(true);
			greenButton.setBackgroundColor(Color.GRAY);
			yellowButton.setEnabled(true);
			yellowButton.setBackgroundColor(Color.GRAY);
			magButton.setEnabled(true);
			magButton.setBackgroundColor(Color.GRAY);
			cyanButton.setEnabled(true);
			cyanButton.setBackgroundColor(Color.GRAY);
		}
		else if (color==Color.BLUE)
		{
			redButton.setEnabled(true);
			redButton.setBackgroundColor(Color.GRAY);
			blueButton.setEnabled(false);
			blueButton.setBackgroundColor(Color.DKGRAY);
			greenButton.setEnabled(true);
			greenButton.setBackgroundColor(Color.GRAY);
			yellowButton.setEnabled(true);
			yellowButton.setBackgroundColor(Color.GRAY);
			magButton.setEnabled(true);
			magButton.setBackgroundColor(Color.GRAY);
			cyanButton.setEnabled(true);
			cyanButton.setBackgroundColor(Color.GRAY);
		}
		else if (color==Color.GREEN)
		{
			redButton.setEnabled(true);
			redButton.setBackgroundColor(Color.GRAY);
			blueButton.setEnabled(true);
			blueButton.setBackgroundColor(Color.GRAY);
			greenButton.setEnabled(false);
			greenButton.setBackgroundColor(Color.DKGRAY);
			yellowButton.setEnabled(true);
			yellowButton.setBackgroundColor(Color.GRAY);
			magButton.setEnabled(true);
			magButton.setBackgroundColor(Color.GRAY);
			cyanButton.setEnabled(true);
			cyanButton.setBackgroundColor(Color.GRAY);
		}
		else if (color==Color.YELLOW)
		{
			redButton.setEnabled(true);
			redButton.setBackgroundColor(Color.GRAY);
			blueButton.setEnabled(true);
			blueButton.setBackgroundColor(Color.GRAY);
			greenButton.setEnabled(true);
			greenButton.setBackgroundColor(Color.GRAY);
			yellowButton.setEnabled(false);
			yellowButton.setBackgroundColor(Color.DKGRAY);
			magButton.setEnabled(true);
			magButton.setBackgroundColor(Color.GRAY);
			cyanButton.setEnabled(true);
			cyanButton.setBackgroundColor(Color.GRAY);
		}
		else if (color==Color.MAGENTA)
		{
			redButton.setEnabled(true);
			redButton.setBackgroundColor(Color.GRAY);
			blueButton.setEnabled(true);
			blueButton.setBackgroundColor(Color.GRAY);
			greenButton.setEnabled(true);
			greenButton.setBackgroundColor(Color.GRAY);
			yellowButton.setEnabled(true);
			yellowButton.setBackgroundColor(Color.GRAY);
			magButton.setEnabled(false);
			magButton.setBackgroundColor(Color.DKGRAY);
			cyanButton.setEnabled(true);
			cyanButton.setBackgroundColor(Color.GRAY);
		}
		else if (color==Color.CYAN)
		{
			redButton.setEnabled(true);
			redButton.setBackgroundColor(Color.GRAY);
			blueButton.setEnabled(true);
			blueButton.setBackgroundColor(Color.GRAY);
			greenButton.setEnabled(true);
			greenButton.setBackgroundColor(Color.GRAY);
			yellowButton.setEnabled(true);
			yellowButton.setBackgroundColor(Color.GRAY);
			magButton.setEnabled(true);
			magButton.setBackgroundColor(Color.GRAY);
			cyanButton.setEnabled(false);
			cyanButton.setBackgroundColor(Color.DKGRAY);
		}
	}
	
	public void setColor(View view)
	{
		if (view.equals(redButton))
			setColor(Color.RED);
		else if (view.equals(blueButton))
			setColor(Color.BLUE);
		else if (view.equals(greenButton))
			setColor(Color.GREEN);
		else if (view.equals(yellowButton))
			setColor(Color.YELLOW);
		else if (view.equals(magButton))
			setColor(Color.MAGENTA);
		else if (view.equals(cyanButton))
			setColor(Color.CYAN);
	}
	
	public void onClick(View view)
	{
		setColor(view);
	}
}
