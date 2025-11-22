# ☕ Java Coffee Socket Demo

A fun and educational **Java networking project** demonstrating:

- Java **Blocking I/O (BIO)** sockets  
- Java **Non-Blocking I/O (NIO)** with selectors  
- A high-performance **Netty** server and client  

The project simulates a small “coffee shop” where a client sends a coffee order, and the server replies with the prepared drink.  
It’s designed as a progression from **BIO → NIO → Netty** so beginners can understand how networking evolves from simple to high-performance frameworks.

## 🚀 Features

### ✔ Java BIO Version  
- Uses `ServerSocket` + `Socket`  
- Blocking read/write  
- One thread per client (classic approach)

### ✔ Java NIO Version  
- Uses `Selector`, `ServerSocketChannel`, `SocketChannel`  
- Non-blocking I/O  
- Event-driven architecture  
- Single-threaded high scalability

### ✔ Netty Version  
- Uses industry-standard networking framework  
- EventLoopGroups + pipelines  
- Automatic encoding/decoding  
- Clean, simple handlers  
- High-performance, production-grade networking

## 📂 Project Structure

```
java-coffee-socket-demo/
│
├─ javaio/               → Blocking I/O version
│   ├─ CoffeeShopServer.java
│   └─ CoffeeCustomer.java
│
├─ javanio/              → Non-blocking NIO version
│   ├─ NioCoffeeServer.java
│   └─ NioCoffeeCustomer.java
│
├─ netty/                → Netty version (recommended)
│   ├─ NettyCoffeeServer.java
│   └─ NettyCoffeeCustomer.java
│
├─ lib/                  → Netty JAR (if plain Java project)
│
└─ README.md
```

## ▶️ Running the Project

### **1. BIO Version**
Start the server:
```
java javaio.CoffeeShopServer
```

Start the client:
```
java javaio.CoffeeCustomer
```

### **2. NIO Version**
Start the server:
```
java javanio.NioCoffeeServer
```

Start the client:
```
java javanio.NioCoffeeCustomer
```

### **3. Netty Version (recommended)**
Start the server:
```
java netty.NettyCoffeeServer
```

Start the client:
```
java netty.NettyCoffeeCustomer
```

## 📦 Dependencies

If using plain Java project, place this JAR into `/lib` and add to Build Path:

```
netty-all-4.1.109.Final.jar
```

## 📘 Learning Goals

This project helps you understand:

- How sockets work in Java  
- Blocking vs non-blocking I/O  
- Selectors and events  
- How Netty simplifies networking  
- How real servers handle connections

Perfect for beginners learning networking step-by-step.

## ⭐ License
Free to use and modify.
