package com.JavaPOS.Controllers;

import com.JavaPOS.Interfaces.IThread;
import com.JavaPOS.Utilities.FXMLForms;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AuthenticationHolder {

    @FXML
    MaterialDesignIconView ivClose;
    @FXML
    MaterialDesignIconView ivMinimize;
    @FXML
    MaterialDesignIconView ivConnectionSettings;
    @FXML
    MaterialDesignIconView ivLogin;
    @FXML
    StackPane stpHolder;

    private Node loginNode;
    private Node connectionNode;

    public void initialize() throws  IOException {

        ivLogin.setVisible(false);
        switchNode(FXMLForms.LOGIN);

        ivClose.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });

        ivMinimize.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ((Stage)((Node)event.getSource()).getScene().getWindow()).setIconified(true);
            }
        });

        ivConnectionSettings.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ivConnectionSettings.setVisible(false);
                switchNode(FXMLForms.CONNECT);
                ivLogin.setVisible(true);
            }
        });

        ivLogin.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ivLogin.setVisible(false);
                switchNode(FXMLForms.LOGIN);
                ivConnectionSettings.setVisible(true);
            }
        });

    }

    private void switchNode(FXMLForms fxmlForms) {
        FXMLLoader fxmlLoader;

        stpHolder.getChildren().clear();

        try {

            fxmlLoader = fxmlForms.getLoader();

            switch(fxmlForms) {
                case LOGIN:
                    if (loginNode == null) {
                        loginNode = fxmlLoader.load();
                        Login loginController;
                        loginController = fxmlLoader.getController();
                        loginController.setTaskRunning(new IThread() {
                            @Override
                            public void isTaskRunning(boolean isRunning) {
                                ivConnectionSettings.setDisable(isRunning);

                            }
                        });
                    }
                    stpHolder.getChildren().add(loginNode);
                    break;
                case CONNECT:
                    if (connectionNode == null) {
                        connectionNode = fxmlLoader.load();
                        ConnectionSettings connectionSettingsController;
                        connectionSettingsController = fxmlLoader.getController();
                        connectionSettingsController.setTaskRunning(new IThread() {
                            @Override
                            public void isTaskRunning(boolean isRunning) {
                                ivLogin.setDisable(isRunning);

                            }
                        });
                    }
                    stpHolder.getChildren().add(connectionNode);
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
