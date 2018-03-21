/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.nailuj.mc.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.HttpHeaders.ACCEPT_ENCODING;
import static org.apache.http.HttpHeaders.USER_AGENT;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 *
 * @author Julian Blazek
 */
public class JsonCommand extends CommandBase {

    @Override
    public int getRequiredPermissionLevel() {
        return 1;
    }

    @Override
    public String getName() {
        return "json";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/json [URL]";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 1 && args[0].length() > 0) {
            try {
                BufferedReader rd;
                URI uri;
                try {
                    URL url = new URL(args[0]);
                    uri = url.toURI();
                } catch (MalformedURLException | URISyntaxException ex) {
                    throw new SyntaxErrorException("Supplied argument is not a valid URI!", new Object[]{args[0]});
                }
                CloseableHttpClient client = HttpClientBuilder.create().build();
                HttpGet request = new HttpGet(uri);
                request.addHeader(ACCEPT, "application/json");
                request.addHeader(USER_AGENT, "Mozilla/5.0");
                request.addHeader(ACCEPT_ENCODING, "gzip, deflate");
                HttpResponse res = client.execute(request);
                if (res.getStatusLine().getStatusCode() != 200) {
                    client.close();
                    throw new CommandException("URL returned status code " + res.getStatusLine().getStatusCode() + "!", new Object[0]);
                }
                rd = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                client.close();
                try {
                    JsonElement jelement = new JsonParser().parse(result.toString());
                    processJson(server, sender, jelement.getAsJsonObject());
                } catch (JsonSyntaxException ex) {
                    throw new CommandException("Something failed while parsing the json!", new Object[0]);
                }
            } catch (IOException | IllegalStateException ex) {
                Logger.getLogger(JsonCommand.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            throw new WrongUsageException("/json [URL]");
        }
    }

    private void processJson(MinecraftServer server, ICommandSender sender, JsonObject json) throws CommandException {
        JsonArray actions = json.getAsJsonArray("actions");
        for (int i = 0; i < actions.size(); i++) {
            JsonObject action = actions.get(i).getAsJsonObject();
            String command = action.get("command").getAsString();
            String args = action.get("args").getAsString();
            if(command == null || command == "") {
                throw new CommandException("Command cannot be null!", new Object[0]);
            }
            int executeCommand = server.getCommandManager().executeCommand(sender, command + " " + args);
            if (executeCommand == 0) {
                throw new CommandException("", new Object[0]);
            }
        }
    }
}
