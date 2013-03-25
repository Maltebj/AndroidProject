package com.example.AndroidProject;

import android.*;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tryggvim
 * Date: 23.3.2013
 * Time: 19:17
 * To change this template use File | Settings | File Templates.
 */
public class PuzzleListActivity extends ListActivity {

    private  List<Integer> puzzles = new ArrayList<Integer>();
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        PuzzleHandler puzzleHandler = new PuzzleHandler();
        AssetManager manager = getAssets();
       try{
        puzzles = puzzleHandler.readChallenge( manager.open("challenge.xml"));
       }catch(Exception ex){
          ex.printStackTrace();
       }


        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1,puzzles);
        setListAdapter(adapter);




    }

    @Override
    protected void onListItemClick(ListView lv, View v,int position, long id){

        Intent intent = new Intent(this,PuzzleActivity.class);
        SharedPreferences preferences = getSharedPreferences("DrawPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt("IdOfPuzzle",puzzles.get(position));
        editor.commit();

        startActivity(intent);
    }
}