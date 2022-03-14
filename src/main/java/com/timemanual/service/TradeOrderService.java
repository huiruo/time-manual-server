package com.timemanual.service;

import com.timemanual.entity.TradeOrder;
import com.timemanual.vo.PaginationVo;

public interface TradeOrderService {
    PaginationVo queryTradeOrder(Integer pageNum, Integer pageSize);
    Boolean add(TradeOrder tradeOrder);
}
