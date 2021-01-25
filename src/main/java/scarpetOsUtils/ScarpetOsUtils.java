package scarpetOsUtils;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.script.CarpetExpression;
import net.fabricmc.api.ModInitializer;
import scarpetOsUtils.script.GetOs;
import scarpetOsUtils.script.OsExecutor;

public class ScarpetOsUtils implements CarpetExtension, ModInitializer {

    @Override
    public void onInitialize() {
        CarpetServer.manageExtension(this);
        System.out.println("Scarpet Os Utils loaded");
    }
    @Override
    public void scarpetApi(CarpetExpression expression){
        OsExecutor.apply(expression.getExpr());
        GetOs.apply(expression.getExpr());
    }
}
