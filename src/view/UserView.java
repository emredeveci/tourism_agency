package view;

import business.UserManager;
import core.Utility;
import entity.User;

import javax.swing.*;

public class UserView extends Layout {
    private JPanel container;
    private JLabel lbl_title;
    private JLabel lbl_username;
    private JTextField fld_username;
    private JTextField fld_password;
    private JButton btn_submit;
    String[] userRoles = {"admin", "agent"};
    private JComboBox<String> cmb_user_role;
    private UserManager userManager;
    private User user;


    public UserView(User user) {
        this.user = user;
        this.userManager = new UserManager();
        this.add(container);
        this.guiInitialize(300, 250);

        this.cmb_user_role.setModel(new DefaultComboBoxModel<>(userRoles));

        if (user != null) {
            this.fld_username.setText(user.getUsername());
            this.fld_password.setText(user.getPassword());
            this.cmb_user_role.getModel().setSelectedItem(this.user.getRole());
        }

        this.btn_submit.addActionListener(e -> {
            if (Utility.isFieldListEmpty(new JTextField[]{this.fld_username, this.fld_password})) {
                Utility.showMessage("fill");
            } else {
                boolean result = false;
                this.user.setUsername(fld_username.getText());
                this.user.setPassword(fld_password.getText());
                this.user.setRole((String) cmb_user_role.getSelectedItem());

                if (this.user.getUserId() != 0) {
                    result = this.userManager.update(this.user);
                } else {
                    result = this.userManager.save(this.user);
                }

                if (result) {
                    Utility.showMessage("done");
                    dispose();
                } else {
                    Utility.showMessage("error");
                }
            }
        });
    }
}
