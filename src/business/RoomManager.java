package business;

import core.Utility;
import dao.RoomDao;
import entity.Hotel;
import entity.Room;

import java.util.ArrayList;
import java.util.List;

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
