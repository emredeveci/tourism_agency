package view;

import business.UserManager;
import com.sun.deploy.ref.Helpers;
import core.Utility;
import entity.User;

import javax.rmi.CORBA.Util;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Objects;

public class AdminView extends Layout {
    private JPanel container;
    private JButton btn_logout;
    private JLabel lbl_greeting;
    private JPanel pnl_top;
    private JTabbedPane tab_menu;
    private JPanel pnl_user;
    private JScrollPane scrl_user;
    private JPanel pnl_user_search;
    private JButton btn_user_search;
    private JButton btn_user_clear;
    private JTable tbl_user;

    private DefaultTableModel tmdl_user = new DefaultTableModel();
    private JPopupMenu user_menu;
    private Object[] col_user;
    String[] userRoles = {"", "admin", "agent"};
    private JComboBox<String> cmb_user_role;

    private User user;
    private UserManager userManager;

    public AdminView(User user) {
        this.userManager = new UserManager();
        this.add(container);
        this.guiInitialize(1000, 500);
        this.user = user;

        if (this.user == null) {
            dispose();
        }

        String name = this.user.getUsername();
        String capitalizedName = name.substring(0, 1).toUpperCase() + name.substring(1);
        this.lbl_greeting.setText("User: " + capitalizedName);

        loadComponent();

        //Tab: Users Tab
        loadUserTable(null);
        loadUserComponent();
        loadUserSearch();
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

    private void loadUserTable(List<Object[]> userList) {
        col_user = new Object[]{"ID", "Username", "Password", "Role"};
        if (userList == null) {
            userList = this.userManager.getForTable(col_user.length, this.userManager.findAll());
        }
        createTable(this.tmdl_user, this.tbl_user, col_user, userList);
    }

    public void loadUserSearch() {
        this.cmb_user_role.setModel(new DefaultComboBoxModel<>(userRoles));

        btn_user_search.addActionListener(e -> {
            if(cmb_user_role.getSelectedItem() != ""){
                List<User> userList = this.userManager.searchForUsers(Objects.requireNonNull(cmb_user_role.getSelectedItem()).toString());
                List<Object[]> userRow = this.userManager.getForTable(this.col_user.length, userList);
                loadUserTable(userRow);
            } else {
                Utility.showMessage("pick a role");
            }
        });
        btn_user_clear.addActionListener(e -> {
            cmb_user_role.setSelectedItem("");
            loadUserTable(null);
        });

    }

    private void loadUserComponent() {
        tableRowSelect(this.tbl_user);
        this.user_menu = new JPopupMenu();

        this.user_menu.add("Add").addActionListener(e -> {
            UserView userView = new UserView(new User(), "add");
            userView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadUserTable(null);
                }
            });
        });

        this.user_menu.add("Update").addActionListener(e -> {
            int selectedUserId = this.getTableSelectedRow(tbl_user, 0);
            UserView userView = new UserView(this.userManager.getById(selectedUserId), "update");
            userView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadUserTable(null);
                }
            });
        });

        this.user_menu.add("Remove").addActionListener(e -> {
            if (Utility.confirm("confirm")) {
                int selectUserId = this.getTableSelectedRow(tbl_user, 0);
                if (this.userManager.delete(selectUserId)) {
                    DefaultTableModel model = (DefaultTableModel) tbl_user.getModel();
                    model.removeRow(tbl_user.getSelectedRow());
                    Utility.showMessage("done");
                } else {
                    Utility.showMessage("error");
                }
            }
        });

        this.tbl_user.setComponentPopupMenu(user_menu);
    }
}
