package Login.Bitrix;


import Login.Main;
import com.google.gson.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import restRequest.request;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

public class bitrixAPI extends Task<ArrayList<String>> {


    private ArrayList<JsonArray> company_list;
    private ArrayList<JsonObject> company_details;
    private int limit = 20; // 1000 companies
    private int speed = 300;
    private Main stageHolder;
    private int iteration = 0;
    private final String AUTH = "https://mlcomponents.bitrix24.com/rest/12/b6pt3a9mlu6prvpl";
    private String firstMethod = "/crm.company.list?select[]=ID&start=";
    private String secondMethod ="/crm.company.get?select[]=ASSIGNED_BY_ID&id=";
    private String thirdMethod ="/user.get";

    public bitrixAPI(Main stageHolder)
    {
       this.company_list = new ArrayList<>();
       this.company_details = new ArrayList<>();
       this.stageHolder = stageHolder;
    }

    @Override
    protected ArrayList<String> call() throws Exception {

        if(!this.isCancelled() && !this.isDone()) {

            stageHolder.progressBarUpdate(0, "Checking company ID's ..");

            for(int i = 0; i<limit; i++)
            {
                JsonObject obj = (JsonObject) new JsonParser().parse(firstRequest(iteration));
                JsonArray array = (JsonArray) obj.get("result");
                this.company_list.add(array);
                this.iteration += 50;
            }
            for(int i = 0; i<this.company_list.size(); i++)
            {

                for(JsonElement element : this.company_list.get(i))
                {
                    String result = secondRequest(element.getAsJsonObject().get("ID").getAsInt());
                    JsonObject object = (JsonObject) new JsonParser().parse(result);
                    this.company_details.add(object.get("result").getAsJsonObject());

                    Thread.sleep(speed);
                }
                stageHolder.progressBarUpdate(Double.parseDouble(String.valueOf(i+1))/Double.parseDouble(String.valueOf(this.company_list.size())), "Fetching companies information ..");

            }

            this.succeeded();
        }
        return null;
    }
    private String firstRequest(int iteration)
    {
        request requestObject = null;
        try {
            requestObject = new request(this.AUTH + firstMethod + iteration);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return requestObject.getResult();
    }
    private String secondRequest(int companyId)
    {
        request requestObject = null;
        try {
            requestObject = new request(this.AUTH + secondMethod + companyId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return requestObject.getResult();
    }
}
