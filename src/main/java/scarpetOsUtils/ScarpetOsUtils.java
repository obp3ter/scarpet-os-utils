package scarpetOsUtils;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.script.CarpetExpression;
import lombok.extern.slf4j.Slf4j;
import net.fabricmc.api.ModInitializer;
import scarpetOsUtils.script.GetOs;
import scarpetOsUtils.script.OsExecutor;

@Slf4j
public class ScarpetOsUtils implements CarpetExtension, ModInitializer {

    @Override
    public void onInitialize() {
        CarpetServer.manageExtension(this);
        log.info("Scarpet Os Utils Loaded");
    }
    @Override
    public void scarpetApi(CarpetExpression expression){
        OsExecutor.apply(expression.getExpr());
        GetOs.apply(expression.getExpr());
    }
}
