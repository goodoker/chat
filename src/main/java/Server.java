import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
    public static ArrayList<ServerService> connectionList = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        Logger logger = Logger.getInstance();

        Scanner set = new Scanner(Paths.get("settings.txt"));
        int port = Integer.parseInt(set.nextLine());
        set.close();

        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Сервер запущен");
        logger.log("Сервер запущен");
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                try {
                    connectionList.add(new ServerService(socket));
                } catch (IOException ex) {
                    socket.close();
                }
            }
        } finally {
            serverSocket.close();
        }
    }
}