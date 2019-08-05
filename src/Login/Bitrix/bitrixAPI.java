package Login.Bitrix;


import com.google.gson.*;
import javafx.concurrent.Task;
import restRequest.request;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

public class bitrixAPI extends Task<ArrayList<String>> {


    private ArrayList<JsonArray> company_list;
    private int iteration = 50;
    private final String AUTH = "https://mlcomponents.bitrix24.com/rest/12/b6pt3a9mlu6prvpl";
    private String firstMethod = "/crm.company.list?select[]=ID&start=";
    private String secondMethod ="/crm.company.get?id=";
    private String thirdMethod ="/user.get";

    public bitrixAPI()
    {
       this.company_list = new ArrayList<>();
    }

    @Override
    protected ArrayList<String> call() throws Exception {

        if(!this.isCancelled()) {

            for(int i = 0; i<100; i++)
            {
                JsonObject obj = (JsonObject) new JsonParser().parse(firstRequest(iteration));
                JsonArray array = (JsonArray) obj.get("result");
                for(JsonElement el : array)
                    System.out.println(el);
                this.company_list.add(array);
                this.iteration += 50;
            }
            System.out.println(this.company_list.size());
            for(int i = 0; i<this.company_list.size(); i++)
            {
                for(JsonElement element : this.company_list.get(i))
                {

                    System.out.println(secondRequest(element.getAsInt()));
                }
            }
            this.cancel();
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
