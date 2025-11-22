package javaio;

import java.io.*;
import java.net.*;

public class CoffeeCustomerMultiple {
	public static void main(String[] args) {

		String host = "localhost";
		int port = 8080;

		String[] orders = { "Cappuccino", "Latte", "Espresso", "Mocha", "Americano", "Flat White", "Macchiato",
				"Irish Coffee", "Cortado", "Affogato" };

		for (int i = 0; i < 10; i++) {
			String order = orders[i];
			System.out.println("\n===== Customer " + (i + 1) + " Entering Coffee Shop =====");

			try (Socket socket = new Socket(host, port)) {

				OutputStream out = socket.getOutputStream();
				InputStream in = socket.getInputStream();

				out.write(order.getBytes());
				out.flush();
				System.out.println("🗣️ Customer " + (i + 1) + " ordered: " + order);

				byte[] buffer = new byte[1024];
				int bytesRead = in.read(buffer);
				String reply = new String(buffer, 0, bytesRead);

				System.out.println("💬 Server replied: " + reply);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		System.out.println("\n🎉 All 10 customers have been served!");
	}
}
