package entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Reservation {
    private int reservationId;
    private int inventoryId;
    private int hotelId;
    private String hotelName;
    private String cityName;
    private int discountId;
    private int pensionId;
    private int roomTypeId;
    private Integer childCount;
    private Integer adultCount;
    private String roomTypeName;
    private String pensionTypeName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String guestName;
    private String guestPhone;
    private String guestIdentificationNumber;
    private String guestEmail;
    private BigDecimal totalCost;

    public Reservation() {
    }

    public Reservation(int reservationId, int inventoryId, int hotelId, String hotelName, String cityName, int discountId, int pensionId, int roomTypeId, Integer childCount, Integer adultCount, String roomTypeName, String pensionTypeName, LocalDate startDate, LocalDate endDate, String guestName, String guestPhone, String guestIdentificationNumber, String guestEmail, BigDecimal totalCost) {
        this.reservationId = reservationId;
        this.inventoryId = inventoryId;
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.cityName = cityName;
        this.discountId = discountId;
        this.pensionId = pensionId;
        this.roomTypeId = roomTypeId;
        this.childCount = childCount;
        this.adultCount = adultCount;
        this.roomTypeName = roomTypeName;
        this.pensionTypeName = pensionTypeName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.guestName = guestName;
        this.guestPhone = guestPhone;
        this.guestIdentificationNumber = guestIdentificationNumber;
        this.guestEmail = guestEmail;
        this.totalCost = totalCost;
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public int getDiscountId() {
        return discountId;
    }

    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }

    public int getPensionId() {
        return pensionId;
    }

    public void setPensionId(int pensionId) {
        this.pensionId = pensionId;
    }

    public int getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(int roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(Integer childCount) {
        this.childCount = childCount;
    }

    public int getAdultCount() {
        return adultCount;
    }

    public void setAdultCount(Integer adultCount) {
        this.adultCount = adultCount;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getGuestPhone() {
        return guestPhone;
    }

    public void setGuestPhone(String guestPhone) {
        this.guestPhone = guestPhone;
    }

    public String getGuestIdentificationNumber() {
        return guestIdentificationNumber;
    }

    public void setGuestIdentificationNumber(String guestIdentificationNumber) {
        this.guestIdentificationNumber = guestIdentificationNumber;
    }

    public String getGuestEmail() {
        return guestEmail;
    }

    public void setGuestEmail(String guestEmail) {
        this.guestEmail = guestEmail;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

    public String getPensionTypeName() {
        return pensionTypeName;
    }

    public void setPensionTypeName(String pensionTypeName) {
        this.pensionTypeName = pensionTypeName;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    // toString() method
    @Override
    public String toString() {
        return "Reservation{" +
                "inventoryId=" + inventoryId +
                ", hotelId=" + hotelId +
                ", hotelName='" + hotelName + '\'' +
                ", discountId=" + discountId +
                ", pensionId=" + pensionId +
                ", roomTypeId=" + roomTypeId +
                ", childCount=" + childCount +
                ", adultCount=" + adultCount +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", guestName='" + guestName + '\'' +
                ", guestPhone='" + guestPhone + '\'' +
                ", guestIdentificationNumber='" + guestIdentificationNumber + '\'' +
                ", guestEmail='" + guestEmail + '\'' +
                ", totalCost=" + totalCost +
                '}';
    }
}