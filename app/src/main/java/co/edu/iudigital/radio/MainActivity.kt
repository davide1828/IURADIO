package co.edu.iudigital.radio

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import co.edu.iudigital.radio.model.RadioStation
import co.edu.iudigital.radio.model.StationProvider
import co.edu.iudigital.radio.ui.components.PlayerCard
import co.edu.iudigital.radio.ui.components.ProfileHeader
import co.edu.iudigital.radio.ui.components.StationList
import co.edu.iudigital.radio.ui.theme.IUDigitalRadioTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IUDigitalRadioTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current

    // Estado reactivo y persistente frente a rotaciones de pantalla (RF-04)
    var selectedStationId by rememberSaveable { mutableStateOf(StationProvider.defaultStations.first().id) }
    var isPlaying by rememberSaveable { mutableStateOf(false) }
    var isMuted by rememberSaveable { mutableStateOf(false) }
    var profileBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var profilePhotoUriString by rememberSaveable { mutableStateOf<String?>(null) }
    val profileUri = profilePhotoUriString?.let { Uri.parse(it) }

    val stations = StationProvider.defaultStations
    val selectedStation = stations.find { it.id == selectedStationId } ?: stations.first()

    // Inicialización de Media3 ExoPlayer para reproducción multimedia (RF-07)
    val exoPlayer = remember(context) {
        val httpDataSourceFactory = DefaultHttpDataSource.Factory()
            .setAllowCrossProtocolRedirects(true)
            .setConnectTimeoutMs(15000)
            .setReadTimeoutMs(15000)
            .setUserAgent("Mozilla/5.0 (Linux; Android 10; Mobile; rv:109.0) Gecko/109.0 Firefox/119.0")

        val mediaSourceFactory = DefaultMediaSourceFactory(httpDataSourceFactory)

        val audioAttributes = AudioAttributes.Builder()
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .setUsage(C.USAGE_MEDIA)
            .build()

        ExoPlayer.Builder(context)
            .setMediaSourceFactory(mediaSourceFactory)
            .build()
            .apply {
                setAudioAttributes(audioAttributes, true)
                playWhenReady = false
                addListener(object : Player.Listener {
                    override fun onPlayerError(error: PlaybackException) {
                        android.util.Log.e("IUDigitalRadio", "Error de reproducción: ${error.message}", error)
                        prepare()
                    }
                })
            }
    }

    // Liberación segura del reproductor al destruir la vista
    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }

    // Efecto cuando cambia la emisora seleccionada
    LaunchedEffect(selectedStation.streamUrl) {
        val mediaItem = MediaItem.fromUri(Uri.parse(selectedStation.streamUrl))
        exoPlayer.stop()
        exoPlayer.clearMediaItems()
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = isPlaying
    }

    // Efecto al alternar reproducción/pausa
    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            if (exoPlayer.playbackState == Player.STATE_IDLE || exoPlayer.playbackState == Player.STATE_ENDED) {
                exoPlayer.prepare()
            }
        }
        exoPlayer.playWhenReady = isPlaying
    }

    // Efecto al alternar silencio (Mute/Unmute)
    LaunchedEffect(isMuted) {
        exoPlayer.volume = if (isMuted) 0f else 1f
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "IU DIGITAL RADIO",
                        fontWeight = FontWeight.Black,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // 1. Cabecera de Perfil (Sección Superior) (RF-02)
            ProfileHeader(
                profileBitmap = profileBitmap,
                profileUri = profileUri,
                onProfileBitmapCaptured = { newBitmap ->
                    profileBitmap = newBitmap
                },
                onProfileUriCaptured = { newUri ->
                    profilePhotoUriString = newUri.toString()
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Reproductor Principal (Sección Central) (RF-04, RF-05, RF-07)
            PlayerCard(
                station = selectedStation,
                isPlaying = isPlaying,
                isMuted = isMuted,
                onPlayClick = { isPlaying = true },
                onPauseClick = { isPlaying = false },
                onMuteToggleClick = { isMuted = !isMuted }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Catálogo de Emisoras (Sección Inferior) (RF-06)
            StationList(
                stations = stations,
                selectedStation = selectedStation,
                isPlaying = isPlaying,
                onStationSelect = { newStation ->
                    selectedStationId = newStation.id
                    isPlaying = true
                },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
