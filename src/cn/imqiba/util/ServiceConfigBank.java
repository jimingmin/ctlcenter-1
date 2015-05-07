package cn.imqiba.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ServiceConfigBank
{
	private Map<String, String> m_stConfigMap = new HashMap<String, String>();
	
	public boolean LoadConfig(String path)
	{
		File file = new File(path);
		String[] fileList = file.list();
		for (int i = 0; i < fileList.length; i++)
		{
			String configName = fileList[i];
			String configFullPath = file.getAbsolutePath() + File.separator + configName;
			
			String content = ReadFileContent(configFullPath);
			m_stConfigMap.put(configName, content);
		}
		
		return true;
	}
	
	public String[] GetFileList()
	{
		Set<String> fileSet = m_stConfigMap.keySet();
		String[] arrFileList = new String[fileSet.size()];
		int count = 0;
		for(String file : fileSet)
		{
			arrFileList[count++] = file;
		}
		return arrFileList;
	}
	
	public String ReadFileContent(String fileName)
	{
		String content = null;
		InputStream inputStream = null;
		InputStreamReader inputReader = null;
		BufferedReader bufferReader = null;
		try
		{
			inputStream = new FileInputStream(new File(fileName));
			inputReader = new InputStreamReader(inputStream);
			bufferReader = new BufferedReader(inputReader);
			
			String line = null;
			StringBuffer strBuffer = new StringBuffer();
			while ((line = bufferReader.readLine()) != null)
            {
                strBuffer.append(line);
            }
			
			content = strBuffer.toString();
			
			inputStream.close();
			inputReader.close();
			bufferReader.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return content;
	}
	
	public String GetFileContent(String fileName)
	{
		if(m_stConfigMap.containsKey(fileName))
		{
			return m_stConfigMap.get(fileName);
		}
		
		return null;
	}
}
