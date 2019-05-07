package com.JavaPOS.Controllers;

import com.JavaPOS.DataAccessObjects.Items;
import com.JavaPOS.DataModels.Item;
import com.JavaPOS.DataModels.POSItemAssembly;
import com.JavaPOS.Interfaces.IItemSearch;
import com.JavaPOS.Interfaces.IItems;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class ItemSearch {

  @FXML
  private JFXTextField txtSearch;
  @FXML
  private JFXButton btnSelect;
  @FXML
  private JFXButton btnBack;
  @FXML
  private TableView<Item> tblSearch;
  @FXML
  private JFXProgressBar pbrIndicator;

  private TableColumn<Item, Image> colItemPhoto = new TableColumn<>("Photo");
  private TableColumn<Item, String> colItemName = new TableColumn<>("Description");

  private IItems iItems = new Items();
  private IItemSearch iItemSearch;
  private POSItemAssembly posItemAssembly;

  public ItemSearch() {
    posItemAssembly = null;
  }

  public ItemSearch(POSItemAssembly posItemAssembly) {
    this.posItemAssembly = posItemAssembly;
  }

  public void initialize() {

    colItemPhoto.getStyleClass().add("colItemPhoto");
    pbrIndicator.setVisible(false);
    btnSelect.setOnAction(event -> {
      if(iItemSearch != null) {
        if(tblSearch.getSelectionModel().getSelectedItem() != null) {
          iItemSearch.getSelectedItem(tblSearch.getSelectionModel().getSelectedItem());
          ((Stage)((Node)(event.getSource())).getScene().getWindow()).close();
        }
      }
    });

    btnBack.setOnAction(event -> ((Stage)((Node)(event.getSource())).getScene().getWindow()).close());

    colItemPhoto.setCellValueFactory(param -> param.getValue().itemPhotoProperty());

    colItemPhoto.setCellFactory(param -> new ItemPhotoTableCell());

    colItemName.setMinWidth(450);
    colItemName.setCellValueFactory(param -> param.getValue().itemNameProperty());

    colItemName.setCellFactory(param -> new ItemInfoTableCell());

    tblSearch.getColumns().addAll(colItemPhoto, colItemName);

    txtSearch.setOnKeyReleased(event -> {
      if (event.getCode() == KeyCode.ENTER) {
        loadItems();
      }
    });
    loadItems();

  }

  public void setiItemSearch(IItemSearch iItemSearch) {
    this.iItemSearch = iItemSearch;

  }

  private void loadItems() {
    lockControls();

    Task<ObservableList<Item>> items = new Task<ObservableList<Item>>() {
      @Override
      protected ObservableList<Item> call() throws Exception {
        if(posItemAssembly == null) {
          return iItems.getItems(txtSearch.getText());
        } else {
          return iItems.getItems(txtSearch.getText(),posItemAssembly.getExtItemSubGroupID());
        }
      }
    };

    new Thread(items).start();

    items.stateProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue == Worker.State.SUCCEEDED) {
        unlockControls();
      } else if (newValue == Worker.State.FAILED) {
        unlockControls();
      } else if (newValue == Worker.State.RUNNING) {
        lockControls();
      }else if (newValue == Worker.State.CANCELLED) {
        unlockControls();
      }
      txtSearch.requestFocus();
    });

    tblSearch.itemsProperty().bind(items.valueProperty());

  }

  private void lockControls() {
    pbrIndicator.setVisible(true);
    btnSelect.setDisable(true);
    btnBack.setDisable(true);
    txtSearch.setDisable(true);
  }

  private void unlockControls() {
    pbrIndicator.setVisible(false);
    btnSelect.setDisable(false);
    btnBack.setDisable(false);
    txtSearch.setDisable(false);
  }

  class ItemPhotoTableCell extends TableCell<Item, Image> {
    ImageView imageView;

    @Override
    protected void updateItem(Image item, boolean empty) {
      super.updateItem(item, empty);
      setText(null);
      if (empty) {
        setGraphic(null);
      } else {
        Circle circle = new Circle(47.5,47.5,47.5);
        imageView = new ImageView(item);
        imageView.getStyleClass().add("imageView");
        imageView.setFitHeight(95);
        imageView.setFitWidth(95);
        imageView.setClip(circle);
        setGraphic(imageView);

      }
    }
  }

  private class ItemInfoTableCell extends TableCell<Item, String> {

    VBox vbxItemInfo;
    Label lblItemName;
    Label lblItemSubGroup;
    Label lblItemClassification;
    ItemInfoTableCell() {
      vbxItemInfo = new VBox();
      vbxItemInfo.getStyleClass().add("vbxItemInfo");
      lblItemName = new Label();
      lblItemName.getStyleClass().add("lblItemName");
      lblItemSubGroup = new Label();
      lblItemClassification = new Label();
      vbxItemInfo.getChildren().addAll(lblItemName,lblItemSubGroup,lblItemClassification);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
      super.updateItem(item, empty);
      setText(null);
      if (empty || item == null) {
         setGraphic(null);
      } else {
        Item i = (Item)getTableRow().getItem();
        if(i !=null ) {
          if(!i.isIsActive()) {
            lblItemName.setText(i.getItemName() + " (INACTIVE)");
          } else {
            lblItemName.setText(i.getItemName());
          }
          lblItemSubGroup.setText(i.getItemSubGroup().toString());
          lblItemClassification.setText(i.getItemClassification().toString());
          setGraphic(vbxItemInfo);
        } else {
          setGraphic(null);
        }

      }
    }
  }

}
