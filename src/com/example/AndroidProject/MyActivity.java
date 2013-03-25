package com.example.AndroidProject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    public void buttonClick(View view){
        int id = view.getId() ;

        Intent intent;

        switch(id)
        {
            case R.id.button_play:
                intent = new Intent(this,PuzzleActivity.class);
                startActivity(intent);
                break;
            case R.id.button_about:
                intent = new Intent(this,PuzzleListActivity.class);
                startActivity(intent);
                break;
        }
    }
}
