import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class SecondClient {
    public static void main(String[] args) throws IOException {
        Scanner set = new Scanner(Paths.get("settings.txt"));
        int port = Integer.parseInt(set.nextLine());
        String hostname = set.nextLine();
        set.close();

        new ClientService(hostname, port);
    }
}