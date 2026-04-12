# 🧺 Laundry Hub — App de Gestión de Lavandería

Aplicación móvil Android para la gestión de una lavandería compartida. Permite a usuarios reservar lavadoras en tiempo real y a administradores gestionar máquinas, mantenimiento y notificaciones.

---

## 🏗️ Arquitectura

El proyecto implementa **Clean Architecture** con el patrón **MVVM**, organizado por features:

```
app/
├── core/
│   ├── database/          # Room — entidades, DAOs, AppDatabase
│   ├── di/                # Módulos Hilt (Network, Hardware, Firebase)
│   ├── hardware/          # Vibrador, Flash, Audio (NotificationAlerter)
│   ├── navigation/        # NavigationWrapper, FeatureNavGraph
│   ├── network/           # Retrofit, OkHttp, CookieJar, WebSocketManager, FCMService
│   ├── service/           # WebSocketForegroundService
│   └── ui/theme/          # Material Theme 3, colores, tipografía
│
└── features/
    ├── auth/              # Login, Registro, Google Sign-In
    ├── machines/          # CRUD de lavadoras (admin)
    ├── laundry_reservation/  # Reservas de lavadoras (usuario)
    ├── maintenance/       # Registros de mantenimiento (admin)
    ├── notifications/     # Centro de notificaciones
    └── shared/            # Componentes compartidos entre features
```

Cada feature sigue la estructura:
```
feature/
├── data/
│   ├── datasources/remote/   # DTOs, mappers, datasources
│   └── repositories/         # Implementaciones de repositorios
├── di/                        # Módulo Hilt de la feature
├── domain/
│   ├── entities/              # Modelos de dominio puros
│   ├── repositories/          # Interfaces de repositorio
│   └── usecases/              # Casos de uso (operator fun invoke)
└── presentation/
    ├── components/            # Composables reutilizables
    ├── navigation/            # NavGraph de la feature
    ├── screens/               # Route (lógica) + Screen (UI pura)
    ├── states/                # Sealed classes de estado UI
    └── viewmodels/            # ViewModels con StateFlow
```

---

## ✅ Requerimientos técnicos implementados

| # | Requerimiento | Estado |
|---|---|---|
| 1 | Patrón MVVM | ✅ Completo |
| 2 | Arquitectura limpia | ✅ Completo |
| 3 | Composables reutilizables | ✅ Completo |
| 4 | Gestión de estados con ViewModel y StateFlow | ✅ Completo |
| 5 | Material Theme 3.0 | ✅ Completo |
| 6 | Inyección de dependencias con Hilt | ✅ Completo |
| 7 | Uso de hardware (≥ 3 tipos) | ✅ Completo |
| 8 | Room con estrategias | ✅ Completo |
| 9 | ForegroundService | ✅ Completo |
| 10 | Push Notifications con Firebase (FCM) | ✅ Completo |
| 11 | Comunicación en tiempo real (WebSocket) | ✅ Completo |

---

## 🔧 Stack tecnológico

| Categoría | Tecnología |
|---|---|
| Lenguaje | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Arquitectura | MVVM + Clean Architecture |
| Inyección de dependencias | Hilt |
| Navegación | Navigation Compose (type-safe) |
| Red | Retrofit 2 + OkHttp |
| Tiempo real | WebSocket (OkHttp) |
| Base de datos local | Room |
| Push Notifications | Firebase Cloud Messaging (FCM) |
| Autenticación | JWT (cookies HttpOnly) + Google Sign-In |
| Serialización | Gson |
| Coroutines | Kotlin Coroutines + Flow |

---

## 🛠️ Hardware utilizado

El sistema de alertas (`AndroidNotificationAlerter`) activa simultáneamente tres tipos de hardware al recibir una notificación:

- **Vibrador** — patrón de vibración con `VibrationEffect.createWaveform()`, con soporte para API 26+ y fallback para versiones anteriores
- **Flash / Cámara** — parpadeo 3 veces usando `CameraManager.setTorchMode()`
- **Audio** — sonido de notificación del sistema con `RingtoneManager`

---

## 📦 Módulo de Notificaciones

El proyecto utiliza un sistema de notificaciones en tres capas:

1. **FCM (Firebase)** — push notifications cuando la app está en background, con canales diferenciados para admin y usuario
2. **WebSocket** — notificaciones en tiempo real mientras la app está activa, emitidas como `SharedFlow` a todos los ViewModels suscritos
3. **Hardware** — alerta multimodal local (vibración + flash + sonido) al recibir un evento

---

## 🗄️ Estrategia de Room

`MaintenanceRepositoryImpl` implementa la estrategia **Cache-Aside con fallback offline**:

```
getAll()
  ├── Remoto OK  → actualiza Room → retorna datos frescos
  └── Remoto FAIL → lee Room
        ├── Room tiene datos → retorna caché (sin error en UI)
        └── Room vacío → propaga error original
```

---

## 🔄 ForegroundService

`WebSocketForegroundService` mantiene la conexión WebSocket activa cuando el usuario sale de la app:

- Se inicia al hacer login (`ACTION_START`)
- Muestra una notificación persistente y silenciosa
- Usa `START_STICKY` para que el sistema lo reinicie si lo mata
- Se detiene al hacer logout (`ACTION_STOP`)
- Tipo: `foregroundServiceType="dataSync"`

---

## 🚀 Configuración del proyecto

### Prerrequisitos

- Android Studio Hedgehog o superior
- JDK 17
- Cuenta de Firebase con FCM habilitado

### Variables de entorno

Crea o edita el archivo `local.properties` en la raíz del proyecto:

```properties
BASE_URL="https://tu-api.com/"
WS_URL="wss://tu-api.com/ws"
GOOGLE_WEB_CLIENT_ID="tu-client-id.apps.googleusercontent.com"
```

### Firebase

1. Descarga el archivo `google-services.json` desde la consola de Firebase
2. Colócalo en el directorio `app/`

### Instalación

```bash
git clone https://github.com/Jaredtorr/Laundry-Hub.git
cd Laundry-Hub
./gradlew assembleDebug
```

---

## 👥 Roles de usuario

| Rol | Acceso |
|---|---|
| **ADMIN** | Panel de lavadoras (CRUD), panel de mantenimiento, notificaciones |
| **USER** | Ver lavadoras disponibles, crear/cancelar/completar reservas, reportar fallas |

---

## 📡 API

El backend está desplegado en Render. Los endpoints principales son:

| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/auth/login` | Inicio de sesión |
| POST | `/auth/register` | Registro de usuario |
| GET | `/machines` | Listar lavadoras |
| POST | `/reservations` | Crear reservación |
| GET | `/reservations/my` | Mis reservaciones |
| POST | `/maintenance` | Reportar mantenimiento |
| GET | `/notifications/my` | Mis notificaciones |

La autenticación se maneja con cookies HttpOnly (`access_token` + `refresh_token`) gestionadas automáticamente por `CookieJarManager`.
