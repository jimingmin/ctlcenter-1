package cn.imqiba.util;

import net.contentobjects.jnotify.JNotifyListener;

//可以在下面的监控方法中添加自己的代码。比如在fileModified中添加重新加载配置文件的代码 
class FileListener implements JNotifyListener
{
	public boolean isXMLFile(String file)
	{
		int index = file.lastIndexOf(".");
		String sufString = file.substring(index);
		if(sufString.equals(".xml"))
		{
			return true;
		}
		
		return false;
	}
	
	public void fileRenamed(int wd, String rootPath, String oldName, String newName)
	{
	    print("renamed " + rootPath + " : " + oldName + " -> " + newName);
	    if(!isXMLFile(newName))
	    {
	    	return;
	    }
	}
	
	public void fileModified(int wd, String rootPath, String name)
	{
	    print("modified " + rootPath + " : " + name);
	    if(!isXMLFile(name))
	    {
	    	return;
	    }
	}
	
	public void fileDeleted(int wd, String rootPath, String name)
	{
	    print("deleted " + rootPath + " : " + name);
	    if(!isXMLFile(name))
	    {
	    	return;
	    }
	}
	
	public void fileCreated(int wd, String rootPath, String name)
	{
	    print("created " + rootPath + " : " + name);
	    if(!isXMLFile(name))
	    {
	    	return;
	    }
	}
	
	void print(String msg)
	{
	    System.err.println(msg); 
	}
} 
