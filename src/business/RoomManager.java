package business;

import dao.RoomDao;
import entity.Room;

import java.util.List;

public class RoomManager {

    private final RoomDao roomDao;

    public RoomManager(){
        this.roomDao = new RoomDao();
    }

    public List<Room> findAll(){
        return this.roomDao.findAll();
    }
}
