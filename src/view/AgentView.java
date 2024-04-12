package view;

import business.HotelManager;
import entity.Hotel;
import entity.User;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    private User user;
    private Hotel hotel;
    private HotelManager hotelManager;
    private DefaultTableModel tmdl_hotels = new DefaultTableModel();
    private DefaultTableModel tmdl_pensions = new DefaultTableModel();
    private DefaultTableModel tmdl_amenities = new DefaultTableModel();
    private DefaultTableModel tmdl_discount_periods = new DefaultTableModel();
    private Object[] col_hotel;
    private Object[] col_pension;
    private Object[] col_amenities;
    private Object[] col_discount;

    public AgentView(User user) {
        this.add(container);
        this.guiInitialize(1000, 700);
        this.user = user;
        this.hotelManager = new HotelManager();

        if (this.user == null) {
            dispose();
        }

        this.lbl_greeting.setText("Welcome, " + this.user.getUsername());

        loadComponent();

        //Tab: Hotels
        loadHotelTable(null);
        loadPensionTable(null, 1);
        loadAmenitiesTable(null, 1);
        loadDiscountPeriodsTable(null, 1);

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
        tableRowSelect(this.tbl_hotels);

        col_hotel = new Object[]{"ID", "Hotel", "City", "District", "Star", "E-mail", "Phone", "Address"};
        if (hotelList == null) {
            hotelList = this.hotelManager.getForTable(col_hotel.length, this.hotelManager.findAll());
        }

        createTable(this.tmdl_hotels, this.tbl_hotels, col_hotel, hotelList);


        tbl_hotels.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = Integer.parseInt(tbl_hotels.getValueAt(tbl_hotels.getSelectedRow(), 0).toString());
                    System.out.println(selectedRow);
                    tbl_hotels.setSelectionBackground(Color.YELLOW); // Set background color of selected row
                    tbl_hotels.setSelectionForeground(Color.BLACK);   // Set foreground color of selected row
                    if (selectedRow != -1) {
                        loadPensionTable(null, selectedRow);
                        loadAmenitiesTable(null, selectedRow);
                        loadDiscountPeriodsTable(null, selectedRow);
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

}
