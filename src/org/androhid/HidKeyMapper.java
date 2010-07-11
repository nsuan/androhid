/*
 * Copyright (C) 2010 Manuel Luitz
 *
 * This file is part of AndroHid.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/>.
 */

package org.androhid;

import java.util.HashMap;

import android.view.KeyEvent;
import android.util.Log;

public class HidKeyMapper {
	HashMap< Integer, Integer > keyMap = new HashMap< Integer, Integer >();
	// The modifier carries the last modifier key press
	static int modifier = 0;
	
	public HidKeyMapper() {
		keyMap.put( 29, 4 ); // a
		keyMap.put( 30, 5 ); // b
		keyMap.put( 31, 6 ); // c
		keyMap.put( 32, 7 ); // d
		keyMap.put( 33, 8 ); // e
		keyMap.put( 34, 9 ); // f
		keyMap.put( 35,10 ); // g
		keyMap.put( 36,11 ); // h
		keyMap.put( 37,12 ); // i
		keyMap.put( 38,13 ); // j
		keyMap.put( 39,14 ); // k
		keyMap.put( 40,15 ); // l
		keyMap.put( 41,16 ); // m
		keyMap.put( 42,17 ); // n
		keyMap.put( 43,18 ); // o
		keyMap.put( 44,19 ); // p
		keyMap.put( 45,20 ); // q
		keyMap.put( 46,21 ); // r
		keyMap.put( 47,22 ); // s
		keyMap.put( 48,23 ); // t
		keyMap.put( 49,24 ); // u
		keyMap.put( 50,25 ); // v
		keyMap.put( 51,26 ); // w
		keyMap.put( 52,27 ); // x
		keyMap.put( 53,28 ); // y
		keyMap.put( 54,29 ); // z
		keyMap.put( 8 ,30 ); // 1
		keyMap.put( 9 ,31 ); // 2
		keyMap.put( 10,32 ); // 3
		keyMap.put( 11,33 ); // 4
		keyMap.put( 12,34 ); // 5
		keyMap.put( 13,35 ); // 6
		keyMap.put( 14,36 ); // 7
		keyMap.put( 15,37 ); // 8
		keyMap.put( 16,38 ); // 9
		keyMap.put( 7 ,39 ); // 0
		keyMap.put( 66,40 ); // Return
		keyMap.put( 67,42 ); // Backspace
		keyMap.put( 62,44 ); // Space
		keyMap.put( 77,20 ); // @
		keyMap.put( 56,55 ); // .
		keyMap.put( 55,54 ); // ,
		
		// The Trackball Key Codes ( Tested on Nexus and G1 )
		keyMap.put( 22,79 ); // Right
		keyMap.put( 21,80 ); // Left
		keyMap.put( 19,82 ); // Up
		keyMap.put( 20,81 ); // Down
	}
	
	public boolean processKeySignal( int keyCode, KeyEvent event ) {
    	/** Return true if event is handled by handler,
	    False if event should be passed to extern handler */
		int keyValue = 0;
		switch ( keyCode ) {
			case 4:		// android back key
				return true;
			case 82:    // android menu key
				return false; 
			case 23:	// emulate STRG
				modifier = 0x01;
				return true;
			case 59:	// emulate SHIFT_LEFT
				modifier = 0x02;
				return true;
			case 60:	// emulate SHIFT_RIGHT
				modifier = 0x20;
				return true;
			case 57:	// emulate ALT_LEFT
				modifier = 0x04;
				return true;
			case 58:	// emulate ALT_RIGHT
				modifier = 0x40;
				return true;
			case 77:	// emulate @ 
				modifier = 0x40;
			case 84:	// Search 
				return false;
			case 19:	// Up
				if ( !AndroHid.myPreferences.getBoolean( "SETTINGS_ENABLE_TRACK_BALL", false ))
					return true;
				if ( !AndroHid.myPreferences.getBoolean( "SETTINGS_MAP_TRACK_BALL", true ))
					return false;
			case 20:	// Down
				if ( !AndroHid.myPreferences.getBoolean( "SETTINGS_ENABLE_TRACK_BALL", false ))
					return true;
				if ( !AndroHid.myPreferences.getBoolean( "SETTINGS_MAP_TRACK_BALL", true ))
					return false;
			case 21:	// Left
				if ( !AndroHid.myPreferences.getBoolean( "SETTINGS_ENABLE_TRACK_BALL", false ))
					return true;
				if ( !AndroHid.myPreferences.getBoolean( "SETTINGS_MAP_TRACK_BALL", true ))
					return false;
			case 22:	// Right
				if ( !AndroHid.myPreferences.getBoolean( "SETTINGS_ENABLE_TRACK_BALL", false ))
					return true;
				if ( !AndroHid.myPreferences.getBoolean( "SETTINGS_MAP_TRACK_BALL", true ))
					return false;
			default:
		}
		
		try { 
			keyValue = keyMap.get( keyCode );
		} catch (NullPointerException e) {
			Log.i(AndroHid.TAG, "Keycode not mapped:" + keyCode);
			return false;
		}
		
		AndroHid.btInterface.sendKeyDownEvent( modifier , keyValue );
		AndroHid.btInterface.sendKeyUpEvent();
		modifier = 0;
		return true;
	}
}
