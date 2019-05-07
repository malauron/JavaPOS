package com.JavaPOS.Controllers;

import com.JavaPOS.DataAccessObjects.Items;
import com.JavaPOS.DataAccessObjects.POSTransactions;
import com.JavaPOS.DataModels.*;
import com.JavaPOS.Interfaces.IItemDetails;
import com.JavaPOS.Interfaces.IItems;
import com.JavaPOS.Interfaces.IPaymentForm;
import com.JavaPOS.Utilities.FXMLForms;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;

public class POSForm {

  @FXML
  private JFXProgressBar pbrIndicator;
  @FXML
  private JFXTextField txtSearchItem;
  @FXML
  private MaterialDesignIconView btnSave;
  @FXML
  private Label lblAddon;
  @FXML
  private Label lblDiscount;
  @FXML
  private Label lblGross;
  @FXML
  private Label lblTotal;
  @FXML
  private MaterialDesignIconView ivSearchItem;
  @FXML
  private TableView<POSItem> tblList;

  private IItems iItems;
  private Task<Item> item;
  private ObservableList<POSItem> posItems = FXCollections.observableArrayList();
  private TableColumn<POSItem, Integer> colItemID = new TableColumn<>("ItemDetails");
  private TableColumn<POSItem, BigDecimal> colItemTotal = new TableColumn<>("Item Total");
  private BigDecimal netAmount = new BigDecimal(0.00);
  private BigDecimal grossAmount = new BigDecimal(0.00);
  private BigDecimal ttlDiscount = new BigDecimal(0.00);
  private BigDecimal ttlAddon = new BigDecimal(0.00);
  private DecimalFormat df = new DecimalFormat("#,##0.00");
  private double xOffset = 0;
  private double yOffset = 0;

  public void initialize() {

    iItems = new Items();
    resetFields();

    txtSearchItem.setOnKeyReleased(event -> {
      if (event.getCode() == KeyCode.ENTER) {
        loadPOSItem();
      }
    });

    ivSearchItem.setOnMouseClicked(event ->browseItem());
    colItemID.setCellValueFactory(param -> param.getValue().itemIDProperty());
    colItemID.setCellFactory(param -> new ItemDetailTableCell());
    colItemID.getStyleClass().add("colItemID");

    colItemTotal.setCellValueFactory(param -> param.getValue().itemTotalProperty());

    colItemTotal.setCellFactory(new Callback<TableColumn<POSItem, BigDecimal>, TableCell<POSItem, BigDecimal>>() {
      @Override
      public TableCell<POSItem, BigDecimal> call(TableColumn<POSItem, BigDecimal> param) {
        return new TableCell<POSItem,BigDecimal>() {
          @Override
          protected void updateItem(BigDecimal item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);
            if(!empty) {
              setText(df.format(item));
            }
          }
        };
      }
    });
    colItemTotal.getStyleClass().add("colItemTotal");

    tblList.getColumns().addAll(colItemID,colItemTotal);
    tblList.setItems(posItems);

    btnSave.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        saveTransaction();
      }
    });

  }

  private void resetFields() {
    pbrIndicator.setVisible(false);
    lblDiscount.setText("0.00");
    lblGross.setText("0.00");
    lblAddon.setText("0.00");
    lblTotal.setText("0.00");
    netAmount = BigDecimal.ZERO;
    grossAmount = BigDecimal.ZERO;
    ttlAddon = BigDecimal.ZERO;
    ttlDiscount = BigDecimal.ZERO;
    posItems.clear();
  }

  private void loadPOSItem() {

    item = new Task<Item>() {
      @Override
      protected Item call() throws Exception {
        return iItems.getItem(txtSearchItem.getText());
      }
    };

    new Thread(item).start();
    item.stateProperty().addListener((observable, oldValue, newValue) -> {
      if(newValue == Worker.State.SUCCEEDED) {
        try {
          if(item.get() != null) {
            loadPOSItem(item.get());
          }
        } catch (InterruptedException ie) {
          ie.printStackTrace();
        } catch (ExecutionException ee) {
          ee.printStackTrace();
        }
      }
    });
  }

  private void loadPOSItem(Item i) {

    POSItem posItem = new POSItem();
    posItem.setItemID(i.getItemID());
    posItem.setItemCode(i.getItemCode());
    posItem.setItemName(i.getItemName());
    posItem.setItemSubGroup(i.getItemSubGroup());
    posItem.setItemClassification(i.getItemClassification());
    posItem.setUom(i.getUom());
    posItem.setItemPhoto(i.getItemPhoto());
    posItem.setRegularPrice(i.getRegularPrice());
    posItem.setItemQuantity(BigDecimal.valueOf(1));
    posItem.setItemDiscount(BigDecimal.valueOf(0));
    posItem.setItemDiscPrct(BigDecimal.valueOf(0));
    posItem.setAddons(BigDecimal.valueOf(0));

    if (i.getItemClassification().equals(Item.ItemClassification.ASSEMBLY)) {
      ObservableList<POSItemAssembly> posItemAssemblies = FXCollections.observableArrayList();

      for (ItemAssembly itmass : i.getItemAssembly()) {

        POSItemAssembly posItemAssembly = new POSItemAssembly();
        posItemAssembly.setExtItemID(itmass.getExtItemID());
        posItemAssembly.setExtItemName(itmass.getExtItemName());
        posItemAssembly.setExtItemSubGroupID(itmass.getExtItemSubGroupID());
        posItemAssembly.setExtItemSubGroupName(itmass.getExtItemSubGroupName());
        posItemAssembly.setRegularPrice(itmass.getRegularPrice());
        posItemAssembly.setExtQty(itmass.getExtQty());
        posItemAssembly.setPriceLimit(itmass.getPriceLimit());

        if(itmass.getExtItemID().compareTo(0) > 0) {
          posItemAssembly.setActItemID(itmass.getExtItemID());
          posItemAssembly.setActItemSubGroupID(itmass.getExtItemSubGroupID());
          posItemAssembly.setUomID(itmass.getUomID());
        }

        if(itmass.getRegularPrice().multiply(itmass.getExtQty()).compareTo(itmass.getPriceLimit()) > 0) {
          posItemAssembly.setAddOnAmt(
                  itmass.getRegularPrice().multiply(itmass.getExtQty()).subtract(itmass.getPriceLimit())
          );
        } else {
          posItemAssembly.setAddOnAmt(BigDecimal.valueOf(0));
        }

        posItemAssemblies.add(posItemAssembly);
        posItem.setAddons(posItem.getAddons().add(posItemAssembly.getAddOnAmt()));
      }
      posItem.setPosItemAssembly(posItemAssemblies);
    }
    posItem.setItemTotal(i.getRegularPrice().add(posItem.getAddons()));

    netAmount = netAmount.add(posItem.getItemTotal());
    grossAmount= grossAmount.add(posItem.getRegularPrice());
    ttlDiscount = ttlDiscount.add(posItem.getItemDiscount());
    ttlAddon = ttlAddon.add(posItem.getAddons());
    lblTotal.setText(df.format(netAmount));
    lblGross.setText(df.format(grossAmount));
    lblDiscount.setText(df.format(ttlDiscount));
    lblAddon.setText(df.format(ttlAddon));
    posItems.add(posItem);
    tblList.scrollTo(posItem);
    txtSearchItem.setText("");
    txtSearchItem.requestFocus();

  }

  private void browseItem() {

    ItemSearch itemSearch = new ItemSearch();
    itemSearch.setiItemSearch(i -> loadPOSItem(i));

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

  private void saveTransaction() {

    PaymentForm paymentForm = new PaymentForm(netAmount);
    paymentForm.setIPaymentForm(new IPaymentForm() {
      @Override
      public void savePOSTransaction() {
        POSTransaction posTransaction = new POSTransaction();

        posTransaction.setShiftdetail_id(0);
        posTransaction.setCustomer_id(0);
        posTransaction.setCustomergroup_id(0);
        posTransaction.setCustomersubgroup_id(0);
        posTransaction.setTotal_cash(paymentForm.getCashAmount());
        posTransaction.setTotal_check(BigDecimal.ZERO);
        posTransaction.setTotal_charged(paymentForm.getChargeAmount());
        posTransaction.setTotal_card(paymentForm.getCardAmount());
        posTransaction.setTotal_prepaid(BigDecimal.ZERO);
        posTransaction.setTotal_amount_due(netAmount);
        posTransaction.setTotal_disc_amt(BigDecimal.ZERO);
        posTransaction.setTotal_disc_prct(BigDecimal.ZERO);
        posTransaction.setTotal_vat(BigDecimal.ZERO);
        posTransaction.setCash_tendered(paymentForm.getCashTendered());
        posTransaction.setCash_change(paymentForm.getCashChange());
        posTransaction.setPosItems(posItems);
        try {
          new POSTransactions().handlePOSItem(posTransaction);
          resetFields();
        } catch (SQLException e) {
          e.printStackTrace();
        } catch (ClassNotFoundException e) {
          e.printStackTrace();
        }
      }
    });

    try {



      FXMLLoader paymentFormLoader = FXMLForms.PAYMENTFORM.getLoader();
      paymentFormLoader.setController(paymentForm);

      Parent root = paymentFormLoader.load();
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
      stage.centerOnScreen();
      stage.show();

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  private void updateTotals(POSItem oldItem, POSItem newItem) {

    ttlAddon = ttlAddon.subtract(oldItem.getAddons().multiply(oldItem.getItemQuantity()).setScale(2, RoundingMode.HALF_UP));
    grossAmount = grossAmount.subtract(oldItem.getRegularPrice().multiply(oldItem.getItemQuantity()).setScale(2, RoundingMode.HALF_UP));
    ttlDiscount = ttlDiscount.subtract(oldItem.getItemDiscount());
    netAmount =  netAmount.subtract(oldItem.getItemTotal());

    ttlAddon = ttlAddon.add(newItem.getAddons().multiply(newItem.getItemQuantity()).setScale(2, RoundingMode.HALF_UP));
    grossAmount = grossAmount.add(newItem.getRegularPrice().multiply(newItem.getItemQuantity()).setScale(2, RoundingMode.HALF_UP));
    ttlDiscount = ttlDiscount.add(newItem.getItemDiscount());
    netAmount =  netAmount.add(newItem.getItemTotal());

    lblAddon.setText(df.format(ttlAddon));
    lblGross.setText(df.format(grossAmount));
    lblDiscount.setText(df.format(ttlDiscount));
    lblTotal.setText(df.format(netAmount));

    tblList.refresh();
  }

  private class ItemDetailTableCell extends TableCell<POSItem, Integer> {

    HBox hbxItemDetail;
    VBox vbxItemDetail;
    HBox hbxActionButtons;
    Label lblItemName;
    Label lblPrice;
    Label lblClassification;
    ImageView imageView;
    MaterialDesignIconView ivEditItem;
    MaterialDesignIconView ivDeleteItem;

    ItemDetailTableCell() {
      hbxItemDetail = new HBox();
      vbxItemDetail = new VBox();
      vbxItemDetail.getStyleClass().add("vbxItemDetail");
      hbxActionButtons = new HBox();
      hbxActionButtons.getStyleClass().add("hbxActionButtons");
      imageView = new ImageView();
      lblItemName = new Label();
      lblItemName.getStyleClass().add("lblItemName");
      lblPrice = new Label();
      lblClassification = new Label();
      imageView = new ImageView();
      ivEditItem = new MaterialDesignIconView(MaterialDesignIcon.PENCIL);
      ivEditItem.setSize("30");
      ivDeleteItem = new MaterialDesignIconView(MaterialDesignIcon.MINUS_CIRCLE);
      ivDeleteItem.setSize("30");

      ivEditItem.setOnMouseClicked(event -> {

        try {

          POSItem oldpi = (POSItem)getTableRow().getItem();
          ItemDetails itemDetails = new ItemDetails(oldpi);
          itemDetails.setItemDetails(new IItemDetails() {
            @Override
            public void getUpdatedPOSItem(POSItem pi) {
              updateTotals(oldpi,pi);
              oldpi.setAddons(pi.getAddons());
              oldpi.setItemQuantity(pi.getItemQuantity());
              oldpi.setItemDiscount(pi.getItemDiscount());
              oldpi.setItemDiscPrct(pi.getItemDiscPrct());
              oldpi.setItemTotal(pi.getItemTotal());
              if(oldpi.getItemClassification().equals(Item.ItemClassification.ASSEMBLY)){
                oldpi.getPosItemAssembly().clear();
                for(POSItemAssembly itm : pi.getPosItemAssembly()) {
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
                  oldpi.getPosItemAssembly().add(ia);
                }
              }
            }
          });

          FXMLLoader itemDetailsFXML = FXMLForms.ITEMDETAILS.getLoader();
          itemDetailsFXML.setController(itemDetails);

          Parent root = itemDetailsFXML.load();
          Scene scene = new Scene(root);
          Stage stage = new Stage();

          root.setOnMousePressed(event1 -> {
            xOffset = event1.getSceneX();
            yOffset = event1.getSceneY();
          });

          root.setOnMouseDragged(event12 -> {
            stage.setX(event12.getScreenX() - xOffset);
            stage.setY(event12.getScreenY() - yOffset);
          });

          stage.initModality(Modality.APPLICATION_MODAL);
          stage.initStyle(StageStyle.UNDECORATED);
          stage.setScene(scene);
          stage.show();

        } catch (IOException e) {
          e.printStackTrace();
        }
      });

      ivDeleteItem.setOnMouseClicked(event -> {
        POSItem posItem = (POSItem)getTableRow().getItem();
        netAmount=netAmount.subtract(posItem.getItemTotal());
        ttlDiscount = ttlDiscount.subtract(posItem.getItemDiscount());
        grossAmount=grossAmount.subtract(posItem.getRegularPrice().multiply(posItem.getItemQuantity()));
        ttlAddon = ttlAddon.subtract(posItem.getAddons().multiply(posItem.getItemQuantity()));
        lblTotal.setText(df.format(netAmount));
        lblDiscount.setText(df.format(ttlDiscount));
        lblGross.setText(df.format(grossAmount));
        lblAddon.setText(df.format(ttlAddon));
        posItems.remove(posItem);
      });

      hbxActionButtons.getChildren().addAll(ivEditItem, ivDeleteItem);
      vbxItemDetail.getChildren().addAll(lblItemName,lblPrice,lblClassification);
      hbxItemDetail.getChildren().addAll(imageView,vbxItemDetail, hbxActionButtons);

    }

    @Override
    protected void updateItem(Integer item, boolean empty) {
      super.updateItem(item, empty);
      setText(null);
      setGraphic(null);
      if(!empty && item != null) {
        POSItem pItem = (POSItem)getTableRow().getItem();
        if(pItem != null) {
          Circle circle = new Circle(30,30,30);
          imageView.setImage(pItem.getItemPhoto());
          imageView.getStyleClass().add("imageView");
          imageView.setFitHeight(60);
          imageView.setFitWidth(60);
          imageView.setClip(circle);
          lblItemName.setText(pItem.getItemName());
          lblPrice.setText("@" + df.format(pItem.getRegularPrice()));
          if(pItem.getAddons().compareTo(BigDecimal.ZERO) > 0) {
            lblPrice.setText("(" + lblPrice.getText() + " + Addon: " + df.format(pItem.getAddons()) + ")");
          }
          lblPrice.setText(lblPrice.getText() +
                  " x" + df.format(pItem.getItemQuantity()) + " " + pItem.getUom().getUomName());
          if(pItem.getItemDiscount().compareTo(BigDecimal.ZERO) > 0) {
            lblPrice.setText(lblPrice.getText() +
                    " Less: " + df.format(pItem.getItemDiscount()));
          }
          lblClassification.setText(pItem.getItemClassification().toString());

          setGraphic(hbxItemDetail);
        }
      }
    }
  }

}
