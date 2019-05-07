package com.JavaPOS.Interfaces;

import com.JavaPOS.DataModels.ItemAssembly;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface IItemAssemblies {
  ObservableList<ItemAssembly> getItemAssemblies(int itemID);
  void handleItemAssemblies(int itemID,ObservableList<ItemAssembly> itemAssemblies) throws SQLException,ClassNotFoundException;
}
