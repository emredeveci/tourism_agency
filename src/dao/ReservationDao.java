package dao;

import core.DatabaseConnection;
import entity.Hotel;
import entity.Reservation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public List<Object[]> findHotelInfoByInventoryId(int inventoryId) {
        List<Object[]> hotelInfoList = new ArrayList<>();
        String query = "SELECT h.hotel_name, h.city, h.district, h.star_rating, h.address\n" +
                "FROM room_inventory ri\n" +
                "JOIN hotels h ON ri.hotel_id = h.hotel_id\n" +
                "WHERE ri.inventory_id = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, inventoryId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String hotelName = resultSet.getString("hotel_name");
                String city = resultSet.getString("city");
                String district = resultSet.getString("district");
                int stars = resultSet.getInt("star_rating");
                String address = resultSet.getString("address");
                Object[] rowData = {hotelName, city, district, stars, address};
                hotelInfoList.add(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hotelInfoList;
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
