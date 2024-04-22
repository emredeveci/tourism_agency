package view;

import business.HotelManager;
import business.ReservationManager;
import business.RoomManager;
import core.Utility;
import entity.Hotel;
import entity.Reservation;
import entity.Room;
import entity.User;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    private JButton btn_rooms_search;
    private JButton btn_rooms_clear;
    private JButton btn_rooms_add;
    private JComboBox cmb_rooms_hotel;
    private JComboBox cmb_rooms_city;
    private JTextField fld_rooms_start_date;
    private JTextField fld_rooms_end_date;
    private JTextField fld_rooms_adults;
    private JTextField fld_rooms_children;
    private JTable tbl_reservations;
    private JScrollPane scrl_reservations;
    private JScrollPane scrl_reservation_details;
    private JTable tbl_reservation_details;
    private JTable tbl_guest_details;
    private JScrollPane scrl_guest_details;
    private JPanel pnl_room_search;

    private User user;
    private Hotel hotel;
    private Room room;
    private HotelManager hotelManager;
    private RoomManager roomManager;
    private ReservationManager reservationManager;
    private DefaultTableModel tmdl_hotels = new DefaultTableModel();
    private DefaultTableModel tmdl_rooms = new DefaultTableModel();
    private DefaultTableModel tmdl_reservations = new DefaultTableModel();
    private DefaultTableModel tmdl_pensions = new DefaultTableModel(new Object[]{"ID", "Pension Type"}, 0);
    private DefaultTableModel tmdl_amenities = new DefaultTableModel(new Object[]{"ID", "Amenity"}, 0);
    private DefaultTableModel tmdl_discount_periods = new DefaultTableModel(new Object[]{"ID", "Start Date", "End Date"}, 0);
    private DefaultTableModel tmdl_room_details = new DefaultTableModel(new Object[]{"Inventory ID", "Bed Capacity", "Room Size (m\u00B2)"}, 0);
    private DefaultTableModel tmdl_room_features = new DefaultTableModel(new Object[]{"Inventory ID", "Room Features"}, 0);
    private DefaultTableModel tmdl_reservation_details = new DefaultTableModel(new Object[]{"Pension Type", "Room Type", "Adult", "Children"}, 0);
    private DefaultTableModel tmdl_contact_details = new DefaultTableModel(new Object[]{"Name", "ID", "Phone", "Email"}, 0);
    private JPopupMenu hotel_menu;
    private JPopupMenu room_menu;
    private JPopupMenu reservation_menu;
    private Object[] col_hotel;
    private Object[] col_pension;
    private Object[] col_amenities;
    private Object[] col_discount;
    private Object[] col_rooms;
    private Object[] col_room_details;
    private Object[] col_room_features;
    private Object[] col_reservations;
    private Object[] col_reservation_details;
    private Object[] col_reservation_contact;
    private Map<Integer, String> hotelMap;
    private Map<Integer, String> cityMap;


    public AgentView(User user) {
        this.add(container);
        this.guiInitialize(1000, 700);
        this.user = user;
        this.hotelManager = new HotelManager();
        this.roomManager = new RoomManager();
        this.reservationManager = new ReservationManager();

        if (this.user == null) {
            dispose();
        }

        //Start the tables empty until aa row is picked
        tbl_pension_types.setModel(tmdl_pensions);
        tbl_amenities.setModel(tmdl_amenities);
        tbl_discount_periods.setModel(tmdl_discount_periods);
        tbl_room_details.setModel(tmdl_room_details);
        tbl_room_features.setModel(tmdl_room_features);
        tbl_reservation_details.setModel(tmdl_reservation_details);
        tbl_guest_details.setModel(tmdl_contact_details);

        this.lbl_greeting.setText("Welcome, " + this.user.getUsername());

        loadComponent();

        //Tab: Hotels
        loadHotelTable(null);

        //Tab: Rooms
        loadRoomsTable(null, "start");
        populateHotelComboBoxForRoomSearch();
        populateCityComboBoxForRoomSearch(null);
        final Integer[] selectedHotelId = {-1};
        final Integer[] selectedCityId = {-1};

        //Tab: Reservations
        loadReservationsTable();

        cmb_rooms_hotel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox comboBox = (JComboBox) e.getSource();
                String selectedHotelName = (String) comboBox.getSelectedItem();
                if (selectedHotelName != null && !selectedHotelName.isEmpty()) {
                    for (Map.Entry<Integer, String> entry : hotelMap.entrySet()) {
                        if (entry.getValue().equals(selectedHotelName)) {
                            selectedHotelId[0] = entry.getKey();
                            break;
                        }
                    }
                } else {
                    // Empty row selected, set selectedHotelId to null
                    selectedHotelId[0] = null;
                }

                populateCityComboBoxForRoomSearch(selectedHotelId[0]);
            }
        });

        cmb_rooms_city.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox comboBox = (JComboBox) e.getSource();
                String selectedCityName = (String) comboBox.getSelectedItem();
            }
        });

        this.btn_rooms_search.addActionListener(e -> {
            if (cmb_rooms_city.getSelectedItem() == null || cmb_rooms_hotel.getSelectedItem() == null || (fld_rooms_start_date.getText().trim().isEmpty() && fld_rooms_end_date.getText().trim().isEmpty())) {
                String hotelName = null;
                String cityName = null;
                String startDateString = fld_rooms_start_date.getText().trim();
                String endDateString = fld_rooms_end_date.getText().trim();
                LocalDate startDate = null;
                LocalDate endDate = null;
                Integer adultCount = null;
                Integer childCount = null;
                if (!(cmb_rooms_hotel.getSelectedItem() == null) && !(((String) cmb_rooms_hotel.getSelectedItem()).isEmpty())) {
                    hotelName = (String) cmb_rooms_hotel.getSelectedItem();
                }
                if (!(cmb_rooms_city.getSelectedItem() == null) && !(((String) cmb_rooms_city.getSelectedItem()).isEmpty())) {
                    cityName = (String) cmb_rooms_city.getSelectedItem();
                }
                if (!startDateString.isEmpty()) {
                    startDate = LocalDate.parse(startDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                }
                if (!endDateString.isEmpty()) {
                    endDate = LocalDate.parse(endDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                }
                if (!(fld_rooms_adults.getText() == null) && !fld_rooms_adults.getText().isEmpty()) {
                    adultCount = Integer.parseInt(fld_rooms_adults.getText());
                }
                if (!(fld_rooms_children.getText() == null) && !fld_rooms_children.getText().isEmpty()) {
                    childCount = Integer.parseInt(fld_rooms_children.getText());
                }

                List<Room> searchResult = this.roomManager.getForRoomSearch(hotelName, cityName, startDate, endDate, adultCount, childCount);

                if (!searchResult.isEmpty()) {
                    loadRoomsTable(searchResult, "update");
                } else {
                    Utility.showMessage("error");
                }
            } else {
                Utility.showMessage("fill");
            }
        });

        this.btn_rooms_clear.addActionListener(e -> {
            loadRoomsTable(null, "update");
            cmb_rooms_hotel.setSelectedItem(null);
            cmb_rooms_city.setSelectedItem(null);
            fld_rooms_start_date.setText(null);
            fld_rooms_end_date.setText(null);
            fld_rooms_adults.setText(null);
            fld_rooms_children.setText(null);
        });

    }

    private void loadRoomsTable(List<Room> roomList, String purpose) {
        tableRowSelect(this.tbl_rooms);
        List<Object[]> rooms;
        col_rooms = new Object[]{"Inventory ID", "Hotel", "City", "Room", "Pension", "Season", "Adult Price", "Child Price", "Stock"};
        if (roomList == null) {
            rooms = this.roomManager.getForTable(col_rooms.length, this.roomManager.findAll());
        } else {
            rooms = this.roomManager.getForTable(col_rooms.length, roomList);
        }

        createTable(this.tmdl_rooms, this.tbl_rooms, col_rooms, rooms);

        this.room_menu = new JPopupMenu();

        if(Objects.equals(purpose, "start")){

            this.room_menu.add("Add").addActionListener(e -> {
                RoomView roomView = new RoomView();
                roomView.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        loadRoomsTable(null, "update");
                    }
                });
            });

            this.room_menu.add("Reserve").addActionListener(e -> {
                int selectedRow = tbl_rooms.getSelectedRow();
                int reservationId = 0;
                if (selectedRow != -1) {
                    reservationId = Integer.parseInt(tbl_rooms.getValueAt(selectedRow, 0).toString());
                }
                ReservationView reservationView = new ReservationView(reservationId, "reserve", null);
                reservationView.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        loadReservationsTable();
                        loadRoomsTable(null, "update");
                    }
                });
            });

            this.btn_rooms_add.addActionListener(e -> {
                RoomView roomView = new RoomView();
                roomView.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        loadRoomsTable(null, "update");
                    }
                });
            });

            this.room_menu.add("Delete").addActionListener(e -> {
                if (Utility.confirm("confirm")) {
                    int selectInventoryId = this.getTableSelectedRow(tbl_rooms, 0);
                    if (this.roomManager.delete(selectInventoryId)) {
                        loadRoomsTable(null, "update");
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

        this.hotel_menu.add("Delete").addActionListener(e -> {
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

    private void loadReservationsTable() {
        tableRowSelect(this.tbl_reservations);

        col_reservations = new Object[]{"Reservation ID", "Hotel", "City", "Start Date", "End Date", "Total Cost"};

        List<Object[]> reservationList = this.reservationManager.getForTable(col_reservations.length, this.reservationManager.findAll());

        this.createTable(this.tmdl_reservations, this.tbl_reservations, col_reservations, reservationList);

        this.reservation_menu = new JPopupMenu();

        this.reservation_menu.add("Remove").addActionListener(e -> {
            if (Utility.confirm("confirm")) {
                int selectedReservationId = this.getTableSelectedRow(tbl_reservations, 0);
                int selectedInventoryId = this.reservationManager.findInventoryId(selectedReservationId);
                if (this.reservationManager.deleteReservation(selectedReservationId) && this.reservationManager.adjustInventoryAfterRemove(selectedInventoryId)) {
                    DefaultTableModel model = (DefaultTableModel) tbl_reservations.getModel();
                    int selectedRow = tbl_reservations.getSelectedRow(); // Get the selected row index before removing
                    model.removeRow(selectedRow);
                    // Ensure that another row is selected to trigger the valueChanged event
                    if (model.getRowCount() > 0) {
                        // If there are rows remaining, select the next row
                        if (selectedRow < model.getRowCount()) {
                            tbl_reservations.setRowSelectionInterval(selectedRow, selectedRow);
                        } else {
                            // If the last row was deleted, select the previous row
                            tbl_reservations.setRowSelectionInterval(selectedRow - 1, selectedRow - 1);
                        }
                    }
                    Utility.showMessage("done");
                    loadReservationsTable();
                    loadRoomsTable(null, "update");
                } else {
                    Utility.showMessage("error");
                }
            }
        });

        this.reservation_menu.add("Update").addActionListener(e -> {
            int selectedReservationId = this.getTableSelectedRow(tbl_reservations, 0);
            int selectedInventoryId = this.reservationManager.findInventoryId(selectedReservationId);
            ReservationView reservationView = new ReservationView(selectedInventoryId, "update", this.reservationManager.findByReservationId(selectedReservationId));
            reservationView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadReservationsTable();
                    loadRoomsTable(null, "update");
                }
            });
        });

        this.tbl_reservations.setComponentPopupMenu(reservation_menu);

        tbl_reservations.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = tbl_reservations.getSelectedRow();
                    if (selectedRow != -1) {
                        int selectedReservationId = Integer.parseInt(tbl_reservations.getValueAt(selectedRow, 0).toString());
                        loadReservationDetailsTable(null, selectedReservationId);
                        loadGuestDetailsTable(null, selectedReservationId);
                    }
                }
            }
        });
    }

    private void loadReservationDetailsTable(List<Object[]> reservationsDetails, int selectedRow) {
        col_reservation_details = new Object[]{"Pension Type", "Room Type", "Adult", "Children"};
        if (reservationsDetails == null) {
            List<Object[]> details = this.reservationManager.findAllReservationDetails(selectedRow);
            createTable(this.tmdl_reservation_details, this.tbl_reservation_details, col_reservation_details, details);
        }
    }

    private void loadGuestDetailsTable(List<Object[]> guestDetails, int selectedRow) {
        col_reservation_contact = new Object[]{"Name", "ID Number", "Phone", "Email"};
        if (guestDetails == null) {
            List<Object[]> details = this.reservationManager.findAllGuestDetails(selectedRow);
            createTable(this.tmdl_contact_details, this.tbl_guest_details, col_reservation_contact, details);
        }
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

    private void loadRoomDetailsTable(List<Object> roomDetailsList, int selectedInventoryId) {
        col_room_details = new Object[]{"Inventory ID", "Bed Capacity", "Room Size (m\u00B2)"};
        if (roomDetailsList == null) {
            List<Object[]> roomDetails = this.roomManager.findAllRoomDetails(selectedInventoryId);
            createTable(this.tmdl_room_details, this.tbl_room_details, col_room_details, roomDetails);
        }
    }

    private void loadRoomFeaturesTable(List<Object> roomFeaturesList, int selectedInventoryId) {
        col_room_features = new Object[]{"Inventory ID", "Room Features"};
        if (roomFeaturesList == null) {
            List<Object[]> roomFeatures = this.roomManager.findAllRoomFeatures(selectedInventoryId);
            createTable(this.tmdl_room_features, this.tbl_room_features, col_room_features, roomFeatures);
        }
    }

    private void populateHotelComboBoxForRoomSearch() {
        hotelMap = roomManager.getHotelNames();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("");
        for (Map.Entry<Integer, String> entry : hotelMap.entrySet()) {
            model.addElement(entry.getValue());
        }
        cmb_rooms_hotel.setModel(model);
    }

    private void populateCityComboBoxForRoomSearch(Integer selectedHotelId) {
        cityMap = roomManager.getCityNames(selectedHotelId);
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("");
        for (Map.Entry<Integer, String> entry : cityMap.entrySet()) {
            model.addElement(entry.getValue());
        }
        cmb_rooms_city.setModel(model);
    }


}
