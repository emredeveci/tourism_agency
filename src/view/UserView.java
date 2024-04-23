package view;

import business.UserManager;
import core.Utility;
import entity.User;

import javax.swing.*;
import java.util.Objects;

public class UserView extends Layout {
    //CRITERIA 1
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


    public UserView(User user, String purpose) {
        this.user = user;
        this.userManager = new UserManager();
        this.add(container);
        this.guiInitialize(400, 350);

        this.cmb_user_role.setModel(new DefaultComboBoxModel<>(userRoles));


        if (Objects.equals(purpose, "update")) {
            this.lbl_title.setText("Update User");
            this.fld_username.setText(user.getUsername());
            this.fld_password.setText(user.getPassword());
            this.cmb_user_role.getModel().setSelectedItem(this.user.getRole());
        } else {
            this.lbl_title.setText("Add User");
        }

        this.btn_submit.addActionListener(e -> {
            if (Utility.isFieldListEmpty(new JTextField[]{this.fld_username, this.fld_password})) {
                //CRITERIA 25
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
