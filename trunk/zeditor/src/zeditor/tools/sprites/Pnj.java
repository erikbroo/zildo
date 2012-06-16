/**
 * Legend of Zildo
 * Copyright (C) 2006-2012 Evariste Boussaton
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

package zeditor.tools.sprites;

import java.util.Arrays;

import zeditor.tools.tiles.GraphChange;
import zildo.monde.util.Zone;

/**
 * @author Tchegito
 * 
 */
public class Pnj extends SpriteBanque {

	public Pnj() {
		zones = new Zone[] {
				// Princesse Roxy
				new Zone(0, 0, 15, 23),
				new Zone(15, 0, 15, 23),
				new Zone(0, 23, 13, 23),
				new Zone(13, 23, 14, 23),
				new Zone(0, 46, 16, 23),
				new Zone(16, 46, 16, 23),
				new Zone(0, 69, 13, 23),
				new Zone(13, 69, 14, 23),

				// Corps du garde vert - 8
				new Zone(68, 109, 16, 17),
				new Zone(84, 109, 16, 17),
				new Zone(100, 109, 18, 17),
				new Zone(118, 109, 18, 17),
				new Zone(0, 109, 16, 17),
				new Zone(16, 109, 16, 17),
				new Zone(32, 109, 18, 17),
				new Zone(50, 109, 18, 17),
				// Tete du garde vert - 16
				new Zone(0, 93, 14, 15),
				new Zone(14, 93, 15, 16),
				new Zone(29, 93, 14, 16),
				new Zone(43, 93, 15, 16),

				// Garde bleu - 20
				new Zone(210, 9, 22, 24),
				new Zone(232, 8, 22, 25),
				new Zone(254, 9, 22, 24),
				new Zone(276, 8, 22, 25),
				new Zone(298, 8, 22, 25),
				new Zone(211, 98, 18, 27),
				new Zone(238, 98, 18, 27),
				new Zone(265, 98, 17, 27),
				new Zone(211, 61, 22, 24),
				new Zone(233, 60, 22, 25),
				new Zone(255, 61, 22, 24),
				new Zone(277, 60, 21, 25),
				new Zone(220, 33, 18, 27),
				new Zone(247, 34, 18, 26),
				new Zone(280, 33, 17, 27),

				// Poule - 35
				new Zone(32, 2, 16, 17),
				new Zone(48, 2, 15, 17),
				new Zone(63, 1, 15, 18),
				new Zone(78, 0, 15, 19),
				// Gar�on - 39
				new Zone(33, 20, 14, 22),
				new Zone(47, 20, 14, 22),
				new Zone(61, 20, 14, 22),
				// Vendeur - 42
				new Zone(33, 43, 16, 25),
				new Zone(49, 43, 16, 25),
				// Marchand oriental - 44
				new Zone(33, 69, 16, 24),
				new Zone(49, 70, 16, 23),
				// Vieux endormi - 46
				new Zone(95, 29, 16, 24),
				new Zone(111, 31, 16, 22),
				new Zone(127, 30, 16, 23),
				// Villageoise bleue - 49
				new Zone(0, 159, 16, 24),
				new Zone(16, 159, 16, 24),
				new Zone(32, 159, 14, 24),
				new Zone(46, 158, 13, 25),
				new Zone(59, 158, 16, 25),
				new Zone(75, 158, 16, 25),
				// Gar�on brun - 55
				new Zone(177, 70, 16, 28),
				new Zone(193, 70, 16, 28),
				// Villageoise jaune - 57
				new Zone(156, 133, 16, 23),
				new Zone(172, 133, 16, 23),
				new Zone(234, 133, 14, 23),
				new Zone(220, 132, 14, 24),
				new Zone(188, 133, 16, 23),
				new Zone(204, 133, 16, 23),
				new Zone(248, 132, 14, 24),
				new Zone(262, 133, 14, 23),
				// Vieille au balai - 65
				new Zone(95, 0, 16, 27),
				new Zone(111, 1, 16, 27),
				// Vieille sans balai - 67
				new Zone(121, 134, 16, 24),
				new Zone(137, 135, 16, 23),
				// Pr�tre - 69
				new Zone(121, 159, 16, 24),
				new Zone(137, 159, 14, 24),
				new Zone(151, 159, 16, 24),
				new Zone(167, 159, 14, 24),
				// Homme lunettes - 73
				new Zone(199, 158, 16, 25),
				new Zone(215, 158, 15, 25),
				new Zone(184, 158, 15, 25),
				// Poule suite - 76
				new Zone(129, 3, 16, 17),
				new Zone(145, 3, 15, 17),
				new Zone(160, 2, 15, 18),
				new Zone(175, 1, 15, 19),
				// Sorcier violet - 80
				new Zone(231, 161, 16, 23),
				new Zone(247, 162, 16, 22),
				// Soulard - 82
				new Zone(95, 54, 16, 25),
				new Zone(111, 54, 15, 25),
				// Panneau - 84
				new Zone(79, 26, 10, 10),
				// Garde au bouclier impassable - 85
				new Zone(67, 129, 25, 28),
				new Zone(25, 129, 21, 28),
				new Zone(0, 127, 25, 30),
				new Zone(46, 129, 21, 28),
				// Spectre volant - 89
				new Zone(0, 184, 17, 16),
				new Zone(17, 184, 17, 16),
				// Corbeau - 91
				new Zone(53, 54, 16, 17),
				new Zone(69, 56, 16, 15),
				new Zone(85, 55, 16, 16),
				new Zone(53, 71, 16, 17),
				new Zone(69, 73, 16, 15),
				new Zone(85, 72, 16, 16),
				// Bestiole rouge - 97
				new Zone(0, 37, 16, 17), new Zone(16, 37, 16, 17),
				new Zone(32, 37, 15, 17),
				new Zone(0, 54, 17, 17),
				new Zone(17, 54, 16, 17),
				new Zone(33, 54, 16, 17),
				new Zone(0, 71, 16, 17),
				new Zone(16, 71, 16, 17),
				new Zone(32, 71, 16, 17),
				new Zone(0, 88, 17, 17),
				new Zone(17, 88, 16, 17),
				new Zone(33, 88, 16, 17),
				// Crabe - 109
				new Zone(53, 37, 16, 15),
				new Zone(69, 39, 15, 13),
				new Zone(84, 39, 15, 13),
				// Abeille - 112
				new Zone(52, 91, 8, 7),
				new Zone(60, 91, 8, 7),
				new Zone(52, 98, 8, 7),
				new Zone(60, 98, 8, 7),

				// Armes du garde bleu
				// Epee
				new Zone(186, 76, 5, 16), new Zone(188, 94, 16, 5),
				new Zone(193, 76, 5, 16), new Zone(188, 100, 16, 5),
				// Lance
				new Zone(164, 84, 5, 16), new Zone(160, 72, 16, 5),
				new Zone(171, 84, 5, 16), new Zone(160, 78, 16, 5),
				
				// Woodcutter
				new Zone(144, 57, 16, 25)};
		
		
		pkmChanges = Arrays.asList(new GraphChange[]{
				new GraphChange("pnj", 0, 0), new GraphChange("pnj2", 55, 0),
				new GraphChange("pnj", 57, 0), new GraphChange("pnj2", 91, 0),
				new GraphChange("pnj", 116, 0)
		});
	}
}