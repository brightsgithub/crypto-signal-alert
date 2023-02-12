package com.owusu.cryptosignalalert.data.datasource


import android.content.Context

class AppPreferences constructor(val context: Context) {

    private val myPref = "asdsdsdddddddddsxccxdsdsd"
    private val isShakeTorchEnabled = "asdasdasdadasda"
    private val numberOfShakes = "xppdhajdajhhvadsfsfsdd"
    private val acceleration = "xxxxxxsfdfsdfdfdsfsfsdfsdddddddd"
    private val isOnBoadingScrCompleted = "xpcccchhvadsfsfsdd"
    private val userKey = "urxxxxlsdhsdhjsdhd"
    private val isAdvSettingsPurchased = "bldussdssdsdsdssssss"
    private val autoOffDuration = "zjgllvvcjfzhlln"
    private val isAppFree = "xxxxxxxxxssssadsjksfhksfjkdhfkdsjfh"
    private val isVibrate = "vibraedssddsdsddsddsds"
    private val coinIdPopulated = "dfdfdgghfghfghgfhhfgdxxxx"


    private val sharedPreferences = context.getSharedPreferences(myPref, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

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

    fun isAppFree(): Boolean {
        return getBoolean(isAppFree, false)
    }

    fun makeAppFree() {
        putBoolean(isAppFree, true)
    }

    fun makeAppPurchaseable() {
        putBoolean(isAppFree, false)
    }

    fun hasCoinIdsBeenPopulated(): Boolean {
        return getBoolean(coinIdPopulated, false)
    }

    fun coinIdsHaveBeenPopulated() {
        putBoolean(coinIdPopulated, true)
    }

    fun coinIdsHaveNotBeenPopulated() {
        putBoolean(coinIdPopulated, false)
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