package xyz.tcbuildmc.minecraft.plugin.velocityplayerlimit.config;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class VelocityPlayerLimitConfig {
    private boolean enable = false;
    private int limit = -1;
    private List<String> exceptPlayers = new ArrayList<>();
}
