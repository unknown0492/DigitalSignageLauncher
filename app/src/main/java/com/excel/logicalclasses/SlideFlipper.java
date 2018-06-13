package com.excel.logicalclasses;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.excel.dstemplates.TemplateHybrid1;
import com.excel.dstemplates.TemplateImage;

public class SlideFlipper {

    final static String TAG = "SlideFlipper";
    Context context;
    Slide[] slides;

    RelativeLayout primaryLayer;
    RelativeLayout secondaryLayer;
    boolean primaryLayerVisible, secondaryLayerVisible;

    Runnable slideFlippingRunnable = null;
    Handler slideFlippingHandler = null;

    int currentIndex = 0;
    int totalSlides = 0;

    private SlideFlipper(){}

    public SlideFlipper( Context context, RelativeLayout primaryLayer, RelativeLayout secondaryLayer, Slide[] slides ){
        this.context = context;
        this.slides = slides;
        totalSlides = slides.length;
        currentIndex = 0;
        this.primaryLayer = primaryLayer;
        this.secondaryLayer = secondaryLayer;
    }

    public void startSlideFlipping(){

        slideFlippingHandler = new Handler();

        //showSlide();

        slideFlippingRunnable = new Runnable() {

            @Override
            public void run() {
                showSlide();
            }

        };

        slideFlippingHandler.post( slideFlippingRunnable );
    }

    boolean is_even = true;

    private void showSlide(){

        Log.d( TAG, "showSlide()" );

        // Calculate even or odd case, even->secondary display, odd->primary display
        boolean even = ((currentIndex+1)%2==0)?true:false;
        is_even = !is_even;
        Log.d( TAG,  ((is_even)?"Even":"Odd") + "-" +currentIndex );

        // Get current slide information
        final Slide currentSlide = getCurrentSlide();
        View view = generateViewForSlide( currentSlide );

        if( !is_even ){ // Odd case --> Set the content on the Primary Layer
            primaryLayer.removeAllViews();
            primaryLayer.invalidate();
            primaryLayer.addView( view );
        }
        else{   // Even case --> Set the content on the Secondary Layer
            secondaryLayer.removeAllViews();
            secondaryLayer.invalidate();
            secondaryLayer.addView( view );
        }

        // Run after a delay of 1 second, so that the loading of stuffs on the slides would finish in this duration
        new Handler().postDelayed( new Runnable() {

            @Override
            public void run() {

                if( !is_even ){ // Odd case --> Hide the secondary layer, show the primary layer
                    ObjectAnimator.ofFloat( secondaryLayer, "alpha", 1.0f, 0.0f ).setDuration( 1000 ).start();
                    ObjectAnimator.ofFloat( primaryLayer, "alpha", 0.0f, 1.0f ).setDuration( 1000 ).start();
                }
                else{   // Even case --> Hide the primary layer, show the secondary layer
                    ObjectAnimator.ofFloat( primaryLayer, "alpha", 1.0f, 0.0f ).setDuration( 1000 ).start();
                    ObjectAnimator.ofFloat( secondaryLayer, "alpha", 0.0f, 1.0f ).setDuration( 1000 ).start();
                }

                // Do the scheduling for the next slide
                slideFlippingHandler.postDelayed( slideFlippingRunnable, currentSlide.getDuration() );

                // Increment to next slide
                nextSlide();

            }

        }, 1000 );

    }

    private View generateViewForSlide( Slide slide ){

        // Get the Type of Slide
        String type = slide.getType();

        // Retrieve appropriate template from the dstemplates package for the Slide Type
        if( type.equals( "image" ) ){
            TemplateImage templateImage = new TemplateImage( context, slide );
            return templateImage.getView();
        }
        else if( type.equals( "hybrid1" ) ){
            TemplateHybrid1 templateHybrid1 = new TemplateHybrid1( context, slide );
            return templateHybrid1.getView();
        }

        return null;
    }

    public void stopSlideFlipping(){
        Log.e( TAG, "stopSlideFlipping()" );

        if( slideFlippingHandler != null )
            slideFlippingHandler.removeCallbacks( slideFlippingRunnable );
    }

    public Slide getCurrentSlide(){
        return slides[ currentIndex ];
    }

    public Slide getNextSlide(){
        if( currentIndex == ( totalSlides - 1 ) ){
            return slides[ 0 ];
        }
        return slides[ currentIndex + 1 ];
    }

    public Slide getPreviousSlide(){
        if( currentIndex == 0 ){
            return slides[ totalSlides - 1 ];
        }
        return slides[ currentIndex - 1 ];
    }

    private void nextSlide(){
        if( currentIndex == ( totalSlides - 1 ) ){
            currentIndex = 0;
            return;
        }
        currentIndex += 1;
    }

}
