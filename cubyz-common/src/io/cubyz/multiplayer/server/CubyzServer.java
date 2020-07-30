package io.cubyz.multiplayer.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class CubyzServer {

	private int port;
	static ServerSettings settings = new ServerSettings();
	
	static Channel ch;
	static EventLoopGroup boss;
	static EventLoopGroup worker;
	static ServerHandler handler;
	
	static {
		settings.maxPlayers = 20;
		settings.playerTimeout = 5000;
		settings.playerPingTime = 5000;
	}

	public CubyzServer(int port) {
		this.port = port;
	}
	
	public void stop() throws Exception {
		ServerHandler.th.interrupt();
		ServerHandler.stellarTorus.cleanup();
		
		ch.close();
		worker.shutdownGracefully();
		boss.shutdownGracefully();
	}

	public void start() throws Exception {
		boss = new NioEventLoopGroup();
		worker = new NioEventLoopGroup();
		handler = new ServerHandler(this, settings);
		
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(boss, worker).channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 128)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(handler);
						}
					});
			
			ch = b.bind(port).channel();
			
			ch.closeFuture().sync(); // wait for server completion
		} finally {
			worker.shutdownGracefully();
			boss.shutdownGracefully();
		}
	}

}
