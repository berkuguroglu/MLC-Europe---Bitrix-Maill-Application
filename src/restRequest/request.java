package restRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URL;

public class request
{
    private final String USER_AGENT = "Mozilla/5.0";
    private String URL;
    private URL REQUEST_ADDRESS;
    private HttpURLConnection connection;
    private StringBuffer result;
    public request() throws IOException
    {
        this.REQUEST_ADDRESS = new URL(this.URL);
        result = new StringBuffer();
        this.sendGETrequest();


    }
    public boolean sendGETrequest() throws IOException {
        connection = (HttpURLConnection) this.REQUEST_ADDRESS.openConnection();
        this.connection.setRequestMethod("GET");
        this.connection.setRequestProperty("USER-AGENT", USER_AGENT);
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while((line = in.readLine()) != null)
            this.result.append(line);
        in.close();
        if(connection.getResponseCode() != 400)
        return true;
        else return false;

    }


}