package com.excel.digitalsignagelauncher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.excel.customitems.CustomItems;
import com.excel.digitalsignage.ConfigurationReader;
import com.excel.excelclasslibrary.UtilMisc;
import com.excel.excelclasslibrary.UtilNetwork;
import com.excel.excelclasslibrary.UtilShell;


public class ShortcutsActivity extends Activity {
	
	TextView tv_mac_address, tv_firmware_version, tv_cms_ip;
	Button  bt_reboot, bt_root_browser, bt_mbox, bt_settings,
			bt_reboot_recovery, bt_ip_address,
			bt_run_ots;
	Context context = this;
	final static String TAG = "ShortcutsActivity";
	LinearLayout ll_left_remaining, ll_right_remaining;

	ConfigurationReader configurationReader = null;
	
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_minimum_shortcuts );
		
		init();

	}

	public void init(){
		initViews();
		
		Intent in = getIntent();
		String who = in.getStringExtra( "who" );
		if( who.equals( "xkx" ) ){ // staff -> hide all remaining views
			ll_left_remaining.setVisibility( View.INVISIBLE );
			ll_right_remaining.setVisibility( View.INVISIBLE );
		}
		else{
			ll_left_remaining.setVisibility( View.VISIBLE );
			ll_right_remaining.setVisibility( View.VISIBLE );
		}
		
		// Set Mac Address
		setMacAddress();

		// Set Firmware Version
		setFirmwareVersion();
		
		// Reboot Box
		rebootBoxButtonClick();

		try {
			// show IP Address
			ipAddressRetrieve();
		}
		catch( Exception e ){
			e.printStackTrace();
		}

		// Run OTS
		runOTSButtonClick();

		// Set CMS IP
		setCMSIP();

		// Root Browser Click
		rootBrowserClick();
		
		// MBox Settings Click
		mboxSettingsClick();
		
		// Android Settings Click
		androidSettingsClick();
		
		// Reboot To Recovery
		rebootToRecovery();
	}

	@Override
	protected void onResume() {
		super.onResume();

		configurationReader = ConfigurationReader.reInstantiate();
	}

	public void initViews(){
		configurationReader = ConfigurationReader.getInstance();

		tv_firmware_version = (TextView) findViewById( R.id.tv_firmware_version );
		tv_mac_address = (TextView) findViewById( R.id.tv_mac_address );
		tv_cms_ip = (TextView) findViewById( R.id.tv_cms_ip );
		bt_reboot = (Button) findViewById( R.id.bt_reboot_box );
		ll_left_remaining = (LinearLayout) findViewById( R.id.ll_left_remaining );
		ll_right_remaining = (LinearLayout) findViewById( R.id.ll_right_remaining );
		bt_root_browser = (Button) findViewById( R.id.bt_root_browser );
		bt_mbox = (Button) findViewById( R.id.bt_mbox );
		bt_settings = (Button) findViewById( R.id.bt_settings );
		bt_reboot_recovery = (Button) findViewById( R.id.bt_reboot_recovery );
		bt_ip_address = (Button) findViewById( R.id.bt_ip_address );
		bt_run_ots = (Button) findViewById( R.id.bt_run_ots );
	}
	
	public void setMacAddress(){
		String mac_address = null;
		try{
			mac_address = UtilNetwork.getMacAddress( context );
		}
		catch ( Exception e ){
			mac_address = null;
			e.printStackTrace();
		}
		if( mac_address == null )
			mac_address = "Network Disconnected";

		tv_mac_address.setText( mac_address );

	}
	
	public void setFirmwareVersion(){
		tv_firmware_version.setText( configurationReader.getFirmwareName() );
	}

	public void rebootBoxButtonClick(){
		bt_reboot.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick( View v ) {
				UtilShell.executeShellCommandWithOp( "reboot" );
			}
		});
	}

	public void ipAddressRetrieve(){
		String ip = "";
		try {
			ip = UtilNetwork.getLocalIpAddressIPv4(context);
		}
		catch( Exception e ){
			ip = null;
		}
		if( ip == null ){
			ip = "Network Disconnected";
		}
		bt_ip_address.setText( ip );
	}

	public void runOTSButtonClick(){
		final String is_ots_completed = configurationReader.getIsOtsCompleted();
		if( is_ots_completed.equals( "1" ) ){
			bt_run_ots.setText( "OTS Completed" );
		}
		else{
			bt_run_ots.setText( "Click To Run OTS" );
		}

		bt_run_ots.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick( View v ) {

				if( is_ots_completed.equals( "1" ) ){
					CustomItems.showCustomToast( context, "warning", "Digital Signage Setup has already been completed !", 5000 );
					return;
				}
				else{
					UtilMisc.startApplicationUsingPackageName( context, "com.excel.digitalsignagesetup.secondgen" );
					return;
				}
			}

		});
	}

	public void rootBrowserClick(){
		bt_root_browser.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick( View v ) {
				if( !UtilMisc.startApplicationUsingPackageName( context, "com.jrummy.root.browserfree" ) ){
					CustomItems.showCustomToast( context, "error", "Root Borwser Free version is not installed", 5000 );
					
					if( !UtilMisc.startApplicationUsingPackageName( context, "com.jrummy.root.browser" ) ){
						CustomItems.showCustomToast( context, "error", "Root Borwser Full version is not installed", 5000 );
					}
				}
			}
		});
	}

	public void mboxSettingsClick(){
		bt_mbox.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick( View v ) {
                if( Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT ) {
                    if (!UtilMisc.startApplicationUsingPackageName(context, "com.mbx.settingsmbox") ) {
                        //if( !UtilMisc.startApplicationUsingPackageName( context, "com.mbx.settingsmbox" ) ){
                        CustomItems.showCustomToast(context, "error", "GIEC Settings not found", 5000);
                    }
                }
                else {
                    if (!UtilMisc.startApplicationUsingPackageName(context, "com.sdmc.settings")) {
                        //if( !UtilMisc.startApplicationUsingPackageName( context, "com.mbx.settingsmbox" ) ){
                        CustomItems.showCustomToast(context, "error", "SDMC Settings not found", 5000);
                    }
                }
			}
		});
	}
	
	public void androidSettingsClick(){
		bt_settings.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick( View v ) {
				Log.d( TAG, "Open Settings" );

				if( Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT ) {
					UtilShell.executeShellCommandWithOp( "monkey -p com.android.settings -c android.intent.category.LAUNCHER 1" );
				}
				else {
					UtilShell.executeShellCommandWithOp( "am start -a android.intent.action.MAIN -n com.android.settings/.Settings" );
				}
			}
		});
	}


	public void rebootToRecovery(){
		bt_reboot_recovery.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick( View v ) {
				UtilShell.executeShellCommandWithOp( "reboot recovery" );
			}
		});
	}

	public void setCMSIP(){
		try {
			tv_cms_ip.setText( configurationReader.getCmsIp() );
		}
		catch ( Exception e ){
			e.printStackTrace();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		Log.i( TAG, "onPause()" );

		finish();
	}
}
