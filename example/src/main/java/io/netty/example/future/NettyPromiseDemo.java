package io.netty.example.future;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;

import java.util.concurrent.TimeUnit;

public class NettyPromiseDemo {
    public static void main(String[] args) {
        //异步线程池
        NioEventLoopGroup loop = new NioEventLoopGroup(4);
        DefaultPromise<String> promise = new DefaultPromise<>(loop.next());

        promise.addListener(future -> System.out.println(future.get()));

        loop.schedule(() -> {
            try {
                Thread.sleep(1000);
                promise.setSuccess("state: 执行成功 " + "result: "+100);
                return promise;
            } catch (InterruptedException ignored) {
                promise.setFailure(ignored);
            }
            return promise;
        }, 0, TimeUnit.SECONDS);
    }
}
