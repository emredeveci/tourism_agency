package view;

import business.ReservationManager;
import core.Utility;
import entity.Reservation;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ReservationView extends Layout {
    private JLabel lbl_reservation_menu;
    private JTextField fld_reservation_hotel;
    private JTextField fld_reservation_city;
    private JTextField fld_reservation_district;
    private JTextField fld_reservation_stars;
    private JTextField fld_reservation_address;
    private JCheckBox cbox_reservation_freeparking;
    private JCheckBox cbox_reservation_freewifi;
    private JCheckBox cbox_reservation_pool;
    private JCheckBox cbox_reservation_fitness;
    private JCheckBox cbox_reservation_concierge;
    private JCheckBox cbox_reservation_spa;
    private JCheckBox cbox_reservation_roomservice;
    private JPanel pnl_reservation_hotel;
    private JPanel pnl_reservation_room;
    private JTextField fld_reservation_roomtype;
    private JTextField fld_reservation_beds;
    private JTextField fld_reservations_pension;
    private JTextField fld_reservations_size;
    private JCheckBox cbox_reservations_tv;
    private JCheckBox cbox_reservations_minibar;
    private JCheckBox cbox_reservations_console;
    private JCheckBox cbox_reservations_safe;
    private JCheckBox cbox_reservations_projector;
    private JTextField fld_reservations_startdate;
    private JTextField fld_reservations_enddate;
    private JTextField fld_reservations_guestname;
    private JTextField fld_reservations_guestId;
    private JTextField fld_reservations_phone;
    private JTextField fld_reservations_email;
    private JTextField fld_reservations_adult;
    private JTextField fld_reservations_cost;
    private JPanel pnl_reservation_guest;
    private JButton btn_reservation_submit;
    private JPanel container;
    private JTextField fld_reservations_children;
    Integer childCount;
    Integer adultCount;
    String startDateString;
    String endDateString;
    LocalDate startDate;
    LocalDate endDate;
    Integer dayCount;

    private ReservationManager reservationManager;

    public ReservationView(Integer inventoryId, String purpose) {
        this.add(container);
        this.guiInitialize(1200, 900);
        this.reservationManager = new ReservationManager();

        if (Objects.equals(purpose, "reserve")) {
            this.lbl_reservation_menu.setText("Make Reservation");
        } else if (Objects.equals(purpose, "update")) {
            this.lbl_reservation_menu.setText("Update Reservation");
        } else {
            dispose();
        }

        JTextField[] textFields = { fld_reservations_children, fld_reservations_adult, fld_reservations_startdate, fld_reservations_enddate };
        addListenersToTextFields(textFields);

        //values that won't change when the agent opens up the reservation window
        List<Object[]> amenitiesData = reservationManager.findAllAmenities(inventoryId);
        List<Object[]> roomFeatures = reservationManager.findAllFeatures(inventoryId);
        List<Object[]> hotelInfoList = reservationManager.findHotelInfo(inventoryId);
        List<Object[]> roomInfoList = reservationManager.findRoomInfo(inventoryId);
        BigDecimal childPrice = reservationManager.findPricePerNight(inventoryId, 2);
        BigDecimal adultPrice = reservationManager.findPricePerNight(inventoryId, 1);
        Integer discountId = reservationManager.findDiscountId(inventoryId);
        Integer roomPensionId = reservationManager.findPensionId(inventoryId);
        Integer roomTypeId = reservationManager.findRoomTypeId(inventoryId);
        Integer hotelId = (int) hotelInfoList.get(0)[0];
        String hotelName = (String) hotelInfoList.get(0)[1];

        //values that will constantly update with keyboard events
//        final Integer[] childCount = new Integer[1];
//        final Integer[] adultCount = new Integer[1];
//        final String[] startDateString = new String[1];
//        final String[] endDateString = new String[1];
//        final LocalDate[] startDate = new LocalDate[1];
//        final LocalDate[] endDate = new LocalDate[1];
//        final Integer[] dayCount = new Integer[1];

        preselectAmenityCheckboxes(amenitiesData);
        preselectFeatureCheckboxes(roomFeatures);
        preFillHotelInformation(hotelInfoList);
        prefillRoomInformation(roomInfoList);



        fld_reservations_children.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateTotalCost();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateTotalCost();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateTotalCost();
            }
        });

// Add a change listener to fld_reservations_adult
        fld_reservations_adult.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateTotalCost();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateTotalCost();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateTotalCost();
            }
        });

        this.btn_reservation_submit.addActionListener(e -> {
            boolean result = false;
            String guestName = fld_reservations_guestname.getText().trim();
            String guestPhone = fld_reservations_phone.getText().trim();
            String guestIdNo = fld_reservations_guestId.getText().trim();
            String guestEmail = fld_reservations_guestId.getText().trim();
            BigDecimal totalCost = new BigDecimal(fld_reservations_cost.getText().trim());

            result = this.reservationManager.save(inventoryId, hotelId, hotelName, discountId, roomPensionId, roomTypeId, childCount, adultCount, startDate, endDate, guestName, guestPhone, guestIdNo, guestEmail, totalCost);

            if (result) {
                Utility.showMessage("done");
                dispose();
            } else {
                Utility.showMessage("error");
            }

        });
    }

    private void preselectAmenityCheckboxes(List<Object[]> amenitiesData) {
        if (amenitiesData != null) {
            for (Object[] rowData : amenitiesData) {
                String amenityName = (String) rowData[1];
                switch (amenityName) {
                    case "Free parking":
                        cbox_reservation_freeparking.setSelected(true);
                        break;
                    case "Free WiFi":
                        cbox_reservation_freewifi.setSelected(true);
                        break;
                    case "Swimming pool":
                        cbox_reservation_pool.setSelected(true);
                        break;
                    case "Fitness center":
                        cbox_reservation_fitness.setSelected(true);
                        break;
                    case "Concierge":
                        cbox_reservation_concierge.setSelected(true);
                        break;
                    case "Spa":
                        cbox_reservation_spa.setSelected(true);
                        break;
                    case "24/7 Room service":
                        cbox_reservation_roomservice.setSelected(true);
                        break;
                }
            }
        }
    }

    private void preselectFeatureCheckboxes(List<Object[]> roomFeatures) {
        if (roomFeatures != null) {
            for (Object[] rowData : roomFeatures) {
                String featureName = (String) rowData[1];
                switch (featureName) {
                    case "Television":
                        cbox_reservations_tv.setSelected(true);
                        break;
                    case "Minibar":
                        cbox_reservations_minibar.setSelected(true);
                        break;
                    case "Gaming console":
                        cbox_reservations_console.setSelected(true);
                        break;
                    case "Safe":
                        cbox_reservations_safe.setSelected(true);
                        break;
                    case "Projector":
                        cbox_reservations_projector.setSelected(true);
                        break;
                }
            }
        }
    }

    private void preFillHotelInformation(List<Object[]> hotelInfoList) {
        if (!hotelInfoList.isEmpty()) {
            Object[] rowData = hotelInfoList.get(0); // Assuming there is only one hotel info in the list
            String hotelName = (String) rowData[1];
            String city = (String) rowData[2];
            String district = (String) rowData[3];
            int stars = (int) rowData[4];
            String address = (String) rowData[5];

            // Assuming these are your text fields
            fld_reservation_hotel.setText(hotelName);
            fld_reservation_city.setText(city);
            fld_reservation_district.setText(district);
            fld_reservation_stars.setText(String.valueOf(stars));
            fld_reservation_address.setText(address);
        }
    }

    private void prefillRoomInformation(List<Object[]> roomInfoList) {
        if (!roomInfoList.isEmpty()) {
            Object[] rowData = roomInfoList.get(0); // Assuming there is only one hotel info in the list
            String roomType = (String) rowData[1];
            int numberOfBeds = (int) rowData[2];
            String roomSize = (String) rowData[3];
            String pensionType = (String) rowData[4];

            fld_reservation_roomtype.setText(roomType);
            fld_reservation_beds.setText(String.valueOf(numberOfBeds));
            fld_reservations_pension.setText(pensionType);
            fld_reservations_size.setText(roomSize);
        }
    }

    // Method to update the total cost based on the number of children and adults
    private void updateTotalCost() {
        try {
            // Get the values from the text fields
            int childrenCount = Integer.parseInt(fld_reservations_children.getText().trim());
            int adultCount = Integer.parseInt(fld_reservations_adult.getText().trim());

            // Calculate the total cost based on your business logic
            BigDecimal totalCost = calculateTotalCost(childrenCount, adultCount);

            // Update the fld_reservations_cost text field with the new total cost
            fld_reservations_cost.setText(totalCost.toString());
        } catch (NumberFormatException ex) {
            // Handle the case where the user enters invalid input (non-numeric characters)
            fld_reservations_cost.setText("Invalid input");
        }
    }

    // Method to calculate the total cost based on the number of children and adults
    private BigDecimal calculateTotalCost(int childrenCount, int adultCount) {
        // Your business logic to calculate the total cost goes here
        // Example: Assume each child costs $50 and each adult costs $100
        BigDecimal childCost = BigDecimal.valueOf(50).multiply(BigDecimal.valueOf(childrenCount));
        BigDecimal adultCost = BigDecimal.valueOf(100).multiply(BigDecimal.valueOf(adultCount));
        return childCost.add(adultCost);
    }

    private void addListenersToTextFields(JTextField[] textFields) {
        for (JTextField textField : textFields) {
            textField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    recalculate();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    recalculate();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    recalculate();
                }
            });
        }
    }

    private void recalculate() {

        if(!fld_reservations_adult.getText().isEmpty() && !fld_reservations_children.getText().isEmpty() && !fld_reservations_enddate.getText().isEmpty() && !fld_reservations_startdate.getText().isEmpty()){
            try{
                childCount = Integer.valueOf(fld_reservations_children.getText().trim());
                adultCount = Integer.valueOf(fld_reservations_adult.getText().trim());
                startDateString = fld_reservations_startdate.getText().trim();
                endDateString = fld_reservations_enddate.getText().trim();
                startDate = LocalDate.parse(startDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                endDate = LocalDate.parse(endDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                dayCount = (int) ChronoUnit.DAYS.between(startDate, endDate);
            } catch (NumberFormatException | DateTimeParseException ex) {
                // Handle invalid input
                ex.printStackTrace(); // Or show an error message
            }
        }

        System.out.println("Child Count: " + childCount);
        System.out.println("Adult Count: " + adultCount);
        System.out.println("Start Date: " + startDate);
        System.out.println("End Date: " + endDate);
        System.out.println("Day Count: " + dayCount);

    }
}

