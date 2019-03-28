package com.flavourheight.apple.skyrestaurantapp.Model;

public class OrderDetailsPlanet {

    String SubitemNmae;
    String ItemeRate;
    String TotalCount;
    String TotalAmount;

    public  OrderDetailsPlanet(String subitemNmae, String itemeRate, String totalCount, String totalAmount)
    {
        this.SubitemNmae = subitemNmae;
        this.ItemeRate = itemeRate;
        this.TotalCount = totalCount;
        this.TotalAmount = totalAmount;
    }

    public String getSubitemNmae() {
        return SubitemNmae;
    }

    public void setSubitemNmae(String subitemNmae) {
        SubitemNmae = subitemNmae;
    }

    public String getItemeRate() {
        return ItemeRate;
    }

    public void setItemeRate(String itemeRate) {
        ItemeRate = itemeRate;
    }

    public String getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(String totalCount) {
        TotalCount = totalCount;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }
}
