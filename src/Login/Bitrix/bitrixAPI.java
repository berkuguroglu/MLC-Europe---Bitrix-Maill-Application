package Login.Bitrix;


import com.google.gson.*;
import javafx.concurrent.Task;
import restRequest.request;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

public class bitrixAPI extends Task<ArrayList<String>> {

    private final String AUTH = "https://mlcomponents.bitrix24.com/rest/12/b6pt3a9mlu6prvpl";
    private String firstMethod = "/crm.company.list";
    private String secondMethod ="/crm.company.get";
    private String thirdMethod ="/user.get";
    private ArrayList<request> requestObjects;

    public bitrixAPI()
    {
       this.requestObjects = new ArrayList<>();
    }

    @Override
    protected ArrayList<String> call() throws Exception {

        if(!this.isCancelled()) {
            JsonObject obj = (JsonObject) new JsonParser().parse(firstRequest());
            JsonArray array = (JsonArray) obj.get("result");
            for(JsonElement check : array)
            {
                System.out.println(check.toString() + "\n");

            }
            this.cancel();
        }
        return null;
    }
    private String firstRequest()
    {
        try {
            requestObjects.add(new request(this.AUTH + this.firstMethod));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return requestObjects.get(0).getResult().toString();
    }
}
