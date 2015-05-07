package cn.imqiba.util;

import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyException;

import java.lang.Thread;

public class FileWatcher
{
	public int watch(String path)
	{
        // watch mask, specify events you care about, 
        // or JNotify.FILE_ANY for all events. 
        int mask = JNotify.FILE_CREATED | JNotify.FILE_DELETED 
                | JNotify.FILE_MODIFIED | JNotify.FILE_RENAMED;
        
        try
        {
        	// watch subtree? 
        	boolean watchSubtree = true;
        	// add actual watch 
        	int watchID = JNotify.addWatch(path, mask, watchSubtree, new FileListener());
        	// sleep a little, the application will exit if you 
        	// don't (watching is asynchronous), depending on your 
        	// application, this may not be required 
//        	Thread.sleep(1000000);
//        	
//        	// to remove watch the watch
//        	boolean res = JNotify.removeWatch(watchID);
//        	if (!res)
//        	{
//        		// invalid watch ID specified. 
//        	}
        }
//        catch(InterruptedException e)
//        {
//        	e.printStackTrace();
//        }
        catch(JNotifyException e)
        {
        	e.printStackTrace();
        }
        
        return 0;
	}
}
