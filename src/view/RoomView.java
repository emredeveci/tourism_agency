package view;

import business.RoomManager;
import core.Utility;
import entity.Room;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class RoomView extends Layout {
    private JPanel container;
    private JComboBox<String> cmb_rooms_hotel_name;
    private JComboBox cmb_rooms_room_type;
    private JComboBox cmb_rooms_pension_type;
    private JComboBox cmb_rooms_season;
    private JPanel pnl_top;
    private JTextField fld_rooms_stock;
    private JTextField fld_rooms_beds;
    private JTextField fld_rooms_size;
    private JPanel pnl_mid;
    private JTextField fld_rooms_adult_price;
    private JTextField fld_rooms_child_price;
    private JCheckBox cbox_rooms_television;
    private JCheckBox cbox_rooms_minibar;
    private JCheckBox cbox_rooms_console;
    private JCheckBox cbox_rooms_projector;
    private JCheckBox cbox_rooms_safe;
    private JButton btn_rooms_submit;
    private JLabel lbl_rooms_title;
    private String[] currentHotelRoomTypes;
    private String[] currentHotelPensionTypes;
    private String[] currentHotelSeasons;
    private Map<Integer, String> hotelMap;

    private Room room;
    private RoomManager roomManager;

    public RoomView() {
//        this.room = room;
        this.roomManager = new RoomManager();
        this.add(container);
        this.guiInitialize(1000, 700);
        populateHotelComboBox();
        final int[] selectedHotelId = {-1};

        cmb_rooms_hotel_name.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox comboBox = (JComboBox) e.getSource();
                String selectedHotelName = (String) comboBox.getSelectedItem();

                for (Map.Entry<Integer, String> entry : hotelMap.entrySet()) {
                    if (entry.getValue().equals(selectedHotelName)) {
                        selectedHotelId[0] = entry.getKey();
                        break;
                    }
                }

                System.out.println("Selected hotel ID: " + selectedHotelId[0]);
                cmb_rooms_room_type.setModel(new DefaultComboBoxModel<>(roomManager.getRoomTypesForHotel(selectedHotelId[0])));
                cmb_rooms_pension_type.setModel(new DefaultComboBoxModel<>(roomManager.getPensionTypesForHotel(selectedHotelId[0])));
                cmb_rooms_season.setModel(new DefaultComboBoxModel<>(roomManager.getSeasonTypesForHotel(selectedHotelId[0])));
            }
        });

        this.btn_rooms_submit.addActionListener(e -> {
            if (Utility.isFieldListEmpty(new JTextField[]{this.fld_rooms_child_price, this.fld_rooms_size, this.fld_rooms_beds, this.fld_rooms_stock, this.fld_rooms_adult_price}) || cmb_rooms_hotel_name.getSelectedItem() == null || cmb_rooms_hotel_name.getSelectedItem().toString().isEmpty() || cmb_rooms_room_type.getSelectedItem() == null || cmb_rooms_room_type.getSelectedItem().toString().isEmpty() || cmb_rooms_season.getSelectedItem() == null || cmb_rooms_season.getSelectedItem().toString().isEmpty() || cmb_rooms_pension_type.getSelectedItem() == null || cmb_rooms_pension_type.getSelectedItem().toString().isEmpty()) {
                Utility.showMessage("fill");
            } else {
                boolean result = false;

                int hotelId = selectedHotelId[0];
                String roomType = this.cmb_rooms_room_type.getSelectedItem().toString();
                String pensionType = this.cmb_rooms_pension_type.getSelectedItem().toString();
                int seasonId = Integer.parseInt(this.cmb_rooms_season.getSelectedItem().toString());
                String stock = fld_rooms_stock.getText();
                int numberOfBeds = Integer.parseInt(fld_rooms_beds.getText());
                String roomSize = fld_rooms_size.getText();
                double adultPrice = Integer.parseInt(fld_rooms_adult_price.getText());
                double childPrice = Integer.parseInt(fld_rooms_child_price.getText());
                List<String> selectedRoomFeatures = getSelectedRoomFeatures();

                result = this.roomManager.save(hotelId, roomType, pensionType, seasonId, stock, numberOfBeds, roomSize, adultPrice, childPrice, selectedRoomFeatures);


                if (result) {
                    Utility.showMessage("done");
                    dispose();
                } else {
                    Utility.showMessage("error");
                }
            }
        });

//        if(room != null){
//            this.cmb_rooms_room_type.setModel(new DefaultComboBoxModel<>(roomManager.getRoomTypesForHotel(room.getInventory_id())));
//            this.cmb_rooms_pension_type.setModel(new DefaultComboBoxModel<>(roomManager.getPensionTypesForHotel(room.getInventory_id())));
//            this.cmb_rooms_season.setModel(new DefaultComboBoxModel<>(roomManager.getSeasonTypesForHotel(room.getInventory_id())));
//            this.cmb_rooms_hotel_name.getModel().setSelectedItem(this.room.getHotel_name());
//            this.cmb_rooms_pension_type.getModel().setSelectedItem(this.room.getPension_type());
//            this.cmb_rooms_room_type.getModel().setSelectedItem(this.room.getRoom_type());
//            this.cmb_rooms_season.getModel().setSelectedItem(this.room.getDiscount_id());
//            this.fld_rooms_stock.setText(String.valueOf(room.getQuantity_available()));
//            this.fld_rooms_adult_price.setText(String.valueOf(room.getAdult_price()));
//            this.fld_rooms_child_price.setText(String.valueOf(room.getChild_price()));
//
//            List<Object[]> roomDetailsData = roomManager.findAllRoomDetails(room.getInventory_id());
//            List<Object[]> roomFeaturesData = roomManager.findAllRoomFeatures(room.getInventory_id());
//
//            preselectRoomFeaturesCheckboxes(roomFeaturesData);
//            prefillRoomDetailsTextFields(roomDetailsData);
//        }
    }

//    private void preselectRoomFeaturesCheckboxes(List<Object[]> roomFeaturesData) {
//        if (roomFeaturesData != null) {
//            for (Object[] rowData : roomFeaturesData) {
//                String featureName = (String) rowData[1];
//                switch (featureName) {
//                    case "Television":
//                        cbox_rooms_television.setSelected(true);
//                        break;
//                    case "Minibar":
//                        cbox_rooms_minibar.setSelected(true);
//                        break;
//                    case "Gaming console":
//                        cbox_rooms_console.setSelected(true);
//                        break;
//                    case "Safe":
//                        cbox_rooms_safe.setSelected(true);
//                        break;
//                    case "Projector":
//                        cbox_rooms_projector.setSelected(true);
//                        break;
//                }
//            }
//        }
//    }
//
//    private void prefillRoomDetailsTextFields(List<Object[]> roomDetailsData) {
//        if (roomDetailsData != null) {
//            for (Object[] rowData : roomDetailsData) {
//                String numberOfBeds = rowData[1].toString();
//                String roomSize = (String) rowData[2];
//                this.fld_rooms_beds.setText(numberOfBeds);
//                this.fld_rooms_size.setText(roomSize);
//            }
//        }
//    }

    private List<String> getSelectedRoomFeatures() {
        List<String> selectedRoomFeatures = new ArrayList<>();
        if (cbox_rooms_television.isSelected()) {
            selectedRoomFeatures.add("Television");
        }
        if (cbox_rooms_minibar.isSelected()) {
            selectedRoomFeatures.add("Minibar");
        }
        if (cbox_rooms_console.isSelected()) {
            selectedRoomFeatures.add("Gaming console");
        }
        if (cbox_rooms_safe.isSelected()) {
            selectedRoomFeatures.add("Safe");
        }
        if (cbox_rooms_projector.isSelected()) {
            selectedRoomFeatures.add("Projector");
        }

        return selectedRoomFeatures;
    }

    private void populateHotelComboBox() {
        hotelMap = roomManager.getHotelNames();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("");
        for (Map.Entry<Integer, String> entry : hotelMap.entrySet()) {
            model.addElement(entry.getValue());
        }
        cmb_rooms_hotel_name.setModel(model);
    }

}
