package dao;

import core.DatabaseConnection;
import entity.Hotel;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HotelDao {
    private final DatabaseConnection databaseConnection;

    public HotelDao() {
        this.databaseConnection = DatabaseConnection.getInstance();
    }

    public Hotel getById(int id) {
        Hotel obj = null;
        String query = "SELECT * FROM public.hotels WHERE hotel_id = ?";
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

//    public boolean save(Hotel hotel) {
//        String query = "INSERT INTO public.hotels " +
//                "(" +
//                "hotel_name," +
//                "star_rating," +
//                "city," +
//                "district," +
//                "email," +
//                "phone," +
//                "address" +
//                ")" +
//                " VALUES (?, ?, ?, ?, ?, ?, ?)";
//
//        try {
//            PreparedStatement pr = this.databaseConnection.getConnection().prepareStatement(query);
//            pr.setString(1, hotel.getHotel_name());
//            pr.setInt(2, hotel.getStar_rating());
//            pr.setString(3, hotel.getCity());
//            pr.setString(4, hotel.getDistrict());
//            pr.setString(5, hotel.getEmail());
//            pr.setString(6, hotel.getPhone());
//            pr.setString(7, hotel.getAddress());
//            return pr.executeUpdate() != -1;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return true;
//    }

    public boolean save(Hotel hotel, List<String> selectedAmenities, List<String> selectedPensions, List<Date[]> enteredSeasons) {
        String query = "INSERT INTO public.hotels " +
                "(hotel_name, star_rating, city, district, email, phone, address) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement pr = this.databaseConnection.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pr.setString(1, hotel.getHotel_name());
            pr.setInt(2, hotel.getStar_rating());
            pr.setString(3, hotel.getCity());
            pr.setString(4, hotel.getDistrict());
            pr.setString(5, hotel.getEmail());
            pr.setString(6, hotel.getPhone());
            pr.setString(7, hotel.getAddress());
            pr.executeUpdate();

            ResultSet generatedKeys = pr.getGeneratedKeys();
            int hotelId = -1;
            if (generatedKeys.next()) {
                hotelId = generatedKeys.getInt(1);
            } else {
                // Handle failure to get generated keys
                throw new SQLException("Failed to retrieve generated hotel ID.");
            }

            for (String amenity : selectedAmenities) {
                String amenityQuery = "INSERT INTO hotel_amenities (hotel_id, amenity_id) VALUES (?, ?)";
                PreparedStatement amenityStatement = this.databaseConnection.getConnection().prepareStatement(amenityQuery);
                amenityStatement.setInt(1, hotelId);
                amenityStatement.setInt(2, getAmenityIdByName(amenity));
                amenityStatement.executeUpdate();
            }

            for (String pension : selectedPensions) {
                String pensionQuery = "INSERT INTO hotel_pensions (hotel_id, pension_id) VALUES (?, ?)";
                PreparedStatement pensionStatement = this.databaseConnection.getConnection().prepareStatement(pensionQuery);
                pensionStatement.setInt(1, hotelId);
                pensionStatement.setInt(2, getPensionIdByName(pension));
                pensionStatement.executeUpdate();
            }

            String seasonQuery = "INSERT INTO discount_periods (hotel_id, start_date, end_date) VALUES (?, ?, ?)";
            PreparedStatement seasonStatement = this.databaseConnection.getConnection().prepareStatement(seasonQuery);
            for (Date[] seasonDates : enteredSeasons) {
                // Extract start and end dates from the Date[]
                Date startDate = seasonDates[0];
                Date endDate = seasonDates[1];

                // Convert Date to java.sql.Date
                java.sql.Date startDateSql = new java.sql.Date(startDate.getTime());
                java.sql.Date endDateSql = new java.sql.Date(endDate.getTime());

                seasonStatement.setInt(1, hotelId);
                seasonStatement.setDate(2, startDateSql);
                seasonStatement.setDate(3, endDateSql);
                seasonStatement.addBatch(); // Add batch for batch insertion
            }
            // Execute batch insertion
            seasonStatement.executeBatch();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false; // Return false if an exception occurs
        }
        return true; // Return true if insertion is successful
    }

    public boolean update(Hotel hotel, List<String> selectedAmenities, List<String> selectedPensions, List<Date[]> enteredSeasons) {
        String query = "UPDATE public.hotels SET " +
                "hotel_name = ? , " +
                "star_rating = ? , " +
                "city = ? ," +
                "district = ? ," +
                "email = ? ," +
                "phone = ? ," +
                "address = ? " +
                "WHERE hotel_id = ?";

        try {
            PreparedStatement pr = this.databaseConnection.getConnection().prepareStatement(query);
            pr.setString(1, hotel.getHotel_name());
            pr.setInt(2, hotel.getStar_rating());
            pr.setString(3, hotel.getCity());
            pr.setString(4, hotel.getDistrict());
            pr.setString(5, hotel.getEmail());
            pr.setString(6, hotel.getPhone());
            pr.setString(7, hotel.getAddress());
            pr.setInt(8, hotel.getHotel_id());
            pr.executeUpdate();

            deleteAmenitiesForHotel(hotel.getHotel_id());

            for (String amenity : selectedAmenities) {
                String amenityQuery = "INSERT INTO hotel_amenities (hotel_id, amenity_id) VALUES (?, ?)";
                PreparedStatement amenityStatement = this.databaseConnection.getConnection().prepareStatement(amenityQuery);
                amenityStatement.setInt(1, hotel.getHotel_id());
                amenityStatement.setInt(2, getAmenityIdByName(amenity));
                amenityStatement.executeUpdate();
            }

            deletePensionsForHotel(hotel.getHotel_id());

            for (String pension : selectedPensions) {
                String pensionQuery = "INSERT INTO hotel_pensions (hotel_id, pension_id) VALUES (?, ?)";
                PreparedStatement pensionStatement = this.databaseConnection.getConnection().prepareStatement(pensionQuery);
                pensionStatement.setInt(1, hotel.getHotel_id());
                pensionStatement.setInt(2, getPensionIdByName(pension));
                pensionStatement.executeUpdate();
            }

            deleteSeasonsForHotel(hotel.getHotel_id());

            String seasonQuery = "INSERT INTO discount_periods (hotel_id, start_date, end_date) VALUES (?, ?, ?)";
            PreparedStatement seasonStatement = this.databaseConnection.getConnection().prepareStatement(seasonQuery);
            for (Date[] seasonDates : enteredSeasons) {
                // Extract start and end dates from the Date[]
                Date startDate = seasonDates[0];
                Date endDate = seasonDates[1];

                // Convert Date to java.sql.Date
                java.sql.Date startDateSql = new java.sql.Date(startDate.getTime());
                java.sql.Date endDateSql = new java.sql.Date(endDate.getTime());

                seasonStatement.setInt(1, hotel.getHotel_id());
                seasonStatement.setDate(2, startDateSql);
                seasonStatement.setDate(3, endDateSql);
                seasonStatement.addBatch(); // Add batch for batch insertion
            }
            // Execute batch insertion
            seasonStatement.executeBatch();


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    public boolean delete(int id) {
        String query = "DELETE FROM public.hotels WHERE hotel_id = ?";
        try {
            PreparedStatement pr = databaseConnection.getConnection().prepareStatement(query);
            pr.setInt(1, id);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
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

        return amenityData;
    }

    public List<Object[]> findDiscountPeriods(int hotelId) {
        List<Object[]> discountPeriods = new ArrayList<>();
        String query = "SELECT hotel_id, start_date, end_date " +
                "FROM discount_periods " +
                "WHERE hotel_id = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, hotelId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int hotel = resultSet.getInt("hotel_id");
                java.sql.Date startDateSql = resultSet.getDate("start_date");
                java.sql.Date endDateSql = resultSet.getDate("end_date");
                LocalDate startDate = startDateSql.toLocalDate();
                LocalDate endDate = endDateSql.toLocalDate();

                Object[] rowData = {hotel, startDate, endDate};
                discountPeriods.add(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return discountPeriods;
    }

    public List<LocalDate[]> findEnteredSeasons(int hotelId) {
        List<LocalDate[]> enteredSeasons = new ArrayList<>();
        String query = "SELECT start_date, end_date " +
                "FROM discount_periods " +
                "WHERE hotel_id = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, hotelId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                LocalDate startDate = resultSet.getDate("start_date").toLocalDate();
                LocalDate endDate = resultSet.getDate("end_date").toLocalDate();

                LocalDate[] seasonDates = {startDate, endDate};
                enteredSeasons.add(seasonDates);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return enteredSeasons;
    }

    public void deleteAmenitiesForHotel(int hotelId) {
        String query = "DELETE FROM hotel_amenities WHERE hotel_id = ?";
        try (PreparedStatement pr = this.databaseConnection.getConnection().prepareStatement(query)) {
            pr.setInt(1, hotelId);
            pr.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePensionsForHotel(int hotelId) {
        String query = "DELETE FROM hotel_pensions WHERE hotel_id = ?";
        try (PreparedStatement pr = this.databaseConnection.getConnection().prepareStatement(query)) {
            pr.setInt(1, hotelId);
            pr.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteSeasonsForHotel(int hotelId) {
        String query = "DELETE FROM discount_periods WHERE hotel_id = ?";
        try (PreparedStatement pr = this.databaseConnection.getConnection().prepareStatement(query)) {
            pr.setInt(1, hotelId);
            pr.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getPensionIdByName(String pensionName) {
        int pensionId = -1;
        String query = "SELECT pension_id FROM pension_types WHERE pension_type = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, pensionName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                pensionId = resultSet.getInt("pension_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pensionId;
    }

    public int getAmenityIdByName(String amenityName) {
        int amenityId = -1;
        String query = "SELECT amenity_id FROM amenities WHERE amenity_name = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, amenityName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                amenityId = resultSet.getInt("amenity_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return amenityId;
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
