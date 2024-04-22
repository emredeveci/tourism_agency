package dao;

import core.DatabaseConnection;
import entity.Hotel;
import entity.Reservation;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationDao {

    private final DatabaseConnection databaseConnection;

    public ReservationDao() {
        this.databaseConnection = DatabaseConnection.getInstance();
    }

    public List<Reservation> findAll() {
        List<Reservation> reservationList = new ArrayList<>();
        String query = "SELECT r.reservation_id, r.inventory_id, r.hotel_id, h.hotel_name, h.city, r.discount_id, r.pension_id, pt.pension_type, r.room_type_id, rt.room_type_name, r.child_count, r.adult_count, r.start_date, r.end_date, r.guest_name, r.guest_phone, r.guest_identification_number, r.guest_email, r.total_cost " +
                "FROM public.reservations r " +
                "JOIN public.hotels h ON r.hotel_id = h.hotel_id " +
                "LEFT JOIN public.pension_types pt ON r.pension_id = pt.pension_id " +
                "LEFT JOIN public.room_types rt ON r.room_type_id = rt.room_type_id";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                reservationList.add(match(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservationList;
    }

    public Reservation findByReservationId(int reservationId) {
        Reservation obj = null;

        String query = "SELECT r.reservation_id, r.inventory_id, r.hotel_id, h.hotel_name, h.city, r.discount_id, r.pension_id, pt.pension_type, r.room_type_id, rt.room_type_name, r.child_count, r.adult_count, r.start_date, r.end_date, r.guest_name, r.guest_phone, r.guest_identification_number, r.guest_email, r.total_cost " +
                "FROM public.reservations r " +
                "JOIN public.hotels h ON r.hotel_id = h.hotel_id " +
                "LEFT JOIN public.pension_types pt ON r.pension_id = pt.pension_id " +
                "LEFT JOIN public.room_types rt ON r.room_type_id = rt.room_type_id " +
                "WHERE r.reservation_id = ?";
        try {
            PreparedStatement pr = this.databaseConnection.getConnection().prepareStatement(query);
            pr.setInt(1, reservationId);
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                obj = this.match(rs);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return obj;
    }

    public Integer findInventoryId(int reservationId) {
        Integer inventoryId = null;

        String query = "SELECT inventory_id FROM Reservations WHERE reservation_id = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, reservationId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                inventoryId = resultSet.getInt("inventory_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inventoryId;
    }

    public boolean deleteReservation(int reservationId) {
        String query = "DELETE FROM Reservations WHERE reservation_id = ?";
        try {
            PreparedStatement pr = databaseConnection.getConnection().prepareStatement(query);
            pr.setInt(1, reservationId);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Object[]> findAllReservationDetails(int reservationId) {
        List<Object[]> detailsData = new ArrayList<>();
        String query = "SELECT r.reservation_id, r.inventory_id, r.hotel_id, h.hotel_name, h.city, r.discount_id, r.pension_id, pt.pension_type, r.room_type_id, rt.room_type_name, r.child_count, r.adult_count, r.start_date, r.end_date, r.guest_name, r.guest_phone, r.guest_identification_number, r.guest_email, r.total_cost " +
                "FROM public.reservations r " +
                "JOIN public.hotels h ON r.hotel_id = h.hotel_id " +
                "LEFT JOIN public.pension_types pt ON r.pension_id = pt.pension_id " +
                "LEFT JOIN public.room_types rt ON r.room_type_id = rt.room_type_id " +
                "WHERE r.reservation_id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, reservationId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String pensionType = resultSet.getString("pension_type");
                String roomType = resultSet.getString("room_type_name");
                Integer adultCount = (resultSet.getObject("adult_count") != null) ? resultSet.getInt("adult_count") : 0;
                Integer childCount = (resultSet.getObject("child_count") != null) ? resultSet.getInt("child_count") : 0;
                Object[] rowData = {pensionType, roomType, adultCount, childCount};
                detailsData.add(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return detailsData;
    }

    public List<Object[]> findAllGuestDetails(int reservationId) {
        List<Object[]> guestData = new ArrayList<>();
        String query = "SELECT r.reservation_id, r.inventory_id, r.hotel_id, h.hotel_name, h.city, r.discount_id, r.pension_id, pt.pension_type, r.room_type_id, rt.room_type_name, r.child_count, r.adult_count, r.start_date, r.end_date, r.guest_name, r.guest_phone, r.guest_identification_number, r.guest_email, r.total_cost " +
                "FROM public.reservations r " +
                "JOIN public.hotels h ON r.hotel_id = h.hotel_id " +
                "LEFT JOIN public.pension_types pt ON r.pension_id = pt.pension_id " +
                "LEFT JOIN public.room_types rt ON r.room_type_id = rt.room_type_id " +
                "WHERE r.reservation_id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, reservationId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String guestName = resultSet.getString("guest_name");
                String guestIdentificationNumber = resultSet.getString("guest_identification_number");
                String guestPhone = resultSet.getString("guest_phone");
                String guestEmail = resultSet.getString("guest_email");

                Object[] rowData = {guestName, guestIdentificationNumber, guestPhone, guestEmail};
                guestData.add(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return guestData;
    }

    public List<Object[]> findAllAmenities(int inventoryId) {
        List<Object[]> amenityData = new ArrayList<>();
        String query = "SELECT ri.inventory_id, a.amenity_name\n" +
                "FROM room_inventory ri\n" +
                "JOIN hotels h ON ri.hotel_id = h.hotel_id\n" +
                "JOIN hotel_amenities ha ON h.hotel_id = ha.hotel_id\n" +
                "JOIN amenities a ON ha.amenity_id = a.amenity_id\n" +
                "WHERE ri.inventory_id = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, inventoryId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int resultInventoryId = resultSet.getInt("inventory_id");
                String amenityName = resultSet.getString("amenity_name");
                Object[] rowData = {resultInventoryId, amenityName};
                amenityData.add(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return amenityData;
    }

    public List<Object[]> findAllFeatures(int inventoryId) {
        List<Object[]> featureData = new ArrayList<>();
        String query = "SELECT ri.inventory_id, rft.feature_type\n" +
                "FROM room_inventory ri\n" +
                "JOIN hotel_room_features hrf ON ri.inventory_id = hrf.inventory_id\n" +
                "JOIN room_feature_types rft ON hrf.feature_type_id = rft.room_feature_id\n" +
                "WHERE ri.inventory_id = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, inventoryId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int resultInventoryId = resultSet.getInt("inventory_id");
                String featureName = resultSet.getString("feature_type");
                Object[] rowData = {resultInventoryId, featureName};
                featureData.add(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return featureData;
    }

    public List<Object[]> findHotelInfo(int inventoryId) {
        List<Object[]> hotelInfoList = new ArrayList<>();
        String query = "SELECT h.hotel_id, h.hotel_name, h.city, h.district, h.star_rating, h.address\n" +
                "FROM room_inventory ri\n" +
                "JOIN hotels h ON ri.hotel_id = h.hotel_id\n" +
                "WHERE ri.inventory_id = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, inventoryId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int hotelId = resultSet.getInt("hotel_id");
                String hotelName = resultSet.getString("hotel_name");
                String city = resultSet.getString("city");
                String district = resultSet.getString("district");
                int stars = resultSet.getInt("star_rating");
                String address = resultSet.getString("address");
                Object[] rowData = {hotelId, hotelName, city, district, stars, address};
                hotelInfoList.add(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hotelInfoList;
    }

    public List<Object[]> findRoomInfo(int inventoryId) {
        List<Object[]> roomInfoList = new ArrayList<>();
        String query = "SELECT ri.inventory_id, rt.room_type_name, ri.bed_count, ri.room_size, pt.pension_type " +
                "FROM room_inventory ri " +
                "JOIN room_types rt ON ri.room_type_id = rt.room_type_id " +
                "LEFT JOIN (SELECT p.inventory_id, MIN(pt.pension_type) AS pension_type " +
                "FROM price p " +
                "JOIN pension_types pt ON p.pension_id = pt.pension_id " +
                "GROUP BY p.inventory_id) AS pt ON ri.inventory_id = pt.inventory_id " +
                "WHERE ri.inventory_id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, inventoryId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int resultInventoryId = resultSet.getInt("inventory_id");
                    String roomTypeName = resultSet.getString("room_type_name");
                    int bedCount = resultSet.getInt("bed_count");
                    String roomSize = resultSet.getString("room_size");
                    String pensionType = resultSet.getString("pension_type");
                    Object[] rowData = {resultInventoryId, roomTypeName, bedCount, roomSize, pensionType};
                    roomInfoList.add(rowData);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roomInfoList;
    }

    public Integer findPensionId(int inventoryId) {
        Integer pensionId = null;
        String query = "SELECT DISTINCT p.pension_id " +
                "FROM price p " +
                "WHERE p.inventory_id = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, inventoryId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                pensionId = resultSet.getInt("pension_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pensionId;
    }

    public Integer findDiscountId(int inventoryId) {
        Integer discountId = null;
        String query = "SELECT DISTINCT p.discount_id " +
                "FROM price p " +
                "WHERE p.inventory_id = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, inventoryId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                discountId = resultSet.getInt("discount_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return discountId;
    }

    public Integer findRoomTypeId(int inventoryId) {
        Integer roomTypeId = null;
        String query = "SELECT DISTINCT p.room_type_id " +
                "FROM price p " +
                "WHERE p.inventory_id = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, inventoryId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                roomTypeId = resultSet.getInt("room_type_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roomTypeId;
    }

    public Boolean adjustInventoryAfterAdd(int inventoryId) {
        String sql = "UPDATE room_inventory SET quantity_available = quantity_available - 1 WHERE inventory_id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, inventoryId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean adjustInventoryAfterRemove(int inventoryId) {
        String sql = "UPDATE room_inventory SET quantity_available = quantity_available + 1 WHERE inventory_id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, inventoryId);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public BigDecimal findPricePerNight(int inventoryId, int guestId) {
        BigDecimal pricePerNight = null;
        String query = "SELECT price_per_night FROM price WHERE inventory_id = ? AND guest_id = ? LIMIT 1";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, inventoryId);
            statement.setInt(2, guestId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    pricePerNight = resultSet.getBigDecimal("price_per_night");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pricePerNight;
    }

    public boolean save(Integer inventoryId, Integer hotelId, String hotelName, Integer discountId, Integer pensionId, Integer roomTypeId, Integer childCount, Integer adultCount, LocalDate startDate, LocalDate endDate, String guestName, String guestPhone, String guestIdNo, String guestEmail, BigDecimal totalCost) {
        String query = "INSERT INTO public.reservations " +
                "(inventory_id, hotel_id, hotel_name, discount_id, pension_id, room_type_id, child_count, adult_count, start_date, end_date, guest_name, guest_phone, guest_identification_number, guest_email, total_cost) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement pr = this.databaseConnection.getConnection().prepareStatement(query);
            pr.setInt(1, inventoryId);
            pr.setInt(2, hotelId);
            pr.setString(3, hotelName);
            pr.setInt(4, discountId);
            pr.setInt(5, pensionId);
            pr.setInt(6, roomTypeId);
            pr.setInt(7, childCount);
            pr.setInt(8, adultCount);
            pr.setDate(9, Date.valueOf(startDate));
            pr.setDate(10, Date.valueOf(endDate));
            pr.setString(11, guestName);
            pr.setString(12, guestPhone);
            pr.setString(13, guestIdNo);
            pr.setString(14, guestEmail);
            pr.setBigDecimal(15, totalCost);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean update(int reservationId, int childCount, int adultCount, LocalDate startDate, LocalDate endDate, String guestName, String guestPhone, String guestIdNo, String guestEmail, BigDecimal totalCost) {
        String query = "UPDATE public.reservations " +
                "SET child_count = ?, adult_count = ?, start_date = ?, end_date = ?, guest_name = ?, guest_phone = ?, guest_identification_number = ?, guest_email = ?, total_cost = ? " +
                "WHERE reservation_id = ?";

        try {
            PreparedStatement pr = this.databaseConnection.getConnection().prepareStatement(query);
            pr.setInt(1, childCount);
            pr.setInt(2, adultCount);
            pr.setDate(3, Date.valueOf(startDate));
            pr.setDate(4, Date.valueOf(endDate));
            pr.setString(5, guestName);
            pr.setString(6, guestPhone);
            pr.setString(7, guestIdNo);
            pr.setString(8, guestEmail);
            pr.setBigDecimal(9, totalCost);
            pr.setInt(10, reservationId);

            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Reservation match(ResultSet rs) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setReservationId(rs.getInt("reservation_id"));
        reservation.setInventoryId(rs.getInt("inventory_id"));
        reservation.setHotelId(rs.getInt("hotel_id"));
        reservation.setHotelName(rs.getString("hotel_name"));
        reservation.setCityName(rs.getString("city"));
        reservation.setDiscountId(rs.getInt("discount_id"));
        reservation.setPensionId(rs.getInt("pension_id"));
        reservation.setPensionTypeName(rs.getString("pension_type"));
        reservation.setRoomTypeId(rs.getInt("room_type_id"));
        reservation.setRoomTypeName(rs.getString("room_type_name"));
        reservation.setChildCount(rs.getInt("child_count"));
        reservation.setAdultCount(rs.getInt("adult_count"));
        reservation.setStartDate(rs.getDate("start_date").toLocalDate());
        reservation.setEndDate(rs.getDate("end_date").toLocalDate());
        reservation.setGuestName(rs.getString("guest_name"));
        reservation.setGuestPhone(rs.getString("guest_phone"));
        reservation.setGuestIdentificationNumber(rs.getString("guest_identification_number"));
        reservation.setGuestEmail(rs.getString("guest_email"));
        reservation.setTotalCost(rs.getBigDecimal("total_cost"));
        return reservation;
    }

}
