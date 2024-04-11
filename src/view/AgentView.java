package view;

import business.HotelManager;
import business.UserManager;
import entity.Hotel;
import entity.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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

    private User user;
    private Hotel hotel;
    private HotelManager hotelManager;
    private DefaultTableModel tmdl_hotels = new DefaultTableModel();
    private Object[] col_hotel;

    public AgentView(User user){
        this.add(container);
        this.guiInitialize(1000, 700);
        this.user = user;
        this.hotelManager = new HotelManager();

        if(this.user == null){
            dispose();
        }

        this.lbl_greeting.setText("Welcome, " + this.user.getUsername());

        loadComponent();

        //Tab: Hotels
        loadHotelTable(null);

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

    private void loadHotelTable(List<Object[]> hotelList){
        col_hotel = new Object[]{"ID", "Hotel", "City", "District", "Star", "E-mail", "Phone", "Address"};
        if(hotelList == null){
            hotelList = this.hotelManager.getForTable(col_hotel.length, this.hotelManager.findAll());
        }

        createTable(this.tmdl_hotels, this.tbl_hotels, col_hotel, hotelList);
    }

}
