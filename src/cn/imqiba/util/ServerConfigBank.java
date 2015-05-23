package cn.imqiba.util;

import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.File;

public class ServerConfigBank
{
	public static Map<String, ServerBank> m_stServerMap = new HashMap<String, ServerBank>();
	public static String m_strBaseDir = null;
	
	public static String GetConfigDir()
	{
		return m_strBaseDir;
	}
	
	public static int ScanDir(String path)
	{
		if(path == null)
		{
			return 1;
		}
		
		File rootDir = new File(path);
		if (!rootDir.isDirectory())
		{
			return 1;
		}
		else
		{
			m_strBaseDir = rootDir.getAbsolutePath();
			String[] fileList = rootDir.list();
			for (int i = 0; i < fileList.length; i++)
			{
				String serverAddress = fileList[i];
				path = m_strBaseDir + File.separator + serverAddress;
				ServerBank serverBank = m_stServerMap.get(serverAddress);
				if(serverBank == null)
				{
					serverBank = new ServerBank();
				}
				
				serverBank.LoadServiceConfig(path);
				m_stServerMap.put(serverAddress, serverBank);
			}
		}
		
		return 0;
	}
	
	public static String[] GetConfigList(String serverAddress, String serviceName)
	{
		if(m_stServerMap.containsKey(serverAddress))
		{
			ServerBank serverBank = m_stServerMap.get(serverAddress);
			ServiceConfigBank serviceConfigBank = serverBank.GetServiceConfigBank(serviceName);
			if(serviceConfigBank != null)
			{
				return serviceConfigBank.GetFileList();
			}
		}
		
		return null;
	}
	
	public static String GetConfigContent(String serverAddress, String serviceName, String configName)
	{
		String content = null;
		if(m_stServerMap.containsKey(serverAddress))
		{
			ServerBank serverBank = m_stServerMap.get(serverAddress);
			ServiceConfigBank serviceConfigBank = serverBank.GetServiceConfigBank(serviceName);
			if(serviceConfigBank != null)
			{
				content = serviceConfigBank.GetFileContent(configName);
			}
		}
		return content;
	}
	
	public static void RegistService(String serverAddress, String serviceName, ChannelHandlerContext ctx)
	{
		if(m_stServerMap.containsKey(serverAddress))
		{
			ServerBank serverBank = m_stServerMap.get(serverAddress);
			serverBank.RegistSession(serviceName, ctx);
		}
	}
	
	public static void UnregistService(String serverAddress, ChannelHandlerContext ctx)
	{
		if(m_stServerMap.containsKey(serverAddress))
		{
			ServerBank serverBank = m_stServerMap.get(serverAddress);
			serverBank.UnregistSession(ctx);
		}
	}
	
	public static ArrayList<ChannelHandlerContext> GetServiceSession(String serverAddress, String serviceName)
	{
		ServerBank serverBank = m_stServerMap.get(serverAddress);
		if(serverBank != null)
		{
			return serverBank.GetServiceSessions(serviceName);
		}
		
		return null;
	}
}
