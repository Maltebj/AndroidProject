package com.example.AndroidProject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tryggvim
 * Date: 23.3.2013
 * Time: 19:17
 * To change this template use File | Settings | File Templates.
 */
public class PuzzleActivity extends Activity {

    PuzzleView mPuzzleView;
    TextView mMoveView;
    TextView mLevelView;
    String m_puzzle;
    List<Block> m_blocks;
    Button mPrevButton;
    Button mNextButton;
    Button mRestartButton;
    PuzzleHandler puzzleHandler = new PuzzleHandler();
    int mId;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzle);
        mPuzzleView = (PuzzleView) findViewById(R.id.puzzle_view);
        mMoveView = (TextView) findViewById(R.id.total_moves);
        mPrevButton = (Button) findViewById(R.id.prevButton);
        mNextButton = (Button) findViewById(R.id.nextButton);
        mRestartButton = (Button) findViewById(R.id.puzzleRestart);
        mLevelView = (TextView) findViewById(R.id.LevelNumber);


        SharedPreferences preferences = getSharedPreferences("DrawPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        mId = preferences.getInt("IdOfPuzzle", 0);
        if (mId == 1)
        {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {

            }else
            {

            String value1 = extras.getString(Intent.EXTRA_TEXT);
            if (value1 != null) {
                mId = Integer.parseInt(value1);
            }
            }
        }
        mLevelView.setText(Integer.toString(mId));



        AssetManager manager = getAssets();
        try{
            m_puzzle = puzzleHandler.readChallenge( manager.open("challenge.xml"),mId);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        if(m_puzzle != null){
           m_blocks = puzzleHandler.setup(m_puzzle);
        }

        mPuzzleView.post(new Runnable() {

            @Override
            public void run() {
               mPuzzleView.addShapes(m_blocks,mId);
            }
        });
        mPuzzleView.setCustomEventHandler(new DragEventHandler() {
            @Override
            public void onShapeMoved() {
                Integer move = Integer.parseInt(mMoveView.getText().toString());
                ++move;
                mMoveView.setText(move.toString());
            }
        });

    }
    public void puzzle(View view){

        mMoveView.setText("0");
        int id = view.getId();
        AssetManager manager = getAssets();
        switch(id){

            case R.id.nextButton:
            {
                mId++;

                SharedPreferences preferences = getSharedPreferences("DrawPrefs",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putInt("IdOfPuzzle",mId);
                editor.commit();
                try{
                    m_puzzle = puzzleHandler.readChallenge( manager.open("challenge.xml"),mId);
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                if(m_puzzle != null){
                    m_blocks = puzzleHandler.setup(m_puzzle);
                }
                mLevelView.setText(Integer.toString(mId));
                mPuzzleView.post(new Runnable() {

                    @Override
                    public void run() {
                        mPuzzleView.addShapes(m_blocks,mId);
                    }
                });
                break;
            }
            case R.id.prevButton:{
                if(mId >1){
                    SharedPreferences preferences = getSharedPreferences("DrawPrefs",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();

                    editor.putInt("IdOfPuzzle",mId);
                    editor.commit();
                    mId--;
                    try{
                        m_puzzle = puzzleHandler.readChallenge( manager.open("challenge.xml"),mId);
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                    if(m_puzzle != null){
                        m_blocks = puzzleHandler.setup(m_puzzle);
                    }
                    mLevelView.setText(Integer.toString(mId));
                    mPuzzleView.post(new Runnable() {

                        @Override
                        public void run() {
                            mPuzzleView.addShapes(m_blocks,mId);
                        }
                    });

                }
                break;
            }
            case R.id.puzzleRestart:{
                    try{
                        m_puzzle = puzzleHandler.readChallenge( manager.open("challenge.xml"),mId);
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                    if(m_puzzle != null){
                        m_blocks = puzzleHandler.setup(m_puzzle);
                    }
                    mLevelView.setText(Integer.toString(mId));
                    mPuzzleView.post(new Runnable() {

                        @Override
                        public void run() {
                            mPuzzleView.addShapes(m_blocks,mId);
                        }
                    });
                break;
                }
            }
        }
    public void puzzleWon(){
        Intent i = new Intent(this,NextLevelActivity.class);
        SharedPreferences preferences = getSharedPreferences("DrawPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("IdOfPuzzle",mId);
        editor.commit();
        i.putExtra(Intent.EXTRA_TEXT, Integer.toString(mId));
        i.putExtra("totalMoves", mMoveView.getText());
         startActivity(i);
    }
    }