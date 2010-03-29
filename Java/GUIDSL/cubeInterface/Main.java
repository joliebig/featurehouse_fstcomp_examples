class Main {

    //Gets gramamr and constraints as XML and joins them up
    // This is the interface that external applicataions will use
    public static String getModelXML(){
        PrintXML printer = new PrintXML();

        String grammar = printer.getXMLString();
        String constraints = ESList.getCTableXML();

        return XMLUtils.formatXMLStr("<model>"+grammar+constraints+"</model>");
    }

}