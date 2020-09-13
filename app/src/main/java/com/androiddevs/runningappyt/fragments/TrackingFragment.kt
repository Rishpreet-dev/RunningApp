package com.androiddevs.runningappyt.fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.androiddevs.runningappyt.R
import com.androiddevs.runningappyt.db.Run
import com.androiddevs.runningappyt.other.CancelTrackingDialog
import com.androiddevs.runningappyt.other.Constants.ACTION_PAUSE_SERVICE
import com.androiddevs.runningappyt.other.Constants.ACTION_START_OR_RESUME_SERVICE
import com.androiddevs.runningappyt.other.Constants.ACTION_STOP_SERVICE
import com.androiddevs.runningappyt.other.Constants.CAMERA_ZOOM
import com.androiddevs.runningappyt.other.Constants.POLYLINE_COLOR
import com.androiddevs.runningappyt.other.Constants.POLYLINE_WIDTH
import com.androiddevs.runningappyt.other.TrackingUtility
import com.androiddevs.runningappyt.services.Polyline
import com.androiddevs.runningappyt.services.TrackingService
import com.androiddevs.runningappyt.ui.viewmodels.MainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_tracking.*
import java.util.*
import javax.inject.Inject
import kotlin.math.round

const val CANCEL_RUN_TAG="CancelDialog"

@AndroidEntryPoint
class TrackingFragment:Fragment(R.layout.fragment_tracking) {

    private val viewModel:MainViewModel by viewModels()

    private var map:GoogleMap?=null

    var isTracking=false
    var pathPoints= mutableListOf<Polyline>()
    private var currentTimeInMillis=0L

    @set:Inject
    var weight=75f

    private  var menu:Menu?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)

        btnFinishRun.setOnClickListener {
            zoomOutToSeeWholeTrack()
            endRunAndSaveToDb()
        }
        //fixing the bug for screen rotation Cancel Dialog Not Appearing
        if(savedInstanceState!=null){
            val cancelTrackingDialog=parentFragmentManager.findFragmentByTag(CANCEL_RUN_TAG) as CancelTrackingDialog?

            cancelTrackingDialog?.setUpListener {
                stopRun()
            }
        }

        btnToggleRun.setOnClickListener {
            toggleRun()
        }

        //when fragment gets destroyed/oriented
        mapView.getMapAsync{
            map=it
            addAllPolyLines()
        }

        subscribeToObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)

    }

    private fun subscribeToObservers(){
        TrackingService.isTracking.observe(viewLifecycleOwner, Observer {
            updateTracking(it)
        })

        TrackingService.pathPoints.observe(viewLifecycleOwner, Observer {
            pathPoints=it
            addLatestPolyline()
            moveCamera()
        })

        TrackingService.timeRunInMillis.observe(viewLifecycleOwner, Observer {
            currentTimeInMillis=it
            val formattedTime=TrackingUtility.getFormattedStopwatchTime(currentTimeInMillis,true)
            tvTimer.text=formattedTime
        })
    }

    private fun zoomOutToSeeWholeTrack(){
        val bounds=LatLngBounds.Builder()
        for(polyline in pathPoints){
            for(pos in polyline){
                bounds.include(pos)
            }
        }

        map?.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds.build(),
                mapView.width,
                mapView.height,
                ( mapView.height*0.05f).toInt()
            )
        )
    }

    private fun endRunAndSaveToDb(){

        map?.snapshot {

            var distanceInMetres=0f
            for(polyline in pathPoints){

                //poore pathpoints ke andar saare polylines ke beech ka distance
                distanceInMetres+=TrackingUtility.calculatePolyLineLength(polyline).toInt()
            }
            val distanceInMeters= distanceInMetres.toInt()

            val avgSpeed= round((distanceInMetres/1000f) /(currentTimeInMillis/1000f/60/60)*10)/10f
            val dateTimeStamp=Calendar.getInstance().timeInMillis
            val caloriesBurnt=((distanceInMetres/1000f)*weight).toInt()
            val run= Run(it,dateTimeStamp,avgSpeed,distanceInMeters,caloriesBurnt,currentTimeInMillis)
            viewModel.insertRun(run)

            Snackbar.make(
                requireActivity().findViewById(R.id.rootView),
                "Run Saved Successfully!",
                Snackbar.LENGTH_LONG
            ).show()

            stopRun()
        }
    }


    private fun toggleRun(){
        if(isTracking){
            menu?.getItem(0)?.isVisible=true
            sendCommandToService(ACTION_PAUSE_SERVICE)
        }else{
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
        }
    }

    private fun updateTracking(isTracking:Boolean){
        this.isTracking=isTracking
        if(!isTracking && currentTimeInMillis>0L){//not tracking
            //fixing the bug of finish ru  button reappearing sometimes
            btnToggleRun.text="START"
            btnFinishRun.visibility=View.VISIBLE
        }else if(isTracking){
            btnToggleRun.text="STOP"
            menu?.getItem(0)?.isVisible=true
            btnFinishRun.visibility=View.GONE
        }
    }

    private  fun moveCamera(){
        if(pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()){
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    CAMERA_ZOOM
                )
            )
        }//fixing a bug here where pathpoints is empty
        /*else{
            Snackbar.make(requireView(),"C'mon you can't run that fast!!",Snackbar.LENGTH_LONG).show()
        }*/
    }

    //fun to connect two last polyline
    private fun addLatestPolyline(){
        if(pathPoints.isNotEmpty() && pathPoints.last().size>1){

            val preLastLatLng=pathPoints.last()[pathPoints.last().size-2]
            val lastLatLng=pathPoints.last().last()

            val polylineOptions=PolylineOptions().apply {
                color(POLYLINE_COLOR)
                width(POLYLINE_WIDTH)
                add(preLastLatLng)
                add(lastLatLng)
            }
            map?.addPolyline(polylineOptions)
        }

    }

    private fun addAllPolyLines(){
        for(polyline in pathPoints){
            val polylineOptions=PolylineOptions().apply {
                color(POLYLINE_COLOR)
                width(POLYLINE_WIDTH)
                addAll(polyline)
            }
            map?.addPolyline(polylineOptions)
        }
    }

    private fun sendCommandToService(action:String)=
        Intent(requireContext(),TrackingService::class.java).also {
            it.action=action
            requireContext().startService(it)
        }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.tracking_toolbar_menu,menu)
        this.menu=menu
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        if (currentTimeInMillis>0L){
            this.menu?.getItem(0)?.isVisible=true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.menuCancelRun->showCancelRunDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showCancelRunDialog(){
        CancelTrackingDialog().apply {
            setUpListener {
                stopRun()
            }
        }.show(parentFragmentManager,CANCEL_RUN_TAG)
    }

    private fun stopRun(){

        tvTimer.text="00:00:00:00"
        sendCommandToService(ACTION_STOP_SERVICE)
        findNavController().navigate(R.id.action_trackingFragment_to_runFragment)
    }
}