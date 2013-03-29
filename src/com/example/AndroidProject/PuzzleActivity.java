package com.example.AndroidProject;

import android.app.Activity;
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
    PuzzleHandler puzzleHandler = new PuzzleHandler();
    int mId;     //TODO LAGA FYRIR BÍL

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzle);
        mPuzzleView = (PuzzleView) findViewById(R.id.puzzle_view);
        mMoveView = (TextView) findViewById(R.id.total_moves);
        mPrevButton = (Button) findViewById(R.id.prevButton);
        mNextButton = (Button) findViewById(R.id.nextButton);
        mLevelView = (TextView) findViewById(R.id.LevelNumber);


        SharedPreferences preferences = getSharedPreferences("DrawPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        mId = preferences.getInt("IdOfPuzzle", 1);  // TODO SETJA ALLA BÍLA INN.
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
               mPuzzleView.addShapes(m_blocks);
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

        int id = view.getId();
        AssetManager manager = getAssets();
        switch(id){

            case R.id.nextButton:
            {
                mId++;
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
                        mPuzzleView.addShapes(m_blocks);
                    }
                });
                break;
            }
            case R.id.prevButton:{
                if(mId >1){
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
                            mPuzzleView.addShapes(m_blocks);
                        }
                    });

                }
                break;
            }
        }
    }
}