package javaio;

import java.io.*;
import java.net.*;

public class CoffeeShopServer {
	public static void main(String[] args) {

		int port = 8080;

		// This is like the coffee shop
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			System.out.println("☕ Coffee Shop Server is open on port " + port + "...");

			// Keep listening for new customers
			while (true) {
				// Entrance door opens, customer walks in
				Socket customer = serverSocket.accept();
				System.out.println("👋 New customer connected: " + customer.getInetAddress());

				// Assign a waiter (thread) to handle this customer
				new Thread(new Waiter(customer)).start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class Waiter implements Runnable {
	private Socket customer;

	public Waiter(Socket customer) {
		this.customer = customer;
	}

	@Override
	public void run() {
		try (InputStream in = customer.getInputStream(); // Stream: Customer → Server
				OutputStream out = customer.getOutputStream() // Stream: Server → Customer
		) {
			byte[] buffer = new byte[1024]; // Buffer = coffee cup
			int bytesRead = in.read(buffer); // Read what the customer says

			String order = new String(buffer, 0, bytesRead);
			System.out.println("🗣️ Customer ordered: " + order);

			// Prepare the response (coffee)
			String response = "☕ Here’s your " + order.trim() + "!\n";
			out.write(response.getBytes()); // Send it back to the customer

			System.out.println("✅ Served: " + order.trim());
			customer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
