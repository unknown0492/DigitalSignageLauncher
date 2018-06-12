package com.excel.dstemplates;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.excel.digitalsignage.ConfigurationReader;
import com.excel.digitalsignagelauncher.R;
import com.excel.imagemanipulator.ImageManipulator;
import com.excel.logicalclasses.Slide;

public class TemplateHybrid1 {

    final static String TAG = "TemplateHybrid1";
    Slide slide;
    LayoutInflater layoutInflater;
    Context context;
    ConfigurationReader configurationReader;
    VideoView videoView;
    MediaPlayer mediaPlayer;
    MediaPlayer.OnPreparedListener mediaPlayerOnPreparedListener;

    private TemplateHybrid1() {}

    public TemplateHybrid1( Context context, Slide slide ){
        layoutInflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        this.slide = slide;
        this.context = context;
        //configurationReader = ConfigurationReader.getInstance();
       /* mediaPlayer = new MediaPlayer();
        mediaPlayer.setLooping( false );
        mediaPlayerOnPreparedListener = new MediaPlayer.OnPreparedListener(){

            @Override
            public void onPrepared( MediaPlayer mp ){
                mp.setLooping( false );
                mp.start();
            }
        };*/

    }

    @SuppressLint("NewApi")
    public View getView(){
        Log.i( TAG, "HybridView generated !" );
        View view = layoutInflater.inflate( R.layout.template_hybrid1, null );
        ImageView iv = view.findViewById( R.id.iv );
        final VideoView vv = view.findViewById( R.id.vv );
        //Drawable dr = ImageManipulator.getDecodedDrawable( configurationReader.getImagesDirectoryPath() + File.separator + slide.getImageName(),1020, 1920 );
        Drawable dr = ImageManipulator.getDecodedDrawable( "/mnt/sdcard/ds_data/graphics/images/" + slide.getImageName(),1080, 1920 );
        iv.setBackgroundDrawable( dr );

        //Uri uri = Uri.parse( "/mnt/sdcard/ds_data/graphics/videos/" + slide.getVideoName() ); //Declare your url here.

        vv.setMediaController( new MediaController( context ) );
        vv.setVideoPath( "/mnt/sdcard/ds_data/graphics/videos/" + slide.getVideoName() );
        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared( MediaPlayer mp ) {
                Log.i( TAG, "onPrepared()" );
                vv.start();
            }

        });

        //vv.setOnPreparedListener( mediaPlayerOnPreparedListener );
        /*vv.setOnCompletionListener( new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion( MediaPlayer mp ) {
                Log.i( TAG, "Video completed" );
                vv.stopPlayback();

            }
        });
        vv.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.e( TAG, "onError()" );
                return true;
            }
        });
        vv.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                Log.i( TAG, "onInfo()" );
                return true;
            }
        });
        */
        videoView = vv;
        // vv.requestFocus();
        // vv.start();

        return view;
    }

    public VideoView getVideoView(){
        return videoView;
    }
}
