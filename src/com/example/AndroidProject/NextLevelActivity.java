package com.example.AndroidProject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created with IntelliJ IDEA.
 * User: tryggvim
 * Date: 29.3.2013
 * Time: 13:23
 * To change this template use File | Settings | File Templates.
 */
public class NextLevelActivity extends Activity {

    Button mRestartButton;
    Button mNextButton;
    Intent intent;
    int levelId;
    TextView mwinScreenTotalMoves;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nextlevel);
        mRestartButton = (Button) findViewById(R.id.Restart);
        mNextButton = (Button) findViewById(R.id.NextLevelButton);
        mwinScreenTotalMoves = (TextView) findViewById(R.id.winScreenTotalMoves);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {

        }else {
            String value1 = extras.getString(Intent.EXTRA_TEXT);
            String value2 = extras.getString("totalMoves","0");
            if (value1 != null) {
                levelId  = Integer.parseInt(value1);
                mwinScreenTotalMoves.setText(value2);
            }
        }


    }
    public void LevelChange (View view){
        int id = view.getId();
        if(id == R.id.Restart){
            intent = new Intent(this,PuzzleActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, Integer.toString(levelId));
        }
        else if(id == R.id.NextLevelButton){

            intent = new Intent(this,PuzzleActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, Integer.toString(++levelId));
        }
        startActivity(intent);
    }
}