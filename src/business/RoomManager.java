package business;

import core.Utility;
import dao.RoomDao;
import entity.Hotel;
import entity.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoomManager {

    private final RoomDao roomDao;

    public RoomManager(){
        this.roomDao = new RoomDao();
    }

    public boolean delete(int inventoryId) {
        if (this.getByInventoryId(inventoryId) == null) {
            Utility.showMessage("Room with ID " + inventoryId + " could not be found.");
            return false;
        }
        return this.roomDao.delete(inventoryId);
    }

    public boolean save(int hotelId, int roomType, int pensionType, int seasonId, String stock, int numberOfBeds, String roomSize, double adultPrice, double childPrice, List<String> selectedRoomFeatures){
        return this.roomDao.save(hotelId, roomType, pensionType, seasonId, stock, numberOfBeds, roomSize, adultPrice, childPrice, selectedRoomFeatures);
    }

    public Room getByInventoryId(int inventoryId) {
        return this.roomDao.getByInventoryId(inventoryId);
    }

    public List<Room> findAll(){
        return this.roomDao.findAll();
    }

    public List<Object[]> findAllRoomDetails(int inventoryId) {
        return this.roomDao.findAllRoomDetails(inventoryId);
    }

    public List<Object[]> findAllRoomFeatures(int inventoryId){
        return this.roomDao.findAllRoomFeatures(inventoryId);
    }

    public String[] getRoomTypesForHotel(int inventoryId){
        return this.roomDao.findRoomTypesForHotel(inventoryId);
    }

    public String[] getPensionTypesForHotel(int inventoryId){
        return this.roomDao.getPensionTypesForHotel(inventoryId);
    }

    public String[] getSeasonTypesForHotel(int inventoryId){
        return this.roomDao.getSeasonTypesForHotel(inventoryId);
    }

    public int findHotelIdByInventoryId(int inventoryId){
        return this.roomDao.findHotelIdByInventoryId(inventoryId);
    }

    public Map<Integer, String> getHotelNames(){
        return this.roomDao.getHotelNames();
    }

    public Map<Integer, String> getRoomTypesForMap(int selectedHotelId){
        return this.roomDao.getRoomTypesForMap(selectedHotelId);
    }
    public Map<Integer, String> getPensionTypesForMap(int selectedHotelId){
        return this.roomDao.getPensionTypesForMap(selectedHotelId);
    }
    public Map<Integer, Integer> getSeasonTypesForMap(int selectedHotelId){
        return this.roomDao.getSeasonTypesForMap(selectedHotelId);
    }

    public List<Object[]> getForTable(int size, List<Room> roomList){
        List<Object[]> roomObjList = new ArrayList<>();
        for (Room obj : roomList) {
            int i = 0;
            Object[] rowObject = new Object[size];
            rowObject[i++] = obj.getInventory_id();
            rowObject[i++] = obj.getHotel_name();
            rowObject[i++] = obj.getRoom_type();
            rowObject[i++] = obj.getPension_type();
            rowObject[i++] = obj.getDiscount_id();
            rowObject[i++] = obj.getAdult_price();
            rowObject[i++] = obj.getChild_price();
            rowObject[i++] = obj.getQuantity_available();
            roomObjList.add(rowObject);
        }
        return roomObjList;
    }
}
