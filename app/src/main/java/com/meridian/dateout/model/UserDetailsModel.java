package com.meridian.dateout.model;

/**
 * Created by user 1 on 06-04-2016.
 */
public class UserDetailsModel {
    private String email;
    private String fulnam;
    private String occptn;

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    private String pass;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFulnam() {
        return fulnam;
    }

    public void setFulnam(String fulnam) {
        this.fulnam = fulnam;
    }

    public String getOccptn() {
        return occptn;
    }

    public void setOccptn(String occptn) {
        this.occptn = occptn;
    }

    public String getLocatn() {
        return locatn;
    }

    public void setLocatn(String locatn) {
        this.locatn = locatn;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private String locatn;
    private String phone;

}
