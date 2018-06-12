package com.excel.digitalsignagelauncher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.excel.customviews.ScrollTextView;
import com.excel.digitalsignage.ConfigurationReader;
import com.excel.dstemplates.TemplateImage;
import com.excel.excelclasslibrary.UtilShell;
import com.excel.logicalclasses.Slide;
import com.excel.logicalclasses.SlideFlipper;

import org.json.JSONObject;

import java.util.Stack;

public class HomeScreen extends AppCompatActivity {

    final static String TAG = "HomeScreen";
    Context context = this;
    ConfigurationReader configurationReader = null;
    Slide[] slides;
    SlideFlipper slideFlipper;


    // Views
    ScrollTextView tv_collar_text;
    View template_image;
    RelativeLayout rl_primary_layer;
    RelativeLayout rl_secondary_layer;
    // Views


    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_home_screen );
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        init();

    }

    private void init(){
        //configurationReader = ConfigurationReader.getInstance();

        initViews();
    }

    private void initViews(){
        tv_collar_text = (ScrollTextView) findViewById( R.id.tv_collar_text );
        setCollarText();

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService( LAYOUT_INFLATER_SERVICE );
        template_image = layoutInflater.inflate( R.layout.template_image, null );

        rl_primary_layer = (RelativeLayout) findViewById( R.id.rl_primary_layer );
        rl_secondary_layer = (RelativeLayout) findViewById( R.id.rl_secondary_layer );

        slides = new Slide[ 6 ];
        slides[ 0 ] = new Slide( "image", "fade", 5000, 2000, getSampleMeta( 0 ) );
        slides[ 1 ] = new Slide( "image", "fade", 5000, 5000, getSampleMeta( 1 ) );
        slides[ 2 ] = new Slide( "image", "fade", 5000, 2000, getSampleMeta( 2 ) );
        slides[ 3 ] = new Slide( "image", "fade", 5000, 5000, getSampleMeta( 3 ) );
        slides[ 4 ] = new Slide( "image", "fade", 5000, 2000, getSampleMeta( 4 ) );
        slides[ 5 ] = new Slide( "hybrid1", "fade", 24000, 2000, getSampleMeta( 5 ) );

        slideFlipper = new SlideFlipper( context, rl_primary_layer, rl_secondary_layer, slides );
        slideFlipper.startSlideFlipping();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //configurationReader = ConfigurationReader.reInstantiate();
    }

    private void setCollarText(){
        tv_collar_text.setText( "Thank you for choosing Hotel Boss as your choice of stay. Tour Desk is located at the hotel lobby if you require attraction tickets with promotional rates. Please dial “0” from the hotel phone if you require assistance." );
        tv_collar_text.setSpeed( new Double( 83.67 ) );
        tv_collar_text.startScroll();
    }



    public JSONObject getSampleMeta( int index ){
        JSONObject jsonObject = new JSONObject();


        try {
            switch ( index ) {
                case 0:
                    jsonObject.put("image_name", "1.jpg" );
                    break;

                case 1:
                    jsonObject.put("image_name", "2.jpg" );
                    break;

                case 2:
                    jsonObject.put("image_name", "3.jpg" );
                    break;

                case 3:
                    jsonObject.put("image_name", "4.jpg" );
                    break;

                case 4:
                    jsonObject.put("image_name", "5.jpg" );
                    break;

                case 5:
                    jsonObject.put("image_name", "3.jpg" );
                    jsonObject.put("video_name", "1.mp4" );
                    break;
            }
        }
        catch ( Exception e ){
            e.printStackTrace();
        }


        return jsonObject;
    }



    //
    //
    // Not much used functions
    //
    //

    Stack<String> key_combination = new Stack<String>();
    static String Z     = "KEYCODE_Z";
    static String K     = "KEYCODE_K";
    static String X     = "KEYCODE_X";
    static String P     = "KEYCODE_P";
    static String O     = "KEYCODE_O";
    static String ONE   = "KEYCODE_1";
    static String THREE = "KEYCODE_3";
    static String NINE  = "KEYCODE_9";
    static String SEVEN = "KEYCODE_7";
    static String DOT   = "KEYCODE_PERIOD";
    String ALPHABET     = "KEYCODE_";

    @Override
    public boolean onKeyDown( int keyCode, KeyEvent keyevent ){
        String key_name = KeyEvent.keyCodeToString( keyCode );
        Log.i( TAG, "KeyPressed : " + keyCode + "," + key_name );
        // if( ( i == 19 ) || ( i == 20 ) || ( i == 21 ) || ( i == 22 ) )
        // return true;

        // Short-Cut key toggling
        shortCutKeyMonitor( key_name );

        if ( keyCode != keyevent.KEYCODE_BACK ) {
            return super.onKeyDown( keyCode, keyevent );
        }
        return true;
    }

    public void shortCutKeyMonitor( String key_name ){
        key_combination.push( key_name );

        if( key_combination.size() == 3 ){
            String key_3 = key_combination.pop();
            String key_2 = key_combination.pop();
            String key_1 = key_combination.pop();

            // Z-K-Z
            if( key_1.equals( Z ) && key_2.equals( K ) && key_3.equals( Z ) ){
                Intent in = new Intent( context, ShortcutsActivity.class );
                in.putExtra( "who", "zkz" );
                startActivity( in );
            }

            // X-K-X
            else if( key_1.equals( X ) && key_2.equals( K ) && key_3.equals( X ) ){
                Intent in = new Intent( context, ShortcutsActivity.class );
                in.putExtra( "who", "xkx" );
                startActivity( in );
            }

            // 1-.-1  -> ZKZ
            if( key_1.equals( ONE ) && key_2.equals( DOT ) && key_3.equals( ONE ) ){
                Intent in = new Intent( context, ShortcutsActivity.class );
                in.putExtra( "who", "zkz" );
                startActivity( in );
            }
            // 1-7-1 -> ZKZ
            if( key_1.equals( ONE ) && key_2.equals( SEVEN ) && key_3.equals( ONE ) ){
                Intent in = new Intent( context, ShortcutsActivity.class );
                in.putExtra( "who", "zkz" );
                startActivity( in );
            }
            // 3-1-3  -> XKX
            else if( key_1.equals( THREE ) && key_2.equals( ONE ) && key_3.equals( THREE ) ){
                Intent in = new Intent( context, ShortcutsActivity.class );
                in.putExtra( "who", "xkx" );
                startActivity( in );
            }
            // 9-1-9
            else if( key_1.equals( NINE ) && key_2.equals( ONE ) && key_3.equals( NINE ) ){
                UtilShell.executeShellCommandWithOp( "reboot" );
            }
            // P-O-P  -> Refresh Launcher
            else if( key_1.equals( P ) && key_2.equals( O ) && key_3.equals( P ) ){
                configurationReader = ConfigurationReader.reInstantiate();
                recreate();
            }
            // 9.9  -> Refresh Launcher
            else if( key_1.equals( NINE ) && key_2.equals( DOT ) && key_3.equals( NINE ) ){
                configurationReader = ConfigurationReader.reInstantiate();
                recreate();
            }
            key_combination.removeAllElements();
        }
    }
}
