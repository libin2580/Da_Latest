package com.meridian.dateout.explore.promo;

/**
 * Created by libin on 5/7/2018.
 */

public class PromoModel {
    String coupon_id,coupon_code,coupon_name,cpn_valid_from,cpn_valid_till,deal_id,deal_name,offer;

    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getCoupon_code() {
        return coupon_code;
    }

    public void setCoupon_code(String coupon_code) {
        this.coupon_code = coupon_code;
    }

    public String getCoupon_name() {
        return coupon_name;
    }

    public void setCoupon_name(String coupon_name) {
        this.coupon_name = coupon_name;
    }

    public String getCpn_valid_from() {
        return cpn_valid_from;
    }

    public void setCpn_valid_from(String cpn_valid_from) {
        this.cpn_valid_from = cpn_valid_from;
    }

    public String getCpn_valid_till() {
        return cpn_valid_till;
    }

    public void setCpn_valid_till(String cpn_valid_till) {
        this.cpn_valid_till = cpn_valid_till;
    }

    public String getDeal_id() {
        return deal_id;
    }

    public void setDeal_id(String deal_id) {
        this.deal_id = deal_id;
    }

    public String getDeal_name() {
        return deal_name;
    }

    public void setDeal_name(String deal_name) {
        this.deal_name = deal_name;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }
}
