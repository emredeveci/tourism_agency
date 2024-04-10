package entity;

public class DiscountPrice {
    private int discount_price_id;
    private int discount_id;
    private int room_id;
    private double discounted_price_per_night;

    public int getDiscount_price_id() {
        return discount_price_id;
    }

    public void setDiscount_price_id(int discount_price_id) {
        this.discount_price_id = discount_price_id;
    }

    public int getDiscount_id() {
        return discount_id;
    }

    public void setDiscount_id(int discount_id) {
        this.discount_id = discount_id;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public double getDiscounted_price_per_night() {
        return discounted_price_per_night;
    }

    public void setDiscounted_price_per_night(double discounted_price_per_night) {
        this.discounted_price_per_night = discounted_price_per_night;
    }
}
