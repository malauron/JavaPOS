package com.JavaPOS.DataAccessObjects;

import com.JavaPOS.DataModels.Shiftdetail;
import com.JavaPOS.Interfaces.IShiftDetails;
import com.JavaPOS.Utilities.ConnectionMain;
import com.JavaPOS.Utilities.CurrentUser;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static com.JavaPOS.DataAccessObjects.DataAccessConstants.*;

public class Shiftdetails implements IShiftDetails {

  // Get shiftdetail by ID
  @Override
  public Shiftdetail getShiftDetail(Integer user_id)  {
    PreparedStatement ps = null;
    ResultSet rs = null;

    StringBuilder sql = new StringBuilder("select x1." + COL_SHIFT_ID + ",x1." + COL_STARTTRANS + ", x1." + COL_ENDTRANS + " " +
            "from " + TBL_SHIFTDETAILS + " x1 where x1." + COL_USER_ID + " = ? and x1." + COL_ENDTRANS + " is null" );

    try {
      ps = ConnectionMain.getInstance().cn().prepareStatement(sql.toString());
      ps.setInt(1, user_id);
      rs = ps.executeQuery();
      if (rs.next()) {
        return getShiftdetail(rs);
      }
      rs.close();
      ps.close();
    } catch (ClassNotFoundException | SQLException  e) {
      e.printStackTrace();
    } finally {
      try {
        if (rs != null && !rs.isClosed()) {
          rs.close();
          System.out.println("rs close");
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
      try {
        if (ps != null && !ps.isClosed()) {
          ps.close();
          System.out.println("ps close");
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  // Add / Update shiftdetail
  @Override
  public Shiftdetail handleShiftDetail(Shiftdetail shiftdetail) {

    Connection cn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    StringBuilder sql = null;

    try {

      cn = ConnectionMain.getInstance().cn();
      cn.setAutoCommit(false);
      sql = new StringBuilder("insert into " + TBL_SHIFTDETAILS + " (" + COL_USER_ID + "," + COL_STARTTRANS + ") values (?,now())");
      ps = cn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
      ps.setInt(1, CurrentUser.getInstance().getUserID());
      ps.executeUpdate();
      rs = ps.getGeneratedKeys();
      rs.next();
      System.out.println(rs.getInt(1));
      shiftdetail = getShiftDetail(rs.getInt(1));
      System.out.println("after error");
      cn.commit();
      System.out.println("after commit");
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("error before rollback");
      try {
        cn.rollback();
        System.out.println("rollback");
      } catch (SQLException e1) {
        e1.printStackTrace();
      }
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (Exception e) {
      System.out.println("generic exception");
    } finally {
      try {
        if (rs != null && !rs.isClosed()) {
          rs.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
      try {
        if (ps != null && !ps.isClosed()) {
          ps.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    return shiftdetail;
  }

  // Get shiftdetail
  private Shiftdetail getShiftdetail(ResultSet rs) throws SQLException {

    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String sDate = rs.getString(COL_STARTTRANS);
    Shiftdetail shiftdetail = new Shiftdetail();

    shiftdetail.setShiftdetail_id(rs.getInt(COL_SHIFT_ID));

    try {
      shiftdetail.setStarttrans(dateFormatter.parse(sDate));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    shiftdetail.setEndtrans(rs.getDate(COL_ENDTRANS));

    System.out.println(rs.getString(COL_STARTTRANS));
    return shiftdetail;

  }
}
