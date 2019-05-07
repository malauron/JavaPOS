package com.JavaPOS.Interfaces;

import com.JavaPOS.DataModels.Shiftdetail;

public interface IShiftDetails {
  Shiftdetail getShiftDetail(Integer user_id);
  Shiftdetail handleShiftDetail(Shiftdetail shiftdetail);
}
