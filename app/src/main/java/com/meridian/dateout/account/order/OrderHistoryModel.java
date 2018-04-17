package com.meridian.dateout.account.order;

/**
 * Created by Anvin on 10/27/2017.
 */

public class OrderHistoryModel {
String title,image,description,booking_date,order_status,adult_total_price,
    adult_number,child_number,child_total_price,quantity,booking_time,payment_status,amount,points_earned,stripe_token,txn_id,cust_name,cust_email,cust_phone,cust_address;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getAdult_total_price() {
        return adult_total_price;
    }

    public void setAdult_total_price(String adult_total_price) {
        this.adult_total_price = adult_total_price;
    }

    public String getAdult_number() {
        return adult_number;
    }

    public void setAdult_number(String adult_number) {
        this.adult_number = adult_number;
    }

    public String getChild_number() {
        return child_number;
    }

    public void setChild_number(String child_number) {
        this.child_number = child_number;
    }

    public String getChild_total_price() {
        return child_total_price;
    }

    public void setChild_total_price(String child_total_price) {
        this.child_total_price = child_total_price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getBooking_time() {
        return booking_time;
    }

    public void setBooking_time(String booking_time) {
        this.booking_time = booking_time;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPoints_earned() {
        return points_earned;
    }

    public void setPoints_earned(String points_earned) {
        this.points_earned = points_earned;
    }

    public String getStripe_token() {
        return stripe_token;
    }

    public void setStripe_token(String stripe_token) {
        this.stripe_token = stripe_token;
    }

    public String getTxn_id() {
        return txn_id;
    }

    public void setTxn_id(String txn_id) {
        this.txn_id = txn_id;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getCust_email() {
        return cust_email;
    }

    public void setCust_email(String cust_email) {
        this.cust_email = cust_email;
    }

    public String getCust_phone() {
        return cust_phone;
    }

    public void setCust_phone(String cust_phone) {
        this.cust_phone = cust_phone;
    }

    public String getCust_address() {
        return cust_address;
    }

    public void setCust_address(String cust_address) {
        this.cust_address = cust_address;
    }
}
