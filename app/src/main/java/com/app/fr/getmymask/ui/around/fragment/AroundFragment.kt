package com.app.fr.getmymask.ui.around.fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app.fr.getmymask.Constants.Companion.LOCATION_PERMISSION_REQUEST_CODE
import com.app.fr.getmymask.R
import com.app.fr.getmymask.api.models.ResponseMasks
import com.app.fr.getmymask.core.BaseFragment
import com.app.fr.getmymask.custom.OnInfoWindowElemTouchListener
import com.app.fr.getmymask.helpers.extensions.show
import com.app.fr.getmymask.helpers.extensions.toPx
import com.app.fr.getmymask.ui.around.AroundViewModel
import com.app.fr.getmymask.ui.around.adapter.AroundAdapter
import com.app.fr.getmymask.ui.dialogs.BaseDialogFragment
import com.app.fr.getmymask.ui.dialogs.DialogFragmentListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.badge_map.view.*
import kotlinx.android.synthetic.main.fragment_around.*
import kotlinx.android.synthetic.main.map_custom_infowindow.view.*
import org.jetbrains.anko.toast

/**
 * A simple [Fragment] subclass.
 */
class AroundFragment : BaseFragment(), OnMapReadyCallback {

    private lateinit var aroundViewModel: AroundViewModel

    private lateinit var googleMap: GoogleMap
    lateinit var builder: LatLngBounds.Builder
    var cu: CameraUpdate? = null


    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var lastLocation: Location

    var test: Boolean = false

    lateinit var aroundAdapter: AroundAdapter
    private var infoButton: Button? = null
    private var infoWindow: ViewGroup? = null

    private var infoButtonListener: OnInfoWindowElemTouchListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        aroundViewModel = ViewModelProviders.of(activity!!).get(AroundViewModel::class.java)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_around, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        try {
            MapsInitializer.initialize(activity!!.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mapView.getMapAsync(this)

        aroundAdapter = AroundAdapter(ArrayList(), context!!)

        rv_around.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            context!!,
            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
            false
        )
        rv_around.adapter = aroundAdapter
        aroundViewModel.onMasksCreate().observe(viewLifecycleOwner, Observer { response ->

                activity.hideProgressDialog()

           response.let {
              if (response.quantity ==1){
                  activity.toast("vous venez d'ajouter ${response.quantity} masque")
              }else{
                  activity.toast("vous venez d'ajouter ${response.quantity} masques")
              }
           }


        })
        aroundViewModel.onMasksFound().observe(viewLifecycleOwner, Observer { response ->
            activity.hideProgressDialog()
            aroundAdapter.setList(response)

            for (i in response.indices) {

                createMarker(response[i].latitude!!, response[i].longitude!!, response[i].quantity.toString())
            }
            onMarkerClick()

            googleMap.setOnMarkerClickListener { marker ->

                for (i in response.indices) {
                    if (marker.position == LatLng(
                            response[i].latitude!!,
                            response[i].longitude!!
                        )
                    ) {
                        marker.tag = response[i]
                    }
                }
                false
            }

        })

        aroundAdapter.setListener(object : AroundAdapter.AroundAdapterListener {
            override fun onShowItineraryPerfomed(latitude: String, longitude: String) {
                val gmmIntentUri = Uri.parse("google.navigation:q=$latitude,$longitude")
                val intent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                context!!.startActivity(intent)
            }


        })
    }

    override fun onMapReady(map: GoogleMap?) {
        System.err.println("OnMapReady start")
        googleMap = map!!

        map_relative_layout.init(googleMap, getPixelsFromDp(context!!, (39 + 20).toFloat()))

        googleMap.setOnMapClickListener {


            val paramLinearLayout = constraint_containt_map.layoutParams
            constraint_containt_map.background = null
            btn_show_list.show()
            paramLinearLayout.height = ViewGroup.LayoutParams.MATCH_PARENT
            constraint_containt_map.layoutParams = paramLinearLayout
            btn_show_list.text = resources.getString(R.string.afficher_la_liste)
            val params = mapView.layoutParams
            params.height = ViewGroup.LayoutParams.MATCH_PARENT
            mapView.layoutParams = params
            test = true

        }

        btn_show_list.setOnClickListener {
            if (btn_show_list.text.toString() == resources.getString(R.string.ajouter_des_masques)) {
                showDialogCreateMask()
            } else {
                constraint_containt_map.background =
                    ContextCompat.getDrawable(context!!, R.drawable.shadow_maps)
                val paramLinearLayout = constraint_containt_map.layoutParams

                paramLinearLayout.height = 310.toPx
                constraint_containt_map.layoutParams = paramLinearLayout
                val params = mapView.layoutParams
                params.height = 300.toPx
                mapView.layoutParams = params
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        lastLocation = location
                        val currentLatLng = LatLng(location.latitude, location.longitude)
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng))
                        googleMap.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                currentLatLng,
                                25f
                            )
                        )
                    }
                }

                test = false
                btn_show_list.text = resources.getString(R.string.ajouter_des_masques)
            }

        }



        googleMap.uiSettings.isZoomControlsEnabled = true
        if (ActivityCompat.checkSelfPermission(
                context!!,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPos()
        } else {

            setUpMap()
        }

    }

    private fun requestPos() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()) {
                    val permissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (permissionGranted) {
                        setUpMap()
                    } else {

                        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                                activity!!,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            )
                        ) {
                            //when click on never ask again
                            activity!!.toast("Vous avez cliquez sur jamais partager ma position !!")
                        } else {
                            requestPos()
                        }
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                context!!,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            googleMap.isMyLocationEnabled = true

            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    lastLocation = location
                    activity.showProgressDialog()
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    aroundViewModel.getMasksByDistance(location.latitude, location.longitude)
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng))
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 25f))
                }
            }
        }
    }

    private fun onMarkerClick() {
        map_relative_layout.init(googleMap, getPixelsFromDp(context!!, (39 + 20).toFloat()))
        this.infoWindow = layoutInflater.inflate(R.layout.map_custom_infowindow, null) as ViewGroup
        this.infoButton = infoWindow!!.findViewById(R.id.btn_blizz_around_store) as Button
        this.infoButtonListener =
            object : OnInfoWindowElemTouchListener(infoButton!!) //btn_default_pressed_holo_light
            {
                override fun onClickConfirmed(v: View, marker: Marker?) {
                    //TODO code more
                }
            }
        this.infoButton!!.setOnTouchListener(infoButtonListener)

        googleMap.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            override fun getInfoWindow(marker: Marker): View? {

                val infoWindowData = marker.tag as ResponseMasks

                marker.title = infoWindowData.id.toString()
                infoWindow!!.tv_name_aroud.text = "Djibril"
                infoWindow!!.tv_adress_store.text = "Argenteuil"
                infoWindow!!.tv_tel_store.text = "06-06-06-06-06"
                infoButtonListener!!.setMarker(marker)
                map_relative_layout.setMarkerWithInfoWindow(marker, infoWindow!!)

                return infoWindow
            }

            override fun getInfoContents(marker: Marker): View? {

                return null
            }
        })
    }

    private fun createMarker(latitude: Double, longitude: Double, numberBlizz: String): Marker {

        val view = layoutInflater.inflate(R.layout.badge_map, null)
        view.fl_container_badge.isDrawingCacheEnabled = true
        view.fl_container_badge.measure(
            View.MeasureSpec.makeMeasureSpec(
                0,
                View.MeasureSpec.UNSPECIFIED
            ), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        view.fl_container_badge.layout(
            0,
            0,
            view.fl_container_badge.measuredWidth,
            view.fl_container_badge.measuredHeight
        )
        view.fl_container_badge.buildDrawingCache(true)
        view.tv_number_mask.text = numberBlizz
        val bitmap = view.fl_container_badge.drawingCache


        return googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(latitude, longitude))
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
        )
    }

    private fun getPixelsFromDp(context: Context, dp: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    private fun showDialogCreateMask() {
        val dialogCreateMask = BaseDialogFragment.createDialog()
        dialogCreateMask.textOkButton = getString(R.string.ok)
        dialogCreateMask.dialogTitle = getString(R.string.ajouter_des_masques)
        dialogCreateMask.setListener(object : DialogFragmentListener {
            override fun onDoneClicked(dialogDoneCancelFragment: DialogFragment,value:String) {
                aroundViewModel.createMask(lastLocation.latitude,lastLocation.longitude,value.toInt())
                activity.toast(lastLocation.latitude.toString() +" "+lastLocation.longitude.toString())
            }

        })
        dialogCreateMask.show(activity, "DialogCreateMask")
    }

}
