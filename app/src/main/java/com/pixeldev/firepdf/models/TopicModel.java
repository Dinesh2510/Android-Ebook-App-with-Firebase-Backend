package com.pixeldev.firepdf.models;

import java.io.Serializable;
import java.util.ArrayList;

public class TopicModel implements Serializable {
    public String pdf_id;
    public String title;
    public String image;
    public String link;
    public String content;

    public String getPdf_id() {
        return this.pdf_id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setPdf_id(String bid2) {
        this.pdf_id = bid2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String book_name2) {
        this.title = book_name2;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String book_thumb_url2) {
        this.image = book_thumb_url2;
    }



    public String getContent() {
        return this.content;
    }

    public void setContent(String desc2) {
        this.content = desc2;
    }
}
