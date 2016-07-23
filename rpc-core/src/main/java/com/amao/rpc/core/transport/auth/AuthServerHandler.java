package com.amao.rpc.core.transport.auth;

import com.amao.rpc.core.data.*;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 阿毛 on 2016/6/21.
 */
public class AuthServerHandler extends ChannelHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(AuthServerHandler.class);
    private Map<String, Boolean> nodesOnLine = new ConcurrentHashMap<>();
    private AuthChecker authChecker = new AuthChecker();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Request request = new Request((MessagePacket) msg);
        if (request.getMessageHeader().getType() == MessageType.HANDSHAKE_REQ.getValue()) {
            InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
            String clientName = socketAddress.toString();

            Object[] body = new Object[1];
            if (nodesOnLine.containsKey(clientName)) {
                body[0] = AuthResultType.AUTH_LOGIN_REPEATED.getValue();
            } else {
                String ip = socketAddress.getAddress().getHostAddress();
                boolean authOK = authChecker.check(ip);
                if (authOK) {
                    body[0] = AuthResultType.AUTH_OK.getValue();
                } else {
                    body[0] = AuthResultType.AUTH_NOT_IN_WHITE_LIST.getValue();
                    logger.error(clientName + "==>" + AuthResultType.AUTH_NOT_IN_WHITE_LIST.getDescription());
                }
                nodesOnLine.put(clientName, true);
            }
            Response Response = ResponseFactory.newHandshakeResponse(body, request.getMessageHeader().getSessionId());

            ctx.channel().writeAndFlush(Response);
        } else {
            super.channelRead(ctx, msg);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        nodesOnLine.remove(ctx.channel().remoteAddress().toString());
        ctx.close();
        cause.printStackTrace();
        super.exceptionCaught(ctx, cause);
    }
}
