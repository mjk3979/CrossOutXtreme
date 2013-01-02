package com.crossoutxtremfree;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.crossoutxtremfree.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SinglePlayerGame extends Activity implements OnClickListener
{
	public static int whoGoesFirst;
	public static int playerColor;
	public static int computerColor;
	
	private boolean whoWentLast;
	private Circle selected;
	public Board board;
	public TextView playerLabel;
	private TextView winTextView;
	private TextView lossTextView;
	public ProgressBar bar;
	private SoundPool soundPool;
	private int clickId;
	public boolean playerTurn;
	public ArrayList<Combination> prevMoves;
	public BoardView boardView;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getLayoutInflater();
        boardView = new BoardView(this, inflater.inflate(R.layout.main, null));
        setContentView(boardView);
        whoWentLast = true;
        
        winTextView = (TextView) findViewById(R.id.textView2);
        lossTextView = (TextView) findViewById(R.id.textView3);
        
        if (TriangleOfCircles.soundOn)
	    {
	        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
	        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
			clickId = soundPool.load(this, R.raw.click, 1);
	    }
        newGame();
    }
	
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
	    if (keyCode == KeyEvent.KEYCODE_BACK)
	    {
	        undo(true);
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	public void closeClicked(View view)
    {
    	finish();
    }
	
	public void newGame()
    {
		setWinLossText();
    	Circle[][] circles = new Circle[5][5];
    	circles[0][0] = new Circle(0, 0, (ImageView) findViewById(R.id.imageView1));
    	circles[1][0] = new Circle(1, 0, (ImageView) findViewById(R.id.imageView2));
    	circles[1][1] = new Circle(1, 1, (ImageView) findViewById(R.id.imageView3));
    	circles[2][0] = new Circle(2, 0, (ImageView) findViewById(R.id.imageView4));
    	circles[2][1] = new Circle(2, 1, (ImageView) findViewById(R.id.imageView5));
    	circles[2][2] = new Circle(2, 2, (ImageView) findViewById(R.id.imageView6));
    	circles[3][0] = new Circle(3, 0, (ImageView) findViewById(R.id.imageView7));
    	circles[3][1] = new Circle(3, 1, (ImageView) findViewById(R.id.imageView8));
    	circles[3][2] = new Circle(3, 2, (ImageView) findViewById(R.id.imageView9));
    	circles[3][3] = new Circle(3, 3, (ImageView) findViewById(R.id.imageView10));
    	circles[4][0] = new Circle(4, 0, (ImageView) findViewById(R.id.imageView11));
    	circles[4][1] = new Circle(4, 1, (ImageView) findViewById(R.id.imageView12));
    	circles[4][2] = new Circle(4, 2, (ImageView) findViewById(R.id.imageView13));
    	circles[4][3] = new Circle(4, 3, (ImageView) findViewById(R.id.imageView14));
    	circles[4][4] = new Circle(4, 4, (ImageView) findViewById(R.id.imageView15));
    	
    	board = new Board(circles);
    	selected = null;
    	playerTurn = true;
    	
    	prevMoves = new ArrayList<Combination>();
    	
    	playerLabel = (TextView) findViewById(R.id.textView1);
    	bar = (ProgressBar) findViewById(R.id.progressBar1);
    	
    	if (whoGoesFirst==1||(whoGoesFirst==2&&!whoWentLast))
    	{
    		whoWentLast=true;
    		bar.setVisibility(View.INVISIBLE);
    		setPlayerLabel(true);
    	}
    	else
    	{
    		whoWentLast=false;
    		bar.setVisibility(View.VISIBLE);
    		setPlayerLabel(false);
    		board.setClickable(false);
    		Handler handler = new Handler();
			AI ai;
			if (AI.currentDifficulty==0)
				ai=new EasyAI(board, handler, this);
			else if (AI.currentDifficulty==1)
				ai=new HardAI(board, handler, this);
			else
			{
				BufferedReader reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.aidata)));
				ai=new SuperAI(board, handler, this, reader);
			}
			ai.start();
    	}
    	boardView.drawBoard(board);
    }
    
    public void imageClicked(View view)
    {
    	if (TriangleOfCircles.soundOn)
        {
			AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
			float actualVolume = (float) audioManager
					.getStreamVolume(AudioManager.STREAM_MUSIC);
			float maxVolume = (float) audioManager
					.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			float volume = actualVolume / maxVolume;
			soundPool.play(clickId, volume, volume, 1, 0, 1f);
        }

    	Circle clicked = board.findSelected((ImageView)view);
    	if (clicked.checkEmpty())
    	{
    		if (selected==null)
    		{
    			clicked.changeStatus(Circle.CLICKED);
    			selected = clicked;
    		}
    		else
    		{
    			if (board.checkValidMove(selected, clicked))
    			{
    				board.makeMove(selected, clicked, playerColor);
    				prevMoves.add(new Combination(selected, clicked));
    				selected = null;
    				if (board.checkDone())
    				{
    					boardView.drawBoard(board);
    					addStat(false);
    					AlertDialog messageBox = new AlertDialog.Builder(this).create();
    					messageBox.setTitle("Game Over");
    					messageBox.setMessage("Android Wins");
    					messageBox.setButton("Ok", this);
    					messageBox.show();
    					return;
    				}
    				playerTurn = false;
    				board.setClickable(false);
    				bar.setVisibility(View.VISIBLE);
    				setPlayerLabel(false);
    				Handler handler = new Handler();
    				AI ai;
    				if (AI.currentDifficulty==0)
    					ai=new EasyAI(board, handler, this);
    				else if (AI.currentDifficulty==1)
    					ai=new HardAI(board, handler, this);
    				else
    				{
    					BufferedReader reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.aidata)));
    					ai=new SuperAI(board, handler, this, reader);
    				}
    				ai.start();
    			}
    		}
    	}
    	else if (selected!=null)
    	{
    		selected.changeStatus(Circle.EMPTY);
    		selected=null;
    	}
    	boardView.drawBoard(board);
    }
    
    public void undo(boolean finish)
    {
    	if (!playerTurn)
    		return;
    	if (selected!=null)
    	{
    		selected.changeStatus(Circle.EMPTY);
    		selected = null;
    	}
    	else if (prevMoves.size()>1)
    	{
    		Combination lastMove = prevMoves.get(prevMoves.size()-1);
    		board.undoMove(lastMove.getCircle1(), lastMove.getCircle2());
    		prevMoves.remove(prevMoves.size()-1);
    		
    		lastMove = prevMoves.get(prevMoves.size()-1);
    		board.undoMove(lastMove.getCircle1(), lastMove.getCircle2());
    		prevMoves.remove(prevMoves.size()-1);
    	}
    	else if (finish)
    	{
    		finish();
    		return;
    	}
    	boardView.drawBoard(board);
    }
    
    public void undoClicked(View view)
    {
    	undo(false);
    }
    
    public void setPlayerLabel(boolean human)
    {
    	if (human)
    		playerLabel.setText("Your Turn");
    	else
    		playerLabel.setText("My Turn");
    }
    
    public void onClick(DialogInterface dialog, int which)
	{
    	newGame();
	}
    
    public void addStat(boolean win)
    {
    	SharedPreferences prefs = this.getSharedPreferences("TriangleOfCircles", Context.MODE_PRIVATE);
    	Editor editor = prefs.edit();
    	if (win)
    	{
    		int wins = prefs.getInt("wins", 0);
    		wins++;
    		editor.putInt("wins", wins);
    	}
    	else
    	{
    		int losses = prefs.getInt("losses", 0);
    		losses++;
    		editor.putInt("losses", losses);
    	}
    	editor.commit();
    }
    
    public void setWinLossText()
    {
    	SharedPreferences prefs = this.getSharedPreferences("TriangleOfCircles", Context.MODE_PRIVATE);
        int wins = prefs.getInt("wins", 0);
        int losses = prefs.getInt("losses", 0);
        winTextView.setText("Wins:    " + wins);
        lossTextView.setText("Losses: " + losses);
    }
}
