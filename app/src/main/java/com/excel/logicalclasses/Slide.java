package com.excel.logicalclasses;

import org.json.JSONObject;

public class Slide {

    private String type;
    private String animation;
    private long duration;
    private long pauseDuration;
    private JSONObject meta;

    public Slide(){}

    public Slide( String type, String animation, long duration, long pauseDuration, JSONObject meta ){
        this.setType(type);
        this.setAnimation(animation);
        this.setDuration(duration);
        this.setPauseDuration(pauseDuration);
        this.setMeta(meta);
    }










    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAnimation() {
        return animation;
    }

    public void setAnimation(String animation) {
        this.animation = animation;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getPauseDuration() {
        return pauseDuration;
    }

    public void setPauseDuration(long pauseDuration) {
        this.pauseDuration = pauseDuration;
    }

    public JSONObject getMeta() {
        return meta;
    }

    public void setMeta(JSONObject meta) {
        this.meta = meta;
    }


    // Getting information from Meta
    public String getImageName(){
        String imageName = "error";
        try{
            imageName = getMeta().getString( "image_name" );
        }
        catch ( Exception e ){
            e.printStackTrace();
        }
        return imageName;
    }

    public String getVideoName(){
        String imageName = "error";
        try{
            imageName = getMeta().getString( "video_name" );
        }
        catch ( Exception e ){
            e.printStackTrace();
        }
        return imageName;
    }
    // Getting information from Meta
}
