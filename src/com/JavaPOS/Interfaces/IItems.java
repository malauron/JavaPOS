package com.JavaPOS.Interfaces;

import com.JavaPOS.DataModels.Item;
import com.JavaPOS.DataModels.ItemAssembly;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.SQLException;

public interface IItems {

  Item getItem(String param);
  ObservableList<Item> getItems();
  ObservableList<Item> getItems(String search);
  ObservableList<Item> getItems(String criteria, int itemSubGroupID);
  ObservableList<Item> getDummyItems(int itemSubGroupID);
  void handleItem(Item item, ObservableList<ItemAssembly> itemAssemblies) throws IOException, SQLException, ClassNotFoundException;

}
