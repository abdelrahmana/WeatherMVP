package com.example.swensonhe.ui.weatherfragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.swensonhe.R
import com.example.swensonhe.data.model.Forecastday
import com.example.swensonhe.data.model.WeatherResponse
import com.example.swensonhe.databinding.WeatherFragmentBinding
import com.example.swensonhe.ui.adaptor.AdaptorPlaces
import com.example.swensonhe.util.*
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


@AndroidEntryPoint
class WeatherFragment : Fragment() {
     val viewModel : WeatherViewModel by viewModels()
    lateinit var binding : WeatherFragmentBinding
    lateinit var placesClient : PlacesClient
    lateinit var adaptorPlaces : AdaptorPlaces
    @Inject
    lateinit var progressDialog : Dialog
    @Inject lateinit var commonUtil: CommonUtil
    val hashMapRequest = HashMap<String,String>()
    val dispossible = CompositeDisposable()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), getString(R.string.google_maps_key))
        }
         placesClient = Places.createClient(requireContext())
        binding = WeatherFragmentBinding.inflate(layoutInflater,container,false)
        adaptorPlaces = AdaptorPlaces(ArrayList(), callBackWhenItemSelected)
        commonUtil.setRecycleView(binding.searchInclude.recyclePredict,adaptorPlaces,LinearLayoutManager.VERTICAL,requireContext(),null,false)
        binding.searchView.setOnClickListener{
            binding.searchInclude.containerSearch.visibility = View.VISIBLE
        }
        binding.searchInclude.backButton.setOnClickListener{
            binding.searchInclude.containerSearch.visibility = View.GONE
            resetSearchList(arrayListOf())

        }
        binding.searchInclude.searchEditText.onChangeTextNew(dispossible,callBackWhenEditTextChanged)
        getForcast(hashMapRequest.also {  it.put(Constant.DAYS,DEFAULTREQUESTS) // default
            it.put(Constant.QUERY_KEY, DEFAULT_CITY) })
        setObserver()
        return binding.root
    }
    val callBackWhenEditTextChanged : (String?,String?)->Unit= {query,error->
        query?.let {
            setInitalizationPlaces(it)
        }
        error?.let {
            Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()
        }
    }
    fun setObserver() {
        viewModel.forecastLiveData.observe(viewLifecycleOwner,Observer {
            progressDialog.dismiss()
            setDataToUi(it)
        })
        viewModel.errorLiveData.observe(viewLifecycleOwner,Observer {
            progressDialog.dismiss()
            Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setDataToUi(it: WeatherResponse?) {
        binding.cityName.text = it?.location?.name?:""
        binding.dateTime.text = commonUtil.getFormattedDateTime((it?.location?.localtime_epoch?:0).toLong(),
            it?.location?.tz_id?:"",DateImplementer("EEEE, dd MMM yyyy"))
        binding.timeText.text = commonUtil.getFormattedDateTime((it?.location?.localtime_epoch?:0).toLong(),
            it?.location?.tz_id?:"",TimeImplementer("hh:mm a"))
        Glide.with(requireActivity())
            .load("https:"+it?.current?.condition?.icon?:"").into(binding.sunImage)
        binding.todayDegreeParent.text = it?.current?.temp_f.toString() +getString(R.string.f)
        binding.valueText.text = it?.current?.condition?.text?:""
        binding.windText.text = getString(R.string.wind_mph,it?.current?.wind_mph.toString())
        binding.humidity.text = getString(R.string.humidity,it?.current?.humidity.toString()) + getString(R.string.percentge)
        setNextThreeDays(it?.forecast?.forecastday,it?.location?.tz_id)
    }

    private fun setNextThreeDays(forecast: List<Forecastday>?,timeZone: String?) {
        fillDay(binding.todayImage,binding.todayDegree,binding.valueToday,forecast?.get(0),timeZone,getString(R.string.today))
        fillDay(binding.tommorowImage,binding.tommorowDegree,binding.valueTommorow,forecast?.get(1),timeZone,getString(R.string.tomorrow))
        fillDay(binding.thirdImage,binding.thirdDegree,binding.thirdValue,forecast?.get(2),timeZone)


    }

    @SuppressLint("SetTextI18n")
    private fun fillDay(todayImage: ImageView, todayDegree: TextView, valueToday: TextView,
                        forecast: Forecastday?,timeZone : String?,defaultDateValue : String? =null) {
        forecast?.let {
            Glide.with(requireActivity())
                .load("https:"+forecast.day?.condition?.icon).into(todayImage)
            todayDegree.text =
                forecast.day?.mintemp_f.toString() + "/" + forecast.day?.maxtemp_f.toString() + getString(
                    R.string.f
                )
            valueToday.text = defaultDateValue?: kotlin.run {
                commonUtil.getFormattedDateTime(
                    (forecast.date_epoch ?: 0).toLong(),
                    timeZone ?: "", DateImplementer("EEEE")
                )
            }
        }
    }

    fun getForcast(hashMap: HashMap<String,String>){
        if (commonUtil.isInternetAvailable(requireContext())) {
            progressDialog.show()
            viewModel.getForecast(hashMap)
        }
        else
            Toast.makeText(requireContext(),getString(R.string.no_internet_connection),Toast.LENGTH_SHORT).show()
    }
    val callBackWhenItemSelected :(String,String)->Unit = {query,days->
        // need to show loading
        resetSearchList(ArrayList<String>())
        binding.searchInclude.containerSearch.visibility = View.GONE
        getForcast(hashMapRequest.also {  it.put(Constant.DAYS,days) // default
            it.put(Constant.QUERY_KEY, query) })
    }

    private fun resetSearchList(arrayList: ArrayList<String>) {
        adaptorPlaces.updateCurrentList(arrayList)
        adaptorPlaces.notifyItemRangeRemoved(0,adaptorPlaces.itemCount)
        binding.searchInclude.searchEditText.setText("")
    }

    fun setInitalizationPlaces(queryTextString : String) {
        val token = AutocompleteSessionToken.newInstance()
        val request: FindAutocompletePredictionsRequest =
            FindAutocompletePredictionsRequest.builder() // Call either setLocationBias() OR setLocationRestriction().
                .setQuery(queryTextString)
                .setSessionToken(token)
                .build()
        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
               val arrayListResult = response.autocompletePredictions.map { it.getPrimaryText(null).toString() }
             if (arrayListResult.isNullOrEmpty())
                 binding.searchInclude.noResultPrediction.visibility = View.VISIBLE
                else
                 binding.searchInclude.noResultPrediction.visibility = View.GONE
                adaptorPlaces.updateCurrentList(arrayListResult as ArrayList<String>)

                /* for (prediction in response.autocompletePredictions) {
                     adaptorPlaces.updateCurrentList()
                     prediction.getPrimaryText(null).toString()

                 }*/
            }.addOnFailureListener { exception: Exception? ->
                if (exception is ApiException) {
                    Toast.makeText(requireContext(),exception.message?:"",Toast.LENGTH_SHORT).show()


                }
            }

    }
companion object {
    val DEFAULTREQUESTS = "3"
    val DEFAULT_CITY ="cairo"
}

}