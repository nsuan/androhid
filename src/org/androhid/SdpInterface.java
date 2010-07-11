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

//import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import android.util.Log;


public class SdpInterface {
	private String REGISTER_SDP_SERVICE = "/system/bin/sdptool add --handle=0x5efe5 KEYB";
	private String RELEASE_SDP_SERVICE  = "/system/bin/sdptool del 0x5efe5";
	private int    processReturnValue;
	
	public void addHidService(){
		try {
			Process process = Runtime.getRuntime().exec("su");
			DataOutputStream osOut = new DataOutputStream(process.getOutputStream());
			//DataInputStream  osIn  = new DataInputStream(process.getInputStream());
			osOut.writeBytes(REGISTER_SDP_SERVICE + "\n");
			osOut.flush();
			osOut.writeBytes("exit\n");
			osOut.flush();
			
			process.waitFor();
			processReturnValue   = process.exitValue();
			
			if (processReturnValue == 0) {
				Log.i("AndroHid", "sdp KEYB profile added");
				}
			else {
				Log.e("AndroHid", "adding sdp service failed!");
				}
			} 
		catch(IOException e) {
			Log.e("AndroHid","sdptool not executable",e);
			}
		catch(InterruptedException e){
			Log.e("AndroHid","sdptool add service interrupted",e);
			}
	}
	
	public void delHidService(){
		try {
			Process process = Runtime.getRuntime().exec(RELEASE_SDP_SERVICE);
			DataOutputStream osOut = new DataOutputStream(process.getOutputStream());
			//DataInputStream  osIn  = new DataInputStream(process.getInputStream());
			osOut.writeBytes(RELEASE_SDP_SERVICE + "\n");
			osOut.flush();
			osOut.writeBytes("exit\n");
			osOut.flush();
			
			process.waitFor();
			processReturnValue   = process.exitValue();
			if (processReturnValue == 0) {
				//Log.i("AndroHid", osIn.readLine());
				}
			else {
				//Log.e("AndroHid", osIn.readLine());
				}
			} 
		catch(IOException e) {
			Log.e("AndroHid","sdptool not executable",e);
			}
		catch(InterruptedException e){
			Log.e("AndroHid","sdptool del service interrupted",e);
			}
	}
}
