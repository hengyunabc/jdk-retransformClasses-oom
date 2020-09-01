# jdk bug

* https://bugs.java.com/bugdatabase/view_bug.do?bug_id=8164921

工程导入IDE，设置jvm参数为： `-XX:MetaspaceSize=20m -XX:MaxMetaspaceSize=20m -verbose:class` ， 执行main函数。

过一段时间，可以看到打印错误日志：

```
java.lang.OutOfMemoryError
	at sun.instrument.InstrumentationImpl.retransformClasses0(Native Method)
	at sun.instrument.InstrumentationImpl.retransformClasses(InstrumentationImpl.java:144)
	at com.example.jdkretransformClassesoom.JdkRetransformClassesOomApplication.main(JdkRetransformClassesOomApplication.java:46)
```

可以同时开一个 visualvm来观察，可以发现meta space突然一下子用完。



---


# demo 包下的说明

demo包演示了一个 arthas trace之后，应用的meta space快速上升的例子。

导入IDE，启动 `demo.TTT`，然后 arthas 3.3.9版本 attach。

```
as.sh --select TTT --use-version 3.3.9
```

然后多次 trace 

```
[arthas@72234]$ trace demo.FFF ttt
Press Q or Ctrl+C to abort.
Affect(class count: 1 , method count: 1) cost in 333 ms, listenerId: 1
[arthas@72234]$ trace demo.FFF ttt
Press Q or Ctrl+C to abort.
Affect(class count: 1 , method count: 1) cost in 330 ms, listenerId: 2
[arthas@72234]$ trace demo.FFF ttt
Press Q or Ctrl+C to abort.
Affect(class count: 1 , method count: 1) cost in 154 ms, listenerId: 3
[arthas@72234]$ trace demo.FFF ttt
Press Q or Ctrl+C to abort.
Affect(class count: 1 , method count: 1) cost in 155 ms, listenerId: 4
[arthas@72234]$ trace demo.FFF ttt
Press Q or Ctrl+C to abort.
Affect(class count: 1 , method count: 1) cost in 167 ms, listenerId: 5
[arthas@72234]$ trace demo.FFF ttt
Press Q or Ctrl+C to abort.
Affect(class count: 1 , method count: 1) cost in 134 ms, listenerId: 6
[arthas@72234]$ trace demo.FFF ttt
Press Q or Ctrl+C to abort.
Affect(class count: 1 , method count: 1) cost in 154 ms, listenerId: 7
[arthas@72234]$ trace demo.FFF ttt
Press Q or Ctrl+C to abort.
Affect(class count: 1 , method count: 1) cost in 139 ms, listenerId: 8
[arthas@72234]$ trace demo.FFF ttt
Press Q or Ctrl+C to abort.
Affect(class count: 1 , method count: 1) cost in 151 ms, listenerId: 9
```

在这过程中，可以用 visualvm 观察到meta space快速上涨。


但这个可以排除和 arthas本身的代码的关联。比如把 arthas生成的字节码dump出来，`options dump true` 。

然后不启动arthas，而是用上面的 retransformClasses ，不断加载arthas dump出来的字节码，则很容易复现。所以排掉arthas其它代码可能原因。