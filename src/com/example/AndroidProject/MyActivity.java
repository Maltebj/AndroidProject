package com.example.AndroidProject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    int mId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    public void buttonClick(View view){


        int id = view.getId() ;
        SharedPreferences preferences = getSharedPreferences("DrawPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        mId = preferences.getInt("IdOfPuzzle", 1);
        if (mId == 1)
        {
            editor.putInt("IdOfPuzzle",mId);
            editor.commit();
        }
        Intent intent;

        switch(id)
        {
            case R.id.button_play:
                intent = new Intent(this,PuzzleActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT,mId);
                startActivity(intent);
                break;
            case R.id.button_about:
                intent = new Intent(this,PuzzleListActivity.class);
                startActivity(intent);
                break;
        }
    }
}
