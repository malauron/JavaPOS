
package com.JavaPOS.Controllers;

import com.JavaPOS.DataAccessObjects.ItemAssemblies;
import com.JavaPOS.DataAccessObjects.ItemSubGroups;
import com.JavaPOS.DataAccessObjects.Items;
import com.JavaPOS.DataAccessObjects.UOMs;
import com.JavaPOS.DataModels.Item;
import com.JavaPOS.DataModels.Item.ItemClassification;
import com.JavaPOS.DataModels.ItemAssembly;
import com.JavaPOS.DataModels.ItemSubGroup;
import com.JavaPOS.DataModels.UnitOfMeasure;
import com.JavaPOS.Interfaces.*;
import com.JavaPOS.Utilities.FXMLForms;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public class ItemForm {

  @FXML
  private HBox hbxSaveStatus;
  @FXML
  private HBox hbxError;
  @FXML
  private ImageView imvDummy;
  @FXML
  private ImageView imvItemPhoto;
  @FXML
  private JFXButton btnBrowse;
  @FXML
  private JFXButton btnSave;
  @FXML
  private JFXButton btnNew;
  @FXML
  private JFXCheckBox chkIsActive;
  @FXML
  private JFXComboBox<Item.ItemClassification> cboItemClassification;
  @FXML
  private JFXComboBox<ItemSubGroup> cboItemSubGroup;
  @FXML
  private JFXComboBox<ItemSubGroup> cboExtItemGroup;
  @FXML
  private JFXComboBox<Item> cboExtItem;
  @FXML
  private JFXComboBox<UnitOfMeasure> cboUOM;
  @FXML
  private TableView<ItemAssembly> tblItemAssemblies;
  @FXML
  private JFXTextField txtPrice;
  @FXML
  private JFXTextField txtItemCode;
  @FXML
  private JFXTextField txtItemName;
  @FXML
  private JFXTextField txtExtQty;
  @FXML
  private JFXTextField txtPriceLimit;
  @FXML
  private Label lblError;
  @FXML
  private MaterialDesignIconView ivAttachPhoto;
  @FXML
  private MaterialDesignIconView ivRemovePhoto;
  @FXML
  private MaterialDesignIconView ivAdd;
  @FXML
  private VBox vbxItemInfo;

  private Task<ObservableList<ItemAssembly>> itemAssemblies;
  private Task<Void> taskHandleUser;
  private IItemSubGroups iItemSubGroups;
  private IUOMs iUOMs;
  private IItems items;
  private Item item;
  private IItemAssemblies iItemAssemblies;

  private TableColumn<ItemAssembly, String> colExtItemName = new TableColumn<>("Item");
  private TableColumn<ItemAssembly, String> colExtItemSubGroupName = new TableColumn<>("Group");
  private TableColumn<ItemAssembly, BigDecimal> colQuantity = new TableColumn<>("Quantity");
  private TableColumn<ItemAssembly, BigDecimal> colPriceLimit = new TableColumn<>("Price Limit");
  private TableColumn<ItemAssembly, Integer> colDeleteItem = new TableColumn<>();

  private IThread taskRunning;

  private double xOffset = 0;
  private double yOffset = 0;

  public void initialize() {

    iItemSubGroups = new ItemSubGroups();
    iUOMs = new UOMs();
    items = new Items();
    item = new Item();
    iItemAssemblies = new ItemAssemblies();

    hbxError.setVisible(false);
    unlockControls();


    Circle circle = new Circle(85,85,85);
    imvItemPhoto.setClip(circle);
    Circle circle2 = new Circle(85,85,85);
    imvDummy.setClip(circle2);
    imvItemPhoto.setPreserveRatio(false);

    cboItemClassification.setItems(
            FXCollections.observableArrayList(ItemClassification.values())
    );
    resetFields();

    ivAttachPhoto.setOnMouseClicked(event -> attachPhoto());

    ivRemovePhoto.setOnMouseClicked(event -> imvItemPhoto.setImage(null));

    txtPrice.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (!newValue.matches("\\d{0,9}([\\.]\\d{0,2})?")) {
          txtPrice.setText(oldValue);
        }
      }
    });

    btnNew.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        resetFields();
      }
    });

    btnSave.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        handleItem();
      }
    });

    btnBrowse.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        browseItem();
      }
    });

    cboExtItemGroup.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      if(newValue!=null) {
        loadDummyItems(newValue.getItemSubGroupID());
      } else {
        loadDummyItems(0);
      }
    });

    txtExtQty.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (!newValue.matches("\\d{0,9}([.]\\d{0,2})?")) {
          txtExtQty.setText(oldValue);
        }
      }
    });

    txtPriceLimit.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (!newValue.matches("\\d{0,9}([.]\\d{0,2})?")) {
          txtPriceLimit.setText(oldValue);
        }
      }
    });

    colExtItemSubGroupName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ItemAssembly, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(TableColumn.CellDataFeatures<ItemAssembly, String> param) {
        return param.getValue().extItemSubGroupNameProperty();
      }
    });

    colExtItemName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ItemAssembly, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(TableColumn.CellDataFeatures<ItemAssembly, String> param) {
        return param.getValue().extItemNameProperty();
      }
    });

    colQuantity.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ItemAssembly, BigDecimal>, ObservableValue<BigDecimal>>() {
      @Override
      public ObservableValue<BigDecimal> call(TableColumn.CellDataFeatures<ItemAssembly, BigDecimal> param) {
        return param.getValue().extQtyProperty();
      }
    });

    colPriceLimit.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ItemAssembly, BigDecimal>, ObservableValue<BigDecimal>>() {
      @Override
      public ObservableValue<BigDecimal> call(TableColumn.CellDataFeatures<ItemAssembly, BigDecimal> param) {
        return param.getValue().priceLimitProperty();
      }
    });

    colDeleteItem.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ItemAssembly, Integer>, ObservableValue<Integer>>() {
      @Override
      public ObservableValue<Integer> call(TableColumn.CellDataFeatures<ItemAssembly, Integer> param) {
        return param.getValue().extItemIDProperty();
      }
    });

    colDeleteItem.setCellFactory(new Callback<TableColumn<ItemAssembly, Integer>, TableCell<ItemAssembly, Integer>>() {
      @Override
      public TableCell<ItemAssembly, Integer> call(TableColumn<ItemAssembly, Integer> param) {
        return new DeleteItemTableCell();
      }
    });

    tblItemAssemblies.getColumns().addAll(colExtItemSubGroupName,colExtItemName,colQuantity,colPriceLimit,colDeleteItem);

    ivAdd.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        ItemAssembly itm = new ItemAssembly();
        itm.setExtItemID(cboExtItem.getSelectionModel().getSelectedItem().getItemID());
        itm.setExtItemName(cboExtItem.getSelectionModel().getSelectedItem().toString());
        itm.setExtItemSubGroupID(cboExtItemGroup.getSelectionModel().getSelectedItem().getItemSubGroupID());
        itm.setExtItemSubGroupName(cboExtItemGroup.getSelectionModel().getSelectedItem().toString());
        itm.setExtQty(BigDecimal.valueOf(Double.parseDouble(txtExtQty.getText())));
        itm.setPriceLimit(BigDecimal.valueOf(Double.parseDouble(txtPriceLimit.getText())));
        try {
          itemAssemblies.get().add(itm);
        } catch (InterruptedException e) {
          e.printStackTrace();
        } catch (ExecutionException e) {
          e.printStackTrace();
        }
      }
    });
  }

  private void loadItemSubGroups() {
    Task<ObservableList<ItemSubGroup>> itemSubGroups = new Task<ObservableList<ItemSubGroup>>() {
      @Override
      protected ObservableList<ItemSubGroup> call() throws Exception {
        return iItemSubGroups.getItemSubGroups();
      }
    };

    new Thread(itemSubGroups).start();

    cboItemSubGroup.itemsProperty().bind(itemSubGroups.valueProperty());

    itemSubGroups.stateProperty().addListener((observable, oldValue, newValue) -> {
      cboItemSubGroup.getSelectionModel().selectFirst();
    });
  }

  private void loadDummyItemSubGroup() {
    Task<ObservableList<ItemSubGroup>> dummyItemSubGroups = new Task<ObservableList<ItemSubGroup>>() {
      @Override
      protected ObservableList<ItemSubGroup> call() throws Exception {
        return iItemSubGroups.getDummyItemSubGroups();
      }
    };

    new Thread(dummyItemSubGroups).start();

    cboExtItemGroup.itemsProperty().bind(dummyItemSubGroups.valueProperty());
    dummyItemSubGroups.stateProperty().addListener((observable, oldValue, newValue) -> {
      cboExtItemGroup.getSelectionModel().selectFirst();
    });

  }

  private void loadUOMs() {
    Task<ObservableList<UnitOfMeasure>> uoms = new Task<ObservableList<UnitOfMeasure>>() {
      @Override
      protected ObservableList<UnitOfMeasure> call() throws Exception {
        return iUOMs.getUOMs();
      }
    };
    new Thread(uoms).start();
    cboUOM.itemsProperty().bind(uoms.valueProperty());
    uoms.stateProperty().addListener((observable, oldValue, newValue) -> {
        cboUOM.getSelectionModel().selectFirst();
    });
  }

  private void loadDummyItems(int i) {
    Task<ObservableList<Item>> dummyItems = new Task<ObservableList<Item>>() {
      @Override
      protected ObservableList<Item> call() throws Exception {
        return items.getDummyItems(i);
      }
    };
    new Thread(dummyItems).start();
    cboExtItem.itemsProperty().bind(dummyItems.valueProperty());
    dummyItems.stateProperty().addListener((observable, oldValue, newValue) -> {
      cboExtItem.getSelectionModel().selectFirst();
    });
  }

  private void loadItemAssemblies(int itemID) {
    itemAssemblies = new Task<ObservableList<ItemAssembly>>() {
      @Override
      protected ObservableList<ItemAssembly> call() throws Exception {
        return iItemAssemblies.getItemAssemblies(itemID);
      }
    };
    new Thread(itemAssemblies).start();
    tblItemAssemblies.itemsProperty().bind(itemAssemblies.valueProperty());
  }

  private void browseItem() {
    ItemSearch itemSearch = new ItemSearch();
    itemSearch.setiItemSearch(new IItemSearch() {
      @Override
      public void getSelectedItem(Item i) {
        item = i;
        txtItemCode.setText(i.getItemCode());
        txtItemName.setText(i.getItemName());
        cboItemClassification.getSelectionModel().select(i.getItemClassification());
        cboItemSubGroup.getSelectionModel().select(i.getItemSubGroup());
        cboUOM.getSelectionModel().select(i.getUom());
        txtPrice.setText(i.getRegularPrice().toString());
        imvItemPhoto.setImage(i.getItemPhoto());
        chkIsActive.setSelected(i.isIsActive());
        cboExtItemGroup.getSelectionModel().selectFirst();
        loadItemAssemblies(item.getItemID());
      }
    });

    try {

      FXMLLoader itemFXML = FXMLForms.SEARCH.getLoader();
      itemFXML.setController(itemSearch);

      Parent root = itemFXML.load();
      Scene scene = new Scene(root);
      Stage stage = new Stage();

      root.setOnMousePressed(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
          xOffset = event.getSceneX();
          yOffset = event.getSceneY();
        }
      });

      root.setOnMouseDragged(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
          stage.setX(event.getScreenX() - xOffset);
          stage.setY(event.getScreenY() - yOffset);
        }
      });

      stage.initModality(Modality.APPLICATION_MODAL);
      stage.initStyle(StageStyle.UNDECORATED);
      stage.setScene(scene);
      stage.show();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void handleItem() {

    if(txtItemCode.getText().isEmpty()) {
      showError("Please provide an item code");
      txtItemCode.requestFocus();
      return;
    }

    if(txtItemName.getText().isEmpty()) {
      showError("Description is blank.");
      txtItemName.requestFocus();
      return;
    }

    if(cboItemClassification.getSelectionModel().isEmpty()) {
      showError("Please specify a classification for the item.");
      cboItemClassification.requestFocus();
      return;
    }

    if(cboItemSubGroup.getSelectionModel().isEmpty()) {
      showError("Please specify an item sub group.");
      cboItemSubGroup.requestFocus();
      return;
    }

    if(cboUOM.getSelectionModel().isEmpty()) {
      showError("Please select a UOM");
      cboUOM.requestFocus();
      return;
    }

    item.setItemCode(txtItemCode.getText());
    item.setItemName(txtItemName.getText());
    item.setItemClassification(cboItemClassification.getSelectionModel().getSelectedItem());
    item.setItemSubGroup(cboItemSubGroup.getSelectionModel().getSelectedItem());
    item.setUom(cboUOM.getSelectionModel().getSelectedItem());
    item.setRegularPrice(BigDecimal.valueOf(Double.parseDouble(txtPrice.getText())));
    item.setItemPhoto(imvItemPhoto.getImage());
    item.setIsActive(chkIsActive.isSelected());

    taskHandleUser = new Task<Void>() {
      @Override
      protected Void call() throws Exception {
        for (int i = 0; i <= 1; i++) {
          Thread.sleep(1000);
        }
        items.handleItem(item,itemAssemblies.get());

        return null;
      }
    };
    new Thread(taskHandleUser).start();
    taskHandleUser.stateProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue == Worker.State.SUCCEEDED) {
        unlockControls();
      } else if (newValue == Worker.State.RUNNING) {
        lockControls();
      } else if (newValue == Worker.State.FAILED) {
        taskHandleUser.getException().printStackTrace();
       if (taskHandleUser.getException() instanceof SQLException ) {
         System.out.println("None");
       } else if (taskHandleUser.getException() instanceof ClassNotFoundException ) {
         System.out.println("Y1");
       } else {
         System.out.println("Y3");
       }
        unlockControls();
       showError("Unable to save item.");
      } else if (newValue == Worker.State.CANCELLED) {
        lockControls();
      }
    });
  }

  private void resetFields() {
    item.setItemID(0);
    imvItemPhoto.setImage(null);
    loadItemSubGroups();
    loadUOMs();
    loadDummyItemSubGroup();
    loadItemAssemblies(0);
    cboItemClassification.getSelectionModel().selectFirst();
    txtItemCode.setText("");
    txtItemName.setText("");
    txtPrice.setText("0.00");
    chkIsActive.setSelected(true);
    txtExtQty.setText("0.00");
    txtPriceLimit.setText("0.00");
  }

  private void attachPhoto() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Attach Photo");
    fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image files", "*.jpg","*.png")
    );
    File file = fileChooser.showOpenDialog(imvItemPhoto.getScene().getWindow());
    if(file != null) {
      Image img = new Image(file.toURI().toString());
      imvItemPhoto.setImage(img);
    }
  }

  private void lockControls() {
    if (taskRunning != null) {
      taskRunning.isTaskRunning(true);
    }

    btnBrowse.setDisable(true);
    vbxItemInfo.setDisable(true);
    hbxSaveStatus.setVisible(true);
  }

  private void unlockControls() {
    btnBrowse.setDisable(false);
    vbxItemInfo.setDisable(false);
    hbxSaveStatus.setVisible(false);

    if (taskRunning != null) {
      taskRunning.isTaskRunning(false);
    }
  }

  private void showError(String error) {
    hbxError.setVisible(true);
    lblError.setText(error);

    FadeTransition ft1 = new FadeTransition(Duration.seconds(1),hbxError);
    ft1.setToValue(1);
    ft1.setFromValue(0);
    ft1.play();
    ft1.setOnFinished(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        FadeTransition ft2 = new FadeTransition(Duration.seconds(3),hbxError);
        ft2.setToValue(0);
        ft2.setFromValue(1);
        ft2.play();
      }
    });
  }

  public void setTaskRunning(IThread taskRunning) {
    this.taskRunning = taskRunning;
  }

  private class DeleteItemTableCell extends TableCell<ItemAssembly,Integer> {

    JFXButton btnDeleteItem;

    public DeleteItemTableCell() {
      btnDeleteItem = new JFXButton("Delete");
      btnDeleteItem.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
          try {
            itemAssemblies.get().remove((ItemAssembly)getTableRow().getItem());
          } catch (InterruptedException e) {
            e.printStackTrace();
          } catch (ExecutionException e) {
            e.printStackTrace();
          }
        }
      });
    }

    @Override
    protected void updateItem(Integer item, boolean empty) {
      super.updateItem(item, empty);
      setText(null);
      setGraphic(null);
      if(!empty) {
        setGraphic(btnDeleteItem);
      }
    }

  }

}