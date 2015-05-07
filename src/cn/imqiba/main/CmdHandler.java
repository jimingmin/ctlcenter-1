package cn.imqiba.main;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.buffer.SlicedByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledUnsafeDirectByteBuf;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import cn.imqiba.frame.MsgExecMap;
import cn.imqiba.impl.AbstractBusiness;

public class CmdHandler extends ChannelInboundHandlerAdapter
{
	private static Logger logger = Logger.getLogger(Class.class);
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		JSONObject reqJson = null;
		if(msg instanceof UnpooledUnsafeDirectByteBuf)
		{
			byte[] data = new byte[((UnpooledUnsafeDirectByteBuf)msg).writerIndex()];
			((UnpooledUnsafeDirectByteBuf)msg).getBytes(0, data);
			if(data.length == 0)
			{
				return;
			}
			reqJson = new JSONObject(new String(data));
		}
		else if(msg instanceof String)
		{
			reqJson = new JSONObject(msg.toString());
		}
		else
		{
			logger.warn("msg is " + msg.toString());
			return;
		}

		String cmd = reqJson.getString("cmd");
		String result = null;
		try
		{
			Class<?> cls = MsgExecMap.getMsgClassByMsg(cmd);
			AbstractBusiness handler = (AbstractBusiness)cls.newInstance();
			result = handler.business(ctx, reqJson.getJSONObject("params"));
		}
		catch(Exception e)
		{
			logger.error( e.getClass().getName()+" : "+ e.getMessage() );
		}
		
		if(result != null)
		{
			ByteBuf buf = Unpooled.buffer(result.length());
			buf.writeBytes(result.getBytes());
			ctx.writeAndFlush(buf);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
	}
}
