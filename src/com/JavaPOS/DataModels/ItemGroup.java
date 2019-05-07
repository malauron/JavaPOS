package com.JavaPOS.DataModels;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ItemGroup {
  private ObjectProperty<Integer> itemGroupID = new SimpleObjectProperty<>();
  private StringProperty itemGroupName = new SimpleStringProperty();

  public Integer getItemGroupID() {
    return itemGroupID.get();
  }

  public ObjectProperty<Integer> itemGroupIDProperty() {
    return itemGroupID;
  }

  public void setItemGroupID(Integer itemGroupID) {
    this.itemGroupID.set(itemGroupID);
  }

  public String getItemGroupName() {
    return itemGroupName.get();
  }

  public StringProperty itemGroupNameProperty() {
    return itemGroupName;
  }

  public void setItemGroupName(String itemGroupName) {
    this.itemGroupName.set(itemGroupName);
  }

  @Override
  public String toString() {
    return itemGroupName.get();
  }
}
