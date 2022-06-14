package SimpleGPX

import org.w3c.dom.Element
import org.w3c.dom.Node
import java.io.FileOutputStream
import java.io.OutputStream
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.stream.XMLOutputFactory
import javax.xml.stream.XMLStreamWriter

open class SimpleGPXWriter(fileName:String) : SimpleGPXFile(fileName){

    protected lateinit var gpx:GPX

    fun writeGPX(){
        try {
            isGPXFile()
        }catch(error:NotGPXFileException){
            error.printStackTrace()
        }
        /*var outputStream: OutputStream = FileOutputStream(fileName)
        var xmlOutputFactory: XMLOutputFactory = XMLOutputFactory.newInstance()
        var xmlStreamWriter: XMLStreamWriter = xmlOutputFactory.createXMLStreamWriter(outputStream)

        xmlStreamWriter.writeStartDocument()
        xmlStreamWriter.setDefaultNamespace("http://www.topografix.com/GPX/1/0")
        xmlStreamWriter.setPrefix("xsi", "http://www.w3.org/2001/XMLSchema-instance")
        xmlStreamWriter.writeStartElement("http://www.topografix.com/GPX/1/0", "gpx");
        xmlStreamWriter.writeDefaultNamespace("http://www.topografix.com/GPX/1/0")
        xmlStreamWriter.writeNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance")
        xmlStreamWriter.writeAttribute("xsi:schemaLocation", "http://www.topografix.com/GPX/1/0 http://www.topografix.com/GPX/1/0/gpx.xsd")
        xmlStreamWriter.writeAttribute("creator", gpx.creator)
        xmlStreamWriter.writeAttribute("version", gpx.version)
        xmlStreamWriter.writeEndElement()
        xmlStreamWriter.flush()
        xmlStreamWriter.close()*/

        val documentBuilderFactory = DocumentBuilderFactory.newInstance()
        documentBuilderFactory.isNamespaceAware = true
        val documentBuilder = documentBuilderFactory.newDocumentBuilder()
        val document = documentBuilder.newDocument()

        val rootElement:Element = document.createElement("gpx")
        rootElement.setAttribute("xmlns", "http://www.topografix.com/GPX/1/0")

        rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance")
        rootElement.setAttribute("xsi:schemaLocation", "http://www.topografix.com/GPX/1/0 http://www.topografix.com/GPX/1/0/gpx.xsd")
        rootElement.setAttribute("creator", gpx.creator)
        rootElement.setAttribute("version", gpx.version)
        document.appendChild(rootElement)


        for (waypoint in gpx.waypoints){
            rootElement.appendChild(createWaypoint(document, waypoint))
        }

        for (track in gpx.tracks){
            rootElement.appendChild(createTrack(document, track))
        }

        writeToFile(document)
    }

    fun connectGPX(gpx:GPX){
        this.gpx = gpx
    }

    fun getGPX():GPX{
        return gpx
    }
}