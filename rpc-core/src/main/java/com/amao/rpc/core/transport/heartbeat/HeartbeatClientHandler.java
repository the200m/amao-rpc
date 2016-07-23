package com.amao.rpc.core.transport.heartbeat;

import com.amao.rpc.core.data.Request;
import com.amao.rpc.core.data.RequestFactory;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 阿毛 on 2016/6/22.
 */
public class HeartbeatClientHandler extends ChannelHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(HeartbeatClientHandler.class);

    private static final int RETRY_TIMES = 3;

    private int currentTime = 0;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.WRITER_IDLE) {
                if (currentTime <= RETRY_TIMES) {
                    currentTime++;
                    Request request = RequestFactory.newHeartBeatRequest();
                    ctx.channel().writeAndFlush(request);
                }
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }


}

