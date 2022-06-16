package SimpleGPX

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.lang.Double.valueOf
import javax.xml.XMLConstants
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.stream.XMLOutputFactory
import javax.xml.stream.XMLStreamWriter
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import javax.xml.validation.SchemaFactory


open class SimpleGPXFile(var fileName: String) : File(fileName){

    override fun createNewFile(): Boolean {
        try {
            isGPXFile()
        }catch(error: NotGPXFileException){
            return false
        }
        return super.createNewFile()
    }

    override fun mkdir(): Boolean {
        return super.mkdir()
    }

    fun deleteGPXFile():Boolean {
        try {
            isGPXFile()
        } catch (error: NotGPXFileException) {
            error.printStackTrace()
            return false
        }

        var file = File(fileName)
        if (file.exists()) {
            file.delete()
            return true
        }else{
            return false
        }
    }

    fun readGPXFile():Boolean{

        val dbf: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
        try {

            // optional, but recommended
            // process XML securely, avoid attacks like XML External Entities (XXE)
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            // parse XML file
            val db:DocumentBuilder = dbf.newDocumentBuilder ();

            val doc:Document = db.parse(File(fileName));

            // optional, but recommended
            // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.documentElement.normalize();
        }
        catch (e:IOException) {
            e.printStackTrace();
        }
        return true
    }

    fun writeGPXFile():Boolean{

        var outputStream:OutputStream = FileOutputStream(fileName)
        var xmlOutputFactory:XMLOutputFactory = XMLOutputFactory.newInstance()
        var xmlStreamWriter:XMLStreamWriter = xmlOutputFactory.createXMLStreamWriter(outputStream)

        xmlStreamWriter.writeStartDocument()
        xmlStreamWriter.setDefaultNamespace("http://www.topografix.com/GPX/1/0")
        xmlStreamWriter.setPrefix("xsi", "http://www.w3.org/2001/XMLSchema-instance")
        xmlStreamWriter.writeStartElement("http://www.topografix.com/GPX/1/0", "gpx");
        xmlStreamWriter.writeDefaultNamespace("http://www.topografix.com/GPX/1/0")
        xmlStreamWriter.writeNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance")
        xmlStreamWriter.writeAttribute("xsi:schemaLocation", "http://www.topografix.com/GPX/1/0 http://www.topografix.com/GPX/1/0/gpx.xsd")
        xmlStreamWriter.writeAttribute("creator", "Simplegpxparser.kt")
        xmlStreamWriter.writeAttribute("version", "1.1")
        xmlStreamWriter.writeEndElement()
        xmlStreamWriter.flush()
        xmlStreamWriter.close()

        return true

    }

    fun addWaypoint(waypoint:GPXWaypoint){

        val document = buildDocument()

        document.documentElement.normalize();
        val rootElement:Element = document.documentElement
        println("Root element: " + rootElement.nodeName);

        rootElement.appendChild(createWaypoint(document, waypoint))

        writeToFile(document)
    }

    fun addWaypoints(waypoints:ArrayList<GPXWaypoint>){

        val document = buildDocument()
        document.documentElement.normalize();
        val rootElement:Element = document.documentElement

        for (waypoint in waypoints){
            rootElement.appendChild(createWaypoint(document, waypoint))
        }

        writeToFile(document)
    }

    fun removeWaypoint(location: GPXParserLocation){

        val document = buildDocument()
        document.documentElement.normalize()
        val rootElement:Element = document.documentElement

        val waypoints:NodeList = rootElement.getElementsByTagName("wpt")

        for (i in 0 until waypoints.length){
            if (check_wpt_to_loc(waypoints.item(i), location)) {
                rootElement.removeChild(waypoints.item(i))
                break
            }
        }
        writeToFile(document)
    }

    fun remove_allWaypoints(location: GPXParserLocation){

        val document = buildDocument()
        document.documentElement.normalize()
        val rootElement:Element = document.documentElement

        val waypoints:NodeList = rootElement.getElementsByTagName("wpt")

        for (i in 0 until waypoints.length){
            if (check_wpt_to_loc(waypoints.item(i), location)) {
                rootElement.removeChild(waypoints.item(i))
            }
        }
        writeToFile(document)
    }

    fun removeWaypoint(fileName:String, locations:ArrayList<GPXParserLocation>){}

    fun removeWaypoints_by_name(fileName:String){

    }

    protected fun createWaypoint(document:Document, waypoint:GPXWaypoint):Element{
        val newWaypoint:Element = document.createElement("wpt")
        newWaypoint.setAttribute("lon", waypoint.longitude.toString())
        newWaypoint.setAttribute("lat", waypoint.latitude.toString())
        if (waypoint.name != null){
            val wptName = document.createElement("name")
            wptName.appendChild(document.createTextNode(waypoint.name))
            newWaypoint.appendChild(wptName)
        }
        if (waypoint.desc != null){
            val wptDesc = document.createElement("desc")
            wptDesc.appendChild(document.createTextNode(waypoint.desc))
            newWaypoint.appendChild(wptDesc)
        }

        return newWaypoint
    }

    protected fun buildDocument(): Document {
        val documentBuilderFactory = DocumentBuilderFactory.newInstance()
        documentBuilderFactory.isNamespaceAware = true
        documentBuilderFactory.isIgnoringElementContentWhitespace = true
        val documentBuilder = documentBuilderFactory.newDocumentBuilder()
        return documentBuilder.parse(fileName)
    }

    protected fun writeToFile(document: Document){
        val source = DOMSource(document)
        val transformerFactory = TransformerFactory.newInstance()
        val transformer: Transformer = transformerFactory.newTransformer()
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        val result = StreamResult(fileName)
        transformer.transform(source, result)
    }

    private fun check_wpt_to_loc(waypoint:Node, location: GPXParserLocation):Boolean{
        return(valueOf(waypoint.attributes.getNamedItem("lat").nodeValue) == (location.latitude) &&
                valueOf(waypoint.attributes.getNamedItem("lon").nodeValue) == (location.longitude))
    }

    protected fun isGPXFile(){
        if(!fileName.endsWith(".gpx")){
            throw NotGPXFileException("File is not GPX file.")
        }
    }

    protected fun createTrack(document:Document, track:trk):Element{
        val trkElement = document.createElement("trk")
        val trackname = document.createElement("name")
        trackname.appendChild(document.createTextNode(track.name))
        trkElement.appendChild(trackname)
        for (trkseg in track.trksegs) {
            val trksegElement = document.createElement("trkseg")
            for (trkpt in trkseg.trkpts){
                val trkptElement = document.createElement("trkpt")
                trkptElement.setAttribute("lat", trkpt.latitude.toString())
                trkptElement.setAttribute("lon", trkpt.longitude.toString())
                val track_point_ele = document.createElement("ele")
                track_point_ele.appendChild(document.createTextNode(trkpt.elevation.toString()))
                val track_point_time = document.createElement("time")
                track_point_time.appendChild(document.createTextNode(trkpt.time.toString()))
                trkptElement.appendChild(track_point_ele)
                trkptElement.appendChild(track_point_time)
                trksegElement.appendChild(trkptElement)
            }
            trkElement.appendChild(trksegElement)
        }
        return trkElement
    }

}

fun main() {
    println("hellloworld")
}