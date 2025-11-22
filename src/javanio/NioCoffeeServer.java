package javanio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class NioCoffeeServer {

    public static void main(String[] args) throws IOException {

        // 1️.Create a selector (event loop)
        Selector selector = Selector.open();

        // 2.Creates a non-blocking ServerSocketChannel
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false); // NIO - non-blocking mode

        // 3.Bind the server to port 8080
        serverChannel.bind(new InetSocketAddress(8080));
        System.out.println("☕ NIO Coffee Shop open on port 8080...");

        // 4.Register this server with selector for ACCEPT events
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 5.Main event loop (like Netty’s event loop, NodeJS loop, etc.)
        while (true) {

            // Blocks until at least one event is ready (ACCEPT or READ)
            selector.select();

            // Get the list of selected (ready) events
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

            while (keys.hasNext()) {
                SelectionKey key = keys.next();
                keys.remove(); // Remove so it won’t be processed again

                // 6.If a new client is connecting
                if (key.isAcceptable()) {
                    handleAccept(serverChannel, selector);
                }

                // 7.If an existing client sent some data
                else if (key.isReadable()) {
                    handleRead(key);
                }

            }

        }

    }

    /**
     * Handles a new incoming client connection
     */
    private static void handleAccept(ServerSocketChannel serverChannel, Selector selector) throws IOException {

        // Accept the client (non-blocking)
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);

        System.out.println("👋 New customer: " + clientChannel.getRemoteAddress());

        // Allocate buffer per client (used to accumulate reads)
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        clientChannel.register(selector, SelectionKey.OP_READ, buffer);

    }

    /**
     * Handles reading data from a client
     */
    private static void handleRead(SelectionKey key) throws IOException {

        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();

        // Read bytes from client (non-blocking)
        int bytesRead = clientChannel.read(buffer);

        if (bytesRead == -1) {
            // Client closed connection
            clientChannel.close();
            System.out.println("❌ Customer disconnected");
            return;
        }

        // Flip buffer to read mode
        buffer.flip();
        String order = StandardCharsets.UTF_8.decode(buffer).toString().trim();

        System.out.println("🗣️ Customer ordered: " + order);

        // Prepare response
        String respone = "☕ Here’s your " + order + "!\n";
        ByteBuffer outBuffer = ByteBuffer.wrap(respone.getBytes());

        // Write response to client
        clientChannel.write(outBuffer);

        System.out.println("✅ Served: " + order);

        // Clear buffer for next read
        buffer.clear();

        // Close connection after serving
        clientChannel.close();

    }

}
