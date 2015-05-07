package cn.imqiba.cmd;

import io.netty.channel.ChannelHandlerContext;


import org.apache.log4j.Logger;
import org.json.JSONObject;

import cn.imqiba.impl.AbstractBusiness;
import cn.imqiba.impl.MsgAndExecAnnotation;

@MsgAndExecAnnotation(msg = "loadconfigerror")
public class LoadConfigError extends AbstractBusiness
{
	private static Logger logger = Logger.getLogger(Class.class);
	
	public String business(ChannelHandlerContext ctx, Object msg)
	{
		JSONObject paramsObject = (JSONObject)msg;
		logger.warn(paramsObject.toString());
		return null;
	}
}

