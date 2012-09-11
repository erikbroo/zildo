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

package zildo.platform.opengl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;

import zildo.fwk.opengl.Sound;
import zildo.resource.Constantes;

public class LwjglSound extends Sound {

	Audio snd = null;

	boolean music;
	
	@Override
	public void finalize() {
		AL.destroy();
	}
	
	public LwjglSound(String p_filename) {
		loadALData(p_filename);
	}

	private int loadALData(String p_filename) {
		String format = p_filename.substring(p_filename.length() - 3).toUpperCase();
		File file=new File(Constantes.DATA_PATH + p_filename);
		
		try {
			InputStream stream=new FileInputStream(file);
			snd = AudioLoader.getAudio(format, stream);
			music = "OGG".equals(format);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		// Do another error check and return.
		if (AL10.alGetError() == AL10.AL_NO_ERROR)
			return AL10.AL_TRUE;

		return AL10.AL_FALSE;

	}

	@Override
	public void play() {
		if (music) {
			snd.playAsMusic(1.0f, 1.0f, true);
		} else {
			snd.playAsSoundEffect(1.0f, 1.0f, false);
		}
	}

	@Override
	public void stop() {
		snd.stop();
	}

	public void pause() {
		snd.stop();
	}
}