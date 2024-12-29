package com.nextgen.qfree.ModelClass;

public class HistoryModelClass {

    String id;
    String productName;
    String price;
    String quantity;
    String date;

    public HistoryModelClass(String id,String productName,String price,String quantity,String date){
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.date = date;
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

    public String getQuantity(){
        return quantity;
    }

    public  String getDate(){
        return date;
    }
}
