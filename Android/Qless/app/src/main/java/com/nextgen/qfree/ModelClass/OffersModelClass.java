package com.nextgen.qfree.ModelClass;

public class OffersModelClass {

    String id;
    String productName;
    String price;
    String offerPrice;

    public OffersModelClass(String id,String productName,String price,String offerPrice){
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.offerPrice = offerPrice;
    }

    public String getId(){
        return id;
    }

    public String getProductName(){
        return productName;
    }

    public  String getPrice(){
        return price;
    }

    public  String getOfferPrice(){
        return offerPrice;
    }

}
