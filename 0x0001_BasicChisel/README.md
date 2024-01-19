该文件夹为Chisel使用的基础的练习

---

环境配置（3.6.0）：

工程管理工具：[mill](https://mill-build.com/mill/Intro_to_Mill.html)

依赖，通过工程配置文件进行添加。
- [Chisel3](https://mvnrepository.com/artifact/edu.berkeley.cs/chisel3)
- [ChiselTest](https://mvnrepository.com/artifact/edu.berkeley.cs/chiseltest)
- [Chisel3 Plugin](https://mvnrepository.com/artifact/edu.berkeley.cs/chisel3-plugin)

添加依赖的格式与sbt类似，上述包的主页中（点击进入所需版本之后）有sbt格式的写法。 \
mill的依赖格式，将「%」替换为「:」即可。即：`ivy"fmt string"`

3.6.0以后的Chisel使用MFC作为后端生成Verilog代码。因此，
需要另外安装[circt](https://circt.llvm.org/docs/GettingStarted/)工具。

结合Chisel的版本，下载对应版本的crict编译并安装，添加安装路径到环境变量。

如果Chisel的版本与crict不匹配（或者crict不存在）时， \
在运行Scala程序生成Verilog代码的话，会提示对应版本。

在编译circt的过程中如果，出现不明原因的编译失败，多半是由于内存不足或者， \
多线程编译造成的。因此，增加交换空间或者使用单线程编译即可通过（`ninja -j 1`）。

circt编译通过后，将`build/bin`与`llvm/bin`下的文件合并到一个`bin`目录， \
并添加到环境变量即可运行Scala程序生成Verilog代码。

---