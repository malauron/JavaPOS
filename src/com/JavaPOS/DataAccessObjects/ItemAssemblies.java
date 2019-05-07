package com.JavaPOS.DataAccessObjects;

import com.JavaPOS.DataModels.ItemAssembly;
import com.JavaPOS.Interfaces.IItemAssemblies;
import com.JavaPOS.Utilities.ConnectionMain;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.JavaPOS.DataAccessObjects.DataAccessConstants.*;

public class ItemAssemblies implements IItemAssemblies {

  @Override
  public ObservableList<ItemAssembly> getItemAssemblies(int itemID) {
    StringBuilder sql = new StringBuilder(
            "select x1.*,x2." + COL_ITEM_NAME + ",x2." + COL_REGULARPRICE + ",x2." + COL_UOM_ID +
            ",x3." + COL_ITEMSUBGROUP_ID  + ",x3." + COL_ITEMSUBGROUP_NAME +
            " from " + TBL_ITEMSASSEMBLIES + " x1 " +
            " left outer join " + TBL_ITEMS + " x2 on x1." + COL_EXTITEMID + " = x2." + COL_ITEM_ID +
            " left outer join " + TBL_ITEMSUBGROUPS + " x3 on x1." + COL_EXTITEMSUBGROUPID + " = x3." + COL_ITEMSUBGROUP_ID +
            " where x1." + COL_ITEM_ID + " = " + itemID
    );
    ObservableList<ItemAssembly> assemblies = FXCollections.observableArrayList();
    try {
      ResultSet rs = ConnectionMain.getInstance().getResultSet(sql.toString());
      while(rs.next()) {
        ItemAssembly item = new ItemAssembly();
        item.setExtItemID(rs.getInt(COL_EXTITEMID));
        item.setExtItemSubGroupID(rs.getInt(COL_EXTITEMSUBGROUPID));
        if(item.getExtItemID().equals(0)) {
          if(item.getExtItemSubGroupID().equals(0)) {
            item.setExtItemName("* (ANY ITEM)");
          } else {
            item.setExtItemName("* (ANY " + rs.getString(COL_ITEMSUBGROUP_NAME).toUpperCase() + ")");
          }
        } else {
          item.setExtItemName(rs.getString(COL_ITEM_NAME).toUpperCase());
          item.setExtItemSubGroupID(rs.getInt(COL_ITEMSUBGROUP_ID));
        }
        if(item.getExtItemSubGroupID().equals(0)) {
          item.setExtItemSubGroupName("* (ANY GROUP)");
        } else {
          item.setExtItemSubGroupName(rs.getString(COL_ITEMSUBGROUP_NAME).toUpperCase());
        }
        if(item.getExtItemID().equals(0)){
          item.setRegularPrice(BigDecimal.valueOf(0));
        } else {
          item.setRegularPrice(rs.getBigDecimal(COL_REGULARPRICE));
        }
        item.setUomID(rs.getInt(COL_UOM_ID));
        item.setExtQty(rs.getBigDecimal(COL_EXTQTY));
        item.setPriceLimit(rs.getBigDecimal(COL_PRICELIMIT));
        assemblies.add(item);
      }
      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return assemblies;
  }

  @Override
  public void handleItemAssemblies(int itemID, ObservableList<ItemAssembly> itemAssemblies) throws SQLException,ClassNotFoundException {
    StringBuilder sql = new StringBuilder();
    PreparedStatement ps = null;

    try {
      sql.append("delete from " + TBL_ITEMSASSEMBLIES + " where " + COL_ITEM_ID + " = " + itemID);
      ps = ConnectionMain.getInstance().cn().prepareStatement(sql.toString());
      ps.executeUpdate();
      ps.close();
      sql.setLength(0);
      sql.append("insert into " + TBL_ITEMSASSEMBLIES + "(" + COL_ITEM_ID + "," + COL_EXTITEMID +
              "," + COL_EXTITEMSUBGROUPID + "," + COL_EXTQTY + "," + COL_PRICELIMIT + ") values (?,?,?,?,?)");
      for(ItemAssembly itemAssembly:itemAssemblies) {
        ps = ConnectionMain.getInstance().cn().prepareStatement(sql.toString());
        ps.setInt(1,itemID);
        ps.setInt(2,itemAssembly.getExtItemID());
        ps.setInt(3,itemAssembly.getExtItemSubGroupID());
        ps.setBigDecimal(4,itemAssembly.getExtQty());
        ps.setBigDecimal(5,itemAssembly.getPriceLimit());
        ps.executeUpdate();
        ps.close();
      }
    } finally {
      if(ps!=null && !ps.isClosed()) {
        try {
          ps.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }



  }
}
