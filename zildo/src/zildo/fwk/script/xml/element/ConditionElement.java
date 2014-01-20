package zildo.fwk.script.xml.element;

import java.util.List;

import org.w3c.dom.Element;

import zildo.fwk.script.model.ZSCondition;
import zildo.fwk.script.model.ZSSwitch;
import zildo.fwk.script.xml.ScriptReader;

public class ConditionElement extends AnyElement {

	public String mapName;
	ZSSwitch expression;	// No expression means it's always verified
	List<LanguageElement> actions;
	
	@Override
	@SuppressWarnings("unchecked")
	public void parse(Element p_elem) {
		xmlElement = p_elem;
		
		mapName = readAttribute("name");
		String strExp = readAttribute("exp");
		expression = strExp == null ? null : ZSSwitch.parseForScript(strExp);	// 1 will be the right value
		actions = (List<LanguageElement>) ScriptReader.parseNodes(p_elem);
	}

	public boolean isRight() {
		return expression == null || expression.evaluate().equals(ZSCondition.TRUE);
	}
	
	public List<LanguageElement> getActions() {
		return actions;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("map:").append(mapName);
		sb.append(", if:").append(expression);
		sb.append(", then:").append(actions);
		return sb.toString();
	}
}
