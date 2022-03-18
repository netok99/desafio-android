package br.com.mercadobitcoin

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ProcessLifecycleOwner
import br.com.mercadobitcoin.address.di.addressModule
import br.com.mercadobitcoin.asset.presentation.di.assetModule
import br.com.mercadobitcoin.cashout.di.cashOutModule
import br.com.mercadobitcoin.core.di.componentsModule
import br.com.mercadobitcoin.core.di.coreModule
import br.com.mercadobitcoin.core.utils.SciChartUtils
import br.com.mercadobitcoin.dashboard.di.dashboardModule
import br.com.mercadobitcoin.data.di.dataModule
import br.com.mercadobitcoin.deeplink.di.deeplinkCoreModule
import br.com.mercadobitcoin.deposit.di.depositModule
import br.com.mercadobitcoin.di.appModule
import br.com.mercadobitcoin.di.createRemoteModule
import br.com.mercadobitcoin.di.fireBaseModule
import br.com.mercadobitcoin.domain.darkmode.IsDarkModeEnabledUseCase
import br.com.mercadobitcoin.domain.di.useCaseModule
import br.com.mercadobitcoin.firebase.remoteconfig.RemoteConfig
import br.com.mercadobitcoin.kyc.di.kycModule
import br.com.mercadobitcoin.local.di.localModule
import br.com.mercadobitcoin.login.di.loginModule
import br.com.mercadobitcoin.permission.di.permissionModule
import br.com.mercadobitcoin.pin.di.pinModule
import br.com.mercadobitcoin.repository.di.repositoryModule
import br.com.mercadobitcoin.services.NotificationBuilder
import br.com.mercadobitcoin.signup.di.signUpModule
import br.com.mercadobitcoin.statement.di.statementModule
import br.com.mercadobitcoin.wallet.pro.di.walletModule
import br.com.mercadobitcoin.welcome.R
import br.com.mercadobitcoin.welcome.di.welcomeModule
import br.com.mercadobitcoin.yield.di.yieldModule
import com.appsflyer.AppsFlyerLib
import com.mercadobitcoin.membergetmember.di.mgmModule
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.heapanalytics.android.Heap
import com.incognia.Incognia
import com.newrelic.agent.android.NewRelic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin

private const val FIFTEEN_MINUTES = 900L

@Suppress("unused")
@ExperimentalStdlibApi
@ExperimentalCoroutinesApi
class App : Application() {

    private val appLifecycleObserver: AppLifecycleObserver by inject()
    private val isDarkModeEnabledUseCase: IsDarkModeEnabledUseCase by inject()
    private val remoteConfig: RemoteConfig by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    repositoryModule,
                    appModule,
                    localModule,
                    fireBaseModule,
                    loginModule,
                    signUpModule,
                    depositModule,
                    coreModule,
                    welcomeModule,
                    pinModule,
                    dashboardModule,
                    cashOutModule,
                    kycModule,
                    useCaseModule,
                    walletModule,
                    statementModule,
                    assetModule,
                    addressModule,
                    yieldModule,
                    deeplinkCoreModule,
                    componentsModule,
                    permissionModule,
                    mgmModule,
                    dataModule
                )
            )
            loadKoinModules(
                listOf(
                    createRemoteModule(
                        isDebug = BuildConfig.DEBUG,
                        versionName = BuildConfig.VERSION_NAME,
                        isPinningEnable = remoteConfig.fetchFeatures().pinning,
                        mobileUrl = BuildConfig.MOBILE_URL,
                        genesisUrl = BuildConfig.GENESIS_URL,
                        authUrl = BuildConfig.AUTH_URL
                    )
                )
            )
        }

        NotificationBuilder.createNotificationChannel(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(appLifecycleObserver)
        initLibs()
    }

    private fun initLibs() {
        initFirebaseRemoteConfig()
        setupSciChart()
        initHeap()
        initNewRelic()
        initAppsFlyer()
        initDarkMode()
        initIcognia()
    }

    private fun initDarkMode() {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkModeEnabledUseCase.invoke()) {
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            } else
                AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    private fun initFirebaseRemoteConfig() =
        FirebaseRemoteConfig.getInstance().apply {
            setDefaultsAsync(R.xml.remote_config_defaults)
            setConfigSettingsAsync(
                FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(FIFTEEN_MINUTES)
                    .build()
            )
            fetchAndActivate().apply {
                addOnCompleteListener { task ->
                    if (task.isCanceled || task.isSuccessful.not())
                        setDefaultsAsync(R.xml.remote_config_defaults)
                }
            }
        }

    private fun setupSciChart() = SciChartUtils.setupSciChartLicense(BuildConfig.SCI_CHART_ENV)

    private fun initHeap() = Heap.init(applicationContext, BuildConfig.HEAP_ENV.toString())

    private fun initNewRelic() =
        NewRelic.withApplicationToken(BuildConfig.NEW_RELIC_ENV).start(this)

    private fun initAppsFlyer() {
        if (!BuildConfig.DEBUG) {
            AppsFlyerLib.getInstance().init(BuildConfig.APPS_FLYER, null, this)
            AppsFlyerLib.getInstance().start(this)
        }
    }

    private fun initIcognia() = Incognia.init(this)
}
