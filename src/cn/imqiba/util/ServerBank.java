package cn.imqiba.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ServerBank
{
	private Map<String, ServiceConfigBank> m_stServiceMap = new HashMap<String, ServiceConfigBank>();
	
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
			
			ServiceConfigBank configBank = new ServiceConfigBank();
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
}
