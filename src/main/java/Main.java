import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

public class Main extends ListenerAdapter {

    private static final String DiscordToken = "NzU1MDc3NDM1Mjg4MDYwMDg1.X1-CqQ.0CmWdzx7MCbrUuq9fAGRJiSd8nE";
    private static JDA jda;

    private final String commandBeginning = "!";

    public static void main(String[] args) throws LoginException {
        jda = JDABuilder.createDefault(DiscordToken).build();
        Main main = new Main();
        jda.addEventListener(main);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) {
            return;
        }

        System.out.println("We received a message from " +
                event.getAuthor().getName() + ": " +
                event.getMessage().getContentDisplay()
        );

        String message = event.getMessage().getContentDisplay().substring(this.commandBeginning.length());

        messageResponse(event, message);
    }

    public void messageResponse(MessageReceivedEvent event, String message) {

        if(message.toLowerCase().equals("ping")) {
            event.getChannel().sendMessage("Pong!").queue();
        }

        if(message.toLowerCase().equals("meme")) {
            String meme = new APIHandler().MemeGenerator();
            event.getChannel().sendMessage(meme).queue();
        }
    }
}
