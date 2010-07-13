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

import java.util.Iterator;
import java.util.Map;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;

public class Preferences extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener { 
	@Override 
	public void onCreate( Bundle savedInstanceState ) { 
    	super.onCreate( savedInstanceState ); 
       	addPreferencesFromResource( R.layout.preferences );
    	// Register the AndroHid shared Preferences Manager
    	AndroHid.myPreferences.registerOnSharedPreferenceChangeListener( this );
	}

	@Override
	public void onDestroy()	{
		super.onDestroy();
		// Unregister the AndroHid shared Preferences Manager
		AndroHid.myPreferences.unregisterOnSharedPreferenceChangeListener( this );
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		updateSummaries();
	   
	    Log.d( AndroHid.TAG ,"PREFERENCE_CHANGED" );
	}
	
	/* Sets for all EditTextPrefences the Entry as Summary */
	private void updateSummaries(){
		Map<String,?> allPreferenceEntries = AndroHid.myPreferences.getAll();
	    Iterator<?> allPreferenceEntriesIterator = allPreferenceEntries.entrySet().iterator();
	    while ( allPreferenceEntriesIterator.hasNext() ) {
	        @SuppressWarnings( "rawtypes" )
			Map.Entry pairs = ( Map.Entry ) allPreferenceEntriesIterator.next();
	        
	        Preference changedPreference = findPreference( ( CharSequence ) pairs.getKey() );

	        if ( changedPreference instanceof EditTextPreference ) {
	        	EditTextPreference editTextPreference = ( EditTextPreference ) changedPreference;
	        	changedPreference.setSummary( editTextPreference.getText() );
	        }
	    }
	}
}


