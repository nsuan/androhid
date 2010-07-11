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

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class PresenterTab extends Activity implements OnClickListener {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.presenter_tab);
        
        findViewById(R.id.one).setOnClickListener(this);
        findViewById(R.id.two).setOnClickListener(this);
        findViewById(R.id.three).setOnClickListener(this);
        findViewById(R.id.four).setOnClickListener(this);
        findViewById(R.id.five).setOnClickListener(this);
        findViewById(R.id.six).setOnClickListener(this);
        findViewById(R.id.seven).setOnClickListener(this);
        findViewById(R.id.eight).setOnClickListener(this);
        findViewById(R.id.nine).setOnClickListener(this);
        findViewById(R.id.ten).setOnClickListener(this);
        findViewById(R.id.eleven).setOnClickListener(this);
        findViewById(R.id.twelve).setOnClickListener(this);
        
	}
    
	@Override
    protected void onResume(){
    	super.onResume(); 
    	// disable/enable rotation
        if ( AndroHid.myPreferences.getBoolean( "SETTINGS_ROTATE_SCREEN", true ) ) {
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }
      
    public final void onClick( final View v ){
    	switch (v.getId()){
    	case R.id.one:
    		this.pressButtonOne();
    		return;
    	case R.id.two:
    		this.pressButtonTwo();
    		return;
    	case R.id.three:
    		this.pressButtonThree();
    		return;
    	case R.id.four:
    		this.pressButtonFour();
    		return;
    	case R.id.five:
    		this.pressButtonFive();
    		return;
    	case R.id.six:
    		this.pressButtonSix();
    		return;
    	case R.id.seven:
    		this.pressButtonSeven();
    		return;
    	case R.id.eight:
    		this.pressButtonEight();
    		return;
    	case R.id.nine:
    		this.pressButtonNine();
    		return;
    	case R.id.ten:
    		this.pressButtonTen();
    		return;
    	case R.id.eleven:
    		this.pressButtonEleven();
    		return;
    	case R.id.twelve:
    		this.pressButtonTwelve();
    		return;
    	default:
    		return;
    	}
    }
    
    public int getIntegerFromPreferences(String key, int defaultValue){
    	int checkedPrefInteger = 0;
    	// Check User Input Strings
    	try {
    		checkedPrefInteger = Integer.parseInt(
    			AndroHid.myPreferences.getString(key, Integer.toString(defaultValue)));
    	} catch (Exception e) {
    		checkedPrefInteger = defaultValue;
		}
    	return checkedPrefInteger; 
    }
	
    public void pressButtonOne(){
    	AndroHid.btInterface.sendKeyDownEvent( 
    				getIntegerFromPreferences("SETTINGS_TAB_PRESENTER_BUTTON_1_MODIFIER", 0),
    				getIntegerFromPreferences("SETTINGS_TAB_PRESENTER_BUTTON_1_KEYCODE", 0));
		AndroHid.btInterface.sendKeyUpEvent();
    }
    
    public void pressButtonTwo(){
    	AndroHid.btInterface.sendKeyDownEvent( 
    				getIntegerFromPreferences("SETTINGS_TAB_PRESENTER_BUTTON_2_MODIFIER", 0),
    				getIntegerFromPreferences("SETTINGS_TAB_PRESENTER_BUTTON_2_KEYCODE", 0));
		AndroHid.btInterface.sendKeyUpEvent();
    }
    
    public void pressButtonThree(){
    	AndroHid.btInterface.sendKeyDownEvent( 
    				getIntegerFromPreferences("SETTINGS_TAB_PRESENTER_BUTTON_3_MODIFIER", 0),
    				getIntegerFromPreferences("SETTINGS_TAB_PRESENTER_BUTTON_3_KEYCODE", 0));
		AndroHid.btInterface.sendKeyUpEvent();
    }
    
    public void pressButtonFour(){
    	AndroHid.btInterface.sendKeyDownEvent( 
    				getIntegerFromPreferences("SETTINGS_TAB_PRESENTER_BUTTON_4_MODIFIER", 0),
    				getIntegerFromPreferences("SETTINGS_TAB_PRESENTER_BUTTON_4_KEYCODE", 0));
		AndroHid.btInterface.sendKeyUpEvent();
    }
    
    public void pressButtonFive(){
    	AndroHid.btInterface.sendKeyDownEvent( 
    				getIntegerFromPreferences("SETTINGS_TAB_PRESENTER_BUTTON_5_MODIFIER", 0),
    				getIntegerFromPreferences("SETTINGS_TAB_PRESENTER_BUTTON_5_KEYCODE", 0));
		AndroHid.btInterface.sendKeyUpEvent();
    }
    
    public void pressButtonSix(){
    	AndroHid.btInterface.sendKeyDownEvent( 
    				getIntegerFromPreferences("SETTINGS_TAB_PRESENTER_BUTTON_6_MODIFIER", 0),
    				getIntegerFromPreferences("SETTINGS_TAB_PRESENTER_BUTTON_6_KEYCODE", 0));
		AndroHid.btInterface.sendKeyUpEvent();
    }
    
    public void pressButtonSeven(){
    	AndroHid.btInterface.sendKeyDownEvent( 
    				getIntegerFromPreferences("SETTINGS_TAB_PRESENTER_BUTTON_7_MODIFIER", 0),
    				getIntegerFromPreferences("SETTINGS_TAB_PRESENTER_BUTTON_7_KEYCODE", 0));
		AndroHid.btInterface.sendKeyUpEvent();
    }
    
    public void pressButtonEight(){
    	AndroHid.btInterface.sendKeyDownEvent( 
    				getIntegerFromPreferences("SETTINGS_TAB_PRESENTER_BUTTON_8_MODIFIER", 0),
    				getIntegerFromPreferences("SETTINGS_TAB_PRESENTER_BUTTON_8_KEYCODE", 0));
		AndroHid.btInterface.sendKeyUpEvent();
    }
    
    public void pressButtonNine(){
    	AndroHid.btInterface.sendKeyDownEvent( 
    				getIntegerFromPreferences("SETTINGS_TAB_PRESENTER_BUTTON_9_MODIFIER", 0),
    				getIntegerFromPreferences("SETTINGS_TAB_PRESENTER_BUTTON_9_KEYCODE", 0));
		AndroHid.btInterface.sendKeyUpEvent();
    }
    
    public void pressButtonTen(){
    	AndroHid.btInterface.sendKeyDownEvent( 
    				getIntegerFromPreferences("SETTINGS_TAB_PRESENTER_BUTTON_10_MODIFIER", 0),
    				getIntegerFromPreferences("SETTINGS_TAB_PRESENTER_BUTTON_10_KEYCODE", 0));
		AndroHid.btInterface.sendKeyUpEvent();
    }
    
    public void pressButtonEleven(){
    	AndroHid.btInterface.sendKeyDownEvent( 
    				getIntegerFromPreferences("SETTINGS_TAB_PRESENTER_BUTTON_11_MODIFIER", 0),
    				getIntegerFromPreferences("SETTINGS_TAB_PRESENTER_BUTTON_11_KEYCODE", 0));
		AndroHid.btInterface.sendKeyUpEvent();
    }
    
    public void pressButtonTwelve(){
    	AndroHid.btInterface.sendKeyDownEvent( 
    				getIntegerFromPreferences("SETTINGS_TAB_PRESENTER_BUTTON_12_MODIFIER", 0),
    				getIntegerFromPreferences("SETTINGS_TAB_PRESENTER_BUTTON_12_KEYCODE", 0));
		AndroHid.btInterface.sendKeyUpEvent();
    }    
}