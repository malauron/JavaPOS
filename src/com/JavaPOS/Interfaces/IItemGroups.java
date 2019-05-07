package com.JavaPOS.Interfaces;

import com.JavaPOS.DataModels.ItemGroup;
import javafx.collections.ObservableList;

public interface IItemGroups {
  ItemGroup getItemGroup(int id);

  ObservableList<ItemGroup> getItemGroups();
}
