package cn.imqiba.main;

import java.io.File;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import cn.imqiba.frame.MsgExecMap;
import cn.imqiba.util.ServerConfigBank;

public class CtlCenter
{
	static
	{
		PropertyConfigurator.configure(System.getProperty("user.dir") + File.separator + "log4j.properties");
	}
	
	private Logger logger = Logger.getLogger(Class.class);
	
	public void startCmdServer(int port)
	{
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup(1);
		try
		{
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(
					new ChannelInitializer<SocketChannel>()
					{
						public void initChannel(SocketChannel ch) throws Exception
						{
							ch.pipeline().addLast("decoder", new LengthFieldBasedFrameDecoder(1024 * 20000, 0, 4, 0, 4));
							ch.pipeline().addLast("encoder", new LengthFieldPrepender(4, false));
							ch.pipeline().addLast(new CmdHandler());
						}
					}).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);
			
			ChannelFuture f = b.bind(port).sync();
			f.channel().closeFuture().sync();
		}
		catch(Exception e)
		{
			logger.error( e.getClass().getName()+" : "+ e.getMessage() );
		}
		finally
		{
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
	
	public void startCtlServer(int port)
	{
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup(1);
		try
		{
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(
					new ChannelInitializer<SocketChannel>()
					{
						public void initChannel(SocketChannel ch) throws Exception
						{
							ch.pipeline().addLast("framedecoder", new DelimiterBasedFrameDecoder(1024, Delimiters.lineDelimiter()));
							ch.pipeline().addLast("stringdecoder", new StringDecoder());
							//ch.pipeline().addLast("frameencoder", new StringEncoder());
							ch.pipeline().addLast(new CmdHandler());
						}
					}).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);
			
			ChannelFuture f = b.bind(port).sync();
			f.channel().closeFuture().sync();
		}
		catch(Exception e)
		{
			logger.error( e.getClass().getName()+" : "+ e.getMessage() );
		}
		finally
		{
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
	
	public boolean initConfig()
	{
		boolean result = false;
		try
		{
			//ServerConfig.getInstance().parser(System.getProperty("user.dir") + "/server_config.xml");
			//StringConfig.getInstance().parser(System.getProperty("user.dir") + "/string_config.xml");
			
			result = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return result;
		}
		
		return result;
	}
	
	public static void main(String[] args)
	{
		System.out.println(System.getProperty("java.library.path"));
		try
		{
			ServerConfigBank.ScanDir("./config");
			
			MsgExecMap.initMsgClassMap("cn.imqiba.cmd");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				new CtlCenter().startCtlServer(5179);
			}
		}).start();
		
		new CtlCenter().startCmdServer(5178);
	}

}
