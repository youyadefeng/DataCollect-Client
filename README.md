# DataCollect-Client

毕业设计《大数据获取系统的设计与实现》客户端

Client项目：https://github.com/youyadefeng/DataCollect-Client

Server项目：https://github.com/youyadefeng/DataCollect-server

# 配置

客户端的主要工作是从服务端获取数据并将数据呈现给用户，客户端使用OkHttp向服务端发送请求，因此需要明确服务端接口的地址。

修改`NetWorkTask.java`文件中的baseUrl属性，令其与服务端接口地址匹配。



