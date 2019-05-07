package com.JavaPOS.DataModels;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import java.math.BigDecimal;

public class POSItemAssembly extends ItemAssembly {
  private ObjectProperty<Integer> actItemID = new SimpleObjectProperty<>();
  private ObjectProperty<Integer> actItemSubGroupID = new SimpleObjectProperty<>();
  private ObjectProperty<BigDecimal> addOnAmt = new SimpleObjectProperty<>();

  public Integer getActItemID() {
    return actItemID.get();
  }

  public ObjectProperty<Integer> actItemIDProperty() {
    return actItemID;
  }

  public void setActItemID(Integer actItemID) {
    this.actItemID.set(actItemID);
  }

  public Integer getActItemSubGroupID() {
    return actItemSubGroupID.get();
  }

  public ObjectProperty<Integer> actItemSubGroupIDProperty() {
    return actItemSubGroupID;
  }

  public void setActItemSubGroupID(Integer actItemSubGroupID) {
    this.actItemSubGroupID.set(actItemSubGroupID);
  }

  public BigDecimal getAddOnAmt() {
    return addOnAmt.get();
  }

  public ObjectProperty<BigDecimal> addOnAmtProperty() {
    return addOnAmt;
  }

  public void setAddOnAmt(BigDecimal addOnAmt) {
    this.addOnAmt.set(addOnAmt);
  }
}
