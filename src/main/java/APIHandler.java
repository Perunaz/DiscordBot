import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONObject;

public class APIHandler {

    private Random random;

    public APIHandler() {
        this.random = new Random();
    }

    public String MemeGenerator(){

        try {
            int memeRandom = this.random.nextInt(228);

            JSONObject jsonObject = getAPI("http://alpha-meme-maker.herokuapp.com/memes/" + memeRandom + "/");
            JSONObject jsonData = jsonObject.getJSONObject("data");
            String imageUrl = jsonData.getString("image");

            if(!imageUrl.contains(".jpg")){
                MemeGenerator();
            }

            return imageUrl;
        } catch(Exception e){
            return "Random meme not found";
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
