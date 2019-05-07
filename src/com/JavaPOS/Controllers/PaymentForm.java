package com.JavaPOS.Controllers;

import com.JavaPOS.Interfaces.IPaymentForm;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class PaymentForm {
  @FXML
  private JFXTextField txtBalance;
  @FXML
  private JFXTextField txtAmountDue;
  @FXML
  private JFXTextField txtCashTendered;
  @FXML
  private JFXTextField txtCashChange;
  @FXML
  private JFXTextField txtCardAmount;
  @FXML
  private JFXButton btnSave;
  @FXML
  private JFXButton btnCancel;

  private IPaymentForm iPaymentForm;
  private DecimalFormat df = new DecimalFormat("#,##0.00");
  private DecimalFormat dfNoComma = new DecimalFormat("###0.00");
  private BigDecimal amountDue;
  private BigDecimal balance;
  private BigDecimal cashTendered;
  private BigDecimal cashChange;
  private BigDecimal cashAmount;
  private BigDecimal cardAmount;
  private BigDecimal chargeAmount;

  public PaymentForm(BigDecimal amountDue) {
    this.amountDue = amountDue;
    balance = amountDue;
    cashTendered = BigDecimal.ZERO;
    cashChange = BigDecimal.ZERO;
    cashAmount = BigDecimal.ZERO;
    cardAmount = BigDecimal.ZERO;
    chargeAmount = BigDecimal.ZERO;
  }

  public void initialize() {

    txtAmountDue.setText(df.format(amountDue));
    txtBalance.setText(df.format(balance));
    txtCashTendered.setText("0.00");
    txtCashChange.setText("0.00");
    txtCardAmount.setText("0.00");

    txtCashTendered.textProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue.matches("\\d{0,9}([.]\\d{0,2})?")) {
        txtCashTendered.setText(oldValue);
      } else {
        if(!newValue.isEmpty()) {
          cashTendered = BigDecimal.valueOf(Double.parseDouble(newValue));
          if(cashTendered.compareTo(amountDue.subtract(cardAmount)) > 0) {
            cashAmount = amountDue.subtract(cardAmount);
            cashChange = cashTendered.subtract(cashAmount);
            balance = BigDecimal.ZERO;
          } else {
            cashAmount = cashTendered;
            cashChange = BigDecimal.ZERO;
            balance = amountDue.subtract(cashTendered).subtract(cardAmount);
          }
          txtCashChange.setText(df.format(cashChange));
          txtBalance.setText(df.format(balance));
        } else {
          txtCashTendered.setText("0.00");
          txtCashTendered.requestFocus();
        }
      }
    });

    txtCardAmount.textProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue.matches("\\d{0,9}([.]\\d{0,2})?")) {
        txtCardAmount.setText(oldValue);
      } else {
        if(!newValue.isEmpty()) {
          cardAmount = BigDecimal.valueOf(Double.parseDouble(newValue));
          if(cardAmount.compareTo(amountDue.subtract(cashAmount)) > 0) {
            cardAmount = amountDue.subtract(cashAmount);
            txtCardAmount.setText(dfNoComma.format(cardAmount));
          }
          balance = amountDue.subtract(cashAmount).subtract(cardAmount);
          txtBalance.setText(df.format(balance));
        } else {
          txtCardAmount.setText("0.00");
          txtCardAmount.requestFocus();
        }
      }
    });

    btnCancel.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        ((Stage)((Node)(event.getSource())).getScene().getWindow()).close();
      }
    });

    btnSave.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        if(iPaymentForm != null) {
          iPaymentForm.savePOSTransaction();
          ((Stage)((Node)(event.getSource())).getScene().getWindow()).close();
        }
      }
    });
  }

  public void setIPaymentForm(IPaymentForm iPaymentForm) {
    this.iPaymentForm = iPaymentForm;
  }

  public BigDecimal getAmountDue() {
    return amountDue;
  }

  public void setAmountDue(BigDecimal amountDue) {
    this.amountDue = amountDue;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  public BigDecimal getCashTendered() {
    return cashTendered;
  }

  public void setCashTendered(BigDecimal cashTendered) {
    this.cashTendered = cashTendered;
  }

  public BigDecimal getCashChange() {
    return cashChange;
  }

  public void setCashChange(BigDecimal cashChange) {
    this.cashChange = cashChange;
  }

  public BigDecimal getCashAmount() {
    return cashAmount;
  }

  public void setCashAmount(BigDecimal cashAmount) {
    this.cashAmount = cashAmount;
  }

  public BigDecimal getCardAmount() {
    return cardAmount;
  }

  public void setCardAmount(BigDecimal cardAmount) {
    this.cardAmount = cardAmount;
  }

  public BigDecimal getChargeAmount() {
    return chargeAmount;
  }

  public void setChargeAmount(BigDecimal chargeAmount) {
    this.chargeAmount = chargeAmount;
  }
}
