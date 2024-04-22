package business;

import core.Utility;
import dao.ReservationDao;
import entity.Hotel;
import entity.Reservation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReservationManager {
    private final ReservationDao reservationDao;

    public ReservationManager() {
        this.reservationDao = new ReservationDao();
    }

    public List<Reservation> findAll() {
        return this.reservationDao.findAll();
    }

    public Reservation findByReservationId(int reservationId) {
        return this.reservationDao.findByReservationId(reservationId);
    }

    public Integer findInventoryId(int reservationId) {
        return this.reservationDao.findInventoryId(reservationId);
    }

    public List<Object[]> findAllReservationDetails(int reservationId) {
        return this.reservationDao.findAllReservationDetails(reservationId);
    }

    public List<Object[]> findHotelInfo(int inventoryId) {
        return this.reservationDao.findHotelInfo(inventoryId);
    }

    public List<Object[]> findRoomInfo(int inventoryId) {
        return this.reservationDao.findRoomInfo(inventoryId);
    }

    public List<Object[]> findAllGuestDetails(int reservationId) {
        return this.reservationDao.findAllGuestDetails(reservationId);
    }

    public List<Object[]> findAllAmenities(int InventoryId) {
        return this.reservationDao.findAllAmenities(InventoryId);
    }

    public List<Object[]> findAllFeatures(int inventoryId) {
        return this.reservationDao.findAllFeatures(inventoryId);
    }

    public Integer findPensionId(int inventoryId) {
        return this.reservationDao.findPensionId(inventoryId);
    }

    public Integer findDiscountId(int inventoryId) {
        return this.reservationDao.findDiscountId(inventoryId);
    }

    public Integer findRoomTypeId(int inventoryId) {
        return this.reservationDao.findRoomTypeId(inventoryId);
    }

    public boolean save(Integer inventoryId, Integer hotelId, String hotelName, Integer discountId, Integer pensionId, Integer roomTypeId, Integer childCount, Integer adultCount, LocalDate startDate, LocalDate endDate, String guestName, String guestPhone, String guestIdNo, String guestEmail, BigDecimal totalCost) {
        return this.reservationDao.save(inventoryId, hotelId, hotelName, discountId, pensionId, roomTypeId, childCount, adultCount, startDate, endDate, guestName, guestPhone, guestIdNo, guestEmail, totalCost);
    }

    public boolean update(int reservationId, int childCount, int adultCount, LocalDate startDate, LocalDate endDate, String guestName, String guestPhone, String guestIdNo, String guestEmail, BigDecimal totalCost) {
        return this.reservationDao.update(reservationId, childCount, adultCount, startDate, endDate, guestName, guestPhone, guestIdNo, guestEmail, totalCost);
    }

    public boolean deleteReservation(int reservationId) {
        return this.reservationDao.deleteReservation(reservationId);
    }

    public boolean adjustInventoryAfterAdd(Integer inventoryId) {
        return this.reservationDao.adjustInventoryAfterAdd(inventoryId);
    }

    public boolean adjustInventoryAfterRemove(Integer inventoryId) {
        return this.reservationDao.adjustInventoryAfterRemove(inventoryId);
    }

    public BigDecimal findPricePerNight(int inventoryId, int guestId) {
        return this.reservationDao.findPricePerNight(inventoryId, guestId);
    }

    public List<Object[]> getForTable(int size, List<Reservation> reservationList) {
        List<Object[]> reservationObjList = new ArrayList<>();
        for (Reservation obj : reservationList) {
            int i = 0;
            Object[] rowObject = new Object[size];
            rowObject[i++] = obj.getReservationId();
            rowObject[i++] = obj.getHotelName();
            rowObject[i++] = obj.getCityName();
            rowObject[i++] = obj.getStartDate();
            rowObject[i++] = obj.getEndDate();
            rowObject[i++] = obj.getTotalCost();
            reservationObjList.add(rowObject);
        }
        return reservationObjList;
    }
}
