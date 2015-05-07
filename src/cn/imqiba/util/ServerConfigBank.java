package cn.imqiba.util;

import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ServerConfigBank
{
	public static Map<String, ServerBank> m_stServerMap = new HashMap<String, ServerBank>();
	
	public static String ScanDir(String path)
	{
		File rootDir = new File(path);
		if (!rootDir.isDirectory())
		{
			return null;
		}
		else
		{
			String[] fileList = rootDir.list();
			for (int i = 0; i < fileList.length; i++)
			{
				String serverAddress = fileList[i];
				path = rootDir.getAbsolutePath() + File.separator + serverAddress;
				ServerBank serverBank = new ServerBank();
				serverBank.LoadServiceConfig(path);
				m_stServerMap.put(serverAddress, serverBank);
			}
		}
		
		return null;
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
}
