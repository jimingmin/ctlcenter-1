package cn.imqiba.cmd;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import cn.imqiba.impl.AbstractBusiness;
import cn.imqiba.impl.MsgAndExecAnnotation;
import cn.imqiba.util.ServerConfigBank;

@MsgAndExecAnnotation(msg = "update")
public class UpdateConfig extends AbstractBusiness
{
	private static Logger logger = Logger.getLogger(Class.class);
	
	public String business(ChannelHandlerContext ctx, Object msg)
	{
		JSONObject paramsObject = (JSONObject)msg;
		String configName = paramsObject.getString("config_name");
		
		System.out.println(configName);
		return null;
	}
}
