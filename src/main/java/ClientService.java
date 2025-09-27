import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientService {

    private String nickname;
    private Socket socket;
    private Scanner scanner;
    private BufferedReader in;
    private PrintWriter out;
    private String host;
    private int port;

    public ClientService(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            this.socket = new Socket(host, port);
            scanner = new Scanner(System.in);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            this.setNickname();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        new SendMessage().start();
        new ReceiveMessage().start();
    }

    private void setNickname() throws IOException {
        Logger logger = Logger.getInstance();
        System.out.print("Введите ваше имя: ");
        nickname = scanner.nextLine();
        logger.log("Пользователь " + nickname + " вошел в чат");
        out.println(nickname);
    }

    private void stopService() {
        Logger logger = Logger.getInstance();

        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                logger.log("Пользователь " + nickname + " вышел из чата");
            }
        } catch (IOException ignored) {
        }
    }

    public class SendMessage extends Thread {
        Logger logger = Logger.getInstance();

        @Override
        public void run() {
            String msg;
            try {
                while (true) {
                    msg = scanner.nextLine();
                    if (msg.equals("exit")) {
                        ClientService.this.stopService();
                        break;
                    } else {
                        logger.log("Пользователь " + nickname + " написал: " + msg);
                        out.println(nickname + ": " + msg);
                    }
                }
            } catch (IOException ignored) {
            }
        }
    }

    private class ReceiveMessage extends Thread {
        Logger logger = Logger.getInstance();

        @Override
        public void run() {
            String msg;
            try {
                while (true) {
                    msg = in.readLine();
                    if (msg.equals("exit")) {
                        ClientService.this.stopService();
                        break;
                    }
                    logger.log("Пользователь " + nickname + " получил сообщение от: " + msg);
                    System.out.println(msg);
                }
            } catch (IOException ex) {
                ClientService.this.stopService();
            }
        }
    }
}