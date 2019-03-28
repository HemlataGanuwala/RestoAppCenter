package com.flavourheight.apple.skyrestaurantapp.Model;

public class ItemPlanet {

    private String Itemname;
    private String SubItemname;
    private String Rate;
    private String Image;
    String Favourite;

    public ItemPlanet(String itemname, String subItemname, String rate,String image, String favourite)
    {
        this.Itemname = itemname;
        this.SubItemname = subItemname;
        this.Rate = rate;
        this.Image = image;
        this.Favourite = favourite;
    }

    public String getItemname() {
        return Itemname;
    }

    public void setItemname(String itemname) {
        Itemname = itemname;
    }

    public String getSubItemname() {
        return SubItemname;
    }

    public void setSubItemname(String subItemname) {
        SubItemname = subItemname;
    }

    public String getRate() {
        return Rate;
    }

    public void setRate(String rate) {
        Rate = rate;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
    public String getFavourite() {
        return Favourite;
    }

    public void setFavourite(String favourite) {
        Favourite = favourite;
    }
}
