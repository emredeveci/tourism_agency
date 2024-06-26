package core;

import javax.swing.*;

public class Utility {

    public static void setTheme() {
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
            }
        }
    }

    //group of pre-made notification messages
    public static void showMessage(String str) {
        String msg;
        String title;
        switch (str) {
            case "fill":
                msg = "Please fill out all the fields.";
                title = "Error";
                break;
            case "done":
                msg = "Operation has been completed.";
                title = "Success";
                break;
            case "notFound":
                msg = "No matching result(s) in the database";
                title = "Notification";
                break;
            case "error":
                msg = "An error occurred.";
                title = "Error";
                break;
            case "reservation":
                msg = "Your reservation has been made. Stock numbers have been adjusted accordingly.";
                title = "Reservation Successful";
                break;
            case "reservation update":
                msg = "Your reservation has been successfully updated.";
                title = "Update Successful";
                break;
            case "pick a role":
                msg = "Please pick a role to perform a search.";
                title = "Error";
                break;
            case "bed limit":
                msg = "The number of guests cannot exceed the number of beds.";
                title = "Error";
                break;
            case "incomplete search parameters":
                msg = "You need to enter at least one of these: hotel, city, or start AND end dates.";
                title = "Date error";
                break;
            case "The length of stay has to at least be 1 day":
                msg = "The length of stay has to at least be 1 day.";
                title = "Date error";
                break;
            case "no stock":
                msg = "There is not enough stock for this room.";
                title = "Not enough stock";
                break;
            default:
                msg = str;
                title = "Tourism Agency";
        }
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirm(String str) {
        String msg;
        if (str.equals("confirm")) {
            msg = "Are you sure you want to proceed with this operation?";
        } else {
            msg = str;
        }

        return JOptionPane.showConfirmDialog(null, msg, "Confirmation", JOptionPane.YES_NO_OPTION) == 0;
    }

    public static boolean isFieldEmpty(JTextField field) {
        return field.getText().trim().isEmpty();
    }

    public static boolean isFieldListEmpty(JTextField[] fieldList) {
        for (JTextField field : fieldList) {
            if (isFieldEmpty(field)) return true;
        }
        return false;
    }

}
