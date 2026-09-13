package co.edu.iudigital.radio.model

data class RadioStation(
    val id: String,
    val name: String,
    val frequencyOrGenre: String,
    val streamUrl: String,
    val imageUrl: String,
    val description: String
)

object StationProvider {
    val defaultStations = listOf(
        RadioStation(
            id = "1",
            name = "IU Digital Stereo",
            frequencyOrGenre = "Chillout & ambient • 101.5 FM",
            streamUrl = "https://stream.somafm.com/groovesalad-128-mp3",
            imageUrl = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?auto=format&fit=crop&w=400&q=80",
            description = "Emisora oficial de la Institución Universitaria Digital de Antioquia con ambiente de estudio y electrónica chill."
        ),
        RadioStation(
            id = "2",
            name = "Dance Wave Radio",
            frequencyOrGenre = "EDM & Club Dance",
            streamUrl = "https://dancewave.online/dance.mp3",
            imageUrl = "https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f?auto=format&fit=crop&w=400&q=80",
            description = "Los grandes éxitos de la música electrónica y dance internacional 24 horas sin cortes."
        ),
        RadioStation(
            id = "3",
            name = "KEXP 90.3 FM",
            frequencyOrGenre = "Rock & Alternativo Independiente",
            streamUrl = "https://kexp-mp3-128.streamguys1.com/kexp128.mp3",
            imageUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?auto=format&fit=crop&w=400&q=80",
            description = "Música independiente, descubrimiento de nuevos artistas y rock alternativo sin comerciales."
        ),
        RadioStation(
            id = "4",
            name = "SomaFM DEF CON",
            frequencyOrGenre = "Electro & Synthwave",
            streamUrl = "https://stream.somafm.com/defcon-128-mp3",
            imageUrl = "https://images.unsplash.com/photo-1511192336575-5a79af67a629?auto=format&fit=crop&w=400&q=80",
            description = "Sonidos cibernéticos, electro, industrial y synthwave directo desde San Francisco."
        ),
        RadioStation(
            id = "5",
            name = "France Inter",
            frequencyOrGenre = "Noticias & Cultura Global",
            streamUrl = "https://direct.franceinter.fr/live/franceinter-midfi.mp3",
            imageUrl = "https://images.unsplash.com/photo-1590602847861-f357a9332bbc?auto=format&fit=crop&w=400&q=80",
            description = "Cultura global, debates, noticias de actualidad y eventos mundiales en vivo."
        ),
        RadioStation(
            id = "6",
            name = "NTS Radio",
            frequencyOrGenre = "Variedad & Pop Alternativo",
            streamUrl = "https://stream-relay-geo.ntslive.net/stream",
            imageUrl = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?auto=format&fit=crop&w=400&q=80",
            description = "Plataforma de radio global independiente con selecciones musicales diversas y programas de autor."
        )
    )
}
