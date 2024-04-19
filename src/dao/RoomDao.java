package dao;

import core.DatabaseConnection;
import entity.Hotel;
import entity.Room;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.Date;

public class RoomDao {

    private final DatabaseConnection databaseConnection;

    public RoomDao() {
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

    public List<Room> findAll() {
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

    public boolean save(int hotelId, int roomType, int pensionType, int seasonId, Integer stock, int numberOfBeds, String roomSize, double adultPrice, double childPrice, List<String> selectedRoomFeatures) {
        String query = "INSERT INTO public.room_inventory " +
                "(hotel_id, quantity_available, room_type_id, room_size, bed_count) " +
                "VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pr = this.databaseConnection.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pr.setInt(1, hotelId);
            pr.setInt(2, stock);
            pr.setInt(3, roomType);
            pr.setString(4, roomSize);
            pr.setInt(5, numberOfBeds);
            pr.executeUpdate();

            ResultSet generatedKeys = pr.getGeneratedKeys();
            int inventoryId = -1;
            if (generatedKeys.next()) {
                inventoryId = generatedKeys.getInt(1);
            } else {
                // Handle failure to get generated keys
                throw new SQLException("Failed to retrieve generated Inventory ID.");
            }

            for (String feature : selectedRoomFeatures) {
                String featureQuery = "INSERT INTO hotel_room_features (inventory_id, feature_type_id) VALUES (?, ?)";
                PreparedStatement featureStatement = this.databaseConnection.getConnection().prepareStatement(featureQuery);
                featureStatement.setInt(1, inventoryId);
                featureStatement.setInt(2, getFeatureByName(feature));
                featureStatement.executeUpdate();
            }

            String adultPriceQuery = "INSERT INTO public.price " +
                    "(hotel_id, room_type_id, pension_id, price_per_night, discount_id, guest_id, inventory_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement adultPriceStatement = this.databaseConnection.getConnection().prepareStatement(adultPriceQuery);
            adultPriceStatement.setInt(1, hotelId);
            adultPriceStatement.setInt(2, roomType);
            adultPriceStatement.setInt(3, pensionType);
            adultPriceStatement.setDouble(4, adultPrice);
            adultPriceStatement.setInt(5, seasonId);
            adultPriceStatement.setInt(6, 1);
            adultPriceStatement.setInt(7, inventoryId);
            adultPriceStatement.executeUpdate();

            String childPriceQuery = "INSERT INTO public.price " +
                    "(hotel_id, room_type_id, pension_id, price_per_night, discount_id, guest_id, inventory_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement childPriceStatement = this.databaseConnection.getConnection().prepareStatement(childPriceQuery);
            childPriceStatement.setInt(1, hotelId);
            childPriceStatement.setInt(2, roomType);
            childPriceStatement.setInt(3, pensionType);
            childPriceStatement.setDouble(4, childPrice);
            childPriceStatement.setInt(5, seasonId);
            childPriceStatement.setInt(6, 2);
            childPriceStatement.setInt(7, inventoryId);
            childPriceStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false; // Return false if an exception occurs
        }
        return true;
    }

    public List<Object[]> findAllRoomDetails(int inventoryId) {
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

    public List<Object[]> findAllRoomFeatures(int inventoryId) {
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

    public String[] findRoomTypesForHotel(int selectedHotelId) {
        List<String> roomTypes = new ArrayList<>();

        String query = "SELECT DISTINCT rt.room_type_name " +
                "FROM room_inventory ri " +
                "JOIN room_types rt ON ri.room_type_id = rt.room_type_id " +
                "WHERE ri.hotel_id = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, selectedHotelId);
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

    public String[] getPensionTypesForHotel(int selectedHotelId) {
        List<String> pensionTypes = new ArrayList<>();

        String query = "SELECT pt.pension_type " +
                "FROM hotel_pensions hp " +
                "JOIN pension_types pt ON hp.pension_id = pt.pension_id " +
                "WHERE hp.hotel_id = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, selectedHotelId);
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

    public String[] getSeasonTypesForHotel(int selectedHotelId) {
        List<String> seasonTypes = new ArrayList<>();

        String query = "SELECT dp.discount_id " +
                "FROM discount_periods dp " +
                "WHERE dp.hotel_id = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, selectedHotelId);
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

//    public String[] getSeasonTypesForHotel(int selectedHotelId){
//        List<String> seasonTypes = new ArrayList<>();
//        int hotelId = findHotelIdByInventoryId(inventoryId);
//
//        String query = "SELECT dp.discount_id " +
//                "FROM discount_periods dp " +
//                "WHERE dp.hotel_id = ?";
//
//        try (Connection connection = databaseConnection.getConnection();
//             PreparedStatement statement = connection.prepareStatement(query)) {
//            statement.setInt(1, hotelId);
//            ResultSet resultSet = statement.executeQuery();
//
//            while (resultSet.next()) {
//                String roomType = resultSet.getString("discount_id");
//                seasonTypes.add(roomType);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        String[] seasonTypesArray = new String[seasonTypes.size()];
//        seasonTypesArray = seasonTypes.toArray(seasonTypesArray);
//        System.out.println(Arrays.toString(seasonTypesArray));
//
//        return seasonTypesArray;
//
//    }

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

    public Map<Integer, String> getRoomTypesForMap() {
        Map<Integer, String> roomMap = new HashMap<>();
        String query = "SELECT room_type_id, room_type_name FROM room_types";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int roomId = resultSet.getInt("room_type_id");
                String roomName = resultSet.getString("room_type_name");
                roomMap.put(roomId, roomName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roomMap;
    }

    public Map<Integer, String> getPensionTypesForMap(int selectedHotelId) {
        Map<Integer, String> pensionMap = new HashMap<>();
        String query = "SELECT pt.pension_id, pt.pension_type " +
                "FROM hotel_pensions hp " +
                "JOIN pension_types pt ON hp.pension_id = pt.pension_id " +
                "WHERE hp.hotel_id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, selectedHotelId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int pensionId = resultSet.getInt("pension_id");
                String pensionType = resultSet.getString("pension_type");
                pensionMap.put(pensionId, pensionType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pensionMap;
    }

    public Map<Integer, String> getSeasonTypesForMap(int selectedHotelId) {
        Map<Integer, String> seasonMap = new HashMap<>();
        String query = "SELECT discount_id, start_date, end_date " +
                "FROM discount_periods " +
                "WHERE hotel_id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, selectedHotelId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int discountId = resultSet.getInt("discount_id");
                String discountString = "From: " + resultSet.getDate("start_date").toLocalDate().toString() + "  ||  To: " + resultSet.getDate("end_date").toLocalDate().toString();
//                int discountNumber = resultSet.getInt("discount_id");
                seasonMap.put(discountId, discountString);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seasonMap;
    }

    public Map<Integer, String> getHotelNames() {
        Map<Integer, String> hotelMap = new HashMap<>();
        String query = "SELECT hotel_id, hotel_name FROM hotels";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int hotelId = resultSet.getInt("hotel_id");
                String hotelName = resultSet.getString("hotel_name");
                hotelMap.put(hotelId, hotelName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hotelMap;
    }

    public int getFeatureByName(String featureName) {
        int featureId = -1;
        String query = "SELECT room_feature_id FROM room_feature_types WHERE feature_type = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, featureName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                featureId = resultSet.getInt("room_feature_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return featureId;
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
