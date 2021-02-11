/*
 * PinSetterView/.java
 *
 * Version:
 *   $Id$
 *
 * Revision:
 *   $Log$
 */

/**
 *  constructs a prototype PinSetter GUI
 *
 */

import java.awt.*;
import javax.swing.*;
import java.util.Vector;


public class PinSetterView implements PinsetterObserver {


    final private Vector pinVect = new Vector ( );
    final private JPanel firstRoll;
    final private JPanel secondRoll;
    private JPanel one;
    private JLabel oneL;
	private JPanel two;
	private JLabel twoL;
	private JPanel three;
	private JLabel threeL;
	private JPanel four;
	private JLabel fourL;
	private JPanel five;
	private JLabel fiveL;
	private JPanel six;
	private JLabel sixL;
	private JPanel seven;
	private JLabel sevenL;
	private JPanel eight;
	private JLabel eightL;
	private JPanel nine;
	private JLabel nineL;
	private JPanel ten;
	private JLabel tenL;
	final private JPanel pins;


    /**
     * Constructs a Pin Setter GUI displaying which roll it is with
     * yellow boxes along the top (1 box for first roll, 2 boxes for second)
     * and displays the pins as numbers in this format:
     *
     *                7   8   9   10
     *                  4   5   6
     *                    2   3
     *                      1
     *
     */
    

	final private JFrame frame;

	private void addPins() {

		//This Vector will keep references to the pin labels to show
		//which ones have fallen.

		pinVect.add ( oneL );
		pinVect.add ( twoL );
		pinVect.add ( threeL );
		pinVect.add ( fourL );
		pinVect.add ( fiveL );
		pinVect.add ( sixL );
		pinVect.add ( sevenL );
		pinVect.add ( eightL );
		pinVect.add ( nineL );
		pinVect.add ( tenL );
	}

	private void drawPins() {

		one = new JPanel ();
		oneL = new JLabel ( "1" );
		one.add (oneL);
		two = new JPanel ();
		twoL = new JLabel ( "2" );
		two.add (twoL);
		three = new JPanel ();
		threeL = new JLabel ( "3" );
		three.add (threeL);
		four = new JPanel ();
		fourL = new JLabel ( "4" );
		four.add (fourL);
		five = new JPanel ();
		fiveL = new JLabel ( "5" );
		five.add (fiveL);
		six = new JPanel ();
		sixL = new JLabel ( "6" );
		six.add (sixL);
		seven = new JPanel ();
		sevenL = new JLabel ( "7" );
		seven.add (sevenL);
		eight = new JPanel ();
		eightL = new JLabel ( "8" );
		eight.add (eightL);
		nine = new JPanel ();
		nineL = new JLabel ( "9" );
		nine.add (nineL);
		ten = new JPanel ();
		tenL = new JLabel ( "10" );
		ten.add (tenL);
	}

	private void setPins() {
		//******************************Fourth Row**************

		pins.add ( seven );
		pins.add ( new JPanel ( ) );
		pins.add ( eight );
		pins.add ( new JPanel ( ) );
		pins.add ( nine );
		pins.add ( new JPanel ( ) );
		pins.add ( ten );

		//*****************************Third Row***********

		pins.add ( new JPanel ( ) );
		pins.add ( four );
		pins.add ( new JPanel ( ) );
		pins.add ( five );
		pins.add ( new JPanel ( ) );
		pins.add ( six );

		//*****************************Second Row**************

		pins.add ( new JPanel ( ) );
		pins.add ( new JPanel ( ) );
		pins.add ( new JPanel ( ) );
		pins.add ( two );
		pins.add ( new JPanel ( ) );
		pins.add ( three );
		pins.add ( new JPanel ( ) );
		pins.add ( new JPanel ( ) );

		//******************************First Row*****************

		pins.add ( new JPanel ( ) );
		pins.add ( new JPanel ( ) );
		pins.add ( new JPanel ( ) );
		pins.add ( one );
		pins.add ( new JPanel ( ) );
		pins.add ( new JPanel ( ) );
		pins.add ( new JPanel ( ) );
		//*********************************************************
	}

    public PinSetterView ( int laneNum ) {
	
	frame = new JFrame ( "Lane " + laneNum + ":" );
	
	Container cpanel = frame.getContentPane ( );
	
	pins = new JPanel ( );
	
	pins.setLayout ( new GridLayout ( 4, 7 ) );
	
	//********************Top of GUI indicates first or second roll
	
	JPanel top = new JPanel ( );
	
	firstRoll = new JPanel ( );
	firstRoll.setBackground( Color.yellow );
	
	secondRoll = new JPanel ( );
	secondRoll.setBackground ( Color.black );
	
	top.add ( firstRoll, BorderLayout.WEST );
	
	top.add ( secondRoll, BorderLayout.EAST );
	drawPins();
	addPins();
	setPins();

	top.setBackground ( Color.black );
	
	cpanel.add ( top, BorderLayout.NORTH );
	
	pins.setBackground ( Color.black );
	pins.setForeground ( Color.yellow );
	
	cpanel.add ( pins, BorderLayout.CENTER );
	
	frame.pack();
	
	
//	frame.show();
    }
    
    
    /**
     * This method receives a pinsetter event.  The event is the current
     * state of the PinSetter and the method changes how the GUI looks
     * accordingly.  When pins are "knocked down" the corresponding label
     * is grayed out.  When it is the second roll, it is indicated by the
     * appearance of a second yellow box at the top.
     *
     * @param e    The state of the pinsetter is sent in this event.
     */
    

    @Override public void receivePinsetterEvent(PinsetterEvent pe){
	if ( !(pe.isFoulCommited()) ) {
	    	JLabel tempPin;
	    	for ( int c = 0; c < 10; c++ ) {
				boolean pin = pe.pinKnockedDown ( c );
				tempPin = (JLabel)pinVect.get ( c );
				if ( pin ) {
		    		tempPin.setForeground ( Color.lightGray );
				}
	    	}
    	}
		if ( pe.getThrowNumber() == 1 ) {
	   		 secondRoll.setBackground ( Color.yellow );
		}
	if ( pe.pinsDownOnThisThrow() == -1) {
		for ( int i = 0; i != 10; i++){
			((JLabel)pinVect.get(i)).setForeground(Color.black);
		}
		secondRoll.setBackground( Color.black);
	}
    }
    
    public void show() {
    	frame.show();
    }

    public void hide() {
    	frame.hide();
    }
    
    public static void main ( String args [ ] ) {
		//PinSetterView pg = new PinSetterView ( 1 );
    }
    
}
