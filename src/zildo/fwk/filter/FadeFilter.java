package zildo.fwk.filter;

import org.lwjgl.opengl.GL11;

public class FadeFilter extends ScreenFilter {

	
	public void renderFilter()
	{
	}
	
	public void preFilter() {
		float coeff=1.0f - ((float) getFadeLevel() / 256.0f);
		GL11.glColor4f(coeff, coeff, coeff, 1.0f);
	}
}
