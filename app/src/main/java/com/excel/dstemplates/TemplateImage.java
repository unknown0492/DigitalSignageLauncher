package com.excel.dstemplates;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.excel.digitalsignage.ConfigurationReader;
import com.excel.digitalsignagelauncher.R;
import com.excel.imagemanipulator.ImageManipulator;
import com.excel.logicalclasses.Slide;

import java.io.File;

public class TemplateImage {

    final static String TAG = "TemplateImage";
    Slide slide;
    LayoutInflater layoutInflater;
    Context context;
    ConfigurationReader configurationReader;

    private TemplateImage() {}

    public TemplateImage( Context context, Slide slide ){
        layoutInflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        this.slide = slide;
        this.context = context;
        //configurationReader = ConfigurationReader.getInstance();
    }

    public View getView(){
        View view = layoutInflater.inflate( R.layout.template_image, null );
        ImageView iv = view.findViewById( R.id.iv );
        //Drawable dr = ImageManipulator.getDecodedDrawable( configurationReader.getImagesDirectoryPath() + File.separator + slide.getImageName(),1020, 1920 );
        Drawable dr = ImageManipulator.getDecodedDrawable( "/mnt/sdcard/ds_data/graphics/images/" + slide.getImageName(),1080, 1920 );
        iv.setBackgroundDrawable( dr );
        return view;
    }
}
