package view;

import business.UserManager;
import core.Utility;
import entity.User;

import javax.rmi.CORBA.Util;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Objects;

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
        this.guiInitialize(300, 300);

        btn_login.addActionListener(e -> handleLogin());

        fld_username.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        });

        fld_password.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        });

        btn_login.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        JTextField[] formFieldList = {this.fld_username, this.fld_password};

        if (Utility.isFieldListEmpty(formFieldList)) {
            Utility.showMessage("fill");
        } else {
            User loginUser = this.userManager.findByLogin(this.fld_username.getText(), this.fld_password.getText());
            if (loginUser == null) {
                Utility.showMessage("notFound");
            } else {
                if (Objects.equals(loginUser.getRole(), "admin")) {
                    AdminView adminView = new AdminView(loginUser);
                    dispose();
                } else {
                    AgentView agentView = new AgentView(loginUser);
                    dispose();
                }
            }
        }
    }
}
