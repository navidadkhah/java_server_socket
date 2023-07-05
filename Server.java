import java.net.Socket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ClassNotFoundException;
import java.net.ServerSocket;
import java.lang.Math;

public class Server {
    static ServerSocket server;
    static int port = 3000;
    static boolean flag;

    static int calculate(String message) {
        String[] witch = message.split(" ");
        int result = 0;
        switch (witch[0]) {
            case "Add" -> result = Integer.parseInt(witch[1]) + Integer.parseInt(witch[2]);
            case "Subtract" -> result = Integer.parseInt(witch[1]) - Integer.parseInt(witch[2]);
            case "Divide" -> result = Integer.parseInt(witch[1]) / Integer.parseInt(witch[2]);
            case "Multiply" -> result = Integer.parseInt(witch[1]) * Integer.parseInt(witch[2]);
            case "Sin" -> result = (int) Math.sin(Integer.parseInt(witch[1]));
            case "Cos" -> result = (int) Math.cos(Integer.parseInt(witch[1]));
            case "Tan" -> result = (int) Math.tan(Integer.parseInt(witch[1]));
            case "Cot" -> {
                int temp = (int) Math.tan(Integer.parseInt(witch[1]));
                result = 1 / temp;
            }
            default -> flag = false;
        }
        return result;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        server = new ServerSocket(port);

        while (true) {
            flag = true;

            long start = System.currentTimeMillis();
            System.out.println("Waiting for the client request");

            Socket socket = server.accept();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            String message = (String) ois.readObject();
            System.out.println("Message Received: " + message);

            int result = calculate(message);
            long finish = System.currentTimeMillis();

            long time = finish - start;
            System.out.println(time);

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            if (!flag) {
                oos.writeObject("Operation not found 404!");
            } else {
                oos.writeObject("Calculation time: " + time + " result: " + result);
            }


            //close resources
            ois.close();
            oos.close();
            socket.close();
            //terminate the server if client sends exit request
            if (message.equals("exit")) break;
        }
        System.out.println("Shutting down Socket server!!");
        //close the ServerSocket object
        server.close();
    }
}
