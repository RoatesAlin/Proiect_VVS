import org.junit.*;
import java.net.*;
import java.io.*;
import java.nio.file.*;


public class WebServerTests {

    public int webPort = 10008;
    public String webRoot = "C:/Users/Alin/Desktop/HTML";
    public String webMaintenance = "C:/Users/Alin/Desktop/HTML";
    public String webStatus  = "Stopped";

    @Test
    public void changeWebPort()
    {
        WebServer webserver = new WebServer(webPort, webRoot, webMaintenance, webStatus);
        webserver.changeWebPort(4004);
        Assert.assertEquals(4004, webserver.getWebPort());
    }

    @Test
    public void changeWebRoot_success()
    {
        WebServer webserver = new WebServer(webPort, webRoot, webMaintenance, webStatus);
        webserver.changeWebRoot("C:/Users/Alin/Desktop/HTML");
        Assert.assertEquals("C:/Users/Alin/Desktop/HTML", webserver.getWebRoot());
    }

    @Test
    public void changeWebRoot_failure()
    {
        WebServer webserver = new WebServer(webPort, webRoot, webMaintenance, webStatus);
        webserver.changeWebRoot("C:/Users/Alin/Desktop/Random");
        Assert.assertNotEquals("C:/Users/Alin/Desktop/Random", webserver.getWebRoot());
    }


    @Test(expected = ComparisonFailure.class)
    public void changeWebMaintenance_success()
    {
        WebServer webserver = new WebServer(webPort, webRoot, webMaintenance, webStatus);
        webserver.changeWebMaintenance("C:/Users/Alin/Desktop/Random");
        Assert.assertEquals("C:/Users/Alin/Desktop/Random", webserver.getWebMaintenance());
    }

    @Test
    public void changeWebMaintenance_failure()
    {
        WebServer webserver = new WebServer(webPort, webRoot, webMaintenance, webStatus);
        webserver.changeWebMaintenance("C:/Users/Alin/Desktop/Random");
        Assert.assertNotEquals("C:/Users/Alin/Desktop/Random", webserver.getWebMaintenance());
    }

    @Test
    public void changeWebStatus()
    {
        WebServer webserver = new WebServer(webPort, webRoot, webMaintenance, webStatus);
        webserver.changeWebStatus("Maintenance");
        Assert.assertEquals("Maintenance", webserver.getWebStatus());

    }

    @Test
    public void getFilePath_full_path()
    {
        Path path = Paths.get("C:/Users/Alin/Desktop","/HTML");
        Assert.assertEquals("C:\\Users\\Alin\\Desktop\\HTML", path.toString());
    }

    @Test
    public void getFilePath_empty_path()
    {
        Path path = Paths.get("","");
        Assert.assertEquals("", path.toString());

    }

    @Test
    public void getFilePath_first_empty_string()
    {
        Path path = Paths.get("","C:/Users/Alin/Desktop");
        Assert.assertEquals("C:\\Users\\Alin\\Desktop", path.toString());

    }

    @Test
    public void getFilePath_second_empty_string()
    {
        Path path = Paths.get("C:/Users/Alin/Desktop","");
        Assert.assertEquals("C:\\Users\\Alin\\Desktop", path.toString());

    }

    @Test (expected = NullPointerException.class)
    public void getFilePath_null_path()
    {
        Path path = Paths.get(null,null);
    }

    @Test(expected = InvalidPathException.class)
    public void getFilePath_null_path_1()
    {
        Path path = Paths.get(null,"C:/Users/Alin/Desktop");
        Assert.assertEquals("\\Users\\Alin\\Desktop", path.toString());
    }

    @Test(expected = NullPointerException.class)
    public void getFilePath_null_path_2()
    {
        Path path = Paths.get("C:/Users/Alin/Desktop",null);
        Assert.assertEquals("C:\\Users\\Alin\\Desktop", path.toString());
    }

    @Test
    public void getFilePath_one_String()
    {
        Path path = Paths.get("C:/Users/Alin/Desktop");
        Assert.assertEquals("C:\\Users\\Alin\\Desktop", path.toString());
    }

    @Test
    public void getFilePath_html_file()
    {
        WebServer webserver = new WebServer(webPort, webRoot, webMaintenance, webStatus);
        Path path = webserver.getFilePath("/");
        Assert.assertEquals("C:\\Users\\Alin\\Desktop\\HTML\\Root.html",path.toString());
    }

    @Test
    public void getFilePath_incorrect_path()
    {
        WebServer webserver = new WebServer(webPort, webRoot, webMaintenance, webStatus);
        Path path = webserver.getFilePath("Random/Folder");
        Assert.assertEquals("Random\\Folder",path.toString());
    }

    @Test
    public void getFilePath_path_exists()
    {
        Path path = Paths.get(webRoot);
        Assert.assertTrue(Files.exists(path));
    }

    @Test
    public void getFilePath_path_not_exists() {
        Path path = Paths.get("Random");
        Assert.assertFalse(Files.exists(path));
    }


    @Test
    public void sendResponse_everything_ok() throws IOException {
        WebServer webserver = new WebServer(webPort, webRoot, webMaintenance, webStatus);
        ServerSocket serverSocket = new ServerSocket(10008);
        Socket clientSocket = serverSocket.accept();
        Path correct_path = Paths.get("C:/Users/Alin/Desktop/HTML/Root.html");
        OutputStream out = clientSocket.getOutputStream();
        webserver.sendResponse(out,"200 OK", "text/html",Files.readAllBytes(correct_path));
        out.close();

    }

    @Test(expected =  IOException.class)
    public void sendResponse_disconnect_before_response() throws IOException
    {
        WebServer webserver = new WebServer(webPort, webRoot, webMaintenance, webStatus);
        ServerSocket serverSocket = new ServerSocket(10008);
        Socket clientSocket = serverSocket.accept();
        Path correct_path = Paths.get("C:/Users/Alin/Desktop/HTML/Root.html");
        OutputStream out = clientSocket.getOutputStream();
        out.close();
        webserver.sendResponse(out,"200 OK", "text/html",Files.readAllBytes(correct_path));
    }

    @Test
    public void handleRequest_null_client()
    {
        WebServer webserver = new WebServer(webPort, webRoot, webMaintenance, webStatus);
        webserver.handleRequest(null);
    }
    

    @Test
    public void handleRequest_html_page() throws IOException
    {
        WebServer.requestString = "";
        WebServer webserver = new WebServer(webPort, webRoot, webMaintenance, webStatus);
        ServerSocket serverSocket = new ServerSocket(10008);
        Socket clientSocket = serverSocket.accept();
        webserver.handleRequest(clientSocket);
        Assert.assertEquals(" C:\\Users\\Alin\\Desktop\\HTML\\Root.html",WebServer.requestString);
        WebServer.requestString ="";
    }

    @Test
    public void handleRequest_maintenance_html_page() throws IOException
    {

        WebServer.requestString = "";
        WebServer webserver = new WebServer(webPort, webRoot, webMaintenance, "Maintenance");
        ServerSocket serverSocket = new ServerSocket(10008);
        Socket clientSocket = serverSocket.accept();
        webserver.handleRequest(clientSocket);
        Assert.assertEquals(" C:\\Users\\Alin\\Desktop\\HTML\\Maintenance.html",WebServer.requestString);
        WebServer.requestString ="";
    }

    @Test
    public void handleRequest_stopped_html_page() throws IOException
    {
        WebServer.requestString = "";
        WebServer webserver = new WebServer(webPort, webRoot, webMaintenance, "Stopped");
        ServerSocket serverSocket = new ServerSocket(10008);
        Socket clientSocket = serverSocket.accept();
        webserver.handleRequest(clientSocket);
        Assert.assertEquals(" C:\\Users\\Alin\\Desktop\\HTML\\Root.html",WebServer.requestString);
        WebServer.requestString ="";
    }

    @Test
    public void handleRequest_disconnect_before_handler() throws IOException
    {
        WebServer webserver = new WebServer(webPort, webRoot, webMaintenance, webStatus);
        ServerSocket serverSocket = new ServerSocket(10008);
        Socket clientSocket = serverSocket.accept();
        clientSocket.close();
        webserver.handleRequest(clientSocket);
    }


    @Test
    public void connectToSocket_success()
    {
        WebServer.requestString ="";
        WebServer webserver = new WebServer(webPort, webRoot, webMaintenance, webStatus);
        webserver.connectToSocket();
        Assert.assertEquals("C:\\Users\\Alin\\Desktop\\HTML\\Root.html",WebServer.requestString);
        WebServer.requestString="";
    }


}


