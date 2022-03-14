package com.timemanual.controller;

import com.timemanual.entity.TradeOrder;
import com.timemanual.service.TradeOrderService;
import com.timemanual.vo.PageParamVo;
import com.timemanual.vo.PaginationVo;
import com.timemanual.vo.ReqVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
public class TradeOrderController {
   @Autowired
   TradeOrderService tradeOrderService;

   @RequestMapping("/trade/order/query")
   public ReqVo<PaginationVo> queryTradeOrder(@RequestBody Map<String, Object> map){
      String symbol = (String) map.get("symbol");
      int pageSize = (Integer) map.get("pageSize");
      int pageNum = (Integer) map.get("pageNum");
      log.debug("queryParam:{}",pageNum);
      log.debug("queryParam:{}",pageSize);
      log.debug("queryParam:{}",symbol);
      PaginationVo paginationVo = tradeOrderService.queryTradeOrder(pageNum,pageSize);
      return new ReqVo<>(paginationVo);
   }
}
