import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class WebServer extends Thread {

    public static String requestString = "";

    private int webPort;

    private String webRoot;

    private String webMaintenance;

    private String webStatus;

    public WebServer(int webPort, String webRoot, String webMaintenance, String webStatus) {
        this.webPort = webPort;
        this.webRoot = webRoot;
        this.webMaintenance = webMaintenance;
        this.webStatus = webStatus;
    }
    
    public int getWebPort() {
        return webPort;
    }

    public String getWebRoot() {
        return webRoot;
    }

    public String getWebMaintenance() {
        return webMaintenance;
    }

    public String getWebStatus() {
        return webStatus;
    }

    public void setWebPort(int webPort) {
        this.webPort = webPort;
    }

    public void setWebRoot(String webRoot) {
        this.webRoot = webRoot;
    }

    public void setWebMaintenance(String webMaintenance) {
        this.webMaintenance = webMaintenance;
    }

    public void setWebStatus(String webStatus) {
        this.webStatus = webStatus;
    }

    public void changeWebPort(int new_webPort) {
        webPort = new_webPort;
    }

    public void changeWebRoot(String new_webRoot) {
        if (Files.exists(Paths.get(new_webRoot, "/Root.html"))) {
            webRoot = new_webRoot;
        } else {
            System.out.println("Root directory requirements not met");
        }
    }

    public void changeWebMaintenance(String new_webMaintenance) {
        if (Files.exists(Paths.get(new_webMaintenance, "/Maintenance.html"))) {
            webMaintenance = new_webMaintenance;
        } else {
            System.out.println("Maintenance directory requirements not met");
        }
    }

    public void changeWebStatus(String new_webStatus) {
        webStatus = new_webStatus;
    }

    public Path getFilePath(String path) {
        if ("/".equals(path)) {
            return Paths.get(webRoot, "/Root.html");
        } else {
            return Paths.get(path);
        }
    }

    public void sendResponse(OutputStream out, String status, String contentType, byte[] content) throws IOException {
        out.write(("HTTP/1.1 \r\n" + status).getBytes());
        out.write(("ContentType: " + contentType + "\r\n").getBytes());
        out.write("\r\n".getBytes());
        out.write(content);
        out.write("\r\n\r\n".getBytes());
    }

    public void connectToSocket() {
        try {
            ServerSocket serverSocket = new ServerSocket(webPort);
            Socket clientSocket = serverSocket.accept();
            handleRequest(clientSocket);
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            System.err.println("Could not listen on port:" + webPort);
        }
    }

    public void handleRequest(Socket clientSocket) {

        try {
            try {
                OutputStream out = clientSocket.getOutputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String inputLine;
                ArrayList<String> inputLines = new ArrayList<>();
                while ((inputLine = in.readLine()) != null) {
                    inputLines.add(inputLine);

                    if (inputLine.trim().equals("")) {
                        break;
                    }
                }

                if (inputLines.size() != 0) {
                    String pathString = inputLines.get(0).split(" ")[1];
                    Path filePath = getFilePath(pathString);
                    String contentType = Files.probeContentType(filePath);

                    requestString = requestString + " " + filePath.toString();
                    if (webStatus.equals("Running")) {
                        if (Files.exists(filePath)) {
                            sendResponse(out, "200 OK", contentType, Files.readAllBytes(filePath));
                        } else {
                            sendResponse(out, "404 Not Found", contentType, Files.readAllBytes(Paths.get(webRoot, "/NotFound.html")));
                        }
                    } else if (webStatus.equals("Maintenance")) {
                        if (contentType.contains("html")) {
                            sendResponse(out, "503 Service Unavailable", contentType, Files.readAllBytes(Paths.get(webMaintenance, "/Maintenance.html")));
                        } else {
                            sendResponse(out, "200 OK", contentType, Files.readAllBytes(filePath));
                        }
                    } else {
                        if (contentType.contains("html")) {
                            sendResponse(out, "503 Service Unavailable", contentType, Files.readAllBytes(Paths.get(webRoot, "/Down.html")));
                        } else {
                            sendResponse(out, "200 OK", contentType, Files.readAllBytes(filePath));
                        }
                    }
                }
                out.close();
                in.close();
            } catch (NullPointerException e) {
                System.err.println("Null client object was given");
            }
        } catch (IOException e) {
            System.err.println("Problem with Communication Server");
        }
    }
}


class Main {
    public static void main(String[] args) {
        System.out.println(" \n  Connect or refresh page \n");
        WebServer webserver = new WebServer(10008, "C:/Users/Alin/Desktop/HTML", "C:/Users/Alin/Desktop/HTML", "Stopped");
        webserver.changeWebStatus("Running");
        while (true) {
            webserver.connectToSocket();
        }
    }
}
