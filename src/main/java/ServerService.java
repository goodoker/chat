import java.io.*;
import java.net.Socket;

public class ServerService extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ServerService(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        start();
    }

    @Override
    public void run() {
        Logger logger = Logger.getInstance();
        String msg;
        try {
            msg = in.readLine();
            out.println("Добро пожаловать в чат " + msg);
            System.out.println("Подключен пользователь " + msg);
            logger.log("Сервер: подключен пользователь " + msg);
            try {
                while (true) {
                    msg = in.readLine();
                    if (msg.equals("exit")) {
                        this.stopService();
                        break;
                    }
                    logger.log("Сервер: сообщение от пользователя " + msg);
                    System.out.println("Сообщение от пользователя " + msg);
                    for (ServerService serverService : Server.connectionList) {
                        if (!serverService.equals(this)) {
                            serverService.send(msg);
                        }
                    }
                }
            } catch (NullPointerException ignored) {
            }
        } catch (IOException ex) {
            this.stopService();
        }
    }

    private void send(String msg) {
        out.println(msg);
    }

    private void stopService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                for (ServerService serverService : Server.connectionList) {
                    if (serverService.equals(this)) {
                        serverService.interrupt();
                        Server.connectionList.remove(this);
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}