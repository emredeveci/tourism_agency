package view;

import business.HotelManager;
import business.RoomManager;
import core.Utility;
import entity.Hotel;
import entity.Room;
import entity.User;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class AgentView extends Layout {
    private JPanel container;
    private JPanel pnl_top;
    private JLabel lbl_greeting;
    private JButton btn_logout;
    private JTabbedPane tab_menu;
    private JScrollPane scrl_hotels;
    private JTable tbl_hotels;
    private JScrollPane scrl_pension_types;
    private JTable tbl_pension_types;
    private JTable tbl_amenities;
    private JScrollPane scrl_amenities;
    private JTable tbl_discount_periods;
    private JScrollPane scrl_discount_periods;
    private JScrollPane scrl_rooms;
    private JTable tbl_rooms;
    private JPanel pnl_details;
    private JTable tbl_room_details;
    private JTable tbl_room_features;
    private JScrollPane scrl_room_details;
    private JScrollPane scrl_room_features;
    private JLabel lbl_room_details;
    private JLabel lbl_room_features;

    private User user;
    private Hotel hotel;
    private Room room;
    private HotelManager hotelManager;
    private RoomManager roomManager;
    private DefaultTableModel tmdl_hotels = new DefaultTableModel();
    private DefaultTableModel tmdl_rooms = new DefaultTableModel();
    private DefaultTableModel tmdl_pensions = new DefaultTableModel(new Object[]{"ID", "Pension Type"}, 0);
    private DefaultTableModel tmdl_amenities = new DefaultTableModel(new Object[]{"ID", "Amenity"}, 0);
    private DefaultTableModel tmdl_discount_periods = new DefaultTableModel(new Object[]{"ID", "Start Date", "End Date"}, 0);
    private DefaultTableModel tmdl_room_details = new DefaultTableModel(new Object[]{"Inventory ID", "Bed Capacity", "Room Size (m\u00B2)"}, 0);
    private DefaultTableModel tmdl_room_features = new DefaultTableModel(new Object[]{"Inventory ID", "Room Features"}, 0);
    private JPopupMenu hotel_menu;
    private JPopupMenu room_menu;
    private Object[] col_hotel;
    private Object[] col_pension;
    private Object[] col_amenities;
    private Object[] col_discount;
    private Object[] col_rooms;
    private Object[] col_room_details;
    private Object[] col_room_features;



    public AgentView(User user) {
        this.add(container);
        this.guiInitialize(1000, 700);
        this.user = user;
        this.hotelManager = new HotelManager();
        this.roomManager = new RoomManager();

        if (this.user == null) {
            dispose();
        }

        this.lbl_greeting.setText("Welcome, " + this.user.getUsername());

        //Start the tables empty until aa row is picked
        tbl_pension_types.setModel(tmdl_pensions);
        tbl_amenities.setModel(tmdl_amenities);
        tbl_discount_periods.setModel(tmdl_discount_periods);
        tbl_room_details.setModel(tmdl_room_details);
        tbl_room_features.setModel(tmdl_room_features);

        loadComponent();

        //Tab: Hotels
        loadHotelTable(null);

        //Tab: Rooms
        loadRoomsTable();


    }

    private void loadRoomsTable(){
        tableRowSelect(this.tbl_rooms);

        col_rooms = new Object[]{"Inventory ID", "Hotel", "Room", "Pension", "Season", "Adult Price", "Child Price", "Stock"};
        List<Object[]> roomList = this.roomManager.getForTable(col_rooms.length, this.roomManager.findAll());

        createTable(this.tmdl_rooms, this.tbl_rooms, col_rooms, roomList);

        this.room_menu = new JPopupMenu();
        this.room_menu.add("Remove").addActionListener(e -> {
            if (Utility.confirm("confirm")) {
                int selectInventoryId = this.getTableSelectedRow(tbl_rooms, 0);
                if (this.roomManager.delete(selectInventoryId)) {
                    DefaultTableModel model = (DefaultTableModel) tbl_rooms.getModel();
                    int selectedRow = tbl_rooms.getSelectedRow(); // Get the selected row index before removing
                    model.removeRow(selectedRow);
                    // Ensure that another row is selected to trigger the valueChanged event
                    if (model.getRowCount() > 0) {
                        // If there are rows remaining, select the next row
                        if (selectedRow < model.getRowCount()) {
                            tbl_rooms.setRowSelectionInterval(selectedRow, selectedRow);
                        } else {
                            // If the last row was deleted, select the previous row
                            tbl_rooms.setRowSelectionInterval(selectedRow - 1, selectedRow - 1);
                        }
                    }
                    Utility.showMessage("done");
                } else {
                    Utility.showMessage("error");
                }
            }
        });

//        this.room_menu.add("Update").addActionListener(e -> {
//            RoomView roomView = new RoomView(this.roomManager.getByInventoryId(this.getTableSelectedRow(tbl_rooms, 0)));
//            roomView.addWindowListener(new WindowAdapter() {
//                @Override
//                public void windowClosed(WindowEvent e) {
//                    int selectedInventoryId = getTableSelectedRow(tbl_rooms, 0);
//                    loadRoomsTable();
//                    loadRoomDetailsTable(null, selectedInventoryId);
//                    loadRoomFeaturesTable(null, selectedInventoryId);
//                }
//            });
//        });

        this.room_menu.add("Add").addActionListener(e -> {
            RoomView roomView = new RoomView();
            roomView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadRoomsTable();
                }
            });
        });

        this.tbl_rooms.setComponentPopupMenu(room_menu);

        tbl_rooms.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = tbl_rooms.getSelectedRow();
                    if (selectedRow != -1) {
                        int selectedInventoryId = Integer.parseInt(tbl_rooms.getValueAt(selectedRow, 0).toString());
                        loadRoomDetailsTable(null, selectedInventoryId);
                        loadRoomFeaturesTable(null, selectedInventoryId);
                    }
                }
            }
        });
    }

    private void loadComponent() {
        this.btn_logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                LoginView loginView = new LoginView();
            }
        });
    }

    private void loadHotelTable(List<Object[]> hotelList) {
        loadHotelTable(hotelList, null);
    }

    private void loadHotelTable(List<Object[]> hotelList, Integer selectedHotelId) {
        tableRowSelect(this.tbl_hotels);
        this.hotel_menu = new JPopupMenu();

        col_hotel = new Object[]{"ID", "Hotel", "City", "District", "Star", "E-mail", "Phone", "Address"};
        if (hotelList == null) {
            hotelList = this.hotelManager.getForTable(col_hotel.length, this.hotelManager.findAll());
        }

        createTable(this.tmdl_hotels, this.tbl_hotels, col_hotel, hotelList);

        this.hotel_menu.add("Add").addActionListener(e -> {
            HotelView userView = new HotelView(new Hotel());
            userView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    int selectedHotelId = getTableSelectedRow(tbl_hotels, 0);

                    loadHotelTable(null, selectedHotelId);
                    loadPensionTable(null, selectedHotelId);
                    loadAmenitiesTable(null, selectedHotelId);
                    loadDiscountPeriodsTable(null, selectedHotelId);
                }
            });
        });

        this.hotel_menu.add("Update").addActionListener(e -> {
            HotelView hotelView = new HotelView(this.hotelManager.getById(this.getTableSelectedRow(tbl_hotels, 0)));
            hotelView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    int selectedHotelId = getTableSelectedRow(tbl_hotels, 0);
                    loadHotelTable(null, selectedHotelId);
                    loadPensionTable(null, selectedHotelId);
                    loadAmenitiesTable(null, selectedHotelId);
                    loadDiscountPeriodsTable(null, selectedHotelId);
                }
            });
        });

        this.hotel_menu.add("Remove").addActionListener(e -> {
            if (Utility.confirm("confirm")) {
                int selectUserId = this.getTableSelectedRow(tbl_hotels, 0);
                if (this.hotelManager.delete(selectUserId)) {
                    DefaultTableModel model = (DefaultTableModel) tbl_hotels.getModel();
                    int selectedRow = tbl_hotels.getSelectedRow(); // Get the selected row index before removing
                    model.removeRow(selectedRow);
                    // Ensure that another row is selected to trigger the valueChanged event
                    if (model.getRowCount() > 0) {
                        // If there are rows remaining, select the next row
                        if (selectedRow < model.getRowCount()) {
                            tbl_hotels.setRowSelectionInterval(selectedRow, selectedRow);
                        } else {
                            // If the last row was deleted, select the previous row
                            tbl_hotels.setRowSelectionInterval(selectedRow - 1, selectedRow - 1);
                        }
                    }
                    Utility.showMessage("done");
                } else {
                    Utility.showMessage("error");
                }
            }
        });

        this.tbl_hotels.setComponentPopupMenu(hotel_menu);

        tbl_hotels.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = tbl_hotels.getSelectedRow();
                    if (selectedRow != -1) {
                        int selectedHotelId = Integer.parseInt(tbl_hotels.getValueAt(selectedRow, 0).toString());
                        loadPensionTable(null, selectedHotelId);
                        loadAmenitiesTable(null, selectedHotelId);
                        loadDiscountPeriodsTable(null, selectedHotelId);
                    }
                }
            }
        });
    }

    private void loadPensionTable(List<Object[]> pensionList, int selectedRow) {
        col_pension = new Object[]{"ID", "Pension Type"};
        if (pensionList == null) {
            List<Object[]> pensionTypes = this.hotelManager.findAllPensions(selectedRow);
            createTable(this.tmdl_pensions, this.tbl_pension_types, col_pension, pensionTypes);
        }
    }

    private void loadAmenitiesTable(List<Object[]> amenitiesList, int selectedRow) {
        col_amenities = new Object[]{"ID", "Amenity"};
        if (amenitiesList == null) {
            List<Object[]> amenities = this.hotelManager.findAllAmenities(selectedRow);
            createTable(this.tmdl_amenities, this.tbl_amenities, col_amenities, amenities);
        }
    }

    private void loadDiscountPeriodsTable(List<Object[]> discountPeriodsList, int selectedRow) {
        col_discount = new Object[]{"ID", "Start Date", "End Date"};
        if (discountPeriodsList == null) {
            List<Object[]> discountPeriods = this.hotelManager.findDiscountPeriods(selectedRow);
            createTable(this.tmdl_discount_periods, this.tbl_discount_periods, col_discount, discountPeriods);
        }
    }

    private void loadRoomDetailsTable(List<Object> roomDetailsList, int selectedInventoryId){
        col_room_details = new Object[]{"Inventory ID", "Bed Capacity", "Room Size (m\u00B2)"};
        if(roomDetailsList == null){
            List<Object[]> roomDetails = this.roomManager.findAllRoomDetails(selectedInventoryId);
            createTable(this.tmdl_room_details, this.tbl_room_details, col_room_details, roomDetails);
        }
    }

    private void loadRoomFeaturesTable(List<Object> roomFeaturesList, int selectedInventoryId){
        col_room_features = new Object[]{"Inventory ID", "Room Features"};
        if(roomFeaturesList == null){
            List<Object[]> roomFeatures = this.roomManager.findAllRoomFeatures(selectedInventoryId);
            createTable(this.tmdl_room_features, this.tbl_room_features, col_room_features, roomFeatures);
        }
    }

}
