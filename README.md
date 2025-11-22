# ☕ Coffee Shop Socket Demo (Java)

A simple and fun **TCP Client–Server application** built using Java’s **Blocking I/O (BIO)**.

The project simulates a coffee shop where:
- The **Server** acts like a coffee shop
- Each **Client** is a customer sending an order
- A **Waiter Thread** serves each customer

## 🚀 Features
- Classic **ServerSocket / Socket** communication
- **Multi-threaded server**
- Simple text protocol
- Clear console logs
- Fully working Java I/O example


▶️ Running the Application
1. Start `CoffeeShopServer.java`
2. Start `CoffeeCustomer.java`

   Java IO (Blocking) This is like a coffee shop with a single counter:
1. When a thread reads from a stream (InputStream/Reader), it waits until the data is available.
2. When it writes, it waits until the write finishes.
3. Each client connection needs a dedicated thread.
4. Not efficient when you have many clients because threads get stuck waiting
