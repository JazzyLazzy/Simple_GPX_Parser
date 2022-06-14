package SimpleGPX


import org.w3c.dom.NodeList

class GPX {

    var creator: String = String()
    var version: String = String()
    var waypoints:ArrayList<GPXWaypoint> = ArrayList()
    var tracks:ArrayList<trk> = ArrayList()

    constructor()

    constructor(creator:String, version:String){
        this.creator = creator
        this.version = version
    }

    constructor(creator:String, version:String, waypoints:ArrayList<GPXWaypoint>, tracks:ArrayList<trk>){
        this.creator = creator
        this.version = version
        this.waypoints = waypoints
        this.tracks = tracks
    }

    fun addWaypoint(waypoint:GPXWaypoint){
        waypoints.add(waypoint)
    }

    fun addWaypoints(waypoints: ArrayList<GPXWaypoint>){
        for(i in waypoints){
            this.waypoints.add(i)
        }
    }

    fun removeWaypoint(){

    }

    fun removeWaypoints(){

    }

    fun addTrack(track:trk){
        tracks.add(track)
    }

    fun addTracks(tracks: ArrayList<trk>){
        for (track in tracks){
            this.tracks.add(track)
        }
    }

    fun remove_allWaypoints(){
        for(i in this.waypoints){
            this.waypoints.remove(i)
        }
    }

    fun remove_allTracks(){
        for (i in tracks){
            tracks.remove(i)
        }
    }
}
