package cn.imqiba.util;

import sun.misc.Signal;
import sun.misc.SignalHandler;

public class UserSignalHandler implements SignalHandler
{
	public void setupUserHandler()
	{
		try
		{
			Signal.handle(new Signal("USR1"), this);
		}
		catch (IllegalArgumentException e)
		{
			// 可能这个信号,并不支持这个平台或JVM作为目前配置
			e.printStackTrace();
		}
	}
	
	@Override
	public synchronized void handle(Signal sig)
	{
		System.out.println(sig.getName());
	}

}
