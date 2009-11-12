/**
 *
 */
package zildo;

import java.util.HashSet;
import java.util.Set;

import zildo.client.Client;
import zildo.client.ClientEngineZildo;
import zildo.client.ClientEventNature;
import zildo.client.Client.ClientType;
import zildo.client.gui.menu.PlayerNameMenu;
import zildo.fwk.input.KeyboardInstant;
import zildo.monde.Game;
import zildo.server.EngineZildo;
import zildo.server.Server;
import zildo.server.state.ClientState;

/**
 * Represents the game owner core class. It could be :
 * - a single player, with no network support
 * - a first player, who can accept client in his game
 * 
 * @author tchegito
 */
public class SinglePlayer {

    EngineZildo engineZildo;
    ClientEngineZildo clientEngineZildo;
    Server server;

    public SinglePlayer(Server p_server) {
        server = p_server;
        engineZildo = p_server.getEngineZildo();
    }

    public SinglePlayer(Game p_game) {
    	p_game.multiPlayer=false;
        engineZildo = new EngineZildo(p_game);

        launchGame();
    }

    /**
     * Launch the game. Several cases:
     * -the current player is both server and client
     * -the same, but in fake mode : we emulate a network traffic
     */
    public void launchGame() {
        Client client = ClientEngineZildo.getClientForGame();
        client.setUpNetwork(ClientType.SERVER_AND_CLIENT, null, 0, server!=null);
        ClientEngineZildo clientEngineZildo = client.getEngineZildo();

        // Initialize map
        ClientEngineZildo.mapDisplay.setCurrentMap(EngineZildo.mapManagement.getCurrentMap());

        boolean done = false;
        Set<ClientState> states=new HashSet<ClientState>();

        // Create Zildo !
        int zildoId;
        ClientState state;
        if (server != null) {
        	zildoId=server.connectClient(null, PlayerNameMenu.loadPlayerName());
        	state=server.getClientStates().iterator().next();
    		ClientEngineZildo.guiDisplay.displayMessage("server started");
        } else {
        	zildoId = engineZildo.spawnClient();
        	state = new ClientState(null, zildoId);
        }
        ClientEngineZildo.spriteDisplay.setZildoId(zildoId);

        while (!done) {
        	states.clear();

            // Server's network job
            if (server != null) {
                server.networkJob();
                
                states.addAll(server.getClientStates());
            }
            
            // Render events (1: client and 2: server)
            state.event = engineZildo.renderEvent(state.event);
            state.event = clientEngineZildo.renderEvent(state.event);
	            
            if (state.event.nature == ClientEventNature.NOEVENT) {
	            // Reset queues
	        	EngineZildo.soundManagement.resetQueue();
	        	EngineZildo.dialogManagement.resetQueue();
	
	            // Read keyboard
	            KeyboardInstant instant = KeyboardInstant.getKeyboardInstant();
	            state.keys = instant;
	            client.setKbInstant(instant);
	            states.add(state);
            }

            // Update server
            engineZildo.renderFrame(states);

            // Dialogs
            if (ClientEngineZildo.dialogDisplay.launchDialog(EngineZildo.dialogManagement.getQueue())) {
            	EngineZildo.dialogManagement.stopDialog(state);
            }

            // Update client
            ClientEngineZildo.spriteDisplay.setEntities(EngineZildo.spriteManagement.getSpriteEntities(state));

            // Render client
            done = client.render();
            
            // Render sounds
            ClientEngineZildo.soundPlay.playSounds(EngineZildo.soundManagement.getQueue());
        }
        
        if (server != null) {
        	// Tells all clients that we're leaving
        	server.disconnectServer();
        }
        clientEngineZildo.cleanUp();
        client.cleanUp();
        engineZildo.cleanUp();
    }
}