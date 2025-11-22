package netty;

import java.nio.charset.StandardCharsets;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettyCoffeeCustomer {

	public static void main(String[] args) throws Exception {

		// Client needs only ONE EventLoopGroup (no boss/worker split)
		EventLoopGroup group = new NioEventLoopGroup();

		try {
			// Bootstrap = Netty’s client builder
			Bootstrap bootstrap = new Bootstrap();

			bootstrap.group(group)
					// Use NIO-based client channel
					.channel(NioSocketChannel.class)

					// Configure pipeline for this client connection
					.handler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) {

							// Add decoders and encoders
							ch.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8));
							ch.pipeline().addLast(new StringEncoder(StandardCharsets.UTF_8));

							// Business logic handler
							ch.pipeline().addLast(new CustomerHandler());
						}
					});

			// Connect to the server
			Channel channel = bootstrap.connect("localhost", 8080).sync().channel();

			// Your coffee order
			String order = "Cappuccino";
			System.out.println("🗣️ Placing order: " + order);

			// Send the order to server
			channel.writeAndFlush(order);

			// Wait until server closes the connection
			channel.closeFuture().sync();

		} finally {
			// Shutdown client thread group
			group.shutdownGracefully();
		}
	}

	// This handler receives responses from the server
	static class CustomerHandler extends SimpleChannelInboundHandler<String> {

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, String msg) {
			// Server reply already decoded into a String
			System.out.println("💬 Server replied: " + msg);
		}
	}
}
