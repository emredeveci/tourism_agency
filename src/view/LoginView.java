package view;

import business.UserManager;
import entity.User;

import javax.swing.*;

public class LoginView extends Layout {
    private JPanel container;
    private JPanel login_title;
    private JPanel login_body;
    private JLabel lbl_username;
    private JLabel lbl_password;
    private JTextField fld_username;
    private JTextField fld_password;
    private JButton btn_login;

    private final UserManager userManager;

    public LoginView() {
        this.userManager = new UserManager();
        this.add(container);
        this.guiInitialize(300,300);

        btn_login.addActionListener(e -> {
            JTextField[] formFieldList = {this.fld_username, this.fld_password};
            User loginUser = this.userManager.findByLogin(this.fld_username.getText(), this.fld_password.getText());
            if(loginUser == null){
                System.out.println("null");
            } else {
                System.out.println("success");
            }
        });
    }
}
