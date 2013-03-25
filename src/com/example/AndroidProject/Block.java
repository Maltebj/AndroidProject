package com.example.AndroidProject;

import android.graphics.Rect;

/**
 * Created with IntelliJ IDEA.
 * User: tryggvim
 * Date: 23.3.2013
 * Time: 21:50
 * To change this template use File | Settings | File Templates.
 */
public class Block {

    public static enum Orientation { Horizontal, Vertical }

        private Orientation m_orientation;
        private int m_col;
        private int m_row;
        private int m_length;
        private int m_color;
        private Rect m_rect;

        public Block( Orientation orientation, int col, int row, int length,int color,Rect rect ) {
            m_orientation = orientation;
            m_col = col;
            m_row = row;
            m_length = length;
            m_color = color;
            m_rect = rect;
        }

        public Block( Block other ) {
            m_orientation = other.m_orientation;
            m_col = other.m_col;
            m_row = other.m_row;
            m_length = other.m_length;
            m_color = other.m_color;
            m_rect = other.m_rect;
        }

        public int getCol() { return m_col; }

        public int getRow() { return m_row; }
        public int getColor() { return m_color; }
        public Rect getRect() { return m_rect; }
        public void setRect(Rect rect) {m_rect = rect; }

        public Orientation getOrientation() { return m_orientation; }

        public int getLength() { return m_length; }

        public void slide( int offset ) {
            if ( getOrientation() == Orientation.Horizontal ) {
                m_col += offset;
            }
            else {
                m_row += offset;
            }
        }

        public String toString( ) {
            StringBuilder sb = new StringBuilder();
            sb.append( '(' );
            sb.append( getOrientation() == Orientation.Horizontal ? 'H' : 'V' );
            sb.append( ' ' );
            sb.append( getCol() );
            sb.append( ' ' );
            sb.append( getRow() );
            sb.append( ' ' );
            sb.append( getLength() );
            sb.append( ')' );
            return sb.toString();
        }

    }
