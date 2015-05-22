package io.github.hof2.client;

import io.github.hof2.enums.Communications;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;

public class ClientThread extends Thread {

    private final Client client;
    private final ObjectInputStream inputStream;
    private boolean request;
    private Object requestedObject;

    public ClientThread(Client client) throws IOException {
        this.client = client;
        inputStream = client.getInputStream();
    }

    @Override
    public void run() {

        try {
            while (!interrupted()) {
                Object message = inputStream.readObject();
                if (message.equals(Communications.STOP)) {
                    client.close();
                } else {
                    if (request) {
                        synchronized (client) {
                            requestedObject = message;
                            request = false;
                            client.notifyAll();
                        }
                    } else {
                        client.receive(message);
                    }
                }
            }
        } catch (SocketException ex) {

        } catch (IOException | ClassNotFoundException ex) {
            client.onException(ex);
        } finally {
            try {
                client.getSocket().close();
            } catch (IOException ex) {
                client.onException(ex);
            }
        }
    }

    public void requestObject() {
        request = true;
    }

    public Object getRequestedObject() {
        return requestedObject;
    }

}
