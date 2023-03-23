# WMS仓储系统的性能测试
——————————————————————————
## 运行方式：gradle gatlingRun
——————————————————————————
### 2023/3/23 
    完成商品服务的性能测试
    测试数据：10条
    测试流程：add goods->query single goods->update goods->query single goods->query all goods->delete goods
    测试方式：全程10并发(atOnceUsers(10))
    测试结果：
        并发总用户数：10
        总请求数：60
        流程：全部通过
        总体请求平均响应时间：19ms
