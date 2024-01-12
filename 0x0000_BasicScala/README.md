作为Chisel的宿主语言，不学习一些Scala的基础语法，总觉的无法深入理解Chisel。

虽然其实只要阅读Chisel的手册，可以直接上手Chisel进行开发。

参考书：《Hand-on Scala Programming》基于 Scalc 2.13.2 介绍。
- [Home](https://www.handsonscala.com/)

创建工程：

    +- Hello                 工程根目录
       +- src                源文件目录
          |- Hello.scala     源文件
       +- test               测试文件目录
          |- HelloTest.scala 测试文件
       |- build.sc           工程配置文件                                                                             Latest Version
       |- mill               工程管理工具（从GitHub下载：curl -L https://github.com/com-lihaoyi/mill/releases/download/0.11.6/0.11.6 > mill && chmod a+x mill）