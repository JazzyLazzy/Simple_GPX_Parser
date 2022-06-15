package SimpleGPX

import org.xml.sax.InputSource
import java.io.ByteArrayInputStream
import java.io.StringReader
import org.w3c.dom.Document;
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.lang.Double
import javax.xml.parsers.DocumentBuilderFactory


fun GPXify(gpxString:String):GPX{
    val document = documentBuilder(gpxString);
    return GPXifyDocument(document);
}

internal fun GPXifyDocument(document: Document):GPX{
    document.documentElement.normalize();
    val rootElement: Element = document.documentElement
    val creator:String = rootElement.getAttribute("creator")
    val version:String = rootElement.getAttribute("version")

    var waypoints:ArrayList<GPXWaypoint> = ArrayList()
    var tracks:ArrayList<trk> = ArrayList()
    val waypoint_nodeList: NodeList = rootElement.getElementsByTagName("wpt")
    for (i in 0 until waypoint_nodeList.length){
        val waypoint_node = waypoint_nodeList.item(i);
        var name:String? = null
        var desc:String? = null
        var sym:String? = null
        for (j in 0 until waypoint_node.childNodes.length){
            if (waypoint_node.childNodes.item(j).nodeName == "name"){
                name = waypoint_node.childNodes.item(j).nodeValue
            }else if (waypoint_node.childNodes.item(j).nodeName == "desc"){
                desc = waypoint_node.childNodes.item(j).nodeValue
            }else if (waypoint_node.childNodes.item(j).nodeName == "sym"){
                sym = waypoint_node.childNodes.item(j).nodeValue
            }
        }
        var waypoint = GPXWaypoint(
            Double.valueOf(waypoint_node.attributes.getNamedItem("lat").nodeValue),
            Double.valueOf(waypoint_node.attributes.getNamedItem("lon").nodeValue), 0.0,
            System.currentTimeMillis(), name.toString(), desc.toString(), sym.toString())

        waypoints.add(waypoint)

    }
    val gpx = GPX(creator, version, waypoints, tracks)
    return gpx
}
private fun documentBuilder(gpxString:String):Document{
    val documentBuilderFactory = DocumentBuilderFactory.newInstance()
    documentBuilderFactory.isNamespaceAware = true
    val documentBuilder = documentBuilderFactory.newDocumentBuilder()
    val inputSource = InputSource(StringReader(gpxString))
    return documentBuilder.parse(inputSource);
}