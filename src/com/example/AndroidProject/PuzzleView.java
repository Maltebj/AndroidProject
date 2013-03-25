package com.example.AndroidProject;

import android.content.Context;
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

    public void addShapes(List<Block> blocks){

        for(Block b:blocks)
        {
        Rect rect = new Rect();
            SHAPE_SIZE = getWidth()/NUM_COLS;
        //int x = mRandom.nextInt(getWidth()-SHAPE_SIZE);
        int y = b.getCol()*SHAPE_SIZE;
        int x = b.getRow()*SHAPE_SIZE;
        int x1 = x+SHAPE_SIZE;
        int y1 = y+SHAPE_SIZE;
        if(b.getOrientation() == Block.Orientation.Vertical){
          x1 = x+SHAPE_SIZE*b.getLength();
        }
        if(b.getOrientation() == Block.Orientation.Horizontal){
            y1 = y+SHAPE_SIZE*b.getLength();
        }

        rect.set(x,y,x1,y1);
        b.setRect(rect);

        mBlocks.add(b);
        }
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
                    if(mMovingShape.getOrientation() == Block.Orientation.Horizontal){

                            y = Math.max(0,Math.min(y,getHeight()-SHAPE_SIZE*mMovingShape.getLength()));
                            x = mMovingShape.getRect().left;
                        //x = Math.max(0,Math.min(x,getWidth()-SHAPE_SIZE));
                    }
                    else{

                        x = Math.max(0,Math.min(x,getWidth()-SHAPE_SIZE*mMovingShape.getLength()));
                        y = mMovingShape.getRect().top;
                        //y = Math.max(0,Math.min(y,getHeight()-SHAPE_SIZE));
                    }

                        mMovingShape.getRect().offsetTo(x,y);
                        invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if(mMovingShape != null){
                    mMovingShape = null;
                    if(mListener != null){
                        mListener.onShapeMoved();
                        //TODO snap to grid.
                        //invalidate();
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
    private boolean isShapeLocatedOn(int x, int y){
        for(Block block:mBlocks){
            if (block != mMovingShape){
                if ( block.getRect().intersect(x,y,SHAPE_SIZE,SHAPE_SIZE)){
                    System.out.println("Virkar");
                    return true;
                }
            }
        }
        return false;
    }

}

