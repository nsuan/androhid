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

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/*
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
*/

public class BtDevicePicker extends ListActivity {
	ListView lvDevices;
	
//	private final int CONTEXT_MENU_FAVORITE_ADD = 0;
//	private final int CONTEXT_MENU_FAVORITE_DEL = 1;
	
	private ProgressDialog processDialogSearching;

	volatile public static String[] btDeviceAddresses;
	
	volatile public static List<BtDevice> btDevices  = new ArrayList<BtDevice>();
	private BtDeviceAdapter btDeviceAdapter;
	
	BtDeviceScan btDeviceScan = new BtDeviceScan();
   
    Thread threadScanningDevices = new Thread(btDeviceScan);
    
    private Context myContext;
    
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.bt_device_picker);
        
        // Set Context to variable
        myContext = this;
        
        // Disable screen rotation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        // Set the List Adapter to connect btDevices with lvDevices
        btDeviceAdapter = new BtDeviceAdapter(this, android.R.layout.two_line_list_item, btDevices);
        setListAdapter(btDeviceAdapter);
        
        // Connect the list view to context menu
        //registerForContextMenu(getListView());
        
        // Start the device search  if BT is enabled
        if( AndroHid.btInterface.isEnabled() ) {
        	threadScanningDevices.start();
        } else {
        	showToast( getString( R.string.bluetooth_not_enabled ) );
        }
        
    };
        
    @Override
    public void onListItemClick(ListView  l, View v, int position, long id) {
    	// Assign new host to pair with
    	AndroHid.remoteHidDeviceAddress = btDevices.get(position).btAddress;
    	// Save this host in Preferences
    	AndroHid.myPreferences.edit().putString( "REMOTE_HID_DEVICE_ADDRESS", 
    											AndroHid.remoteHidDeviceAddress );
    	AndroHid.myPreferences.edit().commit();
		Toast.makeText(this.getApplicationContext(), "Selected Bluetooth Device: " +
				btDevices.get( position ).humanReadableName,
				Toast.LENGTH_LONG).show();
    }
/*    
    public void onCreateContextMenu( ContextMenu menu, View v, ContextMenuInfo menuInfo ) {
    	super.onCreateContextMenu( menu, v, menuInfo );
    	menu.add( 0, CONTEXT_MENU_FAVORITE_ADD, 0, "Add favorite" );
    	menu.add( 0, CONTEXT_MENU_FAVORITE_DEL, 0, "Remove favorite" );
    }
    
    public boolean onContextItemSelected(MenuItem item) {
    	  switch (item.getItemId()) {
    	  case CONTEXT_MENU_FAVORITE_ADD:
    	    return true;
    	  case CONTEXT_MENU_FAVORITE_DEL:
    	    return true;
    	  default:
    	    return super.onContextItemSelected(item);
    	  }
    	}
*/
    
    class BtDeviceScan implements Runnable {
    	public void run(){
    		// Starting device search
    		try {
    			// Show the ProgressDialog 
    			handler.sendEmptyMessage(1);
    			btDeviceAddresses = AndroHid.btInterface.btDeviceScan();
    		} catch ( java.lang.OutOfMemoryError error ) {
    			// inquiry already active
    			handler.sendEmptyMessage(0);
    			handler.sendEmptyMessage(3);
    			return;
    		}
    		
    		// Clear all bt devices than add new found
    		btDevices.clear();
    		for ( String deviceAddr: btDeviceAddresses ) {
    			btDevices.add( new BtDevice( deviceAddr ) );
    		}
    		
    		// Getting human readable names of btDevices if possible
    		for ( BtDevice device: btDevices ) {
				device.humanReadableName = ( AndroHid.btInterface.btDeviceGetName( device.btAddress ) );
			}
    		
    		handler.sendEmptyMessage(0);
    		handler.sendEmptyMessage(2);
    	};
    
    	public Handler handler =  new Handler() {
    		@Override
    		public void handleMessage( Message msg ) {
    			switch (msg.what) {
    			case 0:
    				// Dismiss the Process Dialog
    				processDialogSearching.dismiss();
	                break;
    			case 1:
    				//Start the Process Dialog
    				processDialogSearching = ProgressDialog.show(myContext, "Working..", "Searching Devices...", true, false);
    				break;
    			case 2:
    				// Update the ListView
	                btDeviceAdapter.notifyDataSetChanged();
	                break;
    			case 3:
    				// Inquriy could not be started
    				showToast("System inquiry running, can't search for devices now");
    			default:
    				break;   
	    		}

        	};
        };
    }
        
    public class BtDeviceAdapter extends BaseAdapter {
    	private final Context context;
    	private final int rowResID;
    	private final List<BtDevice> deviceList;
    	private final LayoutInflater layoutInflater; 
    	
    	/**
        *
        * @param _context
        * @param _rowResID
        * @param _deviceList
        */
        public BtDeviceAdapter( final Context _context, final int _rowResID, final List<BtDevice> _deviceList ) {
        	context  = _context;
        	rowResID = _rowResID;
        	deviceList  = _deviceList;
        	
        	layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
        }
        
        /*
         * @see android.widget.Adapter#getCount()
         */
        @Override
         public int getCount()
         {
                 return deviceList.size();
         }

         /*
         * @see android.widget.Adapter#getItem(int)
         */
        @Override
         public Object getItem(int position)
         {
                 return deviceList.get(position);
         }

         /*
         * (non-Javadoc)
         *
         * @see android.widget.Adapter#getItemId(int)
         */
        @Override
         public long getItemId(int position)
         {
                 return position;
         }

         /*
         * (non-Javadoc)
         *
         * @see android.widget.Adapter#getView(int, android.view.View,
         * android.view.ViewGroup)
         */
        @Override
         public View getView(int position, View convertView, ViewGroup parent)
         {
                 final BtDevice _device = deviceList.get(position);
                 final View view = layoutInflater.inflate(rowResID, null);

                 final TextView nameTextView = (TextView) view.findViewById(android.R.id.text1);
                 nameTextView.setText( _device.humanReadableName );
                 
                 final TextView addressTextView = (TextView) view.findViewById(android.R.id.text2);
                 addressTextView.setText( _device.btAddress );

                 return view;
         } 
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu_scan_devices, menu);
        return true;
    }
    
    /* Handles item selections */
    public boolean onOptionsItemSelected( MenuItem item ) {
        switch ( item.getItemId() ) {
        case R.id.scan:
            if ( AndroHid.btInterface.isEnabled() ) {
            	BtDeviceScan btDeviceScan = new BtDeviceScan();  
                Thread threadScanningDevices = new Thread(btDeviceScan);
            	threadScanningDevices.start();
            } else {
            	showToast( getString(R.string.bluetooth_not_enabled) );
            }
            
            return true;
        }
        return false;
    }
    
    public final void showToast(final String text) {
		try {
			Toast.makeText(this.getApplicationContext(), text,
					Toast.LENGTH_LONG).show();
		} catch (RuntimeException e) {
			Log.e(AndroHid.TAG, e.getMessage(), e);
		}
	}
}