import music.MusicHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.util.Random;

public class Main extends ListenerAdapter {

    private static final String DiscordToken = "";
    private static JDA jda;
    private MusicHandler musicHandler = new MusicHandler();

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

        String[] message = event.getMessage().getContentDisplay().substring(this.commandBeginning.length()).split(" ");

        if(!event.getMessage().getContentDisplay().startsWith(commandBeginning)){
            return;
        }

        messageResponse(event, message);
    }

    public void messageResponse(MessageReceivedEvent event, String[] message) {

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(new Color(66, 135, 245));

        boolean handled = handleMusicCommands(event, message);    // Voor alle muziek commands
        if (handled) {
            return;
        }

        if(message[0].equals("commands")) {
            event.getChannel().sendMessage("```" +
                    "!ping              |      Bot answers with Pong! \n" +
                    "!is joël gay?      |      Bot answers the question truthfully.\n" +
                    "!sub subreddit     |      Get a random image from your selected subreddit(s),\n" +
                    "                          Add a + to let it choose between more at once.\n" +
                    "!music play url    |      Let the bot play music from the url you choose.\n" +
                    "!music skip        |      Skips number currently playing.\n" +
                    "!music leave       |      Bot leaves the voice channel.\n" +
                    "!commands          |      See all commands." +
                    "```").queue();
        }

        else if(message[0].equals("ping")) {
            eb.setTitle("Pong!");
            event.getChannel().sendMessage(eb.build()).queue();
        }

        else if(message[0].toLowerCase().equals("is") && message[message.length - 1].toLowerCase().contains("gay")) {
            Random rand = new Random();
            int random = rand.nextInt(2);
            StringBuilder name = new StringBuilder();

            for (int i = 1; i < message.length - 1; i++){
                name.append(message[i]);
            }

            if (name.equals("joël")){
                eb.setTitle("Yes, " + name + " is 100% gay.");
            }else if (random == 0) {
                eb.setTitle("Yes, " + name + " is 100% gay.");
            }else if (random == 1){
                eb.setTitle("No, " + name + " is not gay.");
            }

            event.getChannel().sendMessage(eb.build()).queue();
        }

        else if(message[0].equals("sub")) {
            EmbedBuilder sub = new APIHandler().SubRedditImageGenerator(message[1], event);
            event.getChannel().sendMessage(sub.build()).queue();
        }

        else{
            eb.setTitle("Didn't recognise this command, please use !commands to see which commands I have.");
            event.getChannel().sendMessage(eb.build()).queue();
        }
    }

    public boolean handleMusicCommands(MessageReceivedEvent event, String[] message) {
        if (message[0].equals("music")) {   // Voor muziek commands
            if (message[1].equals("play")) {
                this.musicHandler.loadAndPlay(event.getTextChannel(), message[2]);
            } else if (message[1].equals("skip")) {
                this.musicHandler.skipTrack(event.getTextChannel());
            } else if (message[1].equals("leave")) {
                this.musicHandler.emptyQeue(event.getTextChannel());
                this.musicHandler.skip(event.getTextChannel());
                this.musicHandler.leaveChannel(event.getTextChannel());
            }
            return true;
        }
        return false;
    }
}
