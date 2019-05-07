package com.JavaPOS.Interfaces;

import com.JavaPOS.DataModels.ItemSubGroup;
import javafx.collections.ObservableList;

public interface IItemSubGroups {
  ItemSubGroup getItemSubGroup(int id);

  ObservableList<ItemSubGroup> getItemSubGroups();
  ObservableList<ItemSubGroup> getDummyItemSubGroups();
}
