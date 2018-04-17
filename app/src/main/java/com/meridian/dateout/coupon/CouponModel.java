package com.meridian.dateout.coupon;

/**
 * Created by rishika on 10/10/2017.
 */

public class CouponModel {
    String id,cpn_code,cpn_image,cpn_strt_date,cpn_exp_date,cpn_discount,cpn_discount_unit,cpn_desc,expire_in,category;

    public String getCpn_code() {
        return cpn_code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCpn_code(String cpn_code) {
        this.cpn_code = cpn_code;
    }

    public String getCpn_image() {
        return cpn_image;
    }

    public void setCpn_image(String cpn_image) {
        this.cpn_image = cpn_image;
    }

    public String getCpn_strt_date() {
        return cpn_strt_date;
    }

    public void setCpn_strt_date(String cpn_strt_date) {
        this.cpn_strt_date = cpn_strt_date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCpn_exp_date() {
        return cpn_exp_date;
    }

    public void setCpn_exp_date(String cpn_exp_date) {
        this.cpn_exp_date = cpn_exp_date;
    }

    public String getCpn_discount() {
        return cpn_discount;
    }

    public void setCpn_discount(String cpn_discount) {
        this.cpn_discount = cpn_discount;
    }

    public String getCpn_discount_unit() {
        return cpn_discount_unit;
    }

    public void setCpn_discount_unit(String cpn_discount_unit) {
        this.cpn_discount_unit = cpn_discount_unit;
    }

    public String getCpn_desc() {
        return cpn_desc;
    }

    public void setCpn_desc(String cpn_desc) {
        this.cpn_desc = cpn_desc;
    }

    public String getExpire_in() {
        return expire_in;
    }

    public void setExpire_in(String expire_in) {
        this.expire_in = expire_in;
    }
}
