package com.meridian.dateout.model;

import java.util.ArrayList;

/**
 * Created by user 1 on 03-11-2016.
 */

public class CategoryDealModel {
    private String id;
    private String title;
    private String image;
    private ArrayList<String> array_image;
    private String description;
    private String discount;
    private String inclusion;

    public String getDeal_slug() {
        return deal_slug;
    }

    public void setDeal_slug(String deal_slug) {
        this.deal_slug = deal_slug;
    }

    private String exclusion,deal_slug;

    public String getDeal_type() {
        return deal_type;
    }

    public void setDeal_type(String deal_type) {
        this.deal_type = deal_type;
    }

    private String deal_type;

    public String getInclusion() {
        return inclusion;
    }

    public void setInclusion(String inclusion) {
        this.inclusion = inclusion;
    }

    public String getExclusion() {
        return exclusion;
    }

    public void setExclusion(String exclusion) {
        this.exclusion = exclusion;
    }

    public String getWishlist() {
        return wishlist;
    }

    public void setWishlist(String wishlist) {
        this.wishlist = wishlist;
    }

    private String timing,comment_option;
    private String delivery;
    private String category;
    private String tags;
    private String seller_id;
    private String wishlist;
    private String highlights, address, latitude, logitude, need_toknow, noteon_tickets, currency, price, adult_age_range, adult_tkt_price, adult_discount_tkt_price, child_age_range, child_tkt_price, child_discount_tkt_price;

    public String getHighlights() {
        return highlights;
    }

    public void setHighlights(String highlights) {
        this.highlights = highlights;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLogitude() {
        return logitude;
    }

    public void setLogitude(String logitude) {
        this.logitude = logitude;
    }

    public String getNeed_toknow() {
        return need_toknow;
    }

    public void setNeed_toknow(String need_toknow) {
        this.need_toknow = need_toknow;
    }

    public String getNoteon_tickets() {
        return noteon_tickets;
    }

    public void setNoteon_tickets(String noteon_tickets) {
        this.noteon_tickets = noteon_tickets;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAdult_age_range() {
        return adult_age_range;
    }

    public void setAdult_age_range(String adult_age_range) {
        this.adult_age_range = adult_age_range;
    }

    public String getAdult_tkt_price() {
        return adult_tkt_price;
    }

    public void setAdult_tkt_price(String adult_tkt_price) {
        this.adult_tkt_price = adult_tkt_price;
    }

    public String getAdult_discount_tkt_price() {
        return adult_discount_tkt_price;
    }

    public void setAdult_discount_tkt_price(String adult_discount_tkt_price) {
        this.adult_discount_tkt_price = adult_discount_tkt_price;
    }

    public String getChild_age_range() {
        return child_age_range;
    }

    public void setChild_age_range(String child_age_range) {
        this.child_age_range = child_age_range;
    }

    public String getChild_tkt_price() {
        return child_tkt_price;
    }

    public void setChild_tkt_price(String child_tkt_price) {
        this.child_tkt_price = child_tkt_price;
    }

    public String getChild_discount_tkt_price() {
        return child_discount_tkt_price;
    }

    public void setChild_discount_tkt_price(String child_discount_tkt_price) {
        this.child_discount_tkt_price = child_discount_tkt_price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<String> getArray_image() {
        return array_image;
    }

    public void setArray_image(ArrayList<String> array_image) {
        this.array_image = array_image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public void setcomment_option(String comment_option) {
        this.comment_option = comment_option;
    }
}
