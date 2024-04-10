package entity;

public class RoomType {
    private int room_type_id;
    private String room_type_name;
    private int number_of_beds;
    private double room_size;

    public int getRoom_type_id() {
        return room_type_id;
    }

    public void setRoom_type_id(int room_type_id) {
        this.room_type_id = room_type_id;
    }

    public String getRoom_type_name() {
        return room_type_name;
    }

    public void setRoom_type_name(String room_type_name) {
        this.room_type_name = room_type_name;
    }

    public int getNumber_of_beds() {
        return number_of_beds;
    }

    public void setNumber_of_beds(int number_of_beds) {
        this.number_of_beds = number_of_beds;
    }

    public double getRoom_size() {
        return room_size;
    }

    public void setRoom_size(double room_size) {
        this.room_size = room_size;
    }
}
