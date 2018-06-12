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


        if( is_even ){
            // Log.e( TAG, "inside if" );

            // Fade out primary
            ObjectAnimator.ofFloat( secondaryLayer, "alpha", 1.0f, 0.0f ).setDuration( 1000 ).start();
            // Fade in secondary
            ObjectAnimator.ofFloat( primaryLayer, "alpha", 0.0f, 1.0f ).setDuration( 1000 ).start();

            primaryLayerVisible = true;
            secondaryLayerVisible = false;

        }
        else{
            // Log.e( TAG, "inside else" );

            // Fade out secondary
            ObjectAnimator.ofFloat( primaryLayer, "alpha", 1.0f, 0.0f ).setDuration( 1000 ).start();
            // Fade in primary
            ObjectAnimator.ofFloat( secondaryLayer, "alpha", 0.0f, 1.0f ).setDuration( 1000 ).start();

            primaryLayerVisible = false;
            secondaryLayerVisible = true;
        }

        // Get current slide information
        Slide currentSlide = getCurrentSlide();

        View viewPrimary = generateViewForSlide( currentSlide );

        if( !is_even ) {
            primaryLayer.removeAllViews();
            primaryLayer.invalidate();
            primaryLayer.addView( viewPrimary ); // for odd
        }
        else {
            secondaryLayer.removeAllViews();
            secondaryLayer.invalidate();
            secondaryLayer.addView( viewPrimary ); // for even
        }

        // Get Next slide information
        Slide nextSlide = getNextSlide();

        View viewSecondary = generateViewForSlide( nextSlide );

        if( !is_even ) {
            secondaryLayer.removeAllViews();
            secondaryLayer.invalidate();
            secondaryLayer.addView( viewSecondary );  // for odd
        }
        else {
            primaryLayer.removeAllViews();
            primaryLayer.invalidate();
            primaryLayer.addView( viewSecondary );  // for even
        }

        if( primaryLayerVisible ){
            Log.i( TAG, "Primary Layer is visible" );
            //currentSlide.get
        }
        else{
            Log.i( TAG, "Secondary Layer is visible" );
        }

        // Increment to next slide
        nextSlide();

        // Do the scheduling for the next slide
        slideFlippingHandler.postDelayed( slideFlippingRunnable, nextSlide.getDuration() );
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
