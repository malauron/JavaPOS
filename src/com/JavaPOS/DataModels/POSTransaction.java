package com.JavaPOS.DataModels;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;

import java.math.BigDecimal;

public class POSTransaction {

  private ObjectProperty<Integer> transaction_id = new SimpleObjectProperty<>();
  private ObjectProperty<Integer> shiftdetail_id = new SimpleObjectProperty<>();
  private ObjectProperty<Integer>  customer_id = new SimpleObjectProperty<>();
  private ObjectProperty<Integer>  customergroup_id = new SimpleObjectProperty<>();
  private ObjectProperty<Integer>  customersubgroup_id = new SimpleObjectProperty<>();
  private ObjectProperty<BigDecimal>  total_cash = new SimpleObjectProperty<>();
  private ObjectProperty<BigDecimal>  total_check = new SimpleObjectProperty<>();
  private ObjectProperty<BigDecimal>  total_charged = new SimpleObjectProperty<>();
  private ObjectProperty<BigDecimal>  total_card = new SimpleObjectProperty<>();
  private ObjectProperty<BigDecimal>  total_prepaid = new SimpleObjectProperty<>();
  private ObjectProperty<BigDecimal>  total_disc_prct = new SimpleObjectProperty<>();
  private ObjectProperty<BigDecimal>  total_disc_amt = new SimpleObjectProperty<>();
  private ObjectProperty<BigDecimal>  total_amount_due = new SimpleObjectProperty<>();
  private ObjectProperty<BigDecimal>  total_vat = new SimpleObjectProperty<>();
  private ObjectProperty<BigDecimal>  cash_tendered = new SimpleObjectProperty<>();
  private ObjectProperty<BigDecimal>  cash_change = new SimpleObjectProperty<>();
  private ObjectProperty<ObservableList<POSItem>> posItems = new SimpleObjectProperty<>();

  public Integer getTransaction_id() {
    return transaction_id.get();
  }

  public ObjectProperty<Integer> transaction_idProperty() {
    return transaction_id;
  }

  public void setTransaction_id(Integer transaction_id) {
    this.transaction_id.set(transaction_id);
  }

  public Integer getShiftdetail_id() {
    return shiftdetail_id.get();
  }

  public ObjectProperty<Integer> shiftdetail_idProperty() {
    return shiftdetail_id;
  }

  public void setShiftdetail_id(Integer shiftdetail_id) {
    this.shiftdetail_id.set(shiftdetail_id);
  }

  public Integer getCustomer_id() {
    return customer_id.get();
  }

  public ObjectProperty<Integer> customer_idProperty() {
    return customer_id;
  }

  public void setCustomer_id(Integer customer_id) {
    this.customer_id.set(customer_id);
  }

  public Integer getCustomergroup_id() {
    return customergroup_id.get();
  }

  public ObjectProperty<Integer> customergroup_idProperty() {
    return customergroup_id;
  }

  public void setCustomergroup_id(Integer customergroup_id) {
    this.customergroup_id.set(customergroup_id);
  }

  public Integer getCustomersubgroup_id() {
    return customersubgroup_id.get();
  }

  public ObjectProperty<Integer> customersubgroup_idProperty() {
    return customersubgroup_id;
  }

  public void setCustomersubgroup_id(Integer customersubgroup_id) {
    this.customersubgroup_id.set(customersubgroup_id);
  }

  public BigDecimal getTotal_cash() {
    return total_cash.get();
  }

  public ObjectProperty<BigDecimal> total_cashProperty() {
    return total_cash;
  }

  public void setTotal_cash(BigDecimal total_cash) {
    this.total_cash.set(total_cash);
  }

  public BigDecimal getTotal_check() {
    return total_check.get();
  }

  public ObjectProperty<BigDecimal> total_checkProperty() {
    return total_check;
  }

  public void setTotal_check(BigDecimal total_check) {
    this.total_check.set(total_check);
  }

  public BigDecimal getTotal_charged() {
    return total_charged.get();
  }

  public ObjectProperty<BigDecimal> total_chargedProperty() {
    return total_charged;
  }

  public void setTotal_charged(BigDecimal total_charged) {
    this.total_charged.set(total_charged);
  }

  public BigDecimal getTotal_card() {
    return total_card.get();
  }

  public ObjectProperty<BigDecimal> total_cardProperty() {
    return total_card;
  }

  public void setTotal_card(BigDecimal total_card) {
    this.total_card.set(total_card);
  }

  public BigDecimal getTotal_prepaid() {
    return total_prepaid.get();
  }

  public ObjectProperty<BigDecimal> total_prepaidProperty() {
    return total_prepaid;
  }

  public void setTotal_prepaid(BigDecimal total_prepaid) {
    this.total_prepaid.set(total_prepaid);
  }

  public BigDecimal getTotal_disc_prct() {
    return total_disc_prct.get();
  }

  public ObjectProperty<BigDecimal> total_disc_prctProperty() {
    return total_disc_prct;
  }

  public void setTotal_disc_prct(BigDecimal total_disc_prct) {
    this.total_disc_prct.set(total_disc_prct);
  }

  public BigDecimal getTotal_disc_amt() {
    return total_disc_amt.get();
  }

  public ObjectProperty<BigDecimal> total_disc_amtProperty() {
    return total_disc_amt;
  }

  public void setTotal_disc_amt(BigDecimal total_disc_amt) {
    this.total_disc_amt.set(total_disc_amt);
  }

  public BigDecimal getTotal_amount_due() {
    return total_amount_due.get();
  }

  public ObjectProperty<BigDecimal> total_amount_dueProperty() {
    return total_amount_due;
  }

  public void setTotal_amount_due(BigDecimal total_amount_due) {
    this.total_amount_due.set(total_amount_due);
  }

  public BigDecimal getTotal_vat() {
    return total_vat.get();
  }

  public ObjectProperty<BigDecimal> total_vatProperty() {
    return total_vat;
  }

  public void setTotal_vat(BigDecimal total_vat) {
    this.total_vat.set(total_vat);
  }

  public BigDecimal getCash_tendered() {
    return cash_tendered.get();
  }

  public ObjectProperty<BigDecimal> cash_tenderedProperty() {
    return cash_tendered;
  }

  public void setCash_tendered(BigDecimal cash_tendered) {
    this.cash_tendered.set(cash_tendered);
  }

  public BigDecimal getCash_change() {
    return cash_change.get();
  }

  public ObjectProperty<BigDecimal> cash_changeProperty() {
    return cash_change;
  }

  public void setCash_change(BigDecimal cash_change) {
    this.cash_change.set(cash_change);
  }

  public ObservableList<POSItem> getPosItems() {
    return posItems.get();
  }

  public ObjectProperty<ObservableList<POSItem>> posItemsProperty() {
    return posItems;
  }

  public void setPosItems(ObservableList<POSItem> posItems) {
    this.posItems.set(posItems);
  }
}
