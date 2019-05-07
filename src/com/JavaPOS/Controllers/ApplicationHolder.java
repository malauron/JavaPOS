package com.JavaPOS.Controllers;

import com.JavaPOS.DataAccessObjects.Shiftdetails;
import com.JavaPOS.DataModels.Shiftdetail;
import com.JavaPOS.Interfaces.IShiftDetails;
import com.JavaPOS.Interfaces.IThread;
import com.JavaPOS.Utilities.ConnectionMain;
import com.JavaPOS.Utilities.CurrentUser;
import com.JavaPOS.Utilities.FXMLForms;
import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ApplicationHolder implements Initializable {

    @FXML
    private JFXButton btnClose;
    @FXML
    private JFXButton btnMinimize;
    @FXML
    private JFXButton btnMenu;
    @FXML
    private JFXButton btnHideMenu;
    @FXML
    private JFXButton btnMainForm;
    @FXML
    private VBox vbxMenu;
    @FXML
    private JFXButton btnSupplier;
    @FXML
    private JFXButton btnUser;
    @FXML
    private JFXButton btnItem;
    @FXML
    private JFXButton btnBillingsProcess;
    @FXML
    private JFXButton btnChecksInTransit;
    @FXML
    private JFXButton btnChecksReleasing;
    @FXML
    private JFXButton btnChecksEncashment;
    @FXML
    private JFXButton btnChecksStatus;
    @FXML
    private JFXButton btnMSSQLConnection;
    @FXML
    private JFXButton btnPSQLConnection;
    @FXML
    private AnchorPane appHolder;
    @FXML
    private Label lblUsername;
    @FXML
    private Label lblServer;
    @FXML
    private Label lblDatabase;
    @FXML
    private ImageView imvUserPhoto;

    FadeTransition ft1;
    TranslateTransition animateOpenMenu;
    TranslateTransition animateCloseMenu;
    BoxBlur boxBlur = new BoxBlur(2,2,2);
    private Node posFormNode;
    private Node shiftDetailNode;
    private Node userNode;
//    private Node supplierNode;
    private Node itemNode;
//    private Node billingsProcessNode;
//    private Node checksInTransitNode;
//    private Node checksReleasingNode;
//    private Node checksEncashmentNode;
//    private Node checksStatusNode;
//    private Node mssqlConnectionNode;
//    private Node psqlConnectionNode;
    private IShiftDetails shiftdetails;
    private Shiftdetail shiftdetail;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Initialize
        shiftdetails = new Shiftdetails();
        shiftdetail = shiftdetails.getShiftDetail(CurrentUser.getInstance().getUserID());
        if (shiftdetail == null) {
            shiftdetail = shiftdetails.handleShiftDetail(shiftdetail);
            System.out.println(shiftdetail.toString());
        }
        ft1 = new FadeTransition(Duration.seconds(0.5),vbxMenu);
        animateOpenMenu = new TranslateTransition(new Duration(350), vbxMenu);
        animateCloseMenu = new TranslateTransition(new Duration(350), vbxMenu);

        Circle circle = new Circle(25.0,25.0,25.0);
        imvUserPhoto.setClip(circle);
        imvUserPhoto.setPreserveRatio(false);
        imvUserPhoto.setImage(CurrentUser.getInstance().getUserPhoto());
        lblUsername.setText("User: " + CurrentUser.getInstance().getUserName().toUpperCase());

        try {
            lblServer.setText("Server: " + ConnectionMain.getInstance().getServer().toUpperCase());
            lblDatabase.setText("Database: " + ConnectionMain.getInstance().getDatabase().toUpperCase());
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnHideMenu.setVisible(false);

        btnClose.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });

        btnMinimize.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ((Stage)((Node)event.getSource()).getScene().getWindow()).setIconified(true);
            }
        });

        // Check if has shiftdetails
        if (shiftdetail != null) {
            posFormNode = FXMLForms.POSFORM.getNode();
            appHolder.getChildren().add(posFormNode);
        } else {
            shiftDetailNode = FXMLForms.SHIFTDETAILFORM.getNode();
            appHolder.getChildren().add(shiftDetailNode);
        }



        animateOpenMenu.setToX(0);
        vbxMenu.setVisible(false);
        vbxMenu.setTranslateX(-220);
        btnMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (vbxMenu.getTranslateX() != 0) {
                    fadeInMenu();
                }
            }
        });

        btnHideMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (vbxMenu.getTranslateX() == 0) {
                    fadeOutMenu();
//                    appHolder.setDisable(false);
//                    animateCloseMenu.setToX(-(vbxMenu.getWidth()));
//                    animateCloseMenu.play();
//                    btnHideMenu.setVisible(false);
//                    btnMenu.setVisible(true);
//                    appHolder.setEffect(null);
                }
            }
        });

        btnMainForm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(btnHideMenu.isVisible()) {
                    fadeOutMenu();
                }
                if (shiftdetail != null) {
                    switchNode(FXMLForms.POSFORM);
                } else {
                    switchNode(FXMLForms.SHIFTDETAILFORM);
                }
            }
        });

//        btnSupplier.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                switchNode(FXMLPath.SUPPLIERFORM);
//                appHolder.setDisable(false);
//                animateCloseMenu.setToX(-(vbxMenu.getWidth()));
//                animateCloseMenu.play();
//                btnHideMenu.setVisible(false);
//                btnMenu.setVisible(true);
//                appHolder.setEffect(null);
//            }
//        });

        btnUser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                fadeOutMenu();
                switchNode(FXMLForms.USERFORM);
//                appHolder.setDisable(false);
//                animateCloseMenu.setToX(-(vbxMenu.getWidth()));
//                animateCloseMenu.play();
//                btnHideMenu.setVisible(false);
//                btnMenu.setVisible(true);
//                appHolder.setEffect(null);
            }
        });

        btnItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                fadeOutMenu();
                switchNode(FXMLForms.ITEMFORM);
            }
        });

//        btnBillingsProcess.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                switchNode(FXMLPath.BILLINGSPROCESS);
//                appHolder.setDisable(false);
//                animateCloseMenu.setToX(-(vbxMenu.getWidth()));
//                animateCloseMenu.play();
//                btnHideMenu.setVisible(false);
//                btnMenu.setVisible(true);
//                appHolder.setEffect(null);
//            }
//        });

//        btnChecksInTransit.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                switchNode(FXMLPath.CHECKSINTRANSIT);
//                appHolder.setDisable(false);
//                animateCloseMenu.setToX(-(vbxMenu.getWidth()));
//                animateCloseMenu.play();
//                btnHideMenu.setVisible(false);
//                btnMenu.setVisible(true);
//                appHolder.setEffect(null);
//            }
//        });

//        btnChecksReleasing.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                switchNode(FXMLPath.CHECKSRELEASING);
//                appHolder.setDisable(false);
//                animateCloseMenu.setToX(-(vbxMenu.getWidth()));
//                animateCloseMenu.play();
//                btnHideMenu.setVisible(false);
//                btnMenu.setVisible(true);
//                appHolder.setEffect(null);
//            }
//        });

//        btnChecksEncashment.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                switchNode(FXMLPath.CHECKSENCASHMENT);
//                appHolder.setDisable(false);
//                animateCloseMenu.setToX(-(vbxMenu.getWidth()));
//                animateCloseMenu.play();
//                btnHideMenu.setVisible(false);
//                btnMenu.setVisible(true);
//                appHolder.setEffect(null);
//            }
//        });

//        btnChecksStatus.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                switchNode(FXMLPath.CHECKSSTATUS);
//                appHolder.setDisable(false);
//                animateCloseMenu.setToX(-(vbxMenu.getWidth()));
//                animateCloseMenu.play();
//                btnHideMenu.setVisible(false);
//                btnMenu.setVisible(true);
//                appHolder.setEffect(null);
//            }
//        });

//        btnMSSQLConnection.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                switchNode(FXMLPath.MSSQLCONNECTION);
//                appHolder.setDisable(false);
//                animateCloseMenu.setToX(-(vbxMenu.getWidth()));
//                animateCloseMenu.play();
//                btnHideMenu.setVisible(false);
//                btnMenu.setVisible(true);
//                appHolder.setEffect(null);
//            }
//        });

//        btnPSQLConnection.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                switchNode(FXMLPath.PSQLCONNECTIONS);
//                appHolder.setDisable(false);
//                animateCloseMenu.setToX(-(vbxMenu.getWidth()));
//                animateCloseMenu.play();
//                btnHideMenu.setVisible(false);
//                btnMenu.setVisible(true);
//                appHolder.setEffect(null);
//            }
//        });

    }

    private void switchNode(FXMLForms fxmlForms) {
        FXMLLoader fxmlLoader;

        appHolder.getChildren().clear();

        try {

            fxmlLoader = fxmlForms.getLoader();

            switch(fxmlForms) {
                case SHIFTDETAILFORM:
                    shiftDetailNode = fxmlLoader.load();
                    appHolder.getChildren().add(shiftDetailNode);
                    break;
                case POSFORM:
                    posFormNode = fxmlLoader.load();
                    appHolder.getChildren().add(posFormNode);
                    break;
                case USERFORM:
                    if (userNode == null) {
                        userNode = fxmlLoader.load();
                        UserForm userForm;
                        userForm = fxmlLoader.getController();
                        userForm.setTaskRunning(new IThread() {
                            @Override
                            public void isTaskRunning(boolean isRunning) {
                                btnMenu.setDisable(isRunning);
                                btnClose.setDisable(isRunning);
                            }
                        });
                    }
                    appHolder.getChildren().add(userNode);
                    break;
//                case SUPPLIERFORM:
//                    if (supplierNode == null) {
//                        supplierNode = fxmlLoader.load();
//                        SupplierForm supplierForm;
//                        supplierForm = fxmlLoader.getController();
//                        supplierForm.setTaskRunning(new IThread() {
//                            @Override
//                            public void isTaskRunning(boolean isRunning) {
//                                btnMenu.setDisable(isRunning);
//                                btnClose.setDisable(isRunning);
//                            }
//                        });
//                    }
//                    appHolder.getChildren().add(supplierNode);
//                    break;
                case ITEMFORM:
                    if (itemNode == null) {
                        itemNode = fxmlLoader.load();
                        ItemForm itemForm;
                        itemForm = fxmlLoader.getController();
                        itemForm.setTaskRunning(new IThread() {
                            @Override
                            public void isTaskRunning(boolean isRunning) {
                                btnMenu.setDisable(isRunning);
                                btnClose.setDisable(isRunning);
                            }
                        });
                    }
                    appHolder.getChildren().add(itemNode);
                    break;
//                case BILLINGSPROCESS:
//                    if (billingsProcessNode == null) {
//                        billingsProcessNode = fxmlLoader.load();
//                    }
//                    appHolder.getChildren().add(billingsProcessNode);
//                    break;
//                case CHECKSINTRANSIT:
//                    if (checksInTransitNode == null) {
//                        checksInTransitNode = fxmlLoader.load();
//                        ChecksInTransit checksInTransit;
//                        checksInTransit = fxmlLoader.getController();
//                        checksInTransit.setTaskRunning(new IThread() {
//                            @Override
//                            public void isTaskRunning(boolean isRunning) {
//                                btnMenu.setDisable(isRunning);
//                                btnClose.setDisable(isRunning);
//                            }
//                        });
//                    }
//                    appHolder.getChildren().add(checksInTransitNode);
//                    break;
//                case CHECKSRELEASING:
//                    if (checksReleasingNode == null) {
//                        ChecksOutgoing checksOutgoing = new ChecksOutgoing(CheckStatus.OUTSTANDING);
//                        checksOutgoing.setTaskRunning(new IThread() {
//                            @Override
//                            public void isTaskRunning(boolean isRunning) {
//                                btnMenu.setDisable(isRunning);
//                                btnClose.setDisable(isRunning);
//                            }
//                        });
//                        fxmlLoader.setController(checksOutgoing);
//                        checksReleasingNode = fxmlLoader.load();
//
//                    }
//                    appHolder.getChildren().add(checksReleasingNode);
//                    break;
//                case CHECKSENCASHMENT:
//                    if (checksEncashmentNode == null) {
//                        ChecksOutgoing checksOutgoing = new ChecksOutgoing(CheckStatus.ENCASHED);
//                        checksOutgoing.setTaskRunning(new IThread() {
//                            @Override
//                            public void isTaskRunning(boolean isRunning) {
//                                btnMenu.setDisable(isRunning);
//                                btnClose.setDisable(isRunning);
//                            }
//                        });
//                        fxmlLoader.setController(checksOutgoing);
//                        checksEncashmentNode = fxmlLoader.load();
//
//                    }
//                    appHolder.getChildren().add(checksEncashmentNode);
//                    break;
//                case CHECKSSTATUS:
//                    if (checksStatusNode == null) {
//                        checksStatusNode = fxmlLoader.load();
//                        CheckVouchersStatus checkVouchersStatus = fxmlLoader.getController();
//                    }
//                    appHolder.getChildren().add(checksStatusNode);
//                    break;
//                case MSSQLCONNECTION:
//                    if (mssqlConnectionNode == null) {
//                        mssqlConnectionNode = fxmlLoader.load();
//                        MSSQLConnectionSettings mssqlConnectionSettings = fxmlLoader.getController();
//                        mssqlConnectionSettings.setTaskRunning(new IThread() {
//                            @Override
//                            public void isTaskRunning(boolean isRunning) {
//                                btnMenu.setDisable(isRunning);
//                                btnClose.setDisable(isRunning);
//                            }
//                        });
//                    }
//                    appHolder.getChildren().add(mssqlConnectionNode);
//                    break;
//                case PSQLCONNECTIONS:
//                    if (psqlConnectionNode == null) {
//                        psqlConnectionNode = fxmlLoader.load();
//                        PSQLConnectionSettings psqlConnectionSettings = fxmlLoader.getController();
//                        psqlConnectionSettings.setTaskRunning(new IThread() {
//                            @Override
//                            public void isTaskRunning(boolean isRunning) {
//                                btnMenu.setDisable(isRunning);
//                                btnClose.setDisable(isRunning);
//                            }
//                        });
//                    }
//                    appHolder.getChildren().add(psqlConnectionNode);
//                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void fadeInMenu () {
        vbxMenu.setVisible(true);
        ft1.setToValue(1);
        ft1.setFromValue(0);
        ft1.play();
        appHolder.setEffect(boxBlur);
        appHolder.setDisable(true);
        animateOpenMenu.play();
        btnMenu.setVisible(false);
        btnHideMenu.setVisible(true);
    }

    private void fadeOutMenu () {
        ft1.setToValue(0);
        ft1.setFromValue(1);
        ft1.play();
        appHolder.setDisable(false);
        animateCloseMenu.setToX(-(vbxMenu.getWidth()));
        animateCloseMenu.play();
        btnHideMenu.setVisible(false);
        btnMenu.setVisible(true);
        appHolder.setEffect(null);
    }


}
