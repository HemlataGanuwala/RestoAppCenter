package com.flavourheight.apple.skyrestaurantapp.Model;

public class CartListMenuPlanet {

    String ItemName;
    String Cost;
    String TotalCost;
    String TotalCount;


    public CartListMenuPlanet(String itemName, String cost, String totalCost, String totalCount)
    {
        this.ItemName=itemName;
        this.Cost=cost;
        this.TotalCost=totalCost;
        this.TotalCount=totalCount;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getCost() {
        return Cost;
    }

    public void setCost(String cost) {
        Cost = cost;
    }

    public String getTotalCost() {
        return TotalCost;
    }

    public void setTotalCost(String totalCost) {
        TotalCost = totalCost;
    }

    public String getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(String totalCount) {
        TotalCount = totalCount;
    }
}
