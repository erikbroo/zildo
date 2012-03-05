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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import zildo.Zildo;
import zildo.client.ClientEngineZildo;
import zildo.fwk.ZUtils;
import zildo.fwk.input.KeyboardHandler;
import zildo.fwk.opengl.OpenGLGestion;
import zildo.platform.input.LwjglKeyboardHandler;
import zildo.server.EngineZildo;

public class AndroidOpenGLGestion extends OpenGLGestion {

	GL10 gl10;
	
	final static String title = "Zildo OpenGL";

	KeyboardHandler kbHandler = Zildo.pdPlugin.kbHandler;
	
	public static final byte[] icon = { 71, -110, 88, -1, 75, -113, 84, -1, 84, -106, 98, -1, 54, 89, 42, -1, 48, 30,
			0, -1, 110, 51, 26,
			-1, -101, 61, 47, -1, 113, 22, 12, -1, 80, 29, 12, -1, 77, 78, 49, -1, 80, 125, 86, -1, 94, -122, 103, -1,
			114, -117, 119, -1,
			-37, -1, -20, -1, -115, -56, -94, -1, 61, -119, 73, -1, 97, -113, 96, -1, 94, 122, 77, -1, 55, 71, 35, -1,
			49, 23, 0, -1, -114,
			59, 29, -1, -67, 71, 51, -1, -54, 56, 38, -1, -80, 41, 18, -1, -26, -121, 101, -1, 93, 55, 19, -1, 14, 17,
			0, -1, 115, 123, 87,
			-1, -18, -12, -25, -1, -40, -14, -32, -1, -119, -68, -102, -1, 67, -115, 79, -1, 79, 93, 60, -1, 40, 17, 0,
			-1, 40, 0, 0, -1,
			101, 9, 5, -1, -49, 72, 60, -1, -65, 45, 24, -1, -67, 41, 4, -1, -49, 84, 24, -1, -51, 120, 40, -1, 105,
			56, 0, -1, -59, -87,
			73, -1, 95, 79, 17, -1, -75, -79, -109, -1, -62, -43, -79, -1, 68, 115, 65, -1, 78, -107, 82, -1, 91, 46,
			29, -1, -87, 80, 67,
			-1, -122, 29, 16, -1, 121, 15, 9, -1, 114, 7, 0, -1, -84, 68, 34, -1, -35, -127, 70, -1, -120, 65, 11, -1,
			105, 53, 0, -1, -65,
			-102, 46, -1, -1, -29, 108, -1, -26, -52, 124, -1, 26, 18, 0, -1, -49, -29, -62, -1, 71, 121, 78, -1, 75,
			-104, 77, -1, -127,
			50, 47, -1, -83, 53, 48, -1, -102, 33, 21, -1, 112, 25, 14, -1, 117, 59, 44, -1, 108, 55, 18, -1, 92, 48,
			5, -1, -105, 116, 45,
			-1, -62, -94, 85, -1, -2, -28, -112, -1, 127, 103, 20, -1, 95, 73, 18, -1, -99, -106, 122, -1, 122, -109,
			-128, -1, 81, -117,
			108, -1, 66, -109, 74, -1, 112, 57, 54, -1, -121, 49, 54, -1, 104, 18, 19, -1, 50, 0, 0, -1, -86, 113, 105,
			-1, -79, 126, 99,
			-1, 66, 24, 0, -1, -52, -87, 118, -1, 54, 25, 0, -1, 57, 34, 0, -1, 94, 70, 26, -1, 28, 8, 0, -1, -28, -32,
			-57, -1, 86, 115,
			95, -1, 72, -128, 98, -1, 78, -101, 90, -1, 68, 53, 39, -1, 69, 33, 29, -1, 32, 2, 0, -1, 21, 0, 0, -1,
			114, 66, 44, -1, -46,
			-90, -117, -1, 73, 34, 0, -1, -93, -125, 85, -1, 107, 77, 40, -1, 109, 82, 58, -1, -119, 112, 101, -1, 67,
			51, 36, -1, 106,
			110, 89, -1, 62, 96, 75, -1, 103, -91, -124, -1, 72, -110, 89, -1, -115, -83, -116, -1, 34, 54, 24, -1, 73,
			100, 66, -1, 7, 2,
			0, -1, 122, 83, 36, -1, -107, 113, 72, -1, 69, 31, 0, -1, -98, 122, 70, -1, -111, 112, 63, -1, -111, 115,
			79, -1, -100, 125,
			112, -1, -109, -124, 120, -1, 0, 11, 0, -1, 99, -119, 111, -1, 96, -98, 121, -1, 74, -110, 93, -1, -116,
			-51, -101, -1, 80,
			-113, 85, -1, 57, 124, 62, -1, 94, 117, 56, -1, 57, 32, 0, -1, 74, 47, 20, -1, -113, 109, 85, -1, 96, 60,
			22, -1, -63, -96,
			109, -1, -118, 107, 59, -1, 114, 83, 51, -1, 78, 69, 47, -1, 127, -105, 125, -1, -80, -33, -71, -1, 103,
			-86, 122, -1, 59,
			-127, 75, -1, 124, -56, -113, -1, 65, -115, 76, -1, 93, -88, 96, -1, -112, -74, -121, -1, 24, 23, 20, -1,
			8, 0, 17, -1, 68, 44,
			75, -1, 79, 50, 58, -1, 68, 40, 17, -1, 66, 42, 2, -1, 54, 29, 6, -1, 119, 118, 87, -1, -85, -51, -85, -1,
			-89, -36, -86, -1,
			92, -94, 100, -1, 81, -105, 94, -1, 127, -58, -110, -1, 67, -119, 80, -1, 110, -83, 108, -1, 84, -127, 111,
			-1, 23, 47, 99, -1,
			66, 71, -98, -1, 31, 19, 118, -1, 25, 7, 70, -1, 34, 15, 30, -1, 108, 94, 76, -1, 112, 98, 75, -1, 1, 9, 0,
			-1, -83, -43, -87,
			-1, -96, -36, -100, -1, 97, -86, 97, -1, 66, -119, 73, -1, -123, -58, -111, -1, 81, -119, 89, -1, 114,
			-106, 110, -1, 71, 99,
			101, -1, 1, 19, 81, -1, 74, 80, -76, -1, 84, 75, -53, -1, 50, 37, -107, -1, 42, 31, 97, -1, 10, 1, 29, -1,
			16, 13, 17, -1,
			-111, -97, -114, -1, -70, -31, -70, -1, -90, -34, -91, -1, 91, -93, 90, -1, 73, -110, 77, -1, 122, -69,
			-126, -1, 106, -107,
			107, -1, 56, 72, 51, -1, 24, 25, 35, -1, 30, 25, 75, -1, 44, 38, 126, -1, 75, 69, -64, -1, 73, 69, -61, -1,
			41, 36, -120, -1,
			61, 56, 117, -1, 21, 10, 42, -1, 75, 85, 81, -1, 111, -107, 114, -1, -122, -71, -119, -1, 107, -83, 111,
			-1, 81, -100, 86, -1,
			-67, -7, -60, -1, -98, -56, -95, -1, 0, 0, 0, -1, 38, 26, 41, -1, 70, 45, 101, -1, 63, 50, -117, -1, 54,
			51, -90, -1, 40, 46,
			-99, -1, 52, 54, -112, -1, 59, 51, 111, -1, 94, 68, 113, -1, 22, 20, 26, -1, -60, -26, -58, -1, -59, -15,
			-57, -1, -68, -7,
			-64, -1, 100, -85, 108, -1, -101, -46, -96, -1, -83, -47, -83, -1, 63, 80, 61, -1, 42, 28, 41, -1, 53, 21,
			66, -1, 61, 37, 110,
			-1, 71, 54, -107, -1, 67, 55, -105, -1, 65, 54, -126, -1, 62, 45, 99, -1, 29, 6, 50, -1, 6, 6, 14, -1, 113,
			-114, 115, -1, -77,
			-36, -73, -1, -82, -27, -77, -1, 103, -88, 113, -1, -88, -36, -84, -1, -65, -26, -63, -1, -105, -81, -103,
			-1, 1, 5, 0, -1, 12,
			0, 13, -1, 31, 6, 44, -1, 43, 5, 61, -1, 44, 0, 57, -1, 48, 12, 55, -1, 9, 0, 17, -1, 1, 1, 17, -1, 0, 0,
			0, -1, 16, 46, 20,
			-1, -69, -27, -63, -1, -88, -37, -80, -1, 110, -83, 124, -1 };

	public static final byte[] bigIcon = { 67, -114, 101, -1, 76, -104, 106, -1, 75, -107, 98, -1, 82, -103, 97, -1,
			82, -114, 88, -1, 76, -126, 78, -1, 107, -103, 107, -1, 97, -117, 99, -1,
			93, -123, 98, -1, 93, -128, 95, -1, 103, -128, 98, -1, 95, 114, 82, -1, 95, 110, 79, -1, 96, 113, 79, -1,
			100, 125, 86, -1, 99, -121, 89, -1,
			80, -125, 75, -1, 82, -112, 81, -1, 79, -108, 83, -1, 78, -105, 82, -1, 75, -110, 78, -1, 72, -116, 67, -1,
			82, -114, 68, -1, 83, -117, 64, -1,
			102, -97, 91, -1, -109, -49, -113, -1, 93, -92, 98, -1, 69, -110, 76, -1, 72, -102, 72, -1, 67, -107, 65,
			-1, 78, -104, 77, -1, 74, -110, 73, -1,
			76, -108, 108, -1, 62, -121, 90, -1, 69, -113, 92, -1, 72, -113, 87, -1, 78, -115, 86, -1, 88, -114, 90,
			-1, 93, -117, 92, -1, 86, 124, 83, -1,
			105, -119, 100, -1, 103, 127, 95, -1, 84, 97, 67, -1, 62, 65, 36, -1, 63, 59, 32, -1, 71, 67, 38, -1, 76,
			79, 50, -1, 62, 78, 41, -1,
			80, 116, 68, -1, 96, -111, 89, -1, 78, -119, 79, -1, 75, -115, 79, -1, 83, -107, 88, -1, 85, -106, 86, -1,
			101, -100, 91, -1, 98, -106, 86, -1,
			-121, -70, 127, -1, -69, -11, -69, -1, 109, -77, 117, -1, 73, -106, 82, -1, 68, -105, 71, -1, 69, -103, 68,
			-1, 70, -109, 69, -1, 78, -104, 77, -1,
			74, -115, 100, -1, 77, -110, 102, -1, 76, -106, 99, -1, 74, -109, 91, -1, 84, -107, 93, -1, 85, -116, 85,
			-1, 84, 126, 78, -1, 94, 123, 79, -1,
			84, 102, 64, -1, 94, 98, 65, -1, 85, 70, 41, -1, 51, 23, 0, -1, 45, 7, 0, -1, 50, 8, 0, -1, 43, 4, 0, -1,
			26, 4, 0, -1,
			67, 73, 39, -1, 108, -121, 92, -1, 84, 126, 80, -1, 95, -109, 98, -1, 86, -114, 97, -1, 80, -120, 91, -1,
			89, -122, 91, -1, -106, -63, -106, -1,
			-68, -23, -64, -1, -65, -12, -56, -1, 100, -87, 115, -1, 78, -97, 93, -1, 64, -105, 72, -1, 65, -103, 67,
			-1, 75, -102, 73, -1, 72, -107, 71, -1,
			80, -118, 98, -1, 79, -116, 98, -1, 78, -112, 96, -1, 76, -118, 87, -1, 74, -128, 78, -1, 94, -120, 88, -1,
			96, 121, 79, -1, 70, 78, 39, -1,
			66, 58, 22, -1, 67, 40, 10, -1, 94, 48, 22, -1, 108, 48, 24, -1, 122, 54, 35, -1, 114, 41, 26, -1, 80, 10,
			0, -1, 83, 30, 14, -1,
			59, 35, 7, -1, 108, 110, 73, -1, 107, -128, 85, -1, 96, -124, 86, -1, 94, -121, 95, -1, 89, -125, 95, -1,
			83, 118, 86, -1, -113, -80, -109, -1,
			-58, -23, -55, -1, -49, -3, -40, -1, 106, -86, 120, -1, 62, -115, 76, -1, 64, -105, 72, -1, 66, -102, 68,
			-1, 73, -104, 69, -1, 74, -105, 69, -1,
			91, -119, 100, -1, 82, -128, 89, -1, 89, -120, 94, -1, 94, -119, 94, -1, 100, -125, 90, -1, 113, 127, 92,
			-1, 79, 74, 44, -1, 55, 29, 2, -1,
			64, 20, 0, -1, 86, 24, 0, -1, -127, 49, 24, -1, -105, 61, 35, -1, -84, 76, 51, -1, -85, 75, 50, -1, -119,
			45, 20, -1, -121, 59, 33, -1,
			84, 33, 2, -1, 105, 80, 40, -1, 89, 85, 37, -1, 79, 92, 39, -1, 97, 118, 75, -1, 126, -106, 114, -1, 123,
			-113, 115, -1, -86, -66, -93, -1,
			-44, -19, -49, -1, -41, -3, -42, -1, 115, -84, 121, -1, 69, -115, 77, -1, 73, -102, 75, -1, 71, -101, 69,
			-1, 69, -109, 63, -1, 75, -106, 67, -1,
			-113, -77, -113, -1, -114, -80, -117, -1, 99, -127, 91, -1, 57, 77, 40, -1, 41, 44, 13, -1, 42, 26, 1, -1,
			47, 10, 0, -1, 109, 53, 36, -1,
			-105, 78, 61, -1, -85, 83, 63, -1, -83, 71, 46, -1, -86, 61, 30, -1, -93, 53, 18, -1, -99, 49, 13, -1, -97,
			60, 21, -1, -83, 88, 49, -1,
			-64, 127, 85, -1, 87, 44, 0, -1, 118, 97, 34, -1, -107, -112, 80, -1, 95, 100, 44, -1, 83, 92, 47, -1, -84,
			-74, -108, -1, -41, -28, -58, -1,
			-48, -29, -61, -1, -101, -69, -110, -1, 81, -125, 80, -1, 81, -109, 83, -1, 72, -107, 71, -1, 73, -103, 68,
			-1, 75, -103, 67, -1, 75, -106, 65, -1,
			-116, -86, -122, -1, -122, -98, 122, -1, 88, 99, 65, -1, 58, 53, 23, -1, 63, 37, 14, -1, 81, 34, 16, -1,
			72, 8, 0, -1, 103, 23, 12, -1,
			-108, 55, 40, -1, -85, 66, 47, -1, -93, 52, 24, -1, -84, 58, 22, -1, -77, 66, 22, -1, -75, 75, 27, -1, -65,
			94, 43, -1, -64, 107, 52, -1,
			-108, 80, 19, -1, -106, 100, 29, -1, -64, -96, 75, -1, -41, -61, 109, -1, -106, -116, 68, -1, 105, 103, 44,
			-1, 108, 109, 65, -1, -100, -92, 125, -1,
			-51, -33, -71, -1, -108, -77, -121, -1, 90, -119, 83, -1, 87, -109, 83, -1, 76, -107, 70, -1, 73, -105, 65,
			-1, 73, -104, 63, -1, 73, -104, 63, -1,
			-115, -98, 126, -1, 109, 116, 85, -1, 76, 65, 37, -1, 74, 42, 19, -1, 122, 66, 49, -1, -110, 77, 62, -1,
			89, 7, 0, -1, 112, 22, 11, -1,
			-124, 34, 21, -1, -124, 29, 10, -1, -117, 31, 2, -1, -75, 75, 36, -1, -51, 103, 55, -1, -58, 103, 49, -1,
			-67, 103, 46, -1, -64, 118, 53, -1,
			117, 57, 0, -1, -54, -100, 71, -1, -30, -64, 95, -1, -26, -50, 110, -1, -44, -61, 115, -1, -91, -102, 90,
			-1, 56, 52, 5, -1, 92, 96, 59, -1,
			-54, -36, -74, -1, -105, -72, -115, -1, 104, -104, 102, -1, 82, -112, 81, -1, 78, -106, 74, -1, 73, -105,
			65, -1, 74, -101, 63, -1, 71, -104, 59, -1,
			99, 97, 74, -1, 63, 48, 27, -1, 95, 57, 36, -1, 126, 65, 47, -1, -105, 72, 57, -1, -101, 67, 53, -1, 101,
			13, 0, -1, -122, 46, 32, -1,
			105, 20, 0, -1, 104, 20, 0, -1, -122, 49, 18, -1, -58, 114, 77, -1, -54, 121, 76, -1, -75, 108, 57, -1,
			110, 45, 0, -1, 93, 38, 0, -1,
			-104, 106, 29, -1, -46, -82, 86, -1, -30, -58, 99, -1, -25, -48, 112, -1, -29, -48, -127, -1, -97, -111,
			84, -1, 49, 42, 0, -1, 87, 92, 60, -1,
			-61, -42, -72, -1, -109, -74, -107, -1, 79, -124, 86, -1, 83, -108, 90, -1, 70, -110, 71, -1, 71, -102, 66,
			-1, 75, -97, 63, -1, 71, -100, 55, -1,
			117, 104, 88, -1, 66, 40, 25, -1, 95, 43, 29, -1, -121, 61, 48, -1, -97, 69, 58, -1, -104, 58, 46, -1,
			-125, 41, 30, -1, 118, 36, 22, -1,
			99, 23, 7, -1, 110, 39, 17, -1, 118, 49, 20, -1, -112, 76, 41, -1, -116, 75, 33, -1, -112, 83, 36, -1, 122,
			69, 17, -1, 114, 68, 9, -1,
			-67, -104, 82, -1, -26, -55, 121, -1, -60, -84, 82, -1, -112, 124, 37, -1, -113, 125, 53, -1, -121, 119,
			67, -1, 105, 97, 61, -1, -123, -120, 109, -1,
			-97, -77, -102, -1, 118, -101, 124, -1, 86, -113, 102, -1, 75, -110, 88, -1, 68, -107, 73, -1, 69, -103,
			65, -1, 72, -100, 58, -1, 76, -95, 58, -1,
			106, 93, 84, -1, 32, 8, 0, -1, 98, 45, 37, -1, -112, 71, 64, -1, -109, 60, 53, -1, -121, 45, 37, -1, 113,
			30, 22, -1, 75, 1, 0, -1,
			106, 40, 28, -1, -80, 114, 99, -1, -114, 81, 60, -1, 88, 28, 0, -1, 70, 11, 0, -1, 111, 56, 15, -1, -70,
			-122, 87, -1, -51, -97, 110, -1,
			-49, -82, 121, -1, -60, -86, 111, -1, -118, 118, 49, -1, 74, 55, 0, -1, 55, 38, 0, -1, 91, 76, 33, -1, -88,
			-99, -127, -1, -81, -82, -103, -1,
			121, -117, 115, -1, 93, -126, 99, -1, 79, -119, 96, -1, 68, -115, 85, -1, 68, -106, 74, -1, 73, -99, 69,
			-1, 74, -102, 59, -1, 72, -106, 51, -1,
			87, 80, 70, -1, 16, 0, 0, -1, 93, 50, 43, -1, -117, 76, 69, -1, -122, 59, 54, -1, 118, 39, 34, -1, 80, 6,
			3, -1, 72, 4, 1, -1,
			111, 50, 45, -1, -55, -112, -123, -1, -80, 120, 103, -1, 116, 65, 38, -1, 73, 23, 0, -1, 100, 51, 11, -1,
			-51, -100, 113, -1, -64, -107, 106, -1,
			114, 80, 42, -1, 109, 81, 41, -1, 97, 75, 25, -1, 71, 51, 1, -1, 43, 26, 0, -1, 71, 56, 23, -1, -60, -72,
			-98, -1, -61, -61, -85, -1,
			102, 120, 94, -1, 91, -128, 95, -1, 68, 127, 83, -1, 77, -106, 94, -1, 71, -103, 77, -1, 72, -102, 69, -1,
			79, -103, 64, -1, 80, -106, 61, -1,
			79, 81, 67, -1, 52, 44, 31, -1, 81, 53, 42, -1, 102, 59, 52, -1, 118, 65, 59, -1, 105, 46, 42, -1, 59, 1,
			0, -1, 58, 0, 0, -1,
			93, 35, 33, -1, -51, -105, -115, -1, -52, -103, -120, -1, -96, 114, 88, -1, 92, 52, 16, -1, 98, 59, 16, -1,
			-55, -96, 116, -1, -127, 90, 49, -1,
			62, 27, 0, -1, 38, 7, 0, -1, 89, 63, 30, -1, 122, 99, 65, -1, 61, 42, 12, -1, 57, 41, 15, -1, -73, -80,
			-108, -1, -78, -75, -104, -1,
			96, 117, 86, -1, 97, -121, 98, -1, 68, 126, 78, -1, 87, -98, 100, -1, 79, -101, 83, -1, 72, -110, 67, -1,
			77, -111, 68, -1, 80, -112, 68, -1,
			-121, -106, -127, -1, 95, 104, 83, -1, 67, 62, 43, -1, 55, 38, 22, -1, 75, 49, 34, -1, 80, 48, 35, -1, 44,
			6, 0, -1, 41, 0, 0, -1,
			83, 34, 27, -1, -72, -121, 121, -1, -44, -89, -110, -1, -84, -123, 102, -1, 83, 49, 12, -1, 84, 52, 11, -1,
			-74, -109, 105, -1, -123, 98, 60, -1,
			-119, 103, 75, -1, 47, 15, 0, -1, 97, 69, 45, -1, -102, -126, 106, -1, 72, 54, 32, -1, 52, 40, 18, -1,
			-119, -120, 108, -1, 124, -121, 103, -1,
			68, 94, 59, -1, 95, -119, 97, -1, 85, -113, 95, -1, 82, -106, 93, -1, 76, -107, 80, -1, 76, -108, 75, -1,
			80, -109, 80, -1, 78, -113, 79, -1,
			-70, -36, -69, -1, -70, -41, -72, -1, -101, -77, -109, -1, 88, 103, 72, -1, 38, 47, 16, -1, 51, 51, 23, -1,
			20, 8, 0, -1, 35, 13, 0, -1,
			72, 35, 16, -1, -126, 89, 67, -1, -100, 119, 90, -1, -116, 107, 72, -1, 48, 20, 0, -1, 90, 62, 25, -1, -47,
			-80, -113, -1, 114, 81, 48, -1,
			-77, -110, 113, -1, 71, 40, 11, -1, 95, 67, 43, -1, -70, -95, -115, -1, -87, -104, -120, -1, 116, 109, 91,
			-1, 55, 60, 37, -1, 18, 39, 8, -1,
			45, 80, 47, -1, 94, -114, 104, -1, 96, -101, 111, -1, 80, -109, 96, -1, 75, -111, 83, -1, 76, -110, 83, -1,
			75, -112, 87, -1, 73, -114, 87, -1,
			-73, -25, -67, -1, -71, -24, -66, -1, -107, -62, -103, -1, -123, -82, -124, -1, 94, -127, 87, -1, 98, 123,
			84, -1, 64, 74, 40, -1, -122, 127, 99, -1,
			112, 87, 65, -1, 67, 35, 14, -1, 80, 48, 23, -1, 126, 96, 70, -1, -121, 109, 82, -1, 93, 67, 40, -1, 62,
			35, 8, -1, 117, 87, 59, -1,
			-93, -123, 99, -1, -94, -123, 101, -1, -82, -110, 122, -1, 125, 103, 82, -1, 50, 37, 21, -1, 66, 63, 46,
			-1, 66, 78, 54, -1, 107, -123, 104, -1,
			-105, -63, -99, -1, -105, -52, -94, -1, 95, -102, 108, -1, 68, -124, 80, -1, 72, -119, 79, -1, 83, -105,
			92, -1, 97, -89, 115, -1, 114, -72, -122, -1,
			-78, -16, -65, -1, -89, -27, -76, -1, -111, -49, -96, -1, -126, -67, -115, -1, 85, -117, 90, -1, 91, -123,
			87, -1, 96, 118, 80, -1, 84, 88, 61, -1,
			47, 34, 17, -1, 31, 7, 0, -1, 41, 12, 4, -1, 70, 41, 33, -1, 120, 93, 84, -1, 126, 103, 89, -1, 95, 73, 50,
			-1, 125, 101, 73, -1,
			-88, -118, 110, -1, 91, 61, 33, -1, 65, 41, 15, -1, 72, 54, 32, -1, 114, 103, 85, -1, -128, -127, 111, -1,
			-108, -93, -116, -1, -84, -51, -82, -1,
			-78, -30, -70, -1, -110, -52, -100, -1, 98, -99, 103, -1, 73, -122, 77, -1, 80, -115, 84, -1, 86, -107, 94,
			-1, 105, -83, 122, -1, 123, -61, -109, -1,
			-95, -28, -79, -1, -98, -30, -81, -1, -114, -44, -94, -1, 111, -75, -127, -1, 72, -119, 83, -1, 84, -120,
			87, -1, 117, -104, 112, -1, 70, 88, 62, -1,
			30, 30, 20, -1, 10, 0, 0, -1, 20, 0, 11, -1, 32, 10, 23, -1, 70, 46, 60, -1, 110, 89, 96, -1, 110, 89, 84,
			-1, 107, 86, 69, -1,
			96, 69, 48, -1, 77, 51, 26, -1, 28, 8, 0, -1, 48, 33, 10, -1, -122, -128, 106, -1, -87, -82, -104, -1, -81,
			-61, -86, -1, -71, -36, -68, -1,
			-81, -30, -73, -1, -114, -54, -106, -1, 92, -103, 96, -1, 87, -110, 88, -1, 75, -122, 78, -1, 81, -112, 91,
			-1, 106, -82, 125, -1, 105, -79, -125, -1,
			-107, -44, -91, -1, -87, -21, -69, -1, -126, -56, -106, -1, 111, -75, 127, -1, 93, -93, 103, -1, 81, -115,
			87, -1, -105, -59, -98, -1, -106, -74, -95, -1,
			79, 95, 95, -1, 18, 23, 42, -1, 25, 20, 52, -1, 34, 23, 63, -1, 46, 29, 71, -1, 39, 22, 56, -1, 25, 3, 24,
			-1, 46, 25, 30, -1,
			34, 12, 0, -1, 83, 65, 41, -1, 98, 82, 59, -1, 81, 71, 46, -1, 51, 49, 24, -1, 81, 91, 64, -1, 123, -108,
			118, -1, -90, -52, -89, -1,
			-87, -34, -80, -1, -112, -52, -104, -1, 94, -101, 98, -1, 88, -107, 90, -1, 78, -118, 84, -1, 84, -108, 96,
			-1, 102, -87, 125, -1, 100, -87, 125, -1,
			-101, -40, -84, -1, -95, -32, -79, -1, -121, -53, -102, -1, 112, -75, 127, -1, 89, -99, 98, -1, 81, -112,
			91, -1, 116, -88, -124, -1, 113, -102, -116, -1,
			67, 93, 106, -1, 30, 44, 79, -1, 42, 48, 100, -1, 63, 61, 124, -1, 46, 35, 101, -1, 16, 0, 62, -1, 26, 6,
			55, -1, 18, 0, 26, -1,
			52, 36, 37, -1, 92, 78, 65, -1, 112, 101, 83, -1, 92, 86, 64, -1, 38, 40, 16, -1, 13, 26, 0, -1, 112, -117,
			104, -1, -75, -34, -76, -1,
			-88, -34, -84, -1, -113, -53, -107, -1, 97, -95, 101, -1, 72, -118, 77, -1, 69, -122, 78, -1, 81, -108, 95,
			-1, 103, -86, 126, -1, 115, -72, -116, -1,
			-85, -28, -71, -1, -89, -30, -76, -1, -110, -48, -95, -1, 111, -81, 123, -1, 83, -110, 89, -1, 71, -125,
			80, -1, 86, -121, 106, -1, 15, 54, 51, -1,
			7, 34, 61, -1, 31, 48, 100, -1, 64, 74, -111, -1, 65, 67, -106, -1, 52, 46, -120, -1, 57, 43, -125, -1, 72,
			51, -126, -1, 33, 14, 69, -1,
			59, 47, 67, -1, 92, 84, 82, -1, 90, 81, 74, -1, 67, 64, 49, -1, 44, 48, 25, -1, 69, 86, 54, -1, -105, -75,
			-113, -1, -55, -12, -57, -1,
			-83, -29, -81, -1, -121, -60, -117, -1, 86, -104, 91, -1, 69, -119, 76, -1, 65, -122, 77, -1, 78, -109, 93,
			-1, 97, -91, 118, -1, 105, -84, -128, -1,
			-74, -18, -63, -1, -80, -23, -68, -1, -113, -54, -100, -1, 115, -81, 124, -1, 94, -104, 101, -1, 76, -127,
			85, -1, 116, -98, -118, -1, 19, 52, 57, -1,
			4, 27, 61, -1, 26, 42, 101, -1, 72, 80, -95, -1, 72, 75, -86, -1, 77, 70, -82, -1, 75, 62, -89, -1, 55, 35,
			-122, -1, 43, 27, 102, -1,
			34, 26, 65, -1, 37, 33, 48, -1, 46, 39, 46, -1, 56, 53, 48, -1, 87, 91, 76, -1, -124, -108, 121, -1, -78,
			-48, -84, -1, -66, -23, -68, -1,
			-84, -31, -83, -1, -107, -48, -106, -1, 89, -101, 93, -1, 84, -102, 91, -1, 70, -115, 81, -1, 69, -116, 84,
			-1, 81, -108, 97, -1, 90, -100, 108, -1,
			-91, -33, -81, -1, -104, -46, -94, -1, -115, -58, -103, -1, 126, -74, -119, -1, 103, -104, 107, -1, 100,
			-115, 109, -1, 85, 115, 105, -1, 36, 55, 69, -1,
			23, 35, 75, -1, 23, 29, 91, -1, 51, 54, -123, -1, 81, 80, -82, -1, 85, 78, -72, -1, 74, 62, -84, -1, 68,
			52, -97, -1, 57, 44, -124, -1,
			32, 24, 83, -1, 9, 3, 39, -1, 6, 0, 21, -1, 39, 33, 43, -1, 111, 113, 108, -1, -78, -63, -84, -1, -98, -68,
			-102, -1, -86, -47, -91, -1,
			-89, -41, -91, -1, -110, -52, -110, -1, 81, -109, 83, -1, 76, -109, 81, -1, 67, -114, 77, -1, 70, -115, 81,
			-1, 67, -122, 80, -1, 65, -126, 76, -1,
			121, -75, -126, -1, -112, -52, -103, -1, -125, -68, -113, -1, -109, -56, -98, -1, -127, -82, -121, -1, 99,
			-123, 108, -1, 0, 17, 15, -1, 38, 46, 65, -1,
			42, 41, 83, -1, 41, 37, 98, -1, 44, 37, 114, -1, 52, 46, -122, -1, 68, 60, -93, -1, 76, 67, -80, -1, 72,
			60, -88, -1, 70, 60, -100, -1,
			46, 39, 109, -1, 72, 63, 116, -1, 53, 41, 79, -1, 37, 29, 52, -1, 46, 47, 49, -1, 72, 84, 70, -1, 110,
			-120, 107, -1, -122, -86, -124, -1,
			-114, -68, -115, -1, -115, -60, -115, -1, 123, -69, 126, -1, 105, -80, 110, -1, 72, -109, 82, -1, 75, -109,
			84, -1, 82, -109, 89, -1, 72, -121, 80, -1,
			-89, -24, -78, -1, -102, -40, -91, -1, -89, -30, -76, -1, -70, -17, -59, -1, -83, -39, -74, -1, 105, -119,
			114, -1, 15, 29, 29, -1, 41, 42, 62, -1,
			59, 48, 90, -1, 67, 52, 109, -1, 64, 52, 118, -1, 61, 48, -128, -1, 65, 54, -108, -1, 59, 51, -104, -1, 50,
			43, -110, -1, 58, 51, -110, -1,
			60, 49, 127, -1, 62, 50, 112, -1, 79, 63, 110, -1, 73, 62, 92, -1, 27, 26, 34, -1, 58, 70, 60, -1, -117,
			-93, -117, -1, -70, -36, -69, -1,
			-74, -33, -75, -1, -75, -25, -76, -1, -96, -35, -94, -1, 122, -64, -127, -1, 75, -107, 86, -1, 75, -109,
			84, -1, 82, -109, 89, -1, 77, -119, 83, -1,
			-92, -29, -82, -1, -79, -16, -69, -1, -86, -27, -73, -1, -107, -54, -96, -1, -81, -37, -73, -1, -123, -94,
			-116, -1, 41, 51, 50, -1, 23, 20, 39, -1,
			43, 27, 64, -1, 50, 30, 81, -1, 59, 39, 98, -1, 64, 47, 117, -1, 72, 57, -116, -1, 71, 58, -108, -1, 64,
			53, -109, -1, 69, 59, -110, -1,
			67, 53, -126, -1, 58, 42, 104, -1, 43, 27, 74, -1, 32, 21, 51, -1, 18, 18, 28, -1, 34, 45, 37, -1, 116,
			-119, 118, -1, -84, -53, -84, -1,
			-73, -35, -72, -1, -88, -41, -87, -1, -76, -17, -71, -1, 124, -64, -123, -1, 73, -112, 84, -1, 70, -115,
			83, -1, 80, -111, 91, -1, 88, -108, 96, -1,
			-79, -23, -72, -1, -81, -25, -74, -1, -104, -48, -95, -1, -100, -49, -92, -1, -93, -51, -89, -1, -81, -53,
			-78, -1, 23, 34, 28, -1, 48, 44, 58, -1,
			45, 28, 60, -1, 43, 20, 64, -1, 49, 25, 77, -1, 58, 34, 96, -1, 63, 41, 114, -1, 66, 44, 126, -1, 61, 42,
			124, -1, 62, 43, 122, -1,
			68, 49, 115, -1, 34, 17, 69, -1, 17, 5, 43, -1, 14, 9, 31, -1, 4, 7, 14, -1, 16, 27, 19, -1, 48, 69, 52,
			-1, 103, -123, 107, -1,
			-82, -45, -78, -1, -99, -53, -92, -1, -75, -19, -66, -1, -128, -64, -116, -1, 71, -116, 86, -1, 76, -111,
			91, -1, 94, -99, 110, -1, 108, -86, 123, -1,
			-79, -27, -73, -1, -90, -38, -84, -1, -109, -57, -103, -1, -83, -36, -80, -1, -85, -44, -86, -1, -59, -30,
			-60, -1, 0, 11, 1, -1, 46, 45, 53, -1,
			24, 10, 35, -1, 24, 2, 38, -1, 38, 13, 55, -1, 52, 25, 76, -1, 54, 26, 87, -1, 53, 24, 91, -1, 46, 16, 86,
			-1, 41, 12, 76, -1,
			49, 26, 78, -1, 13, 0, 36, -1, 10, 1, 28, -1, 11, 11, 23, -1, 0, 3, 2, -1, 0, 13, 3, -1, 0, 22, 4, -1, 58,
			88, 64, -1,
			-88, -51, -82, -1, -86, -40, -77, -1, -86, -31, -73, -1, -117, -55, -104, -1, 67, -123, 85, -1, 78, -110,
			97, -1, 108, -85, -128, -1, 119, -74, -117, -1,
			-66, -19, -61, -1, -76, -29, -71, -1, -72, -23, -68, -1, -79, -33, -79, -1, -78, -39, -83, -1, -59, -29,
			-63, -1, 0, 13, 0, -1, 18, 22, 23, -1,
			13, 7, 21, -1, 11, 0, 22, -1, 21, 1, 29, -1, 26, 2, 38, -1, 27, 0, 44, -1, 33, 0, 50, -1, 38, 0, 52, -1,
			38, 2, 50, -1,
			29, 5, 39, -1, 9, 0, 19, -1, 6, 4, 15, -1, 2, 11, 10, -1, 1, 18, 8, -1, 0, 15, 0, -1, 16, 43, 24, -1, 77,
			109, 85, -1,
			-98, -59, -88, -1, -75, -29, -65, -1, -92, -37, -77, -1, -111, -49, -96, -1, 67, -126, 85, -1, 81, -109,
			101, -1, 116, -75, -117, -1, 126, -65, -107, -1,
			-78, -30, -70, -1, -72, -24, -66, -1, -76, -28, -70, -1, -72, -25, -71, -1, -75, -33, -79, -1, -67, -32,
			-72, -1, 78, 100, 79, -1, 14, 25, 17, -1,
			0, 2, 7, -1, 3, 0, 9, -1, 10, 0, 13, -1, 23, 6, 25, -1, 43, 17, 46, -1, 62, 27, 60, -1, 66, 22, 58, -1, 58,
			19, 50, -1,
			58, 35, 53, -1, 13, 3, 11, -1, 0, 2, 1, -1, 0, 10, 0, -1, 0, 14, 0, -1, 16, 45, 23, -1, 70, 99, 77, -1,
			-125, -91, -116, -1,
			-78, -38, -65, -1, -66, -19, -53, -1, -81, -26, -66, -1, -112, -53, -99, -1, 69, -125, 84, -1, 80, -113,
			98, -1, 104, -87, 127, -1, 105, -84, -127, -1,
			-79, -24, -66, -1, -88, -32, -77, -1, -77, -24, -68, -1, -82, -30, -78, -1, -75, -29, -76, -1, -66, -28,
			-69, -1, -106, -76, -102, -1, 44, 67, 51, -1,
			39, 54, 47, -1, 32, 41, 38, -1, 20, 22, 17, -1, 20, 14, 14, -1, 51, 34, 44, -1, 82, 54, 69, -1, 90, 48, 68,
			-1, 83, 44, 62, -1,
			78, 57, 62, -1, 12, 7, 3, -1, 10, 22, 10, -1, 29, 52, 32, -1, 16, 45, 23, -1, 67, 101, 77, -1, -126, -92,
			-116, -1, -77, -41, -67, -1,
			-71, -28, -56, -1, -81, -31, -66, -1, -80, -23, -66, -1, -120, -61, -109, -1, 72, -122, 85, -1, 84, -108,
			98, -1, 104, -84, 125, -1, 98, -89, 122, -1,
			-78, -19, -63, -1, -86, -27, -73, -1, -84, -28, -73, -1, -81, -27, -76, -1, -79, -31, -79, -1, -77, -34,
			-77, -1, -66, -33, -62, -1, -115, -88, -107, -1,
			-117, -97, -109, -1, 112, -128, 117, -1, 57, 65, 52, -1, 10, 10, 0, -1, 13, 1, 1, -1, 36, 11, 17, -1, 47,
			7, 18, -1, 47, 10, 18, -1,
			34, 14, 13, -1, 10, 8, 0, -1, 56, 70, 53, -1, 118, -110, 123, -1, 120, -104, -128, -1, -126, -90, -116, -1,
			-76, -42, -66, -1, -63, -24, -51, -1,
			-73, -30, -58, -1, -92, -42, -79, -1, -80, -23, -66, -1, -122, -63, -111, -1, 70, -124, 81, -1, 83, -109,
			97, -1, 110, -78, -127, -1, 105, -82, 127, -1,
	};

	public AndroidOpenGLGestion() {
		super(title);
	}

	public AndroidOpenGLGestion(boolean fullscreen) {
		super(title, fullscreen);

		z = 0.0f;
	}

	@Override
	protected void mainloopExt() {

		EngineZildo.extraSpeed = 1;
		if (kbHandler.isKeyDown(LwjglKeyboardHandler.KEY_LSHIFT)) {
			EngineZildo.extraSpeed = 2;
		}
	}

	@Override
	public void render(boolean p_clientReady) {

		// Clear the screen and the depth buffer
		gl10.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); 

		gl10.glLoadIdentity(); // Reset The Projection Matrix

		// invert the y axis, down is positive
		float zz = z * 5.0f;
		if (zz != 0.0f) {
			gl10.glTranslatef(-zoomPosition.getX() * zz, zoomPosition.getY() * zz, 0.0f);
		}
		gl10.glScalef(1 + zz, -1 - zz, 1);
		if (ClientEngineZildo.filterCommand != null) {
			ClientEngineZildo.filterCommand.doPreFilter();
		}

		clientEngineZildo.renderFrame(awt);
		if (!p_clientReady && !awt) {
			clientEngineZildo.renderMenu();
		}

		if (ClientEngineZildo.filterCommand != null) {
			ClientEngineZildo.filterCommand.doFilter();
			ClientEngineZildo.filterCommand.doPostFilter();
		}

		if (framerate != 0) {
			//Display.sync(framerate);
		}
	}

	@Override
	protected void cleanUpExt() {
		ClientEngineZildo.cleanUp();
	}


	/**
     * Switch display with given fullscreen mode (TRUE or FALSE=windowed)
     * @param p_fullscreen
     */
	@Override
    public void switchFullscreen(boolean p_fullscreen) {
            fullscreen = p_fullscreen;
            // Always fullscreen on android !
	}

	@Override
	public void initDisplay() throws Exception {

	}

	@Override
	public void init() {
        initGL();
	}

	private void initGL() {
		gl10.glEnable(GL11.GL_TEXTURE_2D); // Enable Texture Mapping
		gl10.glShadeModel(GL11.GL_SMOOTH); // Enable Smooth Shading
		gl10.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Black Background
		gl10.glClearDepthf(1.0f); // Depth Buffer Setup
		gl10.glEnable(GL11.GL_DEPTH_TEST); // Enables Depth Testing
		gl10.glDepthFunc(GL11.GL_LEQUAL); // The Type Of Depth Testing To Do

        // initProjectionScene();

		gl10.glEnable(GL11.GL_CULL_FACE);

        ByteBuffer temp = ByteBuffer.allocateDirect(16);
        temp.order(ByteOrder.nativeOrder());
        gl10.glLightfv(GL11.GL_LIGHT1, GL11.GL_AMBIENT, (FloatBuffer) temp
                        .asFloatBuffer().put(lightAmbient).flip()); // Setup The Ambient
                                                                                                                // Light
        gl10.glLightfv(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, (FloatBuffer) temp
                        .asFloatBuffer().put(lightDiffuse).flip()); // Setup The Diffuse
                                                                                                                // Light
        gl10.glLightfv(GL11.GL_LIGHT1, GL11.GL_POSITION, (FloatBuffer) temp
                        .asFloatBuffer().put(lightPosition).flip()); // Position The
                                                                                                                        // Light
        gl10.glEnable(GL11.GL_LIGHT1); // Enable Light One

        // GL11.glEnable(GL11.GL_LIGHTING);

        //Display.setVSyncEnabled(true);

	}

	@Override
	public void cleanUp() {
		cleanUpExt();
        //Display.destroy();
        //Mouse.destroy();
	}

	@Override
	public boolean mainloop() {
		boolean done = false;
        //if (Display.isCloseRequested()) { // Exit if window is closed
        //        done = true;
        //}
        mainloopExt();

        return done;
	}

	@Override
	public double getTimeInSeconds() {
		return ZUtils.getTime();
	}
	
}
