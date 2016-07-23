package com.amao.rpc.core.client;

import com.amao.rpc.core.client.future.RpcFutureContainer;
import com.amao.rpc.core.client.channel.RpcChannelManager;
import com.amao.rpc.core.client.channel.RpcChannel;
import com.amao.rpc.core.transport.auth.AuthClientHandler;
import com.amao.rpc.core.transport.codec.RpcDecoder;
import com.amao.rpc.core.transport.codec.RpcEncoder;
import com.amao.rpc.core.transport.handler.RpcClientHandler;
import com.amao.rpc.core.transport.heartbeat.HeartbeatClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * Created by 阿毛 on 2016/6/10.
 */
public class RpcClient {

    private static Logger logger = LoggerFactory.getLogger(RpcClient.class);

    private EventLoopGroup group = new NioEventLoopGroup();

    private Bootstrap bootstrap;

    private RpcFutureContainer rpcFutureContainer = new RpcFutureContainer();

    private RpcChannelManager channelPool = new RpcChannelManager(this);

    public RpcClient() {
        initBootstrap();
    }

    private void initBootstrap() {
        bootstrap = new Bootstrap();
        bootstrap.group(group);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline cp = ch.pipeline();
                cp.addLast("LengthFieldBasedFrameDecoder", new LengthFieldBasedFrameDecoder(65536, 0, 2, 0, 2));
                cp.addLast("LengthFieldPrepender", new LengthFieldPrepender(2));
                cp.addLast("RpcDecoder", new RpcDecoder());
                cp.addLast("RpcEncoder", new RpcEncoder());


                cp.addLast("AuthClientHandler", new AuthClientHandler());
                cp.addLast("IdleStateHandler", new IdleStateHandler(0, 4, 0, TimeUnit.SECONDS));
                cp.addLast("HeartbeatClientHandler", new HeartbeatClientHandler());
                cp.addLast("RpcClientHandler", new RpcClientHandler(rpcFutureContainer));
            }
        });

    }


    public RpcChannel connect(InetSocketAddress serviceProviderAddress) {
        RpcChannel rpcChannel = doNotRetryConnectSync(serviceProviderAddress);
        channelPool.addRpcChannel(serviceProviderAddress, rpcChannel);
        return rpcChannel;
    }


    private RpcChannel doNotRetryConnectSync(final InetSocketAddress inetSocketAddress) {
        ChannelFuture f = null;
        try {
            f = bootstrap.connect(inetSocketAddress).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new RpcChannel(f.channel());

    }

    private RpcChannel doConnectAsync(final InetSocketAddress inetSocketAddress) {
        ChannelFuture f = bootstrap.connect(inetSocketAddress).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    logger.warn("fail to connect to remote server,need retry");
                    final EventLoop loop = future.channel().eventLoop();
                    loop.schedule(new Runnable() {
                        @Override
                        public void run() {
                            doConnectAsync(inetSocketAddress);
                        }
                    }, 3L, TimeUnit.SECONDS);
                }
            }
        });
        return new RpcChannel(f.channel());

    }


    public RpcChannelManager getChannelPool() {
        return channelPool;
    }

    public RpcFutureContainer getRpcFutureContainer() {
        return rpcFutureContainer;
    }

    public void shutdown() {
        if (group != null) {
            group.shutdownGracefully();
        }
        channelPool.clear();
    }

    public void stop() {
        shutdown();
    }


}
