package com.meridian.dateout.explore.deliveryaddress;

/**
 * Created by libin on 3/17/2018.
 */

public class AddressModel {
    public String getAddress() {
        return address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFlat_address() {
        return flat_address;
    }

    public void setFlat_address(String flat_address) {
        this.flat_address = flat_address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean getSelected() {
        return isSelected;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getArea() {
        return area;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getState() {
        return state;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getWork_home() {
        return work_home;
    }

    public void setWork_home(String work_home) {
        this.work_home = work_home;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    String address,id,name,email,flat_address,city,area,state,pin,work_home,phone;
    private boolean isSelected;

}
