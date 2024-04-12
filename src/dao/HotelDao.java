package dao;

import core.DatabaseConnection;
import entity.Hotel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HotelDao {
    private final DatabaseConnection databaseConnection;

    public HotelDao() {
        this.databaseConnection = DatabaseConnection.getInstance();
    }

    public List<Hotel> findAll() {
        List<Hotel> hotelList = new ArrayList<>();
        String query = "SELECT * FROM public.hotels";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                hotelList.add(match(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hotelList;
    }

    public List<Object[]> findAllPensions(int id) {
        List<Object[]> pensionData = new ArrayList<>();
        String query = "SELECT h.hotel_id, pt.pension_type\n" +
                "FROM hotels h\n" +
                "JOIN hotel_pensions hp ON h.hotel_id = hp.hotel_id\n" +
                "JOIN pension_types pt ON hp.pension_id = pt.pension_id\n" +
                "WHERE h.hotel_id = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int hotelId = resultSet.getInt("hotel_id");
                String pensionType = resultSet.getString("pension_type");
                Object[] rowData = {hotelId, pensionType};
                pensionData.add(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println(pensionData);
        return pensionData;
    }

    public List<Object[]> findAllAmenities(int id) {
        List<Object[]> amenityData = new ArrayList<>();
        String query = "SELECT h.hotel_id, a.amenity_name\n" +
                "FROM hotels h\n" +
                "JOIN hotel_amenities ha ON h.hotel_id = ha.hotel_id\n" +
                "JOIN amenities a ON ha.amenity_id = a.amenity_id\n" +
                "WHERE h.hotel_id = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int hotelId = resultSet.getInt("hotel_id");
                String amenityName = resultSet.getString("amenity_name");
                Object[] rowData = {hotelId, amenityName};
                amenityData.add(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println(amenityData);
        return amenityData;
    }

    public List<Object[]> findDiscountPeriods(int hotelId) {
        List<Object[]> discountPeriods = new ArrayList<>();
        String query = "SELECT discount_id, start_date, end_date " +
                "FROM discount_periods " +
                "WHERE hotel_id = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, hotelId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int discountId = resultSet.getInt("discount_id");
                java.sql.Date startDateSql = resultSet.getDate("start_date");
                java.sql.Date endDateSql = resultSet.getDate("end_date");
                LocalDate startDate = startDateSql.toLocalDate();
                LocalDate endDate = endDateSql.toLocalDate();

                Object[] rowData = {discountId, startDate, endDate};
                discountPeriods.add(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return discountPeriods;
    }

    public Hotel match(ResultSet rs) throws SQLException {
        Hotel hotel = new Hotel();
        hotel.setHotel_id(rs.getInt("hotel_id"));
        hotel.setHotel_name(rs.getString("hotel_name"));
        hotel.setStar_rating(rs.getInt("star_rating"));
        hotel.setCity(rs.getString("city"));
        hotel.setDistrict(rs.getString("district"));
        hotel.setEmail(rs.getString("email"));
        hotel.setPhone(rs.getString("phone"));
        hotel.setAddress(rs.getString("address"));
        return hotel;
    }


}
