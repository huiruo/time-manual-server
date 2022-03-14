package com.timemanual.service.impl;

import com.timemanual.dao.TradeOrderDao;
import com.timemanual.entity.TradeOrder;
import com.timemanual.service.TradeOrderService;
import com.timemanual.vo.PaginationVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class TradeOrderImpl implements TradeOrderService {
    @Autowired
    private TradeOrderDao tradeOrderDao;

    @Override
    public PaginationVo queryTradeOrder(Integer pageNum, Integer pageSize) {
        long count = tradeOrderDao.countTradeOrder();
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("pageNum", (pageNum - 1) * pageSize);
        map.put("pageSize", pageSize);
        List<TradeOrder> tradeOrderList = tradeOrderDao.selectTradeOrder(map);
        PaginationVo paginationVo = new PaginationVo<>();
        paginationVo.setPageNum(pageNum);
        paginationVo.setPageSize(pageSize);
        paginationVo.setTotal(count);
        paginationVo.setResult(tradeOrderList);

        return paginationVo;
    }

    @Override
    public Boolean add(TradeOrder tradeOrder) {
        return null;
    }
}
