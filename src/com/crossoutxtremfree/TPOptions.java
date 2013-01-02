package com.crossoutxtremfree;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

public class TPOptions extends Activity implements OnClickListener
{
	private ToggleButton tb1;
	private ToggleButton tb2;
	private ToggleButton tb3;
	private ColorPicker p1Picker;
	private ColorPicker p2Picker;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tpoptions);
        SharedPreferences prefs = this.getSharedPreferences("TriangleOfCircles", Context.MODE_PRIVATE);
        int first = prefs.getInt("tpfirst", 0);
        tb1 = (ToggleButton) findViewById(R.id.toggleButton1);
        tb2 = (ToggleButton) findViewById(R.id.toggleButton2);
        tb3 = (ToggleButton) findViewById(R.id.toggleButton5);
        
        if (first==0)
        	tb1.setChecked(true);
        else if (first==1)
        	tb2.setChecked(true);
        else
        	tb3.setChecked(true);
        
        p1Picker = new ColorPicker(this, prefs.getInt("p1Color", Color.RED));
        p2Picker = new ColorPicker(this, prefs.getInt("p2Color", Color.BLUE));
        
        ((LinearLayout) findViewById(R.id.linearLayout9)).addView(p1Picker);
        ((LinearLayout) findViewById(R.id.linearLayout10)).addView(p2Picker);
    }
	
	public void moveClicked(View view)
	{
		int id = view.getId();
		if (id==tb1.getId())
		{
			if (tb1.isChecked())
			{
				tb2.setChecked(false);
				tb3.setChecked(false);
			}
			else
			{
				tb1.setChecked(true);
			}
		}
		else if (id==tb2.getId())
		{
			if (tb2.isChecked())
			{
				tb1.setChecked(false);
				tb3.setChecked(false);
			}
			else
			{
				tb2.setChecked(true);
			}
		}
		else
		{
			if (tb3.isChecked())
			{
				tb2.setChecked(false);
				tb1.setChecked(false);
			}
			else
			{
				tb3.setChecked(true);
			}
		}
	}
	
	public void backClicked(View view)
	{
		finish();
	}
	
	public void startClicked(View view)
	{
		if (p1Picker.getColor()==p2Picker.getColor())
		{
			AlertDialog messageBox = new AlertDialog.Builder(this).create();
			messageBox.setTitle("Invalid");
			messageBox.setMessage("The color for player 1 and the color for player 2 must be different.");
			messageBox.setButton("Ok", this);
			messageBox.show();
			return;
		}
		int move;
		if (tb1.isChecked())
			move = 0;
		else if (tb2.isChecked())
			move = 1;
		else
			move = 2;
		TwoPlayerGame.whoGoesFirst = move;
		TwoPlayerGame.p1Color = p1Picker.getColor();
		TwoPlayerGame.p2Color = p2Picker.getColor();
		Editor editor = this.getSharedPreferences("TriangleOfCircles", Context.MODE_PRIVATE).edit();
		editor.putInt("tpfirst", move);
		editor.putInt("p1Color", TwoPlayerGame.p1Color);
		editor.putInt("p2Color", TwoPlayerGame.p2Color);
		editor.commit();
		
		Intent spIntent = new Intent(view.getContext(), TwoPlayerGame.class);
    	startActivity(spIntent);
    	finish();
	}
	
	public void onClick(DialogInterface dialog, int which)
	{
	}
}
