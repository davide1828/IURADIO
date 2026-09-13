# 📻 IU Digital Radio - Aplicación Móvil Android

Desarrollo integral e individual de la aplicación móvil **IU Digital Radio** para la **Institución Universitaria Digital de Antioquia (IUDigital)**. 

La aplicación fue maquetada **100% en Kotlin con Jetpack Compose declarativo**, siguiendo las mejores prácticas de la arquitectura moderna de Android recomendadas por Google, integrando reproducción de audio streaming en tiempo real, captura de foto de perfil con permisos dinámicos y retroalimentación háptica.

---

## 🎯 Características y Requerimientos Técnicos

### 1. Maquetación UI Declarativa (100% Jetpack Compose)
- Estructura principal construida con `Scaffold`, `Column` y `Row`.
- Diseño moderno (*"IU Digital Branding"*) con soporte para tarjetas con estilo glassmorphic, bordes suavizados, badges informativos y paleta de colores personalizada (`Color.kt`, `Theme.kt`).

### 2. Cabecera de Perfil y Captura de Cámara en Tiempo Real
- Componente `ProfileHeader`: Foto de perfil circular con icono de cámara interactivo.
- Integración de `FileProvider` (`co.edu.iudigital.radio.fileprovider`) y `ActivityResultContracts.TakePicture()` (con fallback a `TakePicturePreview()`).
- Solicitud de permisos en tiempo de ejecución (`Manifest.permission.CAMERA`) utilizando `ActivityResultContracts.RequestPermission()`.
- Carga dinámica del avatar con `coil.compose.AsyncImage` (`ContentScale.Crop`) que actualiza la foto de perfil en pantalla inmediatamente tras la captura.

### 3. Reproductor Multimedia Principal & Retroalimentación Háptica
- Componente `PlayerCard`: Visualización de la emisora activa, carátula, título, género y controles de acción (**Play**, **Pause**, **Mute/Unmute**).
- Motor de reproducción multimedia con **`androidx.media3:media3-exoplayer`** configurado con `DefaultHttpDataSource.Factory`, cabeceras `User-Agent` nativas y redirecciones entre protocolos habilitadas (`setAllowCrossProtocolRedirects(true)`).
- Configuración de `AudioAttributes` (`C.USAGE_MEDIA`, `C.AUDIO_CONTENT_TYPE_MUSIC`) con gestión de foco de audio y reconexión ante pérdidas de señal.
- Retroalimentación háptica (`HapticHelper`): Integración con `Vibrator` / `VibratorManager` para emitir una vibración táctil corta al presionar cualquier botón de control.

### 4. Catálogo Dinámico de Emisoras
- Componente `StationList`: Lista de desplazamiento vertical optimizada con `LazyColumn`.
- Catálogo con 6 emisoras reales HTTPS de alta disponibilidad probadas en tiempo real (`audio/mpeg`):
  1. **IU Digital Stereo** (`Chillout & Ambient • 101.5 FM`)
  2. **Dance Wave Radio** (`EDM & Club Dance`)
  3. **KEXP 90.3 FM** (`Rock & Alternativo Independiente`)
  4. **SomaFM DEF CON** (`Electro & Synthwave`)
  5. **France Inter** (`Noticias & Cultura Global`)
  6. **NTS Radio** (`Variedad & Pop Alternativo`)
- Selección interactiva: Al hacer clic sobre cualquier emisora del catálogo, se actualiza inmediatamente la carátula y el título del reproductor central e inicia la transmisión en vivo.

### 5. Gestión de Estado y Preservación ante Giros de Pantalla
- Uso de `rememberSaveable` con `mutableStateOf` para mantener reactivos el ID de la emisora seleccionada, el estado de reproducción (`isPlaying`), el estado de silencio (`isMuted`) y la ruta de la foto de perfil capturada (`profilePhotoUriString`), preservando todo el estado ante giros de pantalla.

---

## 🛠️ Estructura del Proyecto

```
IUDigitalRadio/
├── app/
│   ├── build.gradle.kts
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml
│           ├── java/co/edu/iudigital/radio/
│           │   ├── MainActivity.kt
│           │   ├── model/
│           │   │   └── RadioStation.kt
│           │   ├── ui/
│           │   │   ├── components/
│           │   │   │   ├── PlayerCard.kt
│           │   │   │   ├── ProfileHeader.kt
│           │   │   │   └── StationList.kt
│           │   │   └── theme/
│           │   │       ├── Color.kt
│           │   │       └── Theme.kt
│           │   └── util/
│           │       └── HapticHelper.kt
│           └── res/
│               └── xml/
│                   ├── file_paths.xml
│                   └── network_security_config.xml
├── .gitignore
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

---

## 🚀 Guía de Instalación y Compilación

### Requisitos Previos
- **Android Studio**: Jellyfish, Koala, Ladybug o superior.
- **JDK**: Java Development Kit 17.
- **Android SDK**: API 34 (Android 14) o superior.
- **Dispositivo o Emulador**: Android 7.0 (API 24) en adelante.

### Pasos para Clonar y Compilar

1. **Clonar el Repositorio**:
   ```bash
   git clone https://github.com/davide1828/IURADIO.git
   cd IURADIO
   ```

2. **Abrir en Android Studio**:
   - Selecciona `File -> Open` y abre la carpeta del proyecto `IUDigitalRadio`.
   - Espera a que Gradle realice la sincronización inicial (*Sync Project with Gradle Files*).

3. **Generar el APK de Depuración (`app-debug.apk`)**:
   - **Desde la Terminal**:
     ```bash
     ./gradlew assembleDebug
     ```
     *(En Windows PowerShell: `.\gradlew.bat assembleDebug`)*
   - **Desde la Interfaz de Android Studio**:
     Ve al menú `Build` -> `Build Bundle(s) / APK(s)` -> `Build APK(s)`.

4. **Ubicación del APK generado**:
   ```
   app/build/outputs/apk/debug/app-debug.apk
   ```

---

## 📱 Guía de Uso y Pruebas en la Aplicación

1. **Captura de Foto de Perfil**:
   - Toca el botón azul circular con el icono de **Cámara 📷** en la cabecera.
   - Acepta el permiso nativo de la cámara (`CAMERA`).
   - Toma la foto y presiona la marca de verificación ✓: la imagen se actualizará al instante en tu perfil.

2. **Reproducción de Emisoras**:
   - Presiona el botón central **Play ▶** para iniciar la emisión.
   - Usa los botones **Pause ⏸** para detener el audio o **Mute 🔊/🔇** para silenciar la transmisión.
   - Siente la respuesta táctil (vibración háptica) con cada pulsación.

3. **Cambio de Emisora**:
   - Desplázate por la lista inferior (`LazyColumn`) y selecciona cualquier emisora (*ej. IU Digital Stereo, KEXP 90.3 FM*).
   - El reproductor central se actualizará automáticamente y comenzará la reproducción de la nueva emisora.

---

## 👨‍💻 Autor

- **Institución**: Institución Universitaria Digital de Antioquia (IUDigital)
- **Asignatura**: Desarrollo Móvil
- **Repositorio GitHub**: [https://github.com/davide1828/IURADIO](https://github.com/davide1828/IURADIO)
