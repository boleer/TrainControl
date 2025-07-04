package org.traincontrol.model;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.traincontrol.automation.Layout;
import org.traincontrol.base.Accessory;
import org.traincontrol.base.NodeExpression;
import org.traincontrol.base.RouteCommand;
import org.traincontrol.marklin.MarklinAccessory;
import org.traincontrol.marklin.MarklinLayout;
import org.traincontrol.marklin.MarklinLocomotive;
import org.traincontrol.marklin.MarklinLocomotive.decoderType;
import org.traincontrol.marklin.MarklinRoute;

/**
 * Model functionality in the eyes of the GUI
 * @author Adam
 */
public interface ViewListener
{
    public void go();
    public void stop();
    public List<String> getLocList();
    public List<String> getRouteList();
    public MarklinLocomotive getLocByName(String name);
    public MarklinAccessory getAccessoryByName(String name);
    public void saveState(boolean backup);
    public MarklinLocomotive newMM2Locomotive(String name, int address);
    public MarklinLocomotive newMFXLocomotive(String name, int address);
    public MarklinLocomotive newDCCLocomotive(String name, int address);
    public boolean deleteLoc(String name);
    public String getLocAddress(String name);
    public boolean renameLoc(String oldName, String newName);
    public void setAccessoryState(int address, Accessory.accessoryDecoderType decoderType, boolean state);
    public void execRoute(String name);
    public void deleteRoute(String name);
    public boolean getAccessoryState(int address, Accessory.accessoryDecoderType decoderType);
    public MarklinAccessory getAccessoryByAddress(int address, Accessory.accessoryDecoderType decoderType);
    public boolean getPowerState();
    public void allFunctionsOff();
    public void locFunctionsOff(MarklinLocomotive l);
    public void lightsOn(List<String> locomotives);
    public void log(String s);
    public void log(Exception e);
    public void stopAllLocs();
    public int syncWithCS2();
    public List<String> getLayoutList();
    public MarklinLayout getLayout(String name);
    public void syncLocomotive(String name);
    public boolean isFeedbackSet(String name);
    public boolean getFeedbackState(String name);
    public boolean setFeedbackState(String name, boolean state); // for simulation purposes
    public boolean isCS3();
    public String getCS3AppUrl();
    public boolean newRoute(String name, List<RouteCommand> route, int s88, MarklinRoute.s88Triggers s88Trigger, boolean routeEnabled, NodeExpression conditions);
    public void editRoute(String name, String newName, List<RouteCommand> route, int s88, MarklinRoute.s88Triggers s88Trigger, boolean routeEnabled, NodeExpression conditions);
    public MarklinRoute getRoute(String name);
    public MarklinRoute getRoute(int id);
    public int getRouteId(String name);
    public Map<Integer, Set<MarklinLocomotive>> getDuplicateLocAddresses();
    public void parseAuto(String s);
    public Layout getAutoLayout();
    public boolean isAutonomyRunning();
    public boolean isDebug();
    MarklinAccessory newSignal(int address, Accessory.accessoryDecoderType decoderType, boolean state);
    MarklinAccessory newSwitch(int address, Accessory.accessoryDecoderType decoderType, boolean state);
    public boolean getNetworkCommState();
    public int getNumMessagesProcessed();
    public boolean changeRouteId(String name, int newId);
    public void clearLayouts();
    public String exportRoutes() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, Exception;
    public void importRoutes(String json);
    public List<MarklinLocomotive> getLocomotives();
    public void changeLocAddress(String locName, int newAddress, decoderType newDecoderType) throws Exception;
    public void sendPing(boolean force);
    public long getTimeSinceLastPing();
    public TreeMap<String, Long> getDailyRuntimeStats(int days, long offset);
    public TreeMap<String, Integer> getDailyCountStats(int days, long offset);
    public int getTotalLocStats(int days, long offset);
    public MarklinLocomotive isLocLinkedToOthers(MarklinLocomotive l);
    public void waitForPowerState(boolean state) throws InterruptedException;
    public void downloadLayout(File path) throws Exception;
}
