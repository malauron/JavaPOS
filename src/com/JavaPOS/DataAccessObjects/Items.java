package com.JavaPOS.DataAccessObjects;

import com.JavaPOS.DataModels.Item;
import com.JavaPOS.DataModels.Item.ItemClassification;
import com.JavaPOS.DataModels.ItemAssembly;
import com.JavaPOS.Interfaces.IItemAssemblies;
import com.JavaPOS.Interfaces.IItemSubGroups;
import com.JavaPOS.Interfaces.IItems;
import com.JavaPOS.Interfaces.IUOMs;
import com.JavaPOS.Utilities.ConnectionMain;
import com.JavaPOS.Utilities.CurrentUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

import static com.JavaPOS.DataAccessObjects.DataAccessConstants.*;

public class Items implements IItems {

    private IItemAssemblies iItemAssemblies = new ItemAssemblies();

    @Override
    public Item getItem(String param) {
      PreparedStatement ps = null;
      ResultSet rs = null;

      StringBuilder sql = new StringBuilder("select x1." + COL_ITEM_ID + ",x1." + COL_ITEM_CODE +
              ",ucase(x1." + COL_ITEM_NAME + ") " + COL_ITEM_NAME + ",x1." + COL_ALTDESC +
              ",x1." + COL_ITEMSUBGROUP_ID + ",x1." + COL_UOM_ID + ",x1." + COL_ITEM_CLASSIFICATION +
              ",x1." + COL_REGULARPRICE + ",x1." + COL_ISACTIVE + ",x2." + COL_ITEM_FILEBLOB +
              " from " + TBL_ITEMS + " x1 " +
              " left outer join " + TBL_ITEMS_PHOTOS + " x2 on x1." + COL_ITEM_ID + " = x2." + COL_ITEM_ID +
              " where " + COL_ITEM_CODE + " = ?") ;

      try {
        ps = ConnectionMain.getInstance().cn().prepareStatement(sql.toString());
        ps.setString(1,param);
        rs = ps.executeQuery();
        if (rs.next()) {
          return getItem(rs);
        }
        rs.close();
        ps.close();
      } catch (ClassNotFoundException | SQLException | IOException e) {
        e.printStackTrace();
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
      return null;
    }

  @Override
  public ObservableList<Item> getItems() {
    return getItems("");
  }

  @Override
  public ObservableList<Item> getItems(String criteria) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    StringBuilder sql = new StringBuilder("select x1." + COL_ITEM_ID + ",x1." + COL_ITEM_CODE +
            ",ucase(x1." + COL_ITEM_NAME + ") " + COL_ITEM_NAME + ",x1." + COL_ALTDESC +
            ",x1." + COL_ITEMSUBGROUP_ID + ",x1." + COL_UOM_ID + ",x1." + COL_ITEM_CLASSIFICATION +
            ",x1." + COL_REGULARPRICE + ",x1." + COL_ISACTIVE + ",x2." + COL_ITEM_FILEBLOB +
            " from " + TBL_ITEMS + " x1 " +
            " left outer join " + TBL_ITEMS_PHOTOS + " x2 on x1." + COL_ITEM_ID + " = x2." + COL_ITEM_ID +
            " where " + COL_ITEM_NAME  + " like ? " +
            " order by " + COL_ITEM_NAME ) ;
    try {

      ps = ConnectionMain.getInstance().cn().prepareStatement(sql.toString());
      ps.setString(1, "%" + criteria + "%");
      rs = ps.executeQuery();
      ObservableList<Item> items = getItems(rs);
      rs.close();
      ps.close();
      return items;
    } catch (ClassNotFoundException | SQLException | IOException | NullPointerException e) {
      e.printStackTrace();
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
    return null;
  }

  @Override
  public ObservableList<Item> getItems(String criteria, int itemSubGroupID) {

    PreparedStatement ps = null;
    ResultSet rs = null;
    String ig;

    if(itemSubGroupID == 0) {
      ig = " > ";
    } else {
      ig = " = ";
    }

    StringBuilder sql = new StringBuilder("select x1." + COL_ITEM_ID + ",x1." + COL_ITEM_CODE +
            ",ucase(x1." + COL_ITEM_NAME + ") " + COL_ITEM_NAME + ",x1." + COL_ALTDESC +
            ",x1." + COL_ITEMSUBGROUP_ID + ",x1." + COL_UOM_ID + ",x1." + COL_ITEM_CLASSIFICATION +
            ",x1." + COL_REGULARPRICE  + ",x1." + COL_ISACTIVE + ",x2." + COL_ITEM_FILEBLOB +
            " from " + TBL_ITEMS + " x1 " +
            " left outer join " + TBL_ITEMS_PHOTOS + " x2 on x1." + COL_ITEM_ID + " = x2." + COL_ITEM_ID +
            " where x1." + COL_ITEM_NAME  + " like ? " +
            " and x1." + COL_ITEM_CLASSIFICATION  + " in ('" + ItemClassification.STOCK +
            "','" + ItemClassification.SERVICE +
            "') and x1." + COL_ISACTIVE + " = 'Y' and x1." + COL_ITEMSUBGROUP_ID + ig + itemSubGroupID +
            " order by " + COL_ITEM_NAME ) ;

    try {
      ps = ConnectionMain.getInstance().cn().prepareStatement(sql.toString());
      ps.setString(1, "%" + criteria + "%");
      rs = ps.executeQuery();
      ObservableList<Item> items = getItems(rs);
      rs.close();
      ps.close();
      return items;
    } catch (ClassNotFoundException | SQLException | IOException e) {
      e.printStackTrace();
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
    return null;
  }

  private ObservableList<Item> getItems(ResultSet rs) throws SQLException,IOException {
    ObservableList<Item> items = FXCollections.observableArrayList();
    while (rs.next()) {
      items.add(getItem(rs));
    }
    return items;
  }

  private Item getItem(ResultSet rs) throws SQLException,IOException {
    Item item = new Item();
    IItemSubGroups itemSubGroups = new ItemSubGroups();
    IUOMs uoms = new UOMs();

    Blob blobPhoto;
    InputStream isPhoto;

    if(rs.getBlob(COL_ITEM_FILEBLOB) != null) {
      blobPhoto = rs.getBlob(COL_ITEM_FILEBLOB);
      isPhoto = blobPhoto.getBinaryStream();
      item.setItemPhoto(new Image(isPhoto));
      isPhoto.close();
    }

    item.setItemID(rs.getInt(COL_ITEM_ID));
    item.setItemSubGroup(itemSubGroups.getItemSubGroup(rs.getInt(COL_ITEMSUBGROUP_ID)));
    item.setItemCode(rs.getString(COL_ITEM_CODE));
    item.setItemName(rs.getString(COL_ITEM_NAME));
    item.setAltDesc(rs.getString(COL_ALTDESC));
    item.setUom(uoms.getUOM(rs.getInt(COL_UOM_ID)));
    item.setItemClassification(Item.ItemClassification.valueOf(rs.getString(COL_ITEM_CLASSIFICATION)));
    item.setRegularPrice(rs.getBigDecimal(COL_REGULARPRICE));
    item.setIsActive(isTrue(rs.getString(COL_ISACTIVE)));
    if(item.getItemClassification().equals(Item.ItemClassification.ASSEMBLY)) {
      item.setItemAssembly(iItemAssemblies.getItemAssemblies(item.getItemID()));
    }
    return item;
  }

  @Override
  public ObservableList<Item> getDummyItems(int itemSubGroupID) {

    String ig;
    if(itemSubGroupID == 0) {
      ig = " > ";
    } else {
      ig = " = ";
    }

    StringBuilder sql = new StringBuilder("select x1." + COL_ITEM_ID + ",x1." + COL_ITEM_CODE +
            ",ucase(x1." + COL_ITEM_NAME + ") " + COL_ITEM_NAME + ",x1." + COL_ALTDESC +
            ",x1." + COL_ITEMSUBGROUP_ID + ",x1." + COL_UOM_ID + ",x1." + COL_ITEM_CLASSIFICATION +
            ",x1." + COL_REGULARPRICE +
            " from " + TBL_ITEMS + " x1 " +
            " where " + COL_ITEM_CLASSIFICATION  + " in ('" + ItemClassification.STOCK +
            "','" + ItemClassification.SERVICE +
            "') and " + COL_ISACTIVE + " = 'Y' and " + COL_ITEMSUBGROUP_ID + ig + itemSubGroupID +
            " order by " + COL_ITEM_NAME ) ;

    try {

      ObservableList<Item> items = FXCollections.observableArrayList();
      Item item = new Item();
      item.setItemID(0);
      item.setItemName("* (ANY ITEM)");
      items.add(item);
      PreparedStatement ps = ConnectionMain.getInstance().cn().prepareStatement(sql.toString());
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        item = new Item();
        item.setItemID(rs.getInt(COL_ITEM_ID));
        item.setItemCode(rs.getString(COL_ITEM_CODE));
        item.setItemName(rs.getString(COL_ITEM_NAME));
        item.setAltDesc(rs.getString(COL_ALTDESC));
        item.setRegularPrice(rs.getBigDecimal(COL_REGULARPRICE));
        items.add(item);
      }
      rs.close();
      ps.close();
      return items;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public void handleItem(Item item,ObservableList<ItemAssembly> itemAssemblies) throws IOException, SQLException, ClassNotFoundException {

    StringBuilder sql = new StringBuilder();
    PreparedStatement ps = null;
    ResultSet lastID = null;

    try {
      ConnectionMain.getInstance().cn().setAutoCommit(false);
      if (item.getItemID().equals(0)) {
        sql.append("insert into " + TBL_ITEMS + "(" + COL_ITEM_CODE + ", " + COL_ITEM_NAME +
                "," + COL_ITEM_CLASSIFICATION + "," + COL_ITEMSUBGROUP_ID + "," + COL_UOM_ID +
                "," + COL_REGULARPRICE + "," + COL_ISACTIVE +
                "," + COL_ITEM_ADDEDBYUSER + "," + COL_ITEM_DATECREATED +
                ") values (?,?,?,?,?,?,?,?,now())");
        ps = ConnectionMain.getInstance().cn().prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
        ps.setString(1,item.getItemCode());
        ps.setString(2,item.getItemName());
        ps.setString(3,item.getItemClassification().name());
        ps.setInt(4,item.getItemSubGroup().getItemSubGroupID());
        ps.setInt(5,item.getUom().getUomID());
        ps.setBigDecimal(6,item.getRegularPrice());
        ps.setString(7,isTrue(item.isIsActive()));
        ps.setInt(8,CurrentUser.getInstance().getUserID());
        ps.executeUpdate();
        lastID = ps.getGeneratedKeys();
        lastID.next();
        item.setItemID(lastID.getInt(1));
        lastID.close();
      } else {

        sql.append("update " + TBL_ITEMS + " set " + COL_ITEM_CODE + "=?, " + COL_ITEM_NAME + "=?," +
                COL_ITEM_CLASSIFICATION + "=?," + COL_ITEMSUBGROUP_ID + "=?," + COL_UOM_ID + "=?," +
                COL_REGULARPRICE + "=?, " + COL_ISACTIVE + "=? where " + COL_ITEM_ID + "=? ");
        ps = ConnectionMain.getInstance().cn().prepareStatement(sql.toString());
        ps.setString(1,item.getItemCode());
        ps.setString(2,item.getItemName());
        ps.setString(3,item.getItemClassification().name());
        ps.setInt(4,item.getItemSubGroup().getItemSubGroupID());
        ps.setInt(5,item.getUom().getUomID());
        ps.setBigDecimal(6,item.getRegularPrice());
        ps.setString(7,isTrue(item.isIsActive()));
        ps.setInt(8,item.getItemID());
        ps.executeUpdate();
      }
      ps.close();

      if(item.getItemClassification() == ItemClassification.ASSEMBLY) {
        iItemAssemblies.handleItemAssemblies(item.getItemID(),itemAssemblies);
      }

      sql.setLength(0);
      sql.append("delete from " + TBL_ITEMS_PHOTOS + " where " + COL_ITEM_ID + " =? ");
      ps = ConnectionMain.getInstance().cn().prepareStatement(sql.toString());
      ps.setInt(1,item.getItemID());
      ps.executeUpdate();
      ps.close();

      if(item.getItemPhoto() != null) {
        sql.setLength(0);
        sql.append("insert into "+ TBL_ITEMS_PHOTOS +
                " ("+ COL_ITEM_ID +","+ COL_ITEM_FILESIZE +", "+ COL_ITEM_FILEBLOB +") values (?,?,?)");
        ps = ConnectionMain.getInstance().cn().prepareStatement(sql.toString());
        ps.setInt(1,item.getItemID());

        BufferedImage bImage = SwingFXUtils.fromFXImage(item.getItemPhoto(),null);
        ByteArrayOutputStream osPhoto = new ByteArrayOutputStream();
        ImageIO.write(bImage,"jpg",osPhoto);
        byte[] res = osPhoto.toByteArray();
        ps.setInt(2, osPhoto.size());
        InputStream inputStream = new ByteArrayInputStream(res);
        osPhoto.close();

        ps.setBinaryStream(3,inputStream);

        ps.executeUpdate();
        ps.close();
      }


      ConnectionMain.getInstance().cn().commit();
    } catch (SQLException | IOException se) {
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

  private boolean isTrue(String b) {
    return (b.equals("Y") || b.equals("y"));
  }

  private String isTrue(boolean b) {
    if (b) {
      return "Y";
    } else {
      return "N";
    }
  }

}
