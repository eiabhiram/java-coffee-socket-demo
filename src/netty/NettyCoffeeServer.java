package netty;

import java.nio.charset.StandardCharsets;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * 
 * ⭐ Netty has 3 magical components:
 *
 * 1️. EventLoopGroup - A pool of threads that manages events (accept, read,
 * write) - Works like NIO Selectors but is highly optimized for performance
 *
 * 2️. ChannelPipeline - A conveyor belt through which data flows - Contains
 * multiple handlers that process data in order
 *
 * 3️. Handlers - Your business logic goes here - Example: reading customer
 * order, sending back reply
 *
 * This server: - Accepts client connections (bossGroup) - Handles read/write
 * (workerGroup) - Decodes incoming bytes → String (StringDecoder) - Encodes
 * outgoing String → bytes (StringEncoder) - Uses CoffeeHandler for final
 * processing
 *
 */

public class NettyCoffeeServer {

	public static void main(String[] args) throws Exception {

		// Thread group #1 — ACCEPTS new client connections
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);

		// Thread group #2 — Handles READ/WRITE for connected clients
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			// Bootstrap = Netty’s server builder
			ServerBootstrap bootstrap = new ServerBootstrap();

			bootstrap.group(bossGroup, workerGroup)
					// Use NIO-based server channel (non-blocking)
					.channel(NioServerSocketChannel.class)

					// This runs for EACH new client connection
					.childHandler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) {

							// Retrieve the pipeline (data processing chain)
							ChannelPipeline pipeline = ch.pipeline();

							// Incoming bytes → Java String
							pipeline.addLast(new StringDecoder(StandardCharsets.UTF_8));

							// Outgoing String → bytes
							pipeline.addLast(new StringEncoder(StandardCharsets.UTF_8));

							// Your business logic
							pipeline.addLast(new CoffeeHandler());
						}
					});

			// Bind the server to port 8080 and wait until it starts
			ChannelFuture future = bootstrap.bind(8080).sync();
			System.out.println("☕ Netty Coffee Shop open on port 8080!");

			// Wait until the server socket is closed (runs forever)
			future.channel().closeFuture().sync();

		} finally {
			// Gracefully shutdown thread pools
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	/**
	 * This handler receives fully-decoded String messages from the client.
	 *
	 * SimpleChannelInboundHandler<String>: - Automatically receives decoded String
	 * messages - Automatically releases buffers - Calls channelRead0() for each
	 * message
	 */
	static class CoffeeHandler extends SimpleChannelInboundHandler<String> {

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, String msg) {

			// Netty already decoded msg → String
			System.out.println("🗣️ Customer ordered: " + msg);

			// Prepare server response
			String reply = "☕ Here’s your " + msg + "!\n";

			// Writes back to client through pipeline
			ctx.writeAndFlush(reply);

			System.out.println("✅ Served: " + msg);
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) {
			// Triggered when a new client connects
			System.out.println("👋 New customer connected!");
		}

		@Override
		public void channelInactive(ChannelHandlerContext ctx) {
			// Triggered when client disconnects
			System.out.println("❌ Customer disconnected.");
		}
	}
}
