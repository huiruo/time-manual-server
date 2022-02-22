

# 
```
        /*
        *MybatisPlus的update默认机制是更新字段时判断是否为null,做值为null,则不更新该字段
        当我们需要将部分字段更新为null时,可利用UpdateWrapper解决该问题
        UpdateWrapper用法 :
        WarehouseItem warehouseItem = warehouseItemService.getById(2117733125);
        UpdateWrapper<WarehouseItem> updateWrapper = new UpdateWrapper<>();
        //可将指定字段更新为null
        updateWrapper.set("ownerId", null); 
        updateWrapper.set("product_id",123456);
        warehouseItemService.update(warehouseItem, updateWrapper);
        * */
```

## UpdateWrapper的使用
https://blog.csdn.net/weixin_44684812/article/details/121465271



#### 修改指定id的名字（可在后增加条件）
```
UpdateWrapper updateWrapper = new UpdateWrapper();
updateWrapper.eq("id", User .getId());
updateWrapper.set("username", "新名称");
userMapper.user(null, updateWrapper);
```

#### 常用的连接条件
```
eq	等于 
ne  不等于	
gt	大于	 
ge	大于等于	 
lt	小于	 
le	小于等于	 
betwen	在...之间	 
noBetween	不在....之间	 
like	       模糊匹配	 
noLike	  	  
likeLeft	  	 
likeRight	 	 
isNull	 	 是空
isNotNull	 非空
in	 	    sql中的in 
notLn	 	
```