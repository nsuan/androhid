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

import android.app.TabActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.KeyEvent;
import android.widget.TabHost;
import android.widget.Toast;
import android.util.Log;

public class AndroHid extends TabActivity {
    /** Called when the activity is first created. */
	public final static String TAG = "AndroHid";
	
	//This is the bt mac address we associate to
	public static String remoteHidDeviceAddress = "00:00:00:00:00:00";//"00:02:5B:00:C8:91";
		
	public static NativeBtHid  btInterface;
	public static SdpInterface sdpInterface;
	public static HidKeyMapper hidKeyMapper;
	public static PowerManager myPowerManager;			 // The global Power Manager instance
	public static SharedPreferences myPreferences;
	public static PowerManager.WakeLock wakeLock;        // The wake lock to keep the screen on
	
	/** We must save the wake lock state due to a 
	*   bug in the wake lock system. With this toggle we 
	*   guarantee not to Under-Wake-Lock the system
	**/
	public static boolean isWakeLockAquired = false;

	// Save the current Tab to restore it after pausing the activity
	public static int currentTab = 0;
	
	// TabHost stuff
	private static TabHost mTabHost;
	private static Resources mRes;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
                
        mTabHost = getTabHost();	// The activity TabHost
        mRes = getResources();		// Resource object to get Drawables

        // Initiate static instances of wide used classes 
        btInterface    = new NativeBtHid();
        sdpInterface   = new SdpInterface();
        hidKeyMapper   = new HidKeyMapper();
        myPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        myPreferences  = PreferenceManager.getDefaultSharedPreferences(this);
           
        // Get the system WakeLock
		wakeLock = myPowerManager.newWakeLock( PowerManager.SCREEN_DIM_WAKE_LOCK, TAG );
        
        // Load preferred remote host btAddress
        remoteHidDeviceAddress = AndroHid.myPreferences.getString( "REMOTE_HID_DEVICE_ADDRESS",
        		remoteHidDeviceAddress );
        
        // Setup the tabs
        setupPlayerTab();
        setupVideoTab();
        setupPresenterTab();
        
        // Show the Copyright dialog the first time the application executes
        if ( myPreferences.getBoolean("SHOW_COPYRIGHT_DIALOG", true)){
        	showCopyrightDialog();
        	myPreferences.edit().putBoolean("SHOW_COPYRIGHT_DIALOG", false).commit();
        }      
        
        // Check if BT is enabled
        if ( ! btInterface.isEnabled() ) {
        	showEnableBtDialog();
        }
        
        // Check if su is available 
        // If not, exit the application
        if ( ! sdpInterface.hasRootPermission() ){
        	showNoRootPermissionDialog();
        }
        
        // Add the KEYB Service to sdpd
        // TODO: Wait until btInterface is really enabled!
        sdpInterface.addHidService();
        
        // TODO: Can't listen on PSM 0x11 and 0x13 at the moment
        // 		 so the HID standard is not implemented completely at the moment
        //btInterface.setupClientSocket();
    }
    
    @Override
    protected void onResume(){
    	super.onResume();    	  	
    	// Check if screen should stay on and toggle that
		if ( myPreferences.getBoolean( getString(R.string.settings_stay_screen_on), false ) ) {
			try {
				wakeLock.acquire();
				isWakeLockAquired = true;
				//showToast("Acquired wake Lock");
			} catch ( Exception e ) {
				//showToast("Acquired wake Lock error");
			}
		}
		else {//showToast("No wake Lock");
			}
		// Restore active tab state
		mTabHost.setCurrentTab(currentTab);
    }
 
    @Override
    protected void onPause(){
    	super.onPause();
    	if ( isWakeLockAquired ) {
	    	try {
	   			wakeLock.release();
	  			isWakeLockAquired = false;
	   			//showToast("Release wake Lock");
	    	} catch ( Exception e ) {
	    		//showToast("Release wake Lock failed");
	    	}
    	}
    	// Save current tab
    	currentTab = mTabHost.getCurrentTab();
    }
    protected void onStop(){
    	super.onStop();
    	// Save the bluetooth host address to preferences
    	myPreferences
    		.edit()
    		.putString("REMOTE_HID_DEVICE_ADDRESS", remoteHidDeviceAddress)
    		.commit();
    }
    /** 
    @Override
    protected void onDestroy(){
    	super.onDestroy();
    	btInterface.deconnectClient();
    	sdpInterface.delHidService();
    }
       
    @Override
     protected void onStart();
     
     protected void onRestart();

    

    */
    
    /* Setup the Player Tab Activity */
    private void setupPlayerTab() {
    	Intent mIntent = new Intent().setClass(this, PlayerTab.class);
    	TabHost.TabSpec mSpec;
        mSpec = mTabHost.newTabSpec("player").setIndicator("Player",
                mRes.getDrawable(R.drawable.ic_tab_player))
                .setContent(mIntent);
        mTabHost.addTab(mSpec);
    }

    /* Setup the Video Tab Activity */
    private void setupVideoTab() {
    	Intent mIntent = new Intent().setClass(this, VideoTab.class);
    	TabHost.TabSpec mSpec;
        mSpec = mTabHost.newTabSpec("video").setIndicator("Video",
                mRes.getDrawable(R.drawable.ic_tab_video))
                .setContent(mIntent);
        mTabHost.addTab(mSpec);
    }    
    
    /* Setup the Presenter Tab Activity */
    private void setupPresenterTab() {
    	Intent mIntent = new Intent().setClass(this, PresenterTab.class);
    	TabHost.TabSpec mSpec;
        mSpec = mTabHost.newTabSpec("presenter").setIndicator("Presenter",
                mRes.getDrawable(R.drawable.ic_tab_presenter))
                .setContent(mIntent);
        mTabHost.addTab(mSpec);
    }
    
    /* Shows a dialog to enable bluetooth or quit the application */
    public void showEnableBtDialog(){
    	AlertDialog.Builder btEnableDialogBuilder = new AlertDialog.Builder(this);
    	btEnableDialogBuilder.setMessage( getString( R.string.enable_bluetooth ) )
    	       .setCancelable(false)
    	       .setPositiveButton( getString( R.string.yes ), new DialogInterface.OnClickListener() {
    	           public void onClick( DialogInterface dialog, int id ) {
    	        	   startActivityForResult(new Intent( Settings.ACTION_BLUETOOTH_SETTINGS ),1);
    	           }
    	       })
    	       .setNegativeButton( getString( R.string.no ), new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	        	   dialog.cancel();
    	        	   finish();
    	           }
    	       });
    	AlertDialog btEnableDialog = btEnableDialogBuilder.create();
    	btEnableDialog.show();
    }
    
    /* Dialog which will show the copyright dialog at first usage */
    public void showCopyrightDialog(){
    	AlertDialog.Builder copyrightDialogBuilder = new AlertDialog.Builder(this);
       	copyrightDialogBuilder
			.setTitle( getString( R.string.copyright_dialog_title ) )
			.setMessage( getString( R.string.copyright_dialog ) )
			.setCancelable( true )
			.setIcon( R.drawable.icon_small )
			.show();
    }
    
    /* Shows a dialog that superuser permissions are required and exits the app */
    public void showNoRootPermissionDialog(){
    	AlertDialog.Builder noRootPermissionDialogBuilder = new AlertDialog.Builder(this);
    	noRootPermissionDialogBuilder.setMessage( getString( R.string.no_root_permission_dialog ) )
    		.setTitle( getString( R.string.no_root_permission_title ) )
    		.setIcon( android.R.drawable.ic_dialog_alert )
    		.setCancelable( false )
    	    .setPositiveButton( getString( R.string.ok ) , new DialogInterface.OnClickListener() {
    	    	public void onClick( DialogInterface dialog, int id ) {
    	    		finish(); 
    	        	}
    	     	})
    	    .show();
    }
    
    /* Creates the menu items */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu_main, menu);
        return true;
    }
    
    /* Handles item selections */
    public boolean onOptionsItemSelected( MenuItem item ) {
        switch ( item.getItemId() ) {
        
        case R.id.CONNECT:
            this.connect();
            return true;
        
        case R.id.DISCONNECT:
            this.disconnect();
            this.finish();
            return true;
        
        case R.id.SEARCH_DEVICE:
        	if ( btInterface.isEnabled() ){
        		Intent btDevicePickerIntent = new Intent( this.getBaseContext(), BtDevicePicker.class);
        		startActivityForResult( btDevicePickerIntent, 0);
        	} else {
            	showToast( getString( R.string.bluetooth_not_enabled ) );
            }
        	return true;

        case R.id.PREFERENCES:
        	startActivity(new Intent(this, Preferences.class));
            return true;
        case R.id.INFO:
        	showCopyrightDialog();
        	return true;
            
        }
        return false;
    }
    
	public void connect() {
		// Load preferred remote host btAddress
        remoteHidDeviceAddress = AndroHid.myPreferences.getString( "REMOTE_HID_DEVICE_ADDRESS",
        		remoteHidDeviceAddress );
		try {
			btInterface.connectToHost(remoteHidDeviceAddress);
		} catch (Exception connectionFailed){
			showToast(getString(R.string.connection_failed));
		}
			showToast(getString(R.string.connection_established_to) + " " + AndroHid.remoteHidDeviceAddress);
			//dialog.dismiss();
    }
    
	public void disconnect() {
		//ProgressDialog dialog = ProgressDialog.show( this, "", "Disconnecting..." );
		btInterface.deconnectClient();
		//dialog.dismiss();	
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent  event) {
    	/** Return true if event is handled by handler,
    	    False if event should be passed to external handler */
    	return hidKeyMapper.processKeySignal( keyCode, event );
    }
    
    public final void showToast(final String text) {
		try {
			Toast.makeText(this.getApplicationContext(), text,
					Toast.LENGTH_LONG).show();
		} catch (RuntimeException e) {
			Log.e(TAG, e.getMessage(), e);
		}
	}
}