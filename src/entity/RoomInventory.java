package entity;

public class RoomInventory {
    private int inventory_id;
    private int hotel_id;
    private int room_id;
    private int quantity_available;

    public int getInventory_id() {
        return inventory_id;
    }

    public void setInventory_id(int inventory_id) {
        this.inventory_id = inventory_id;
    }

    public int getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(int hotel_id) {
        this.hotel_id = hotel_id;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public int getQuantity_available() {
        return quantity_available;
    }

    public void setQuantity_available(int quantity_available) {
        this.quantity_available = quantity_available;
    }
}
