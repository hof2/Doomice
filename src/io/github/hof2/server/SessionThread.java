package io.github.hof2.server;

import io.github.hof2.enums.Communications;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketException;

public class SessionThread extends Thread {

    private final Socket socket;
    private final Server server;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public SessionThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            while (!isInterrupted()) {
                Object message = inputStream.readObject();
                if (message.equals(Communications.STOP)) {
                    interrupt();
                    server.closeSession(this);
                } else {
                    Serializable response = server.performResponse((Serializable) message, this);
                    if (!response.equals(Communications.DONT_ANSWER)) {
                        send(response);
                    }
                }
            }
        } catch (SocketException | EOFException ex) {

        } catch (IOException | ClassNotFoundException ex) {
            server.onException(ex);
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                server.onException(ex);
            }
        }
    }

    public void send(Serializable object) {
        try {
            outputStream.writeUnshared(object);
        } catch (IOException ex) {
            server.closeSession(this);
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void close() throws IOException {
        send(Communications.STOP);
        socket.close();
    }

}
