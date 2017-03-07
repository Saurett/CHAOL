package com.indev.chaol.models;

/**
 * Created by jvier on 07/03/2017.
 */

public class DecodeItem {

    private int position;
    private Object itemModel;
    private int idView;

    public DecodeItem() {
        itemModel = new Object();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Object getItemModel() {
        return itemModel;
    }

    public void setItemModel(Object itemModel) {
        this.itemModel = itemModel;
    }

    public int getIdView() {
        return idView;
    }

    public void setIdView(int idView) {
        this.idView = idView;
    }
}
