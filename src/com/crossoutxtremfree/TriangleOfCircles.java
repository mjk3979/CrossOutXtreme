package com.crossoutxtremfree;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.crossoutxtremfree.Statistics;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;

public class TriangleOfCircles extends Activity implements OnClickListener
{
	public static boolean soundOn;
	private boolean eulaShowing;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);
        SharedPreferences prefs = this.getSharedPreferences("TriangleOfCircles", Context.MODE_PRIVATE);
        boolean eula = false;
        
        eula = prefs.getBoolean("eula", false);
        if (!eula)
        {
        	eulaShowing = true;
        	AlertDialog eulaBox = new AlertDialog.Builder(this).create();
        	eulaBox.setTitle("EULA");
        	eulaBox.setMessage(readTxt());
        	eulaBox.setButton("Agree", this);
        	eulaBox.setButton2("Disagree", this);
        	eulaBox.show();
        }
        else
        	startup();
    }
    
    private String readTxt()
    {
    	try
    	{
    		BufferedReader reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.eula)));
        
    		String text = "";
    		while (reader.ready())
    		{
    			String line = reader.readLine();
    			text += line + "\n";
    		}
    		reader.close();
    		return text;
    	}
    	catch (Exception ex)
    	{
    		finish();
    		return "ERROR";
    	}
    }
    
    private void startup()
    {
    	SharedPreferences prefs = this.getSharedPreferences("TriangleOfCircles", Context.MODE_PRIVATE);
    	AI.currentDifficulty=prefs.getInt("diff", 0);
        int sound = prefs.getInt("sound", -1);
        if (sound==-1)
        {
        	AlertDialog messageBox = new AlertDialog.Builder(this).create();
			messageBox.setTitle("Sound");
			messageBox.setMessage("Would you like to enable sound for this game (you can change this at any time in the \"Options\" menu)?");
			messageBox.setButton("Yes", this);
			messageBox.setButton2("No", this);
			messageBox.show();
        }
        else if (sound==0)
        {
        	soundOn = false;
        }
        else
        {
        	soundOn = true;
        	this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        }
    }
    
    public void twoPlayerButtonClicked(View view)
    {
		Intent tpIntent = new Intent(view.getContext(), TPOptions.class);
		startActivity(tpIntent);
    }
    
    public void singlePlayerButtonClicked(View view)
    {
    	Intent spIntent = new Intent(view.getContext(), SPOptions.class);
    	startActivity(spIntent);
    }
    
    public void optionsClicked(View view)
    {
    	Intent oIntent = new Intent(view.getContext(), Options.class);
    	startActivity(oIntent);
    }
    
    public void htpClicked(View view)
    {
    	Intent oIntent = new Intent(view.getContext(), HowToPlay.class);
    	startActivity(oIntent);
    }
    
    public void statsClicked(View view)
    {
    	Intent oIntent = new Intent(view.getContext(), Statistics.class);
    	startActivity(oIntent);
    }
    
    public void onClick(DialogInterface dialog, int which)
	{
    	if (eulaShowing)
    	{
    		if (which==-1)
    		{
    			eulaShowing = false;
    			Editor editor = this.getSharedPreferences("TriangleOfCircles", Context.MODE_PRIVATE).edit();
    			editor.putBoolean("eula", true);
    			editor.commit();
    			startup();
    		}
    		else
    			finish();
    	}
    	else
    	{
	    	if (which==-1)
	    	{
	    		soundOn = true;
	    		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
	    	}
	    	else
	    	{
	    		soundOn = false;
	    	}
	    	int sound = 0;
			if (soundOn)
				sound = 1;
			Editor editor = this.getSharedPreferences("TriangleOfCircles", Context.MODE_PRIVATE).edit();
			editor.putInt("diff", AI.currentDifficulty);
			editor.putInt("sound", sound);
			editor.commit();
    	}
	}
    
    public void onResume()
    {
    	super.onResume();
    	if (soundOn)
    		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
    	else
    		this.setVolumeControlStream(AudioManager.STREAM_RING);
    }
}