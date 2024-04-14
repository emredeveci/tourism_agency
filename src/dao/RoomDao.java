package dao;

import core.DatabaseConnection;
import entity.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDao {

    private final DatabaseConnection databaseConnection;

    public RoomDao(){
        this.databaseConnection = DatabaseConnection.getInstance();
    }

    public List<Room> findAll(){
        List<Room> roomList = new ArrayList<>();

        // Prepare SQL query
        String sql = "SELECT ri.inventory_id, h.hotel_name, pt.pension_type, dp.discount_id, rt.room_type_name, ri.quantity_available, p.price_per_night AS adult_price, p2.price_per_night AS child_price " +
                "FROM room_inventory ri " +
                "JOIN hotels h ON ri.hotel_id = h.hotel_id " +
                "LEFT JOIN hotel_pensions hp ON ri.hotel_id = hp.hotel_id " +
                "LEFT JOIN pension_types pt ON hp.pension_id = pt.pension_id " +
                "LEFT JOIN price p ON ri.inventory_id = p.inventory_id AND p.guest_id = 1 " +
                "LEFT JOIN price p2 ON ri.inventory_id = p2.inventory_id AND p2.guest_id = 2 " +
                "LEFT JOIN discount_periods dp ON p.discount_id = dp.discount_id " +
                "JOIN room_types rt ON ri.room_type_id = rt.room_type_id " +
                "WHERE (p.pension_id IS NULL OR pt.pension_id = p.pension_id) " +
                "AND (p2.pension_id IS NULL OR pt.pension_id = p2.pension_id)";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            // Iterate over the result set
            while (resultSet.next()) {
                // Construct Room object with retrieved information
                Room room = new Room();
                room.setInventory_id(resultSet.getInt("inventory_id"));
                room.setHotel_name(resultSet.getString("hotel_name"));
                room.setPension_type(resultSet.getString("pension_type"));
                room.setDiscount_id(resultSet.getInt("discount_id"));
                room.setRoom_type(resultSet.getString("room_type_name"));
                room.setQuantity_available(resultSet.getInt("quantity_available"));
                room.setAdult_price(resultSet.getDouble("adult_price"));
                room.setChild_price(resultSet.getDouble("child_price"));

                // Add Room object to roomList
                roomList.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any potential database errors
        }

        return roomList;
    }

}
