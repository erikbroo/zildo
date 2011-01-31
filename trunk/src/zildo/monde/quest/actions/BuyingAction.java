/**
 * Legend of Zildo
 * Copyright (C) 2006-2011 Evariste Boussaton
 * Based on original Zelda : link to the past (C) Nintendo 1992
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package zildo.monde.quest.actions;

import java.util.ArrayList;
import java.util.List;

import zildo.monde.dialog.ActionDialog;
import zildo.monde.items.Item;
import zildo.monde.items.ItemKind;
import zildo.monde.sprites.persos.Perso;
import zildo.monde.sprites.persos.PersoZildo;
import zildo.server.state.ClientState;

/**
 * @author Tchegito
 *
 */
public class BuyingAction extends ActionDialog {

	PersoZildo zildo;
	Perso seller;
	
	/**
	 * @param p_text
	 */
	public BuyingAction(String p_text) {
		super(p_text);
	}

	public BuyingAction(PersoZildo p_zildo, Perso p_seller) {
		super(null);
		zildo = p_zildo;
		seller = p_seller;
	}
	
	@Override
	public void launchAction(ClientState p_clientState) {
		
		List<Item> items=new ArrayList<Item>();
		items.addAll(zildo.getInventory());
		items.add(new Item(ItemKind.BOOMERANG, 2));
		items.add(new Item(ItemKind.BOMB, 3));
		zildo.lookItems(items, seller, true);
		
		p_clientState.dialogState.dialoguing=true;
	}

}
