package javaio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class CoffeeCustomer {
	public static void main(String[] args) {

		String host = "localhost"; // The Coffee Shop address
		int port = 8080; // Shop door, where customer enters
		String order = "Cappuccino"; // Your coffee order

		// Customer connecting to the coffee shop
		try (Socket socket = new Socket(host, port)) {
			System.out.println("✅ Connected to the Coffee Shop!");

			OutputStream out = socket.getOutputStream();
			InputStream in = socket.getInputStream();

			// Send the order
			out.write(order.getBytes());
			out.flush();
			System.out.println("🗣️ Placed order: " + order);

			// Wait for the response
			byte[] buffer = new byte[1024];
			int bytesRead = in.read(buffer);
			String reply = new String(buffer, 0, bytesRead);

			System.out.println("💬 Server replied: " + reply);

			// socket.close() is called automatically when try block ends

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
