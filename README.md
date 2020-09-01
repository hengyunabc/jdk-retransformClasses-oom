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