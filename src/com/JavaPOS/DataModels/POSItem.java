package com.JavaPOS.DataModels;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;

import java.math.BigDecimal;

public class POSItem extends Item {

  private ObjectProperty<ObservableList<POSItemAssembly>> posItemAssembly = new SimpleObjectProperty<>();
  private ObjectProperty<BigDecimal> itemQuantity = new SimpleObjectProperty<>();
  private ObjectProperty<BigDecimal> itemDiscount = new SimpleObjectProperty<>();
  private ObjectProperty<BigDecimal> itemDiscPrct = new SimpleObjectProperty<>();
  private ObjectProperty<BigDecimal> addons = new SimpleObjectProperty<>();
  private ObjectProperty<BigDecimal> itemTotal = new SimpleObjectProperty<>();

  public ObservableList<POSItemAssembly> getPosItemAssembly() {
    return posItemAssembly.get();
  }

  public ObjectProperty<ObservableList<POSItemAssembly>> posItemAssemblyProperty() {
    return posItemAssembly;
  }

  public void setPosItemAssembly(ObservableList<POSItemAssembly> posItemAssembly) {
    this.posItemAssembly.set(posItemAssembly);
  }

  public BigDecimal getItemQuantity() {
    return itemQuantity.get();
  }

  public ObjectProperty<BigDecimal> itemQuantityProperty() {
    return itemQuantity;
  }

  public void setItemQuantity(BigDecimal itemQuantity) {
    this.itemQuantity.set(itemQuantity);
  }

  public BigDecimal getItemDiscount() {
    return itemDiscount.get();
  }

  public ObjectProperty<BigDecimal> itemDiscountProperty() {
    return itemDiscount;
  }

  public void setItemDiscount(BigDecimal itemDiscount) {
    this.itemDiscount.set(itemDiscount);
  }

  public BigDecimal getItemDiscPrct() {
    return itemDiscPrct.get();
  }

  public ObjectProperty<BigDecimal> itemDiscPrctProperty() {
    return itemDiscPrct;
  }

  public void setItemDiscPrct(BigDecimal itemDiscPrct) {
    this.itemDiscPrct.set(itemDiscPrct);
  }

  public BigDecimal getAddons() {
    return addons.get();
  }

  public ObjectProperty<BigDecimal> addonsProperty() {
    return addons;
  }

  public void setAddons(BigDecimal addons) {
    this.addons.set(addons);
  }

  public BigDecimal getItemTotal() {
    return itemTotal.get();
  }

  public ObjectProperty<BigDecimal> itemTotalProperty() {
    return itemTotal;
  }

  public void setItemTotal(BigDecimal itemTotal) {
    this.itemTotal.set(itemTotal);
  }


}
