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
