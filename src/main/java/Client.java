
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static org.mortbay.jetty.HttpHeaders.HOST;


public class Client {
    public static void main(String[] args) {

        List<PageEntry> list = new ArrayList<>();

        try (Socket socket = new Socket(HOST, ServerConfig.PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            //общение с клиентом в консоли
            out.println("менеджер");
            String json = in.readLine();
            System.out.println(json);

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }
}

