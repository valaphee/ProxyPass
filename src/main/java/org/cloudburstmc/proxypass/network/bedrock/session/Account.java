package org.cloudburstmc.proxypass.network.bedrock.session;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import net.lenni0451.commons.httpclient.HttpClient;
import net.raphimc.minecraftauth.MinecraftAuth;
import net.raphimc.minecraftauth.step.bedrock.session.StepFullBedrockSession;
import org.cloudburstmc.proxypass.network.bedrock.util.MinecraftServicesUtils;

@Accessors(fluent = true)
@Data
@AllArgsConstructor
// adapted from https://github.com/ViaVersion/ViaProxy/blob/ca40e290092d99abd842f8cce645d8db407de105/src/main/java/net/raphimc/viaproxy/saves/impl/accounts/BedrockAccount.java#L29-L101
public class Account {
    private StepFullBedrockSession.FullBedrockSession bedrockSession;
    private String mcToken;

    public Account(JsonObject jsonObject) throws Exception {
        this.bedrockSession = MinecraftAuth.BEDROCK_DEVICE_CODE_LOGIN.fromJson(jsonObject);
        this.mcToken = MinecraftServicesUtils.getMcToken(MinecraftAuth.createHttpClient(), bedrockSession.getPlayFabToken());
    }

    public JsonObject toJson() {
        JsonObject jsonObject = MinecraftAuth.BEDROCK_DEVICE_CODE_LOGIN.toJson(bedrockSession);
        return jsonObject;
    }

    public boolean refresh(HttpClient httpClient) throws Exception {
        bedrockSession = MinecraftAuth.BEDROCK_DEVICE_CODE_LOGIN.refresh(httpClient, bedrockSession);
        return true;
    }
}
