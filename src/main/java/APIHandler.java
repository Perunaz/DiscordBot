import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONObject;

public class APIHandler {

    public APIHandler() {
    }

    public String SubRedditImageGenerator(String subReddit){

        try {
            JSONObject jsonObject = getAPI("http://h2892166.stratoserver.net/api/meme/?subs=" + subReddit);
            String imageUrl = jsonObject.getString("result");

            return imageUrl;
        } catch(Exception e){
            return "Random sub image not found.";
        }
    }

    private JSONObject getAPI(String url) {
        try {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response responses = null;

            responses = client.newCall(request).execute();

            String jsonData = null;
            jsonData = responses.body().string();

            JSONObject jsonObject = new JSONObject(jsonData);
            System.out.println(jsonObject);
            return jsonObject;

        } catch (Exception e) {
            return null;
        }
    }
}
