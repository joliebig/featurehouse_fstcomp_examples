package notepad;

class Actions {
	//to cut the selected text
	public void cuT(){
		n.getTextArea().cut();
	}
	//to copy the selected text
	public void copY(){
		n.getTextArea().copy();
	}
	//to paste the selected text
	public void pastE(){
		n.getTextArea().paste();
	}
	//to select all the text
	public void selectALL(){
		n.getTextArea().selectAll();
	}
}
