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
    private Map<Integer, String> roomTypeMap;
    private Map<Integer, String> pensionTypeMap;
    private Map<Integer, String> seasonTypeMap;

    private Room room;
    private RoomManager roomManager;

    public RoomView() {
        this.roomManager = new RoomManager();
        this.add(container);
        this.guiInitialize(1000, 700);
        populateHotelComboBox();
        populateRoomTypeComboBox();
        final int[] selectedHotelId = {-1};
        final int[] selectedRoomId = {-1};
        final int[] selectedPensionId = {-1};
        final int[] selectedSeasonId = {-1};

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

                populateSeasonTypeComboBox(selectedHotelId[0]);
                populatePensionTypeComboBox(selectedHotelId[0]);
            }
        });

        cmb_rooms_room_type.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox comboBox = (JComboBox) e.getSource();
                String selectedRoomName = (String) comboBox.getSelectedItem();
                if (selectedRoomName != null && !selectedRoomName.isEmpty()) {
                    for (Map.Entry<Integer, String> entry : roomTypeMap.entrySet()) {
                        if (entry.getValue().equals(selectedRoomName)) {
                            selectedRoomId[0] = entry.getKey();
                            break;
                        }
                    }
                }
            }
        });

        cmb_rooms_pension_type.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox comboBox = (JComboBox) e.getSource();
                String selectedPensionName = (String) comboBox.getSelectedItem();
                if (selectedPensionName != null && !selectedPensionName.isEmpty()) {
                    for (Map.Entry<Integer, String> entry : pensionTypeMap.entrySet()) {
                        if (entry.getValue().equals(selectedPensionName)) {
                            selectedPensionId[0] = entry.getKey();
                            break;
                        }
                    }
                }
            }
        });

        cmb_rooms_season.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox comboBox = (JComboBox) e.getSource();
                String selectedSeasonName = (String) comboBox.getSelectedItem();
                if (selectedSeasonName != null && !selectedSeasonName.isEmpty()) {
                    for (Map.Entry<Integer, String> entry : seasonTypeMap.entrySet()) {
                        if (entry.getValue().equals(selectedSeasonName)) {
                            selectedSeasonId[0] = entry.getKey();
                            break;
                        }
                    }
                }
            }
        });

        this.btn_rooms_submit.addActionListener(e -> {
            if (Utility.isFieldListEmpty(new JTextField[]{this.fld_rooms_child_price, this.fld_rooms_size, this.fld_rooms_beds, this.fld_rooms_stock, this.fld_rooms_adult_price}) || cmb_rooms_hotel_name.getSelectedItem() == null || cmb_rooms_hotel_name.getSelectedItem().toString().isEmpty() || cmb_rooms_room_type.getSelectedItem() == null || cmb_rooms_room_type.getSelectedItem().toString().isEmpty() || cmb_rooms_season.getSelectedItem() == null || cmb_rooms_season.getSelectedItem().toString().isEmpty() || cmb_rooms_pension_type.getSelectedItem() == null || cmb_rooms_pension_type.getSelectedItem().toString().isEmpty()) {
                Utility.showMessage("fill");
            } else {
                boolean result = false;

                int hotelId = selectedHotelId[0];
                int roomType = selectedRoomId[0];
                int pensionType = selectedPensionId[0];
                int seasonId = selectedSeasonId[0];

                int stock = Integer.parseInt(fld_rooms_stock.getText());
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
    }

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

    private void populateRoomTypeComboBox() {
        roomTypeMap = roomManager.getRoomTypesForMap();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("");
        for (Map.Entry<Integer, String> entry : roomTypeMap.entrySet()) {
            model.addElement(entry.getValue());
        }
        cmb_rooms_room_type.setModel(model);
    }

    private void populatePensionTypeComboBox(int selectedHotelId) {
        pensionTypeMap = roomManager.getPensionTypesForMap(selectedHotelId);
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("");
        for (Map.Entry<Integer, String> entry : pensionTypeMap.entrySet()) {
            model.addElement(entry.getValue());
        }
        cmb_rooms_pension_type.setModel(model);
    }

    private void populateSeasonTypeComboBox(int selectedHotelId) {
        seasonTypeMap = roomManager.getSeasonTypesForMap(selectedHotelId);
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("");
        for (Map.Entry<Integer, String> entry : seasonTypeMap.entrySet()) {
            model.addElement(String.valueOf(entry.getValue()));
        }
        cmb_rooms_season.setModel(model);
    }
}