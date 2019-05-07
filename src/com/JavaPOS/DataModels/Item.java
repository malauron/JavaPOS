package com.JavaPOS.DataModels;

import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.math.BigDecimal;

public class Item {

  private ObjectProperty<Integer> itemID = new SimpleObjectProperty<>();
  private ObjectProperty<ItemSubGroup> itemSubGroup = new SimpleObjectProperty<>();
  private StringProperty itemCode = new SimpleStringProperty();
  private StringProperty itemName = new SimpleStringProperty();
  private StringProperty altDesc = new SimpleStringProperty();
  private ObjectProperty<UnitOfMeasure> uom = new SimpleObjectProperty<>();
  private ObjectProperty<ItemClassification> itemClassification = new SimpleObjectProperty<>();
  private ObjectProperty<BigDecimal> regularPrice = new SimpleObjectProperty<>();
  private ObjectProperty<ObservableList<ItemAssembly>> itemAssembly = new SimpleObjectProperty<>();
  private BooleanProperty isActive = new SimpleBooleanProperty();
  private ObjectProperty<Image> itemPhoto = new SimpleObjectProperty<>();

  public enum ItemClassification {
    STOCK, SERVICE, ASSEMBLY;
    @Override
    public String toString() {
      return this.name();
    }
  }

  public Integer getItemID() {
    return itemID.get();
  }

  public ObjectProperty<Integer> itemIDProperty() {
    return itemID;
  }

  public void setItemID(Integer itemID) {
    this.itemID.set(itemID);
  }

  public ItemSubGroup getItemSubGroup() {
    return itemSubGroup.get();
  }

  public ObjectProperty<ItemSubGroup> itemSubGroupProperty() {
    return itemSubGroup;
  }

  public void setItemSubGroup(ItemSubGroup itemSubGroup) {
    this.itemSubGroup.set(itemSubGroup);
  }

  public String getItemCode() {
    return itemCode.get();
  }

  public StringProperty itemCodeProperty() {
    return itemCode;
  }

  public void setItemCode(String itemCode) {
    this.itemCode.set(itemCode);
  }

  public String getItemName() {
    return itemName.get();
  }

  public StringProperty itemNameProperty() {
    return itemName;
  }

  public void setItemName(String itemName) {
    this.itemName.set(itemName);
  }

  public String getAltDesc() {
    return altDesc.get();
  }

  public StringProperty altDescProperty() {
    return altDesc;
  }

  public void setAltDesc(String altDesc) {
    this.altDesc.set(altDesc);
  }

  public UnitOfMeasure getUom() {
    return uom.get();
  }

  public ObjectProperty<UnitOfMeasure> uomProperty() {
    return uom;
  }

  public void setUom(UnitOfMeasure uom) {
    this.uom.set(uom);
  }

  public ItemClassification getItemClassification() {
    return itemClassification.get();
  }

  public ObjectProperty<ItemClassification> itemClassificationProperty() {
    return itemClassification;
  }

  public void setItemClassification(ItemClassification itemClassification) {
    this.itemClassification.set(itemClassification);
  }

  public BigDecimal getRegularPrice() {
    return regularPrice.get();
  }

  public ObjectProperty<BigDecimal> regularPriceProperty() {
    return regularPrice;
  }

  public void setRegularPrice(BigDecimal regularPrice) {
    this.regularPrice.set(regularPrice);
  }

  public ObservableList<ItemAssembly> getItemAssembly() {
    return itemAssembly.get();
  }

  public ObjectProperty<ObservableList<ItemAssembly>> itemAssemblyProperty() {
    return itemAssembly;
  }

  public void setItemAssembly(ObservableList<ItemAssembly> itemAssembly) {
    this.itemAssembly.set(itemAssembly);
  }

  public boolean isIsActive() {
    return isActive.get();
  }

  public BooleanProperty isActiveProperty() {
    return isActive;
  }

  public void setIsActive(boolean isActive) {
    this.isActive.set(isActive);
  }

  public Image getItemPhoto() {
    return itemPhoto.get();
  }

  public ObjectProperty<Image> itemPhotoProperty() {
    return itemPhoto;
  }

  public void setItemPhoto(Image itemPhoto) {
    this.itemPhoto.set(itemPhoto);
  }

  @Override
  public String toString() {
    return itemName.get();
  }
}
