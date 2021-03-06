package com.alcoapps.actionbarextras;

import java.util.HashMap;
import java.util.List;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollPropertyChange;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.KrollProxyListener;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.util.TiUIHelper;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;


@Kroll.proxy(creatableInModule=ActionbarextrasModule.class, propertyAccessors="activeItem")
public class DropdownProxy extends KrollProxy implements KrollProxyListener  {
	
	ActionBar.OnNavigationListener navigationListener = new ActionBar.OnNavigationListener() {

        @Override
        public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        	
        	setProperty("activeItem", itemPosition);
            
        	if (hasListeners("change")) {
    			HashMap<String, Object> event = new HashMap<String, Object>();
    			event.put("index", itemPosition);
    			fireEvent("change", event);
    		}
        	
            return false;
        }
    };
	
	public DropdownProxy() {
		super();
	}
	
	@Override
	public void handleCreationDict(KrollDict options) {
		
		final ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
		final boolean keepTitle;
		
		if (options.containsKey("keepTitle")) {
			keepTitle = options.getBoolean("keepTitle");
		}else{
			keepTitle = false;
		}
		
		TiUIHelper.runUiDelayedIfBlock(new Runnable() {
			@Override
			public void run() {
				actionBar.setDisplayShowTitleEnabled(keepTitle);
			    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
			}
		});
	    
	    if (options.containsKey("titles")) {
	    	final String[] dropdownValues = options.getStringArray("titles");
	    	
	        ArrayAdapter<String> adapter = new ArrayAdapter<String>(actionBar.getThemedContext(),
	            android.R.layout.simple_spinner_item, android.R.id.text1,
	            dropdownValues);

	        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

	        actionBar.setListNavigationCallbacks(adapter, navigationListener);
		}

		super.handleCreationDict(options);
	}
	
	@Kroll.method
	public void remove(){
		
		final ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
		
		TiUIHelper.runUiDelayedIfBlock(new Runnable() {
			@Override
			public void run() {
				actionBar.setDisplayShowTitleEnabled(true);
			    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			}
		});
	}
    
	@Override
	public void propertyChanged(String key, Object oldValue, Object newValue, KrollProxy proxy) {
        if ((oldValue == newValue) ||
            ((oldValue != null) && oldValue.equals(newValue))) {
            return;
        }
        
        if (key.equals("activeItem")){
        	final ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        	final int activeItem = (Integer) newValue;
    		
    		TiUIHelper.runUiDelayedIfBlock(new Runnable() {
    			@Override
    			public void run() {
    				actionBar.setSelectedNavigationItem(activeItem);
    			}
    		});
        }
	}

	@Override
	public void listenerAdded(String type, int count, KrollProxy proxy) {
		
	}

	@Override
	public void listenerRemoved(String type, int count, KrollProxy proxy) {
		
	}

	@Override
	public void processProperties(KrollDict dict) {
		
	}

	@Override
	public void propertiesChanged(List<KrollPropertyChange> changes, KrollProxy proxy) {
		
	}

}
