package com.JavaPOS.DataAccessObjects;

import com.JavaPOS.DataModels.Item;
import com.JavaPOS.DataModels.POSItem;
import com.JavaPOS.DataModels.POSItemAssembly;
import com.JavaPOS.DataModels.POSTransaction;
import com.JavaPOS.Utilities.ConnectionMain;
import com.JavaPOS.Utilities.CurrentUser;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.JavaPOS.DataAccessObjects.DataAccessConstants.*;

public class POSTransactions {

  public void handlePOSItem(POSTransaction posTransaction) throws SQLException, ClassNotFoundException {

    StringBuilder sql = new StringBuilder();
    StringBuilder sqlItemAssembly = new StringBuilder();
    PreparedStatement ps = null;
    ResultSet lastID = null;

    try {

      ConnectionMain.getInstance().cn().setAutoCommit(false);

      sql.append("insert into " + TBL_TRANSACTIONS_HEADER +
              "(" + COL_SHIFT_ID + "," + COL_CUSTOMER_ID +
              "," + COL_CUSTOMERGROUP_ID + "," + COL_CUSTOMERSUBGROUP_ID +
              "," + COL_TOTALCASH + "," + COL_TOTALCHECK + "," + COL_TOTALCHARGED +
              "," + COL_TOTALCARD + "," + COL_TOTALPREPAID + "," + COL_AMOUNTDUE +
              "," + COL_DISCAMT + "," + COL_DISCPRCT + "," + COL_TOTALVAT +
              "," + COL_CASHTENDERED + "," + COL_CASHCHANGE + "," + COL_USER_ID +
              "," + COL_TRNXDATE + "," + COL_TRNXTIME + ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),now())");

      ps = ConnectionMain.getInstance().cn().prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
      ps.setInt(1,posTransaction.getShiftdetail_id());
      ps.setInt(2,posTransaction.getCustomer_id());
      ps.setInt(3,posTransaction.getCustomergroup_id());
      ps.setInt(4,posTransaction.getCustomersubgroup_id());
      ps.setBigDecimal(5,posTransaction.getTotal_cash());
      ps.setBigDecimal(6,posTransaction.getTotal_check());
      ps.setBigDecimal(7,posTransaction.getTotal_charged());
      ps.setBigDecimal(8,posTransaction.getTotal_card());
      ps.setBigDecimal(9,posTransaction.getTotal_prepaid());
      ps.setBigDecimal(10,posTransaction.getTotal_amount_due());
      ps.setBigDecimal(11,posTransaction.getTotal_disc_amt());
      ps.setBigDecimal(12,posTransaction.getTotal_disc_prct());
      ps.setBigDecimal(13,posTransaction.getTotal_vat());
      ps.setBigDecimal(14,posTransaction.getCash_tendered());
      ps.setBigDecimal(15,posTransaction.getCash_change());
      ps.setInt(16, CurrentUser.getInstance().getUserID());
      ps.executeUpdate();
      lastID = ps.getGeneratedKeys();
      lastID.next();
      posTransaction.setTransaction_id(lastID.getInt(1));
      lastID.close();
      ps.close();

      int ctr = 0;
      sql.setLength(0);
      sql.append("insert into " + TBL_TRANSACTIONS_ITEMS + "(" + COL_TRANSACTION_ID +
              "," + COL_SHIFT_ID + "," + COL_LINENO + "," + COL_ITEM_ID +
              "," + COL_ITEMSUBGROUP_ID + "," + COL_UOM_ID + "," + COL_ITEMPRICE +
              "," + COL_ITEMQUANTITY + "," + COL_ITEMADDON + "," + COL_ITEMDISCAMT +
              "," + COL_ITEMDISCPRCT + "," + COL_ITEMTOTAL + ") values (?,?,?,?,?,?,?,?,?,?,?,?)");

      sqlItemAssembly.append("insert into " + TBL_ITEMS_ASSEMBLIES  +
              "(" + COL_TRANSACTION_ID + "," + COL_LINENO + "," + COL_ITEM_ID + "," + COL_ITEMSUBGROUP_ID +
              "," + COL_UOM_ID + "," + COL_ITEMPRICE + "," + COL_ITEMQUANTITY + "," + COL_PRICELIMIT +
              "," + COL_ITEMADDON + ") values (?,?,?,?,?,?,?,?,?)");

      for(POSItem posItem : posTransaction.getPosItems()) {
        ctr++;

        ps = ConnectionMain.getInstance().cn().prepareStatement(sql.toString());
        ps.setInt(1,posTransaction.getTransaction_id());
        ps.setInt(2,posTransaction.getShiftdetail_id());
        ps.setInt(3,ctr);
        ps.setInt(4,posItem.getItemID());
        ps.setInt(5,posItem.getItemSubGroup().getItemSubGroupID());
        ps.setInt(6,posItem.getUom().getUomID());
        ps.setBigDecimal(7,posItem.getRegularPrice());
        ps.setBigDecimal(8,posItem.getItemQuantity());
        ps.setBigDecimal(9,posItem.getAddons());
        ps.setBigDecimal(10,posItem.getItemDiscount());
        ps.setBigDecimal(11,posItem.getItemDiscPrct());
        ps.setBigDecimal(12,posItem.getItemTotal());
        ps.executeUpdate();
        ps.close();

        if(posItem.getItemClassification().equals(Item.ItemClassification.ASSEMBLY)) {
          for(POSItemAssembly posItemAssembly : posItem.getPosItemAssembly()) {
            ps = ConnectionMain.getInstance().cn().prepareStatement(sqlItemAssembly.toString());
            ps.setInt(1, posTransaction.getTransaction_id());
            ps.setInt(2, ctr);
            ps.setInt(3, posItemAssembly.getActItemID());
            ps.setInt(4, posItemAssembly.getActItemSubGroupID());
            ps.setInt(5, posItemAssembly.getUomID());
            ps.setBigDecimal(6, posItemAssembly.getRegularPrice());
            ps.setBigDecimal(7, posItemAssembly.getExtQty());
            ps.setBigDecimal(8, posItemAssembly.getPriceLimit());
            ps.setBigDecimal(9, posItemAssembly.getAddOnAmt());
            ps.executeUpdate();
            ps.close();
          }
        }
      }

      ConnectionMain.getInstance().cn().commit();

    } catch (SQLException se) {
      try {
        ConnectionMain.getInstance().cn().rollback();
      } catch (SQLException | ClassNotFoundException e) {
        e.printStackTrace();
      }
      throw se;
    } finally {
      if(ps != null) {
        try {
          ps.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
      if(lastID != null) {
        try {
          lastID.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
      try {
        ConnectionMain.getInstance().cn().setAutoCommit(true);
      } catch (SQLException | ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
  }

}
