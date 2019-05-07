package com.JavaPOS.DataModels;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.Date;

public class Shiftdetail {

  private ObjectProperty<Integer> shiftdetail_id = new SimpleObjectProperty<>();
  private ObjectProperty<Date> starttrans = new SimpleObjectProperty<>();
  private ObjectProperty<Date> endtrans = new SimpleObjectProperty<>();

  public Integer getShiftdetail_id() {
    return shiftdetail_id.get();
  }

  public ObjectProperty<Integer> shiftdetail_idProperty() {
    return shiftdetail_id;
  }

  public void setShiftdetail_id(Integer shiftdetail_id) {
    this.shiftdetail_id.set(shiftdetail_id);
  }

  public Date getStarttrans() {
    return starttrans.get();
  }

  public ObjectProperty<Date> starttransProperty() {
    return starttrans;
  }

  public void setStarttrans(Date starttrans) {
    this.starttrans.set(starttrans);
  }

  public Date getEndtrans() {
    return endtrans.get();
  }

  public ObjectProperty<Date> endtransProperty() {
    return endtrans;
  }

  public void setEndtrans(Date endtrans) {
    this.endtrans.set(endtrans);
  }

  @Override
  public String toString() {
    return "Shiftdetail{" +
            "shiftdetail_id=" + shiftdetail_id +
            ", starttrans=" + starttrans +
            ", endtrans=" + endtrans +
            '}';
  }
}
