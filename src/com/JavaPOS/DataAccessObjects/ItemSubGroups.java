package com.JavaPOS.DataAccessObjects;

import com.JavaPOS.DataModels.ItemGroup;
import com.JavaPOS.DataModels.ItemSubGroup;
import com.JavaPOS.Interfaces.IItemGroups;
import com.JavaPOS.Interfaces.IItemSubGroups;
import com.JavaPOS.Utilities.ConnectionMain;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;

import static com.JavaPOS.DataAccessObjects.DataAccessConstants.*;

public class ItemSubGroups implements IItemSubGroups {

  @Override
  public ItemSubGroup getItemSubGroup(int id) {
    StringBuilder sql = new StringBuilder("select "+ COL_ITEMGROUP_ID +","+ COL_ITEMSUBGROUP_ID +",ucase("+ COL_ITEMSUBGROUP_NAME +") "+ COL_ITEMSUBGROUP_NAME +
            " from "+ TBL_ITEMSUBGROUPS +" where "+ COL_ITEMSUBGROUP_ID +" = " + id) ;
    try {
      ResultSet rs = ConnectionMain.getInstance().getResultSet(sql.toString());
      while (rs.next()) {
        ItemSubGroup itemSubGroup = new ItemSubGroup();
        IItemGroups itemGroups = new ItemGroups();
        itemSubGroup.setItemSubGroupID(rs.getInt(COL_ITEMSUBGROUP_ID));
        itemSubGroup.setItemGroup(itemGroups.getItemGroup(rs.getInt(COL_ITEMGROUP_ID)));
        itemSubGroup.setItemSubGroupName(rs.getString(COL_ITEMSUBGROUP_NAME));
        return itemSubGroup;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public ObservableList<ItemSubGroup> getItemSubGroups() {

    StringBuilder sql = new StringBuilder("select "+ COL_ITEMGROUP_ID +","+ COL_ITEMSUBGROUP_ID +",ucase("+ COL_ITEMSUBGROUP_NAME +") "+ COL_ITEMSUBGROUP_NAME +
            " from "+ TBL_ITEMSUBGROUPS +" order by  "+ COL_ITEMSUBGROUP_NAME) ;
    try {
      ObservableList<ItemSubGroup> itemSubGroups = FXCollections.observableArrayList();
      ResultSet rs = ConnectionMain.getInstance().getResultSet(sql.toString());
      while (rs.next()) {
        ItemSubGroup itemSubGroup = new ItemSubGroup();
        IItemGroups itemGroups = new ItemGroups();
        itemSubGroup.setItemSubGroupID(rs.getInt(COL_ITEMSUBGROUP_ID));
        itemSubGroup.setItemGroup(itemGroups.getItemGroup(rs.getInt(COL_ITEMGROUP_ID)));
        itemSubGroup.setItemSubGroupName(rs.getString(COL_ITEMSUBGROUP_NAME));
        itemSubGroups.add(itemSubGroup);
      }
      return itemSubGroups;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public ObservableList<ItemSubGroup> getDummyItemSubGroups() {
    ObservableList<ItemSubGroup> itemSubGroups = FXCollections.observableArrayList();
    ItemSubGroup itemSubGroup = new ItemSubGroup();
    ItemGroup itemGroup = new ItemGroup();
    itemGroup.setItemGroupID(0);
    itemGroup.setItemGroupName("ANY GROUP");
    itemSubGroup.setItemSubGroupID(0);
    itemSubGroup.setItemSubGroupName("*");
    itemSubGroup.setItemGroup(itemGroup);
    itemSubGroups.add(itemSubGroup);
    itemSubGroups.addAll(getItemSubGroups());
    return itemSubGroups;
  }

}
