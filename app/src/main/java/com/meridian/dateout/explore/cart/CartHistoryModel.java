package com.meridian.dateout.explore.cart;

/**
 * Created by Anvin on 10/27/2017.
 */

public class CartHistoryModel {
String title,image,order_status,amount,booking_date,quantity,cart_item_id,deal_id;
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getamount() {
        return amount;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }


    public void setcart_item_id(String cart_item_id) {
        this.cart_item_id = cart_item_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getcart_item_id() {
        return cart_item_id;
    }


    public String getDeal_id() {
        return deal_id;
    }

    public void setDeal_id(String deal_id) {
        this.deal_id = deal_id;
    }
}
