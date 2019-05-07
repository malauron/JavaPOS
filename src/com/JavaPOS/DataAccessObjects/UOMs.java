package com.JavaPOS.DataAccessObjects;

import com.JavaPOS.DataModels.UnitOfMeasure;
import com.JavaPOS.Interfaces.IUOMs;
import com.JavaPOS.Utilities.ConnectionMain;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;

import static com.JavaPOS.DataAccessObjects.DataAccessConstants.*;

public class UOMs implements IUOMs {

  @Override
  public UnitOfMeasure getUOM(int id) {
    StringBuilder sql = new StringBuilder("select "+ COL_UOM_ID +",ucase("+ COL_UOM_NAME +") "+ COL_UOM_NAME +
            " from "+ TBL_UOMS +" where "+ COL_UOM_ID +" = " + id) ;
    try {
      ResultSet rs = ConnectionMain.getInstance().getResultSet(sql.toString());
      while (rs.next()) {
        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setUomID(rs.getInt(COL_UOM_ID));
        uom.setUomName(rs.getString(COL_UOM_NAME));
        return uom;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public ObservableList<UnitOfMeasure> getUOMs() {
    StringBuilder sql = new StringBuilder("select "+ COL_UOM_ID +",ucase("+ COL_UOM_NAME +") "+ COL_UOM_NAME +
            " from "+ TBL_UOMS +" order by "+ COL_UOM_NAME) ;
    try {
      ObservableList<UnitOfMeasure> uoms = FXCollections.observableArrayList();
      ResultSet rs = ConnectionMain.getInstance().getResultSet(sql.toString());
      while (rs.next()) {
        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setUomID(rs.getInt(COL_UOM_ID));
        uom.setUomName(rs.getString(COL_UOM_NAME));
        uoms.add(uom);
      }
      return uoms;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

}
