package com.flavourheights.apple.skyrestaurantapp.Model;

public class FavouritePlanet {

    String ItemName;
    String SubItemName;
    String Rate;
    String FImage;

    public FavouritePlanet(String itemName, String subItemName, String rate, String image)
    {
        this.ItemName = itemName;
        this.SubItemName = subItemName;
        this.Rate =rate;
        this.FImage = image;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getSubItemName() {
        return SubItemName;
    }

    public void setSubItemName(String subItemName) {
        SubItemName = subItemName;
    }

    public String getRate() {
        return Rate;
    }

    public void setRate(String rate) {
        Rate = rate;
    }

    public String getFImage() {
        return FImage;
    }

    public void setFImage(String image) {
        FImage = image;
    }
}
