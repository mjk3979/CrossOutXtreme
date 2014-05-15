package com.crossoutxtrem;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.ToggleButton;

public class Options extends Activity
{
	private ToggleButton tb1;
	private ToggleButton tb2;
	private ToggleButton tb3;
	private ToggleButton tb4;
	private ToggleButton tb5;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);
        SharedPreferences prefs = this.getSharedPreferences("TriangleOfCircles", Context.MODE_PRIVATE);
        int sound = prefs.getInt("sound", -1);
        if (sound==0)
        	TriangleOfCircles.soundOn = false;
        else
        	TriangleOfCircles.soundOn = true;
        tb1 = (ToggleButton) findViewById(R.id.toggleButton1);
        tb2 = (ToggleButton) findViewById(R.id.toggleButton2);
        tb3 = (ToggleButton) findViewById(R.id.toggleButton3);
        tb4 = (ToggleButton) findViewById(R.id.toggleButton4);
        tb5 = (ToggleButton) findViewById(R.id.toggleButton5);
        if (TriangleOfCircles.soundOn)
        {
        	tb3.setChecked(true);
        	tb4.setChecked(false);
        }
        else
        {
        	tb3.setChecked(false);
        	tb4.setChecked(true);
        }
        
        AI.currentDifficulty = prefs.getInt("diff", 0);
        if (AI.currentDifficulty==0)
        	tb1.setChecked(true);
        else if (AI.currentDifficulty==1)
        	tb2.setChecked(true);
        else
        	tb5.setChecked(true);
    }
	
	public void saveClicked(View view)
	{
		if (tb3.isChecked())
			TriangleOfCircles.soundOn = true;
		else
			TriangleOfCircles.soundOn = false;
		int sound = 0;
		if (TriangleOfCircles.soundOn)
			sound = 1;
		
		if (tb1.isChecked())
			AI.currentDifficulty=0;
		else if (tb2.isChecked())
			AI.currentDifficulty=1;
		else
			AI.currentDifficulty=2;
		Editor editor = this.getSharedPreferences("TriangleOfCircles", Context.MODE_PRIVATE).edit();
		editor.putInt("sound", sound);
		editor.putInt("diff", AI.currentDifficulty);
		editor.commit();
		this.finish();
	}
	
	public void cancelClicked(View view)
	{
		this.finish();
	}
	
	public void soundClicked(View view)
	{
		if (view.equals(tb3))
		{
			if (tb3.isChecked())
			{
				tb4.setChecked(false);
			}
			else
			{
				tb3.setChecked(true);
			}
		}
		else
		{
			if (tb4.isChecked())
			{
				tb3.setChecked(false);
			}
			else
			{
				tb4.setChecked(true);
			}
		}
	}
	
	public void diffClicked(View view)
	{
		if (view.getId()==tb1.getId())
		{
			if (tb1.isChecked())
			{
				tb2.setChecked(false);
				tb5.setChecked(false);
			}
			else
				tb1.setChecked(true);
		}
		else if (view.getId()==tb2.getId())
		{
			if (tb2.isChecked())
			{
				tb1.setChecked(false);
				tb5.setChecked(false);
			}
			else
				tb2.setChecked(true);
		}
		else
		{
			if (tb5.isChecked())
			{
				tb1.setChecked(false);
				tb2.setChecked(false);
			}
			else
				tb5.setChecked(true);
		}
	}
}
