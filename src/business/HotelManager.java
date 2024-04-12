package business;

import dao.HotelDao;
import entity.Hotel;
import entity.User;

import java.util.ArrayList;
import java.util.List;

public class HotelManager {

    private final HotelDao hotelDao;

    public HotelManager() {
        this.hotelDao = new HotelDao();
    }

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
