package com.JavaPOS.Utilities;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;

public enum FXMLForms {

    SHIFTDETAILFORM("/Resources/FXMLs/ShiftDetailForm.fxml"),
    POSFORM("/Resources/FXMLs/POSForm.fxml"),
    AUTHENTICATIONHOLDER("/Resources/FXMLs/AuthenticationHolder.fxml"),
    LOGIN("/Resources/FXMLs/Login.fxml"),
    CONNECT("/Resources/FXMLs/ConnectionSettings.fxml"),
    APPLICATIONHOLDER("/Resources/FXMLs/ApplicationHolder.fxml"),
    USERFORM("/Resources/FXMLs/UserForm.fxml"),
    ITEMFORM("/Resources/FXMLs/ItemForm.fxml"),
    ITEMDETAILS("/Resources/FXMLs/ItemDetails.fxml"),
    PAYMENTFORM("/Resources/FXMLs/PaymentForm.fxml"),
    SEARCH("/Resources/FXMLs/Search.fxml");

    private final String path;

    FXMLForms(String path) {
        this.path = path;
    }

    public Node getNode() {
        Node node = null;
        try {
            node = FXMLLoader.load(getClass().getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return node;
    }
    public FXMLLoader getLoader() throws IOException {
        return new FXMLLoader(getClass().getResource(path));
    }

}
