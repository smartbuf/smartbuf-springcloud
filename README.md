# smartbuf-springcloud  [![Travis CI](https://travis-ci.org/smartbuf/smartbuf-springcloud.svg?branch=master)](https://travis-ci.org/smartbuf/smartbuf-springcloud)

[中文文档](https://sulin.me/2019/JGDXHH.html)

`smartbuf-springcloud` is a `spring-cloud` serialization plugin based on `smartbuf`.

# [SmartBuf](https://github.com/smartbuf/smartbuf-java) Introduce

`smartbuf` is a novel, efficient, intelligent and easy-to-use cross-language serialization framework. 
It has the same high performance as `protobuf`, and has the same versatility, scalability and flexibility as `json`. 

In `Java` ecosystem, it supports multiple `RPC` through the following plugins:

 + [`smartbuf-dubbo`](https://github.com/smartbuf/smartbuf-dubbo): Provides a serialization plugin in `stream` mode for `dubbo`.
 + [`smartbuf-springcloud`](https://github.com/smartbuf/smartbuf-springcloud): Provides a serialization plugin in `packet` mode for `spring-cloud`.

The following is a detailed introduction of the `smartbuf-springcloud`.

# `smartbuf-springcloud` Introduce

This plugins wraps `packet` mode of [`smartbuf`](https://github.com/smartbuf/smartbuf-java), 
and exposes an `HTTP` message converter named `application/x-smartbuf` to `spring` container via the custom `SmartbufMessageConverter`.

This new `application/x-smartbuf` could provide better performance than `protobuf` during data trasfer of complex object.

# `application/x-smartbuf` Codec

This plugins declares an `Auto-Configuration` named `SmartbufAutoConfiguration` in `META-INFO/spring.factories`.

`spring-boot` will scan and register it during initializing, it's fully automatic, no additional configuration is required.
For more infomation, please refer to [SpringBoot Doc](https://docs.spring.io/autorepo/docs/spring-boot/2.0.0.M3/reference/html/boot-features-developing-auto-configuration.html)。

`SmartbufAutoConfiguration` will add a new `SmartbufMessageConverter` instance into `spring` container, 
it's a custom `HttpMessageConverter`, and its `MediaType` is `application/x-smartbuf`.

`spring-mvc` will include this `SmartbufMessageConverter` into `messageConverters`, and uses it globally.
If the header of the subsequent `http` request includes `accept: application/x-smartbuf` 
or `content-type: application/x-smartbuf`, `spring-mvc` will use `SmartbufMessageConverter` to decode the request's input,
and encode the request's output.

The whole process is similar to the implementation of `application/json`, expect that the `application/x-smartbuf` is 
based on another serialization: [SmartBuf](https://github.com/smartbuf/smartbuf-java)

***SUMMARY: No additional configuration is required, this plugin will register a message codec 
named `application/x-smartbuf` into `spring-mvc` automatically, it has no effect on normal `http` request, 
and will be activated only if `accept` or `context-type` in the header matches `application/x-smartbuf`.***

# Demonstration

This chapter introduces how to add and use `smartbuf-springcloud` into your project with a simple example.

## Add Maven Dependency

You could add `smartbuf-springcloud` dependency by the following `maven` configuration:

```xml
<dependency>
  <groupId>com.github.smartbuf</groupId>
  <artifactId>smartbuf-springcloud</artifactId>
  <version>1.0.0</version>
</dependency>
```

## Use `application/json`

The `spring-cloud` layer uses `http` to communicate with the server's `spring-mvc`, the server's `controller` might like this:

```java
@RestController
public class DemoController {
    @PostMapping("/hello")
    public String sayHello(String name) { return "hello " + name; }
}
```

The client could use this `FeignClient` to invoke server:

```java
@FeignClient(name = "demo")
public interface DemoClient {
    @PostMapping(value = "/hello", consumes = "application/json", produces = "application/json")
    String hello(@RequestParam("name") String name);
}
```

When client uses `DemoClient` to invoke server's `DemoController`, `feign` will send a request similar to this to the server
based on `consumes` and `produces` declared in the `interface`:

>=== MimeHeaders ===
>accept = **application/json**
>content-type = **application/json**
>user-agent = Java/1.8.0_191
>connection = keep-alive

The server's `spring-mvc` will determine to use `application/json` to perform `input` decoding and `output` decoding 
based on `accept` and `content-type` in the headers of request.   

## Use `application/x-smartbuf`

As mentioned earily, `smartbuf-springcloud` will automatically register a codec named `application/x-smartbuf` into `spring-mvc`,
so after add the `maven` dependency, no additional configuration is required.

To use `application/x-smartbuf`, you just need to change `DemoClient` like this: 

```java
@FeignClient(name = "demo")
public interface DemoClient {
    @PostMapping(value = "/hello", consumes = "application/x-smartbuf", produces = "application/x-smartbuf")
    String hello(@RequestParam("name") String name);
}
```

Please pay attention to the changes in `consumes` and `produces`.

After this, `feign` will use `application/x-smartbuf` to communicate with the server's `spring-mvc`, 
the headers will like this:

>=== MimeHeaders ===
>accept = **application/x-smartbuf**
>content-type = **application/x-smartbuf**
>user-agent = Java/1.8.0_191
>connection = keep-alive

Even better, you can use `application/json` and `application/x-smartbuf` at the same time, like this:

```java
@FeignClient(name = "demo")
public interface DemoClient {
    @PostMapping(value = "/hello", consumes = "application/json", produces = "application/json")
    String helloJSON(@RequestParam("name") String name);

    @PostMapping(value = "/hello", consumes = "application/x-smartbuf", produces = "application/x-smartbuf")
    String helloSmartbuf(@RequestParam("name") String name);
}
```

The client could use `application/json` to communicate with the server by invoke `helloJSON`,
and use `application/x-smartbuf` by invoke `helloSmartbuf`.

The server's `spring-mvc` will switch the correct converter automatically by the specified `accept` and `content-type`. 

You can `checkout` this project and find the whole example in `demo` submodule. 

# Performance Comparison

The advantage of `smartbuf` is the high compression ratio brought by its partition serialization, 
especially when it comes to complex objects and arrays, its space utilization is far superior to other serialization schemes.

For RPC, the serialization time is often nanoseconds, 
while the logical processing and network transmission are often in the order of milliseconds.
Therefore, the following test will use a single thread to performing test.

Below we test the difference between `json` and `smartbuf` through three different types of `api`.

## `hello` Comparison

`hello` is the `helloJson` and `helloSmartbuf` mentioned above. 
The input and output parameters are `String`, its internal logic is very simple, `400,000` times requests will cost:

 + `JSON`: **169s**
 + `SmartBuf`: **170s**

Network input(`bytes_input`) and output(`bytes_output`) are like this:

![hello-bytes](https://github.com/smartbuf/smartbuf-springcloud/raw/master/doc/imgs/springcloud-hello.png)

Since `smartbuf` encoding requires an few extra bytes to describe the complete data information, 
its space utilization is not as good as `json` when dealing with simple data like `String`.

## `getUser` Comparison

`getUser`'s implementation is like this:

```java
@RestController
public class DemoController {
    private UserModel user = xxx; // initialized at somewhere else

    @PostMapping("/getUser")
    public UserModel getUser(Integer userId) { return user; }
}
```

The `user` is a randomly assigned object for testing. 
The specific model can be found in the `UserModel` class in the `demo` source.

The client's `FeignClient` is defined as follows:

```java
@FeignClient(name = "demo")
public interface DemoClient {
    @PostMapping(value = "/getUser", consumes = "application/json", produces = "application/json")
    UserModel getUserJSON(@RequestParam("userId") Integer userId);

    @PostMapping(value = "/getUser", consumes = "application/x-smartbuf", produces = "application/x-smartbuf")
    UserModel getUserSmartbuf(@RequestParam("userId") Integer userId);
}
```

`300,000` times invocation will cost:

 + `JSON`: **162s**
 + `SmartBuf`: **149s**

Network input(`bytes_input`) and output(`bytes_output`) are like this:

![getUser-bytes](https://github.com/smartbuf/smartbuf-springcloud/raw/master/doc/imgs/springcloud-getUser.png)

We can see that the parameter `userId` is a single data type, 
so the network traffic used by `json` and `smartbuf` is almost the same.
 
The return result `UserModel` is a more complex object, 
and the `json` network resource consumption is nearly **3 times** of `smartbuf`.

Because the test environment is `localhost`, 
network transmission time does not have much impact on the total time consumption of the interface.

## `queryPost` Comparison

`queryPost`'s implementation is like this:

```java
@RestController
public class DemoController {
    private List<PostModel> posts = xxx; // initialized at somewhere else

    @PostMapping("/queryPost")
    public List<PostModel> queryPost(String keyword) { return posts; }
}
```

The return value `posts` is a pre-allocated `PostModel` array for testing. 
Its length is fixed `100`. its model and initialization can be found in the `demo` source.

The client's `FeignClient` is defined as follows:

```java
@FeignClient(name = "demo")
public interface DemoClient {
    @PostMapping(value = "/queryPost", consumes = "application/json", produces = "application/json")
    List<PostModel> queryPostJSON(@RequestParam("keyword") String keyword);

    @PostMapping(value = "/queryPost", consumes = "application/x-smartbuf", produces = "application/x-smartbuf")
    List<PostModel> queryPostSmartbuf(@RequestParam("keyword") String keyword);
}
```

`100,000` times invocation will cost:

 + `JSON`: **195s**
 + `SmartBuf`: **155s**

Network input(`bytes_input`) and output(`bytes_output`) are like this:

![queryPost-bytes](https://github.com/smartbuf/smartbuf-springcloud/raw/master/doc/imgs/springcloud-queryPost.png)

We can see that the parameter `keyword` is a single `String`, 
so the network traffic used by `json` and `smartbuf` is almost the same.

The return result `List<PostModel>` is a large array of complex objects, 
and the `json` network resource consumption is nearly **10 times** of `smartbuf`.

Because the test environment is `localhost`, 
network transmission time does not have much impact on the total time consumption of the interface.

# Summary

When the `RPC` interface's input and output are very simple, `application/x-smartbuf` does not have any performance advantages.

When the `RPC` interface's input and output are large data such as complex objects, arrays, collections.
Using `application/x-smartbuf` could greatly reduce the bytecodes size of the network transmission. 
For example, in the `queryPost` above, `application/x-smartbuf`'s network transmission is only **1/10** of `application/json`.

What is commendable is that the `application/x-smartbuf` and `application/json` can coexist and seamleassly switch.

For example, you can use `application/json` directly for some simple `api`, 
and use the efficient `application/x-smartbuf` for complex `api` with large object to achieve better performance.

For example, you can use `application/json` during development and test,
and switch to `application/x-smartbuf` when going online.

# License

MIT
