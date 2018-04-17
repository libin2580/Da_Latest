package com.meridian.dateout.model;

/**
 * Created by user 1 on 03-11-2016.
 */

public class CategoryModel {
    private String id;
    private String category;
    private String background;
    private String all_background;
    private String type,_18plusOnly;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAll_background() {
        return all_background;
    }

    public void setAll_background(String all_background) {
        this.all_background = all_background;
    }

    private String icon;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void set_18plusOnly(String _18plusOnly) {
        this._18plusOnly = _18plusOnly;
    }

    public String get_18plusOnly() {
        return _18plusOnly;
    }
}
