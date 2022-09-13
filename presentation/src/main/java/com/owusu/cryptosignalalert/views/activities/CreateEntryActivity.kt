package com.owusu.cryptosignalalert.views.activities

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.owusu.cryptosignalalert.R
import com.owusu.cryptosignalalert.data.datasource.db.PriceTargetDao
import com.owusu.cryptosignalalert.data.models.entity.PriceTargetEntity
import com.owusu.cryptosignalalert.domain.models.PriceTargetDirection
import com.owusu.cryptosignalalert.domain.utils.CryptoDateUtils
import kotlinx.android.synthetic.main.activity_create_entry.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.core.KoinComponent
import java.util.*

class CreateEntryActivity: AppCompatActivity(), KoinComponent {

    private var selectedDirection = PriceTargetDirection.NOT_SET
    private val cryptoDateUtils: CryptoDateUtils by inject()
    private val repoForTesting: PriceTargetDao by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_entry)
        initViews()
        initListeners()
    }


    private fun initViews() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.price_direction_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            price_direction.adapter = adapter
        }
    }

    private fun initListeners() {
        initSpinnerListener()
        initSubmitListener()
    }


    private fun initSpinnerListener() {
        price_direction.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                // An item was selected. You can retrieve the selected item using
                val direction = parent.getItemAtPosition(pos) as String

                selectedDirection = if (direction.equals(
                        PriceTargetDirection.ABOVE.toString(), ignoreCase = true)) {
                    PriceTargetDirection.ABOVE
                } else if (direction.equals(
                        PriceTargetDirection.BELOW.toString(),
                        ignoreCase = true)) {
                    PriceTargetDirection.BELOW
                }else {
                    PriceTargetDirection.NOT_SET
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
                selectedDirection = PriceTargetDirection.NOT_SET
            }
        }
    }

    private fun initSubmitListener() {
        submit_entry.setOnClickListener {
            val coinId = coin_id.text.toString().lowercase().trim()
            val priceTarget = price_target.text.toString().trim()
            if (coinId.isNotEmpty() && priceTarget.isNotEmpty() && selectedDirection != PriceTargetDirection.NOT_SET) {
                submit_entry.isEnabled = false
                createEntry(coinId, priceTarget, selectedDirection)
            }
        }
    }

    private fun createEntry(id: String, priceTarget: String, direction: PriceTargetDirection) {
        lifecycleScope.launch(Dispatchers.IO) {
            val currentTime =
                cryptoDateUtils.convertDateToFormattedStringWithTime(Calendar.getInstance().timeInMillis)
            val list = arrayListOf<PriceTargetEntity>()
            list.add(
                getPriceTarget(
                    id = id,
                    name = id.uppercase(),
                    lastUpdated = currentTime,
                    userPriceTarget = priceTarget.toDouble(),
                    priceTargetDirection = direction
                )
            )
            repoForTesting.insertPriceTargets(list)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@CreateEntryActivity, id + " Created", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun getPriceTarget(
        id: String,
        name: String,
        lastUpdated: String,
        userPriceTarget: Double,
        priceTargetDirection: PriceTargetDirection
    ): PriceTargetEntity {
        return PriceTargetEntity(
            localPrimeId = 0,
            id = id,
            symbol = "",
            name = name,
            image = null,
            currentPrice = 0.0,
            marketCap = 411096596530.0,
            marketCapRank = 1.0,
            fullyDilutedValuation = 452245724314L,
            totalVolume = 48963778515.0,
            high24h = 22007.0,
            low24h = 21251.0,
            priceChange24h = -16.19950226412402,
            priceChangePercentage24h = -0.07523,
            marketCapChange24h = -2334324524.703125,
            marketCapChangePercentage24h = -0.56462,
            circulatingSupply = 19089243.0,
            totalSupply = 21000000.0,
            maxSupply = 21000000.0,
            ath = 69045.0,
            athChangePercentage = -68.88983,
            athDate = "2021-11-10T14:24:11.849Z",
            atl = 67.81,
            atlChangePercentage = 31577.13346,
            atlDate = "2013-07-06T00:00:00.000Z",
            lastUpdated = lastUpdated,
            userPriceTarget = userPriceTarget,
            hasPriceTargetBeenHit = false,
            hasUserBeenAlerted = false,
            priceTargetDirection = priceTargetDirection
        )
    }
}
