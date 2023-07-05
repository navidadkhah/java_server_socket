import java.net.Socket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ClassNotFoundException;
import java.net.ServerSocket;
import java.lang.Math;

public class Server {
    // Set the global variables
    private static ServerSocket server;
    private static int port = 3000;
    private static boolean is_404;

    // This function is used for calculating the operations that we get from the client
    static int calculate(String message) {
        // Split the message
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
            default -> is_404 = false;
        }
        // return the result
        return result;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // Setting the port of server
        server = new ServerSocket(port);
        while (true) {
            is_404 = true;
            // Get the early time
            long start = System.currentTimeMillis();
            System.out.println("Waiting for the client request");
            // If the message has been sent, it has to go to the next line
            Socket socket = server.accept();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            String message = (String) ois.readObject();
            System.out.println("Message Received: " + message);
            // Call the calculate function
            int result = calculate(message);
            // Get the secondary time
            long finish = System.currentTimeMillis();
            // Calculating the time that the server is working
            long time = finish - start;
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            // If operation not found, print "Operation not found 404!" else goto else:)
            if (!is_404) {
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
