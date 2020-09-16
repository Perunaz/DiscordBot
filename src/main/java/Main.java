import music.JoinCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

public class Main extends ListenerAdapter {

    private static final String DiscordToken = "NzU1MDc3NDM1Mjg4MDYwMDg1.X1-CqQ.mtlldzmFRR14uT67YxxWk09mvKM";
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

        if(!event.getMessage().getContentDisplay().startsWith(commandBeginning)){
            return;
        }

        messageResponse(event, message);
    }

    public void messageResponse(MessageReceivedEvent event, String message) {

        if(message.toLowerCase().equals("commands")) {
            event.getChannel().sendMessage("```" +
                    "!ping              |      Bot answers with Pong! \n" +
                    "!sub subreddit     |      Get a random image from your selected subreddit(s),\n" +
                    "                          Add a + to let it choose between more at once.\n" +
                    "!commands          |      See all commands." +
                    "```").queue();
        }

        else if(message.toLowerCase().equals("ping")) {
            event.getChannel().sendMessage("Pong!").queue();
        }

        else if(message.toLowerCase().contains("sub ")) {
            EmbedBuilder sub = new APIHandler().SubRedditImageGenerator(message.substring(4));
            event.getChannel().sendMessage(sub.build()).queue();
        }

        else if(message.toLowerCase().contains("music")){
            JoinCommand joinCommand = new JoinCommand();
            joinCommand.joining(event);
        }

        else{
            event.getChannel().sendMessage("Didn't recognise this command, please use **!commands** to see which commands I have.").queue();
        }
    }
}
