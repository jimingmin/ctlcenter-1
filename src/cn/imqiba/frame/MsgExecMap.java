package cn.imqiba.frame;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.imqiba.impl.MsgAndExecAnnotation;
import cn.imqiba.util.ScanClasses;

public class MsgExecMap
{
	private static MsgExecMap m_stMsgExecMap = new MsgExecMap();
	private static Map<String, Class<?>> m_MsgClassMap;
	
	public static MsgExecMap getInstance()
	{
		return m_stMsgExecMap;
	}
	
	// 根据类型得到对应的消息类的class对象  
	public static Class<?> getMsgClassByMsg(String msg)
	{
		return m_MsgClassMap.get(msg);
	}
	
	public static void initMsgClassMap(String path) throws ClassNotFoundException, IOException
	{
		Map<String, Class<?>> tmpMap = new HashMap<String, Class<?>>();  
		Set<Class<?>> classSet = ScanClasses.getClasses(path);
		if (classSet != null)
		{
			for (Class<?> clazz : classSet)
			{
				if (clazz.isAnnotationPresent(MsgAndExecAnnotation.class))
				{
					MsgAndExecAnnotation annotation = clazz.getAnnotation(MsgAndExecAnnotation.class);
					tmpMap.put(annotation.msg(), clazz);
				}
			}
		}
		m_MsgClassMap = Collections.unmodifiableMap(tmpMap);  
	}
}
