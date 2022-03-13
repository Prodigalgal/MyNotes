# 问题

## 1、com.microsoft.sqlserver.jdbc.SQLServerException: 列名 id 无效。

在调用SQLServer的存储过程并获取结果的同时，报出该错误。

```sql
 if @x_tag=4      /*查询警员信息*/
	   	   begin
		   if @id<>0                                        /*班级编号不为0,班级名称为空,则查询等于@x_classno的信息*/
		        select name,sex,telphone,address from people where @id=id 
 
             else                                          /*班级编号为0和班级名称为空则显示前10条记录*/
		        select top 10 name,sex,telphone,address from people order by name asc
	   end
```

```java
vRow.set(1, eventtrs.getString("eno").trim());
vRow.set(2, eventtrs.getString("text").trim());
vRow.set(3, eventtrs.getString("time").trim());
vRow.set(4, eventtrs.getString("pno"));
vRow.set(5, eventtrs.getString("situation"));
```

**原因**：结果集获取的字段与结果不匹配，在存储过程中添加对应字段

```sql
 if @x_tag=4      /*查询警员信息*/
	   	   begin
		   if @id<>0                                        /*班级编号不为0,班级名称为空,则查询等于@x_classno的信息*/
		        select id, name,sex,telphone,address from people where @id=id 
 
             else                                          /*班级编号为0和班级名称为空则显示前10条记录*/
		        select top 10  name,sex,telphone,address from people order by name asc
	   end
```

