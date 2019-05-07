package com.JavaPOS.Interfaces;

import com.JavaPOS.DataModels.UnitOfMeasure;
import javafx.collections.ObservableList;

public interface IUOMs {
  UnitOfMeasure getUOM(int id);

  ObservableList<UnitOfMeasure> getUOMs();
}
