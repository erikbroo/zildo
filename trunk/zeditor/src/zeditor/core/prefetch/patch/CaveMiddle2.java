/**
 * The Land of Alembrum
 * Copyright (C) 2006-2013 Evariste Boussaton
 * 
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

package zeditor.core.prefetch.patch;

/**
 * @author Tchegito
 *
 */
public class CaveMiddle2 extends AbstractPatch12 {

	byte[] conv_value = 
	{ 96, 13, 11, 12, 6, 10, -1, 64, 4, -1, 8, 55, 5, 60, 67, 96};
	/*
	{ 96, 13, 11, 12, 6, 10, -1, 32, 4, -1, 8, 29, 5, 30, 31, 96};
	 */
	byte[] value =
	getReverseTab(conv_value, 4);

	public CaveMiddle2() {
		super(true);
	}

	@Override
	public
	int toBinaryValue(int p_val) {
		int a = p_val - 256 * 3 - 4;
		if (a < 0 || a >= value.length) {
			return 0;
		}
		return value[a];
	}

	@Override
	public
	int toGraphicalValue(int p_val) {
		return conv_value[p_val] + 256 * 3;
	}

}