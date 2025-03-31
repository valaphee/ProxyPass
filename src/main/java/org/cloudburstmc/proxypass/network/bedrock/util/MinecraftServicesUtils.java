package org.cloudburstmc.proxypass.network.bedrock.util;

import com.google.gson.JsonObject;
import lombok.SneakyThrows;
import net.lenni0451.commons.httpclient.HttpClient;
import net.lenni0451.commons.httpclient.requests.impl.GetRequest;
import net.lenni0451.commons.httpclient.requests.impl.PostRequest;
import net.raphimc.minecraftauth.responsehandler.PlayFabResponseHandler;
import net.raphimc.minecraftauth.step.bedrock.StepPlayFabToken;
import net.raphimc.minecraftauth.util.JsonContent;

import java.net.InetSocketAddress;
import java.util.UUID;

public class MinecraftServicesUtils {
    @SneakyThrows
    public static String getMcToken(HttpClient httpClient, StepPlayFabToken.PlayFabToken playFabToken) {
        JsonObject postData = new JsonObject();
        JsonObject device = new JsonObject();
        device.addProperty("applicationType", "MinecraftPE");
        device.addProperty("gameVersion", "1.21.70");
        device.addProperty("id", UUID.randomUUID().toString());
        device.addProperty("memory", 8 * 1024 * 1024 * 1024);
        device.addProperty("platform", "Windows10");
        device.addProperty("playFabTitleId", "20CA2");
        device.addProperty("storePlatform", "uwp.store");
        device.addProperty("type", "Windows10");
        postData.add("device", device);
        JsonObject user = new JsonObject();
        user.addProperty("language", "en");
        user.addProperty("languageCode", "en-US");
        user.addProperty("regionCode", "US");
        user.addProperty("token", playFabToken.getSessionTicket());
        user.addProperty("tokenType", "PLayFab");
        postData.add("user", user);
        PostRequest postRequest = new PostRequest("https://authorization.franchise.minecraft-services.net/api/v1.0/session/start");
        postRequest.setContent(new JsonContent(postData));
        JsonObject obj = httpClient.execute(postRequest, new PlayFabResponseHandler());
        JsonObject result = obj.getAsJsonObject("result");
        return result.get("authorizationHeader").getAsString();
    }

    @SneakyThrows
    public static InetSocketAddress getVenueAddress(HttpClient httpClient, String mcToken, UUID venueId) {
        GetRequest getRequest = new GetRequest("https://gatherings.franchise.minecraft-services.net/api/v1.0/venue/" + venueId);
        getRequest.setHeader("authorization", mcToken);
        JsonObject obj = httpClient.execute(getRequest, new PlayFabResponseHandler());
        JsonObject result = obj.getAsJsonObject("result");
        JsonObject venue = result.getAsJsonObject("venue");
        return InetSocketAddress.createUnresolved(venue.get("serverIpAddress").getAsString(), venue.get("serverPort").getAsInt());
    }
}
