/**
 * Legend of Zildo
 * Copyright (C) 2006-2010 Evariste Boussaton
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

package zeditor.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tchegito
 *
 */
public enum Prefetch {

	Colline(PrefKind.Drop), CollineGauche(PrefKind.Drop), CollineDroite(PrefKind.Drop), CollineBordure(PrefKind.Colour),
	CollineMarron(PrefKind.Colour), PetitChemin(PrefKind.Drop), GrandChemin(PrefKind.Drop), Eau(PrefKind.Trace),
	
	Arbre(PrefKind.Drop), Souche(PrefKind.Drop), Statue(PrefKind.Drop), ArcheVillage(PrefKind.Drop), GrossePierre(PrefKind.Drop),
	MaisonRouge(PrefKind.Trace), MaisonBleue(PrefKind.Colour), MaisonVerte(PrefKind.Colour),
	Souterrain(PrefKind.Trace), SouterrainLarge(PrefKind.Trace),
	ArbreRouge(PrefKind.Drop), ArbreJaune(PrefKind.Drop),
	RouteDesert(PrefKind.Drop);
	
	PrefKind kind;
	
	private Prefetch(PrefKind p_kind) {
		kind = p_kind;	
	}
	
	static public String[] getNames() {
		List<String> names=new ArrayList<String>();
		StringBuilder sb;
		for (Prefetch pref : values()) {
			String name=pref.name();
			sb=new StringBuilder();
			for (int i=0;i<name.length();i++) {
				char a=name.charAt(i);
				if (Character.isUpperCase(a) && i>0) {
					sb.append(" ");
				}
				sb.append(Character.toLowerCase(a));
			}
			names.add(sb.toString());
		}
		return names.toArray(new String[]{});
	}
}
