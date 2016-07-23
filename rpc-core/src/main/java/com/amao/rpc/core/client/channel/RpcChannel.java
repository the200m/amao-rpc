package com.amao.rpc.core.client.channel;


import com.amao.rpc.core.data.MessagePacket;
import com.amao.rpc.core.util.AssertUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by 阿毛 on 2016/6/23.
 */
public class RpcChannel {

    private static Logger logger = LoggerFactory.getLogger(RpcChannel.class);
    private Channel channel;

    public RpcChannel(Channel channel) {
        AssertUtils.notNull(channel, "no channel");
        this.channel = channel;
    }

    public void send(MessagePacket messagePacket, final SendListener sendListener ) {
        if (channel.isOpen()) {
            ChannelFuture f = channel.writeAndFlush(messagePacket);
            f.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(final ChannelFuture channelFuture) throws Exception {
                    if (!channelFuture.isSuccess()) {
                        sendListener.onSendFail();
                    }
                }
            });
        } else {
            logger.warn("channel is not valid");
        }

    }


}
