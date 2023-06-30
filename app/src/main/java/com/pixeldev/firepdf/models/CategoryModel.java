package com.pixeldev.firepdf.models;

import java.io.Serializable;
import java.util.ArrayList;

public class CategoryModel implements Serializable {
    public String category_id;
    public String title;
    public ArrayList<TopicModel> topiclist;


    public String getCategory_id() {
        return this.category_id;
    }

    public void setCategory_id(String bid2) {
        this.category_id = bid2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String book_name2) {
        this.title = book_name2;
    }

}
