package dao;

import core.DatabaseConnection;
import entity.Hotel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
