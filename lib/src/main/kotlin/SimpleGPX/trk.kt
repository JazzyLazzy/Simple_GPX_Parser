package SimpleGPX

class trk {

    var name:String? = null
    var trksegs:ArrayList<trkseg> = ArrayList()

    constructor()
    constructor(name:String?){
        this.name = name
    }
    constructor(name:String?, trksegs: ArrayList<trkseg>){
        this.name = name
        this.trksegs = trksegs
    }

    fun createNewTrack(trkPoints:ArrayList<TrackPoint>){
        val trkseg = trkseg(trkPoints)
        trksegs.add(trkseg)
    }

}

fun createNewTrack(trkPoints:ArrayList<TrackPoint>):trk{
    val trkseg = trkseg(trkPoints)
    val trksegs = ArrayList<trkseg>()
    trksegs.add(trkseg)
    return trk("trk_un", trksegs)
}