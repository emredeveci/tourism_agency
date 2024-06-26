package business;

import dao.RoomDao;
import entity.Room;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoomManager {

    private final RoomDao roomDao;

    public RoomManager() {
        this.roomDao = new RoomDao();
    }

    //CRITERIA 1
    public boolean delete(int inventoryId) {
        return this.roomDao.delete(inventoryId);
    }

    public boolean save(int hotelId, int roomType, int pensionType, int seasonId, Integer stock, int numberOfBeds, String roomSize, double adultPrice, double childPrice, List<String> selectedRoomFeatures) {
        return this.roomDao.save(hotelId, roomType, pensionType, seasonId, stock, numberOfBeds, roomSize, adultPrice, childPrice, selectedRoomFeatures);
    }

    public Room getByInventoryId(int inventoryId) {
        return this.roomDao.getByInventoryId(inventoryId);
    }

    public List<Room> findAll() {
        return this.roomDao.findAll();
    }

    public List<Object[]> findAllRoomDetails(int inventoryId) {
        return this.roomDao.findAllRoomDetails(inventoryId);
    }

    public List<Object[]> findAllRoomFeatures(int inventoryId) {
        return this.roomDao.findAllRoomFeatures(inventoryId);
    }

    public String[] getRoomTypesForHotel(int inventoryId) {
        return this.roomDao.findRoomTypesForHotel(inventoryId);
    }

    public String[] getPensionTypesForHotel(int inventoryId) {
        return this.roomDao.getPensionTypesForHotel(inventoryId);
    }

    public String[] getSeasonTypesForHotel(int inventoryId) {
        return this.roomDao.getSeasonTypesForHotel(inventoryId);
    }

    public int findHotelIdByInventoryId(int inventoryId) {
        return this.roomDao.findHotelIdByInventoryId(inventoryId);
    }

    public List<Object[]> findAllRoomAmenities(int inventoryId) {
        return this.roomDao.findAllRoomAmenities(inventoryId);
    }

    public Map<Integer, String> getHotelNames() {
        return this.roomDao.getHotelNames();
    }

    public Map<Integer, String> getRoomTypesForMap() {
        return this.roomDao.getRoomTypesForMap();
    }

    public Map<Integer, String> getPensionTypesForMap(int selectedHotelId) {
        return this.roomDao.getPensionTypesForMap(selectedHotelId);
    }

    public Map<Integer, String> getSeasonTypesForMap(int selectedHotelId) {
        return this.roomDao.getSeasonTypesForMap(selectedHotelId);
    }

    public Map<Integer, String> getCityNames(Integer selectedHotelId) {
        return this.roomDao.getCityNames(selectedHotelId);
    }

    public List<Room> getForRoomSearch(String hotelName, String cityName, LocalDate startDate, LocalDate endDate, Integer adultCount, Integer childCount) {
        return this.roomDao.getForRoomSearch(hotelName, cityName, startDate, endDate, adultCount, childCount);
    }

    public List<Object[]> getForTable(int size, List<Room> roomList) {
        List<Object[]> roomObjList = new ArrayList<>();
        for (Room obj : roomList) {
            int i = 0;
            Object[] rowObject = new Object[size];
            rowObject[i++] = obj.getInventory_id();
            rowObject[i++] = obj.getHotel_name();
            rowObject[i++] = obj.getCity();
            rowObject[i++] = obj.getRoom_type();
            rowObject[i++] = obj.getPension_type();
            rowObject[i++] = obj.getStar();
            rowObject[i++] = obj.getDiscount_id();
            rowObject[i++] = obj.getAdult_price();
            rowObject[i++] = obj.getChild_price();
            rowObject[i++] = obj.getQuantity_available();
            roomObjList.add(rowObject);
        }
        return roomObjList;
    }
}
