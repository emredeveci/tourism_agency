package business;

import core.Utility;
import dao.HotelDao;
import entity.Hotel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HotelManager {

    private final HotelDao hotelDao;

    public HotelManager() {
        this.hotelDao = new HotelDao();
    }

    //CRITERIA 1
    public List<Hotel> findAll() {
        return this.hotelDao.findAll();
    }

    public List<Object[]> findAllPensions(int id) {
        return this.hotelDao.findAllPensions(id);
    }

    public List<Object[]> findAllAmenities(int id) {
        return this.hotelDao.findAllAmenities(id);
    }

    public List<Object[]> findDiscountPeriods(int id) {
        return this.hotelDao.findDiscountPeriods(id);
    }

    public List<LocalDate[]> findEnteredSeasons(int id) {
        return this.hotelDao.findEnteredSeasons(id);
    }

    public Hotel getById(int id) {
        return this.hotelDao.getById(id);
    }

    public boolean save(Hotel hotel, List<String> selectedAmenities, List<String> selectedPensions, List<Date[]> enteredSeasons) {
        if (this.getById(hotel.getHotel_id()) != null) {
            //CRITERIA 25
            Utility.showMessage("error");
            return false;
        }
        return this.hotelDao.save(hotel, selectedAmenities, selectedPensions, enteredSeasons);
    }

    public boolean update(Hotel hotel, List<String> selectedAmenities, List<String> selectedPensions, List<Date[]> enteredSeasons) {
        if (this.getById(hotel.getHotel_id()) == null) {
            //CRITERIA 25
            Utility.showMessage("Model with " + hotel.getHotel_id() + " could not be found.");
            return false;
        }
        return this.hotelDao.update(hotel, selectedAmenities, selectedPensions, enteredSeasons);
    }

    public boolean delete(int id) {
        if (this.getById(id) == null) {
            //CRITERIA 25
            Utility.showMessage("Hotel with ID " + id + " could not be found.");
            return false;
        }
        return this.hotelDao.delete(id);
    }

    public List<Object[]> getForTable(int size, List<Hotel> hotelList) {
        List<Object[]> hotelObjList = new ArrayList<>();
        for (Hotel obj : hotelList) {
            int i = 0;
            Object[] rowObject = new Object[size];
            rowObject[i++] = obj.getHotel_id();
            rowObject[i++] = obj.getHotel_name();
            rowObject[i++] = obj.getCity();
            rowObject[i++] = obj.getDistrict();
            rowObject[i++] = obj.getStar_rating();
            rowObject[i++] = obj.getEmail();
            rowObject[i++] = obj.getPhone();
            rowObject[i++] = obj.getAddress();
            hotelObjList.add(rowObject);
        }
        return hotelObjList;
    }
}
