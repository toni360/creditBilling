creditBilling
=============

信用支付（creditBilling）：蜂鸟娱乐公司的探索性项目，目的是优化支付体验，从弱联网计费向强联网计费突破的重要利器。

Github源代码目录结构

/ ：项目根目录

/readme.md ：readme文件

/docs/ ：存放设计文档目录，包括需求、架构设计、接口设计、数据库设计等文档，要求格式为markdown文档，不建议采用其他格式文档。

/src/app/ ：存放客户端源代码目录

>*/src/app/ios/* ：存放IOS app源代码目录

>*/src/app/Android/* ：存放客户端安卓源代码目录

>*/src/sdk/* :存放客户端SDK源代码目录

/src/server/ ：存放服务端源代码目录

>*/src/server/portal/* ：存放服务端门户源代码目录

>*/src/server/gateway/* ：存放服务端接入网关源代码目录

>*/src/server/service/* ：存放服务端业务层源代码目录

Github使用注意事项：

1. clone指定的分支，不建议clone master分支，如果要clone可以，但不要基于master分支进行开发。
2. 只能向自己负责的分支提交和合并代码，禁止向master分支提交代码，必须使用“pull request”请求合并到master分支。
3. 在分支上合并代码发现冲突时，需要充分沟通后进行合并，避免擅自或者不清楚目的的合并。
4. 当到版本或者补丁发布时，master负责人会要求合并分支，形成完整的代码版本。
