package com.owusu.cryptosignalalert.data.datasource


import android.content.Context
import com.owusu.cryptosignalalert.domain.models.states.PurchasedStateDomain
import com.owusu.cryptosignalalert.domain.repository.AppPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class AppPreferences constructor(val context: Context): AppPreferencesRepository {

    private val myPref = "asdsdsdddddddddsxccxdsdsd"
    private val isShakeTorchEnabled = "asdasdasdadasda"
    private val numberOfShakes = "xppdhajdajhhvadsfsfsdd"
    private val acceleration = "xxxxxxsfdfsdfdfdsfsfsdfsdddddddd"
    private val isOnBoadingScrCompleted = "xpcccchhvadsfsfsdd"
    private val userKey = "urxxxxlsdhsdhjsdhd"
    private val isAdvSettingsPurchased = "bldussdssdsdsdssssss"
    private val autoOffDuration = "zjgllvvcjfzhlln"
    private val isAppFree = "xxxxxxxxxssssadsjksfhksfjkdhfkdsjfh"
    private val isAdsFree = "ccccxxxkjghyyrtrrtrthhrtfrgtdfgdgf"
    private val isPriceTargetLimitFree = "ccccxzzzzxxkjghyyrtrrtrthhrtfrgtdfgdgf"
    private val isVibrate = "vibraedssddsdsddsddsds"
    private val coinIdLastUpdated = "dfdfdgghfghfghgfhhfgdxxxxxzadddddx"
    private val isWorkManagerExecuting = "qqqqqqpppppooooibcbcbjhjhwehbchwbechjkk"
    private val isSirenEnabled = "sirensoundasdasdasdasd"


    private val sharedPreferences = context.getSharedPreferences(myPref, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    private val _purchasedState = MutableStateFlow(PurchasedStateDomain())
    private val purchasedState: Flow<PurchasedStateDomain> = _purchasedState

    fun enableShakeTorch() {
        putBoolean(isShakeTorchEnabled,true)
    }

    fun disableShakeTorch() {
        putBoolean(isShakeTorchEnabled,false)
    }

    fun isShakeTorchEnabled() : Boolean {
        return getBoolean(isShakeTorchEnabled, true)
    }

    fun enableVibrate() {
        putBoolean(isVibrate,true)
    }

    fun disableVibrate() {
        putBoolean(isVibrate,false)
    }

    fun isVibrateEnabled() : Boolean {
        return getBoolean(isVibrate, false)
    }

    fun setNumberOfShakes(shakes: Int) {
        putInt(numberOfShakes, shakes)
    }

    fun advSettingsPurchased() {
        putBoolean(isAdvSettingsPurchased, true)
    }

    fun advSettingsNotPurchased() {
        putBoolean(isAdvSettingsPurchased, false)
    }

    fun isAdvSettingsPurchased(): Boolean {
        return getBoolean(isAdvSettingsPurchased, false)
    }

    fun completeOnBoardingScreens() {
        putBoolean(isOnBoadingScrCompleted,true)
    }

    fun isOnBoadingScrFinished() : Boolean {
        return getBoolean(isOnBoadingScrCompleted, false)
    }

    override fun isSirenEnabled(): Boolean {
        return getBoolean(isSirenEnabled, false)
    }

    override fun allowSirenSound() {
        putBoolean(isSirenEnabled, true)
    }

    override fun disableSirenSound() {
        putBoolean(isSirenEnabled, false)
    }

    override fun isAppFree(): Boolean {
        return getBoolean(isAppFree, false)
    }

    override fun isAdsPurchased(): Boolean {
        return getBoolean(isAdsFree, false)
    }

    override fun isPriceTargetLimitPurchased(): Boolean {
        return getBoolean(isPriceTargetLimitFree, false)
    }

    override fun makePriceTargetLimitFree() {
        val value = true
        putBoolean(isPriceTargetLimitFree, value)
        _purchasedState.value = _purchasedState.value.copy(isPriceTargetLimitPurchased = value)
    }

    override fun makePriceTargetLimitPurchasable() {
        val value = false
        putBoolean(isPriceTargetLimitFree, value)
        _purchasedState.value = _purchasedState.value.copy(isPriceTargetLimitPurchased = value)
    }

    override fun makeAdsFree() {
        val value = true
        putBoolean(isAdsFree, value)
        _purchasedState.value = _purchasedState.value.copy(isAdsPurchased = value)
    }

    override fun makeAdsPurchasable() {
        val value = false
        putBoolean(isAdsFree, value)
        _purchasedState.value = _purchasedState.value.copy(isAdsPurchased = value)
    }

    override fun makeAppFree() {
        val value = true
        putBoolean(isAppFree, value)
        _purchasedState.value = _purchasedState.value.copy(isAppFree = value)
    }

    override fun makeAppPurchasable() {
        val value = false
        putBoolean(isAppFree, value)
        _purchasedState.value = _purchasedState.value.copy(isAppFree = value)
    }

    override fun listenForPurchasedDataState(): Flow<PurchasedStateDomain> {
        return purchasedState
    }


    override fun getLastCoinIdUpdate(): Long {
        return getLong(coinIdLastUpdated, 0)
    }

    override fun setLastCoinIdUpdate(timestamp: Long) {
        putLong(coinIdLastUpdated, timestamp)
    }

    override fun isWorkManagerExecuting(): Boolean {
        return getBoolean(isWorkManagerExecuting, false)
    }

    override fun workManagerIsExecuting() {
        putBoolean(isWorkManagerExecuting, true)
    }

    override fun workManagerHasFinishedExecuting() {
        putBoolean(isWorkManagerExecuting, false)
    }

    @Synchronized private fun putLong(key: String, value:Long) {
        editor.putLong(key,value)
        editor.commit()
    }

    @Synchronized private fun getLong(key: String, default: Long): Long {
        return sharedPreferences.getLong(key, default)
    }

    @Synchronized private fun putInt(key: String, value:Int) {
        editor.putInt(key,value)
        editor.commit()
    }

    @Synchronized private fun getInt(key: String, default: Int): Int {
        return sharedPreferences.getInt(key, default)
    }

    @Synchronized private fun putFloat(key: String, value:Float) {
        editor.putFloat(key,value)
        editor.commit()
    }

    @Synchronized private fun getFloat(key: String, default: Float): Float {
        return sharedPreferences.getFloat(key, default)
    }

    @Synchronized private fun putBoolean(key: String, boolVal: Boolean) {
        editor.putBoolean(key,boolVal)
        editor.commit()
    }

    @Synchronized private fun getBoolean(key: String, default: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, default)
    }

    @Synchronized fun setUser(username: String) {
        return putString(userKey, username)
    }

    @Synchronized fun getUser(): String? {
        return getString(userKey, null)
    }

    @Synchronized  private fun putString(key: String, value:String) {
        editor.putString(key,value)
        editor.commit()
    }

    @Synchronized private fun getString(key: String, default: String?): String? {
        return sharedPreferences.getString(key, default)
    }
}