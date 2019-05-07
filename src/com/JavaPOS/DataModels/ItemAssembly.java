package com.JavaPOS.DataModels;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.math.BigDecimal;

public class ItemAssembly {

  private ObjectProperty<Integer> extItemID = new SimpleObjectProperty<>();
  private ObjectProperty<String> extItemCode = new SimpleObjectProperty<>();
  private StringProperty extItemName = new SimpleStringProperty();
  private ObjectProperty<Integer> extItemSubGroupID = new SimpleObjectProperty<>();
  private StringProperty extItemSubGroupName = new SimpleStringProperty();
  private ObjectProperty<Integer> uomID = new SimpleObjectProperty<>();
  private ObjectProperty<BigDecimal> regularPrice = new SimpleObjectProperty<>();
  private ObjectProperty<BigDecimal> extQty = new SimpleObjectProperty<>();
  private ObjectProperty<BigDecimal> priceLimit = new SimpleObjectProperty<>();
  //private ObjectProperty<BigDecimal> itemTotal = new SimpleObjectProperty<>();

  public Integer getExtItemID() {
    return extItemID.get();
  }

  public ObjectProperty<Integer> extItemIDProperty() {
    return extItemID;
  }

  public void setExtItemID(Integer extItemID) {
    this.extItemID.set(extItemID);
  }

  public String getExtItemCode() {
    return extItemCode.get();
  }

  public ObjectProperty<String> extItemCodeProperty() {
    return extItemCode;
  }

  public void setExtItemCode(String extItemCode) {
    this.extItemCode.set(extItemCode);
  }

  public String getExtItemName() {
    return extItemName.get();
  }

  public StringProperty extItemNameProperty() {
    return extItemName;
  }

  public void setExtItemName(String extItemName) {
    this.extItemName.set(extItemName);
  }

  public Integer getExtItemSubGroupID() {
    return extItemSubGroupID.get();
  }

  public ObjectProperty<Integer> extItemSubGroupIDProperty() {
    return extItemSubGroupID;
  }

  public void setExtItemSubGroupID(Integer extItemSubGroupID) {
    this.extItemSubGroupID.set(extItemSubGroupID);
  }

  public Integer getUomID() {
    return uomID.get();
  }

  public ObjectProperty<Integer> uomIDProperty() {
    return uomID;
  }

  public void setUomID(Integer uomID) {
    this.uomID.set(uomID);
  }

  public String getExtItemSubGroupName() {
    return extItemSubGroupName.get();
  }

  public StringProperty extItemSubGroupNameProperty() {
    return extItemSubGroupName;
  }

  public void setExtItemSubGroupName(String extItemSubGroupName) {
    this.extItemSubGroupName.set(extItemSubGroupName);
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

  public BigDecimal getExtQty() {
    return extQty.get();
  }

  public ObjectProperty<BigDecimal> extQtyProperty() {
    return extQty;
  }

  public void setExtQty(BigDecimal extQty) {
    this.extQty.set(extQty);
  }

  public BigDecimal getPriceLimit() {
    return priceLimit.get();
  }

  public ObjectProperty<BigDecimal> priceLimitProperty() {
    return priceLimit;
  }

  public void setPriceLimit(BigDecimal priceLimit) {
    this.priceLimit.set(priceLimit);
  }

//  public BigDecimal getItemTotal() {
//    return itemTotal.get();
//  }
//
//  public ObjectProperty<BigDecimal> itemTotalProperty() {
//    return itemTotal;
//  }
//
//  public void setItemTotal(BigDecimal itemTotal) {
//    this.itemTotal.set(itemTotal);
//  }
}
