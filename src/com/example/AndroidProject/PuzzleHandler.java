package com.example.AndroidProject;

import android.graphics.Color;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.InputStream;
import java.util.*;
import java.util.regex.MatchResult;

/**
 * Created with IntelliJ IDEA.
 * User: tryggvim
 * Date: 23.3.2013
 * Time: 21:38
 * To change this template use File | Settings | File Templates.
 */
public class PuzzleHandler {


    public static final int NUM_COLS = 6;
    public static final int NUM_ROWS = 6;

    public static final int GOAL_COL = 5;
    public static final int GOAL_ROW = 3;
    public static final int GOAL_BLOCK_ID = 0;  // first can assumed to be the goal car.

    public String readChallenge(InputStream is,int number) {
        try {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            //System.out.println( doc.getDocumentElement().getNodeName() );
            NodeList nList = doc.getElementsByTagName("puzzle");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    if(Integer.parseInt(eElement.getAttribute("id")) == number )
                    return eElement.getElementsByTagName("setup").item(0).getTextContent();

                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public List<Integer> readChallenge( InputStream is) {
        List<Integer> puzzles = new ArrayList<Integer>();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            NodeList nList = doc.getElementsByTagName("puzzle");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    int id = Integer.parseInt(eElement.getAttribute("id"));


                    puzzles.add( id );
                }
            }
            return puzzles;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<Block> setup( String puzzleStr ) {
        List<Block> blocks = new ArrayList<Block>();
        String blockssStr[] = puzzleStr.split( "," );
        for ( String blockStr : blockssStr ) {
            Block block = blockFromString(blockStr);
            if ( block != null ) {
                blocks.add(block);
            }
            else { }
        }
        return blocks;
    }
    /*
    public List<Block> setup( List<Block> blocks ) {
        m_blocks.clear();
        m_isSolved = false;
        for ( Block block : blocks ) {
            if ( !canPlace( block ) ) {
                return false;
            }
            if ( block == blocks.get( GOAL_BLOCK_ID ) ) {
                m_isSolved = m_isSolved || doOverlap( block, m_goal );
            }
            m_blocks.add( new Block( block ) );
        }
        updateGrid();
        return true;
    }
    */
    public List<Action> getActions( ) {
        List<Action> actions = new ArrayList<Action>( );
        if ( !isSolved() ) {
            updateGrid();
            for ( int n=m_blocks.size(), i=0; i<n; ++i  ) {
                Block block   = m_blocks.get(i);
                int colOn = block.getCol();
                int rowOn = block.getRow();
                if ( block.getOrientation() == Block.Orientation.Horizontal ) {
                    for ( int col=colOn-1; col>=0 && m_grid[col][rowOn]==-1; --col ) {
                        actions.add( new Action( i, col-colOn ) );
                    }
                    colOn += block.getLength() - 1;
                    for ( int col=colOn+1; col<NUM_COLS && m_grid[col][rowOn]==-1; ++col ) {
                        actions.add( new Action( i, col-colOn ) );
                    }
                }
                else {
                    for ( int row=rowOn-1; row>=0 && m_grid[colOn][row]==-1; --row ) {
                        actions.add( new Action( i, row-rowOn ) );
                    }
                    rowOn += block.getLength() - 1;
                    for ( int row=rowOn+1; row<NUM_ROWS && m_grid[colOn][row]==-1; ++row ) {
                        actions.add( new Action( i, row-rowOn ) );
                    }
                }
            }
        }
        return actions;
    }
    public static Block blockFromString( String blockStr ) {
        Block blockReturn = null;
        Scanner s = new Scanner( blockStr );
        s.findInLine("\\s*\\(\\s*(\\w+)\\s*(\\d+)\\s*(\\d+)\\s*(\\d+)\\s*\\)\\s*");
        try {
            MatchResult result = s.match();
            if ( result.groupCount() == 4 ) {
                boolean isSuccessful = true;
                Block.Orientation orientation = null;
                if ( result.group(1).equals("H") ) {
                    orientation = Block.Orientation.Horizontal;
                }
                else if ( result.group(1).equals( "V" ) ) {
                    orientation = Block.Orientation.Vertical;
                }
                else { isSuccessful = false; }
                if ( isSuccessful ) {
                    int col = Integer.parseInt( result.group( 2 ) );
                    int row = Integer.parseInt( result.group( 3 ) );
                    int length = Integer.parseInt( result.group( 4 ) );
                    Block block = new Block( orientation, col, row, length, getRandomColor(),null );
                    if ( isWithinBounds( block ) ) {
                        blockReturn = block;
                    }
                }
            }
        }
        catch ( IllegalStateException e ) {
            // Match not found.
        }
        s.close();
        return blockReturn;
    }
    public void makeAction( Action action ) {
        Block block = m_blocks.get( action.getId() );
        block.slide(action.getOffset());
        if ( action.getId() == GOAL_BLOCK_ID ) {
            m_isSolved = doOverlap( block, m_goal );
        }
    }
    public void retractAction( Action action ) {
        m_blocks.get( action.getId() ).slide( -action.getOffset() );
        if ( m_isSolved ) {
            m_isSolved = false;
        }
    }
    public boolean isSolved( ) {
        return m_isSolved;
    }
    public List<Block> getCars() {
        return Collections.unmodifiableList(m_blocks);
    }
    private final Block m_goal = new Block( Block.Orientation.Horizontal, GOAL_COL, GOAL_ROW, 1,getRandomColor(),null );

    private List<Block> m_blocks;
    private boolean   m_isSolved;
    private int[][]   m_grid = new int[NUM_COLS][NUM_ROWS];

    private static boolean intersect( int x1, int dx1, int x2, int dx2 ) {
        return ( (x1 <= x2) && (x2 < x1 + dx1) ) || ( (x2 <= x1) && (x1 < x2 + dx2) );
    }

    private static boolean doOverlap( Block block1, Block block2 ) {
        if ( block1.getOrientation() == Block.Orientation.Horizontal ) {
            if ( block2.getOrientation() == Block.Orientation.Horizontal ) {
                return (block1.getRow() == block2.getRow()) &&
                        intersect(block1.getCol(), block1.getLength(), block2.getCol(), block2.getLength());
            }
            else {
                return intersect( block1.getCol(), block1.getLength(), block2.getCol(), 1 ) &&
                        intersect(block1.getRow(), 1, block2.getRow(), block2.getLength());
            }
        }
        else {
            if ( block2.getOrientation() == Block.Orientation.Vertical ) {
                return (block1.getCol() == block2.getCol()) &&
                        intersect(block1.getRow(), block1.getLength(), block2.getRow(), block2.getLength());
            }
            else {
                return intersect( block1.getRow(), block1.getLength(), block2.getRow(), 1 ) &&
                        intersect( block1.getCol(), 1, block2.getCol(), block2.getLength() );
            }
        }
    }

    private static boolean isWithinBounds( Block blockIn  ) {
        boolean ok;
        if ( blockIn.getOrientation() == Block.Orientation.Horizontal ) {
            ok = (blockIn.getCol() >= 0) && ((blockIn.getCol() + blockIn.getLength()) <= NUM_COLS);
        }
        else {
            ok = (blockIn.getRow() >= 0) && ((blockIn.getRow() + blockIn.getLength()) <= NUM_ROWS);
        }
        return ok;
    }

    private boolean canPlace( Block blockIn ) {
        if ( !isWithinBounds( blockIn ) ) {
            return false;
        }
        for ( Block block : m_blocks ) {
            if ( doOverlap( blockIn, block ) ) {
                return false;
            }
        }
        return true;
    }

    private void updateGrid( ) {
        for ( int col=0; col<NUM_COLS; ++col ) {
            for ( int row=0; row<NUM_ROWS; ++row ) {
                m_grid[col][row] = -1;
            }
        }

        for ( int i=0; i<m_blocks.size(); ++i ) {
            Block block = m_blocks.get(i);
            if ( block.getOrientation() == Block.Orientation.Horizontal ) {
                for ( int end=block.getCol()+block.getLength(), col=block.getCol(); col<end; ++col ) {
                    m_grid[col][block.getRow()] = i;
                }
            }
            else {
                for ( int end=block.getRow()+block.getLength(), row=block.getRow(); row<end; ++row ) {
                    m_grid[block.getCol()][row] = i;
                }
            }
        }
    }
    public static int getRandomColor(){
        Random rand = new Random();
        int i = rand.nextInt(100);

        if(i<10)
        {
            i = Color.RED;
        }else if(i<20)
        {
            i = Color.GREEN;
        }else if(i<30)
        {
            i = Color.MAGENTA;
        }else if(i<40){
            i = Color.BLACK;
        }else if(i<50){
            i = Color.BLUE;
        }else if(i<60){
            i = Color.CYAN;
        }else if(i<70){
            i = Color.GRAY;
        }else if(i<80){
            i = Color.DKGRAY;
        }else if(i<90){
            i = Color.YELLOW;
        }else{
            i = Color.LTGRAY;
        }
        return i;
    }



}
