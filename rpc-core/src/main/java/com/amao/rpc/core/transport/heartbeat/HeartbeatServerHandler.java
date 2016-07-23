package com.amao.rpc.core.transport.heartbeat;

import com.amao.rpc.core.data.MessagePacket;
import com.amao.rpc.core.data.MessageType;
import com.amao.rpc.core.data.Request;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 阿毛 on 2016/6/22.
 */
public class HeartbeatServerHandler extends ChannelHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(HeartbeatServerHandler.class);

    private int lossConnectTime = 0;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                lossConnectTime++;
                logger.warn("5 秒没有接收到客户端的信息了");
                if (lossConnectTime > 2) {
                    logger.warn("关闭这个不活跃的channel");
                    ctx.channel().close();
                }
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Request request = new Request((MessagePacket) msg);
        if (request.getMessageHeader().getType() == MessageType.HEARTBEAT_REQ.getValue()) {
            logger.info("server receive heartbeat:" + request.getMessageHeader().getSessionId());
        } else {
            super.channelRead(ctx, msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

