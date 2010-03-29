
import pkjab.de.uni_passau.fim.pkjab.model.tags.AbstractXMLTag;
import pkjab.de.uni_passau.fim.pkjab.model.tags.XMLTag;
import pkjab.de.uni_passau.fim.pkjab.util.Stack;

public class InvalidXMLException extends UnknownXMLException {

	private static final long serialVersionUID = -7017075118406282850L;

	private final XMLTag closedTag;
	
	public InvalidXMLException(final Stack xmlStack, final XMLTag closedTag) {
		super(xmlStack);
		this.closedTag = closedTag;
	}
}
