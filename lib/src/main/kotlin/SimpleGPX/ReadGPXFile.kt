package SimpleGPX

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory

fun readGPXFile(fileName:String){
    val file = File(fileName);
    val dbf = DocumentBuilderFactory.newInstance();
    val db = dbf.newDocumentBuilder();
    val doc = db.parse(file);
    doc.documentElement.normalize();
    val creator = doc.attributes.getNamedItem("creator");
    println(creator);
}