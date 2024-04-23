package entity;

import java.util.List;

public class Hotel {
    //CRITERIA 1
    private int hotel_id;
    private String hotel_name;
    private int star_rating;
    private String city;
    private String district;
    private String email;
    private String phone;
    private String address;
    private List<PensionType> pensionTypes;

    public Hotel() {
    }

    public int getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(int hotel_id) {
        this.hotel_id = hotel_id;
    }

    public String getHotel_name() {
        return hotel_name;
    }

    public void setHotel_name(String hotel_name) {
        this.hotel_name = hotel_name;
    }

    public int getStar_rating() {
        return star_rating;
    }

    public void setStar_rating(int star_rating) {
        this.star_rating = star_rating;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<PensionType> getPensionTypes() {
        return pensionTypes;
    }

    public void setPensionTypes(List<PensionType> pensionTypes) {
        this.pensionTypes = pensionTypes;
    }
}
