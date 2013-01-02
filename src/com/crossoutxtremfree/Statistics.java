package com.crossoutxtremfree;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class Statistics extends Activity
{
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.statistics);
	    SharedPreferences prefs = this.getSharedPreferences("TriangleOfCircles", Context.MODE_PRIVATE);
	    int wins = prefs.getInt("wins", 0);
	    int losses = prefs.getInt("losses", 0);
	    int total = wins+losses;
	    double percent;
	    if (total==0)
	    	percent = 0;
	    else
	    	percent = (wins*100.0)/(double)total;
	    
	    TextView tv1 = (TextView) findViewById(R.id.textView3);
	    TextView tv2 = (TextView) findViewById(R.id.textView5);
	    TextView tv3 = (TextView) findViewById(R.id.textView9);
	    TextView tv4 = (TextView) findViewById(R.id.textView7);
	    
	    tv1.setText(wins+"");
	    tv2.setText(losses+"");
	    tv3.setText(total+"");
	    tv4.setText(String.format("%5.2f", percent)+"%");
	}
}
