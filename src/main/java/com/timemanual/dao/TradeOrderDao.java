package com.timemanual.dao;

import com.timemanual.entity.TradeOrder;
import java.util.List;
import java.util.Map;

public interface TradeOrderDao {
    List<TradeOrder> selectTradeOrder(Map<String,Integer> map);
    long countTradeOrder();
}
