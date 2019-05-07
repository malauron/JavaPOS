package com.JavaPOS.Controllers;

import com.JavaPOS.DataModels.Item;
import com.JavaPOS.DataModels.POSItem;
import com.JavaPOS.DataModels.POSItemAssembly;
import com.JavaPOS.Interfaces.IItemDetails;
import com.JavaPOS.Utilities.FXMLForms;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class ItemDetails {

  @FXML
  private JFXTextField txtItemCode;
  @FXML
  private JFXTextField txtItemName;
  @FXML
  private JFXTextField txtQuantity;
  @FXML
  private JFXTextField txtDiscount;
  @FXML
  private JFXTextField txtDiscPrct;
  @FXML
  private JFXTextField txtAddons;
  @FXML
  private JFXTextField txtTotal;
  @FXML
  private JFXTextField txtPrice;
  @FXML
  private TableView<POSItemAssembly> tblItemAssembly;
  @FXML
  private JFXButton btnUpdate;
  @FXML
  private JFXButton btnCancel;

  private DecimalFormat df = new DecimalFormat("#,##0.00");
  private POSItem posItem;
  private TableColumn<POSItemAssembly,String> colItemName = new TableColumn<>("Description");
  private TableColumn<POSItemAssembly,BigDecimal> colAddons = new TableColumn<>("Add-ons");
  private double xOffset = 0;
  private double yOffset = 0;
  private IItemDetails iItemDetails;

  public ItemDetails(POSItem posItem) {
    this.posItem  = new POSItem();
    this.posItem.setItemCode(posItem.getItemCode());
    this.posItem.setItemName(posItem.getItemName());
    this.posItem.setRegularPrice(posItem.getRegularPrice());
    this.posItem.setItemQuantity(posItem.getItemQuantity());
    this.posItem.setAddons(posItem.getAddons());
    this.posItem.setItemDiscount(posItem.getItemDiscount());
    this.posItem.setItemDiscPrct(posItem.getItemDiscPrct());
    this.posItem.setItemTotal(posItem.getItemTotal());
    if(posItem.getItemClassification().equals(Item.ItemClassification.ASSEMBLY)) {
      ObservableList<POSItemAssembly> ias = FXCollections.observableArrayList();
      for(POSItemAssembly itm : posItem.getPosItemAssembly()) {
        POSItemAssembly ia = new POSItemAssembly();
        ia.setExtItemID(itm.getExtItemID());
        ia.setExtItemCode(itm.getExtItemCode());
        ia.setExtItemName(itm.getExtItemName());
        ia.setExtItemSubGroupID(itm.getExtItemSubGroupID());
        ia.setExtItemSubGroupName(itm.getExtItemSubGroupName());
        ia.setUomID(itm.getUomID());
        ia.setExtQty(itm.getExtQty());
        ia.setRegularPrice(itm.getRegularPrice());
        ia.setPriceLimit(itm.getPriceLimit());
        ia.setActItemID(itm.getActItemID());
        ia.setActItemSubGroupID(itm.getActItemSubGroupID());
        ia.setAddOnAmt(itm.getAddOnAmt());
        ias.add(ia);
      }
      this.posItem.setPosItemAssembly(ias);
    }
  }

  public void initialize() {

    posItem.itemQuantityProperty().addListener((observable, oldValue, newValue) -> {
      compute_total();
    });

    posItem.itemDiscountProperty().addListener((observable, oldValue, newValue) -> {
      compute_total();
    });

    posItem.addonsProperty().addListener((observable, oldValue, newValue) -> {
      compute_total();
    });

    colItemName.getStyleClass().add("colItemName");
    colItemName.setResizable(false);
    colItemName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<POSItemAssembly, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(TableColumn.CellDataFeatures<POSItemAssembly, String> param) {
        return param.getValue().extItemNameProperty();
      }
    });

    colItemName.setCellFactory(new Callback<TableColumn<POSItemAssembly, String>, TableCell<POSItemAssembly, String>>() {
      @Override
      public TableCell<POSItemAssembly, String> call(TableColumn<POSItemAssembly, String> param) {
        return new ItemDetailTableCell();
      }
    });

    colAddons.getStyleClass().add("colAddons");
    colAddons.setResizable(false);
    colAddons.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<POSItemAssembly, BigDecimal>, ObservableValue<BigDecimal>>() {
      @Override
      public ObservableValue<BigDecimal> call(TableColumn.CellDataFeatures<POSItemAssembly, BigDecimal> param) {
        return param.getValue().addOnAmtProperty();
      }
    });

    colAddons.setCellFactory(new Callback<TableColumn<POSItemAssembly, BigDecimal>, TableCell<POSItemAssembly, BigDecimal>>() {
      @Override
      public TableCell<POSItemAssembly, BigDecimal> call(TableColumn<POSItemAssembly, BigDecimal> param) {
        return new TableCell<POSItemAssembly,BigDecimal>() {
          @Override
          protected void updateItem(BigDecimal item, boolean empty) {
            setText(null);
            if(!empty) {
              setText(df.format(item));
            }
          }
        };
      }
    });

    tblItemAssembly.getColumns().addAll(colItemName,colAddons);

    btnCancel.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        ((Stage)((Node)(event.getSource())).getScene().getWindow()).close();
      }
    });

    btnUpdate.setOnAction(event -> {
      if(iItemDetails != null) {
        iItemDetails.getUpdatedPOSItem(posItem);
        ((Stage)((Node)(event.getSource())).getScene().getWindow()).close();
      }
    });

    txtItemCode.setText(posItem.getItemCode());
    txtItemName.setText(posItem.getItemName());
    txtQuantity.setText(df.format(posItem.getItemQuantity()));
    txtPrice.setText(df.format(posItem.getRegularPrice()));
    txtDiscount.setText(df.format(posItem.getItemDiscount()));
    txtDiscPrct.setText(df.format(posItem.getItemDiscPrct()));
    txtAddons.setText(df.format(posItem.getAddons()));
    txtTotal.setText(df.format(posItem.getItemTotal()));

    txtQuantity.textProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue.matches("\\d{0,9}([.]\\d{0,2})?")) {
        txtQuantity.setText(oldValue);
      } else {
        if(!newValue.isEmpty()) {
          BigDecimal qty = new BigDecimal(newValue);
          if(qty.equals(BigDecimal.ZERO)) {
            qty = BigDecimal.ONE;
            txtQuantity.setText("1");
          }
          posItem.setItemQuantity(qty);
          txtDiscount.setText(df.format(posItem.getItemDiscount()));
          txtDiscPrct.setText(df.format(posItem.getItemDiscPrct()));
        } else {
          txtQuantity.setText("1");
        }
      }
    });

    txtDiscount.textProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue.matches("\\d{0,9}([.]\\d{0,2})?")) {
        txtDiscount.setText(oldValue);
      } else {
        if(!newValue.isEmpty()) {
          if(txtDiscount.isFocused()) {
            posItem.setItemDiscount(BigDecimal.valueOf(Double.parseDouble(newValue)));
            txtDiscPrct.setText(df.format(posItem.getItemDiscPrct()));
          }
        } else {
          txtDiscount.setText("0.00");
        }
      }
    });

    txtDiscPrct.textProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue.matches("\\d{0,9}([.]\\d{0,2})?")) {
        txtDiscPrct.setText(oldValue);
      } else {
        if(!newValue.isEmpty()) {
          if (txtDiscPrct.isFocused()) {
            compute_discount(BigDecimal.valueOf(Double.parseDouble(newValue)));
            txtDiscount.setText(posItem.getItemDiscount().toString());
          }
        } else {
          txtDiscPrct.setText("0.00");
        }
      }
    });

    Task<Void> loadAssemblies;
    loadAssemblies = new Task<Void>() {
      @Override
      protected Void call() throws Exception {
        return null;
      }
    };

    new Thread(loadAssemblies).start();
    loadAssemblies.stateProperty().addListener((observable, oldValue, newValue) -> {
      if(newValue == Worker.State.SUCCEEDED){
        tblItemAssembly.setItems(posItem.getPosItemAssembly());
      }
    });

  }

  private void browseItem(POSItemAssembly pi) {

    ItemSearch itemSearch = new ItemSearch(pi);

    itemSearch.setiItemSearch(i -> {
      pi.setActItemID(i.getItemID());
      pi.setActItemSubGroupID(i.getItemSubGroup().getItemSubGroupID());
      pi.setUomID(i.getUom().getUomID());
      pi.setExtItemName(i.getItemName());
      pi.setRegularPrice(i.getRegularPrice());
      posItem.setAddons(posItem.getAddons().subtract(pi.getAddOnAmt()));

      if(pi.getRegularPrice().multiply(pi.getExtQty()).compareTo(pi.getPriceLimit()) > 0) {
        pi.setAddOnAmt(pi.getRegularPrice().multiply(pi.getExtQty()).subtract(pi.getPriceLimit()));
      } else {
        pi.setAddOnAmt(BigDecimal.valueOf(0));
      }

      posItem.setAddons(posItem.getAddons().add(pi.getAddOnAmt()));
//      posItem.setItemTotal(posItem.getRegularPrice().add(posItem.getAddons()).multiply(posItem.getItemQuantity()));

      txtAddons.setText(df.format(posItem.getAddons()));
//      txtTotal.setText(df.format(posItem.getItemTotal()));
      txtDiscPrct.setText(df.format(posItem.getItemDiscPrct()));

    });

    try {

      FXMLLoader itemFXML = FXMLForms.SEARCH.getLoader();
      itemFXML.setController(itemSearch);

      Parent root = itemFXML.load();
      Scene scene = new Scene(root);
      Stage stage = new Stage();

      root.setOnMousePressed(event -> {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
      });

      root.setOnMouseDragged(event -> {
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
      });

      stage.initModality(Modality.APPLICATION_MODAL);
      stage.initStyle(StageStyle.UNDECORATED);
      stage.setScene(scene);
      stage.show();

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public void compute_discount(BigDecimal dprct) {
    BigDecimal price = posItem.getRegularPrice();
    BigDecimal addOn = posItem.getAddons();
    BigDecimal qty = posItem.getItemQuantity();
    BigDecimal discprct = dprct.divide(BigDecimal.valueOf(100));
    BigDecimal disc = price.add(addOn).multiply(qty).multiply(discprct).setScale(2,RoundingMode.HALF_UP);
    posItem.setItemDiscount(disc);
  }

  public void compute_total() {
    BigDecimal price = posItem.getRegularPrice();
    BigDecimal addOn = posItem.getAddons();
    BigDecimal qty = posItem.getItemQuantity();
    BigDecimal disc = posItem.getItemDiscount();
    BigDecimal ttl = price.add(addOn).multiply(qty).setScale(2,RoundingMode.HALF_UP);
    BigDecimal discprct = disc.divide(ttl, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
    ttl = ttl.subtract(disc);
    posItem.setItemDiscPrct(discprct);
    posItem.setItemTotal(ttl);

//    if(txtDiscount.isFocused() || txtQuantity.isFocused()) {
//      txtDiscPrct.setText(df.format(discprct));
//    }

//    if(txtDiscPrct.isFocused()) {
//      txtDiscount.setText(df.format(disc));

//    }

    txtTotal.setText(df.format(ttl));
  }

  public void setItemDetails(IItemDetails iItemDetails) {
    this.iItemDetails = iItemDetails;
  }

  private class ItemDetailTableCell extends TableCell<POSItemAssembly, String> {

    HBox hbx;
    Label lblItemDetail;
    MaterialDesignIconView ivSelectItem;

    ItemDetailTableCell() {
      hbx = new HBox();
      lblItemDetail = new Label();
      lblItemDetail.setMinWidth(400);
      lblItemDetail.setPrefWidth(400);
      ivSelectItem = new MaterialDesignIconView(MaterialDesignIcon.MAGNIFY);
      ivSelectItem.setSize("25");
      ivSelectItem.setOnMouseClicked(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
          POSItemAssembly pi = (POSItemAssembly)getTableRow().getItem();
          browseItem(pi);
        }
      });
      hbx.getChildren().addAll(lblItemDetail,ivSelectItem);

    }

    @Override
    protected void updateItem(String item, boolean empty) {
      super.updateItem(item, empty);
      setText(null);
      setGraphic(null);
      if(!empty && item != null){
          lblItemDetail.setText(item.toUpperCase());
          POSItemAssembly pi = (POSItemAssembly)getTableRow().getItem();
          if(pi != null) {
            if(pi.getExtItemID().equals(0)) {
              ivSelectItem.setVisible(true);
            } else {
              ivSelectItem.setVisible(false);
            }
          }
          setGraphic(hbx);
        }

    }
  }

}
