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

package zeditor.windows.dialogs;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import zeditor.tools.builder.Modifier;
import zeditor.tools.ui.SizedGridPanel;
import zildo.fwk.gfx.engine.TileEngine;
import zildo.monde.sprites.SpriteStore;

@SuppressWarnings("serial")
public class BuilderDialog extends JDialog {

	JComboBox comboTileBank;
	JComboBox comboSpriteBank;
	
	public BuilderDialog() {
		SizedGridPanel panel = new SizedGridPanel(3, 5);

		// Tile bank combo
		List<String> listTileBanks = new ArrayList<String>();
		for (String bankName : TileEngine.tileBankNames) {
			listTileBanks.add(bankName);
		}
		comboTileBank = new JComboBox(listTileBanks.toArray(new String[]{}));
		JButton buttonBuildTileBank = new JButton(new AbstractAction("Build tile bank") {
			@Override
			public void actionPerformed(ActionEvent e) {
				String bankName = (String) comboTileBank.getSelectedItem();

				new Modifier().saveNamedTileBank(bankName);
				JOptionPane.showMessageDialog(getParent(), "Tile bank "+bankName+" has been builded !", "Builder", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		panel.addComp(comboTileBank, buttonBuildTileBank);
				
		// All motif bank
		JButton buttonAllTileBanks = new JButton(new AbstractAction("Build all tile banks") {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					new Modifier().saveAllMotifBank();
					JOptionPane.showMessageDialog(getParent(), "All tile banks builded successfully !", "Builder", JOptionPane.INFORMATION_MESSAGE);
				} catch (RuntimeException ex) {
					JOptionPane.showMessageDialog(getParent(), "Error building all tile banks !\n\nCause : "+ex.getMessage(), "Builder", JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			}
		});
		panel.add(buttonAllTileBanks);
		
		// Sprite bank combo
		List<String> listSpriteBanks = new ArrayList<String>();
		for (String bankName : SpriteStore.sprBankName) {
			listSpriteBanks.add(bankName);
		}
		comboSpriteBank = new JComboBox(listSpriteBanks.toArray(new String[]{}));
		JButton buttonBuildSpriteBank = new JButton(new AbstractAction("Build sprite bank") {
			@Override
			public void actionPerformed(ActionEvent e) {
				String bankName = (String) comboSpriteBank.getSelectedItem();

				try {
					new Modifier().saveNamedSpriteBank(bankName);
					JOptionPane.showMessageDialog(getParent(), "Sprite bank "+bankName+" has been builded !", "Builder", JOptionPane.INFORMATION_MESSAGE);
				} catch (RuntimeException ex) {
					JOptionPane.showMessageDialog(getParent(), "Error building bank "+bankName+"\n\nCause : "+ex.getMessage(), "Builder", JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			}
		});
		panel.addComp(comboSpriteBank, buttonBuildSpriteBank);

		
		setTitle("ZEditor builder");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		add(new JLabel("Build your own thing."), BorderLayout.NORTH);
		add(panel, BorderLayout.CENTER);
		add(new JButton(new AbstractAction("OK") {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		}), BorderLayout.SOUTH);
		
		pack();
	}
	
	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				BuilderDialog inst = new BuilderDialog();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
}
