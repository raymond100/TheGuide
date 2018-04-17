package com.ray100.theguide;

import java.io.Serializable;

/**
 * Created by ray100 on 07/03/18.
 */

public class Guide implements Serializable {

    private String guideName;
    private String guideStreet;
    private String guideDesc;

    /**
     * Image Resource ID for the word
     */
    private String imageResourceID =NO_IMAGE_PROVIDED;

    private static final String NO_IMAGE_PROVIDED = "";

    public Guide(String guideName, String guideStreet){
        this.guideName = guideName;
        this.guideStreet = guideStreet;
    }

    public Guide(String guideName, String guideStreet,String imageResourceID){
        this(guideName,guideStreet);
        this.imageResourceID = imageResourceID;
    }

    public Guide(String guideName, String guideStreet, String guideDesc, String imageResourceID){
        this(guideName,guideStreet);
        this.guideDesc = guideDesc;
        this.imageResourceID = imageResourceID;
    }

    public String getGuideName(){
        return this.guideName;
    }

    public String getGuideStreet(){
        return this.guideStreet;
    }

    /**
     * Get the Image Resource ID
     * @return
     */
    public  String getImageResourceID(){
        return this.imageResourceID;
    }

    public  boolean hasImage(){
        return imageResourceID != NO_IMAGE_PROVIDED;
    }
}
