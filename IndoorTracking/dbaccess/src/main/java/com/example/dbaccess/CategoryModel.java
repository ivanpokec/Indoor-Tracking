package com.example.dbaccess;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Paula on 19.12.2016..
 */

public class CategoryModel {

    public int catId;
    public String catName;

    public CategoryModel(int catId, String catName) {
        this.catId = catId;
        this.catName = catName;

    }

}
