package com.example.AndroidProject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: tryggvim
 * Date: 23.3.2013
 * Time: 19:18
 * To change this template use File | Settings | File Templates.
 */
public class PuzzleView extends View {


    public static final int NUM_COLS = 6;
    public static final int NUM_ROWS = 6;
    public static final int GOAL_COL = 5;
    public static final int GOAL_ROW = 3;
    private int id;
    private int SHAPE_SIZE = getWidth();
    private Paint mPaint = new Paint();
    private Block mMovingShape = null;
    private List<Block> mBlocks = new ArrayList<Block>();
    private Random mRandom = new Random();
    private DragEventHandler mListener = null;

    public PuzzleView(Context context, AttributeSet attrs){
        super(context,attrs);
        setBackgroundColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void setCustomEventHandler(DragEventHandler listener){
        mListener = listener;
    }

    @Override protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        int size = Math.min(widthMeasureSpec,heightMeasureSpec);
        setMeasuredDimension(size,size);
    }

    public void addShapes(List<Block> blocks,int i){
        mBlocks.clear();
        id = i;
        for(Block b:blocks)
        {
        Rect rect = new Rect();
            SHAPE_SIZE = getWidth()/NUM_COLS;
        //int x = mRandom.nextInt(getWidth()-SHAPE_SIZE);
        int y = b.getRow()*SHAPE_SIZE;
        int x = b.getCol()*SHAPE_SIZE;
        int x1 = x+SHAPE_SIZE;
        int y1 = y+SHAPE_SIZE;
        if(b.getOrientation() == Block.Orientation.Horizontal){
          x1 = x+SHAPE_SIZE*b.getLength();
        }
        if(b.getOrientation() == Block.Orientation.Vertical){
            y1 = y+SHAPE_SIZE*b.getLength();
        }

        rect.set(x,y,x1,y1);
        b.setRect(rect);

        mBlocks.add(b);
        }
        invalidate();
    }
    protected void onDraw(Canvas canvas){

        for(Block block: mBlocks ){
            mPaint.setColor(block.getColor());
            canvas.drawRect(block.getRect(),mPaint);
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent){

        int x = (int)motionEvent.getX();
        int y = (int)motionEvent.getY();

        switch(motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                mMovingShape = shapeLocatedOn(x,y);
                break;
            case MotionEvent.ACTION_MOVE:
                if(mMovingShape != null){
                    if(mMovingShape.getOrientation() == Block.Orientation.Vertical){

                         //  y = Math.max(getBlockFromBottom(x,mMovingShape.getRect().bottom),Math.min(y,getBlockFromTop(x,mMovingShape.getRect().top)));
                            y = Math.max(getBlockFromTop(x,mMovingShape.getRect().top),Math.min(y,getBlockFromBottom(x,mMovingShape.getRect().bottom)));
                            x = mMovingShape.getRect().left;

                    }
                    else{

                        x = Math.max(getBlockFromLeft(mMovingShape.getRect().left,y),Math.min(x,getBlockFromRight(mMovingShape.getRect().right,y)));
                        y = mMovingShape.getRect().top;

                    }

                        mMovingShape.getRect().offsetTo(x,y);
                        invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if(mMovingShape != null){
                    SnapToGrid();
                    mMovingShape = null;
                    if(mListener != null){
                        mListener.onShapeMoved();

                        invalidate();
                        if(shapeLocatedOn(SHAPE_SIZE*GOAL_COL,SHAPE_SIZE*GOAL_ROW) != null)
                        {
                            if(shapeLocatedOn(SHAPE_SIZE*GOAL_COL,SHAPE_SIZE*GOAL_ROW).getColor() == Color.RED){
                                PuzzleActivity p = (PuzzleActivity)super.getContext();
                                p.puzzleWon();

                            }
                        }
                    }
                }
                break;

        }


        return true;
    }
    private Block shapeLocatedOn(int x, int y){
        for(Block block:mBlocks){
            if ( block.getRect().contains(x, y)){
                return block;
            }
        }
        return null;
    }

    private int getBlockFromLeft(int x,int y){



        int number = x+1;
        Double a = 0.0;
        int finalValue;
        while(number > 0)
        {
           Block nextBlock = shapeLocatedOn(number,y);
            if(nextBlock != null && nextBlock.getRect().left != mMovingShape.getRect().left && nextBlock.getRect().right != mMovingShape.getRect().right )
            {
                a = (double)number;
                break;
            }
            number = number - SHAPE_SIZE/2;
        }

        a = a/SHAPE_SIZE;
        finalValue = (int)Math.ceil(a);
        return (finalValue*SHAPE_SIZE);
    }
    private int getBlockFromRight(int x,int y){


        //
        int number = x-1;
        Double a = (double)number;
        int finalValue;
        while(number < getWidth())
        {
            Block nextBlock = shapeLocatedOn(number,y);
            if(nextBlock != null && nextBlock.getRect().left != mMovingShape.getRect().left && nextBlock.getRect().right != mMovingShape.getRect().right )
            {
                a = (double)number;
                break;
            }
            number = number + SHAPE_SIZE;
        }
        a = a/SHAPE_SIZE;
        finalValue = (int)Math.floor(a);
        if(number >= getWidth())
        {
            return getWidth()-SHAPE_SIZE*mMovingShape.getLength();
        }
        return (finalValue*SHAPE_SIZE-(mMovingShape.getLength()*SHAPE_SIZE));

    }
    private int getBlockFromTop(int x,int y){

        int number = y-1;
        Double a = 0.0;
        int finalValue;
        while(number > 0)
        {
            Block nextBlock = shapeLocatedOn(x,number);
            if(nextBlock != null && nextBlock.getRect().top != mMovingShape.getRect().top && nextBlock.getRect().bottom != mMovingShape.getRect().bottom )
            {
                a = (double)number;
                break;
            }
            number = number - (SHAPE_SIZE/2);
        }
        a = a/SHAPE_SIZE;
        finalValue = (int)Math.ceil(a);
        return (finalValue*SHAPE_SIZE);
    }
    private int getBlockFromBottom(int x,int y){


        //
        int number = y+1;
        Double a = (double)getWidth();
        int finalValue;
        while(number < getHeight())
        {
            Block nextBlock = shapeLocatedOn(x,number);
            if(nextBlock != null && nextBlock.getRect().bottom != mMovingShape.getRect().bottom)
            {
                a = (double)number;
                break;
            }
            number = number + (SHAPE_SIZE/2);
        }
        a = a/SHAPE_SIZE;
        System.out.println(a);
        finalValue = (int)Math.floor(a);
        System.out.println(finalValue);
        if(finalValue >= 6)
        {
            return getHeight()-SHAPE_SIZE*mMovingShape.getLength();
        }
        return (finalValue*SHAPE_SIZE-(mMovingShape.getLength()*SHAPE_SIZE));

    }
    private void SnapToGrid(){
        if (mMovingShape.getOrientation().equals(Block.Orientation.Vertical))
        {
            int newY = mMovingShape.getRect().top;
            double number = Math.round(newY/SHAPE_SIZE);
            newY = (int)number*SHAPE_SIZE;
            mMovingShape.getRect().offsetTo(mMovingShape.getRect().left,newY);
        }
        else{
            int newX = mMovingShape.getRect().left;
            double number = Math.round(newX/SHAPE_SIZE);
            newX = (int)number*SHAPE_SIZE;
            mMovingShape.getRect().offsetTo(newX,mMovingShape.getRect().top);


        }



    }

}

