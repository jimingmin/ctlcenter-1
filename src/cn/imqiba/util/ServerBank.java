package cn.imqiba.util;

import io.netty.channel.ChannelHandlerContext;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServerBank
{
	private Map<String, ServiceConfigBank> m_stServiceMap = new HashMap<String, ServiceConfigBank>();
	private Map<String, ArrayList<ChannelHandlerContext>> m_stServiceSessionMap = new HashMap<String, ArrayList<ChannelHandlerContext>>();
	private Map<ChannelHandlerContext, String> m_stSessionServiceMap = new HashMap<ChannelHandlerContext, String>();
	
	public void LoadServiceConfig(String path)
	{
		File file = new File(path);
		if(!file.isDirectory())
		{
			return;
		}
		
		String[] fileList = file.list();
		for (int i = 0; i < fileList.length; i++)
		{
			String serviceName = fileList[i];
			String serviceDir = file.getAbsolutePath() + File.separator + serviceName;
			
			ServiceConfigBank configBank = m_stServiceMap.get(serviceName);
			if(configBank == null)
			{
				configBank = new ServiceConfigBank();
			}
			configBank.LoadConfig(serviceDir);
			m_stServiceMap.put(serviceName, configBank);
		}
	}
	
	public ServiceConfigBank GetServiceConfigBank(String serviceName)
	{
		if(m_stServiceMap.containsKey(serviceName))
		{
			return m_stServiceMap.get(serviceName);
		}
		
		return null;
	}
	
	public void RegistSession(String serviceName, ChannelHandlerContext ctx)
	{
		ArrayList<ChannelHandlerContext> stCtxList = null;
		if(m_stServiceSessionMap.containsKey(serviceName))
		{
			stCtxList = m_stServiceSessionMap.get(serviceName);
		}
		else
		{
			stCtxList = new ArrayList<ChannelHandlerContext>();
			m_stServiceSessionMap.put(serviceName, stCtxList);
		}
		
		stCtxList.add(ctx);
		m_stSessionServiceMap.put(ctx, serviceName);
	}
	
	public void UnregistSession(ChannelHandlerContext ctx)
	{
		String serviceName = m_stSessionServiceMap.get(ctx);
		if(serviceName == null)
		{
			return;
		}
		
		ArrayList<ChannelHandlerContext> stCtxList = m_stServiceSessionMap.get(serviceName);
		for(ChannelHandlerContext serviceSession : stCtxList)
		{
			if(serviceSession == ctx)
			{
				stCtxList.remove(ctx);
				break;
			}
		}
		m_stSessionServiceMap.remove(ctx);
	}
	
	public ArrayList<ChannelHandlerContext> GetServiceSessions(String serviceName)
	{
		if(m_stServiceSessionMap.containsKey(serviceName))
		{
			return m_stServiceSessionMap.get(serviceName);
		}
		
		return null;
	}
}
