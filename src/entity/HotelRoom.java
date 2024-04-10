package entity;

public class HotelRoom {
    private int room_id;
    private int hotel_id;
    private int room_type_id;
    private String room_number;
    private double price_per_night_regular;

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public int getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(int hotel_id) {
        this.hotel_id = hotel_id;
    }

    public int getRoom_type_id() {
        return room_type_id;
    }

    public void setRoom_type_id(int room_type_id) {
        this.room_type_id = room_type_id;
    }

    public String getRoom_number() {
        return room_number;
    }

    public void setRoom_number(String room_number) {
        this.room_number = room_number;
    }

    public double getPrice_per_night_regular() {
        return price_per_night_regular;
    }

    public void setPrice_per_night_regular(double price_per_night_regular) {
        this.price_per_night_regular = price_per_night_regular;
    }
}
