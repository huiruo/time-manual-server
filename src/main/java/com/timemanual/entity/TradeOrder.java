package com.timemanual.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeOrder {
    private Integer id;
    private String symbol;
    private Integer orderId;
    private Integer orderListId;
    private String price;
    private String qty;
    private String quoteQty;
    private String commission;
    private String commissionAsset;
    private Integer time;
    private Integer isBuyer;
    private Integer isMaker;
    private Integer isBestMatch;
    private Date create_time;
    private Date update_time;
}
