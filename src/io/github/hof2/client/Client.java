package io.github.hof2.client;

import io.github.hof2.enums.Communications;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public abstract class Client {

    private final Socket socket;
    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;
    private ClientThread thread;
    private boolean closed = true;

    public Client(Socket socket) throws IOException {
        this.socket = socket;
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
    }

    public final Object performRequest(Serializable object) throws IOException, ClassNotFoundException, InterruptedException {
        send(object);
        return getNextObject();
    }

    public final synchronized Object getNextObject() throws InterruptedException {
        thread.requestObject();
        wait();
        return thread.getRequestedObject();
    }

    public final void send(Serializable object) throws IOException {
        outputStream.writeUnshared(object);
    }

    public final Socket getSocket() {
        return socket;
    }

    public final void start() throws IOException {
        if (thread == null || !thread.isAlive()) {
            thread = new ClientThread(this);
            thread.start();
            closed = false;
        }
    }

    public final void stop() throws IOException {
        if (thread != null && thread.isAlive()) {
            send(Communications.STOP);
            close();
        }
    }

    public final void close() throws IOException {
        onClose();
        thread.interrupt();
        socket.close();
        closed = true;
    }

    public final ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    public final ObjectInputStream getInputStream() {
        return inputStream;
    }

    public abstract void onException(Exception ex);

    public void onClose() {
    }

    public final boolean isClosed() {
        return closed;
    }

    public void receive(Object object) {
    }

}
