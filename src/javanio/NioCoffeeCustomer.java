package javanio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class NioCoffeeCustomer {

	public static void main(String[] args) {

		String host = "localhost";
		int port = 8080;
		String order = "Cappuccino";

		try {
			// Create a non-blocking client channel
			SocketChannel client = SocketChannel.open();
			client.configureBlocking(true); // blocking=true is fine for client

			// Connect to server
			client.connect(new InetSocketAddress(host, port));
			System.out.println("✅ Connected to NIO Coffee Shop!");

			// Send order
			ByteBuffer buffer = ByteBuffer.wrap(order.getBytes());
			client.write(buffer);
			System.out.println("🗣️ Placed order: " + order);

			// Prepare buffer to receive response
			ByteBuffer responseBuffer = ByteBuffer.allocate(1024);

			// Read response
			int bytesRead = client.read(responseBuffer);
			responseBuffer.flip();

			String response = StandardCharsets.UTF_8.decode(responseBuffer).toString();
			System.out.println("💬 Server replied: " + response);

			client.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
