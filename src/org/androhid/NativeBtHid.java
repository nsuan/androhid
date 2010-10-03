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

public class NativeBtHid {

  static {
    System.loadLibrary("androhid");
  }
  
   /** 
   * Setup the bluetooth HID socket to listen on incoming host connection
   * should be called on construction 
   * Takes as argument the bluetooth Address of the host
   * 
   * @return	0 if successful 
   */
  public native int setupClientSocket();
  
  /**
   * Connect to the host
   * 
   * @return	0 if successful
   * @return	1 if not successful 
   */
  public native int connectToHost( String hostAddress );
  
  /**
   * Deinitialize the HID client and disconnects
   * Should be called on destruction
   * 
   * @return	0 if successful
   */
  public native int deconnectClient();
  
  /**
   * Send a key press event
   * 
   * @return	0 if successful
   * @param	modifiers	the modifier HID code
   * @param keyValue	the key value HID code 
   */
  public native int sendKeyDownEvent( int modifiers, int keyValue );
  
  /**
   * Send a key release event
   * 
   * @return: 0 if successful
   */
  public native int sendKeyUpEvent();
  
  /**
   * Send a mouse event
   * 
   * @returns: 0 if successful
   */
  public native int sendMouseEvent( int button, int movX, int movY, int whell );
  
  /**
   * Check status of bluetooth adapter
   * 
   * @return true if Bluetooth is currently enabled and ready for use.
   */
  public native boolean isEnabled();
  
  /**
   * Detect remote bt devices in visible state
   * 
   * @return Array of Strings with detected bt device addresses
   */
  public native String[] btDeviceScan();
  
  /**
   * Get the human readable name of a bt device
   * 
   * @return	Human readable name of bt device
   * @param		bluetooth mac address
   */
  public native String btDeviceGetName( String btAddress );
    
}
