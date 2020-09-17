import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONObject;

import java.awt.*;

public class APIHandler {

    public APIHandler() {
    }

    public EmbedBuilder SubRedditImageGenerator(String subReddit, MessageReceivedEvent event){

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(new Color(66, 135, 245));

        try {
            JSONObject jsonObject = getAPI("http://h2892166.stratoserver.net/api/meme/?subs=" + subReddit);
            JSONObject jsonData = jsonObject.getJSONObject("article");
            String title = jsonData.getString("title");
            String imageUrl = jsonData.getString("image");
            String upvotes = String.valueOf(jsonData.getInt("upvotes"));
            boolean nsfw = jsonData.getBoolean("nsfw");

            if(nsfw && !event.getTextChannel().isNSFW()){
                eb.setTitle("Can't display NSFW content in a non NSFW channel.");
                return eb;
            }

            eb.setTitle(title);
            eb.setImage(imageUrl);
            eb.setFooter("Upvotes: " + upvotes);

            return eb;

        } catch(NullPointerException e){
            eb.setTitle("This sub probably doesn't exist, or I'm just a moron.");

            return eb;
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
