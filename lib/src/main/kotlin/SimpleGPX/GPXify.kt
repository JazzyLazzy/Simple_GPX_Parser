package SimpleGPX

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import java.io.StringReader
import java.lang.Double
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.String
import kotlin.collections.ArrayList
import kotlin.toString


fun GPXify(gpxString:String):GPX{
    val document = documentBuilder(gpxString);
    return GPXifyDocument(document);
}

internal fun GPXifyDocument(document: Document): GPX {
    document.documentElement.normalize();
    val rootElement: Element = document.documentElement
    val creator: String = rootElement.getAttribute("creator")
    val version: String = rootElement.getAttribute("version")

    val waypoints: ArrayList<GPXWaypoint> = ArrayList()
    val waypoint_nodeList: NodeList = rootElement.getElementsByTagName("wpt")
    for (i in 0 until waypoint_nodeList.length) {
        val waypoint_node = waypoint_nodeList.item(i);
        var name: String? = null
        var desc: String? = null
        var sym: String? = null
        for (j in 0 until waypoint_node.childNodes.length) {
            if (waypoint_node.childNodes.item(j).nodeName == "name") {
                name = waypoint_node.childNodes.item(j).nodeValue
            } else if (waypoint_node.childNodes.item(j).nodeName == "desc") {
                desc = waypoint_node.childNodes.item(j).nodeValue
            } else if (waypoint_node.childNodes.item(j).nodeName == "sym") {
                sym = waypoint_node.childNodes.item(j).nodeValue
            }
        }
        val waypoint = GPXWaypoint(
            Double.valueOf(waypoint_node.attributes.getNamedItem("lat").nodeValue),
            Double.valueOf(waypoint_node.attributes.getNamedItem("lon").nodeValue), 0.0,
            System.currentTimeMillis(), name.toString(), desc.toString(), sym.toString()
        )

        waypoints.add(waypoint)

    }

    /*Ok, let's try to explain this bogusness.
    GPX uses an array of tracks.
    A track is an array of track segments.
    A track segment is an array of track points.*/
    val trk_nodeList: NodeList = rootElement.getElementsByTagName("trk");
    val trkpts = ArrayList<TrackPoint>();
    val trkseg = trkseg(trkpts);
    val trackName:String? = null;
    val track = trk(trackName, ArrayList());
    val tracks = ArrayList<trk>();
    for (i in 0 until trk_nodeList.length) {
        val trk_node = trk_nodeList.item(i);
        for (j in 0 until trk_node.childNodes.length) {
            val trkseg_node = trk_node.childNodes.item(j)
            for (k in 0 until trkseg_node.childNodes.length) {
                val trkpt_node = trkseg_node.childNodes.item(k)
                val latitude = Double.valueOf(trkpt_node.attributes.getNamedItem("lat").nodeValue)
                val longitude = Double.valueOf(trkpt_node.attributes.getNamedItem("lon").nodeValue)
                val elevation = Double.valueOf(trkpt_node.attributes.item(0).nodeValue)
                val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.ss")
                val time = LocalDateTime.parse(trkpt_node.attributes.item(1).nodeValue, formatter)
                val trkpt = TrackPoint(
                    latitude,
                    longitude,
                    elevation,
                    time
                )
                trkpts.add(trkpt);
            }
            trkseg.trkpts = trkpts
            track.trksegs.add(trkseg)
        }
        tracks.add(track)
    }

    return GPX(creator, version, waypoints, tracks)
}
private fun documentBuilder(gpxString:String):Document{
    val documentBuilderFactory = DocumentBuilderFactory.newInstance()
    documentBuilderFactory.isNamespaceAware = true
    val documentBuilder = documentBuilderFactory.newDocumentBuilder()
    val inputSource = InputSource(StringReader(gpxString))
    return documentBuilder.parse(inputSource);
}