package io.netty.example.future;

import io.netty.util.concurrent.*;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

//用于测试 Netty 基于 JDK 同步阻塞式 Future 额外封装的异步 Future 使用方式
public class NettyFutureDemo {
    public static void main(String[] args) throws InterruptedException {
        //Future 的任务执行以及逻辑回调都依赖于异步线程执行，因此要构造一个 EventExecutorGroup 作为异步线程池
        EventExecutorGroup group = new DefaultEventExecutorGroup(4);
        //这里返回的是的类型既是 Netty Promise 又是 Netty  Future，同时也是 JDK 的 Future
        //1.提交任务
        Future<Integer> f = group.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws InterruptedException {
                new CountDownLatch(1).await();
                System.out.println("hello world");
                return 100;
            }
        });
        //2.添加事件监听器
        f.addListener(new FutureListener<Object>() {
            @Override
            public void operationComplete(Future<Object> objectFuture) throws Exception {
                System.out.println("计算结果: " + objectFuture.get());
            }
        });
        //block on purpose

        Promise p = (Promise) f;

        try{
            p.setSuccess(100);
        }catch (Exception e){
            System.out.println("exception");
        }


        new CountDownLatch(1).await();
    }

}

