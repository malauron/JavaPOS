package com.JavaPOS.DataModels;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UnitOfMeasure {
  private ObjectProperty<Integer> uomID = new SimpleObjectProperty<>();
  private StringProperty uomName = new SimpleStringProperty();

  public Integer getUomID() {
    return uomID.get();
  }

  public ObjectProperty<Integer> uomIDProperty() {
    return uomID;
  }

  public void setUomID(Integer uomID) {
    this.uomID.set(uomID);
  }

  public String getUomName() {
    return uomName.get();
  }

  public StringProperty uomNameProperty() {
    return uomName;
  }

  public void setUomName(String uomName) {
    this.uomName.set(uomName);
  }

  @Override
  public String toString() {
    return uomName.get() ;
  }
}
