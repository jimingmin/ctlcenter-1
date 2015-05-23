package cn.imqiba.cmd;

import java.net.InetSocketAddress;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import org.json.JSONObject;

import cn.imqiba.impl.AbstractBusiness;
import cn.imqiba.impl.MsgAndExecAnnotation;
import cn.imqiba.util.ServerConfigBank;

@MsgAndExecAnnotation(msg = "regist")
public class CmdRegist extends AbstractBusiness
{
//	private static Logger logger = Logger.getLogger(Class.class);
	
	public String business(ChannelHandlerContext ctx, Object msg)
	{
		JSONObject paramsObject = (JSONObject)msg;
		String serviceName = paramsObject.getString("server_name");
		InetSocketAddress remoteAddress = (InetSocketAddress)ctx.channel().remoteAddress();
		String remoteServerAddress = remoteAddress.getAddress().getHostAddress();
		int loadConfig = paramsObject.getInt("load_config");
		if(loadConfig == 0)
		{
			JSONObject respJson = new JSONObject();
			respJson.put("cmd", "load config");
			JSONObject paramsObj = new JSONObject();
			respJson.put("params", paramsObj);
			
			String fileList[] = ServerConfigBank.GetConfigList(remoteServerAddress, serviceName);
			for(String file : fileList)
			{
				String content = ServerConfigBank.GetConfigContent(remoteServerAddress, serviceName, file);
				content = content.replaceAll("\t", "");
				paramsObj.put("config", file);
				paramsObj.put("content", content);
				
				ByteBuf buf = Unpooled.buffer(respJson.toString().length() + 1);
				buf.writeBytes(respJson.toString().getBytes());
				buf.writeByte(0);
				ctx.writeAndFlush(buf);
			}
			
			respJson.put("cmd", "load finish");
			respJson.put("params", "");
			ByteBuf buf = Unpooled.buffer(respJson.toString().length() + 1);
			buf.writeBytes(respJson.toString().getBytes());
			buf.writeByte(0);
			ctx.writeAndFlush(buf);
		}
		
		ServerConfigBank.RegistService(remoteServerAddress, serviceName, ctx);
		//String content = ServerConfigBank.GetConfigContent("192.168.80.132", "account", "redis_config.xml");
		//System.out.println(content);
		return null;
	}
}
