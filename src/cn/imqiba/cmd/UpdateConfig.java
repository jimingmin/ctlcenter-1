package cn.imqiba.cmd;

import java.util.ArrayList;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import org.json.JSONObject;

import cn.imqiba.impl.AbstractBusiness;
import cn.imqiba.impl.MsgAndExecAnnotation;
import cn.imqiba.util.ServerConfigBank;

@MsgAndExecAnnotation(msg = "update")
public class UpdateConfig extends AbstractBusiness
{
//	private static Logger logger = Logger.getLogger(Class.class);
	
	public String business(ChannelHandlerContext ctx, Object msg)
	{
		JSONObject paramsObject = (JSONObject)msg;
		String serverName = paramsObject.getString("server_name");
		String serviceName = paramsObject.getString("service_name");
		String configName = paramsObject.getString("config_name");
		
		ServerConfigBank.ScanDir(ServerConfigBank.GetConfigDir());
		
		String content = ServerConfigBank.GetConfigContent(serverName, serviceName, configName);
		if(content == null)
		{
			ctx.writeAndFlush("update config failed!");
			return null;
		}
		
		JSONObject respJson = new JSONObject();
		respJson.put("cmd", "load config");
		JSONObject paramsObj = new JSONObject();
		respJson.put("params", paramsObj);
		
		content = content.replaceAll("\t", "");
		paramsObj.put("config", configName);
		paramsObj.put("content", content);
		
		ByteBuf buf = Unpooled.buffer(respJson.toString().length() + 1);
		buf.writeBytes(respJson.toString().getBytes());
		buf.writeByte(0);
		
		ArrayList<ChannelHandlerContext> serviceSessions = ServerConfigBank.GetServiceSession(serverName, serviceName);
		for(ChannelHandlerContext serviceSession : serviceSessions)
		{
			serviceSession.writeAndFlush(buf);
		}
		
		ctx.writeAndFlush("update config success!");
		return null;
	}
}
