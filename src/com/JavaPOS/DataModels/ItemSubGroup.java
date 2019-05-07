package com.JavaPOS.DataModels;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ItemSubGroup {
  private ObjectProperty<Integer> itemSubGroupID = new SimpleObjectProperty<>();
  private ObjectProperty<ItemGroup> itemGroup = new SimpleObjectProperty<>();
  private StringProperty itemSubGroupName = new SimpleStringProperty();

  public Integer getItemSubGroupID() {
    return itemSubGroupID.get();
  }

  public ObjectProperty<Integer> itemSubGroupIDProperty() {
    return itemSubGroupID;
  }

  public void setItemSubGroupID(Integer itemSubGroupID) {
    this.itemSubGroupID.set(itemSubGroupID);
  }

  public ItemGroup getItemGroup() {
    return itemGroup.get();
  }

  public ObjectProperty<ItemGroup> itemGroupProperty() {
    return itemGroup;
  }

  public void setItemGroup(ItemGroup itemGroup) {
    this.itemGroup.set(itemGroup);
  }

  public String getItemSubGroupName() {
    return itemSubGroupName.get();
  }

  public StringProperty itemSubGroupNameProperty() {
    return itemSubGroupName;
  }

  public void setItemSubGroupName(String itemSubGroupName) {
    this.itemSubGroupName.set(itemSubGroupName);
  }

  @Override
  public String toString() {
    return itemSubGroupName.get() + " ("+ itemGroup.getValue().getItemGroupName() +")" ;
  }
}
