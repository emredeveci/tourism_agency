package business;

import dao.ReservationDao;
import entity.Hotel;
import entity.Reservation;

import java.util.ArrayList;
import java.util.List;

public class ReservationManager {
    private final ReservationDao reservationDao;

    public ReservationManager() {
        this.reservationDao = new ReservationDao();
    }

    public List<Reservation> findAll(){
        return this.reservationDao.findAll();
    }

    public List<Object[]> findAllReservationDetails(int reservationId) {
        return this.reservationDao.findAllReservationDetails(reservationId);
    }

    public List<Object[]> findAllGuestDetails(int reservationId) {
        return this.reservationDao.findAllGuestDetails(reservationId);
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
