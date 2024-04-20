package view;

import business.ReservationManager;

import javax.swing.*;
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

        List<Object[]> amenitiesData = reservationManager.findAllAmenities(inventoryId);
        List<Object[]> roomFeatures = reservationManager.findAllFeatures(inventoryId);
        List<Object[]> hotelInfoList = reservationManager.findHotelInfoByInventoryId(inventoryId);

        preselectAmenityCheckboxes(amenitiesData);
        preselectFeatureCheckboxes(roomFeatures);
        preFillHotelInformation(hotelInfoList);
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

    private void preFillHotelInformation(List<Object[]> hotelInfoList){
        if (!hotelInfoList.isEmpty()) {
            Object[] rowData = hotelInfoList.get(0); // Assuming there is only one hotel info in the list
            String hotelName = (String) rowData[0];
            String city = (String) rowData[1];
            String district = (String) rowData[2];
            int stars = (int) rowData[3];
            String address = (String) rowData[4];

            // Assuming these are your text fields
            fld_reservation_hotel.setText(hotelName);
            fld_reservation_city.setText(city);
            fld_reservation_district.setText(district);
            fld_reservation_stars.setText(String.valueOf(stars));
            fld_reservation_address.setText(address);
        }
    }
}
