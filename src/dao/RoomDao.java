package dao;

import core.DatabaseConnection;
import entity.Hotel;
import entity.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RoomDao {

    private final DatabaseConnection databaseConnection;

    public RoomDao(){
        this.databaseConnection = DatabaseConnection.getInstance();
    }

    public Room getByInventoryId(int id) {
        Room obj = null;
        String query = "SELECT ri.inventory_id, h.hotel_name, pt.pension_type, dp.discount_id, rt.room_type_name, ri.quantity_available, p.price_per_night AS adult_price, p2.price_per_night AS child_price " +
                "FROM room_inventory ri " +
                "JOIN hotels h ON ri.hotel_id = h.hotel_id " +
                "LEFT JOIN hotel_pensions hp ON ri.hotel_id = hp.hotel_id " +
                "LEFT JOIN pension_types pt ON hp.pension_id = pt.pension_id " +
                "LEFT JOIN price p ON ri.inventory_id = p.inventory_id AND p.guest_id = 1 " +
                "LEFT JOIN price p2 ON ri.inventory_id = p2.inventory_id AND p2.guest_id = 2 " +
                "LEFT JOIN discount_periods dp ON p.discount_id = dp.discount_id " +
                "JOIN room_types rt ON ri.room_type_id = rt.room_type_id " +
                "WHERE (p.pension_id IS NULL OR pt.pension_id = p.pension_id) " +
                "AND (p2.pension_id IS NULL OR pt.pension_id = p2.pension_id) " +
                "AND ri.inventory_id = ?";
        try {
            PreparedStatement pr = this.databaseConnection.getConnection().prepareStatement(query);
            pr.setInt(1, id);
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                obj = this.match(rs);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return obj;
    }

    public List<Room> findAll(){
        List<Room> roomList = new ArrayList<>();

        // Prepare SQL query
        String query = "SELECT ri.inventory_id, h.hotel_name, pt.pension_type, dp.discount_id, rt.room_type_name, ri.quantity_available, p.price_per_night AS adult_price, p2.price_per_night AS child_price " +
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
             PreparedStatement statement = connection.prepareStatement(query);
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

    public boolean delete(int inventoryId) {
        String query = "DELETE FROM public.room_inventory WHERE inventory_id = ?";
        try {
            PreparedStatement pr = databaseConnection.getConnection().prepareStatement(query);
            pr.setInt(1, inventoryId);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public List<Object[]> findAllRoomDetails(int inventoryId){
        List<Object[]> roomDetailsData = new ArrayList<>();

        String query = "SELECT inventory_id, bed_count, room_size " +
                "FROM room_inventory " +
                "WHERE inventory_id = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, inventoryId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int inventory = resultSet.getInt("inventory_id");
                int bedCount = resultSet.getInt("bed_count");
                String roomSize = resultSet.getString("room_size");
                Object[] rowData = {inventory, bedCount, roomSize};
                roomDetailsData.add(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roomDetailsData;
    }

    public List<Object[]> findAllRoomFeatures(int inventoryId){
        List<Object[]> roomFeaturesData = new ArrayList<>();
        String query = "SELECT hrf.inventory_id, rft.feature_type " +
                "FROM hotel_room_features hrf " +
                "JOIN room_feature_types rft ON hrf.feature_type_id = rft.room_feature_id " +
                "WHERE hrf.inventory_id = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, inventoryId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int inventory = resultSet.getInt("inventory_id");
                String featureType = resultSet.getString("feature_type");
                Object[] rowData = {inventory, featureType};
                roomFeaturesData.add(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roomFeaturesData;
    }

    public String[] findRoomTypesForHotel(int inventoryId){
        List<String> roomTypes = new ArrayList<>();

        int hotelId = findHotelIdByInventoryId(inventoryId);

        String query = "SELECT DISTINCT rt.room_type_name " +
                "FROM room_inventory ri " +
                "JOIN room_types rt ON ri.room_type_id = rt.room_type_id " +
                "WHERE ri.hotel_id = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, hotelId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String roomType = resultSet.getString("room_type_name");
                roomTypes.add(roomType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String[] roomTypesArray = new String[roomTypes.size()];
        roomTypesArray = roomTypes.toArray(roomTypesArray);
        System.out.println(Arrays.toString(roomTypesArray));

        return roomTypesArray;
    }

    public String[] getPensionTypesForHotel(int inventoryId){
        List<String> pensionTypes = new ArrayList<>();
        int hotelId = findHotelIdByInventoryId(inventoryId);

        String query = "SELECT pt.pension_type " +
                "FROM hotel_pensions hp " +
                "JOIN pension_types pt ON hp.pension_id = pt.pension_id " +
                "WHERE hp.hotel_id = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, hotelId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String roomType = resultSet.getString("pension_type");
                pensionTypes.add(roomType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String[] pensionTypesArray = new String[pensionTypes.size()];
        pensionTypesArray = pensionTypes.toArray(pensionTypesArray);
        System.out.println(Arrays.toString(pensionTypesArray));

        return pensionTypesArray;
    }

    public String[] getSeasonTypesForHotel(int inventoryId){
        List<String> seasonTypes = new ArrayList<>();
        int hotelId = findHotelIdByInventoryId(inventoryId);

        String query = "SELECT dp.discount_id " +
                "FROM discount_periods dp " +
                "WHERE dp.hotel_id = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, hotelId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String roomType = resultSet.getString("discount_id");
                seasonTypes.add(roomType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String[] seasonTypesArray = new String[seasonTypes.size()];
        seasonTypesArray = seasonTypes.toArray(seasonTypesArray);
        System.out.println(Arrays.toString(seasonTypesArray));

        return seasonTypesArray;

    }

    public int findHotelIdByInventoryId(int inventoryId) {
        int hotelId = -1;

        String query = "SELECT hotel_id FROM room_inventory WHERE inventory_id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, inventoryId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                hotelId = resultSet.getInt("hotel_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hotelId;
    }



    public Room match(ResultSet rs) throws SQLException {
        Room room = new Room();
        room.setInventory_id(rs.getInt("inventory_id"));
        room.setHotel_name(rs.getString("hotel_name"));
        room.setPension_type(rs.getString("pension_type"));
        room.setDiscount_id(rs.getInt("discount_id"));
        room.setRoom_type(rs.getString("room_type_name"));
        room.setQuantity_available(rs.getInt("quantity_available"));
        room.setAdult_price(rs.getDouble("adult_price"));
        room.setChild_price(rs.getDouble("child_price"));
        return room;
    }
}
