class atmostone{
    public String toXMLString() {
        //return "atmostone" +array2String( children().toArray(), "," );
        StringBuffer str=new StringBuffer();
        Object obj[] = children().toArray();

        str.append("<atmostone>");
        for(int i=0;i< obj.length;i++){
            str.append( ((node)obj[i]).toXMLString());
        }
        str.append("</atmostone>");

        return str.toString();
    }
}