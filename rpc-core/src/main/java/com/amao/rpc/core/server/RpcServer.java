package com.amao.rpc.core.server;

import com.amao.rpc.core.transport.auth.AuthServerHandler;
import com.amao.rpc.core.transport.codec.RpcDecoder;
import com.amao.rpc.core.transport.codec.RpcEncoder;
import com.amao.rpc.core.transport.handler.RpcServerHandler;
import com.amao.rpc.core.transport.heartbeat.HeartbeatServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by 阿毛 on 2016/6/10.
 */
public class RpcServer {

    private static Logger logger = LoggerFactory.getLogger(RpcServer.class);


    private ServerBootstrap serverBootstrap;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;


    public RpcServer() {
        initBootstrap();
    }

    private void initBootstrap() {
        serverBootstrap = new ServerBootstrap();
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();

        serverBootstrap.group(bossGroup, workerGroup);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline cp = ch.pipeline();
                cp.addLast("LengthFieldBasedFrameDecoder", new LengthFieldBasedFrameDecoder(65536, 0, 2, 0, 2));
                cp.addLast("LengthFieldPrepender", new LengthFieldPrepender(2));
                cp.addLast("RpcDecoder", new RpcDecoder());
                cp.addLast("RpcEncoder", new RpcEncoder());

                cp.addLast("AuthServerHandler", new AuthServerHandler());
                cp.addLast("IdleStateHandler", new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));
                cp.addLast("HeartbeatServerHandler", new HeartbeatServerHandler());
                cp.addLast("RpcServerHandler", new RpcServerHandler());
            }
        });

    }


    public void start(int port) throws InterruptedException {
        logger.info("run server start:" + port);
        ChannelFuture f = serverBootstrap.bind(port).sync();
        f.channel().closeFuture().sync();
    }

    public void stop() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }


}
