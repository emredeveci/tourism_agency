package view;

import business.HotelManager;
import core.Utility;
import entity.Hotel;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HotelView extends Layout {
    //CRITERIA 1
    private JPanel container;
    private JPanel pnl_title;
    private JTextField fld_name;
    private JPanel pnl_body1;
    private JTextField fld_city;
    private JTextField fld_district;
    private JComboBox cmb_stars;
    private JPanel pnl_body2;
    private JTextField fld_email;
    private JTextField fld_phone;
    private JTextField fld_address;
    private JCheckBox cbox_freeparking;
    private JCheckBox cbox_freewifi;
    private JCheckBox cbox_swimmingpool;
    private JCheckBox cbox_fitnesscenter;
    private JCheckBox cbox_concierge;
    private JCheckBox cbox_spa;
    private JPanel pnl_amenities;
    private JCheckBox cbox_roomservice;
    private JCheckBox cbox_ultraallinclusive;
    private JCheckBox cbox_everythingincluded;
    private JCheckBox cbox_roombreakfast;
    private JCheckBox cbox_fullpension;
    private JCheckBox cbox_halfpension;
    private JCheckBox cbox_roomonly;
    private JCheckBox cbox_fullcredit;
    private JPanel pnl_pension_types;
    private JButton btn_hotel_submit;
    private JPanel pnl_seasons;
    private JTextField fld_season_one_start;
    private JTextField fld_season_one_end;
    private JTextField fld_seasontwo_start;
    private JTextField fld_seasontwo_end;


    private Hotel hotel;
    private HotelManager hotelManager;
    Integer[] stars = {5, 4, 3, 2, 1};

    public HotelView(Hotel hotel) {
        this.hotel = hotel;
        this.hotelManager = new HotelManager();
        this.add(container);
        this.guiInitialize(1000, 900);

        this.cmb_stars.setModel(new DefaultComboBoxModel<>(stars));

        /* If a hotel is picked before this window opens,
        all the necessary hotel information is retrieved from the database,
        and related text fields and combo boxes are prefilled with the data and become uneditable*/
        if (hotel != null) {
            this.fld_name.setText(hotel.getHotel_name());
            this.fld_city.setText(hotel.getCity());
            this.fld_district.setText(hotel.getDistrict());
            this.cmb_stars.getModel().setSelectedItem(this.hotel.getStar_rating());
            this.fld_email.setText(hotel.getEmail());
            this.fld_phone.setText(hotel.getPhone());
            this.fld_address.setText(hotel.getAddress());

            //CRITERIA 11 - 12 (Season and pension management)
            // Retrieve amenities and pension types associated with the hotel from the database
            List<Object[]> amenitiesData = hotelManager.findAllAmenities(hotel.getHotel_id());
            List<Object[]> pensionTypesData = hotelManager.findAllPensions(hotel.getHotel_id());
            List<LocalDate[]> preselectSeasons = hotelManager.findEnteredSeasons(hotel.getHotel_id());

            // Pre-select checkboxes for amenities
            preselectAmenityCheckboxes(amenitiesData);

            // Pre-select checkboxes for pension types
            preselectPensionTypeCheckboxes(pensionTypesData);

            // Pre-fill checkboxes for pension types
            preselectSeasonFields(preselectSeasons);


            this.btn_hotel_submit.addActionListener(e -> {
                if (Utility.isFieldListEmpty(new JTextField[]{this.fld_address, this.fld_phone, this.fld_email, this.fld_name, this.fld_city, this.fld_district})) {
                    //CRITERIA 25
                    Utility.showMessage("fill");
                } else {
                    boolean result = false;
                    //CRITERIA 10 - All the necessary info for adding a hotel is saved here
                    this.hotel.setHotel_name(fld_name.getText());
                    this.hotel.setCity(fld_city.getText());
                    this.hotel.setDistrict(fld_district.getText());
                    this.hotel.setStar_rating((Integer) cmb_stars.getSelectedItem());
                    this.hotel.setEmail(fld_email.getText());
                    this.hotel.setPhone(fld_phone.getText());
                    this.hotel.setAddress(fld_address.getText());

                    List<String> selectedAmenities = getSelectedAmenities();
                    List<String> selectedPensions = getSelectedPensions();
                    List<Date[]> enteredSeasons = getEnteredSeasons();

                    if (this.hotel.getHotel_id() != 0) {
                        result = this.hotelManager.update(this.hotel, selectedAmenities, selectedPensions, enteredSeasons);
                    } else {
                        result = this.hotelManager.save(this.hotel, selectedAmenities, selectedPensions, enteredSeasons);
                    }

                    if (result) {
                        //CRITERIA 24
                        Utility.showMessage("done");
                        dispose();
                    } else {
                        //CRITERIA 25
                        Utility.showMessage("error");
                    }
                }
            });

        }

    }

    private List<String> getSelectedAmenities() {
        List<String> selectedAmenities = new ArrayList<>();
        if (cbox_freeparking.isSelected()) {
            selectedAmenities.add("Free parking");
        }
        if (cbox_freewifi.isSelected()) {
            selectedAmenities.add("Free WiFi");
        }
        if (cbox_swimmingpool.isSelected()) {
            selectedAmenities.add("Swimming pool");
        }
        if (cbox_fitnesscenter.isSelected()) {
            selectedAmenities.add("Fitness center");
        }
        if (cbox_concierge.isSelected()) {
            selectedAmenities.add("Concierge");
        }
        if (cbox_spa.isSelected()) {
            selectedAmenities.add("Spa");
        }
        if (cbox_roomservice.isSelected()) {
            selectedAmenities.add("24/7 Room service");
        }

        return selectedAmenities;
    }

    //CRITERIA 12 - This method finds if the selected hotel has pensions entered
    private List<String> getSelectedPensions() {
        List<String> selectedPensions = new ArrayList<>();
        if (cbox_ultraallinclusive.isSelected()) {
            selectedPensions.add("Ultra All Inclusive");
        }
        if (cbox_everythingincluded.isSelected()) {
            selectedPensions.add("Everything Included");
        }
        if (cbox_roombreakfast.isSelected()) {
            selectedPensions.add("Room Breakfast");
        }
        if (cbox_fullpension.isSelected()) {
            selectedPensions.add("Full Pension");
        }
        if (cbox_halfpension.isSelected()) {
            selectedPensions.add("Half Pension");
        }
        if (cbox_roomonly.isSelected()) {
            selectedPensions.add("Room Only");
        }
        if (cbox_fullcredit.isSelected()) {
            selectedPensions.add("Full Credit No Alcohol");
        }

        return selectedPensions;
    }

    //CRITERIA 11 - This method finds if the selected hotel has seasons entered
    private List<Date[]> getEnteredSeasons() {
        List<Date[]> enteredSeasons = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // adjust the date format as per your requirements

        String seasonOneStart = fld_season_one_start.getText().trim();
        String seasonOneEnd = fld_season_one_end.getText().trim();

        try {
            // Check if season 1 dates are entered
            if (!seasonOneStart.isEmpty() && !seasonOneEnd.isEmpty()) {
                Date startDate = dateFormat.parse(seasonOneStart);
                Date endDate = dateFormat.parse(seasonOneEnd);
                Date[] seasonOneDates = {startDate, endDate};
                enteredSeasons.add(seasonOneDates);
            }

            // Check if season 2 text fields are not null and contain dates
            if (fld_seasontwo_start != null && fld_seasontwo_end != null) {
                String seasonTwoStart = fld_seasontwo_start.getText().trim();
                String seasonTwoEnd = fld_seasontwo_end.getText().trim();

                // Parse season 2 dates into Date objects
                if (!seasonTwoStart.isEmpty() && !seasonTwoEnd.isEmpty()) {
                    Date startDate = dateFormat.parse(seasonTwoStart);
                    Date endDate = dateFormat.parse(seasonTwoEnd);
                    Date[] seasonTwoDates = {startDate, endDate};
                    enteredSeasons.add(seasonTwoDates);
                }
            }
        } catch (ParseException e) {
            // Handle parsing errors
            e.printStackTrace(); // or log the error
        } catch (NullPointerException e) {
            // Handle null pointer exceptions
            e.printStackTrace(); // or log the error
        }

        return enteredSeasons;
    }

    private void preselectAmenityCheckboxes(List<Object[]> amenitiesData) {
        if (amenitiesData != null) {
            for (Object[] rowData : amenitiesData) {
                String amenityName = (String) rowData[1];
                switch (amenityName) {
                    case "Free parking":
                        cbox_freeparking.setSelected(true);
                        break;
                    case "Free WiFi":
                        cbox_freewifi.setSelected(true);
                        break;
                    case "Swimming pool":
                        cbox_swimmingpool.setSelected(true);
                        break;
                    case "Fitness center":
                        cbox_fitnesscenter.setSelected(true);
                        break;
                    case "Concierge":
                        cbox_concierge.setSelected(true);
                        break;
                    case "Spa":
                        cbox_spa.setSelected(true);
                        break;
                    case "24/7 Room service":
                        cbox_roomservice.setSelected(true);
                        break;
                }
            }
        }
    }

    private void preselectPensionTypeCheckboxes(List<Object[]> pensionTypesData) {
        if (pensionTypesData != null) {
            for (Object[] rowData : pensionTypesData) {
                String pensionType = (String) rowData[1];
                switch (pensionType) {
                    case "Full Pension":
                        cbox_fullpension.setSelected(true);
                        break;
                    case "Half Pension":
                        cbox_halfpension.setSelected(true);
                        break;
                    case "Room Only":
                        cbox_roomonly.setSelected(true);
                        break;
                    case "Full Credit No Alcohol":
                        cbox_fullcredit.setSelected(true);
                        break;
                    case "Ultra All Inclusive":
                        cbox_ultraallinclusive.setSelected(true);
                        break;
                    case "Room Breakfast":
                        cbox_roombreakfast.setSelected(true);
                }
            }
        }
    }

    private void preselectSeasonFields(List<LocalDate[]> preselectSeasons) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (preselectSeasons != null && !preselectSeasons.isEmpty()) {
            int seasonCounter = 0;
            for (LocalDate[] seasonDates : preselectSeasons) {
                if (seasonDates.length >= 2) { // Ensure that the array has the required elements
                    try {
                        String seasonStart = seasonDates[0].format(dateFormatter);
                        String seasonEnd = seasonDates[1].format(dateFormatter);

                        if (seasonCounter == 0) {
                            // For the first season, populate the first set of text fields
                            fld_season_one_start.setText(seasonStart);
                            fld_season_one_end.setText(seasonEnd);
                        } else if (seasonCounter == 1) {
                            // For the second season, populate the second set of text fields
                            fld_seasontwo_start.setText(seasonStart);
                            fld_seasontwo_end.setText(seasonEnd);
                            // Break the loop after the second season is populated
                            break;
                        }

                        seasonCounter++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            // If there are no seasons, clear all season fields
            fld_season_one_start.setText("");
            fld_season_one_end.setText("");
            fld_seasontwo_start.setText("");
            fld_seasontwo_end.setText("");
        }
    }
}
