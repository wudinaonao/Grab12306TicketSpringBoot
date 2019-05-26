# Grab12306Ticket　SpringBoot

这是12306抢票系列的SpringBoot后端。

###技术栈：
 * SpringBoot
 * MyBatis
 * Redis

###使用方法：
你需要在resource文件夹里创建两个文件，一个是SpringBoot的配置文件，一个是项目的配置文件
 * application.yml
 * configuration.yml

###项目配置：
 * application.yml 文件内容
 * ```yaml
   spring:
     mvc:
       throw-exception-if-no-handler-found: true
     resources:
       add-mappings: false
     datasource:
       type: com.alibaba.druid.pool.DruidDataSource
       driver-class-name: com.mysql.cj.jdbc.Driver
       url: 你的数据库地址
       username: 数据库用户名
       password: 数据库密码
       # 使用 durid 数据库连接池
       druid:
         initial-size: 5
         min-idle: 5
         max-active: 20
         max-wait: 60000
         time-between-eviction-runs-millis: 60000
         min-evictable-idle-time-millis: 300000
         validation-query: SELECT 1 FROM DUAL
         test-while-idle: true
         test-on-borrow: false
         test-on-return: false
         pool-prepared-statements: true
         max-pool-prepared-statement-per-connection-size: 20
         filter: stat, wall
         connection-properties: druid.stat.mergeSql\=true;druid.stat.slowSqlMillis\=5000
         web-stat-filter:
           enabled: true
           url-pattern: "/*"
           exclusions: "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*"
           stat-view-servlet:
             url-pattern: "/druid/*"
             allow: 0.0.0.0
             reset-enable: false
             login-username: 你的 durid 后台登陆用户名
             login-password: 你的 durid 后台登陆密码
     redis:
       host: Redis服务器地址
       port: Redis服务器端口
       password: Redis服务器密码,如果没有删除该项即可
     session:
       store-type: redis
       timeout: 30
       
   # 内嵌Tomcat设置，因为我们最后项目需要部署到外部Tomcat，所以这个键底下的值并不是非常重要
   server:
     port: 8848
   
   # mybatis 相关设置
   mybatis:
     config-location: classpath:mybatis/mybatis-config.xml
     mapper-locations: classpath:mybatis/mapper/*.xml
     check-config-location: true
     type-aliases-package: com.naonao.grab12306ticket.version.springboot.entity.database
   
   # 分页插件，暂时没有想到哪里会用。
   pagehelper:
     auto-dialect: true
     close-conn: false
     reasonable: true
   
   # Spring boot 集成 HttpClient 的设置，因为最后 HttpClient 并没有交由Spring容器管控，所以这部分设置暂时没用。
   http:
     maxTotal: 300
     defaultMaxPerRoute: 50
     connectionTimeOut: 30000
     connectionRequestTimeOut: 30000
     socketTimeOut: 30000
     staleConnectionCheckEnabled: true
   
   # Spring 日志设置
   logging:
     file: springbootlogging.txt
     level:
       root: info
   
   # url前缀，所有url都会加这个前缀，方便以后扩展。
   # 例如登陆接口url：/api/v1/user/login
   url:
     prefix: "/api/v1/"

   ``` 
 * configuration.yml 文件内容
 * ```yaml
    platform:
      config:
        twilio:
          accountSid:       从twilio获取
          authToken:        同上
          voiceUrl:         这是一个url地址，指向一个xml文件，xml内容详情见官方文档
          defaultVoiceUrl:  同上
          fromPhoneNumber:  从twilio获取
        nexmo:
          apiKey:           从nexmo获取
          apiSecret:        从nexmo获取
        yunzhixin:
          appCode:          从接口提供商获取
          templateId:       从接口提供商获取
          
    # interfaceName 大小写敏感，请填写大写
    notification:
      config:
        phone:
          # phone 通知接口名字，例如：TWILIO
          interfaceName: TWILIO
        sms:
          # sms 通知接口名字，例如：TWILIO
          interfaceName: NEXMO
          # Nexmo 可以自定义短信标题
          title: "12306Ticket"
          # 短信内容
          content: "Congratulations! grab the ticket, please log in to 12306 to pay."
          # 用于测试是否可以收到通知的短信内容
          defaultContent: "if you can look this message, then you can receiver notification, from 12306 grab ticket system."
        email:
          # email 发件人的相关设置
          # 例如
          senderEmail: "xxx@gmail.com"
          senderEmailHost: "smtp.gmail.com"
          senderEmailPort: "465"
          senderEmailUsername: "xxx"
          senderEmailPassword: "xxx"
          # 用于测试是否可以收到通知的邮件标题和内容
          defaultTitle: "12306 grab ticket notification"
          defaultContent: "12306 grab ticket notification test, if you can look this email, then you can receive notification."
    
    setting:
      # 启动抢票代码
      grabTicketCode: true
   ```
###通知方式：
 * Email：
    * 目前测试了Gmail邮箱没有问题，但是需要进行一些相关的设置，国内的邮箱我暂时没有测试过。当然也可以自己搭建邮件服务器用自己的邮箱。
 * SMS：
    * 实现了两个接口
        * Nemxo　　　  Nexmo接口可以自定义短信标题。
        * Twilio　　　 Twilio接口需要一个发送方号码。
    * 使用详情见各个接口官方文档。
 * Phone：
    * 实现了两个接口
        * Twilio    
        * Yunzhixin
    * Yunzhixin 是一个国内的短信接口，但是需要联系客服申请模板，因为我当时使用的是这个接口，所以也把这个接口封装了一下。
 * 注意：
    * Nexmo 和 Twilio 是一个国际服务商，所以接收号码的时候需要加国际区号，例如中国：86xxxxxxx
 
