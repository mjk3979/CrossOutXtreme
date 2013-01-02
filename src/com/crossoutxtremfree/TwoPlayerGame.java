package com.crossoutxtremfree;

import java.util.ArrayList;

import com.crossoutxtremfree.BoardView;
import com.crossoutxtremfree.R;
/*import com.mobclix.android.sdk.MobclixAdView;
import com.mobclix.android.sdk.MobclixFullScreenAdView;
import com.mobclix.android.sdk.MobclixMMABannerXLAdView;*/

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TwoPlayerGame extends Activity implements OnClickListener
{
	public static int whoGoesFirst;
	public static int p1Color;
	public static int p2Color;
	
	private Circle selected;
	private Board board;
	private boolean player1Turn;
	private TextView playerLabel;
	private SoundPool soundPool;
	private int clickId;
	private ArrayList<Combination> prevMoves;
	private boolean whoWentLast;
	private boolean firstRun;
	private TextView winTextView;
	private TextView lossTextView;
	private int wins;
	private int losses;
	private BoardView boardView;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getLayoutInflater();
        boardView = new BoardView(this, inflater.inflate(R.layout.main, null));
        setContentView(boardView);
        
        /*MobclixAdView adview = new MobclixMMABannerXLAdView(this);
        adview.addMobclixAdViewListener(new MyAdViewListener());
        LinearLayout parentView = (LinearLayout) findViewById(R.id.linearLayout10);
        parentView.addView(adview);*/
        firstRun = true;
        
        wins = 0;
        losses = 0;
        winTextView = (TextView) findViewById(R.id.textView2);
        lossTextView = (TextView) findViewById(R.id.textView3);
        
        if (TriangleOfCircles.soundOn)
        {
        	this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        	soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        	clickId = soundPool.load(this, R.raw.click, 1);
        }
        whoWentLast = false;
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
	
	private void newGame()
    {
		setWinLossText();
		if (firstRun)
			firstRun = false;
		else
		{
			/*MobclixFullScreenAdView adview = new MobclixFullScreenAdView(this);
			adview.requestAndDisplayAd();*/
		}
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
    	
    	prevMoves = new ArrayList<Combination>();
    	playerLabel = (TextView) findViewById(R.id.textView1);
    	if (whoGoesFirst==0||(whoGoesFirst==2&&!whoWentLast))
    	{
    		whoWentLast=true;
    		player1Turn = true;
    	}
    	else
    	{
    		whoWentLast=false;
    		player1Turn = false;
    	}
    	setPlayerLabel();
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
    				int color;
    				if (player1Turn)
    					color = p1Color;
    				else
    					color = p2Color;
    				board.makeMove(selected, clicked, color);
    				
    				prevMoves.add(new Combination(selected, clicked));
    				selected = null;
    				if (board.checkDone())
    				{
    					boardView.drawBoard(board);
    					AlertDialog messageBox = new AlertDialog.Builder(this).create();
    					messageBox.setTitle("Game Over");
    					if (player1Turn)
    					{
    						addStat(false);
    						messageBox.setMessage("Player 2 Wins");
    					}
    					else
    					{
    						addStat(true);
    						messageBox.setMessage("Player 1 Wins");
    					}
    					messageBox.setButton("Ok", this);
    					messageBox.show();
    					return;
    				}
    				player1Turn = !player1Turn;
    				setPlayerLabel();
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
    
    public void closeClicked(View view)
    {
    	finish();
    }
    
    public void undo(boolean finish)
    {
    	if (selected!=null)
    	{
    		selected.changeStatus(Circle.EMPTY);
    		selected = null;
    	}
    	else if (prevMoves.size()>0)
    	{
    		Combination lastMove = prevMoves.get(prevMoves.size()-1);
    		board.undoMove(lastMove.getCircle1(), lastMove.getCircle2());
    		prevMoves.remove(prevMoves.size()-1);
    		player1Turn = !player1Turn;
    		setPlayerLabel();
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
    
    public void setPlayerLabel()
    {
    	if (player1Turn)
    		playerLabel.setText("Player 1 Turn");
    	else
    		playerLabel.setText("Player 2 Turn");
    }
    
    public void onClick(DialogInterface dialog, int which)
	{
    	newGame();
	}
    
    public void addStat(boolean win)
    {
    	if (win)
    		wins++;
    	else
    		losses++;
    }
    
    public void setWinLossText()
    {
        winTextView.setText("Player 1 Wins:  " + wins);
        lossTextView.setText("Player 2 Wins: " + losses);
    }
}
