import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class Main {
    public static void main(String[] args) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(ServerConfig.PORT)) { // стартуем сервер один(!) раз
            System.out.println("Сервер запущен!");
            BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
            while (true) { // в цикле(!) принимаем подключения
                try (
                        Socket socket = serverSocket.accept();
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    String word = in.readLine();//принимаем запрос
                    var gson = new GsonBuilder().setPrettyPrinting().create();
                    var request = engine.search("бизнес");
                    var response = gson.toJson(request);
                    out.write(response); //  // отвечать на запросы /{word} -> возвращённое значение метода search(word) в JSON-формате

                }
            }
        }
    }
}