package SimpleGPX;

import java.io.File;
import java.io.IOException
import java.io.StringWriter
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult


fun Stringify(fileName:String):String{
    val file = File(fileName);
    val dbf = DocumentBuilderFactory.newInstance();
    val db = dbf.newDocumentBuilder();
    val doc = db.parse(file);
    doc.documentElement.normalize();
    val tf = TransformerFactory.newInstance()
    val transformer = tf.newTransformer()
    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes")
    val writer = StringWriter()
    transformer.transform(DOMSource(doc), StreamResult(writer));
    val output = writer.buffer.toString().replace("\n|\r".toRegex(), "");

    return output;
}

fun Stringify(gpx:GPX, fileName: String):String{
    val bogusGPXWriter = SimpleGPXWriter(fileName);
    bogusGPXWriter.createNewFile()
        //Log.d("filefolder","found")

    bogusGPXWriter.connectGPX(gpx)
    bogusGPXWriter.writeGPX()
    return Stringify(fileName)
}
