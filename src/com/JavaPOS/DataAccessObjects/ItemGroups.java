package com.JavaPOS.DataAccessObjects;

import com.JavaPOS.DataModels.ItemGroup;
import com.JavaPOS.Interfaces.IItemGroups;
import com.JavaPOS.Utilities.ConnectionMain;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;

import static com.JavaPOS.DataAccessObjects.DataAccessConstants.*;

public class ItemGroups implements IItemGroups {

  @Override
  public ItemGroup getItemGroup(int id) {
    StringBuilder sql = new StringBuilder("select "+ COL_ITEMGROUP_ID +",ucase("+ COL_ITEMGROUP_NAME +") "+ COL_ITEMGROUP_NAME +
            " from "+ TBL_ITEMGROUPS +" where "+ COL_ITEMGROUP_ID +" = " + id) ;
    try {
      ResultSet rs = ConnectionMain.getInstance().getResultSet(sql.toString());
      while (rs.next()) {
        ItemGroup itemGroup = new ItemGroup();
        itemGroup.setItemGroupID(rs.getInt(COL_ITEMGROUP_ID));
        itemGroup.setItemGroupName(rs.getString(COL_ITEMGROUP_NAME));
        return itemGroup;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public ObservableList<ItemGroup> getItemGroups() {
    StringBuilder sql = new StringBuilder("select "+ COL_ITEMGROUP_ID +",ucase("+ COL_ITEMGROUP_NAME +") "+ COL_ITEMGROUP_NAME +
            " from "+ TBL_ITEMGROUPS +" order by  "+ COL_ITEMGROUP_NAME) ;
    try {
      ObservableList<ItemGroup> itemGroups = FXCollections.observableArrayList();
      ResultSet rs = ConnectionMain.getInstance().getResultSet(sql.toString());
      while (rs.next()) {
        ItemGroup itemGroup = new ItemGroup();
        itemGroup.setItemGroupID(rs.getInt(COL_ITEMGROUP_ID));
        itemGroup.setItemGroupName(rs.getString(COL_ITEMGROUP_NAME));
        itemGroups.add(itemGroup);
      }
      return itemGroups;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

}
